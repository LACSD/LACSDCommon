package org.lacsd.common.exceptions;

/******************************************************************************
//* Copyright (c) 2003 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		LACSDDatabaseException.java
//* Revision: 		1.0
//* Author:			M Feinberg
//* Created On: 	07-07-2003
//* Modified by:	
//* Modified On:	
//*					
//* Description:	LACSD Exception to wrap runtime SQL/Database err conditions
/******************************************************************************/

public class LACSDDatabaseException extends LACSDException {

private static final long serialVersionUID = -3440443457241258586L;

/**
 * Default Constructor
*/
public LACSDDatabaseException() {
	super();
}

/**
 * Message Constructor
*/
public LACSDDatabaseException(String msg) {
	super(msg);
}

/**
 * Root-Cause Constructor
*/
public LACSDDatabaseException(Throwable rootCause) {
	super(rootCause);
}

/**
 * Message + Root-Cause Constructor
*/
public LACSDDatabaseException(String msg, Throwable rootCause) {
	super(msg,rootCause);
}
}

