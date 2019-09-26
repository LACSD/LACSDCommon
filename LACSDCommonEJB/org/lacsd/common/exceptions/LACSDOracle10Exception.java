package org.lacsd.common.exceptions;

/******************************************************************************
//* Copyright (c) 2006 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		LACSDOracle10Exception.java
//* Revision: 		1.0
//* Author:			M Feinberg
//* Created On: 	08-09-2006
//* Modified by:	
//* Modified On:	
//*					
//* Description:	LACSD Exception for formatting ORACLE10G Errors using Error Layout
/******************************************************************************/

import org.lacsd.common.util.*;

public class LACSDOracle10Exception extends LACSDDatabaseException {

private static final long serialVersionUID = -8839189687588842085L;

private DataErrorInfoLayout dataErrorInfoLayout;
private Oracle10ErrorDetailLayout oracle10ErrorDetailLayout;

/**
 * Default Constructor
*/
public LACSDOracle10Exception() {
	super();
}

/**
 * Message Constructor
*/
public LACSDOracle10Exception(String msg) {
	super(msg);
}

/**
 * Root-Cause Constructor
*/
public LACSDOracle10Exception(Throwable rootCause) {
	super(rootCause);
}

/**
 * Message + Root-Cause Constructor
*/
public LACSDOracle10Exception(String msg, Throwable rootCause) {
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
 * Returns the oracleErrorDetailLayout.
 * @return Oracle10ErrorDetailLayout
*/
public Oracle10ErrorDetailLayout getOracleErrorDetailLayout() {
	return oracle10ErrorDetailLayout;
}

/**
 * Sets the dataErrorInfoLayout.
 * @param dataErrorInfoLayout The dataErrorInfoLayout to set
*/
public void setDataErrorInfoLayout(DataErrorInfoLayout dataErrorInfoLayout) {
	this.dataErrorInfoLayout = dataErrorInfoLayout;
}

/**
 * Sets the oracleErrorDetailLayout.
 * @param oracle10ErrorDetailLayout The oracle10ErrorDetailLayout to set
*/
public void setDb2ErrorDetailLayout(Oracle10ErrorDetailLayout oracle10ErrorDetailLayout) {
	this.oracle10ErrorDetailLayout = oracle10ErrorDetailLayout;
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
