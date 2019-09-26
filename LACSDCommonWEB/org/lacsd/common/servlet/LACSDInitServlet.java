package org.lacsd.common.servlet;

/******************************************************************************
//* Copyright (c) 2005 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		LACSDInitServlet.java
//* Revision: 		1.7
//* Author:			DYIP@LACSD.ORG
//* Created On: 	06-10-2003
//* Modified by:	MFEINBERG@LACSD.ORG
//* Modified On:	11-33-2005
//*					
//* Description:	Servlet instance used to initialize LOG4J Service
/******************************************************************************/
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;
import org.lacsd.common.constants.LACSDPrintConstants;
import org.lacsd.common.constants.LACSDWebConstants;
import org.lacsd.common.threads.DBConnRefresherThread;
import org.lacsd.common.threads.PDFCleanerThread;
import org.lacsd.common.util.ApplicationContext;

public class LACSDInitServlet extends HttpServlet {

private static final long serialVersionUID = -2717016866546345936L;
private final String CLASS_NAME = this.getClass().getName();

/**
 * Init Method
 * @param ServletConfig config
 * @return void
*/
public void init(ServletConfig config) throws ServletException {
	super.init(config);
	ApplicationContext env = ApplicationContext.getInstance();	// Singleton
	try {
		//	1)	Initialize Log4J (Read in Log4J Config File)
		String prefix = getServletContext().getRealPath("/");
		String file = getInitParameter(LACSDWebConstants.LOG4J_INIT_FILE);
	    if(file != null) {
	      ConfigurationSource source = new ConfigurationSource(new FileInputStream(prefix+file));
	      Configurator.initialize(null, source);
	    }
	    Logger log = LogManager.getLogger(CLASS_NAME);
	    log.info("LOG4J Initialized");
	    
		
	    //	2)	Getting build number from property file
		getServletContext().setAttribute("BUILD_NO"," ");
		String buildNO = env.get(LACSDWebConstants.APPLICATION_BUILD_NO);
		if (buildNO != null)  {
			getServletContext().setAttribute("BUILD_NO"," | " + buildNO);
		}
		String httpURL = "";
		if (env.get(LACSDWebConstants.OSXURL) != null) {//for all application
			httpURL = env.get(LACSDWebConstants.OSXURL);
			httpURL = httpURL.substring(0, httpURL.indexOf("osx")-1);
			if (httpURL.lastIndexOf(":") != -1 && httpURL.lastIndexOf(":") > 6) {
				httpURL = httpURL.substring(0, httpURL.lastIndexOf(":"));
			}
		}else if (env.get("iServer.url") != null){//for osx
			if (env.get("iServer.url").indexOf("prodreportserver") != -1) {
				httpURL = "https://prodapps.lacsd.org";
			}else {
					httpURL = "https://testapps.lacsd.org";
			}
		}
		if (httpURL.indexOf("prodapps.lacsd.org") == -1  ) {
			httpURL = "https://testapps.lacsd.org";
		}else {
			httpURL = "https://prodapps.lacsd.org";
		}
		env.setHttpServerURL(httpURL);
	}
	catch (Exception e) {
		throw new ServletException(e.fillInStackTrace());
	}
	
//	4)	Start database Connection refresher Thread! - 
	DBConnRefresherThread refresher = new DBConnRefresherThread();
	Thread one = new Thread(refresher);
	one.setPriority(Thread.MIN_PRIORITY);
	one.setName("Database Automatic Connection Refresher");
	one.start();	
	
	//	4)	Start PDF Cleaner Thread! -
	String webRootTempDir = this.getServletContext().getRealPath("/") + LACSDPrintConstants.ADOBE_PDF_WEBDIR;
	PDFCleanerThread pdfCleaner   = new PDFCleanerThread(webRootTempDir);
	Thread two = new Thread(pdfCleaner);
	two.setPriority(Thread.MIN_PRIORITY);
	two.setName("PDF TempDir Cleaner");
	two.start();

}

/**
 * doPost
*/
public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
	// do post
}

/**
 * doGet
*/
public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
	doPost(req,res);
}
}
