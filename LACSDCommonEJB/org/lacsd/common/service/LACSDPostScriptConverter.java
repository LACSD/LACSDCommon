package org.lacsd.common.service;

/******************************************************************************
//* Copyright (c) 2004 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		LACSDPostScriptConverter.java
//* Revision: 		1.0
//* Author:			MFEINBERG@LACSD.ORG
//* Created On: 	06-24-2004
//* Modified by:	
//* Modified On:	
//*					
//* Description:	Convert Adobe PDF Files into Adobe Postscript (PS) Files
/******************************************************************************/

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.lacsd.common.constants.LACSDPrintConstants;
import org.lacsd.common.exceptions.LACSDException;

public class LACSDPostScriptConverter {

private static LACSDPostScriptConverter _INSTANCE = new LACSDPostScriptConverter();		// Singleton Instance

public static final int GREYSCALE 	= 1;
public static final int COLOR 		= 2;

private boolean debug = false;

private static final String TOOL 	= "\\tool\\pdftops";

/**
 * Private Constructor - Singleton Pattern
*/
private LACSDPostScriptConverter() {
	super();
}

/**
 * Return Singleton Instance
 * @return LACSDFileIO
*/
public static LACSDPostScriptConverter getInstance() {
	return _INSTANCE;
}


/**
 * Overload Method with static tool location
 * @param inputPDF
 * @param level
 * @param executableTool
 * @return
 * @throws LACSDException
 */
public String doWritePostscript(File inputPDF, int level) throws LACSDException {
    String tool = new String(System.getProperty("user.dir") + TOOL);
	return this.doWritePostscript(inputPDF,level,tool);
}

/**
 * Attempt to write PDF files as Adobe Postscript PS,
 * which is a Java-Printer friendly format!
 * @param CustomerBillingStatementVO monthlyReportVO,
 * @param MonthlyProcessVO monthlyProcessVO
 * @param ReportVO reportVO
 * @return String
 * @throws LACSDException
*/
public String doWritePostscript(File inputPDF, int level, String executableTool) throws LACSDException {

	if ((inputPDF == null) || (!inputPDF.getName().endsWith(LACSDPrintConstants.ADOBE_PDF))) {
		throw new LACSDException("Cannot convert postscript - Unusable input File [" + inputPDF.toString() + "]");
	}

	String tool = new String("\"" + executableTool + "\"");
	
	String args = " -q ";
	if (level == GREYSCALE) {
		args += "-level1 ";
	}
	else if (level == COLOR) {
		args += "-level2 ";
	}

	// Updated 6/28/2004 - Long directory names with spaces cause Runtime.exec to choke.
	// example: "My Directory".   Working solution is to surround these names with \"quotations\"

	File workingDir = new File(inputPDF.getParent() + "/");
	File psFile = new File(inputPDF.getAbsolutePath().substring(0,(inputPDF.getAbsolutePath().length()-4)) + LACSDPrintConstants.ADOBE_POSTSCRIPT);
	String cmd = "cmd /c " + tool + args + inputPDF.getName() + " " + psFile.getName();

	String stdout = null;
	String errout = null;
	int outcode = 0;
	
	try {
		Runtime rt = Runtime.getRuntime();
		Process p = rt.exec(cmd, null, workingDir);

		stdout = processInput(p.getInputStream());
		errout = processInput(p.getErrorStream());
	
		outcode = p.waitFor();
		
		if (debug) {
			System.out.println(stdout);
			System.err.println(errout);
			System.out.println("outcode: " + outcode);
		}
		
	}
	catch (Exception e) {
		throw new LACSDException(e.getMessage(),e.fillInStackTrace());
	}
	
	if ((outcode != 0) || (errout != null)) {
		throw new LACSDException("Write to postscript failed! - CODE[" + outcode + "] " + errout);		
	}
	
	return psFile.getAbsolutePath();
}

/**
 * Takes an inputstream, and uses BufferedReader.readLine() to
 * take in text line-by-line, then add it all to one big string
*/ 
private String processInput(InputStream is) {
	
	String text = null;

	try {
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		String line = null;
			
		while ((line = br.readLine()) != null) {
			text += new String(line + "\n");
		}
		
	} catch (IOException ioe) {
		ioe.printStackTrace();
	}
	return text;	
}
}
