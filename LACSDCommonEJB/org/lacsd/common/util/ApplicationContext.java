package org.lacsd.common.util;

/******************************************************************************
//* Copyright (c) 2003 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		ApplicationContext.java
//* Revision: 		1.0
//* Author:			M Feinberg
//* Created On: 	07-07-2003
//* Modified by:	
//* Modified On:	
//*					
//* Description:	Wrapper for resource-bundle local text properties file
/******************************************************************************/
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ResourceBundle;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.lacsd.common.constants.LACSDWebConstants;
import org.lacsd.common.dao.LACSDGenericDAO;
import org.lacsd.common.exceptions.LACSDException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ApplicationContext {

private static ApplicationContext _INSTANCE = new ApplicationContext();	// singleton

public static final String PROVIDER_URL 				= "providerUrl";	//	Constant
public static final String WAS_CONTEXT_FACTORY 			= "com.ibm.websphere.naming.WsnInitialContextFactory"; // note: not subject to change frequently enough to merit properties file
public static final String TOMCAT_CONTEXT_FACTORY 		= "com.sun.jndi.cosnaming.CNCtxFactory"; // note: not subject to change frequently enough to merit properties file
public static final String CONFIG_FILE_TOP_LEVEL 		= "application"; 	// top level of application config XML file

private String initialContextFactory;

private static final String APPLICATION_BUNDLE 			= "resources.application_resources";	//	text file
private HashMap<String, String> rb;										//	RB for Application's Primary Config File (server administrator)
private ResourceBundle applicationRb;					//	Alternate RB for variables maintained inside JAR file
private String httpServerURL;

/**
 * Returns singleton instance
 * @return ApplicationContext
*/
public static ApplicationContext getInstance() {
	return _INSTANCE;
}

/**
 * private Constructor initializes resource bundle
*/
private ApplicationContext() {
	super();
	this.applicationRb = ResourceBundle.getBundle(APPLICATION_BUNDLE);
	this.initialContextFactory = WAS_CONTEXT_FACTORY;	// default
		
	//	New! 11-30-05 (MF) - Get location of application config file "app.cfg" from System.properties
	try {
		String systemPropKey = applicationRb.getString(LACSDWebConstants.APPLICATION_CFG_FILE_SYS);	
		String bundleLocation =  System.getProperty(systemPropKey);
		if (systemPropKey.equals(LACSDWebConstants.WATSMAIN_CONFIG)) {
			bundleLocation =  applicationRb.getString(LACSDWebConstants.WATSMAIN_CONFIG);
		}
		if (bundleLocation != null) {
			// port xml file into HashMap
			this.rb = loadConfigXMLFile(bundleLocation);
		}
		else {
			throw new Exception("System Property [" + systemPropKey + "] Not Setup!  Please Inform Server Administrator to set a location for the application config file");
		}
		

	}
	catch (Exception e) {
		System.err.println("System cannot load the application's properties file! - TERMINATING JVM - " + e.getMessage());
		e.printStackTrace();
		//System.exit(1); // do not terminate JVM when everything is lumped on same server!
	}
}

/**
 * Allow an application to trigger the hot-swap of it's own config file
 * @throws LACSDException
*/
public void hotSwapConfigFile() throws LACSDException {

	try {
		String systemPropKey = applicationRb.getString(LACSDWebConstants.APPLICATION_CFG_FILE_SYS);
		String bundleLocation = System.getProperty(systemPropKey);
		if (bundleLocation != null) {
			// port xml file into HashMap
			this.rb = loadConfigXMLFile(bundleLocation);
		}
		else {
			throw new Exception("System Property [" + systemPropKey + "] Not Setup!  Please Inform Server Administrator to set a location for the application config file");
		}
	}
	catch (Exception e) {
		throw new LACSDException(e.getMessage(),e.fillInStackTrace());
	}
}

/**
 * Takes key as parameter and returns its value 
 * @return java.lang.String
 * @param keyStr java.lang.String
*/
public String get(String keyStr) {
	String rval = null;
	try {
		
		// PATCH 11-30-05 (MF) - 
		// Note: environment_resources.properties was abstracted out into a
		// unique configuration file whose location is known by the server
		// administrator and a JNDI lookup.  Specific strings that are not
		// appropriate for the application's primary config file (ie: "rdx.cfg")
		// are stored in a local resource properties bundle "application_resources.properties"
		// in a JAR file, just as the older config file was.  The following 
		// if/else conditions "filter" out properties that are not appropriate
		// for the technical services group or server administrator.
		if (keyStr.equalsIgnoreCase(LACSDWebConstants.APPLICATION_ID)) {
			rval = applicationRb.getString(keyStr);
		}
		else if (keyStr.equalsIgnoreCase(LACSDWebConstants.APPLICATION_BUILD_NO)) {
			rval = applicationRb.getString(keyStr);
		}
		else if (keyStr.equalsIgnoreCase(LACSDWebConstants.SERVER_TYPE)) {
			rval = applicationRb.getString(keyStr);
		}
		else if (keyStr.endsWith("." + LACSDGenericDAO.POLICY)) {
			rval = applicationRb.getString(keyStr);
		}
		else {
			// USE TECHNICAL SERVICE ADMINISTERED "APP.CFG" FILE FOR PARAMS
			rval = (String)rb.get(keyStr);
		}
	}
	catch(java.util.MissingResourceException mre) {
		System.err.println("Cannot get resource from configuration file! - " + mre.getMessage());
		// digest this error - note: log4J will not have been initialized at this point, so the next line won't work!
		//log.warn("Application Context requested value [" + keyStr + "] that does not exist in resource properties file!", mre);
	}
	return rval;
}

/**
 * Returns an Iterator of Keys from the associated
 * Configuration File
 * @return Iterator
*/
public Iterator<String> getKeys() {
	return rb.keySet().iterator();
}

/**
 * @return
 */
public String getInitialContextFactory() {
	return initialContextFactory;
}

/**
 * @param string
 */
public void setInitialContextFactory(String string) {
	initialContextFactory = string;
}

/**
 * Load XML Config File as HashMap
 * @param String filePath
 * @return HashMap
 * @throws LACSDException
*/
private HashMap<String, String> loadConfigXMLFile(String filePath) throws LACSDException {

	HashMap<String, String> variableMap = new HashMap<String, String>();
	try {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder parser = factory.newDocumentBuilder();
		File theFile = new File(filePath);
		if (!theFile.exists()) {
			throw new Exception("Cannot find application config file! [" + filePath + "]");
		}
		Document document = parser.parse(theFile);
		NodeList xmlRoot = document.getElementsByTagName(CONFIG_FILE_TOP_LEVEL);
		for (int i=0; i<xmlRoot.getLength(); i++) {
			Node rootNode = xmlRoot.item(i);
			NodeList attribs = rootNode.getChildNodes();
			for (int j=0; j<attribs.getLength(); j++) {
				Node node = attribs.item(j);
				short nodeType = node.getNodeType();
				// filter XML comment tags out of HashMap!
				if ((nodeType != Node.TEXT_NODE) && (nodeType != Node.COMMENT_NODE)) {
					variableMap.put(node.getNodeName(),node.getFirstChild().getNodeValue());
				}
			}
		}
	}
	catch (Exception e) {
		throw new LACSDException("Could not load config XML file!", e.fillInStackTrace());
	}
	return variableMap;
}

/**
 * Get server domain
 * @return
 */
public String getHttpServerURL() {
	
	return httpServerURL;
}

public void setHttpServerURL(String httpServerURL) {
	this.httpServerURL = httpServerURL;
}

public void addingAttribute(String key, String value) {
	this.rb.put(key, value);
}

}
