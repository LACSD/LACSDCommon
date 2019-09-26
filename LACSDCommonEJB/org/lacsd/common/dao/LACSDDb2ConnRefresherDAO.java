package org.lacsd.common.dao;

/******************************************************************************
//* Copyright (c) 2004 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		LACSDDb2ConnRefresherDAO.java
//* Revision: 		1.0
//* Author:			MFEINBERG@LACSD.ORG
//* Created On: 	09-07-2004
//* Modified by:	
//* Modified On:	
//*					
//* Description:	DB2 DAO Implementation for manually refreshing Connection Pool
/******************************************************************************/

import java.sql.ResultSet;
import java.sql.SQLException;

import org.lacsd.common.exceptions.LACSDException;

public class LACSDDb2ConnRefresherDAO extends LACSDDb2SqlDAO {

private static final String ACTION_REFRESH 		= "R";

private String action;


/**
* Get User Details populates the UserProfile object with detailed
* information about the WSI employee.  example:  Name and Email Address.
* @param UserProfile userProfile
* @return UserProfile
* @throws WSIException
*/
public void doRefreshDB() throws LACSDException {

	log.debug("ENTERING doRefreshDB()");
	
	this.action = ACTION_REFRESH;

	execute();

	log.debug("EXITING doRefreshDB()");
}

//****************************************************************//

/**
 * SETUP STEP 1:  Register SQL Statement Input with super class
 * @return void
 * @throws WSIException
*/
protected void registerInputs() throws LACSDException {

	if (this.action.equalsIgnoreCase(ACTION_REFRESH)) {		
		setQueryType(EXECUTE_QUERY);
		setSqlStatement(buildRefreshInput());
	}
	else {
		throw new LACSDException("Bad Action Name in DAO Input Setup");
	}
}

/**
 * HANDLE OUTPUT TYPE 1:	SQL Statement Returns an Open Cursor
 * @param ResultSet rs
 * @return void
 * @throws SQLException, WSIException
*/
protected void getResultsFromResultSet(ResultSet rs) throws SQLException, LACSDException {	
}

//****************************************************************//


/**
 * Build Input String for Verify Login
 * @return String
*/
private String buildRefreshInput() {
	StringBuffer sql = new StringBuffer("SELECT current date FROM sysibm.sysdummy1");
	return sql.toString();
}

}
