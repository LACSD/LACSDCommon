package org.lacsd.common.dao;

/******************************************************************************
//* Copyright (c) 2005 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		LACSDSybaseDAO.java
//* Revision: 		1.0
//* Author:			tnguyen@lacsd.org
//* Created On: 	06-06-2005
//* Modified by:	
//* Modified On:	
//*					
//* Description:	Abstract core class for handling SQL Sybase DB Connections
/******************************************************************************/

import java.sql.Connection;
import org.lacsd.common.exceptions.LACSDDatabaseException;

public abstract class LACSDSybaseDAO extends LACSDGenericDAO {

private static final String SYBASE_DATABASE_TYPE = "SybaseDatabase";
private ConnectionPolicy policy = null;


/**
 * Default Constructor
*/
public LACSDSybaseDAO() {
	super();
	
	try {
		policy = getPolicy(SYBASE_DATABASE_TYPE);
	}
	catch (LACSDDatabaseException e) {
		//just log here as exception will be thrown in getConnection() method
		//print to console since static initialize
		System.err.println("Error in initialize policy for SybaseDAO" + e);
	}
}

/**
 * Answers with a connection.
 * @return java.sql.Connection
*/
public Connection getConnection() throws LACSDDatabaseException {
	
	if (policy == null) {
		throw new LACSDDatabaseException("Policy was not set for the: " + SYBASE_DATABASE_TYPE);
	}
	
	try {
		return (Connection)policy.getConnection();
	}
	catch (Exception e) {
		throw new LACSDDatabaseException("Error getting the Connection from policy for the: " + SYBASE_DATABASE_TYPE, e);
	}	
}

/**
 * Returns a connection to the pool.
 * @param connection java.sql.Connection
 * @return void
*/
public void returnConnection(Connection connection) throws Exception {
	
	if (policy == null) {
		throw new LACSDDatabaseException("Policy was not set for the: " + SYBASE_DATABASE_TYPE);
	}
	
	if (connection == null) {
		throw new LACSDDatabaseException("Connection being returned to " + SYBASE_DATABASE_TYPE + " is null");
	}

	policy.returnConnection(connection);
}
}
