package org.lacsd.common.struts2.action;

/******************************************************************************
//* Copyright (c) 2003 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:       LACSDLoginAction.java
//* Revision:       1.5
//* Author:         MFEINBERG@LACSD.ORG
//* Created On:     10-15-2003
//* Modified By:    tnguyen@lacsd.org
//* Modified On:    07-14-2004
//*                 Change to generic action for all applications
//*                 
//* Description:    Login to System - Uses Single Sign-on Stored Procedure
/******************************************************************************/

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpSession;

import org.lacsd.common.authentication.LACSDAuthenticationPO;
import org.lacsd.common.authentication.LACSDUser;
import org.lacsd.common.authentication.UserProfile;
import org.lacsd.common.constants.LACSDWebConstants;
import org.lacsd.common.exceptions.LACSDException;
import org.lacsd.common.util.ApplicationContext;
import org.lacsd.common.util.LACSDErrorMapping;
import org.lacsd.common.values.EmployeeRoleVO;
import org.lacsd.common.values.EmployeeVO;




public class LACSDLoginAction extends  LACSDGenericAction{

private static final long serialVersionUID = -7617739521861595382L;
public static final String ACTION_NAME_SETUP             =        "setup";
public static final String ACTION_NAME_AUTH              =        "authenticate";
public static final String ACTION_NAME_AUTO_AUTH         =        "autoAuthenticate";

public static final String FORWARD_PAGE_SETUP            =        "setup";

public static final String MESSAGE_LOGIN_FAILED          =        "login.authenticate.failed";
public static final String MESSAGE_LOGIN_ID_BLANK        =        "login.userName.blank";
public static final String MESSAGE_LOGIN_ID_NUMBER       =        "login.employeeID.numberFormat";
public static final String MESSAGE_LOGIN_PASSWD_BLANK    =        "login.password.blank";

public static final String MESSAGE_EMPLOYEE_ID_NOTFOUND  =        "login.employeeID.notfound";
public static final String MESSAGE_LOGIN_ID_NOTRDX       =        "login.employeeID.notrdx";
public static final String MESSAGE_EMP_NOT_AUTH_FOR_APP	 =        "login.employee.not.authorized.for.app";

private UserProfile userProfile;



/**
 * Constructor initializes static controller forwards
 * 
*/
public LACSDLoginAction() {
    super();
    LACSDErrorMapping eMapping1 = new LACSDErrorMapping();
    LACSDErrorMapping eMapping2 = new LACSDErrorMapping();
    
    eMapping1.setErrCode("00003");
    eMapping1.setErrorMessageKey(MESSAGE_EMPLOYEE_ID_NOTFOUND);
    eMapping1.setForwardPageKey(FORWARD_PAGE_SETUP);

    eMapping2.setErrCode("00104");
    eMapping2.setErrorMessageKey(MESSAGE_LOGIN_ID_NOTRDX);
    eMapping2.setForwardPageKey(ACTION_NAME_SETUP);
    
    
    errorForwardMap.put(eMapping1.getErrCode(),eMapping1);
    errorForwardMap.put(eMapping2.getErrCode(),eMapping2);
}

//supper class abstract method
public void initErrControl() {
	//not implement
}

//supper class abstract method
public String executeWithResult() throws LACSDException {
	//not implement
	return null;
}
/**
 * Primary (TOP LEVEL) Execute Method - Jakarta Struts Framework v1.1
 * @param ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletReponse res
 * @return ActionForward
 * @throws IOException, ServletException
*/
public String executeApplicationGlobal() throws LACSDException {

    log.debug("ENTERING execute()");
    String fwd = null;
    LACSDUser user = new LACSDUser();
    
    EmployeeVO employeeVO = new EmployeeVO();
    
    ApplicationContext appCtx = ApplicationContext.getInstance();
    EmployeeRoleVO employeeRoleVO = new EmployeeRoleVO();
    employeeRoleVO.setApplicationID(appCtx.get(LACSDWebConstants.APPLICATION_ID));
    employeeVO.setEmployeeRoleVO(employeeRoleVO);
    
    user.setEmployeeVO(employeeVO);
    
    // Initialize UserProfile Interface
    userProfile = user;

    this.userProfile = user;
    if (actionName.equalsIgnoreCase(ACTION_NAME_SETUP)) {
        fwd = FORWARD_PAGE_SETUP;
    }
    else if ((actionName.equalsIgnoreCase(ACTION_NAME_AUTH)) || (actionName.equalsIgnoreCase(ACTION_NAME_AUTO_AUTH))) {
    	try {
    		fwd = doAuthenticate();
    	}catch(Throwable t) {
    		throw new LACSDException(t.getMessage(),t);
    	}
    }
    else {
    	fwd = FORWARD_PAGE_SETUP;
    }

    log.debug("EXITING execute()");

    return fwd;
}


/**
 * Process Authentication
 * @param LoginForm form
 * @param ActionMapping mapping
 * @param HttpServletRequest req
 * @return ActionForward
 * @throws LACSDException, UnknownHostException
 */
private String doAuthenticate() throws  Throwable, LACSDException {

   log.debug("ENTERING doAuthenticate()");

    String fwd = null;
    HttpSession session = request.getSession(true);
    LACSDAuthenticationPO authenticationPO = new LACSDAuthenticationPO();
    
    //  1)  Populate UserProfile with necessary info
    String ipaddress = request.getRemoteAddr();
    if (ipaddress.equals("127.0.0.1")) {
        ipaddress = java.net.InetAddress.getLocalHost().getHostAddress();
    }
    userProfile.setIPAddress(ipaddress);
    String sessionID = session.getId();
    
    //  2)  Verify user is already (currently) logged into single sign-on
    userProfile = authenticationPO.verifyLogin(userProfile);
    
    //If user logged in from the share station, force user to log in again if it is a new session
    if (forceToLogout(userProfile, sessionID)) {
    	userProfile = authenticationPO.removeSignon(userProfile);
    }
   
    if (userProfile.isLoggedIN()) {

    	// user is logged in but not authorized to use the application
    	// (logged in from another application)
    	// forward to OSX sign-on screen and set flag isAuthorized to false
        if (!userProfile.isAuthenticated()) {
        	ApplicationContext appCtx = ApplicationContext.getInstance();
        	String url = appCtx.get("osxurl");
        	//res.sendRedirect(url + "&isAuthorized=false");
        	response.sendRedirect(url + "&isAuthorized=false");;
        }

        //  B)  Refresh database with current HTTPSessionID - (Show other Apps that RDX is most recent)
        userProfile.setHttpSessionID(session.getId());
        userProfile = authenticationPO.updateSignon(userProfile);
        
        setUserProfile(userProfile);
        //  User is already authenticated by single sign-on - Forward to Welcome Page
        fwd = LACSDWebConstants.FORWARD_WELCOME_PAGE;
    }
    else {
        //  A)  Auto Authenticate should put user at login page so they can now login
        fwd = LACSDWebConstants.FORWARD_LOGIN_SCREEN;
    }

    log.debug("EXITING doAuthenticate()");
    
    return fwd;
}
/**
 * Check to see if a user needs to login again when he/she is using the share station
 * @param profile
 * @param sessionID
 * @return
 * @throws Exception
 */
private boolean forceToLogout(UserProfile profile,  String sessionID)  throws Exception{
	//If user logged in from the share station, force user to log in again if it is a new session
	SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    String machineLanID = (request.getParameter("userName")==null?"":request.getParameter("userName").trim());
    long secondDiff = 0;
    if (profile.getLastLogin() != null) {
    	Date loginDate = format.parse(profile.getLastLogin());
    	Date today = Calendar.getInstance().getTime();
    	long diff = today.getTime() - loginDate.getTime();
    	secondDiff = diff / 1000;
    }
    if (profile.isLoggedIN() && !profile.getHttpSessionID().trim().equalsIgnoreCase(sessionID)
        && 	!profile.getUserName().trim().equalsIgnoreCase(machineLanID)
        && 	secondDiff > LACSDWebConstants.SECOND_DIFF_ALLOW
       	) {
    	return true;
    }
    return false;
}


}