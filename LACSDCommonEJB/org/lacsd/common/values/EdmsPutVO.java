package org.lacsd.common.values;

/******************************************************************************
//* Copyright (c) 2004 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		EdmsPutVO.java
//* Revision: 		1.0
//* Author:			M Feinberg
//* Created On: 	04-13-2004
//* Modified by:	
//* Modified On:	
//*					
//* Description:	Value Object abstraction inserting a new "Document" record
//* 				into the Hummingbird EDMS via Java-EDMS Bridge Programme
/******************************************************************************/

import java.util.Hashtable;

public class EdmsPutVO implements java.io.Serializable {

private static final long serialVersionUID = 7440147584285865385L;

private String putCYD;								//	Hummingbird VB Form definition by: RSARUT@LACSD.ORG
private Hashtable<String, String> propertyList;					//	Dynamic set of NAME/VALUE pairs as META DATA for insert
private Hashtable<String, EdmsTrusteeVO> trusteeList;						//	Dynamic set of NAME/VALUE pairs as Security ACL for Trustees
private EdmsAuthenticateVO edmsAuthenticateVO;		//	login security credentials

private String docnumber;							//	Set by insert of meta data
private String versionid;							//	Set by insert of meta data

private byte[] theFile;							//	Binary Data File for insert


/**
 * Returns the docnumber.
 * @return String
 */
public String getDocnumber() {
	return docnumber;
}

/**
 * Returns the edmsAuthenticateVO.
 * @return EdmsAuthenticateVO
 */
public EdmsAuthenticateVO getEdmsAuthenticateVO() {
	return edmsAuthenticateVO;
}

/**
 * Returns the propertyList.
 * @return Hashtable
 */
public Hashtable<String, String> getPropertyList() {
	return propertyList;
}

/**
 * Returns the putCYD.
 * @return String
 */
public String getPutCYD() {
	return putCYD;
}

/**
 * Returns the theFile.
 * @return byte[]
 */
public byte[] getTheFile() {
	return theFile;
}

/**
 * Returns the trusteeList.
 * @return Hashtable
 */
public Hashtable<String, EdmsTrusteeVO> getTrusteeList() {
	return trusteeList;
}

/**
 * Returns the versionid.
 * @return String
 */
public String getVersionid() {
	return versionid;
}

/**
 * Sets the docnumber.
 * @param docnumber The docnumber to set
 */
public void setDocnumber(String docnumber) {
	this.docnumber = docnumber;
}

/**
 * Sets the edmsAuthenticateVO.
 * @param edmsAuthenticateVO The edmsAuthenticateVO to set
 */
public void setEdmsAuthenticateVO(EdmsAuthenticateVO edmsAuthenticateVO) {
	this.edmsAuthenticateVO = edmsAuthenticateVO;
}

/**
 * Sets the propertyList.
 * @param propertyList The propertyList to set
 */
public void setPropertyList(Hashtable<String, String> propertyList) {
	this.propertyList = propertyList;
}

/**
 * Sets the putCYD.
 * @param putCYD The putCYD to set
 */
public void setPutCYD(String putCYD) {
	this.putCYD = putCYD;
}

/**
 * Sets the theFile.
 * @param theFile The theFile to set
 */
public void setTheFile(byte[] theFile) {
	this.theFile = theFile;
}

/**
 * Sets the trusteeList.
 * @param trusteeList The trusteeList to set
 */
public void setTrusteeList(Hashtable<String, EdmsTrusteeVO> trusteeList) {
	this.trusteeList = trusteeList;
}

/**
 * Sets the versionid.
 * @param versionid The versionid to set
 */
public void setVersionid(String versionid) {
	this.versionid = versionid;
}
}
