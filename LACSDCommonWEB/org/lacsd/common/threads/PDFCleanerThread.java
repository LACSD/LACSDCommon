package org.lacsd.common.threads;

/******************************************************************************
//* Copyright (c) 2005 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		PDFCleanerThread.java
//* Revision: 		1.0
//* Author:			MFEINBERG@LACSD.ORG
//* Created On: 	11-14-2005
//* Modified by:	
//* Modified On:	
//*					
//* Description:	Maintenence thread for cleaning up temporary PDF directory
/******************************************************************************/
import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.lacsd.common.constants.LACSDPrintConstants;

public class PDFCleanerThread implements Runnable {

private final String CLASS_NAME = this.getClass().getName();
protected Logger log = LogManager.getLogger(CLASS_NAME);

private static final int CLEANUP_INTERVAL_MINUTES 	= 720;			// thread activation every 12 hours
private static final int EXPIRE_DAYS 				= 2;			// number of days before pdf cleanup

private String pdfDir;

/**
 * Default Constructor
*/
public PDFCleanerThread(String webRoot_plus_PDFTempDirectory_concatenation) {
	super();
	this.pdfDir = webRoot_plus_PDFTempDirectory_concatenation;
}

/**
 * MAINTENENCE THREAD - RUN CLEANUP PROCESS
*/
public void run() {
	
	log.debug("INITIALIZING PDF Cleaner Thread");
	
	while (true) {
		try {Thread.sleep(60000 * CLEANUP_INTERVAL_MINUTES);}catch(InterruptedException ioe) {}
		log.debug("PDF/ZIP Cleaner Thread - Scheduled Maintence");
		cleanDirectory();
	}
}

/**
 * Clean Directory - Attempts to clean directory every NN Seconds
*/
private void cleanDirectory() {
	
    File directory = new File(this.pdfDir);
    if (directory.isDirectory()) {
	    File[] allFiles = directory.listFiles();
	    for (int i = 0; i < allFiles.length; i++) {
	        String fileName = allFiles[i].getName().toLowerCase();
	        if ( fileName.equalsIgnoreCase("RDXTest.pdf") )
	        	continue;
	        if ( fileName.endsWith(LACSDPrintConstants.ADOBE_PDF) || 
	             fileName.endsWith(LACSDPrintConstants.ZIP_EXT) || 
	             fileName.endsWith(LACSDPrintConstants.METADATA_EXT ) || 
	             fileName.endsWith(LACSDPrintConstants.CSV_EXT ) ||
	             fileName.endsWith(LACSDPrintConstants.MS_EXCEL)) {
	        	long currentTime = System.currentTimeMillis();
	        	long lastModifid = allFiles[i].lastModified();
	        	if (currentTime - lastModifid > (86400000 * EXPIRE_DAYS)) {
	        		log.info("Removing Temporary File: " + allFiles[i].getName());
					allFiles[i].delete();
	        	}
	        }
	        else {
	        	// do nothing
	        }
	    }
    }
}
}
