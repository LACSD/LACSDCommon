package org.lacsd.common.values;

/******************************************************************************
//* Copyright (c) 2004 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		BatchPrintVO.java
//* Revision: 		1.1
//* Author:			MFEINBERG@LACSD.ORG
//* Created On: 	07-07-2004
//* Modified By:	
//* Modified On:	
//*					
//* Description:	Batch Printing Value Object
/******************************************************************************/

public class BatchPrintVO implements java.io.Serializable {

private static final long serialVersionUID = 7920698928076320098L;

private String uniqueJobPrefix;			//	A prefix prepended to filenames to identify unique print jobs
private String directory;					//	Directory for loader on both ends of wire
private String canonicalFilename;			//	Canonical File Name
private String email;						//	Email Address of requesting user (for failure)

private String printer;					//	Hostname of specific network printer - used in conjunction with boolean flag

private int status;						//	Realtime printjob status
private int totalPagesLoaded;				//	Number of pages loaded into printjob

private byte[] binary;					//	Actual binary file for download

private Object backwardCompatibleListener;	//	Object placement for a JDK 1.3 AND 1.4 Compliant Listener

private boolean coverPageRequired;		//	Required Flag for Cover Page
private boolean useBackupPrinter;	//	Force the use of a specific printer - requires NOT NULL value for printer name

private String pdfTool;                 // Directory for postscript conversion tool ( pdftops )

/**
 * Returns the backwardCompatibleListener.
 * @return Object
 */
public Object getBackwardCompatibleListener() {
	return backwardCompatibleListener;
}

/**
 * Returns the binary.
 * @return byte[]
 */
public byte[] getBinary() {
	return binary;
}

/**
 * Returns the canonicalFilename.
 * @return String
 */
public String getCanonicalFilename() {
	return canonicalFilename;
}

/**
 * Returns the coverPageRequired.
 * @return boolean
 */
public boolean isCoverPageRequired() {
	return coverPageRequired;
}

/**
 * Returns the directory.
 * @return String
 */
public String getDirectory() {
	return directory;
}

/**
 * Returns the email.
 * @return String
 */
public String getEmail() {
	return email;
}

/**
 * Returns the printer.
 * @return String
 */
public String getPrinter() {
	return printer;
}

/**
 * Returns the status.
 * @return int
 */
public int getStatus() {
	return status;
}

/**
 * Returns the totalPagesLoaded.
 * @return int
 */
public int getTotalPagesLoaded() {
	return totalPagesLoaded;
}

/**
 * Returns the uniqueJobPrefix.
 * @return String
 */
public String getUniqueJobPrefix() {
	return uniqueJobPrefix;
}

/**
 * Sets the backwardCompatibleListener.
 * @param backwardCompatibleListener The backwardCompatibleListener to set
 */
public void setBackwardCompatibleListener(Object backwardCompatibleListener) {
	this.backwardCompatibleListener = backwardCompatibleListener;
}

/**
 * Sets the binary.
 * @param binary The binary to set
 */
public void setBinary(byte[] binary) {
	this.binary = binary;
}

/**
 * Sets the canonicalFilename.
 * @param canonicalFilename The canonicalFilename to set
 */
public void setCanonicalFilename(String canonicalFilename) {
	this.canonicalFilename = canonicalFilename;
}

/**
 * Sets the coverPageRequired.
 * @param coverPageRequired The coverPageRequired to set
 */
public void setCoverPageRequired(boolean coverPageRequired) {
	this.coverPageRequired = coverPageRequired;
}

/**
 * Sets the directory.
 * @param directory The directory to set
 */
public void setDirectory(String directory) {
	this.directory = directory;
}

/**
 * Sets the email.
 * @param email The email to set
 */
public void setEmail(String email) {
	this.email = email;
}

/**
 * Sets the printer.
 * @param printer The printer to set
 */
public void setPrinter(String printer) {
	this.printer = printer;
}

/**
 * Sets the status.
 * @param status The status to set
 */
public void setStatus(int status) {
	this.status = status;
}

/**
 * Sets the totalPagesLoaded.
 * @param totalPagesLoaded The totalPagesLoaded to set
 */
public void setTotalPagesLoaded(int totalPagesLoaded) {
	this.totalPagesLoaded = totalPagesLoaded;
}

/**
 * Sets the uniqueJobPrefix.
 * @param uniqueJobPrefix The uniqueJobPrefix to set
 */
public void setUniqueJobPrefix(String uniqueJobPrefix) {
	this.uniqueJobPrefix = uniqueJobPrefix;
}
/**
 * @return Returns the pdfTool.
 */
public String getPdfTool() {
    return pdfTool;
}
/**
 * @param pdfTool The pdfTool to set.
 */
public void setPdfTool(String pdfTool) {
    this.pdfTool = pdfTool;
}

public boolean isUseBackupPrinter() {
	return useBackupPrinter;
}

public void setUseBackupPrinter(boolean useBackupPrinter) {
	this.useBackupPrinter = useBackupPrinter;
}
}