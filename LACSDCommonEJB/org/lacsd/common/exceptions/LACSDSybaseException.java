package org.lacsd.common.exceptions;

/******************************************************************************
//* Copyright (c) 2005 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		LACSDSybaseException.java
//* Revision: 		1.0
//* Author:			tnguyen@lacsd.org
//* Created On: 	06-06-2005
//* Modified by:	
//* Modified On:	
//*					
//* Description:	LACSD Exception for formatting Sybase Errors using Error Layout
/******************************************************************************/

import org.lacsd.common.util.*;

public class LACSDSybaseException extends LACSDDatabaseException {

private static final long serialVersionUID = 326539416215387858L;

private DataErrorInfoLayout dataErrorInfoLayout;
private DB2ErrorDetailLayout db2ErrorDetailLayout;

/**
 * Default Constructor
*/
public LACSDSybaseException() {
	super();
}

/**
 * Message Constructor
*/
public LACSDSybaseException(String msg) {
	super(msg);
}

/**
 * Root-Cause Constructor
*/
public LACSDSybaseException(Throwable rootCause) {
	super(rootCause);
}

/**
 * Message + Root-Cause Constructor
*/
public LACSDSybaseException(String msg, Throwable rootCause) {
	super(msg,rootCause);
}

/**
 * Returns the dataErrorInfoLayout.
 * @return DataErrorInfoLayout
*/
public DataErrorInfoLayout getDataErrorInfoLayout() {
	return dataErrorInfoLayout;
}

/**
 * Returns the db2ErrorDetailLayout.
 * @return DB2ErrorDetailLayout
*/
public DB2ErrorDetailLayout getDb2ErrorDetailLayout() {
	return db2ErrorDetailLayout;
}

/**
 * Sets the dataErrorInfoLayout.
 * @param dataErrorInfoLayout The dataErrorInfoLayout to set
*/
public void setDataErrorInfoLayout(DataErrorInfoLayout dataErrorInfoLayout) {
	this.dataErrorInfoLayout = dataErrorInfoLayout;
}

/**
 * Sets the db2ErrorDetailLayout.
 * @param db2ErrorDetailLayout The db2ErrorDetailLayout to set
*/
public void setDb2ErrorDetailLayout(DB2ErrorDetailLayout db2ErrorDetailLayout) {
	this.db2ErrorDetailLayout = db2ErrorDetailLayout;
}


/**
 * Override StackTrace()
 * @return void
*/
public void printStackTrace(){
	super.printStackTrace();
	System.out.println("Details are: " + getErrorDetails());
}

/**
 * Override StackTrace(PrintWriter)
 * @param java.io.PrintWriter pw
 * @return void
*/
public void printStackTrace(java.io.PrintWriter pw){
	super.printStackTrace(pw);
	pw.println("Details are: " + getErrorDetails());
}

/**
 * Override StackTrace(PrintStream)
 * @param java.io.PrintStream ps
 * @return void
*/		

public void printStackTrace(java.io.PrintStream ps){
	super.printStackTrace(ps);
	ps.println("Details are: " + getErrorDetails());	
}

/**
 * Get Error Details from Data Layout Buffer
 * @return String
*/
public String getErrorDetails(){
	StringBuffer answer = new StringBuffer("");
	if(dataErrorInfoLayout!=null){
		answer.append(" Error Program:");
		answer.append(dataErrorInfoLayout.getPRG_NAME());
		answer.append(" Error SqlCode:");
		answer.append(dataErrorInfoLayout.getSQL_CODE());
		answer.append(" Error Message: ");
		answer.append(dataErrorInfoLayout.getERR_MESSAGE());
		answer.append(" Error Paragraph:");
		answer.append(dataErrorInfoLayout.getPARAGRAPH());
		answer.append(" Error TableName: ");
		answer.append(dataErrorInfoLayout.getTABLE_NAME());
	}
	return answer.toString();
}
}
