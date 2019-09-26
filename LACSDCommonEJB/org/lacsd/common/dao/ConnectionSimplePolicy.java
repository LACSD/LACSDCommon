package org.lacsd.common.dao;

/******************************************************************************
//* Copyright (c) 2003 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		ConnectionSimplePolicy.java
//* Revision: 		1.0
//* Author:			M Feinberg
//* Created On: 	07-07-2003
//* Modified by:	
//* Modified On:	
//*					
//* Description:	Simple policy for creating connections.
/******************************************************************************/

import java.sql.Connection;
import java.sql.DriverManager;

import org.lacsd.common.util.ApplicationContext;

public class ConnectionSimplePolicy extends ConnectionPolicy {

private String driverName;
private String connectionString;

/**
 * Construct a policy using information from the application context.
 * @param String databaseType
 * @param context ApplicationContext
 * @throws Exception
*/
public ConnectionSimplePolicy(String databaseType, ApplicationContext context) throws Exception {
	super(databaseType, context);
	this.setDriverName(context.get(databaseType + "." + LACSDGenericDAO.DRIVER));
	this.setConnectionString(context.get(databaseType + "." + LACSDGenericDAO.CONNECTIONSTRING));
	this.setConnectionID(context.get(databaseType + "." + LACSDGenericDAO.USERID));
	this.setConnectionPassword(context.get(databaseType + "." + LACSDGenericDAO.USERPASSWORD));
}

public ConnectionSimplePolicy(String databaseType, ApplicationContext context,String userName,String password) throws Exception {
	super(databaseType, context);
	this.setDriverName(context.get(databaseType + "." + LACSDGenericDAO.DRIVER));
	this.setConnectionString(context.get(databaseType + "." + LACSDGenericDAO.CONNECTIONSTRING));
	this.setConnectionID(userName);
	this.setConnectionPassword(password);
}

/**
 * Answers with a connection.
 * @return java.sql.Connection
 * @throws Exception
*/
public Object getConnection() throws Exception {
	Class.forName(this.getDriverName());
	Connection conn = DriverManager.getConnection(
			this.getConnectionString(),
			this.getConnectionID(),
			this.getConnectionPassword());
	conn.setAutoCommit(false);
	return conn;
}

/**
 * Accessor for driver name.
 * @return java.lang.String
*/
protected java.lang.String getDriverName() {
	return driverName;
}

/**
 * Returns a connection to the pool.
 * @param connection java.sql.Connection
 * @throws Exception
*/
public void returnConnection(Object connection) throws Exception {
	if (connection != null) {
		try {
			((Connection)connection).commit();
		} catch (Exception x) {
			throw x;
		} finally {
			((Connection)connection).close();
		}
	}
}

/**
 * Mutator for driver name.
 * @param newDriverName java.lang.String
 * @return void
 */
private void setDriverName(String newDriverName) {
	driverName = newDriverName;
}
/**
 * @return
 */
public String getConnectionString() {
    return connectionString;
}

/**
 * @param string
 */
public void setConnectionString(String string) {
    connectionString = string;
}

}
