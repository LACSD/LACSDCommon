package org.lacsd.common.authentication;

/******************************************************************************
//* Copyright (c) 2004 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		UserProfile.java
//* Revision: 		1.1
//* Author:			MFEINBERG@LACSD.ORG
//* Created On: 	02-20-2004
//* Modified By:	
//* Modified On:	
//*					
//* Description:	Designed to be extensible, this interface allows for multiple
//* 				applications to maintain different types of user profile objects
//* 				while still using the core LACSD Authentication and multithreading
//* 				functionality.
/******************************************************************************/

import org.lacsd.common.values.EmployeeVO;

public interface UserProfile extends java.io.Serializable {

    
/**
 * return the BypassSecurity
 * @return boolean
 */    
public boolean isBypassSecurity();
/**
 * Returns the authenticated.
 * @return boolean
 */
public boolean isAuthenticated();

/**
 * Returns the employeeVO.
 * @return EmployeeVO
 */
public EmployeeVO getEmployeeVO();

/**
 * Returns the httpSessionID.
 * @return String
 */
public String getHttpSessionID();

/**
 * Returns the iPAddress.
 * @return String
 */
public String getIPAddress();

/**
 * Returns the loggedIN.
 * @return boolean
 */
public boolean isLoggedIN();

/**
 * Returns the password.
 * @return String
 */
public String getPassword();

/**
 * Returns the userID.
 * @return String
 */
public String getUserID();

/**
 * Returns the userName.
 * @return String
 */
public String getUserName();

/**
 * Sets the authenticated.
 * @param authenticated The authenticated to set
 */
public void setAuthenticated(boolean authenticated);

/**
 * Sets the employeeVO.
 * @param employeeVO The employeeVO to set
 */
public void setEmployeeVO(EmployeeVO employeeVO);

/**
 * Sets the httpSessionID.
 * @param httpSessionID The httpSessionID to set
 */
public void setHttpSessionID(String httpSessionID);

/**
 * Sets the iPAddress.
 * @param iPAddress The iPAddress to set
 */
public void setIPAddress(String iPAddress);

/**
 * Sets the loggedIN.
 * @param loggedIN The loggedIN to set
 */
public void setLoggedIN(boolean loggedIN);

/**
 * Sets the password.
 * @param password The password to set
 */
public void setPassword(String password);

/**
 * Sets the userID.
 * @param userID The userID to set
 */
public void setUserID(String userID);

/**
 * Sets the userName.
 * @param userName The userName to set
 */
public void setUserName(String userName);

/**
 * Set the  BypassSecurity
 * @param isBypassSecurity
 */
public void setBypassSecurity(boolean isBypassSecurity);



public String getLastLogin() ;
public void setLastLogin(String lastLogin) ;
}

