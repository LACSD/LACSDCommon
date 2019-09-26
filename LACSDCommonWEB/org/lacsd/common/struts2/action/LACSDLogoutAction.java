package org.lacsd.common.struts2.action;

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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.lacsd.common.authentication.LACSDAuthenticationPO;
import org.lacsd.common.authentication.UserProfile;
import org.lacsd.common.constants.LACSDWebConstants;
import org.lacsd.common.exceptions.LACSDException;


public class LACSDLogoutAction extends  LACSDGenericAction{

private static final long serialVersionUID = 1L;

public static final String ACTION_NAME_LOGOUT       = "logout";

//super class abstract method
public void initErrControl() {
	//not implement
}

//super class abstract method
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
public String executeApplicationGlobal() throws LACSDException{
	String fwd = null;
    
    log.debug("ENTERING executeWithResult()");
   
    if(actionName.equalsIgnoreCase(ACTION_NAME_LOGOUT)) {
    	try {
    		fwd = doLogout();
    	}catch(Throwable t) {
    		throw new LACSDException(t.getMessage(),t);
    	}        
    }
    
    log.debug("EXITING executeWithResult()");

    return fwd;
}

//****************************************************************//

/**
 * Remove User from Logged-in Session
 * @param ActionMapping mapping
 * @return ActionForward
 * @throws Throwable
 * @throws LACSDException
 */
private String doLogout() throws  Throwable, LACSDException{
	return doLogout(request);
}

/**
 * Remove User from Logged-in Session
 * @param ActionMapping mapping
 * @param HttpServletRequest req
 * @return ActionForward
 * @throws Throwable
 * @throws LACSDException
 */
public String doLogout(HttpServletRequest request) throws  Throwable, LACSDException{

    log.debug("ENTERING doLogout()");

    LACSDAuthenticationPO authenticationPO = new LACSDAuthenticationPO();

    HttpSession session = request.getSession();
    UserProfile profile = (UserProfile)session.getAttribute(LACSDWebConstants.USER_PROFILE);

    //  Remove User from Single Sign-On
    //  Note:   This removal only deletes from database if 
    //          the current SessionID owns the signon.
    if (profile  != null) {
    	authenticationPO.forceRemoveSignon(profile);
    }

    log.debug("EXITING doLogout()");
    
    return LACSDWebConstants.FORWARD_LOGIN_SCREEN;
}

}