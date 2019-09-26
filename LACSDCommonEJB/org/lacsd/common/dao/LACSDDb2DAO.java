package org.lacsd.common.dao;

/******************************************************************************
//* Copyright (c) 2003 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		LACSDDb2DAO.java
//* Revision: 		1.0
//* Author:			M Feinberg
//* Created On: 	07-08-2003
//* Modified by:	
//* Modified On:	
//*					
//* Description:	Abstract core class for handling DB2 Database Connections
/******************************************************************************/

import java.sql.Connection;
import org.lacsd.common.exceptions.LACSDDatabaseException;

public abstract class LACSDDb2DAO extends LACSDGenericDAO {

public static final String DB2_DATABASE_TYPE = "Db2Database";
private ConnectionPolicy policy = null;

/** 
 * Static Initializer
*
static {
	
	try {
		policy = getPolicy(DB2_DATABASE_TYPE);
	}
	catch (LACSDDatabaseException e) {
		//just log here as exception will be thrown in getConnection() method
		//print to console since static initialize
		System.err.println("Error in initialize policy for DB2DAO" + e);
	}
}
*/

/**
 * Default Constructor
*/
public LACSDDb2DAO() {
	super();
	
	try {
		policy = getPolicy(DB2_DATABASE_TYPE);
	}
	catch (LACSDDatabaseException e) {
		//just log here as exception will be thrown in getConnection() method
		//print to console since static initialize
		System.err.println("Error in initialize policy for DB2DAO" + e);
	}
}

/**
 * Answers with a connection.
 * @return java.sql.Connection
*/
public Connection getConnection() throws LACSDDatabaseException {
	
	if (policy == null) {
		throw new LACSDDatabaseException("Policy was not set for the: " + DB2_DATABASE_TYPE);
	}
	
	try {
		return (Connection)policy.getConnection();
	}
	catch (Exception e) {
		throw new LACSDDatabaseException("Error getting the Connection from policy for the: " + DB2_DATABASE_TYPE, e);
	}	
}

/**
 * Returns a connection to the pool.
 * @param connection java.sql.Connection
 * @return void
*/
public void returnConnection(Connection connection) throws Exception {
	
	if (policy == null) {
		throw new LACSDDatabaseException("Policy was not set for the: " + DB2_DATABASE_TYPE);
	}
	
	if (connection == null) {
		throw new LACSDDatabaseException("Connection being returned to " + DB2_DATABASE_TYPE + " is null");
	}

	policy.returnConnection(connection);
}
}
