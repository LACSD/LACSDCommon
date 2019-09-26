package org.lacsd.common.service;

/******************************************************************************
//* Copyright (c) 2004 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		LACSDFileExtractor.java
//* Revision: 		1.0
//* Author:			MFEINBERG@LACSD.ORG
//* Created On: 	11-19-2004
//* Modified by:	
//* Modified On:	
//*					
//* Description:	Recursively combines files from a subdirectory
/******************************************************************************/
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class LACSDFileExtractor {

private static LACSDFileExtractor _INSTANCE = new LACSDFileExtractor();		// Singleton Instance

/**
 * Private Constructor - Singleton Pattern
*/
private LACSDFileExtractor() {
	super();
}

/**
 * Return Singleton Instance
 * @return LACSDFileIO
*/
public static LACSDFileExtractor getInstance() {
	return _INSTANCE;
}

/**
 * GO!!
 * @param inDir
 * @param outDir
 * @throws Exception
 */
public void extract(File inDir, File outDir) throws Exception	{
	
	File[] files = inDir.listFiles();
	for(int i=0; i<files.length; ++i) {
		if(files[i].isDirectory()) {
			extract(files[i],outDir);
		}
		else {
			copyTo(files[i],outDir);
		}
	}
}

/**
 * Copy Files
 * @param inFile
 * @param outDir
 * @throws Exception
 */
@SuppressWarnings("resource")
private void copyTo(File inFile,File outDir) throws Exception {

	if (!inFile.exists()) {
		throw new Exception("Could not find file " + inFile.getName());
	}
	
	InputStream is = new FileInputStream(inFile);
	long length = inFile.length();


	// Ensure that file is not larger than Integer.MAX_VALUE.
	if (length > Integer.MAX_VALUE) {
		throw new Exception("File to large to read");
	}

	// Create the byte array to hold the data
	byte[] bytes = new byte[(int)length];

	// Read in the bytes
	int offset = 0;
	int numRead = 0;
	while (offset < bytes.length && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
		offset += numRead;
	}

	// Ensure all the bytes have been read in
	if (offset < bytes.length) {
		throw new Exception("Could not completely read file " + inFile.getName());
	}

	// Close the input stream and return bytes
	is.close();
	is = null;
	
	// Write file to new location	
	File outputFile = new File(outDir + "/" + inFile.getName());
	OutputStream out = new FileOutputStream(outputFile);
	out.write(bytes);
	out.close();
}

/**
 * Main Execution Block
 * @param args
 */
public static void main(String[] args) {

	LACSDFileExtractor ex = new LACSDFileExtractor();
	try {

		String in = args[0];
		String out = args[1];
	//	String in = "C:\\temp\\DB2UDB";
	//	String out = "c:\\temp\\out";
		
		if ((in == null) || (out == null)) {
			System.out.println("USAGE: Extractor <inDirectory> <outDirectory>");
			System.exit(1);
		}
		File inDir = new File(in);
		File outDir = new File(out);		
		ex.extract(inDir,outDir);
	}
	catch(Exception e) {
		e.printStackTrace();
		System.exit(1);
	}
	System.exit(0);
}
}
