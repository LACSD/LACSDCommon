package org.lacsd.common.values;

/******************************************************************************
//* Copyright (c) 2004 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		EdmsGetVO.java
//* Revision: 		1.0
//* Author:			M Feinberg
//* Created On: 	04-13-2004
//* Modified by:	
//* Modified On:	
//*					
//* Description:	Value Object abstraction retrieving a single "Document" 
//* 				object (BINARY FORM) from the Java-EDMS Bridge Programme
/******************************************************************************/

public class EdmsGetVO implements java.io.Serializable {

private static final long serialVersionUID = 5805007391255864477L;

private String docnum;			//	from search result
private String versionID;		//	from search result - HIDDEN

private byte[] theFile;		//	the actual binary document

private EdmsAuthenticateVO edmsAuthenticateVO;		//	login security credentials


/**
 * Returns the docnum.
 * @return String
 */
public String getDocnum() {
	return docnum;
}

/**
 * Returns the edmsAuthenticateVO.
 * @return EdmsAuthenticateVO
 */
public EdmsAuthenticateVO getEdmsAuthenticateVO() {
	return edmsAuthenticateVO;
}

/**
 * Returns the theFile.
 * @return byte[]
 */
public byte[] getTheFile() {
	return theFile;
}

/**
 * Returns the versionID.
 * @return String
 */
public String getVersionID() {
	return versionID;
}

/**
 * Sets the docnum.
 * @param docnum The docnum to set
 */
public void setDocnum(String docnum) {
	this.docnum = docnum;
}

/**
 * Sets the edmsAuthenticateVO.
 * @param edmsAuthenticateVO The edmsAuthenticateVO to set
 */
public void setEdmsAuthenticateVO(EdmsAuthenticateVO edmsAuthenticateVO) {
	this.edmsAuthenticateVO = edmsAuthenticateVO;
}

/**
 * Sets the theFile.
 * @param theFile The theFile to set
 */
public void setTheFile(byte[] theFile) {
	this.theFile = theFile;
}

/**
 * Sets the versionID.
 * @param versionID The versionID to set
 */
public void setVersionID(String versionID) {
	this.versionID = versionID;
}
}
