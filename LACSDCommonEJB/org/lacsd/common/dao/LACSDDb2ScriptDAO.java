package org.lacsd.common.dao;

/******************************************************************************
//* Copyright (c) 2005 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:       DB2ScriptDAO.java
//* Revision:       1.0
//* Author:         TNGUYEN@LACSD.ORG
//* Created On:     08-06-2005
//* Modified By:
//* Modified On:
//*
//* Description:	DB2 Script Data Object
//* 
/******************************************************************************/

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.lacsd.common.constants.LACSDPrintConstants;
import org.lacsd.common.constants.LACSDWebConstants;
import org.lacsd.common.exceptions.LACSDDatabaseException;
import org.lacsd.common.exceptions.LACSDException;

public abstract class LACSDDb2ScriptDAO extends LACSDGenericDAO {

private static final String DB2_CONNECT_TYPE = "Db2Connect";
private ConnectionSimplePolicy policy = null;

private final String CLASS_NAME = this.getClass().getName();
protected Logger log = LogManager.getLogger(CLASS_NAME);

private HashMap<Integer, String> db2Commands = new HashMap<Integer, String>(); // Temporary storage of registered db2 commands
private HashMap<Integer, String> parameters = new HashMap<Integer, String>();  // Temporary storage of registered parameters
private String scriptFileName;

/**
 * Constructor - Takes Script file name
 * -----------------------------------------------------------------------
 * @param String spname
*/
public LACSDDb2ScriptDAO(String fileName) {
    super();
    this.scriptFileName = fileName;
    try {
        policy = (ConnectionSimplePolicy)getPolicy(DB2_CONNECT_TYPE);
    }
    catch (LACSDDatabaseException e) {
        //just log here as exception will be thrown in getConnection() method
        //print to console since static initialize
        System.err.println("Error in initialize policy for DB2ScriptDAO" + e);
    }
}

/**
 * PRIMARY EXECUTE METHOD - Run DB2 batch file
 * -----------------------------------------------------------------------
 * Calls a series of sub methods private & protected (subclass implementor)
 * to build and execute DB2 batch file.
 * @return void
 * @throws LACSDException
*/
protected void runScript() throws LACSDException {
    
    db2Commands.clear();
    registerDB2Commands();
    
    parameters.clear();
    registerParameters();

    generateDB2ScriptFile();
    String params = generateParameters();
    
    log.debug("Executing with batch file : " + scriptFileName + " " + params);
    doDB2Command( scriptFileName + " " + params );
    
}


/**
 * Util to run DB2 command, parameter could be DB2 command or DOS batch file
 * of one or more DB2 commands
 * @param String db2cmd
 * @return void
 * @throws LACSDException
*/
public void doDB2Command(String db2cmd) throws LACSDException {
    
    String tempDir = System.getProperty(LACSDWebConstants.TEMP_DIR_KEY);
    
    File workingDir = new File(tempDir);
    if (!workingDir.exists()) {
        workingDir.mkdir();
    }
        
    String cmd = "db2cmd /c /w /i " + db2cmd;

    int outcode = 0;
    
    try {
        Runtime rt = Runtime.getRuntime();
        Process p = rt.exec(cmd, null, workingDir);

        processInput(p.getInputStream());
        processInput(p.getErrorStream());
    
        outcode = p.waitFor(); 
    }
    catch (Throwable e) {
        e.printStackTrace();
        throw new LACSDException(e.getMessage(),e.fillInStackTrace());
    }
    
    if ( outcode != 0 ) {
        throw new LACSDException("Run DB2 command failed! - CODE[" + outcode + "]");      
    }
    
}

/**
 * Generate DB2 script file in temp folder
 * @return void
*/
private void generateDB2ScriptFile() throws LACSDException {

	String tempDir = System.getProperty(LACSDWebConstants.TEMP_DIR_KEY);
    
    String fileName = tempDir + File.separator + scriptFileName + LACSDPrintConstants.BAT_EXT;   

    FileOutputStream out = null; // declare a file output object
    PrintStream p; // declare a print stream object

    try {
        // Create a new file output stream
        // connected to "myfile.txt"
        out = new FileOutputStream(fileName);
    }
    catch ( FileNotFoundException ex ) {
        throw new LACSDException("Cannot open file " + fileName);
    }

    // Connect print stream to the output stream
    p = new PrintStream( out );

    Integer[] params = new Integer[db2Commands.size()];
    Iterator<Integer> it = db2Commands.keySet().iterator();
    int c=0;
    while (it.hasNext()) {
        Integer key = (Integer)it.next();
        params[c] = key;
        c++;
    }
    Arrays.sort(params);    //  Place outputs in numerically ascending order
    
    p.println( "@echo connect to " + this.policy.getConnectionString() + " user " + this.policy.getConnectionID() + " using ******** " );
    p.println( "db2 connect to " + this.policy.getConnectionString() + " user " + this.policy.getConnectionID() + " using " + this.policy.getConnectionPassword() );
            
    for (int i=0; i<params.length; i++) {
        String db2Command = (String)db2Commands.get(params[i]);
        p.println( "@echo \"" + db2Command + "\"" );
        p.println( "db2 \"" + db2Command + "\"" );
    }    
    
    p.println( "@echo terminate" );
    p.println( "db2 terminate" );
    p.close();
 
}

/**
 * Build parameter String for batch file
 * @return String
*/
private String generateParameters() throws LACSDException {

    StringBuffer params = new StringBuffer();

    Integer[] keys = new Integer[parameters.size()];
    Iterator<Integer> it = parameters.keySet().iterator();
    int c=0;
    while (it.hasNext()) {
        Integer key = (Integer)it.next();
        keys[c] = key;
        c++;
    }
    Arrays.sort(keys);    //  Place outputs in numerically ascending order
            
    for (int i=0; i<keys.length; i++) {
        String param = (String)parameters.get(keys[i]);
        params.append( param ).append(" ");
    }
    return params.toString();   
}

/**
 * REGISTER DB2 COMMANDS - (SUBCLASS STEP #1)
 * -----------------------------------------------
 * Subclass must implement this method so that 
 * all db2 commands will be called in the batch
 * file
 * -----------------------------------------------
 * Usage: >>    super.setDB2Command(1, "db2Command")
 *              super.setDB2Command(2, "db2Command");
*/
protected abstract void registerDB2Commands() throws LACSDException;

/**
 * REGISTER PARAMETERS - (SUBCLASS STEP #2)
 * -----------------------------------------------
 * Subclass must implement this method so that 
 * script I/O handshake matches what
 * is expected by batch input signature
 * -----------------------------------------------
 * Usage: >>    super.setParameter(1, "param1")
 *              super.setParameter(2, "param2");
*/
protected abstract void registerParameters() throws LACSDException;

/**
 * Takes an inputstream, and uses BufferedReader.readLine() to
 * take in text line-by-line, then add it all to one big string
*/ 
protected abstract void processInput(InputStream is) throws Throwable;


/**
 * Sets a DB2 command into the batch file
 * @param int position ++ error parms
 * @param String db2Command
 * @return void
*/
protected void setDB2Command(int position, String db2Command) {    
    Integer pos = new Integer(position);
    db2Commands.put(pos,db2Command);
}

/**
 * Sets a parameter of the batch Input
 * @param int position ++ error parms
 * @param String parameter
 * @return void
*/
protected void setParameter(int position, String parameter) {    
    Integer pos = new Integer(position);
    parameters.put(pos,parameter);
}

}
