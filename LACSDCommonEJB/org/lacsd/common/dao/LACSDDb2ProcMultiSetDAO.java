package org.lacsd.common.dao;

/******************************************************************************
//* Copyright (c) 2004 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		LACSDDb2ProcMultiSetDAO.java
//* Revision: 		1.0
//* Author:			M Feinberg
//* Created On: 	03-19-2004
//* Modified By:	
//* Modified On:	
//*					
//* Description:	Abstract core class for handling DB2  * STORED PROCEDURES *
//*
//*					Specifically handles multiple ResultSet returns from SP!
//* 
/******************************************************************************/

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.lacsd.common.exceptions.LACSDDb2Exception;
import org.lacsd.common.exceptions.LACSDException;
import org.lacsd.common.service.TimeMarker;


public abstract class LACSDDb2ProcMultiSetDAO extends LACSDDb2DAO {

private final String CLASS_NAME = this.getClass().getName();
protected Logger log = LogManager.getLogger(CLASS_NAME);

protected String storedProcedureName;

private HashMap<Integer, Object> inputs;	//	Temporary storage of registered inputs
private HashMap<Integer, Integer> outputs;	//	Temporary storage of registered outputs

private boolean logProcedures = true;

/**
 * Constructor - Takes Stored Procedure name
 * -----------------------------------------------------------------------
 * @param String spname
*/
public LACSDDb2ProcMultiSetDAO(String spName) {
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
public LACSDDb2ProcMultiSetDAO(String spName, boolean useLogging) {
	this(spName);
	this.logProcedures = useLogging;
}

/**
 * PRIMARY EXECUTE METHOD - Contact DB2 Stored Procedure via Prepared Call
 * -----------------------------------------------------------------------
 * Calls a series of sub methods private & protected (subclass implementor)
 * to build and execute a stored procedure call.
 * @return void
 * @throws LACSDException
*/
public void execute() throws LACSDException, LACSDDb2Exception {

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

		//	5)	Register the Return and Error parameters for output
		statement.registerOutParameter(1, Types.VARCHAR);
		statement.registerOutParameter(2, Types.VARCHAR);
	
		//	6)	Register Input/Output Parameters into CallableStatement
		registerStatement(statement);
	
		//	7)	EXECUTE & TIME STORED PROCEDURE
		tm.setTimeMark(1);
		boolean isResultSet = statement.execute();
		tm.setTimeMark(2);
	
		//	8)	Package Output & Error Messages Returned from SP
		String sqlstate_out = statement.getString(1);
		String sqlcode_out  = statement.getString(2);
	
		//	9)	Error-Message Handling for database - If Error Msg Found - Exception will be thrown here!
		checkStoredProcErrors(sqlstate_out, sqlcode_out);


		//	10)	Send Results back to (RESULTSET of STRING) methods
		if (isResultSet) {
			//	11/19/2003 - Added Conditional Logging (too noisy)
			if (logProcedures) {
				log.debug("[" + this.storedProcedureName + "] Returned a RESULTSET in: " + tm.compareTimeMilli(1,2) + " milliseconds");
			}
			
			//	03/19/2004 - Parse multiple resultsets
			do {
				ResultSet rs = statement.getResultSet();
				this.getResultsFromResultSet(rs); // execute subclass implementation!
			}
			while (statement.getMoreResults());

		}
		else {	
			int[] params = new int[outputs.size()];
			Iterator<Integer> it = outputs.keySet().iterator();
			int c=0;
			while (it.hasNext()) {
				Integer key = (Integer)it.next();
				int val = key.intValue();
				params[c] = val;
				c++;
			}
			Arrays.sort(params);	//	Place outputs in numerically ascending order
			
			String[] outputStrings = new String[params.length];
			for (int i=0; i<params.length; i++) {
				outputStrings[i] = statement.getString(params[i]);
			}
			//	11/19/2003 - Added Conditional Logging (too noisy)
			if (logProcedures) {
				log.debug("[" + this.storedProcedureName + "] Returned [" + outputStrings.length + "] STRINGS in: " + tm.compareTimeMilli(1,2) + " milliseconds");
			}
			this.getResultsFromString(outputStrings);
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
		throw new LACSDDb2Exception("Unable to execute stored procedure", sqle.fillInStackTrace());
	}
	catch (LACSDDb2Exception db2e) {
		
		//	DB2 Exceptions will be thrown by the method "" at the bottom of this file
		//	They are usually unexpected exceptions thrown by the procedure at runtime.
		
		try {
			conn.rollback();	// explicit rollback!  <- Java Framework aborts DB TX
								// This will happen in the majority of cases, except where
								// defined by a large transactional SP Container
		}
		catch (Exception e) {
			log.error("**SEVERE** DB2 Procedure Error - But could not be rolled back!!");
		}
		throw db2e;	//	rethrow exception after rollback
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
			log.error("**SEVERE** DB2 Procedure Error - But could not be rolled back!!");
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
			throw new LACSDDb2Exception("Connections Failed to Close", e.fillInStackTrace());
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
 * PARSE RESULTSET - (SUBCLASS CONDITIONAL STEP #4)
 * -----------------------------------------------
 * Subclass must implement this method so that 
 * String array outputs (parameter #3 and higher)
 * can be properly parsed.  If the procedure does not
 * return a String array, the implementation of this
 * method should throw a new LACSDException(), 
 * with the message "Parse String not implemented"
 * -----------------------------------------------
*/
protected abstract void getResultsFromString(String[] output) throws LACSDException;



/**
 * Sets a parameter on the JDBC Prepared-Call Input
 * Note:  This method should be utilized by the 
 * 	      subclass implementation during STEP #1
 * -----------------------------------------------
 * @param int position ++ error parms
 * @param Object input
 * @return void
*/
protected void setInputParam(int position, Object input) {
	
	Integer pos = new Integer(position + 2);	//	Error Parms are slots 1 & 2
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

	Integer pos = new Integer((position + 2) + inputs.size());	//	Error Parms ++ Input Parms
	outputs.put(pos,new Integer(type));
}


/**
 * Build parameter String for Callable Statement
 * @return String
*/
private String buildStatementString() throws LACSDException {

	StringBuffer call = new StringBuffer();
	call.append("CALL ");
	call.append(this.storedProcedureName);
	call.append("(");
	call.append("?");	//	Error Param 1
	call.append(",");
	call.append("?");	//	Error Param 2

	
	for (int i=0; i<inputs.size(); i++) {
		call.append(",?");
	}
	
	for (int i=0; i<outputs.size(); i++) {
		call.append(",?");
	}
	
	call.append(")");
	
	return call.toString();
}

/**
 * Register Statement Parameters
 * @param CallableStatement statement
 * @return void
 * @throws LACSDException
*/
private void registerStatement(CallableStatement statement) throws SQLException, LACSDException {

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
		else {
			statement.setObject(pos,type);
		}
	}
	
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
	
	//	Set First 2 parameters - always SQLSTATE & SQLCODE
	sb.append(new Integer(Types.VARCHAR) + "," + new Integer(Types.VARCHAR));
	
	//	Append Input Params	
	Object[] inputsArray = new Object[inputs.size()];
	int tmpPosition = 0;
	Iterator<Integer> it = inputs.keySet().iterator();
	while (it.hasNext()) {
		Object key = it.next();
		Object val = inputs.get(key);
		inputsArray[(inputsArray.length - tmpPosition) - 1] = val;
		tmpPosition++;
	}
	for (int i=0; i<inputsArray.length; i++) {	// reverse sort order
		Object input = inputsArray[i];
		sb.append("," + input);
	}
	
	//	Append additional Output Params
	Object[] outputsArray = new Object[outputs.size()];
	tmpPosition = 0;
	it = outputs.keySet().iterator();
	while (it.hasNext()) {
		Object key = it.next();
		Object val = outputs.get(key);
		outputsArray[(outputsArray.length - tmpPosition) - 1] = val;
		tmpPosition++;
	}
	for (int i=0; i<outputsArray.length; i++) {	// reverse sort order
		Object output = outputsArray[i];
		sb.append("," + output);
	}
	return sb.toString();
}

/**
 * Determine non-recoverable stored procedure errors
 * @param String outparameter
 * @param String errorparameter
 * @return void
*/
private void checkStoredProcErrors(String sqlstate_out, String sqlcode_out) throws LACSDDb2Exception, LACSDException {

	
	if (sqlstate_out.trim().equalsIgnoreCase("00000")) {
		//	Do Nothing - SP Success!
	}
	else if (sqlstate_out.trim().equalsIgnoreCase("40001")) {
		throw new LACSDDb2Exception("Stored Procedure Timed Out! - SQLSTATE:" + sqlstate_out);
	}
	else {
		//	Assume any other error code is a custom "RECOVERABLE" exception
		//	Note: Developer must register this code, it's FWD page, and message
		//		  in the controller tier for proper handling
		
		// rollback
		LACSDDb2Exception ex = new LACSDDb2Exception();
		ex.setIsRecoverable(true);
		ex.setErrorCode(sqlstate_out);
		ex.setStoredProcedureName(this.storedProcedureName);
		
		throw ex;
	}
}
/**
 * Returns the logProcedures.
 * @return boolean
 */
protected boolean isLogProcedures() {
	return logProcedures;
}
}
