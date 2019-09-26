package org.lacsd.common.taglib;

/******************************************************************************
//* Copyright (c) 2004 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		OSXTagManager.java
//* Revision: 		1.0
//* Author:			dyip@lacsd.org
//* Created On: 	08/12/2004
//* Modified By:	
//* Modified On:	
//*					
//* Description:	This class manages OSX tag
/******************************************************************************/

import java.lang.reflect.Method;

import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.lacsd.common.authentication.UserProfile;
import org.lacsd.common.constants.LACSDWebConstants;
import org.lacsd.common.security.SecurityRoleMap;

public class OSXTagManager {

private static OSXTagManager _INSTANCE = new OSXTagManager();

private final String CLASS_NAME = this.getClass().getName();
private Logger log = LogManager.getLogger(CLASS_NAME);

/**
 * Initialize OSXTagManager
*/
private OSXTagManager() {
	super();
}

/**
 * Return Singleton Instance
 * 
*/
public static OSXTagManager getInstance() {
	return _INSTANCE;
}

/**
 * Process the OSX Tags
 * Uses the Security Role Map
 * 
*/
@SuppressWarnings({ "rawtypes", "unchecked" })
public void processTag(HttpSession session, Object obj) {
	
	String roleID = this.getRoleIDfromSession(session);

	SecurityRoleMap map = SecurityRoleMap.getInstance();
    String key;
	String resp;
	
	Method aM = null;
	Class cls = null;
	
	try {
		
		if ( obj instanceof OSXTextTag) {
			cls = Class.forName("org.lacsd.common.taglib.OSXTextTag");
		} else if (obj instanceof OSXTextAreaTag) {
			cls = Class.forName("org.lacsd.common.taglib.OSXTextAreaTag");
		} else if (obj instanceof OSXRadioTag) {
			cls = Class.forName("org.lacsd.common.taglib.OSXRadioTag");
		} else if (obj instanceof OSXSelectTag) {
			cls = Class.forName("org.lacsd.common.taglib.OSXSelectTag");
		} else if (obj instanceof OSXCheckboxTag) {
			cls = Class.forName("org.lacsd.common.taglib.OSXCheckboxTag");
		} else if (obj instanceof OSXButtonTag) {
			cls = Class.forName("org.lacsd.common.taglib.OSXButtonTag");
		} else {
			// do nothing
			log.warn("OSX Tag object " + obj.toString() + " is not defined.");
			return;
		}
	
		// get the tag id + role id from mapping
		aM = cls.getMethod("getId");
		key = (String)aM.invoke(obj) + roleID;
		resp = (String)map.getSecurityFieldIDRoleMap().get(key);
		if ( resp == null ) resp = "";
		
		// disable the field	
		Class[] clsParams = {boolean.class};
		aM = cls.getMethod("setDisabled",clsParams);
		if ( resp.equals("disable") ) {
			Object[] methParams = {new Boolean(true)};
			aM.invoke(obj,methParams);
		}
	
	} catch (Exception e) {
		log.error(e);
	}

}

/**
 * Gets the role ID from the session obj
 * 
 * @return String
 * 
*/
public String getRoleIDfromSession(HttpSession session) {
	
	UserProfile userProfile = (UserProfile)session.getAttribute(LACSDWebConstants.USER_PROFILE);
	return userProfile.getEmployeeVO().getSecurityRoleID();
}


}
