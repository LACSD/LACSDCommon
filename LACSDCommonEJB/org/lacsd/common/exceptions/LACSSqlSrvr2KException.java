package org.lacsd.common.exceptions;

/******************************************************************************
//* Copyright (c) 2003 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		LACSSqlSrvr2KException.java
//* Revision: 		1.0
//* Author:			T Nguyen
//* Created On: 	04-10-2006
//* Modified by:	
//* Modified On:	
//*					
//* Description:	LACSD Exception for formatting SQLSRVR2K Errors using Error Layout
/******************************************************************************/

import org.lacsd.common.util.*;

public class LACSSqlSrvr2KException extends LACSDDatabaseException {

private static final long serialVersionUID = 8518618996443159564L;

private DataErrorInfoLayout dataErrorInfoLayout;
private SqlSrvr2KErrorDetailLayout SqlSrvr2KErrorDetailLayout;

/**
 * Default Constructor
*/
public LACSSqlSrvr2KException() {
	super();
}

/**
 * Message Constructor
*/
public LACSSqlSrvr2KException(String msg) {
	super(msg);
}

/**
 * Root-Cause Constructor
*/
public LACSSqlSrvr2KException(Throwable rootCause) {
	super(rootCause);
}

/**
 * Message + Root-Cause Constructor
*/
public LACSSqlSrvr2KException(String msg, Throwable rootCause) {
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
 * Returns the SqlSrvr2KErrorDetailLayout.
 * @return SqlSrvr2KErrorDetailLayout
*/
public SqlSrvr2KErrorDetailLayout getSqlSrvr2KErrorDetailLayout() {
	return SqlSrvr2KErrorDetailLayout;
}

/**
 * Sets the dataErrorInfoLayout.
 * @param dataErrorInfoLayout The dataErrorInfoLayout to set
*/
public void setDataErrorInfoLayout(DataErrorInfoLayout dataErrorInfoLayout) {
	this.dataErrorInfoLayout = dataErrorInfoLayout;
}

/**
 * Sets the SqlSrvr2KErrorDetailLayout.
 * @param SqlSrvr2KErrorDetailLayout The SqlSrvr2KErrorDetailLayout to set
*/
public void setSqlSrvr2KErrorDetailLayout(SqlSrvr2KErrorDetailLayout SqlSrvr2KErrorDetailLayout) {
	this.SqlSrvr2KErrorDetailLayout = SqlSrvr2KErrorDetailLayout;
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
