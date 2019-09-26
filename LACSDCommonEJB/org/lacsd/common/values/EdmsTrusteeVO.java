package org.lacsd.common.values;

/******************************************************************************
//* Copyright (c) 2004 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		EdmsTrusteeVO.java
//* Revision: 		1.0
//* Author:			M Feinberg
//* Created On: 	04-29-2004
//* Modified by:	
//* Modified On:	
//*					
//* Description:	Value Object abstraction for inserting a "Trustee" into a 
//* 				new document Record
/******************************************************************************/

public class EdmsTrusteeVO implements java.io.Serializable {

private static final long serialVersionUID = -6731972181719197163L;

private String trusteeName;
private int trusteeType;
private int accessSecurity;

/**
 * Returns the accessSecurity.
 * @return int
 */
public int getAccessSecurity() {
	return accessSecurity;
}

/**
 * Returns the trusteeName.
 * @return String
 */
public String getTrusteeName() {
	return trusteeName;
}

/**
 * Returns the trusteeType.
 * @return int
 */
public int getTrusteeType() {
	return trusteeType;
}

/**
 * Sets the accessSecurity.
 * @param accessSecurity The accessSecurity to set
 */
public void setAccessSecurity(int accessSecurity) {
	this.accessSecurity = accessSecurity;
}

/**
 * Sets the trusteeName.
 * @param trusteeName The trusteeName to set
 */
public void setTrusteeName(String trusteeName) {
	this.trusteeName = trusteeName;
}

/**
 * Sets the trusteeType.
 * @param trusteeType The trusteeType to set
 */
public void setTrusteeType(int trusteeType) {
	this.trusteeType = trusteeType;
}
}
