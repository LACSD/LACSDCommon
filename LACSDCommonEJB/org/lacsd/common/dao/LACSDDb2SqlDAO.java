package org.lacsd.common.dao;

/******************************************************************************
//* Copyright (c) 2004 LACSD INC of Los Angeles. All Rights Reserved.
//* Filename:		LACSDDb2SqlDAO.java
//* Revision: 		1.0
//* Author:			M Feinberg
//* Created On: 	08-18-2004
//* Modified by:	
//* Modified On:	
//*					
//* Description:	Abstract core class for handling MYSQL Direct SQL Queries
/******************************************************************************/

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.lacsd.common.exceptions.LACSDDb2Exception;
import org.lacsd.common.exceptions.LACSDException;
import org.lacsd.common.service.TimeMarker;


public abstract class LACSDDb2SqlDAO extends LACSDDb2DAO {

private final String CLASS_NAME = this.getClass().getName();
protected Logger log = LogManager.getLogger(CLASS_NAME);

protected String ownerID;

public static final int EXECUTE_QUERY 			= 1;
public static final int EXECUTE_UPDATE 			= 2;

private String sqlStatement;	//	Sub classes will set the statement to be executed during registerInputs()
private int queryType = 0;	//	Sub classes determine if this is a SELECT or an INSERT/UPDATE/DELETE

private boolean logging = false;

/**
 * Constructor - 
*/
public LACSDDb2SqlDAO() {
	super();
	this.ownerID = "";
}

/**
 * Constructor - 
 * Overloaded for silencing noisy logging!
*/
public LACSDDb2SqlDAO(boolean useLogging) {
	this();
	this.logging = useLogging;
}

/**
 * EXECUTE-UPDATE METHOD - Perform ExecuteUpdate to MYSQL Database via SQL Statement
 * @param String sqlStatement
 * @return void
 * @throws LACSDException
*/
public void execute() throws LACSDException {

	Connection conn = null;
	Statement statement = null;
	TimeMarker tm = new TimeMarker();
	
	ResultSet rs = null;

	try {
		
		tm.setTimeMark(1);
		
		//	1)	Acquire JDBC Connection from Super Class
		conn = super.getConnection();
	//	conn.setAutoCommit(false);	//	Added 08-31-04 (MF) - ConnectionSimplePolicy.java commits!  MySQL jives with this!


		//	2)	Accept SQL Input from SubClass
		registerInputs();
		
		//	3)	Initialize SQL Statement
		statement = conn.createStatement();
		
		//	4)	Execute SQL and delegate response
		switch (queryType) {
			
			case 0:				throw new LACSDException("DAO Class did not set the Query Type!");
									
			case EXECUTE_QUERY:	rs = statement.executeQuery(sqlStatement);
									int rows = 0;
									int cols = 0;
									
									if (rs != null) {
										rows = rs.getFetchSize();
										ResultSetMetaData rsMeta = rs.getMetaData();
										cols = rsMeta.getColumnCount();
									}
									tm.setTimeMark(2);
									if (logging) {
										log.debug("DB2 Query returned [" + rows + "] rows with [" + cols +"] columns in: [" + tm.compareTimeMilli(1,2) + "] milliseconds");
									}
									getResultsFromResultSet(rs);
									break;
									
			case EXECUTE_UPDATE:	statement.executeUpdate(sqlStatement);
									tm.setTimeMark(2);
									if (logging) {
										log.debug("DB2 Updated successfully in: [" + tm.compareTimeMilli(1,2) + "] milliseconds");
									}
									break;
			
			default:				throw new LACSDException("DAO Set the Query to Type unknown by " + this.getClass().getName());
		}
		
	}
	catch (SQLException sqle) {
		try {
			conn.rollback();	// explicit rollback! 
		}
		catch (Exception e) {
			log.error("**SEVERE** Connection could not be rolled back!!");
		}
		throw new LACSDDb2Exception("Unable to execute sql statement", sqle.fillInStackTrace());
	}
	catch (Throwable t) {
		
		try {
			conn.rollback();	// explicit rollback! 
		}
		catch (Exception e) {
			log.error("**SEVERE** Connection could not be rolled back!!");
		}
		if (t instanceof LACSDException) {
			throw (LACSDException)t;
		}
		else {
			throw new LACSDDb2Exception("Unable to execute sql statement", t.fillInStackTrace());
		}
	}
	finally {
		//	Attempt to close statements and return connections
		try {
			if (rs != null) {
				rs.close();
			}
			if (statement != null) {
				statement.close();
			}
			if (conn != null) {
				super.returnConnection(conn);
			}
		}
		catch (Exception e) {
			throw new LACSDDb2Exception("Connections Failed to Close", e.fillInStackTrace());
		}
	}
}

/**
 * REGISTER INPUT PARAMETERS - (SUBCLASS STEP #1)
 * -----------------------------------------------
 * Subclass must implement this method so that 
 * SQL Statement I/O Handshake is complete
 * -----------------------------------------------
 * Usage: >>	super.setSQLStatement(sql.toString());
 * 				super.setInputParam("SELECT * FROM MIKE");
*/
protected abstract void registerInputs() throws LACSDException;


/**
 * PARSE RESULTSET - (SUBCLASS CONDITIONAL)
 * -----------------------------------------------
 * Subclass must implement this method so that 
 * resultset (cursors) from a stored procedure can
 * be properly parsed.   If the procedure does not
 * return a resultset, the implementation of this
 * method should throw a new LACSDException(), 
 * with the message "Parse Resultset not implemented"
 * -----------------------------------------------
*/
protected abstract void getResultsFromResultSet(ResultSet rs) throws SQLException, LACSDException;


/**
 * Sets the queryType.
 * @param queryType The queryType to set
 */
public void setQueryType(int queryType) {
	this.queryType = queryType;
}

/**
 * Sets the sqlStatement.
 * @param sqlStatement The sqlStatement to set
 */
public void setSqlStatement(String sqlStatement) {
	this.sqlStatement = sqlStatement;
}

/**
 * Returns the logging.
 * @return boolean
 */
public boolean isLogging() {
	return logging;
}
}
