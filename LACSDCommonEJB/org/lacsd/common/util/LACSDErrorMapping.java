package org.lacsd.common.util;

/******************************************************************************
//* Copyright (c) 2003 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		LACSDErrorMapping.java
//* Revision: 		1.0
//* Author:			M Feinberg
//* Created On: 	11-14-2003
//* Modified By:	
//* Modified On:	
//*					
//* Description:	Value Object containing useful information that
//* 				the controller-tier will use to return errors
//* 				to the proper pages (with the proper messages)
/******************************************************************************/

public class LACSDErrorMapping {

private String errCode;
private String storedProcedureName;
private String forwardPageKey;
private String errorMessageKey;

private boolean forwardFullyQualifiedURI;	// added per reporting req. 06/10/2004


/**
 * Returns the errCode.
 * @return String
 */
public String getErrCode() {
	return errCode;
}

/**
 * Returns the errorMessageKey.
 * @return String
 */
public String getErrorMessageKey() {
	return errorMessageKey;
}

/**
 * Returns the forwardFullyQualifiedURI.
 * @return boolean
 */
public boolean isForwardFullyQualifiedURI() {
	return forwardFullyQualifiedURI;
}

/**
 * Returns the forwardPageKey.
 * @return String
 */
public String getForwardPageKey() {
	return forwardPageKey;
}

/**
 * Returns the storedProcedureName.
 * @return String
 */
public String getStoredProcedureName() {
	return storedProcedureName;
}

/**
 * Sets the errCode.
 * @param errCode The errCode to set
 */
public void setErrCode(String errCode) {
	this.errCode = errCode;
}

/**
 * Sets the errorMessageKey.
 * @param errorMessageKey The errorMessageKey to set
 */
public void setErrorMessageKey(String errorMessageKey) {
	this.errorMessageKey = errorMessageKey;
}

/**
 * Sets the forwardFullyQualifiedURI.
 * @param forwardFullyQualifiedURI The forwardFullyQualifiedURI to set
 */
public void setForwardFullyQualifiedURI(boolean forwardFullyQualifiedURI) {
	this.forwardFullyQualifiedURI = forwardFullyQualifiedURI;
}

/**
 * Sets the forwardPageKey.
 * @param forwardPageKey The forwardPageKey to set
 */
public void setForwardPageKey(String forwardPageKey) {
	this.forwardPageKey = forwardPageKey;
}

/**
 * Sets the storedProcedureName.
 * @param storedProcedureName The storedProcedureName to set
 */
public void setStoredProcedureName(String storedProcedureName) {
	this.storedProcedureName = storedProcedureName;
}

}
