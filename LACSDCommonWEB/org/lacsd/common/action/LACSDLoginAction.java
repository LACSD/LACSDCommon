package org.lacsd.common.action;

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
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.lacsd.common.authentication.LACSDAuthenticationPO;
import org.lacsd.common.authentication.UserProfile;
import org.lacsd.common.constants.LACSDWebConstants;
import org.lacsd.common.exceptions.LACSDDb2Exception;
import org.lacsd.common.exceptions.LACSDEciException;
import org.lacsd.common.exceptions.LACSDException;
import org.lacsd.common.exceptions.LACSDSqlSrvr2KException;
import org.lacsd.common.util.ApplicationContext;
import org.lacsd.common.util.LACSDErrorMapping;

public class LACSDLoginAction extends Action {

private final String CLASS_NAME = this.getClass().getName();
protected Logger log = LogManager.getLogger(CLASS_NAME);

private HashMap<String, LACSDErrorMapping> errorForwardMap;

public static final String ACTION_NAME_SETUP             =        "setup";
public static final String ACTION_NAME_AUTH              =        "authenticate";
public static final String ACTION_NAME_AUTO_AUTH         =        "autoAuthenticate";

public static final String FORWARD_PAGE_SETUP            =        "setup";
public static final String FORWARD_PAGE_GLOBAL_SETUP     =	      "globalSetup";
public static final String FORWARD_LOGIN_SCREEN          =        "login-screen";
public static final String FORWARD_WELCOME_PAGE          =        "welcome-page";
public static final String FORWARD_GLOBAL_FAILURE        =        "global-failure";


/** GLOBAL ERROR SET **/

public static final String MESSAGE_FRAMEWORK_NOTUSED     =        "error.framework.notused";
public static final String MESSAGE_BAD_ACTIONFORWARD     =        "error.badactionfwd";
public static final String MESSAGE_RECOVERABLE_NULL      =        "error.recoverable.notfound";
/** GLOBAL ERROR SET **/

public static final String MESSAGE_LOGIN_FAILED          =        "login.authenticate.failed";
public static final String MESSAGE_LOGIN_ID_BLANK        =        "login.userName.blank";
public static final String MESSAGE_LOGIN_ID_NUMBER       =        "login.employeeID.numberFormat";
public static final String MESSAGE_LOGIN_PASSWD_BLANK    =        "login.password.blank";

public static final String MESSAGE_EMPLOYEE_ID_NOTFOUND  =        "login.employeeID.notfound";
public static final String MESSAGE_LOGIN_ID_NOTRDX       =        "login.employeeID.notrdx";
public static final String MESSAGE_EMP_NOT_AUTH_FOR_APP	 =        "login.employee.not.authorized.for.app";



/**
 * Constructor initializes static controller forwards
 * 
*/
public LACSDLoginAction() {
    super();
    errorForwardMap = new HashMap<String, LACSDErrorMapping>();
    
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

/**
 * Primary (TOP LEVEL) Execute Method - Jakarta Struts Framework v1.1
 * @param ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletReponse res
 * @return ActionForward
 * @throws IOException, ServletException
*/
public ActionForward execute(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res) throws IOException, ServletException {

    
    log.debug("ENTERING execute()");
    
    ActionForward fwd = null;
    LACSDLoginForm loginForm = (LACSDLoginForm)form;
    String actionName = loginForm.getActionName();

    if (actionName.equalsIgnoreCase(ACTION_NAME_SETUP)) {
        fwd = doSetup(mapping);
    }
    else if ((actionName.equalsIgnoreCase(ACTION_NAME_AUTH)) || (actionName.equalsIgnoreCase(ACTION_NAME_AUTO_AUTH))) {

        try {
            fwd = doAuthenticate(loginForm,mapping,req,res,actionName);
        }
        
        
        //  HANDLE ERROR CONDITIONS
        //****************************************************************************/
        catch (LACSDException e) {
            
            // determine if recoverable or not
            if (e.isRecoverable()) {
                
                //  12-17-2003 -> Get recoverable message key and forward pages from Action Registry
                LACSDErrorMapping eMapping = (LACSDErrorMapping)errorForwardMap.get(e.getErrorCode());
                
                if (eMapping == null) {
                    this.addError("nonrecoverable",LACSDWebConstants.MESSAGE_APP_FAILURE,MESSAGE_RECOVERABLE_NULL,req,true);
                    return mapping.findForward(LACSDWebConstants.FORWARD_APP_FAILURE);
                }
                
                String errorMessageKey = eMapping.getErrorMessageKey();
                this.addError("recoverable",errorMessageKey,e.getRootCause(),req);
                
                //  REVISED 12-17-2003 ->   3% of Actions have Dynamic FWD Key (derived from VO)
                //                          These forwards are set into LACSDException at the DAO Tier
                //                          Example:  RDXSwitchAccount Use Case
                if (e.isDynamicForward()) {
                    return new ActionForward(e.getDynamicForwardURL());
                }
                
                String recoverableFwd  = eMapping.getForwardPageKey();
                if (recoverableFwd == null) {
                    this.addError("nonrecoverable",LACSDWebConstants.MESSAGE_APP_FAILURE,MESSAGE_RECOVERABLE_NULL,req,true);
                    return mapping.findForward(LACSDWebConstants.FORWARD_APP_FAILURE);
                }
                return mapping.findForward(recoverableFwd);
            }
            else {
                log.error(e.getMessage(),e.getRootCause());
                
                if (e instanceof LACSDDb2Exception) {
                    this.addError("nonrecoverable",LACSDWebConstants.MESSAGE_DB2_FAILURE,e.getRootCause(),req);
                    return mapping.findForward(LACSDWebConstants.FORWARD_DB2_FAILURE);
                }
                else if (e instanceof LACSDSqlSrvr2KException) {
                    this.addError("nonrecoverable",LACSDWebConstants.MESSAGE_SQL2K_FAILURE,e.getRootCause(),req);
                    return mapping.findForward(LACSDWebConstants.FORWARD_SQL2K_FAILURE);
                }
                else if (e instanceof LACSDEciException) {
                    this.addError("nonrecoverable",LACSDWebConstants.MESSAGE_ECI_FAILURE,e.getRootCause(),req);
                    return mapping.findForward(LACSDWebConstants.FORWARD_ECI_FAILURE);
                }
                else {
                    this.addError("nonrecoverable",LACSDWebConstants.MESSAGE_APP_FAILURE,e.getRootCause(),req);
                    return mapping.findForward(LACSDWebConstants.FORWARD_APP_FAILURE);
                }
            }
        }
        catch (Throwable t) {   //  This would happen as a result of EJB Failure throwing throwable
                                //  or any other non-expected runtime exception in the application
            log.error(t.getMessage(),t);
            this.addError("nonrecoverable",LACSDWebConstants.MESSAGE_APP_FAILURE,t.getMessage(),req);
            return mapping.findForward(LACSDWebConstants.FORWARD_APP_FAILURE);
        }
    }
    else {
        fwd = doSetup(mapping);
    }

    log.debug("EXITING execute()");

    return fwd;
}

/**
 * Build Login Screen
 * @param ActionMapping mapping
 * @return ActionForward
*/
private ActionForward doSetup(ActionMapping mapping) {
    
    log.debug("ENTERING doSetup()");
    return mapping.findForward(FORWARD_PAGE_SETUP);
}

/**
 * Process Authentication
 * @param LoginForm form
 * @param ActionMapping mapping
 * @param HttpServletRequest req
 * @return ActionForward
 * @throws LACSDException, UnknownHostException
 */
private ActionForward doAuthenticate(LACSDLoginForm form, ActionMapping mapping, HttpServletRequest req, HttpServletResponse res, String actionName) throws LACSDException, Throwable {

    log.debug("ENTERING doAuthenticate()");

    ActionForward fwd = null;
    HttpSession session = req.getSession(true);
    LACSDAuthenticationPO authenticationPO = new LACSDAuthenticationPO();
    UserProfile profile = form.getUserProfile();

    //  1)  Populate UserProfile with necessary info
    String ipaddress = req.getRemoteAddr();
    if (ipaddress.equals("127.0.0.1")) {
        ipaddress = java.net.InetAddress.getLocalHost().getHostAddress();
    }
    profile.setIPAddress(ipaddress);
    
    String sessionID = session.getId();
    
    
    //  2)  Verify user is already (currently) logged into single sign-on
    profile = authenticationPO.verifyLogin(profile);

    
    //If user logged in from the share station, force user to log in again if it is a new session
    if (forceToLogout(profile, req, sessionID)) {
    	profile = authenticationPO.removeSignon(profile);
    }

    if (profile.isLoggedIN()) {

    	// user is logged in but not authorized to use the application
    	// (logged in from another application)
    	// forward to OSX sign-on screen and set flag isAuthorized to false
        if (!profile.isAuthenticated()) {
        	ApplicationContext appCtx = ApplicationContext.getInstance();
        	String url = appCtx.get("osxurl");
        	res.sendRedirect(url + "&isAuthorized=false");
        	return null;
        }
        //  B)  Refresh database with current HTTPSessionID - (Show other Apps that RDX is most recent)
        profile.setHttpSessionID(session.getId());
        profile = authenticationPO.updateSignon(profile);
        
        setUserProfile(profile, session);

        //  User is already authenticated by single sign-on - Forward to Welcome Page
        fwd = mapping.findForward(FORWARD_WELCOME_PAGE);
    }
    else {
        //  A)  Auto Authenticate should put user at login page so they can now login
        fwd = mapping.findForward(FORWARD_LOGIN_SCREEN);
    }

    log.debug("EXITING doAuthenticate()");
    
    return fwd;
}

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
    
    addError(errKey,propertyFileKey,dynamicCause,req,false);

}

/**
 * Add ActionErrors to request object
 * - Get Cause Message from Message Key (if boolean is true)
 * @param String errKey (for JSP Screen)
 * @param String propertyFileKey (for Text File)
 * @param String dynamicMessage (for rootCause)
 * @param HttpServletRequest req
 * @param boolean dynaCauseIsKeyed
 * @return void
*/
protected void addError(String errKey, String propertyFileKey, Object dynamicCause, HttpServletRequest req, boolean dynaCauseIsKeyed) {
    
    ResourceBundle rb = ResourceBundle.getBundle(LACSDWebConstants.MESSAGE_RESOURCES_FILE, new Locale("en","US"));
    
    ActionMessages errors = new ActionMessages();
    ActionMessage error = null;
    if (dynaCauseIsKeyed) {
        String cause = rb.getString(dynamicCause.toString());
        error = new ActionMessage(propertyFileKey,cause);
    }
    else {
        error = new ActionMessage(propertyFileKey,dynamicCause);
    }
    
    errors.add(errKey,error);
    this.saveMessages(req,errors);
    
    String message = rb.getString(propertyFileKey);
    req.setAttribute(LACSDWebConstants.ERROR_MESSAGE,message);
    if (dynamicCause != null) {
        
        if (dynaCauseIsKeyed) {
            String cause = rb.getString(dynamicCause.toString());
            req.setAttribute(LACSDWebConstants.ERROR_ROOT_CAUSE,cause);
        }
        else {
            req.setAttribute(LACSDWebConstants.ERROR_ROOT_CAUSE,dynamicCause.toString());
        }
    }
}

/**
 * Update the UserProfile object.  This object lives in the User's HTTPSESSION
 * @param UserProfile profile
 * @param HttpServletRequest req
 * @return void
*/
private void setUserProfile(UserProfile profile, HttpSession session) {
    session.setAttribute(LACSDWebConstants.USER_PROFILE,profile);
}

/**
 * Retrieve the UserProfile object.  This object lives in the User's HTTPSESSION
 * @param HttpSession session
 * @return UserProfile
*/
@SuppressWarnings("unused")
private UserProfile getUserProfile(HttpSession session) {
    UserProfile userProfile = null;
    Object upObject = session.getAttribute(LACSDWebConstants.USER_PROFILE);
    if (upObject != null) {
        userProfile = (UserProfile)upObject;
    }
    return userProfile;
}       

/**
 * Check to see if a user needs to login again when he/she is using the share station
 * @param profile
 * @param req
 * @param sessionID
 * @return
 * @throws Exception
 */
private boolean forceToLogout(UserProfile profile, HttpServletRequest req, String sessionID)  throws Exception{
	//If user logged in from the share station, force user to log in again if it is a new session
	SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    String machineLanID = (req.getParameter("userName")==null?"":req.getParameter("userName").trim());
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