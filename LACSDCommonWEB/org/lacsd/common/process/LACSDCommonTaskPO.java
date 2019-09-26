package org.lacsd.common.process;

import java.util.ArrayList;

import org.lacsd.common.constants.LACSDJNDIConstants;
import org.lacsd.common.exceptions.LACSDException;
import org.lacsd.common.helper.LACSDEJBFinder;
import org.lacsd.common.util.ApplicationContext;
import org.lacsd.osx.dao.ejb.view.GroupEmailDAOBeanRemote;

/******************************************************************************
//* Copyright (c) 2017 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		LACSDCommonTaskPO.java
//* Revision: 		1.0
//* Author:			hho
//* Created On: 	Jun 14, 2017
//* Modified by:	
//* Modified On:	
//*					
//* Description:	
//* 
/******************************************************************************/

public class LACSDCommonTaskPO extends LACSDGenericPO{
	
	private static final String JNDI_GROUPEMAILDA0 = "ejb/GroupEmailDAO";
	
	/**
	 * Get group email list
	 * @param applicationID
	 * @param groupName
	 * @return
	 * @throws LACSDException
	 */
	public ArrayList<String> getApplicationGroupEmails(String applicationID, String groupName) throws Throwable {
	    ApplicationContext appctx = ApplicationContext.getInstance();
	    String remoteURL = appctx.get(LACSDJNDIConstants.REMOTE_IIOP_URL_RESOURCE);
	    Object remoteObj = LACSDEJBFinder.getRemoteEJB(remoteURL,JNDI_GROUPEMAILDA0);
	    GroupEmailDAOBeanRemote groupEmailDAO = (GroupEmailDAOBeanRemote)remoteObj;
	   	return groupEmailDAO.getEmailInGroup(applicationID, groupName);
	 }
	
	
}
