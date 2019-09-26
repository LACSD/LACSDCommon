package org.lacsd.common.service;

/******************************************************************************
//* Copyright (c) 2006 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:       LACSDPrinterInterface.java
//* Revision:       1.0
//* Author:         TNGUYEN@LACSD.ORG
//* Created On:     09-01-2006
//* Modified by:
//* Modified On:
//*	
//* Description:	Printer Utility
/******************************************************************************/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.lacsd.common.exceptions.LACSDException;

public class LACSDPrinterInterface {

private static LACSDPrinterInterface _INSTANCE = new LACSDPrinterInterface();		// Singleton Instance

private boolean debug = false;

/**
 * Private Constructor - Singleton Pattern
*/
private LACSDPrinterInterface() {
	super();
}

/**
 * Return Singleton Instance
 * @return LACSDFileIO
*/
public static LACSDPrinterInterface getInstance() {
	return _INSTANCE;
}


/**
 * Add window network printer
 * @param String printerName ex. \\JA784\IBM70
 * @return void
 * @throws LACSDException
*/
public void addNetworkPrinter( String printerName ) throws LACSDException {

	String cmd = "cmd /c rundll32 printui.dll,PrintUIEntry /q /in /n " + printerName;

	String stdout = null;
	String errout = null;
	int outcode = 0;
	
	try {
		Runtime rt = Runtime.getRuntime();
		Process p = rt.exec(cmd);

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
	
}

/**
 * Print test page to specified printer
 * @param String printerName ex. \\JA784\IBM70
 * @return void
 * @throws LACSDException
*/
public void printTestPage( String printerName ) throws LACSDException {

    String cmd = "cmd /c rundll32 printui.dll,PrintUIEntry /q /k /n " + printerName;

    String stdout = null;
    String errout = null;
    int outcode = 0;
    
    try {
        Runtime rt = Runtime.getRuntime();
        Process p = rt.exec(cmd);

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
