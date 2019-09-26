package org.lacsd.common.dao;

/******************************************************************************
//* Copyright (c) 2003 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:       LACSDSqlServerProcDAO.java
//* Revision:       1.0
//* Author:         T Nguyen
//* Created On:     04-10-2006
//* Modified By:    tnguyen@lacsd.org     	
//* Modified On:    06-06-06
//*                 Fix to support Decimal parameter in SP
//*                 Hoa Ho 04/19/2011
//*					Adding ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY 
//* Description:    Abstract core class for handling SQL Server2K  * STORED PROCEDURES *
//*
/******************************************************************************/

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.lacsd.common.exceptions.LACSDException;
import org.lacsd.common.exceptions.LACSDSqlSrvr2KException;
import org.lacsd.common.helper.SQLTypeNullObject;
import org.lacsd.common.service.TimeMarker;


public abstract class LACSDSqlServerProcDAO extends LACSDSqlServerDAO {

private final String CLASS_NAME = this.getClass().getName();
protected Logger log = LogManager.getLogger(CLASS_NAME);

protected String storedProcedureName;

private HashMap<Integer, Object> inputs;     //  Temporary storage of registered inputs
private HashMap<Integer, Integer> outputs;	 //	Temporary storage of registered outputs

public static final int EXECUTE_QUERY           = 1;
public static final int EXECUTE_UPDATE          = 2;
public static final int EXECUTE                 = 3; //For multiple transactions in store procedure

private int queryType = 0;  //  Sub classes determine if this is a SELECT or an INSERT/UPDATE/DELETE

private HashMap<Integer, String> errorMap;    //  Each DAO will register it's SQL error code / custom error code
private static final String RECOVERABLE = "RECOVERABLE";
private boolean logProcedures = true;

public static final int SQL_ERROR_CODE_DUPLICATE_KEY    = 2601;

/**
 * Default constructor 
 * Added by Hoa Ho on 04-20-2011 
 */
public LACSDSqlServerProcDAO() {
	super();
    inputs = new HashMap<Integer, Object>();
	outputs = new HashMap<Integer, Integer>();
    errorMap = new HashMap<Integer, String>();
}
/**
 * Constructor - Takes Stored Procedure name
 * -----------------------------------------------------------------------
 * @param String spname
*/
public LACSDSqlServerProcDAO(String spName) {
	super();
	storedProcedureName = spName;
    inputs = new HashMap<Integer, Object>();
	outputs = new HashMap<Integer, Integer>();
    errorMap = new HashMap<Integer, String>();
}

/**
 * Constructor - Takes Stored Procedure name,database user name and password
 * -----------------------------------------------------------------------
 * @param String spname
*/
public LACSDSqlServerProcDAO(String spName,String userName,String password) {
	super(userName,password);
	storedProcedureName = spName;
    inputs = new HashMap<Integer, Object>();
	outputs = new HashMap<Integer, Integer>();
    errorMap = new HashMap<Integer, String>();
}

/**
 * Overloaded Constructor - Specify whether to use Logging of SP I/O & Time
 * -----------------------------------------------------------------------
 * @param String spname
 * @param boolean useLogging
*/
public LACSDSqlServerProcDAO(String spName, boolean useLogging) {
	this(spName);
	this.logProcedures = useLogging;
	inputs = new HashMap<Integer, Object>();
	outputs = new HashMap<Integer, Integer>();
    errorMap = new HashMap<Integer, String>();
}

/**
 * Constructor - Takes a second SQL Server database name and Stored Procedure name 
 * -----------------------------------------------------------------------
 * @param String spname
*/
public LACSDSqlServerProcDAO(String otherDatabaseName, String spName) {
	super(otherDatabaseName);
	storedProcedureName = spName;
	inputs = new HashMap<Integer, Object>();
	outputs = new HashMap<Integer, Integer>();
    errorMap = new HashMap<Integer, String>();
}

/**
 * Constructor - Takes a second SQL Server database name and Stored Procedure name 
 * database user name and password
 * -----------------------------------------------------------------------
 * @param String spname
*/
public LACSDSqlServerProcDAO(String otherDatabaseName, String spName,String userName,String password) {
	super(otherDatabaseName,userName,password);
	storedProcedureName = spName;
	inputs = new HashMap<Integer, Object>();
	outputs = new HashMap<Integer, Integer>();
    errorMap = new HashMap<Integer, String>();
}

/**
 * PRIMARY EXECUTE METHOD - Contact SQL Server2K Stored Procedure via Prepared Call
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
		registerInputs();
		
		//	3)	Accept Output Parameters from SubClass
		registerOutputs();
		
		//	4)	Build String (?,?,?,?)
        String statementString = this.buildStatementString();
        statement = conn.prepareCall(statementString);
        if (logProcedures) {
        	log.debug(statementString + " Executing with (" + this.getHandshake() + " )");
        	
        }
      
		//	5)	Register Input/Output Parameters into CallableStatement
		registerStatement(statement);
	
		//	6)	EXECUTE & TIME STORED PROCEDURE
        boolean isResultSet = false;
		tm.setTimeMark(1);
        switch (queryType) {
            
            case 0:             throw new LACSDException("Error 001 - DAO Class did not set the Query Type!");
                                    
            case EXECUTE_QUERY: isResultSet = true;
                                 statement.executeQuery();
                                 break;
                                    
            case EXECUTE_UPDATE: statement.executeUpdate();
                                  break;
            case EXECUTE:        isResultSet = true;
            	                 statement.execute();
                                 break;                                  
            
            default:  throw new LACSDException("Error 002 - DAO Set the Query to Type unknown by " + this.getClass().getName());
        }
		tm.setTimeMark(2);
		
		//	7)	Send Results back to (RESULTSET of STRING) methods
		if (isResultSet) {
			//	11/19/2003 - Added Conditional Logging (too noisy)
			if (logProcedures) {
				log.debug("[" + this.storedProcedureName + "] Returned a RESULTSET in: " + tm.compareTimeMilli(1,2) + " milliseconds");
			}
            try {
            	this.getResultsFromResultSet(statement.getResultSet());
            }catch(LACSDException e) {
            	throw e;
            }catch(Exception e) {
            	throw new LACSDSqlSrvr2KException("Error 003 - Failed to transfer data from database layer to application layer. <br>REASON: " + e.getMessage());
            }
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
            log.debug("[" + this.storedProcedureName + "] Returned [" + outputStrings.length + "] STRINGS in: " + tm.compareTimeMilli(1,2) + " milliseconds");
            try {
            	this.getResultsFromString(outputStrings);
            }catch(LACSDException e) {
            	throw e;
            }catch(Exception e) {
            	throw new LACSDSqlSrvr2KException("Error 004 - Failed to transfer data from database layer to application layer. <br>REASON: " + e.getMessage());
            }
		}
	}
    catch (SQLException sqle) {
        //	SQL Exceptions will occur if the stored procedure fails at a high level
        //	ie:  No results come back, JDBC connection failure,  bad IO Handshake, etc
        
        try {
            conn.rollback();	// explicit rollback!  <- Java Framework aborts DB TX
                                // This will happen in the majority of cases, except where
                                // defined by a large transactional SP Container
        }
        catch (Exception e) {
            log.error("**SEVERE** Database Connection Failed - But could not be rolled back!!");
            throw new LACSDSqlSrvr2KException("Error 005 - Failed to execute stored procedure. Database Connection Failed.<br>REASON: " + e.getMessage(), sqle.fillInStackTrace());
        }
        if (sqle.getMessage().startsWith(RECOVERABLE)) {
        	String values[] = sqle.getMessage().split("-");
        	LACSDSqlSrvr2KException ex = new LACSDSqlSrvr2KException();
        	ex.setIsRecoverable(true);
            ex.setErrorCode(values[1]);
            ex.setStoredProcedureName(this.storedProcedureName);            
            throw ex;            
        }else {
	        if (errorMap.containsKey( new Integer(sqle.getErrorCode()) )) {
	            LACSDSqlSrvr2KException ex = new LACSDSqlSrvr2KException();
	            ex.setIsRecoverable(true);
	            ex.setErrorCode((String)errorMap.get( new Integer(sqle.getErrorCode())) );
	            ex.setStoredProcedureName(this.storedProcedureName);            
	            throw ex;            
	        }
	        else
	            throw new LACSDSqlSrvr2KException("Error 006 - Unable to execute stored procedure.<br>REASON: " + sqle.getMessage(), sqle.fillInStackTrace());
        }
    }
    catch (LACSDException le) {
        
        try {
            conn.rollback();    // explicit rollback!  <- Java Framework aborts DB TX
                                // This will happen in the majority of cases, except where
                                // defined by a large transactional SP Container
        }
        catch (Exception e) {
            log.error("**SEVERE** Database Procedure Error - But could not be rolled back!!.<br>REASON: " + e.getMessage());
        }
        throw le;
    }
    finally {
        //	Attempt to close statements and return connections
        try {
            if (statement !=null ) statement.close();
            if (conn != null) {
                super.returnConnection(conn);
            }
        }
        catch (Exception e) {
            throw new LACSDSqlSrvr2KException("Error 007 - Database Connection Failed to Close.<br>REASON:  " + e.getMessage(), e.fillInStackTrace());
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
	return call.toString();
}

//private LACSDSqlSrvr2KException createNewException(SQLException e) {
//	String errorMsg = e.getMessage();
//	String values[] = errorMsg.split("-");
//	LACSDSqlSrvr2KException ex = new LACSDSqlSrvr2KException();
//    ex.setIsRecoverable(true);
//    ex.setErrorCode(values[1]);
//    ex.setStoredProcedureName(this.storedProcedureName);   
//	return ex;
//}
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
        else if (type instanceof BigDecimal) {
            BigDecimal input = (BigDecimal)type;
            statement.setBigDecimal(pos,input);
        }
        else if (type instanceof java.sql.Timestamp) {
            java.sql.Timestamp input = (java.sql.Timestamp)type;
            statement.setTimestamp(pos,input);
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
 * Sets the queryType.
 * @param queryType The queryType to set
 */
public void setQueryType(int queryType) {
    this.queryType = queryType;
}

/**
 * Allow subclass to register 5-char error code for SQL error code 
 * 
 * @param int SQLErrCode
 * @param String errCode
 * @return void
*/
protected synchronized void regRecoverableErr(int SQLErrCode, String errCode) {    
    errorMap.put( new Integer(SQLErrCode), errCode );    
}
public boolean isLogProcedures() {
	return logProcedures;
}
public void setLogProcedures(boolean logProcedures) {
	this.logProcedures = logProcedures;
}

}
