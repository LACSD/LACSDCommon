package org.lacsd.common.helper;

/******************************************************************************
//* Copyright (c) 2004 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		LACSDEJBFinder.java
//* Revision: 		1.0
//* Author:			MFEINBERG@LACSD.ORG
//* Created On: 	11-04-2004
//* Modified by:	
//* Modified On:	
//*					
//* Description:	EJB Finder Helper object
/******************************************************************************/

import java.net.ConnectException;
import java.rmi.MarshalException;
import java.rmi.RemoteException;
import java.util.Hashtable;

import javax.ejb.CreateException;
import javax.ejb.RemoveException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.lacsd.common.constants.LACSDWebConstants;
import org.lacsd.common.exceptions.LACSDDb2Exception;
import org.lacsd.common.exceptions.LACSDException;
import org.lacsd.common.exceptions.LACSDOracle10Exception;
import org.lacsd.common.exceptions.LACSDSqlSrvr2KException;
import org.lacsd.common.util.ApplicationContext;

public abstract class LACSDEJBFinder {

private static final String EJB_URL      = "ReportingServer.iiop_fullpath";	// from environment_resources.properties file


/**
 * Private constructor WITHOUT instance method to restrict direct object use
*/
private LACSDEJBFinder() {
	super();
}

/**
 * Get remote 
 * @param iiopURL
 * @param jndiName
 * @return
 * @throws LACSDException
 */
public static Object getRemoteEJB(String iiopURL, String jndiName) throws LACSDException {
	ApplicationContext apctx = ApplicationContext.getInstance();
	try {
		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY,apctx.getInitialContextFactory());
		env.put(Context.PROVIDER_URL,iiopURL);
		
		Context ctx = new InitialContext(env);
		Object remote = ctx.lookup(jndiName);
		return remote;
	}
	catch(NamingException namex) {
		// cannot find Remote Object
		//throw new LACSDException(apctx.get(LACSDWebConstants.APPLICATION_ID) + " cannot contact system [" + iiopURL + "] -> See Explanation:\n\n" + namex.getExplanation());
		throw evaluateRemoteExceptions(namex, iiopURL);
	}
}

/**
* Get local ejb 
* @param iiopURL
* @param jndiName
* @return
* @throws LACSDException
*/
public static Object getLocalEJB(String jndiName) throws LACSDException {
	ApplicationContext apctx = ApplicationContext.getInstance();
	try {
		Context ctx = new InitialContext();
		
		Object local = ctx.lookup(jndiName);
		return local;
	}
	catch(NamingException namex) {
		throw new LACSDException(apctx.get(LACSDWebConstants.APPLICATION_ID) + " cannot aquire local EJB from JNDI Context -> See Explanation:\n\n" + namex.getExplanation());
	}
}
/**
 * Derive EJB Home Interface - Remote
 * iiopURL - The URL of the remote server will come from resource properties file
 * 
 * @param String jndiName - The JNDI lookup name of the remote object
 * @param String homeObjectClassName - The fully qualified classname of the EJB Home Interface
 * @return Object - The Home Interface
 * @throws LACSDException
*/
public static Object getEJBHomeInterfaceRemote(String jndiName, String homeObjectClassName) throws LACSDException {
	ApplicationContext apctx = ApplicationContext.getInstance();
	String iiopURL = apctx.get(EJB_URL); 
	return getEJBHomeInterfaceRemote(iiopURL,jndiName,homeObjectClassName);
}

	/**
	 * Derive EJB Home Interface - Remote
	 * @param String iiopURL - The URL of the remote server
	 * @param String jndiName - The JNDI lookup name of the remote object
	 * @param String homeObjectClassName - The fully qualified classname of the EJB Home Interface
	 * @return Object - The Home Interface
	 * @throws LACSDException
	*/
	public static Object getEJBHomeInterfaceRemote(String iiopURL, String jndiName, String homeObjectClassName) throws LACSDException {

	ApplicationContext apctx = ApplicationContext.getInstance();

	try {

		/** NOTE:  Port Scanning Logic could be included here to dynamically
		 * 		   find EJB services at the IPADDRESS of the iiopURL without
		 * 		   the server administrator having to explicitly define a port.
		*/
		
		//CorbaScanner corb = new CorbaScanner();
		//int vpt = corb.locateValidORBStrap(iiopURL, jndiName);

		//JNDIScanner scanner = new JNDIScanner();
		//int validPort = scanner.locateValidPort(iiopURL, jndiName);

		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY,apctx.getInitialContextFactory());
		env.put(Context.PROVIDER_URL,iiopURL);
		Context ctx = new InitialContext(env);
		
		java.lang.Object ejbHome = ctx.lookup(jndiName);
		
		Class<?> homeClass = Class.forName(homeObjectClassName);
		Object home = javax.rmi.PortableRemoteObject.narrow(ejbHome,homeClass);
	
		return home;
	}
	catch(NamingException namex) {
		// cannot find Remote Object
		//throw new LACSDException(apctx.get(LACSDWebConstants.APPLICATION_ID) + " cannot contact system [" + iiopURL + "] -> See Explanation:\n\n" + namex.getExplanation());
		throw evaluateRemoteExceptions(namex, iiopURL);
	}
	catch(ClassNotFoundException classex) {
		// cannot find local stubs
		throw new LACSDException("This server is missing the components required to contact remote system:",classex.getException());
	}
}

/**
 * Derive EJB Home Interface - Local
 * @param String jndiName - The JNDI lookup name of the remote object
 * @return Object - The Home Interface
 * @throws LACSDException
*/
public static Object getEJBHomeInterfaceLocal(String jndiName) throws LACSDException {

	ApplicationContext apctx = ApplicationContext.getInstance();

	try {

		Context ctx = new InitialContext();
		java.lang.Object localHome = ctx.lookup("local:ejb/" + jndiName);

		return localHome;
	}
	catch(NamingException namex) {
		// cannot find Local Object
		throw new LACSDException(apctx.get(LACSDWebConstants.APPLICATION_ID) + " cannot aquire local EJB from JNDI Context -> See Explanation:\n\n" + namex.getExplanation());
	}
}

/**
 * Evaluate Remote or CORBA Exceptions -
 * Convenience method for use when calling REMOTE systems via EJB/IIOP.  This method will
 * parse remote exceptions and rethrow them as "user-friendly" LACSDExceptions that can
 * be more easily interpretted by the Struts Web-Tier Framework.
 * @param Throwable t
 * @return void
 * @throws LACSDException
*/
public static LACSDException evaluateRemoteExceptions(Throwable t, String remoteAddress) throws LACSDException {
	
	ApplicationContext apctx = ApplicationContext.getInstance();
	
	if (t instanceof RemoteException) {

		// analyze corba codes for more meaningful err message here!
		if (t instanceof MarshalException) {
			
			MarshalException marex = (MarshalException)t;
			String msg = marex.getMessage();
			
			if (msg == null) {
				throw new LACSDException(apctx.get(LACSDWebConstants.APPLICATION_ID) + " encountered problem while contacting remote system [" + remoteAddress + "]", t.fillInStackTrace());
			}
			else if (msg.startsWith("CORBA.NO_RESPONSE")) {
				throw new LACSDException(apctx.get(LACSDWebConstants.APPLICATION_ID) + " received TIMEOUT while working with remote system [" + remoteAddress + "] - check CORBA timeout setting on remote server!");
			}
			else if (msg.startsWith("CORBA COMM_FAILURE 3")) {
				throw new LACSDException(apctx.get(LACSDWebConstants.APPLICATION_ID) + " cannot contact remote system [" + remoteAddress + "] - remote is down or unavailable!");
			}
			else if (msg.startsWith("CORBA MARSHAL 0x4942f89a")) {
				throw new LACSDException(apctx.get(LACSDWebConstants.APPLICATION_ID) + " partial communication blockage with remote system [" + remoteAddress + "] - server platform version mismatch!");
			}
			else if (msg.startsWith("CORBA MARSHAL 0")) {
				throw new LACSDException(apctx.get(LACSDWebConstants.APPLICATION_ID) + " cannot negotiate with remote system [" + remoteAddress + "] - systems are out of sync!");
			}
			else if (msg.startsWith("CORBA TRANSIENT 0x4942fe02")) {
				throw new LACSDException(apctx.get(LACSDWebConstants.APPLICATION_ID) + " was denied a connection by the remote system [" + remoteAddress + "] - The remote system may not have a STATIC PORT configured properly in ORB Server Setup!  Remote system setup should be checked!  \n\nRESTART [" + remoteAddress + "] to clear up this problem!");
			}
			else {
				throw new LACSDException(apctx.get(LACSDWebConstants.APPLICATION_ID) + " encountered problem while contacting remote system [" + remoteAddress + "]", t.fillInStackTrace());
			}
		}
		else if (t instanceof ConnectException) {
			
			ConnectException conex = (ConnectException)t;
			String msg = conex.getMessage();
			
			if (msg == null) {
				throw new LACSDException(apctx.get(LACSDWebConstants.APPLICATION_ID) + " encountered problem while contacting remote system [" + remoteAddress + "]", t.fillInStackTrace());
			}
			else if (msg.startsWith("CORBA MARSHAL 0x4942f89a")) {
				
				// case example:  WBR IS RUNNING.  RDX IS THE CLIENT.   WBR RESTARTS.   RDX WILL NOW THROW THIS!!
				//
				//				  This happens on the 2nd of 3 remote EJB Hits, EJB HOME.CREATE();
				//				  ...Perhaps due to a stale connection
				
				// attempt to refresh connection here!
				
				
				throw new LACSDException(apctx.get(LACSDWebConstants.APPLICATION_ID) + " partial communication blockage with remote system [" + remoteAddress + "] - server platform version mismatch!");
			}
			else {
				throw new LACSDException(apctx.get(LACSDWebConstants.APPLICATION_ID) + " encountered problem while contacting remote system [" + remoteAddress + "]", t.fillInStackTrace());
			}
		}
		else {
			//added on 11/29/2007 by katielee@lacsd.org to correctly catch CORBA TIME OUT exception 
			String msg = t.getMessage();
			if (msg != null && msg.length() > 0) {
				if (msg.startsWith("CORBA NO_RESPONSE")) {
					throw new LACSDException(apctx.get(LACSDWebConstants.APPLICATION_ID) + " received TIMEOUT while working with remote system [" + remoteAddress + "] - check CORBA timeout setting on remote server!");
				} else {
					throw new LACSDException(apctx.get(LACSDWebConstants.APPLICATION_ID) + " cannot contact remote system [" + remoteAddress + "] with error message " + msg , t.fillInStackTrace());
				}
			} else {
				throw new LACSDException(apctx.get(LACSDWebConstants.APPLICATION_ID) + " cannot contact remote system [" + remoteAddress + "]", t.fillInStackTrace());
			}
		}
	}
	else if (t instanceof CreateException) {
		throw new LACSDException(apctx.get(LACSDWebConstants.APPLICATION_ID) + " cannot CREATE EJB for remote system [" + remoteAddress + "]", t.fillInStackTrace());
	}
	else if (t instanceof RemoveException) {
		throw new LACSDException(apctx.get(LACSDWebConstants.APPLICATION_ID) + " cannot disconnect from [" + remoteAddress + "]", t.fillInStackTrace());
	}
	else if (t instanceof LACSDDb2Exception) {
		LACSDException castEx = (LACSDException)t;
    	castEx.setRootCause(new Throwable(apctx.get(LACSDWebConstants.APPLICATION_ID) + " received DB2 exception from remote system [" + remoteAddress + "] - " + t.getMessage()));
	    throw castEx;
	}
	else if (t instanceof LACSDOracle10Exception) {
		LACSDException castEx = (LACSDException)t;
    	castEx.setRootCause(new Throwable(apctx.get(LACSDWebConstants.APPLICATION_ID) + " received ORACLE exception from remote system [" + remoteAddress + "] - " + t.getMessage()));
	    throw castEx;
	}
	else if (t instanceof LACSDSqlSrvr2KException) {
		LACSDException castEx = (LACSDException)t;
    	castEx.setRootCause(new Throwable(apctx.get(LACSDWebConstants.APPLICATION_ID) + " received SQL Server exception from remote system [" + remoteAddress + "] - " + t.getMessage()));
	    throw castEx;
	}
	else {
		try {
			LACSDException castEx = (LACSDException)t;
			throw castEx;
		}
		catch (ClassCastException cex) {
			throw new LACSDException(t.getMessage(),t.fillInStackTrace());
		}
	}
}
}
