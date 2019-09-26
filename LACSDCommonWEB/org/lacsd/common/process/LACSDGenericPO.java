package org.lacsd.common.process;

/******************************************************************************
//* Copyright (c) 2003 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		LACSDGenericPO.java
//* Revision: 		1.0
//* Author:			plee@lacsd.org, dyip@lacsd.org
//* Created On: 	12-19-2003
//* Modified By:	MFEINBERG@LACSD.ORG
//* Modified On:	04-08-2004
//*					
//* Description:	Super Class for all LACSD Process Object
//*					Maintains database testing fascade and
//*					Houses load balancing logic 
//* 
//*  Modification:  Added helper method to derive EJB Home interface.  This can
//* 				be used for local servers as well as remote servers, and
//* 				elliviates the need for IBM Proprietary EJB Access Beans.
//* 				Future Considerations:  EJB 2.0,  Local Home vs. Remote Home
/******************************************************************************/
import org.lacsd.common.constants.LACSDWebConstants;

public abstract class LACSDGenericPO {

	private boolean _TEST = false;
	private boolean _WEBSERVICES = false;

/**
 * Default Constructor - Initializes super class
*/
public LACSDGenericPO() {
	super();
	init();
}

/**
 * Initialization method - reads test parameter from properties file
 * 
 * <code>future : load balance broker init sequence here </code>
*/
private void init() {
	
//	ApplicationContext appctx = ApplicationContext.getInstance();
	_TEST = LACSDWebConstants.GLOBAL_TEST;
	_WEBSERVICES = LACSDWebConstants.GLOBAL_WEBSERVICES;
}

/**
 * Returns the test.
 * @return boolean
 */
public boolean isTest() {
	return _TEST;
}

/**
 * Returns the WebServices.
 * @return boolean
 */
public boolean isWebServices() {
	return _WEBSERVICES;
}
}
