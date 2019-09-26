package org.lacsd.common.dao;

/******************************************************************************
//* Copyright (c) 2003 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		LACSDSqlServerDAO.java
//* Revision: 		1.0
//* Author:			M Feinberg
//* Created On: 	01-06-2004
//* Modified by:	
//* Modified On:	
//*					
//* Description:	Abstract core class for handling SQL Server2K DB Connections
/******************************************************************************/

import java.sql.Connection;
import org.lacsd.common.exceptions.LACSDDatabaseException;

public abstract class LACSDSqlServerDAO extends LACSDGenericDAO {

public static final String SQL2K_DATABASE_TYPE = "SQLServer2K";
private ConnectionPolicy policy = null;


/**
 * Default Constructor
*/
public LACSDSqlServerDAO() {
	super();
	
	try {
		policy = getPolicy(SQL2K_DATABASE_TYPE);
	}
	catch (LACSDDatabaseException e) {
		//just log here as exception will be thrown in getConnection() method
		//print to console since static initialize
		System.err.println("Error in initialize policy for SQLServer2KDAO" + e);
	}
}
/**
 * Constructor with database username and password
*/
public LACSDSqlServerDAO(String userName,String password) {
	super();
	
	try {
		policy = getPolicy(SQL2K_DATABASE_TYPE,userName,password);
	}
	catch (LACSDDatabaseException e) {
		//just log here as exception will be thrown in getConnection() method
		//print to console since static initialize
		System.err.println("Error in initialize policy for SQLServer2KDAO" + e);
	}
}

/**
 * Constructor if there is a second sql server database
*/
public LACSDSqlServerDAO(String otherDatabaseName) {
	super();
	
	try {
		policy = getPolicy(otherDatabaseName);
	}
	catch (LACSDDatabaseException e) {
		//just log here as exception will be thrown in getConnection() method
		//print to console since static initialize
		System.err.println("Error in initialize policy for SQLServer2KDAO" + e);
	}
}

/**
 * Constructor if there is a second sql server database, with username and password
*/
public LACSDSqlServerDAO(String otherDatabaseName, String userName,String password) {
	super();
	
	try {
		policy = getPolicy(otherDatabaseName,userName,password);
	}
	catch (LACSDDatabaseException e) {
		//just log here as exception will be thrown in getConnection() method
		//print to console since static initialize
		System.err.println("Error in initialize policy for SQLServer2KDAO" + e);
	}
}


/**
 * Answers with a connection.
 * @return java.sql.Connection
*/
public Connection getConnection() throws LACSDDatabaseException {
	
	if (policy == null) {
		throw new LACSDDatabaseException("Policy was not set for the: " + SQL2K_DATABASE_TYPE);
	}
	
	try {
		return (Connection)policy.getConnection();
	}
	catch (Exception e) {
		throw new LACSDDatabaseException("Error getting the Connection from policy for the: " + SQL2K_DATABASE_TYPE, e);
	}	
}

/**
 * Returns a connection to the pool.
 * @param connection java.sql.Connection
 * @return void
*/
public void returnConnection(Connection connection) throws Exception {
	
	if (policy == null) {
		throw new LACSDDatabaseException("Policy was not set for the: " + SQL2K_DATABASE_TYPE);
	}
	
	if (connection == null) {
		throw new LACSDDatabaseException("Connection being returned to " + SQL2K_DATABASE_TYPE + " is null");
	}

	policy.returnConnection(connection);
}
}
