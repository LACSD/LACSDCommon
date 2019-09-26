package org.lacsd.common.util;

/******************************************************************************
//* Copyright (c) 2006 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		Oracle10ErrorDetailLayout.java
//* Revision: 		1.0
//* Author:			M Feinberg
//* Created On: 	08-09-2006
//* Modified by:	
//* Modified On:	
//*					
//* Description:	Custom Layout Buffer for formatting Oracle10 Database Error Msgs
/******************************************************************************/

public class Oracle10ErrorDetailLayout extends LayoutBuffer {


private static final int ORACLE_ERR_LAYOUT_LENGTH = 246;


/**
 * Constructor for DB2ErrorDetailLayout
*/
public Oracle10ErrorDetailLayout(int length) {
	super(length);
}

/**
 * Constructor for DB2ErrorDetailLayout
*/
public Oracle10ErrorDetailLayout() {
	this(ORACLE_ERR_LAYOUT_LENGTH);
	initializeLayout();
}

/**
 * Initialize Inner Class in Super Class
*/	
private void initializeLayout(){}

}
