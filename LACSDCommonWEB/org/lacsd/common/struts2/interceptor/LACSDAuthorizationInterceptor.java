package org.lacsd.common.struts2.interceptor;

/******************************************************************************
//* Copyright (c) 2010 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:       LACSDAuthorizationInterceptor.java
//* Revision:       1.0
//* Author:         Hoa Ho
//* Created On:     06-11-2010
//* Modified by:    
//* Modified On:      
//*                 
//* Description:   Authorization Interceptor
//*                
/******************************************************************************/

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.apache.struts2.StrutsStatics;
import org.lacsd.common.authentication.LACSDAuthenticationPO;
import org.lacsd.common.authentication.LACSDUser;
import org.lacsd.common.authentication.UserProfile;
import org.lacsd.common.constants.LACSDWebConstants;
import org.lacsd.common.exceptions.LACSDException;
import org.lacsd.common.security.SecurityRoleMap;
import org.lacsd.common.util.ApplicationContext;
import org.lacsd.common.util.LACSDErrorAnalyzer;
import org.lacsd.common.values.EmployeeRoleVO;
import org.lacsd.common.values.EmployeeVO;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class LACSDAuthorizationInterceptor extends AbstractInterceptor implements StrutsStatics{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3016197521488610439L;
	private final String CLASS_NAME = this.getClass().getName();
	protected Logger log = LogManager.getLogger(CLASS_NAME);
	private ActionContext context;

	public void destroy() {
	}

	public void init() {
	}

	public String intercept( ActionInvocation actionInvocation ) throws Exception {
		LACSDErrorAnalyzer errorAnalyzer = new LACSDErrorAnalyzer();
		context = actionInvocation.getInvocationContext ();
	    HttpServletRequest request = (HttpServletRequest) context.get(HTTP_REQUEST);
	    HttpSession session =  request.getSession (true);
	    UserProfile profile = null;
	    if (session.getAttribute(LACSDWebConstants.USER_PROFILE) != null) {
	       profile = (UserProfile)session.getAttribute(LACSDWebConstants.USER_PROFILE);
	    }
	    String actionName = request.getParameter("actionName");
	    if (actionName == null ) {
        	errorAnalyzer.addError(LACSDWebConstants.ERROR_MESSAGE,LACSDWebConstants.MESSAGE_BAD_ACTIONFORWARD,  request);
            return LACSDWebConstants.FORWARD_APP_FAILURE;
	    }
	    String callingPage = request.getParameter("callingPage");
	    if (profile != null) {

	    	try {
	    		// Check against the database to make sure 
	    		// that the session is really still active
	        	LACSDAuthenticationPO authenticationPO = new LACSDAuthenticationPO();
	        	
	        	if (!profile.isBypassSecurity()) {
	        	    profile = authenticationPO.verifyLogin(profile);
	        	}

	            //  A)  Check if UserProfile is Logged In
	            if (!profile.isLoggedIN()) {
	                request.getSession().invalidate();  //  Kill HTTPSession
	                return LACSDWebConstants.FORWARD_LOGIN_SCREEN;
	            }
	    	} catch (Throwable t) {
	    		log.error(t.getMessage(),t);
	            if (t instanceof LACSDException) {
	                LACSDException converted = (LACSDException)t;
	                return errorAnalyzer.examineLACSDException(converted, request);
	            }
	            else {
	            	errorAnalyzer.addError(LACSDWebConstants.ERROR_MESSAGE,LACSDWebConstants.MESSAGE_APP_FAILURE,t.getMessage() + "\n" + t.fillInStackTrace(), request);
	                return LACSDWebConstants.FORWARD_APP_FAILURE;
	            }
	    	}
	    }
	    else {

	    	// if profile is null,
	    	// verify log-in from DB2
	        String ipaddress = request.getRemoteAddr();
	        if (ipaddress.equals("127.0.0.1")) {
	            ipaddress = java.net.InetAddress.getLocalHost().getHostAddress();
	        }
	        // set up variables to verify log-in
	        profile = new LACSDUser();
	        profile.setIPAddress(ipaddress);
	        profile.setHttpSessionID(request.getSession(true).getId());

	        EmployeeVO employeeVO = new EmployeeVO();
	        ApplicationContext appCtx = ApplicationContext.getInstance();
	        EmployeeRoleVO employeeRoleVO = new EmployeeRoleVO();
	        employeeRoleVO.setApplicationID(appCtx.get(LACSDWebConstants.APPLICATION_ID));
	        employeeVO.setEmployeeRoleVO(employeeRoleVO);
	        profile.setEmployeeVO(employeeVO);

	        LACSDAuthenticationPO authenticationPO = new LACSDAuthenticationPO();
	        try {
	            profile = authenticationPO.verifyLogin(profile);
	        } catch (Throwable t) {
	            if (t instanceof LACSDException) {
	                LACSDException converted = (LACSDException)t;
	                return errorAnalyzer.examineLACSDException(converted, request);
	            }
	            else {
	            	log.error(t.getMessage(),t);
	            	errorAnalyzer.addError(LACSDWebConstants.ERROR_MESSAGE,LACSDWebConstants.MESSAGE_APP_FAILURE,t.getMessage() + "\n" + t.fillInStackTrace(), request);
	                return LACSDWebConstants.FORWARD_APP_FAILURE;
	            }
	        }

	        if (! profile.isLoggedIN()) {
	            request.getSession().invalidate();  //  Kill HTTPSession
	            //  B)  UserProfile object not found in memory - Forward to Login Action
	            return LACSDWebConstants.FORWARD_LOGIN_ACTION;
	            //  return mapping.findForward(FORWARD_LOGIN_SCREEN);
	        }
	    }
	    session.setAttribute(LACSDWebConstants.USER_PROFILE, profile);
	    //  4)  ENFORCE ROLE-BASED SECURITY FOR A PARTICULAR ACTIVITY
	    //****************************************************************************/

	    try {
	        //  Acquire Security Role Map - Compare this user's ROLEID against the role map!
	        SecurityRoleMap securityRoleMap = SecurityRoleMap.getInstance();
	        String servletPath = request.getPathInfo();
	        servletPath = servletPath.substring(servletPath.lastIndexOf("/")+1);
	        profile.getEmployeeVO().getEmployeeRoleVO().getApplicationID();
	        	   
	        boolean permitted = false;
	        if (profile.isBypassSecurity()) {
	            permitted = true;
	        } else {
	            permitted = securityRoleMap.isPermittedStruts(servletPath, actionName, profile.getEmployeeVO().getEmployeeRoleVO());
	        }
	        if ((!permitted) && (actionName != null) && (!actionName.equalsIgnoreCase("legacy"))) {
	        	 //String activityDesc = new LACSDActivityPO().getActivitieDescription(applicationID,servletPath,actionName);
	        	 errorAnalyzer.addError(LACSDWebConstants.ERROR_MESSAGE,LACSDWebConstants.MESSAGE_SECURITY_NO_PERMIT,null,request);
	             
	            if ( callingPage != null ) {
	                return callingPage;
	            } else {
	                return LACSDWebConstants.FORWARD_WELCOME_PAGE;
	            }
	        }
	    }
	    catch(Throwable t) {
	        if (t instanceof LACSDException) {
	            LACSDException converted = (LACSDException)t;
	            return errorAnalyzer.examineLACSDException(converted, request);
	        }
	        else {
	            log.error(t.getMessage(),t);
	            errorAnalyzer.addError(LACSDWebConstants.ERROR_MESSAGE,LACSDWebConstants.MESSAGE_APP_FAILURE,t.getMessage() + "\n" + t.fillInStackTrace(),  request);
	            return LACSDWebConstants.FORWARD_APP_FAILURE;
	        }
	    }


		return actionInvocation.invoke();
	}
	
	
	
}
