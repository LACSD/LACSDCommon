package org.lacsd.common.dao;

/******************************************************************************
//* Copyright (c) 2006 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		LACSDOracleProcDAO.java
//* Revision: 		1.0
//* Author:			MFEINBERG@LACSD.ORG
//* Created On: 	08-09-2006
//* Modified by:	
//* Modified On:	
//*					
//* Description:	Abstract core class for handling Oracle10g PL/SQL SP'S
/******************************************************************************/
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;

import oracle.jdbc.OracleTypes;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.lacsd.common.exceptions.LACSDException;
import org.lacsd.common.exceptions.LACSDOracle10Exception;
import org.lacsd.common.service.TimeMarker;

public abstract class LACSDOracleProcDAO extends LACSDOracleDAO {

private final String CLASS_NAME = this.getClass().getName();
protected Logger log = LogManager.getLogger(CLASS_NAME);

protected String storedProcedureName;

private HashMap<Integer, Object> inputs;	 // Temporary storage of registered inputs
private TreeMap<Integer, Integer> outputs; // Temporary storage of registered outputs

public static final int EXECUTE_QUERY           = 1;
public static final int EXECUTE_UPDATE          = 2;

private int queryType = 0;  //  Sub classes determine if this is a SELECT or an INSERT/UPDATE/DELETE

private boolean logProcedures = true;

/**
 * Constructor - Takes Stored Procedure name
 * -----------------------------------------------------------------------
 * @param String spname
*/
public LACSDOracleProcDAO(String spName) {
	super();
	storedProcedureName = spName;
	inputs = new HashMap<Integer, Object>();
	outputs = new TreeMap<Integer, Integer>();
}

/**
 * Overloaded Constructor - Specify whether to use Logging of SP I/O & Time
 * -----------------------------------------------------------------------
 * @param String spname
 * @param boolean useLogging
*/
public LACSDOracleProcDAO(String spName, boolean useLogging) {  
	this(spName);
	this.logProcedures = useLogging;
}


public LACSDOracleProcDAO(String otherDatabaseName, String spName) {
	super(otherDatabaseName);
	storedProcedureName = spName;
	inputs = new HashMap<Integer, Object>();
	outputs = new TreeMap<Integer, Integer>();
}

/**
 * PRIMARY EXECUTE METHOD - Contact Oracle Stored Procedure via Prepared Call
 * -----------------------------------------------------------------------
 * Calls a series of sub methods private & protected (subclass implementor)
 * to build and execute a stored procedure call.
 * @return void
 * @throws LACSDException
*/
public void execute() throws LACSDException {

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
		//if (logProcedures) {
			log.debug(statementString + " Executing with (" + this.getHandshake() + ")");
		//}


		//	5)	Register Input/Output Parameters into CallableStatement
		registerStatement(statement);
	
	
		//	6)	EXECUTE & TIME STORED PROCEDURE
		tm.setTimeMark(1);
		switch (queryType) {
			
				case 0:					throw new LACSDException("DAO Class did not set the Query Type!");
	                                    
				case EXECUTE_QUERY:		statement.executeQuery();
										break;
	                                    
				case EXECUTE_UPDATE: 	statement.executeUpdate();
									  	break;
	            
				default:				throw new LACSDException("DAO Set the Query to Type unknown by " + this.getClass().getName());
		}
		tm.setTimeMark(2);
	
	
		//	7)	Send Results back to (RESULTSET of STRING) methods
		if (logProcedures) {
			log.debug("[" + this.storedProcedureName + "] Executed in: " + tm.compareTimeMilli(1,2) + " milliseconds");
		}
		// Note: Cursor must be pulled explicitly from the output parameter from which it was set!
		ArrayList<String> outputStrings = new ArrayList<String>();
		Iterator<Integer> it = outputs.keySet().iterator();
		while (it.hasNext()) {
			Integer key = (Integer)it.next();
			Integer value = (Integer)outputs.get(key);
			if (value.intValue() == OracleTypes.CURSOR) {
				ResultSet rs = (ResultSet)statement.getObject(key.intValue());
				this.getResultsFromResultSet(rs);
				rs.close();
			}
			else {
				String out = String.valueOf(statement.getObject(key.intValue()));
				outputStrings.add(out);
			}
		}
		if (outputStrings.size() > 0) {
			String[] outputStringArray = (String[])outputStrings.toArray();
			this.getResultsFromString(outputStringArray);
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
		throw new LACSDOracle10Exception("Unable to execute stored procedure", sqle.fillInStackTrace());
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
			throw new LACSDOracle10Exception("Connections Failed to Close", e.fillInStackTrace());
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

	Integer pos = new Integer((position) + inputs.size());
	outputs.put(pos,new Integer(type));
}

/**
 * Build parameter String for Callable Statement
 * @return String
*/
private String buildStatementString() throws LACSDException {

	// eg: conn.prepareCall("BEGIN SYNRPRT001SP(?,?); END;");
	StringBuffer call = new StringBuffer();
	call.append("BEGIN ");
	call.append(this.storedProcedureName);
	call.append("(");
	
	for (int i=0; i<inputs.size(); i++) {
		if (i==0) {
			call.append("?");
		}
		else {
			call.append(",?");
		}
	}
	
	for (int i=0; i<outputs.size(); i++) {
		if ((inputs.size() == 0) && (i==0)) {
			call.append("?");
		}
		else {
			call.append(",?");
		}
	}
	call.append("); END;");

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
		else if (type instanceof java.sql.Date) {
			java.sql.Date input = (java.sql.Date)type;
			statement.setDate(pos,input);
		}
		else if (type instanceof java.sql.Timestamp) {
			java.sql.Timestamp input = (java.sql.Timestamp)type;
			statement.setTimestamp(pos,input);
		}
        else if (type instanceof java.util.Date) {
            java.util.Date dt = (java.util.Date)type;
            java.sql.Timestamp input = new java.sql.Timestamp(dt.getTime());
            statement.setTimestamp(pos,input);
        }
		else {
			statement.setObject(pos,type);
		}
	}
	
	// Note:  Oracle SP CALL will have output params AFTER input params
	// eg: stmt.registerOutParameter(2, OracleTypes.CURSOR); //REF CURSOR
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
		if (i==0) {
			sb.append(input);
		}
		else {
			sb.append("," + input);
		}
	}
	
	//	Append Output Params
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
		if ((inputs.size() == 0) && (i==0)) {
			sb.append(output);
		}
		else {
			sb.append("," + output);
		}
	}
	return sb.toString();
}

/**
 * Sets the queryType.
 * @param queryType The queryType to set
 */
public void setQueryType(int queryType) {
	this.queryType = queryType;
}
}
