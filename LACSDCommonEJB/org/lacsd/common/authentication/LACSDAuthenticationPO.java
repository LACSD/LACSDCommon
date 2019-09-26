package org.lacsd.common.authentication;

/******************************************************************************
//* Copyright (c) 2003 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		LACSDAuthenticationPO.java
//* Revision: 		1.1
//* Author:			MFEINBERG@LACSD.ORG
//* Created On: 	02-19-2004
//* Modified By:	
//* Modified On:	
//*					
//* Description:	Proxy object for LACSD Authentication Source
//* 
/******************************************************************************/

import org.lacsd.common.constants.LACSDJNDIConstants;
import org.lacsd.common.exceptions.LACSDException;
import org.lacsd.common.helper.LACSDEJBFinder;
import org.lacsd.common.util.ApplicationContext;
import org.lacsd.common.values.EmployeeRoleVO;
import org.lacsd.osx.business.ejb.view.ADBOBeanRemote;
import org.lacsd.osx.dao.ejb.view.AuthenticationDAOBeanRemote;
import org.lacsd.osx.dao.ejb.view.RoleActivityDAOBeanRemote;

public class LACSDAuthenticationPO {

private static final String JNDI_ADBO                   = "ejb/ADBO";
private static final String JNDI_AUTHENTICATIONDAO      = "ejb/AuthenticationDAO";
private static final String JNDI_ROLEACTIVITYDA0      = "ejb/RoleActivityDAO";

private String remoteURL;


private boolean isActiveDirectory = false;
	
	public LACSDAuthenticationPO() {
		ApplicationContext appctx = ApplicationContext.getInstance();
		 remoteURL = appctx.get(LACSDJNDIConstants.REMOTE_IIOP_URL_RESOURCE);
		 
	}
	
	/**
	* Verify Login - Inquires against the single sign-on tables and
	* sets the boolean flag "isLoggedIN" in the UserProfile.
	* @param UserProfile userProfile
	* @return UserProfile
	* @throws LACSDException
	*/
	public boolean isPermittedStruts(String securityRoleID, String servletPath, String actionName, String applicationID) throws LACSDException, Throwable {
	    return this.getAuthenticationDAOBean().isPermittedStruts(securityRoleID, servletPath, actionName, applicationID);
	}
	
	
	/**
	* Verify Login - Inquires against the single sign-on tables and
	* sets the boolean flag "isLoggedIN" in the UserProfile.
	* @param UserProfile userProfile
	* @return UserProfile
	* @throws LACSDException
	*/
	public UserProfile verifyLogin(UserProfile userProfile) throws LACSDException, Throwable {
		userProfile = getAuthenticationDAOBean().verifyLogin(userProfile);
		if (userProfile.isAuthenticated()) {
        	userProfile = getRoleGrantedActivities(userProfile);
        }
		return userProfile;
	}
	
	/**
	* Get User Details populates the UserProfile object with detailed
	* information about the LACSD employee.  example:  Name and Email Address.
	* @param UserProfile userProfile
	* @return UserProfile
	* @throws LACSDException
	*/
	public UserProfile getUserDetail(UserProfile userProfile) throws LACSDException, Throwable {
		return this.getAuthenticationDAOBean().getUserDetail(userProfile);
	}
	
	/**
	* Authenticate Username and Password.  Note:  Passwords received by this
	* Data Access Object in plain text are subsequently converted into Base64 encoded
	* format before being passed to the stored procedure.  The boolean flag "isAuthenticated"
	* in the UserProfile object will be set.
	* @param UserProfile userProfile
	* @return UserProfile
	* @throws LACSDException
	*/
	public UserProfile doAuthenticate(UserProfile userProfile) throws LACSDException, Throwable {
		return this.getAuthenticationDAOBean().doAuthenticate(userProfile);
	}
	
	/**
	* Add this LACSDEmployee to the single sign-on tables
	* @param UserProfile userProfile
	* @return UserProfile
	* @throws LACSDException
	*/
	public UserProfile addSignon(UserProfile userProfile) throws LACSDException, Throwable {
		return this.getAuthenticationDAOBean().addSignon(userProfile);
	}
	
	/**
	* Update the single sign-on tables to reflect that this application was
	* the last program touched by the logged-in user.   This touch is denoted
	* by HTTPSESSIONID.   This prevents other applications from logging a user
	* off of the single sign-on if their sessions timeout.
	* @param UserProfile userProfile
	* @return UserProfile
	* @throws LACSDException
	*/
	public UserProfile updateSignon(UserProfile userProfile) throws LACSDException, Throwable {
		return this.getAuthenticationDAOBean().updateSignon(userProfile);
	}
	
	/**
	* Remove current user from single sign-on tables,  logging them out of the system.
	* Log-off will only take effect if THIS application (by SESSIONID) was the last
	* program to touch the single sign-on system.
	* @param UserProfile userProfile
	* @return UserProfile
	* @throws LACSDException
	*/
	public UserProfile removeSignon(UserProfile userProfile) throws LACSDException, Throwable {
		return this.getAuthenticationDAOBean().removeSignon(userProfile);
	}
	
	/**
	* Lookup employeeID by IPAddress that is present in the OSXEmployeeSession table.
	* @param UserProfile userProfile
	* @return UserProfile
	* @throws LACSDException
	*/
	public UserProfile lookupEmployeeID(UserProfile userProfile) throws LACSDException, Throwable {
		return this.getAuthenticationDAOBean().lookupEmployeeID(userProfile);
	}
	
	/**
	* FORCE Remove current user from single sign-on tables,  logging them out of the system.
	* Log-off will only take effect regardless whether or not THIS application (by SESSIONID) 
	* was the last program to touch the single sign-on system.
	* @param UserProfile userProfile
	* @return UserProfile
	* @throws LACSDException
	*/
	public UserProfile forceRemoveSignon(UserProfile userProfile) throws LACSDException, Throwable {
		return this.getAuthenticationDAOBean().forceRemoveSignon(userProfile);
	}
	
	public UserProfile doADAuthenticate(UserProfile userProfile) throws LACSDException, Throwable {
		 Object remoteObj = LACSDEJBFinder.getRemoteEJB(remoteURL,JNDI_ADBO);
		 ADBOBeanRemote adBO = (ADBOBeanRemote)remoteObj;
	     return adBO.doAuthenticate(userProfile);
	}
	
	
	
	/**
	 * Returns a collection of all granted activities  with specified RoleId
	 * EmployeeRoleVO holds an ArrayList of itself
	 * @param EmployeeRoleVO employeeRoleVO
	 * @return EmployeeRoleVO
	 * @throws Throwable
	*/
	public UserProfile getRoleGrantedActivities(UserProfile userProfile) throws Throwable {
	
		Object remoteObj = LACSDEJBFinder.getRemoteEJB(remoteURL,JNDI_ROLEACTIVITYDA0);
		RoleActivityDAOBeanRemote roleActivityDAO = (RoleActivityDAOBeanRemote)remoteObj;
	    
	   	EmployeeRoleVO employeeRoleVO = userProfile.getEmployeeVO().getEmployeeRoleVO();
	   	employeeRoleVO.setRoleID(Integer.parseInt(userProfile.getEmployeeVO().getSecurityRoleID()));
	   	employeeRoleVO = roleActivityDAO.getRoleGrantedActivities(employeeRoleVO);
	   	userProfile.getEmployeeVO().setEmployeeRoleVO(employeeRoleVO);
	    return userProfile;
	}
	
	/**
	 * Returns the isActiveDirectory.
	 * @return boolean
	 */
	public boolean isActiveDirectory() {
		return isActiveDirectory;
	}
	
	
	private AuthenticationDAOBeanRemote getAuthenticationDAOBean () throws LACSDException, Throwable{
		 Object remoteObj = LACSDEJBFinder.getRemoteEJB(remoteURL,JNDI_AUTHENTICATIONDAO);
		 return ((AuthenticationDAOBeanRemote)remoteObj);
	}

}
