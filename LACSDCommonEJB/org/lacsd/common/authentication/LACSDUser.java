package org.lacsd.common.authentication;

/******************************************************************************
//* Copyright (c) 2003 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		LACSDUser.java
//* Revision: 		1.7
//* Author:			MFEINBERG@LACSD.ORG
//* Created On: 	09-24-03
//* Modified By:	MFEINBERG@LACSD.ORG
//* Modified On:	11-07-03
//*					
//* Description:	User Profile (Security) represents a user's login session.
//* 				Scope: HTTPSession Context
//* 
//* Modification:	User Profile becomes Runnable Object.  Thread will determine
//* 				periodically whether User Profile is still valid in single 
//* 				sign-on tables.   This modification in effort to save 
//* 				network bandwidth.
/******************************************************************************/

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.lacsd.common.constants.LACSDWebConstants;
import org.lacsd.common.exceptions.LACSDException;
import org.lacsd.common.values.EmployeeVO;

public class LACSDUser implements UserProfile, java.io.Serializable {

private static final long serialVersionUID = -5877780982123984151L;

private volatile String userID;					//	OSX EmployeeID Security Details
private volatile String userName;               //  OSX UserName Security Details
private volatile String password;
private volatile String IPAddress;
private volatile String httpSessionID;

private volatile boolean authenticated = false;
private volatile boolean loggedIN = false;
private volatile boolean bypassSecurity = false;
private String lastLogin;


private volatile EmployeeVO employeeVO; 

private final String CLASS_NAME = this.getClass().getName(); 

private String multiValidValue; 

/**
 * Default Constructor
*/
public LACSDUser() {
	super();
}

/**
 * RUN Method is base execution frame for single sign-on check
 * @return void
*/
public void run() {

	while (this.isLoggedIN()) {
		
		try {
			Thread.sleep(LACSDWebConstants.USER_PROFILE_PAUSE_INTERVAL * 1000);
			checkLogin();
		}
		catch(Throwable t) {
			this.setLoggedIN(false);
			Logger log = LogManager.getLogger(this.CLASS_NAME);;
			log.error(t.getMessage());
		}
		
		//  Session cleaner may have invalidated this session - check for termination
		SessionCleaner cleaner = SessionCleaner.getInstance();
		if (cleaner.isStaleSession(this.IPAddress)) {
			cleaner.removeStaleSession(this.IPAddress);
			break;
		}
	}
	
	//	Outside of while loop - This Thread is destroyed
	//	Note: Remove Chained Objects (employeeRoleVO)
	
	Logger log = LogManager.getLogger(CLASS_NAME);
	log.debug("Thread Terminated: " + this.getHttpSessionID());

}

/**
 * User Profile Object is responsible for querying the database
 * periodically to determine whether or not it is still a valid
 * member of the single sign-on.   If no longer part of the single
 * sign-on database table,  the object will destroy itself.
 * @return void
 * @throws LACSDException, Throwable
*/
private synchronized void checkLogin() throws LACSDException, Throwable {


	LACSDAuthenticationPO lACSDAuthenticationPO = new LACSDAuthenticationPO();
	
	//	1)	Query Stored Procedure to validate login status
	LACSDUser userProfile = (LACSDUser)lACSDAuthenticationPO.verifyLogin(this);
	refreshFields(userProfile);	//	DAO on EJB-Tier breaks referential integrity, - SELF UPDATE HERE

	//	2)	Update single sign-on to show that the app was the last application to touch it
	if(this.isLoggedIN()) {
		
		//	A)	Touch Database to show the app as last application owner
		lACSDAuthenticationPO.updateSignon(this);
		
		//	B)	Refresh UserProfile version of security roles
		//	employeeRoleVO.setRoleID(this.securityRoleID);
		//	RoleActivityDAORemoteAccessBean roleActivityDAO = new RoleActivityDAORemoteAccessBean();
		//	employeeRoleVO = roleActivityDAO.getRoleDetail(employeeRoleVO);
	}

}

/**
 * Refresh-Self
 * @param UserProfile userProfile
 * @return void
*/
private void refreshFields(LACSDUser userProfile) {

	this.setAuthenticated(userProfile.authenticated);

	this.setUserID(userProfile.userID);
	this.setPassword(userProfile.password);
	this.setHttpSessionID(userProfile.httpSessionID);
	this.setIPAddress(userProfile.IPAddress);
	this.setLoggedIN(userProfile.loggedIN);

	this.employeeVO.setFirstName(userProfile.getEmployeeVO().getFirstName());
	this.employeeVO.setLastName(userProfile.getEmployeeVO().getLastName());
	this.employeeVO.setEmailAddress(userProfile.getEmployeeVO().getEmailAddress());
	this.employeeVO.setSecurityRoleID(userProfile.getEmployeeVO().getSecurityRoleID());
}

/**
 * valueBound() method Implementation of HttpSessionBinding Interface
 * Occurs when HTTPSession is acquired.
 * @param HttpSessionBindingEvent arg1
 * @return void
*/
public void valueBound(javax.servlet.http.HttpSessionBindingEvent arg1) {

	/** METHOD NOT IMPLEMENTED **/
}

/**
 * valueUnbound() method Implementation of HttpSessionBinding Interface
 * Occurs when HTTPSession is released.   Attemps to logoff user from Single Sign-on
 * @param HttpSessionBindingEvent arg1
 * @return void
*/
public void valueUnbound(javax.servlet.http.HttpSessionBindingEvent arg1) {
	
	this.setLoggedIN(false);
	
	try {
		//	EJB DAO Implementation	
		LACSDAuthenticationPO lACSDAuthenticationPO = new LACSDAuthenticationPO();
		lACSDAuthenticationPO.removeSignon(this);
	}
	catch(Throwable t) {
		Logger log = LogManager.getLogger(CLASS_NAME);
		log.error(t.getMessage());
	}
}



/**
 * Returns the authenticated.
 * @return boolean
 */
public boolean isAuthenticated() {
	return authenticated;
}

/**
 * Returns the employeeVO.
 * @return EmployeeVO
 */
public EmployeeVO getEmployeeVO() {
	return employeeVO;
}

/**
 * Returns the httpSessionID.
 * @return String
 */
public String getHttpSessionID() {
	return httpSessionID;
}

/**
 * Returns the iPAddress.
 * @return String
 */
public String getIPAddress() {
	return IPAddress;
}

/**
 * Returns the loggedIN.
 * @return boolean
 */
public boolean isLoggedIN() {
	return loggedIN;
}

/**
 * Returns the password.
 * @return String
 */
public String getPassword() {
	return password;
}

/**
 * Returns the userID.
 * @return String
 */
public String getUserID() {
	return userID;
}

/**
 * Returns the userName.
 * @return String
 */
public String getUserName() {
    return userName;
}

/**
 * Sets the authenticated.
 * @param authenticated The authenticated to set
 */
public void setAuthenticated(boolean authenticated) {
	this.authenticated = authenticated;
}

/**
 * Sets the employeeVO.
 * @param employeeVO The employeeVO to set
 */
public void setEmployeeVO(EmployeeVO employeeVO) {
	this.employeeVO = employeeVO;
}

/**
 * Sets the httpSessionID.
 * @param httpSessionID The httpSessionID to set
 */
public void setHttpSessionID(String httpSessionID) {
	this.httpSessionID = httpSessionID;
}

/**
 * Sets the iPAddress.
 * @param iPAddress The iPAddress to set
 */
public void setIPAddress(String iPAddress) {
	IPAddress = iPAddress;
}

/**
 * Sets the loggedIN.
 * @param loggedIN The loggedIN to set
 */
public void setLoggedIN(boolean loggedIN) {
	this.loggedIN = loggedIN;
}

/**
 * Sets the password.
 * @param password The password to set
 */
public void setPassword(String password) {
	this.password = password;
}

/**
 * Sets the userID.
 * @param userID The userID to set
 */
public void setUserID(String userID) {
	this.userID = userID;
}

/**
 * Sets the userName.
 * @param userName The userName to set
 */
public void setUserName(String userName) {
    this.userName = userName;
}

/** 
 * Returns the value for multiValidValue 
 * @return String
 */
public String getMultiValidValue(){
    return multiValidValue;
} 

/** 
 * Sets the value for multiValidValue 
 * @param multiValidValue the multiValidValue to set 
 */ 
public void setMultiValidValue(String multiValidValue) {
    this.multiValidValue = multiValidValue;
}

/**
 * @return Returns the bypassSecurity.
 */
public boolean isBypassSecurity() {
    return bypassSecurity;
}
/**
 * @param bypassSecurity The bypassSecurity to set.
 */
public void setBypassSecurity(boolean bypassSecurity) {
    this.bypassSecurity = bypassSecurity;
}



public String getLastLogin()  {
	return lastLogin;
}
public void setLastLogin(String lastLogin)  {
	this.lastLogin = lastLogin;
}
}
