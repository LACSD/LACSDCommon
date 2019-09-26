package org.lacsd.common.threads;

/******************************************************************************
//* Copyright (c) 2004 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		UserProfileThread.java
//* Revision: 		1.0
//* Author:			MFEINBERG@LACSD.ORG
//* Created On: 	02-20-2004
//* Modified by:	
//* Modified On:	
//*					
//* Description:	Explicit wrapper Thread to manage the execution of UserProfile
/******************************************************************************/

import org.lacsd.common.authentication.UserProfile;

public class UserProfileThread extends Thread {

/**
 * Constructor
*/
public UserProfileThread(UserProfile userProfile) {
	super((Runnable)userProfile);	//	Cast Interface to Runnable Implementation
	this.setName(userProfile.getEmployeeVO().getLastName());
	this.setPriority(Thread.NORM_PRIORITY);
}
}
