package org.lacsd.common.values;

/******************************************************************************
//* Copyright (c) 2004 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		EdmsAuthenticateVO.java
//* Revision: 		1.0
//* Author:			M Feinberg
//* Created On: 	04-13-2004
//* Modified by:	
//* Modified On:	
//*					
//* Description:	Value Object abstraction representing authentication credentials
//* 				for interacting with the DOCS API
/******************************************************************************/

public class EdmsAuthenticateVO implements java.io.Serializable {

private static final long serialVersionUID = 222357744844637886L;

private String username;			//	login docs username
private String password;			//	login docs password
private String docsUserGroup;		//	login docs user group
private String targetLibrary;		//	login library
private String dst;				//	login credentials (DST)


/**
 * Returns the docsUserGroup.
 * @return String
 */
public String getDocsUserGroup() {
	return docsUserGroup;
}

/**
 * Returns the dst.
 * @return String
 */
public String getDst() {
	return dst;
}

/**
 * Returns the password.
 * @return String
 */
public String getPassword() {
	return password;
}

/**
 * Returns the targetLibrary.
 * @return String
 */
public String getTargetLibrary() {
	return targetLibrary;
}

/**
 * Returns the username.
 * @return String
 */
public String getUsername() {
	return username;
}

/**
 * Sets the docsUserGroup.
 * @param docsUserGroup The docsUserGroup to set
 */
public void setDocsUserGroup(String docsUserGroup) {
	this.docsUserGroup = docsUserGroup;
}

/**
 * Sets the dst.
 * @param dst The dst to set
 */
public void setDst(String dst) {
	this.dst = dst;
}

/**
 * Sets the password.
 * @param password The password to set
 */
public void setPassword(String password) {
	this.password = password;
}

/**
 * Sets the targetLibrary.
 * @param targetLibrary The targetLibrary to set
 */
public void setTargetLibrary(String targetLibrary) {
	this.targetLibrary = targetLibrary;
}

/**
 * Sets the username.
 * @param username The username to set
 */
public void setUsername(String username) {
	this.username = username;
}
}
