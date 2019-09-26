package org.lacsd.common.dao;

/******************************************************************************
//* Copyright (c) 2003 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		ConnectionPoolPolicy.java
//* Revision: 		1.0
//* Author:			M Feinberg
//* Created On: 	07-07-2003
//* Modified by:	
//* Modified On:	
//*					
//* Description:	Connection policy uses connections from the pool.
//* 				The most common pools wrap Connection objects and use the 
//* 				Connection.close() method to return the wrapped connection 
//* 				to the pool.  A subclass that uses a pool that does not wrap
//* 				the Connection should override the returnConnection method.
/******************************************************************************/

import java.sql.Connection;

import javax.sql.DataSource;

import org.lacsd.common.util.*;

public class ConnectionPoolPolicy extends ConnectionPolicy {

private DataSource dataSource;
private String dataSourceName;
private LACSDInitialContext ctx;

/**
 * Construct a policy using information from the application context.
 * @param String databaseType
 * @param ApplicationContext context
 * @throws Exception
*/
public ConnectionPoolPolicy(String databaseType, ApplicationContext context, LACSDInitialContext namingContext) throws Exception {
	super(databaseType, context);
    this.ctx = namingContext;	
	this.setDataSourceName(context.get(databaseType + "." + LACSDGenericDAO.DATA_SOURCE_NAME));
}

/**
 * Answers with a connection.
 * @return java.sql.Connection
 * @throws Exception
*/
public Object getConnection() throws Exception {
	Connection conn = this.getDataSource().getConnection();
	conn.setAutoCommit(false);
	return conn;
}

/**
 * Accessor for data source.
 * @return javax.sql.DataSource
 * @throws Exception
*/
protected DataSource getDataSource() throws Exception {
	if (this.dataSource == null) {
		this.setDataSource();
	}
	return this.dataSource;
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
		try {
			((Connection)connection).commit();
		} finally {
			((Connection)connection).close();
		}
	}
}

/**
 * Mutator for data source.
 * @return void
 * @throws Exception
*/
private synchronized void setDataSource() throws Exception {
	if (this.dataSource == null) {
		this.dataSource =(DataSource) ctx.lookup(this.getDataSourceName());
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
