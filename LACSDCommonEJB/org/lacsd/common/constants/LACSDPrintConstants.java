package org.lacsd.common.constants;

/******************************************************************************
//* Copyright (c) 2004 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		LACSDPrintConstants.java
//* Revision: 		1.1
//* Author:			MFEINBERG@LACSD.ORG
//* Created On: 	07-07-2004
//* Modified by:	
//* Modified On:	
//*					
//* Description:	Constants class for LACSD Batch Printing Server
/******************************************************************************/

public class LACSDPrintConstants {


public static final String COVER_PAGE 					= "CVR";	//	Prefix for files to be placed on top of batch printjobs
public static final String ADOBE_POSTSCRIPT 			= ".ps"; 	//	only queue-up Adobe Postscript files
public static final String ADOBE_PDF 					= ".pdf"; 	//	only queue-up Adobe Postscript files
public static final String ADOBE_PDF_WEBDIR 			= "/pdf/"; 	//	web-apps will put temporary PDF's in this folder
public static final String MS_EXCEL_WEBDIR 				= "/xls/"; 	//	web-apps will put temporary XLS's in this folder
public static final String METADATA_EXT 				= ".xml"; 	//	serialized meta-data
public static final String ZIP_EXT 						= ".zip"; 	//	zip files
public static final String BAT_EXT 						= ".bat";	//	batch files
public static final String CSV_EXT                      = ".csv";   //  csv files
public static final String MS_EXCEL                     = ".xls";   //  xls files
public static final String SER_EXT						= ".ser";   //  serializable object files

public static final String TOOLS_KEY_PDF2PS 			= "printing.tools.pdf2ps";
public static final String TOOLS_KEY_GHOSTSCRIPT 		= "printing.tools.ghostscript";

public static final int PENDING_START 					= 1;
public static final int IN_PROGRESS 					= 2;
public static final int JOB_COMPLETE 					= 3;
public static final int JOB_NOT_FOUND 					= 4;
public static final int JOB_FAILED_JAVA_EXCEPTION 		= 5;
public static final int FORCE_TERMINATE 				= 6;


public static final int TOTAL_MAX_SECONDS_PER_PRINT 	= 60;		// longer than NN seconds per page will cause failure

}
