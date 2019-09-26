package org.lacsd.common.dao;

/******************************************************************************
//* Copyright (c) 2003 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		ConnectionCICS.java
//* Revision: 		1.0
//* Author:			tnguyen@lacsd.org
//* Created On: 	05-24-2005
//* Modified by:	
//* Modified On:	
//*					
//* Description:	Connection policy uses connections from the pool.
//* 				The most common pools wrap Connection objects and use the 
//* 				Connection.close() method to return the wrapped connection 
//* 				to the pool.  A subclass that uses a pool that does not wrap
//* 				the Connection should override the returnConnection method.
/******************************************************************************/

import org.lacsd.common.util.ApplicationContext;

import com.ibm.ctg.client.JavaGateway;

public class ConnectionECIPolicy extends ConnectionPolicy {

private String dataSourceName;
private static JavaGateway jgaConnection = null;

/**
 * Construct a policy using information from the application context.
 * @param String databaseType
 * @param ApplicationContext context
 * @throws Exception
*/
public ConnectionECIPolicy(String databaseType, ApplicationContext context) throws Exception {
	super(databaseType, context);	
	this.setDataSourceName(context.get(databaseType + "." + LACSDGenericDAO.DATA_SOURCE_NAME));
}

/**
 * Answers with a connection.
 * @return java.sql.Connection
 * @throws Exception
*/
public Object getConnection() throws Exception {
	if ( jgaConnection == null || !jgaConnection.getURL().equals(this.dataSourceName)) {
		jgaConnection = new JavaGateway();
		jgaConnection.setURL( this.dataSourceName );
		jgaConnection.open();       
		return jgaConnection;
	} else {
		if ( !jgaConnection.isOpen() ) jgaConnection.open();
		return jgaConnection;
	}
}

/**
 * Accessor for data source name.
 * @return java.lang.String
*/
protected String getDataSourceName() {
	return dataSourceName;
}

/**
 * Returns a connection to the pool.
 * @param java.sql.Connection connection
 * @return void
 * @throws Exception
*/
public void returnConnection(Object connection) throws Exception {
	if (connection != null) {
		((JavaGateway)connection).close();
	}
}

/**
 * Mutator for data source name.
 * @param newDataSourceName java.lang.String
 * @return void
*/
private void setDataSourceName(String newDataSourceName) {
	dataSourceName = newDataSourceName;
}

}
