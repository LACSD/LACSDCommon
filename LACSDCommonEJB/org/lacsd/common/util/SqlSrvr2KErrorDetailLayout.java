package org.lacsd.common.util;

/******************************************************************************
//* Copyright (c) 2003 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		SqlSrvr2KErrorDetailLayout.java
//* Revision: 		1.0
//* Author:			T Nguyen
//* Created On: 	04-10-2006
//* Modified by:	
//* Modified On:	
//*					
//* Description:	Custom Layout Buffer for formatting SQL Server 2K Database Error Msgs
/******************************************************************************/

public class SqlSrvr2KErrorDetailLayout extends LayoutBuffer {


private static final int SQL_SERVER_2K_ERR_LAYOUT_LENGTH = 246;


/**
 * Constructor for SQLServer2KErrorDetailLayout
*/
public SqlSrvr2KErrorDetailLayout(int length) {
	super(length);
}

/**
 * Constructor for SQLServer2KErrorDetailLayout
*/
public SqlSrvr2KErrorDetailLayout() {
	this(SQL_SERVER_2K_ERR_LAYOUT_LENGTH);
	initializeLayout();
}

/**
 * Initialize Inner Class in Super Class
*/	
private void initializeLayout(){}

}
