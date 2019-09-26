package org.lacsd.common.values;

/******************************************************************************
//* Copyright (c) 2004 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		CVSVO.java
//* Revision: 		1.1
//* Author:			MFEINBERG@LACSD.ORG
//* Created On: 	08-10-2004
//* Modified By:	
//* Modified On:	
//*					
//* Description:	CVS Value Object - 
//* 				
//* 				For use in conjuntion with WBR Reporting Facility
//*
/******************************************************************************/

public class CVSVO implements java.io.Serializable {

private static final long serialVersionUID = -6582833945732534047L;

private String username;
private String password;
private String hostname;
private String cvsRootDir;

private String fileName;

private String fileDirectory;
private String fileVersion;
private byte[] fileRawData;
private String comment;

/**
 * Returns the comment.
 * @return String
 */
public String getComment() {
	return comment;
}

/**
 * Returns the cvsRootDir.
 * @return String
 */
public String getCvsRootDir() {
	return cvsRootDir;
}

/**
 * Returns the fileDirectory.
 * @return String
 */
public String getFileDirectory() {
	return fileDirectory;
}

/**
 * Returns the fileName.
 * @return String
 */
public String getFileName() {
	return fileName;
}

/**
 * Returns the fileRawData.
 * @return byte[]
 */
public byte[] getFileRawData() {
	return fileRawData;
}

/**
 * Returns the fileVersion.
 * @return String
 */
public String getFileVersion() {
	return fileVersion;
}

/**
 * Returns the hostname.
 * @return String
 */
public String getHostname() {
	return hostname;
}

/**
 * Returns the password.
 * @return String
 */
public String getPassword() {
	return password;
}

/**
 * Returns the username.
 * @return String
 */
public String getUsername() {
	return username;
}

/**
 * Sets the comment.
 * @param comment The comment to set
 */
public void setComment(String comment) {
	this.comment = comment;
}

/**
 * Sets the cvsRootDir.
 * @param cvsRootDir The cvsRootDir to set
 */
public void setCvsRootDir(String cvsRootDir) {
	this.cvsRootDir = cvsRootDir;
}

/**
 * Sets the fileDirectory.
 * @param fileDirectory The fileDirectory to set
 */
public void setFileDirectory(String fileDirectory) {
	this.fileDirectory = fileDirectory;
}

/**
 * Sets the fileName.
 * @param fileName The fileName to set
 */
public void setFileName(String fileName) {
	this.fileName = fileName;
}

/**
 * Sets the fileRawData.
 * @param fileRawData The fileRawData to set
 */
public void setFileRawData(byte[] fileRawData) {
	this.fileRawData = fileRawData;
}

/**
 * Sets the fileVersion.
 * @param fileVersion The fileVersion to set
 */
public void setFileVersion(String fileVersion) {
	this.fileVersion = fileVersion;
}

/**
 * Sets the hostname.
 * @param hostname The hostname to set
 */
public void setHostname(String hostname) {
	this.hostname = hostname;
}

/**
 * Sets the password.
 * @param password The password to set
 */
public void setPassword(String password) {
	this.password = password;
}

/**
 * Sets the username.
 * @param username The username to set
 */
public void setUsername(String username) {
	this.username = username;
}
}
