package org.lacsd.common.constants;

/******************************************************************************
//* Copyright (c) 2004 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		LACSDWebConstants.java
//* Revision: 		1.0
//* Author:			MFEINBERG@LACSD.ORG
//* Created On: 	02-19-2004
//* Modified by:	
//* Modified On:	
//*					
//* Description:	LACSD Web Constants
/******************************************************************************/

public abstract class LACSDWebConstants {

	
public static final String STRUTS2_MESSAGE 				= "message"; // Request Param Name for GPX Popup
public static final String INFO_MESSAGE 				= "info"; // Request Param Name for GPX Popup
public static final String ERROR_MESSAGE 				= "error"; // Request Param Name for GPX Popup
public static final String CONFIRM_MESSAGE 				= "confirm"; // Request Param Name for GPX Popup
public static final String STRUTS_ACTION_ERROR_KEY 		= "org.apache.struts.action.ERROR";


public static final String ERROR_ROOT_CAUSE 			= "error_root"; // Request Param Name for GPX Popup
public static final String ACTION 						= "action"; // Request Param Name for GPX Popup
public static final String ACTION_NAME 					= "actionName"; // Request Param Name for GPX Popup

public static final String MESSAGE_RESOURCES_FILE 		= "resources.error_messages"; // Resource Bundle Name
public static final String ENVIRONMENT_RESOURCES 		= "resources.environment_resources";	//	Application Environment

public static final String USER_PROFILE 				= "user_profile"; // Session Ctx Attibute Name
public static final String SYS_SETTINGS 				= "sys_settings";	// Application Ctx Attribute Name
public static final String SECURITY_MAPPING 			= "security_mapping";	// Application Ctx Attribute Name
public static final String APPLICATION_ID 				= "webApplicationID";	// ApplicationID for OSX Security + Roles
public static final String APPLICATION_BUILD_NO 		= "buildNO";			// ApplicationID Build Number
public static final String APP_ERROR_CONTACT_EMAIL 		= "app.error.contact.email"; // Contact email in case of application error
public static final String LACSD_EMAIL_HOST 			= "mailout.lacsd.org";
public static final String APPLICATION_CFG_FILE_JNDI 	= "config.file.jndi";
public static final String APPLICATION_CFG_FILE_SYS 	= "config.file.sysprops";
public static final String WATSMAIN_CONFIG          	= "WAO_CONFIG";
public static final String LOG4J_INIT_FILE 				= "log4j-init-file";
public static final String SERVER_TYPE 					= "server.type";
public static final String SERVER_TOMCAT 				= "tomcat";
public static final String SERVER_WEBSPHERE 			= "websphere";
public static final String SERIALIZED_REPORT_VO_DIR 	= "serialized.reportVO.location";
public static final String OSXURL						= "osxurl";


public static final boolean GLOBAL_TEST 				= false;
public static final boolean GLOBAL_WEBSERVICES 			= false;

public static final int USER_PROFILE_PAUSE_INTERVAL		= 45;	// Hit Database Every NN Seconds

public static final int MAX_MEGS_UPLOAD 				= 10;
public static final int CLEANUP_OFFLINE_REPORTS_DAYS	= 5;	// If a report is sent via 'email', it will remain on the server temp dir for NN Days

public static final String TEMP_DIR_KEY 				= "user.home";

public static final String ROLE_GUEST					= "Guest";

public static final String FORWARD_APP_FAILURE            =     "app-failure";
public static final String FORWARD_DB2_FAILURE            =     "db2-failure";
public static final String FORWARD_SQL2K_FAILURE          =     "sql2k-failure";
public static final String FORWARD_ORACLE_FAILURE         =     "oracle-failure";
public static final String FORWARD_ECI_FAILURE            =     "eci-failure";

public static final String MESSAGE_APP_FAILURE            =     "error.app.failure";
public static final String MESSAGE_DB2_FAILURE            =     "error.db2.failure";
public static final String MESSAGE_SQL2K_FAILURE          =     "error.sql2k.failure";
public static final String MESSAGE_ORACLE_FAILURE         =     "error.oracle.failure";
public static final String MESSAGE_ECI_FAILURE            =     "error.eci.failure";

public static final String FORWARD_LOGIN_SCREEN           =     "login-screen";
public static final String FORWARD_LOGIN_ACTION           =     "login-action";
public static final String FORWARD_WELCOME_PAGE           =     "welcome-page";

public static final String MESSAGE_SECURITY_FAILURE       =     "error.security.failure";
public static final String MESSAGE_SECURITY_NO_PERMIT     =     "error.security.notauthorized";
public static final String MESSAGE_BAD_ACTIONFORWARD      =     "error.badactionfwd"; 
public static final String MESSAGE_FAILED_MULTIVALIDATE   = 	"error.failed.multivalidate";

//generic struts error messages.
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
public static final String STRUTS_ERRORS_NUMBER 		= "errors.number";
public static final String STRUTS_ERRORS_PHONE 			= "errors.phone";
public static final String STRUTS_ERRORS_PHONE_NO_EXT	= "errors.phoneNoExt";
public static final String STRUTS_ERRORS_FAX 			= "errors.fax";
public static final String STRUTS_ERRORS_ZIP 			= "errors.zip";
public static final String STRUTS_ERRORS_EMPTY_MESSAGE 	= "errors.emptyMessage";

public static final String EMPLOYEE_ID_AD_NOTFOUND 	= "000000";

public static final String EMPLOYEE_ID_AD_MAX_SOCKET= "000001";

//number of seconds different to allow cluster environment forward user to different node 
//before force users to log in again from the share station
public static final int    SECOND_DIFF_ALLOW = 20;

}