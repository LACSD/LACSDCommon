package org.lacsd.common.exceptions;

/******************************************************************************
//* Copyright (c) 2003 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		LACSDEciException.java
//* Revision: 		1.0
//* Author:			M Feinberg
//* Created On: 	12-17-2003
//* Modified by:	
//* Modified On:	
//*					
//* Description:	LACSD Exception to wrap runtime CICS ECI err conditions
/******************************************************************************/

public class LACSDEciException extends LACSDException {

private static final long serialVersionUID = -4106619687925741678L;

/**
 * Default Constructor
*/
public LACSDEciException() {
	super();
}

/**
 * Message Constructor
*/
public LACSDEciException(String msg) {
	super(msg);
}

/**
 * Root-Cause Constructor
*/
public LACSDEciException(Throwable rootCause) {
	super(rootCause);
}

/**
 * Message + Root-Cause Constructor
*/
public LACSDEciException(String msg, Throwable rootCause) {
	super(msg,rootCause);
}
}

