package org.lacsd.common.util;

/******************************************************************************
//* Copyright (c) 2003 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		DB2ErrorDetailLayout.java
//* Revision: 		1.0
//* Author:			M Feinberg
//* Created On: 	07-09-2003
//* Modified by:	
//* Modified On:	
//*					
//* Description:	Custom Layout Buffer for formatting DB2 Database Error Msgs
/******************************************************************************/

public class DB2ErrorDetailLayout extends LayoutBuffer {


private static final int DB2_ERR_LAYOUT_LENGTH = 246;


/**
 * Constructor for DB2ErrorDetailLayout
*/
public DB2ErrorDetailLayout(int length) {
	super(length);
}

/**
 * Constructor for DB2ErrorDetailLayout
*/
public DB2ErrorDetailLayout() {
	this(DB2_ERR_LAYOUT_LENGTH);
	initializeLayout();
}

/**
 * Initialize Inner Class in Super Class
*/	
private void initializeLayout(){}

}
