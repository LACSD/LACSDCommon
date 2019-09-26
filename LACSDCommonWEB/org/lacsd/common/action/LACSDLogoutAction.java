package org.lacsd.common.action;

/******************************************************************************
//* Copyright (c) 2003 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:       LACSDLogoutAction.java
//* Revision:       1.4
//* Author:         MFEINBERG@LACSD.ORG
//* Created On:     10-20-2003
//* Modified By:    tnguyen@lacsd.org
//* Modified On:    07-14-2004
//*                 Change to generic action for all applications
//*                 03-01-2005 tnguyen@lacsd.org
//*                 Inherit Struts Action instead of LACSDGenericAction
//*                 
//* Description:    Log user out of Single Sign-On System
/******************************************************************************/

import java.io.IOException;
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

public class LACSDLogoutAction extends Action {

private final String CLASS_NAME = this.getClass().getName();
protected Logger log = LogManager.getLogger(CLASS_NAME);
    
public static final String ACTION_NAME_LOGOUT       = "logout";

public static final String FORWARD_PAGE_SETUP =         "setup";
public static final String FORWARD_LOGIN_SCREEN =       "login-screen";
public static final String FORWARD_WELCOME_PAGE =       "welcome-page";
public static final String FORWARD_GLOBAL_FAILURE =     "global-failure";

public static final String FORWARD_APP_FAILURE =        "app-failure";
public static final String FORWARD_DB2_FAILURE =        "db2-failure";
public static final String FORWARD_SQL2K_FAILURE =      "sql2k-failure";
public static final String FORWARD_ECI_FAILURE =        "eci-failure";

/** GLOBAL ERROR SET **/
public static final String MESSAGE_APP_FAILURE =        "error.app.failure";
public static final String MESSAGE_DB2_FAILURE =        "error.db2.failure";
public static final String MESSAGE_SQL2K_FAILURE =      "error.sql2k.failure";
public static final String MESSAGE_ECI_FAILURE =        "error.eci.failure";
public static final String MESSAGE_FAILED_MULTIVALIDATE = "error.failed.multivalidate";
public static final String MESSAGE_FRAMEWORK_NOTUSED =  "error.framework.notused";
public static final String MESSAGE_BAD_ACTIONFORWARD =  "error.badactionfwd";
public static final String MESSAGE_RECOVERABLE_NULL =   "error.recoverable.notfound";
/** GLOBAL ERROR SET **/


/**
 * Primary (TOP LEVEL) Execute Method - Jakarta Struts Framework v1.1
 * @param ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletReponse res
 * @return ActionForward
 * @throws IOException, ServletException
*/
public ActionForward execute(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res) throws IOException, ServletException {
    ActionForward fwd = null;
    
    
    log.debug("ENTERING executeWithResult()");

    LACSDLogoutForm logoutForm = (LACSDLogoutForm)form;
    String actionName = logoutForm.getActionName();

    if(actionName.equalsIgnoreCase(ACTION_NAME_LOGOUT)) {
        fwd = doLogout(mapping,req);
    }
    
    log.debug("EXITING executeWithResult()");

    return fwd;
}

//****************************************************************//

/**
 * Remove User from Logged-in Session
 * @param ActionMapping mapping
 * @param HttpServletRequest req
 * @return ActionForward
 * @throws LACSDException
*/
private ActionForward doLogout(ActionMapping mapping, HttpServletRequest req) {

    log.debug("ENTERING doLogout()");

    LACSDAuthenticationPO authenticationPO = new LACSDAuthenticationPO();
    
    HttpSession session = req.getSession();
    UserProfile profile = (UserProfile)session.getAttribute(LACSDWebConstants.USER_PROFILE);
    
    if ( profile == null ) {            
        return mapping.findForward(FORWARD_LOGIN_SCREEN);
    }
    
    //  Remove User from Single Sign-On
    //  Note:   This removal only deletes from database if 
    //          the current SessionID owns the signon.
    try {
        authenticationPO.forceRemoveSignon(profile);
    }
    //  HANDLE ERROR CONDITIONS
    //****************************************************************************/
    catch (LACSDException e) {
            
        // determine if recoverable or not
        if (e.isRecoverable()) {
                
            this.addError("nonrecoverable",MESSAGE_APP_FAILURE,MESSAGE_RECOVERABLE_NULL,req,true);
            return mapping.findForward(FORWARD_APP_FAILURE);
        }
        else {
            log.error(e.getMessage(),e.getRootCause());
                
            if (e instanceof LACSDDb2Exception) {
                this.addError("nonrecoverable",MESSAGE_DB2_FAILURE,e.getRootCause(),req);
                return mapping.findForward(FORWARD_DB2_FAILURE);
            }
            else if (e instanceof LACSDSqlSrvr2KException) {
                this.addError("nonrecoverable",MESSAGE_SQL2K_FAILURE,e.getRootCause(),req);
                return mapping.findForward(FORWARD_SQL2K_FAILURE);
            }
            else if (e instanceof LACSDEciException) {
                this.addError("nonrecoverable",MESSAGE_ECI_FAILURE,e.getRootCause(),req);
                return mapping.findForward(FORWARD_ECI_FAILURE);
            }
            else {
                this.addError("nonrecoverable",MESSAGE_APP_FAILURE,e.getRootCause(),req);
                return mapping.findForward(FORWARD_APP_FAILURE);
            }
        }
    }
    catch (Throwable t) {   //  This would happen as a result of EJB Failure throwing throwable
                            //  or any other non-expected runtime exception in the application
        log.error(t.getMessage(),t);
        this.addError("nonrecoverable",MESSAGE_APP_FAILURE,t.getMessage(),req);
        return mapping.findForward(FORWARD_APP_FAILURE);
    }
    
    
    
    log.debug("EXITING doLogout()");
    
    return mapping.findForward(FORWARD_LOGIN_SCREEN);
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


}

