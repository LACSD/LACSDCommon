package org.lacsd.common.authentication;

/******************************************************************************
//* Copyright (c) 2003 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		SessionCleaner.java
//* Revision: 		1.0
//* Author:			MFEINBERG@LACSD.ORG
//* Created On: 	11-17-2003
//* Modified by:	
//* Modified On:	
//*					
//* Description:	Singleton object responsible for ensuring that UserProfile
//* 				threads do not become orphaned in the HttpSessionContext
/******************************************************************************/

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.lacsd.common.constants.LACSDWebConstants;

public class SessionCleaner {

private static SessionCleaner _INSTANCE = new SessionCleaner();
private final String CLASS_NAME = this.getClass().getName();
private Logger log = LogManager.getLogger(CLASS_NAME);
private HashMap<String, HttpSession> userMap;
private HashMap<String, HttpSession> staleMap;

/**
 * Private Constructor - Singleton Pattern
*/
private SessionCleaner() {
	super();
	userMap = new HashMap<String, HttpSession>();
	staleMap = new HashMap<String, HttpSession>();
}

/**
 * Return Singleton Instance
 * @return DateCovnert
*/
public static SessionCleaner getInstance() {
	return _INSTANCE;
}

/**
 * Add userProfile to mapping - Remove Unnecessary Duplicate Objects
 * Note:  Method synchronized to protect against Concurrent Modification Exception
 * @param UserProfile userProfile
 * @return HttpSession
*/
public synchronized HttpSession regSession(HttpSession session, String IPAddress, HttpServletRequest req) {



	//	1)	If there is a duplicate of this UserProfile object, kill it.
	//		Note: 	This could happen if user closes browser then opens
	//				a new browser during the same login period.
	if (userMap.containsKey(IPAddress)) {
		Object staleObject = (Object)userMap.get(IPAddress);
		userMap.remove(IPAddress);
		HttpSession staleSession = (HttpSession)staleObject;
		
		//	11-10-04 - WAS 5 may have automatically cleaned this session already,
		//	thus calling invalidate() will cause an "IllegalStateException"
		try {
			staleSession.invalidate();
		}
		catch (IllegalStateException illex) {
			//log.warn("HttpSession failed to invalidate() - " + illex.getMessage(), illex.fillInStackTrace());
		}
		
		staleMap.put(IPAddress,staleSession);
		log.debug("Duplicate UserProfile Thread De-activate Within [" + LACSDWebConstants.USER_PROFILE_PAUSE_INTERVAL + "] seconds: " + staleSession.getId());
	}
	
	//	2)	Add this fresh UserProfile object to the Mapping
	session = req.getSession(true);	// create new HttpSession!
	userMap.put(IPAddress,session);
	return session;
}

/**
 * Remove HttpSession from mapping 
 * Note:  Used only by Logout Action for memory cleanup purposes
 * @param String IPAddress
 * @return void
*/
public synchronized void clearMap(String IPAddress) {
	userMap.remove(IPAddress);
}

/**
 * Allow caller to check if this IPAddress has a stale session
 * @param String IPAddress
 * @return boolean
*/
public boolean isStaleSession(String IPAddress) {
	boolean rval = false;
	if (staleMap.containsKey(IPAddress)) {
		rval = true;
	}
	return rval;
}

/**
 * Allow caller to remove a stale session
 * @param String IPAddress
 * @return void
*/
public synchronized void removeStaleSession(String IPAddress) {
	staleMap.remove(IPAddress);
}
}