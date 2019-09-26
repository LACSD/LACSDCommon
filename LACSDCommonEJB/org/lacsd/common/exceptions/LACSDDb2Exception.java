package org.lacsd.common.exceptions;

/******************************************************************************
//* Copyright (c) 2003 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		LACSDDb2Exception.java
//* Revision: 		1.0
//* Author:			M Feinberg
//* Created On: 	07-09-2003
//* Modified by:	
//* Modified On:	
//*					
//* Description:	LACSD Exception for formatting DB2 Errors using Error Layout
/******************************************************************************/

import org.lacsd.common.util.*;

public class LACSDDb2Exception extends LACSDDatabaseException {

private static final long serialVersionUID = 9010807869339995768L;

private DataErrorInfoLayout dataErrorInfoLayout;
private DB2ErrorDetailLayout db2ErrorDetailLayout;

/**
 * Default Constructor
*/
public LACSDDb2Exception() {
	super();
}

/**
 * Message Constructor
*/
public LACSDDb2Exception(String msg) {
	super(msg);
}

/**
 * Root-Cause Constructor
*/
public LACSDDb2Exception(Throwable rootCause) {
	super(rootCause);
}

/**
 * Message + Root-Cause Constructor
*/
public LACSDDb2Exception(String msg, Throwable rootCause) {
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
