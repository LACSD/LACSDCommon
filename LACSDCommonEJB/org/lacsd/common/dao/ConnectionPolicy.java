package org.lacsd.common.dao;

/******************************************************************************
//* Copyright (c) 2003 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		ConnectionPolicy.java
//* Revision: 		1.0
//* Author:			M Feinberg
//* Created On: 	07-07-2003
//* Modified by:	
//* Modified On:	
//*					
//* Description:	This policy is used by the connection factory to handle 
//* 				connections.  ConnectionFactory.setPolicy must be modified 
//* 				for each subclass.
/******************************************************************************/

import org.lacsd.common.util.ApplicationContext;

public abstract class ConnectionPolicy {

private String connectionID;
private String connectionPassword;

/**
 * Construct a policy using information from the application context.
 * @param String databaseType
 * @param ApplicationContext context
 * @throws Exception
 */

public ConnectionPolicy(String databaseType, ApplicationContext context) throws Exception {
	super();
	try {
		this.setConnectionID(context.get(databaseType + "." + LACSDGenericDAO.USERID));
	}
	catch ( Exception ex ) {
		this.setConnectionID( null );
	}
	String password = null;
	try {
		//PasswordUtil pwutil = PasswordUtil.getInstance();
		password = (String)context.get(databaseType + "." + LACSDGenericDAO.USERPASSWORD);
	}
	catch ( Exception ignored ) {}
	
	//this.setConnectionPassword(pwutil.decryptPassword(password));
	this.setConnectionPassword(password);
}



/**
 * Answers with a connection.
 * @return java.sql.Connection
 * @throws Exception
*/
public abstract Object getConnection() throws Exception;

/**
 * Accessor for the connection id.
 * @return java.lang.String
*/
public String getConnectionID() {
	return connectionID;
}

/**
 * Accessor for the connection password.
 * @return java.lang.String
*/
public String getConnectionPassword() {
	return connectionPassword;
}

/**
 * Returns a connection to the pool.
 * @param java.sql.Connection connection
 * @return void
 * @throws Exception
*/
public abstract void returnConnection(Object connection) throws Exception;

/**
 * Mutator for the connection id.
 * @param java.lang.String newConnectionID
 * @return void
*/
protected void setConnectionID(String newConnectionID) {
	connectionID = newConnectionID;
}

/**
 * Mutator for the connection password.
 * @param java.lang.String newConnectionPassword
*/
protected void setConnectionPassword(String newConnectionPassword) {
	connectionPassword = newConnectionPassword;
}
}

