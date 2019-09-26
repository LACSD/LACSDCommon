package org.lacsd.common.util;

/******************************************************************************
//* Copyright (c) 2010 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:       LACSDErrorAnalyzer.java
//* Revision:       1.0
//* Author:         Hoa Ho
//* Created On:     06-16-2010
//* Modified by:    HHO
//* Modified On:      
//*                 
//* Description:   Error message and exception analyzer.  These methods used to be in LACSDGenericAction
//*                
/******************************************************************************/

import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import org.lacsd.common.constants.LACSDWebConstants;
import org.lacsd.common.exceptions.LACSDDb2Exception;
import org.lacsd.common.exceptions.LACSDEciException;
import org.lacsd.common.exceptions.LACSDException;
import org.lacsd.common.exceptions.LACSDOracle10Exception;
import org.lacsd.common.exceptions.LACSDSqlSrvr2KException;


public class LACSDErrorAnalyzer {

	private HashMap<String, Object> errorForwardMap;
	public static final String MESSAGE_RECOVERABLE_NULL       =     "error.recoverable.notfound";

	public LACSDErrorAnalyzer() {
		this.errorForwardMap = new HashMap<String, Object>();
	}
	
	/**
	 * Examine LACSDException at runtime - Determine if Fatal or Recoverable
	 * @param LACSDException e
	 
	*/
	public String examineLACSDException(LACSDException e, HttpServletRequest req) {

	    // determine if recoverable or not
	    if (e.isRecoverable()) {
	            
	        //  12-17-2003 -> Get recoverable message key and forward pages from Action Registry
	        LACSDErrorMapping eMapping = getErrorMapping(e);

	        //  This exception will occur if the developer neglected to register a recoverable error code
	        //  in the initialization sequence of his struts action.
	        if (eMapping == null) {
	            
	            // error was originally deemed recoverable, but has not been registered - 
	            // if this error was sent from a stored procedure, give a "special" message with SP Info
	            if (e instanceof LACSDDb2Exception) {
	                this.addError(LACSDWebConstants.ERROR_MESSAGE,LACSDWebConstants.MESSAGE_APP_FAILURE,e.getUnhandledLACSDErrorCode(),  req);
	                return LACSDWebConstants.FORWARD_DB2_FAILURE;
	            }
	            else if (e instanceof LACSDSqlSrvr2KException) {
	                this.addError(LACSDWebConstants.ERROR_MESSAGE,LACSDWebConstants.MESSAGE_APP_FAILURE,e.getUnhandledLACSDErrorCode(),  req);
	                return LACSDWebConstants.FORWARD_SQL2K_FAILURE;
	            }
	            //03-27-2007 ->  Added Oracle Exception Handling
	            else if (e instanceof LACSDOracle10Exception) {
	                this.addError(LACSDWebConstants.ERROR_MESSAGE,LACSDWebConstants.MESSAGE_APP_FAILURE,e.getUnhandledLACSDErrorCode(), req);
	                return LACSDWebConstants.FORWARD_ORACLE_FAILURE;
	            }                        
	            else {
	                return processNonRecoverableException(e, req);
	            }
	        }
	                
	        String errorMessageKey = eMapping.getErrorMessageKey();
	        this.addError(LACSDWebConstants.ERROR_MESSAGE,errorMessageKey,e.getRootCause(), req);
	                
	        //  REVISED 12-17-2003 ->   3% of Actions have Dynamic FWD Key (derived from VO)
	        //                          These forwards are set into LACSDException at the DAO Tier
	        //                          Example:  RDXSwitchAccount Use Case
	        if (e.isDynamicForward()) {
	            return e.getDynamicForwardURL();
	        }

	        
	        //  REVISED 06-10-2004 ->   Just as above, some actions will have Dynamic FWD Key (derived)
	        //                          from the Form itself (not DAO Tier)
	        //                          Example:    RDXReport Use Case
	        if (eMapping.isForwardFullyQualifiedURI()) {
	            return eMapping.getForwardPageKey(); // note this is not a KEY.  it is the URI.
	        }
	        else {
	            String recoverableFwd  = eMapping.getForwardPageKey();
	            if (recoverableFwd == null) {
	                this.addError(LACSDWebConstants.ERROR_MESSAGE,LACSDWebConstants.MESSAGE_APP_FAILURE,MESSAGE_RECOVERABLE_NULL, req, true);
	                return LACSDWebConstants.FORWARD_APP_FAILURE;
	            }
	            return recoverableFwd;
	        }
	    }
	    else {
	        return processNonRecoverableException(e, req);
	    }
	}
	
	
	/**
	 * Handle exceptions that are deemed "Non-Recoverable" and forward 
	 * to a generic system "splat" page
	 * @param LACSDException e
	 * @param ActionMapping mapping
	 * @param HttpServletRequest req
	 * @return ActionForward
	 * @throws Throwable
	*/
	private String processNonRecoverableException(LACSDException e, HttpServletRequest req) {

	    // DB2 Splat Page
	    if (e instanceof LACSDDb2Exception) {
	        this.addError(LACSDWebConstants.ERROR_MESSAGE,LACSDWebConstants.MESSAGE_DB2_FAILURE,e.getMessage() ,  req);
	        return LACSDWebConstants.FORWARD_DB2_FAILURE;
	    }

	    // SQL Server 2000 Splat Page
	    else if (e instanceof LACSDSqlSrvr2KException) {
	        this.addError(LACSDWebConstants.ERROR_MESSAGE,LACSDWebConstants.MESSAGE_SQL2K_FAILURE,e.getMessage(), req);
	        return LACSDWebConstants.FORWARD_SQL2K_FAILURE;
	    }

	    // 03/27/2007 -> Added Oracle Handling
	    // Oracle Splat Page
	    else if (e instanceof LACSDOracle10Exception) {
	        this.addError(LACSDWebConstants.ERROR_MESSAGE,LACSDWebConstants.MESSAGE_ORACLE_FAILURE,e.getMessage() ,  req);
	        return LACSDWebConstants.FORWARD_ORACLE_FAILURE;
	    }

	    // ECI Splat Page
	    else if (e instanceof LACSDEciException) {
	        this.addError(LACSDWebConstants.ERROR_MESSAGE,LACSDWebConstants.MESSAGE_ECI_FAILURE,e.getMessage(),  req);
	        return LACSDWebConstants.FORWARD_ECI_FAILURE;
	    }

	    // BY DEFAULT ->> All remaining unknown / unhandled exceptions are JAVA APPLICATION SPLAT PAGE!
	    else {
	    	if (e.getRootCause() != null) {
	            this.addError(LACSDWebConstants.ERROR_MESSAGE,LACSDWebConstants.MESSAGE_APP_FAILURE,e.getMessage() ,req);
	    	} else {
	    		this.addError(LACSDWebConstants.ERROR_MESSAGE,LACSDWebConstants.MESSAGE_APP_FAILURE,e.getMessage() ,  req);
	    	}
	        return LACSDWebConstants.FORWARD_APP_FAILURE;
	    }
	}
	
	
	/**
	 * Add ActionErrors to request object
	 * @param String errKey (for JSP Screen)
	 * @param String propertyFileKey (for Text File)
	 * @param HttpServletRequest req
	 * @return void
	*/
	public void addError(String errKey, String propertyFileKey, HttpServletRequest req) {
	    this.addError(errKey, propertyFileKey, null, req);
	}

	/**
	 * Add ActionErrors to request object
	 * @param String errKey (for JSP Screen)
	 * @param String propertyFileKey (for Text File)
	 * @param String dynamicMessage (for rootCause)
	 * @param HttpServletRequest req
	 * @return void
	*/
	public void addError(String errKey, String propertyFileKey, Object dynamicCause, HttpServletRequest req) {
	    
		addError(errKey,propertyFileKey,dynamicCause,req,false);

	}
	
	/**
	 * Add ActionErrors to request object
	 * - Get Cause Message from Message Key from error properties file (if boolean is true)
	 * otherwise, use the message sent in from caller (@param dynamicMessage)
	 * 
	 * @param String errKey (for JSP Screen)
	 * @param String propertyFileKey (for Text File)
	 * @param String dynamicMessage (for rootCause)
	 * @param HttpServletRequest req
	 * @param boolean dynaCauseIsKeyed
	 * @return void
	*/
	public void addError(String errKey, String propertyFileKey, Object dynamicCause, HttpServletRequest req, boolean dynaCauseIsKeyed) {
	    
	    ResourceBundle rb = ResourceBundle.getBundle(LACSDWebConstants.MESSAGE_RESOURCES_FILE, new Locale("en","US"));
	    String message = rb.getString(propertyFileKey);
	    req.setAttribute(LACSDWebConstants.ERROR_MESSAGE,message);
	 
	    if (dynamicCause != null) {
	        
	        if (dynaCauseIsKeyed) {
	            String cause = rb.getString(dynamicCause.toString());
	            req.setAttribute(LACSDWebConstants.ERROR_MESSAGE,cause);
	        }
	        else {
	        	req.setAttribute(LACSDWebConstants.ERROR_MESSAGE,dynamicCause.toString());
	        }
	    }

	}
	
	/**
	 * Allow subclass to register parameters for error messages 
	 * and page forwarding constants..
	 * 
	 * @see standard usage signature overloaded below
	 * 
	 * ADDED 06/10/2004 
	 * Overloaded version of regRecoverableErr per Reporting requirement to return to a 
	 * dynamically registered input screen.  Extra boolean value in eMapping class will
	 * indicate that the fully qualified URI String, eg "bonds.do?actionName=setup" should
	 * be used by the controller to redirect, instead of the standard actionName string,
	 * eg: "setup".
	 * 
	 * @param String errCode
	 * @param String spName
	 * @param String errorMessage
	 * @param String forwardPage
	 * @param boolean useDynamicInputFwd
	 * @return void
	*/
	public synchronized void regRecoverableErr(String errCode, String spName, String errorMessage, String forwardPage, boolean useDynamicInputFwd) {
	    
	    LACSDErrorMapping eMapping = new LACSDErrorMapping();
	    
	    eMapping.setErrCode(errCode);
	    eMapping.setStoredProcedureName(spName);
	    eMapping.setErrorMessageKey(errorMessage);
	    eMapping.setForwardPageKey(forwardPage);

	    //  06-10-2004 - added boolean parameter to eMapping to determine "standard" action Fwd or "fully qualified" forward
	    if (useDynamicInputFwd) {
	        eMapping.setForwardFullyQualifiedURI(useDynamicInputFwd);
	    }

	    //  05-11-04 - expanded error mapping to hold multiple stored procedures
	    String errKey = (spName + errCode);
	    if (!errorForwardMap.containsKey(errKey)) {
	        errorForwardMap.put(errKey,eMapping);
	    }
	    else {
	        if (useDynamicInputFwd) {  // herein lies a possible race-condition when erroring from the same report, but coming from different inputs
	            errorForwardMap.remove(errKey); // this is dynamic (per reporting screens, etc) so remove old one
	            errorForwardMap.put(errKey,eMapping);
	        }
	    }
	}

	/**
	 * Convenience method for the error-handling section of
	 * the Execute() method to derive the appropriate error
	 * mapping from an LACSD Recoverable exception
	 * @param LACSDException
	 * @return LACSDErrorMapping
	*/
	private LACSDErrorMapping getErrorMapping(LACSDException e) {

	    LACSDErrorMapping theMapping = null;
	    
	    Iterator<String> it = errorForwardMap.keySet().iterator();
	    while (it.hasNext()) {
	        String key = (String)it.next();
	        LACSDErrorMapping map = (LACSDErrorMapping)errorForwardMap.get(key);
	        String errCode = map.getErrCode();
	        String spcName = map.getStoredProcedureName();
	        
	        // patch 09-16-04 (MF) - e.getProcName may have bug introduced from old WBR admin screen
	        e.setStoredProcedureName(e.getStoredProcedureName().trim());
	        
	        if (errCode != null && spcName != null) {
	            // patch 11-19-04 (MF) - db2 8 causes error codes to have white space at the end - trim() added
	            if ((errCode.equalsIgnoreCase(e.getErrorCode().trim())) && (spcName.equalsIgnoreCase(e.getStoredProcedureName()))) {
	                theMapping = map;
	                break;
	            }
	        }
	    }
	    return theMapping;
	}

	public HashMap<String, Object> getErrorForwardMap() {
		return errorForwardMap;
	}

	public void setErrorForwardMap(HashMap<String, Object> errorForwardMap) {
		this.errorForwardMap = errorForwardMap;
	}

	
}
