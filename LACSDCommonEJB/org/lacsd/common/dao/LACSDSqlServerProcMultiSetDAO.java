/******************************************************************************
//* Copyright (c) 2006 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:       LACSDSqlServerProcMultiSetDAO.java
//* Revision:       1.0
//* Author:         katielee@lacsd.org
//* Created On:     May 4, 2006
//* Modified By:
//* Modified On:
//*	
//* Description:    Account Details Data Access Object (in WATS) 
//* 				returning detail information on an account
//*					
/******************************************************************************/
package org.lacsd.common.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.lacsd.common.exceptions.LACSDException;
import org.lacsd.common.exceptions.LACSDSqlSrvr2KException;
import org.lacsd.common.helper.SQLTypeNullObject;
import org.lacsd.common.service.TimeMarker;

public abstract class LACSDSqlServerProcMultiSetDAO extends LACSDSqlServerDAO {

	private final String CLASS_NAME = this.getClass().getName();
	protected Logger log = LogManager.getLogger(CLASS_NAME);

	protected String storedProcedureName;

	private HashMap<Integer, Object> inputs;	//	Temporary storage of registered inputs
	private HashMap<Integer, Integer> outputs;	//	Temporary storage of registered outputs

	private boolean logProcedures = true;

	/**
	 ** Constructor - Uses External WATS MS-SQL Stored Procedure [WATDRATE200SPE]
	 */
	public LACSDSqlServerProcMultiSetDAO(String spName) {
		super();
		storedProcedureName = spName;
		inputs = new HashMap<Integer, Object>();
		outputs = new HashMap<Integer, Integer>();	
	}




	/**
	 * Overloaded Constructor - Specify whether to use Logging of SP I/O & Time
	 * -----------------------------------------------------------------------
	 * @param String spname
	 * @param boolean useLogging
	*/
	public LACSDSqlServerProcMultiSetDAO(String spName, boolean useLogging) {
		this(spName);
		this.logProcedures = useLogging;
	}

	/**
	 * PRIMARY EXECUTE METHOD - Contact SQL Server Stored Procedure via Prepared Call
	 * -----------------------------------------------------------------------
	 * Calls a series of sub methods private & protected (subclass implementor)
	 * to build and execute a stored procedure call.
	 * @return void
	 * @throws LACSDException
	*/
	public void execute() throws LACSDException, LACSDSqlSrvr2KException {

		Connection conn = null;
		CallableStatement statement = null;
		TimeMarker tm = new TimeMarker();

		try {
		
			//	1)	Acquire JDBC Connection from Super Class
			conn = super.getConnection();
		
			//	2)	Accept Input Parameters from SubClass
			inputs.clear();
			registerInputs();
		
			//	3)	Accept Output Parameters from SubClass
			outputs.clear();
			registerOutputs();
		
			//	4)	Build String (?,?,?,?)
			String statementString = this.buildStatementString();
			statement = conn.prepareCall(statementString);
			if (logProcedures) {
				log.debug(statementString + " Executing with (" + this.getHandshake() + ")");
			}

	
			//	5)	Register Input/Output Parameters into CallableStatement
			registerStatement(statement);
	
			//	7)	EXECUTE & TIME STORED PROCEDURE
			tm.setTimeMark(1);
			statement.execute();
			tm.setTimeMark(2);

			while (true) // loop through the resultset
				{ 
				  ResultSet rs    = statement.getResultSet(); 
				  int updateCount = statement.getUpdateCount(); 

				  // If there are no more results or counts, we're done. 
				  if (rs == null && updateCount == -1) 
					break; 

				  if (rs != null) { 
					this.getResultsFromResultSet(rs); // execute subclass implementation!					} 
					rs.close(); 
				  } // Otherwise, there will be an update count 
//				  else {
//					System.out.println("Update count = " + updateCount); 
//				  } 
				  statement.getMoreResults(); 
			   }

		}
		catch (SQLException sqle) {
		
			//	SQL Exceptions will occur if the stored proecure fails at a high level
			//	ie:  No results come back, JDBC connection failure,  bad IO Handshake, etc
		
			try {
				conn.rollback();	// explicit rollback!  <- Java Framework aborts DB TX
									// This will happen in the majority of cases, except where
									// defined by a large transactional SP Container
			}
			catch (Exception e) {
				log.error("**SEVERE** Connection Failed - But could not be rolled back!!");
			}
			throw new LACSDSqlSrvr2KException("Unable to execute stored procedure", sqle.fillInStackTrace());
		}
		catch (LACSDSqlSrvr2KException sqlE) {
		
			//	SqlServer Exceptions will be thrown by the method "" at the bottom of this file
			//	They are usually unexpected exceptions thrown by the procedure at runtime.
		
			try {
				conn.rollback();	// explicit rollback!  <- Java Framework aborts DB TX
									// This will happen in the majority of cases, except where
									// defined by a large transactional SP Container
			}
			catch (Exception e) {
				log.error("**SEVERE** SQL Server Procedure Error - But could not be rolled back!!");
			}
			throw sqlE;	//	rethrow exception after rollback
		}
		catch (LACSDException le) {
		
			//	LACSDException is caught here when a CUSTOM ("Recoverable") Exception is thrown
			//	Added 04-14-2004 (M.F.)
		
			try {
				conn.rollback();	// explicit rollback!  <- Java Framework aborts DB TX
									// This will happen in the majority of cases, except where
									// defined by a large transactional SP Container
			}
			catch (Exception e) {
				log.error("**SEVERE** SQL Server Procedure Error - But could not be rolled back!!");
			}
			throw le;	//	rethrow exception after rollback
		}
		finally {
			//	Attempt to close statements and return connections
			try {
				if (statement != null) {
					statement.close();
				}
				if (conn != null) {
					super.returnConnection(conn);
				}
			}
			catch (Exception e) {
				throw new LACSDSqlSrvr2KException("Connections Failed to Close", e.fillInStackTrace());
			}
		}
	}



	/**
	 * REGISTER INPUT PARAMETERS - (SUBCLASS STEP #1)
	 * -----------------------------------------------
	 * Subclass must implement this method so that 
	 * stored procedure I/O handshake matches what
	 * is expected by stored procedure input signature
	 * -----------------------------------------------
	 * Usage: >>	super.setInputParam(1, Integer(12345))
	 * 				super.setInputParam(2,"MyInput");
	*/
	protected abstract void registerInputs() throws LACSDException;


	/**
	 * REGISTER OUTPUT PARAMETERS - (SUBCLASS STEP #2)
	 * -----------------------------------------------
	 * Subclass must implement this method so that 
	 * stored procedure I/O handshake matches what
	 * is expected by stored procedure output parameter
	 * -----------------------------------------------
	 * Usage: >>	super.setOutputAsString(1);
	 * 				super.setOutputAsInt(2); 
	*/
	protected abstract void registerOutputs() throws LACSDException;


	/**
	 * PARSE RESULTSET - (SUBCLASS CONDITIONAL STEP #3)
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
	 * Sets a parameter on the JDBC Prepared-Call Input
	 * Note:  This method should be utilized by the 
	 *        subclass implementation during STEP #1
	 * -----------------------------------------------
	 * @param int position ++ error parms
	 * @param Object input
	 * @return void
	*/
	protected void setInputParam(int position, Object input) {
    
		Integer pos = new Integer(position);
		inputs.put(pos,input);
	}

	/**
	 * Registers a parameter on the Prepared-Call Output
	 * Note:  This method should be utilized by the 
	 * 	      subclass implementation during STEP #2
	 * -----------------------------------------------
	 * @param int position ++ error parms ++ input parms
	 * @param int type
	 * @return void
	*/
	protected void setOutput(int position, int type) throws LACSDException {

		Integer pos = new Integer(position + inputs.size());
		outputs.put(pos,new Integer(type));
	}

	/**
	 * Build parameter String for Callable Statement
	 * @return String
	*/
	private String buildStatementString() throws LACSDException {

		StringBuffer call = new StringBuffer();
		//if the input and output are both empty, then we will just need to call the 
		//stored procedure by it's name.
		if (inputs.size() == 0 && outputs.size() == 0) {
			call.append("{CALL ");
			call.append(this.storedProcedureName + "()");
			call.append("}");
		
		} else {
			call.append("{CALL ");
			call.append(this.storedProcedureName);

			if ( inputs.size() > 0 || outputs.size() > 0 ) call.append("(");

			for (int i=0; i<inputs.size(); i++) {
				call.append("?,");
			}
    
			for (int i=0; i<outputs.size(); i++) {
				call.append("?,");
			}
	
			String almostDone = call.toString();
			if (almostDone.endsWith(",")) {
				almostDone = almostDone.substring(0,(almostDone.length()-1));
				call = new StringBuffer(almostDone);
				call.append(")");
			}

			call.append(" }");
		
		}
		System.out.println("this is sql string " + call.toString());	
		return call.toString();
	}

	/**
	 * Register Statement Parameters
	 * @param CallableStatement statement
	 * @return void
	 * @throws LACSDException
	*/
	private void registerStatement(CallableStatement statement) throws SQLException, LACSDException {

		// non-null inputs
		Iterator<Integer> it = inputs.keySet().iterator();
		while (it.hasNext()) {
			Integer key = (Integer)it.next();
			int pos = key.intValue();
        
			Object type = inputs.get(key);
			if (type instanceof String) {
				String input = type.toString();
				statement.setString(pos,input);
			}
			else if (type instanceof Integer) {
				Integer tmp = (Integer)type;
				int input = tmp.intValue();
				statement.setInt(pos,input);
			}
			else if (type instanceof java.util.Date) {
				java.util.Date dt = (java.util.Date)type;
				java.sql.Date input = new java.sql.Date(dt.getTime());
				statement.setDate(pos,input);
			}
			else if (type instanceof java.sql.Date) {
				java.sql.Date input = (java.sql.Date)type;
				statement.setDate(pos,input);
			}
			else if (type instanceof java.sql.Timestamp) {
				java.sql.Timestamp input = (java.sql.Timestamp)type;
				statement.setTimestamp(pos,input);
			}
			else if (type instanceof SQLTypeNullObject ) {
				SQLTypeNullObject input = (SQLTypeNullObject)type;
				statement.setNull(pos,input.getSqlType());
			}
			else {
				statement.setObject(pos,type);
			}
		}

		//set outputs	
		it = outputs.keySet().iterator();
		while (it.hasNext()) {
		
			Integer key = (Integer)it.next();
			int pos = key.intValue();
		
			Integer value = (Integer)outputs.get(key);
			int val = value.intValue();
		
			statement.registerOutParameter(pos,val);
		}
	}

	/**
	 * Compile Handshake String - Used for debugging output
	 * @return String
	*/
	private String getHandshake() {

		StringBuffer sb = new StringBuffer();
    
		TreeMap<Integer, Object> tMap = new TreeMap<Integer, Object>();
    
		tMap.putAll(inputs);
		tMap.putAll(outputs);

		Iterator<Integer> it = tMap.keySet().iterator();
		while (it.hasNext()) {
			Object key = it.next();
			Object val = tMap.get(key);
        
			if ( val instanceof SQLTypeNullObject ) val = val.getClass().getName();
        
			if (sb.length() == 0 ) {
				sb.append(" " + val);
			} else {
				sb.append(", " + val);
			}
		}

		return sb.toString();
	}


	/**
	 * Returns the logProcedures.
	 * @return boolean
	 */
	protected boolean isLogProcedures() {
		return logProcedures;
	}
}
