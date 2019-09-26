package org.lacsd.common.action;

/******************************************************************************
//* Copyright (c) 2003 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:       LACSDLoginForm.java
//* Revision:       1.3
//* Author:         MFEINBERG@LACSD.ORG
//* Created On:     10-15-03
//* Modified By:    tnguyen@lacsd.org
//* Modified On:    07-14-2004
//*                 Change to generic form for all applications
//*                 
//* Description:    Struts Form for Login Screen
/******************************************************************************/

import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.*;
import org.lacsd.common.authentication.LACSDUser;
import org.lacsd.common.authentication.UserProfile;
import org.lacsd.common.constants.LACSDWebConstants;
import org.lacsd.common.util.ApplicationContext;
import org.lacsd.common.values.EmployeeRoleVO;
import org.lacsd.common.values.EmployeeVO;

public class LACSDLoginForm extends ActionForm {

private static final long serialVersionUID = -2837337210300960197L;
protected String actionName;
protected UserProfile userProfile;

/**
 * Reset Method
 * @param ActionMapping mapping
 * @param HttpServletRequest req
 * @return void
*/
public void reset(ActionMapping mapping, HttpServletRequest req) {    
    LACSDUser user = new LACSDUser();
    reset( user );  
}

/**
 * Reset Method
 * @param LACSDUser user
 * @return void
*/
protected void reset(LACSDUser user) {
    EmployeeVO employeeVO = new EmployeeVO();
    
    // Added 04-09-2004! - Add application ID to EmployeeRoleVO
    // SPECIFICALLY:  This comes into play when the authentication piece calls "Get Detail" and
    // looks up this employee's "securityRoleID".  Security RoleID is unique to every application   
    ApplicationContext appCtx = ApplicationContext.getInstance();
    EmployeeRoleVO employeeRoleVO = new EmployeeRoleVO();
    employeeRoleVO.setApplicationID(appCtx.get(LACSDWebConstants.APPLICATION_ID));
    employeeVO.setEmployeeRoleVO(employeeRoleVO);
    
    user.setEmployeeVO(employeeVO);
    
    // Initialize UserProfile Interface
    userProfile = user;
}


/**
 * Validate Method
 * @return ActionErrors
 * @param ActionMapping mapping
 * @param HttpServletRequest req
*/
public ActionErrors validate(ActionMapping mapping, HttpServletRequest req) {

    ActionErrors errors = new ActionErrors();

    

    if (actionName.equalsIgnoreCase(LACSDLoginAction.ACTION_NAME_AUTH)) {
    
        //  1)  Validate EmployeeID Field
        if ((userProfile.getUserName() == null) || (userProfile.getUserName().equals(""))) {
            ActionMessage error = new ActionMessage(LACSDLoginAction.MESSAGE_LOGIN_ID_BLANK);
            errors.add("error",error);
            this.addError("error",LACSDLoginAction.MESSAGE_LOGIN_ID_BLANK,req);
            return errors;
        }
        
        /** Do not enforce numeric check
        try {
            int numCheck = Integer.parseInt(userProfile.getUserID());
        }
        catch (NumberFormatException nfe) {
            ActionMessage error = new ActionMessage(LoginAction.MESSAGE_LOGIN_ID_NUMBER);
            errors.add("error",error);
            this.addError("error",LoginAction.MESSAGE_LOGIN_ID_NUMBER,req);
            return errors;
        }
        **/
            
        //  2)  Validate Password Field
        if ((userProfile.getPassword() == null) || (userProfile.getPassword().equals(""))) {
            ActionMessage error = new ActionMessage(LACSDLoginAction.MESSAGE_LOGIN_PASSWD_BLANK);
            errors.add("error",error);
            this.addError("error",LACSDLoginAction.MESSAGE_LOGIN_PASSWD_BLANK,req);
            return errors;
        }
    }

    return errors;
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

    ResourceBundle rb = ResourceBundle.getBundle(LACSDWebConstants.MESSAGE_RESOURCES_FILE, new Locale("en","US"));
    String message = rb.getString(propertyFileKey);
    req.setAttribute(LACSDWebConstants.ERROR_MESSAGE,message);
    if (dynamicCause != null) {
        req.setAttribute(LACSDWebConstants.ERROR_ROOT_CAUSE,dynamicCause.toString());
    }
}

/**
 * Returns the userProfile.
 * @return UserProfile
 */
public UserProfile getUserProfile() {
    return userProfile;
}

/**
 * Sets the userProfile.
 * @param userProfile The userProfile to set
 */
public void setUserProfile(UserProfile userProfile) {
    this.userProfile = userProfile;
}
/**
 * Returns the actionName.
 * @return String
 */
public String getActionName() {
    return actionName;
}

/**
 * Sets the actionName.
 * @param actionName The actionName to set
 */
public void setActionName(String actionName) {
    this.actionName = actionName;
}
}