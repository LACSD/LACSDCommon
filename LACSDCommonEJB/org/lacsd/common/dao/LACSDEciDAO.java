package org.lacsd.common.dao;

/******************************************************************************
//* Copyright (c) 2003 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		LACSEciDAO.java
//* Revision: 		1.0
//* Author:			tnguyen@lacsd.org
//* Created On: 	05-25-2005
//* Modified by:	
//* Modified On:	
//*					
//* Description:	Abstract core class for handling ECI call
/******************************************************************************/

import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.lacsd.common.exceptions.LACSDDatabaseException;
import org.lacsd.common.exceptions.LACSDEciException;
import org.lacsd.common.exceptions.LACSDException;

import com.ibm.ctg.client.ECIRequest;
import com.ibm.ctg.client.JavaGateway;

public abstract class LACSDEciDAO extends LACSDGenericDAO {

protected Logger log = LogManager.getLogger(this.getClass().getName());

private static final String CICS_DATASOURCE_TYPE = "CicsDatasource";
private ConnectionPolicy policy = null;
private String CICSServerName;
private String CICSProgramName;

/**
 * Default Constructor
*/
public LACSDEciDAO(String serverName, String programName) {
	super();
	
    this.CICSServerName = serverName;
    this.CICSProgramName = programName;
    
	try {
		policy = getPolicy(CICS_DATASOURCE_TYPE);
	}
	catch (LACSDDatabaseException e) {
		//just log here as exception will be thrown in getConnection() method
		//print to console since static initialize
		System.err.println("Error in initialize policy for LACSDEciDAO" + e);
	}
}

/**
 * Answers with a connection.
 * @return java.sql.Connection
*/
public JavaGateway getConnection() throws LACSDDatabaseException {
	
	if (policy == null) {
		throw new LACSDDatabaseException("Policy was not set for the: " + CICS_DATASOURCE_TYPE);
	}
	
	try {
		return (JavaGateway)policy.getConnection();
	}
	catch (Exception e) {
		throw new LACSDDatabaseException("Error getting the Connection from policy for the: " + CICS_DATASOURCE_TYPE, e);
	}	
}

/**
 * Returns a connection to the pool.
 * @param connection java.sql.Connection
 * @return void
*/
public void returnConnection(JavaGateway connection) throws Exception {
	
	if (policy == null) {
		throw new LACSDDatabaseException("Policy was not set for the: " + CICS_DATASOURCE_TYPE);
	}
	
	if (connection == null) {
		throw new LACSDDatabaseException("Connection being returned to " + CICS_DATASOURCE_TYPE + " is null");
	}

	policy.returnConnection(connection);
}

/**
 * REGISTER INPUT PARAMETERS - (SUBCLASS STEP #1)
 * -----------------------------------------------
 * Subclass must implement this method so that 
 * stored procedure I/O handshake matches what
 * is expected by stored procedure input signature
 * -----------------------------------------------
 * Usage: >>    super.setInputParam(1, Integer(12345))
 *              super.setInputParam(2,"MyInput");
*/
protected abstract String registerInput() throws LACSDException;

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
protected abstract void getResultsFromString(String output) throws LACSDException;

/**
 * PRIMARY EXECUTE METHOD - Call CICS programm using IBM ECI API
 * -----------------------------------------------------------------------
 * @return void
 * @throws LACSDException
*/
public void execute() throws LACSDException {

    JavaGateway jgaConnection = null;
    try {
        
        //  1)  Acquire Java Gateway connection
        jgaConnection = this.getConnection();
        
        ECIRequest eciRequest = null;

        String input = this.registerInput();
        byte[] buffer = input.getBytes();

        eciRequest = new ECIRequest( ECIRequest.ECI_SYNC,
                                     CICSServerName,                 // CICS Server
                                     policy.getConnectionID(),       // user 
                                     policy.getConnectionPassword(), // password
                                     CICSProgramName,                // Program name
                                     "CMPI",                         // Transaction ID
                                     buffer,                         // Common Area
                                     buffer.length,                  // Common Area Length
                                     ECIRequest.ECI_NO_EXTEND, 
                                     ECIRequest.ECI_LUW_NEW);

        eciRequest.Cics_Rc = 0;

        // Set ECI_NO_EXTEND on the last call only, otherwise
        // set ECI_EXTENDED
        eciRequest.Extend_Mode = ECIRequest.ECI_NO_EXTEND;

        log.debug( " Executing with (" + input.trim() + ") Commarea length is : " + buffer.length );
        // Flow the request via the JGate to CICS
        jgaConnection.flow(eciRequest);
        buffer = eciRequest.Commarea;
        
        this.getResultsFromString(new String( buffer ));
        
    }
    catch (IOException ioe) {
        throw new LACSDEciException("ECI Failed to run", ioe.fillInStackTrace());
    }
    finally {
        //  Attempt to return connections
        try {
            if (jgaConnection != null) {
                this.returnConnection(jgaConnection);
            }
        }
        catch (Exception e) {
            throw new LACSDEciException("Connections Failed to Close", e.fillInStackTrace());
        }
    }
}

}
