package org.lacsd.common.security;

/******************************************************************************
//* Copyright (c) 2003 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		SecurityRoleMap.java
//* Revision: 		1.0
//* Author:			MFEINBERG@LACSD.ORG
//* Created On: 	12-29-2003
//* Modified by:    tnguyen@lacsd.org	
//* Modified On:	02-25-2005 - turn on security if activity is not setup in OSX
//*					
//* Description:	Singleton object holds security Role Mapping
/******************************************************************************/

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.lacsd.common.values.ActivityVO;
import org.lacsd.common.values.EmployeeRoleVO;

public class SecurityRoleMap {

private static SecurityRoleMap _INSTANCE = new SecurityRoleMap();

private final String CLASS_NAME = this.getClass().getName();
private Logger log = LogManager.getLogger(CLASS_NAME);

private static final String SECURITY_RESOURCES  = "resources.tag_security_resources";	//	text file

public static final int NORMAL = 0;
public static final int READ_ONLY = 1;

private HashMap<String, String> securityRoles;
private HashMap<String, String> securityFieldIDRoleMap;

/**
 * Initialize Security Role Mapping
 * @return void
*/
private void init() {

	log.debug("ENTERING init()");

	try {

		// SECURITY ROLE MAP FROM TEXT FILE - ELEMENT / ROLE MAPPING	
		ResourceBundle rb = ResourceBundle.getBundle("resources.osx_security");
		Enumeration<?> keys = rb.getKeys();
		
		securityRoles = new HashMap<String, String>();
		
		while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			securityRoles.put(key, rb.getString(key));
		}
				
		securityFieldIDRoleMap = new HashMap<String, String>();
        
        rb = ResourceBundle.getBundle(SecurityRoleMap.SECURITY_RESOURCES);
		keys = rb.getKeys();
		while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			String value = rb.getString(key);
			if ( value == null || value.trim().length() == 0 || value.indexOf(":") < 0 ) continue;
			
			StringTokenizer stk = new StringTokenizer(value.substring(0,value.indexOf(":")),",");
			value = value.substring(value.indexOf(":")+1).trim();
			
			while(stk.hasMoreElements()) {
				String rKey = key.trim() + ((String)stk.nextElement()).trim();
				securityFieldIDRoleMap.put(rKey, value);
			}
			
		}
		log.info("TAG SECURITY MAPPING LOADED");
		

	} catch (MissingResourceException e) {
		log.warn("TAG SECURITY MAPPING EMPTY. " + e.getMessage());
	}
	
	log.debug("EXITING init()");	
}


/**
 * Private Constructor - Singleton Pattern
*/
private SecurityRoleMap() {
	super();
	init();
}

/**
 * Return Singleton Instance
 * @return DateCovnert
*/
public static SecurityRoleMap getInstance() {
	return _INSTANCE;
}

/**
 * Return the rule associated with a current Security Role ID
 * @param int securityRoleID
 * @return int
*/
public int getSecurityRoleRule(int securityRoleID) {

	int rval = READ_ONLY;	// default
	
	String key = ""+securityRoleID;

	if (securityRoles.containsKey(key)) {
		String val = (String) securityRoles.get(key);
		rval = (new Integer(val)).intValue();
	}
	return rval;
}

/**
 * Retrieve EmployeeRoleVO
 * @param String roleID
 * @return EmployeeRoleVO
*/
//private EmployeeRoleVO getEmployeeRoleVO(String roleID) throws LACSDException {
//
//	if ((roleID != null) && (securityRolesActionMap.containsKey(roleID))) { // application must be in compliance
//		return (EmployeeRoleVO)securityRolesActionMap.get(roleID);
//	}
//	else {
//		//throw new LACSDException("Security Role [" + roleID + "] for this user was not found!");
//		return null;
//	}
//}

/**
 * Compare selected ActivityID against granted activities list
 */
public boolean isPermittedStruts(String servletPath, String actionName, EmployeeRoleVO employeeRoleVO) {

	boolean permitted = false;
	
	ArrayList<ActivityVO> activities = employeeRoleVO.getActivities();
	Iterator<ActivityVO> it = activities.iterator();
	while (it.hasNext()) {
		ActivityVO activityVO = (ActivityVO)it.next();
		
		if (activityVO.getFormName().trim().equals(servletPath.trim()) 
				&& activityVO.getActivityProperty().trim().equalsIgnoreCase(actionName.trim())) {
			permitted = true;
			break;
		}
	}	
	return permitted;
}

/**
 * Returns the securityFieldIDRoleMap.
 * @return HashMap
 */
public HashMap<String, String> getSecurityFieldIDRoleMap() {
	return securityFieldIDRoleMap;
}

/**
 * Sets the securityFieldIDRoleMap.
 * @param securityFieldIDRoleMap The securityFieldIDRoleMap to set
 */
public void setSecurityFieldIDRoleMap(HashMap<String, String> securityFieldIDRoleMap) {
	this.securityFieldIDRoleMap = securityFieldIDRoleMap;
}

}
