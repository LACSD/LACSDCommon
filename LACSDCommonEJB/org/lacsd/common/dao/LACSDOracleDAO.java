package org.lacsd.common.dao;

/******************************************************************************
//* Copyright (c) 2006 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		LACSDOracle10gDAO.java
//* Revision: 		1.0
//* Author:			MFEINBERG@LACSD.ORG
//* Created On: 	08-09-2006
//* Modified by:	
//* Modified On:	
//*					
//* Description:	Abstract core class for handling ORACLE 10G DB Connections
/******************************************************************************/
import java.sql.Connection;
import org.lacsd.common.exceptions.LACSDDatabaseException;

public abstract class LACSDOracleDAO extends LACSDGenericDAO {

private static final String ORACLE10G_DATABASE_TYPE = "oracle10g";
private ConnectionPolicy policy = null;

/**
 * Default Constructor
*/
public LACSDOracleDAO() {
	super();
	
	try {
		policy = getPolicy(ORACLE10G_DATABASE_TYPE);
	}
	catch (LACSDDatabaseException e) {
		System.err.println("Error in initialize policy for OracleDAO" + e);
	}
}

/**
 * Constructor if there is a second sql server database
*/
public LACSDOracleDAO(String otherDatabaseName) {
	super();
	
	try {
		policy = getPolicy(otherDatabaseName);
	}
	catch (LACSDDatabaseException e) {
		System.err.println("Error in initialize policy for OracleDAO" + e);
	}
}


/**
 * Answers with a connection.
 * @return java.sql.Connection
*/
public Connection getConnection() throws LACSDDatabaseException {
	
	if (policy == null) {
		throw new LACSDDatabaseException("Policy was not set for the: " + ORACLE10G_DATABASE_TYPE);
	}
	
	try {
		return (Connection)policy.getConnection();
	}
	catch (Exception e) {
		throw new LACSDDatabaseException("Error getting the Connection from policy for the: " + ORACLE10G_DATABASE_TYPE, e);
	}	
}

/**
 * Returns a connection to the pool.
 * @param connection java.sql.Connection
 * @return void
*/
public void returnConnection(Connection connection) throws Exception {
	
	if (policy == null) {
		throw new LACSDDatabaseException("Policy was not set for the: " + ORACLE10G_DATABASE_TYPE);
	}
	
	if (connection == null) {
		throw new LACSDDatabaseException("Connection being returned to " + ORACLE10G_DATABASE_TYPE + " is null");
	}

	policy.returnConnection(connection);
}
}
