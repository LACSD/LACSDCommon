package org.lacsd.common.util;

/******************************************************************************
//* Copyright (c) 2003 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		DataErrorInfoLayout.java
//* Revision: 		1.0
//* Author:			M Feinberg
//* Created On: 	07-09-2003
//* Modified by:	
//* Modified On:	
//*					
//* Description:	Custom Layout Buffer for formatting Database Error Msgs
/******************************************************************************/

public class DataErrorInfoLayout extends LayoutBuffer {

private final int EOF_INDICATOR				= 0;
private final int PRG_NAME					= 1;
private final int ERR_FLAG					= 2;
private final int RETURN_CODE				= 3;
private final int PARAGRAPH					= 4;
private final int TABLE_NAME				= 5;
private final int SQL_CODE					= 6;
private final int ERR_MESSAGE				= 7;
private final int ERR_PARAS					= 8;

private static final int ERR_LAYOUT_LENGTH	= 256;

//private final String PROC_SUCCESS_CODE 		= "000";
private final String DB2_ERROR_CODE 		= "001";
	
private final String STORED_PROC_ERROR_FLAG = "Y";


/**
 * Constructor for DataErrorInfoLayout
*/
public DataErrorInfoLayout(int length) {
	super(length);
}

/**
 * Constructor for DataErrorInfoLayout
*/
public DataErrorInfoLayout() {
	this(ERR_LAYOUT_LENGTH);
	initializeLayout();
}

/**
 * Initialize Inner Class in Super Class
*/	
public void initializeLayout(){
	super.addField(FIELDTYPE_PICX, 1);
	super.addField(FIELDTYPE_PICX, 8);
	super.addField(FIELDTYPE_PICX, 1);
	super.addField(FIELDTYPE_PICX, 3);
	super.addField(FIELDTYPE_PICX, 5);
	super.addField(FIELDTYPE_PICX, 18);
	super.addField(FIELDTYPE_PICX, 4);
	super.addField(FIELDTYPE_PICX, 110);
	super.addField(FIELDTYPE_PICX, 105);
}

/**
 * Informs if this is a stored procedure error
 * @return boolean
*/
public boolean isStoredProcError(){
	return getERR_FLAG().equals(STORED_PROC_ERROR_FLAG);
}

/**
 * Informs if this is a DB2 stored procedure error
 * @return boolean
*/
public boolean isStoredProcDB2Error(){
	return getERR_FLAG().equals(STORED_PROC_ERROR_FLAG) && getRETURN_CODE().equals(DB2_ERROR_CODE);
}

/**
 * Gets the EOF_INDICATOR
 * @return String
*/
public String getEOF_INDICATOR() {
	return super.getString(EOF_INDICATOR);
}

/**
 * Gets the PRG_NAME
 * @return String
*/
public String getPRG_NAME() {
	return super.getString(PRG_NAME);
}

/**
 * Gets the ERR_FLAG
 * @return String
*/
public String getERR_FLAG() {
	return super.getString(ERR_FLAG);
}

/**
 * Gets the RETURN_CODE
 * @return String
*/
public String getRETURN_CODE() {
	return super.getString(RETURN_CODE);
}

/**
 * Gets the PARAGRAPH
 * @return String
*/
public String getPARAGRAPH() {
	return super.getString(PARAGRAPH);
}

/**
 * Gets the TABLE_NAME
 * @return String
*/
public String getTABLE_NAME() {
	return super.getString(TABLE_NAME);
}

/**
 * Gets the SQL_CODE
 * @return String
*/
public String getSQL_CODE() {
	return super.getString(SQL_CODE);
}

/**
 * Gets the ERR_CODE
 * @return String
 */
public String getERR_MESSAGE() {
	return super.getString(ERR_MESSAGE);
}

/**
 * Gets the ERR_PARAS
 * @return String
*/
public String getERR_PARAS() {
	return super.getString(ERR_PARAS);
}
}
