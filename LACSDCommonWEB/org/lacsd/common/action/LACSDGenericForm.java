package org.lacsd.common.action;

/******************************************************************************
//* Copyright (c) 2003 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		LACSDGenericForm.java
//* Revision: 		1.0
//* Author:			M Feinberg
//* Created On: 	07-22-2003
//* Modified by:	M Feinberg
//* Modified On:	10-07-2003
//*					
//* Description:	Super Class for all LACSD Struts Forms-
//* 				Handles Default & "INCLUDE" Form Variables
/******************************************************************************/

import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.validator.ValidatorForm;
import org.lacsd.common.constants.LACSDWebConstants;
import org.lacsd.common.values.PagingVO;

public abstract class LACSDGenericForm extends ValidatorForm {

private static final long serialVersionUID = -1385120053744373606L;
private String actionName;			//	Control parameter/variable for all web screens
private String multiValidate;		//	Forms with multiple input screens return properly
private String confirmationFlag;	// Holds user's response from js confirm() box in GPX popup

private String callingPage;		//	JSP's can include the URL pattern for the calling page for automatic return to caller

protected PagingVO pagingVO = new PagingVO();

public static final String STRUTS_ACTION_ERROR_KEY = "org.apache.struts.action.ERROR";
								//  Attribute name in request

// generic error messages.
public static final String STRUTS_ERRORS_BAD_ACTION_NAME 	= "error.badactionname";
public static final String STRUTS_ERRORS_BAD_ACTION_FWD 		= "error.badactionfwd";
public static final String STRUTS_ERRORS_FAILED_MULTIVALIDATE = "error.failed.multivalidate";
public static final String STRUTS_ERRORS_FRAMEWORK_NOT_USED 	= "error.framework.notused";
public static final String STRUTS_ERRORS_RECOVERABLE_NOT_FOUND = "error.recoverable.notfound";

// generic struts error messages.
public static final String STRUTS_ERRORS_REQUIRED 	= "errors.required";
public static final String STRUTS_ERRORS_MIN_LENGTH 	= "errors.minlength";
public static final String STRUTS_ERRORS_MAX_LENGTH 	= "errors.maxlength";
public static final String STRUTS_ERRORS_INVALID 	= "errors.invalid";

public static final String STRUTS_ERRORS_BYTE 	= "errors.byte";
public static final String STRUTS_ERRORS_SHORT 	= "errors.short";
public static final String STRUTS_ERRORS_INTEGER  = "errors.integer";
public static final String STRUTS_ERRORS_LONG 	= "errors.long";
public static final String STRUTS_ERRORS_FLOAT 	= "errors.float";
public static final String STRUTS_ERRORS_DOUBLE 	= "errors.double";
public static final String STRUTS_ERRORS_ALPHA_NUMERIC = "errors.alphaNumeric";

public static final String STRUTS_ERRORS_DATE 		= "errors.date";
public static final String STRUTS_ERRORS_RANGE 		= "errors.range";
public static final String STRUTS_ERRORS_CREDIT_CARD  = "errors.creditcard";
public static final String STRUTS_ERRORS_EMAIL 		= "errors.email";

public static final String STRUTS_ERRORS_CURRENCY 		= "errors.currency";
public static final String STRUTS_ERRORS_NUMBER 			= "errors.number";
public static final String STRUTS_ERRORS_PHONE 			= "errors.phone";
public static final String STRUTS_ERRORS_PHONE_NO_EXT	= "errors.phoneNoExt";
public static final String STRUTS_ERRORS_FAX 			= "errors.fax";
public static final String STRUTS_ERRORS_ZIP 			= "errors.zip";
public static final String STRUTS_ERRORS_EMPTY_MESSAGE 	= "errors.emptyMessage";

/**
 * Add ActionErrors to request object
 * @param String errKey (for JSP Screen)
 * @param String propertyFileKey (for Text File)
 * @param HttpServletRequest req
 * @return void
*/
protected void addError(String errKey, String propertyFileKey, HttpServletRequest req) {
	this.addError(errKey, propertyFileKey, null, req);
}

/**
 * Add ActionErrors to request object
 * @param String errKey (for JSP Screen)
 * @param String propertyFileKey (for Text File)
 * @param String dynamicMessage (for rootCause)
 * @param HttpServletRequest req
 * @return void
*/
protected void addError(String errKey, String propertyFileKey, Object dynamicCause, HttpServletRequest req) {

	ResourceBundle rb = ResourceBundle.getBundle(LACSDWebConstants.MESSAGE_RESOURCES_FILE, new Locale("en","US"));
	String message = rb.getString(propertyFileKey);
	req.setAttribute(LACSDWebConstants.ERROR_MESSAGE,message);
	if (dynamicCause != null) {
		req.setAttribute(LACSDWebConstants.ERROR_ROOT_CAUSE,dynamicCause.toString());
	}
}

/**
 * Add ActionMessages to request object
 * @param String msgKey (for JSP Screen)
 * @param String propertyFileKey (for Text File)
 * @param HttpServletRequest req
 * @return void
*/
protected void addMessage(String msgKey, String propertyFileKey, HttpServletRequest req) {

	ResourceBundle rb = ResourceBundle.getBundle(LACSDWebConstants.MESSAGE_RESOURCES_FILE, new Locale("en","US"));
	String message = rb.getString(propertyFileKey);
	req.setAttribute(LACSDWebConstants.INFO_MESSAGE,message);
}

/**
 * Hold Objects Temporarily
 * @param String key
 * @param Object value
 * @param HttpServletRequest req
 * @return void
*/
protected void hold(String key, Object value, HttpServletRequest req) {
	req.setAttribute(key,value);	
}

/**
 * Release Objects From Temporary Hold
 * @param String key
 * @param HttpServletRequest req
 * @return object
*/
protected Object release(String key, HttpServletRequest req) {
	
	Object obj = req.getAttribute(key);
	req.removeAttribute(key);
	return obj;
}

/**
 * Returns the actionName.
 * @return String
 */
public String getActionName() {
	return actionName;
}

/**
 * Returns the multiValidate.
 * @return String
 */
public String getMultiValidate() {
	return multiValidate;
}

/**
 * Returns the confirmationFlag
 * @return String
 */
public String getConfirmationFlag() {
	return confirmationFlag;
}

/**
 * Sets the actionName.
 * @param actionName The actionName to set
 */
public void setActionName(String actionName) {
	this.actionName = actionName;
}

/**
 * Sets the multiValidate.
 * @param multiValidate The multiValidate to set
 */
public void setMultiValidate(String multiValidate) {
	this.multiValidate = multiValidate;
}

/**
 * Sets the confirmationFlag.
 * @param confirmationFlag The confirmationFlag to set
 */
public void setConfirmationFlag(String confirmationFlag) {
	this.confirmationFlag = confirmationFlag;
}
/**
 * Returns the callingPage.
 * @return String
 */
public String getCallingPage() {
	return callingPage;
}

/**
 * Sets the callingPage.
 * @param callingPage The callingPage to set
 */
public void setCallingPage(String callingPage) {
	this.callingPage = callingPage;
}

/**
 * @return the pagingVO
 */
public PagingVO getPagingVO() {
	return pagingVO;
}

/**
 * @param pagingVO the pagingVO to set
 */
public void setPagingVO(PagingVO pagingVO) {
	this.pagingVO = pagingVO;
}


}