package org.lacsd.common.exceptions;

/******************************************************************************
//* Copyright (c) 2003 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		LACSDException.java
//* Revision: 		1.0
//* Author:			M Feinberg
//* Created On: 	07-07-2003
//* Modified by:	
//* Modified On:	
//*					
//* Description:	LACSD Generic Exception - Parent Exception Class for LACSD
/******************************************************************************/

public class LACSDException extends Exception {

private static final long serialVersionUID = 6530951773379387766L;

private Throwable rootCause;

private String errorCode;
private String storedProcedureName;
private String dynamicForwardURL;

private Object infoObject;

private boolean isRecoverable = false;
private boolean isDynamicForward = false;

private boolean isApplicationSpecific = false;

	
/**
 * Default Constructor
*/
public LACSDException() {
	super();
}

/**
 * Message Constructor
*/
public LACSDException(String msg) {
	super(msg);
}

/**
 * Root-Cause Constructor
*/
public LACSDException(Throwable rootCause) {
	super();
	this.rootCause = rootCause;
}

/**
 * Message + Root-Cause Constructor
*/
public LACSDException(String msg, Throwable rootCause) {
	super(msg);
	this.rootCause = rootCause;
}


/**
 * Override StackTrace()
 * @return void
*/
public void printStackTrace(){
	
	if( rootCause != null ){
		System.err.println("Caused By: " + rootCause.getMessage());
	}	
	super.printStackTrace();
}

/**
 * Override StackTrace(PrintWriter)
 * @param java.io.PrintWriter pw
 * @return void
*/
public void printStackTrace(java.io.PrintWriter pw){

	if( rootCause != null ){
		pw.println("Caused By: " + rootCause.getMessage());
	}	
	super.printStackTrace(pw);	
}

/**
 * Override StackTrace(PrintStream)
 * @param java.io.PrintStream ps
 * @return void
*/		

public void printStackTrace(java.io.PrintStream ps){

	if( rootCause != null ){
		ps.println("Caused By: " + rootCause.getMessage());
	}	
	super.printStackTrace(ps);	
}

/**
 * Returns the dynamicForwardURL.
 * @return String
 */
public String getDynamicForwardURL() {
	return dynamicForwardURL;
}

/**
 * Returns the errorCode.
 * @return String
 */
public String getErrorCode() {
	return errorCode;
}

/**
 * Returns the isDynamicForward.
 * @return boolean
 */
public boolean isDynamicForward() {
	return isDynamicForward;
}

/**
 * Returns the isRecoverable.
 * @return boolean
 */
public boolean isRecoverable() {
	return isRecoverable;
}

/**
 * Returns the rootCause.
 * @return Throwable
 */
public Throwable getRootCause() {
	return rootCause;
}

/**
 * Returns the storedProcedureName.
 * @return String
 */
public String getStoredProcedureName() {
	return storedProcedureName;
}

/**
 * Sets the dynamicForwardURL.
 * @param dynamicForwardURL The dynamicForwardURL to set
 */
public void setDynamicForwardURL(String dynamicForwardURL) {
	this.dynamicForwardURL = dynamicForwardURL;
}

/**
 * Sets the errorCode.
 * @param errorCode The errorCode to set
 */
public void setErrorCode(String errorCode) {
	this.errorCode = errorCode;
}

/**
 * Sets the isDynamicForward.
 * @param isDynamicForward The isDynamicForward to set
 */
public void setIsDynamicForward(boolean isDynamicForward) {
	this.isDynamicForward = isDynamicForward;
}

/**
 * Sets the isRecoverable.
 * @param isRecoverable The isRecoverable to set
 */
public void setIsRecoverable(boolean isRecoverable) {
	this.isRecoverable = isRecoverable;
}

/**
 * Sets the rootCause.
 * @param rootCause The rootCause to set
 */
public void setRootCause(Throwable rootCause) {
	this.rootCause = rootCause;
}

/**
 * Sets the storedProcedureName.
 * @param storedProcedureName The storedProcedureName to set
 */
public void setStoredProcedureName(String storedProcedureName) {
	this.storedProcedureName = storedProcedureName;
}
/**
 * Returns the infoObject.
 * @return Object
 */
public Object getInfoObject() {
	return infoObject;
}

/**
 * Returns the isApplicationSpecific.
 * @return boolean
 */
public boolean isApplicationSpecific() {
	return isApplicationSpecific;
}

/**
 * Sets the infoObject.
 * @param infoObject The infoObject to set
 */
public void setInfoObject(Object infoObject) {
	this.infoObject = infoObject;
}

/**
 * Sets the isApplicationSpecific.
 * @param isApplicationSpecific The isApplicationSpecific to set
 */
public void setIsApplicationSpecific(boolean isApplicationSpecific) {
	this.isApplicationSpecific = isApplicationSpecific;
}

/**
 * To String of local values
 * @param isApplicationSpecific The isApplicationSpecific to set
 */
public String getUnhandledLACSDErrorCode() {
	StringBuffer sb = new StringBuffer();
	
	String rc = null;
	if ( this.rootCause == null ) {
		rc = "null";
	} else {
		rc = this.rootCause.getMessage();
	}
	sb.append("\r\n");
	sb.append(" RECOVERABLE-ERROR CODE NOT REGISTERED IN ACTION: >>> [initErrControl() method]").append("\r\n");
	sb.append(" ERROR MESSAGE: ").append(this.getMessage()).append("\r\n");
	sb.append(" ROOT CAUSE: ").append(rc).append("\r\n\r\n");
	sb.append(" Error Code: ").append(this.errorCode).append("\r\n");
	sb.append(" SP Name: ").append(this.storedProcedureName).append("\r\n");
	sb.append(" Is Recoverable: ").append(this.isRecoverable).append("\r\n");
	sb.append(" Is Dynamic Forward: ").append(this.isDynamicForward).append("\r\n");
	sb.append(" Dynamic Forward URL: ").append(this.dynamicForwardURL).append("\r\n");
	sb.append(" Is Application Specific: ").append(this.isApplicationSpecific).append("\r\n");

	return sb.toString();

}

}



