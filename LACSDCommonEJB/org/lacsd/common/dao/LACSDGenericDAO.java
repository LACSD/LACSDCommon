package org.lacsd.common.dao;

/******************************************************************************
//* Copyright (c) 2003 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		LACSDGenericDAO.java
//* Revision: 		1.0
//* Author:			M Feinberg
//* Created On: 	06-25-2003
//* Modified by:	T Nguyen
//* Modified On:	
//*					
//* Description:	Abstract core class for handling Data Connection Policies
//*
//* Modification: 	Introduces multiple static connection policy concepts : ie: Tomcat / ECI
//* 
/******************************************************************************/

import org.lacsd.common.constants.LACSDWebConstants;
import org.lacsd.common.exceptions.LACSDDatabaseException;
import org.lacsd.common.util.*;

public abstract class LACSDGenericDAO {

	public static final String POLICY 					= "policy";
	public static final String POLICY_SIMPLE 			= "simple";
	public static final String POLICY_CONNECTIONPOOL 	= "pool";
	public static final String POLICY_ECI 				= "eci";
	public static final String DRIVER 					= "driver";
	public static final String CONNECTIONSTRING 		= "connectionString";
	public static final String USERID 					= "userid";
	public static final String USERPASSWORD 			= "userPassword";
	public static final String DATA_SOURCE_NAME 		= "dataSourceName";

	/**
	 * Returns a connection policy based on particular database type
	 * @param String databaseType
	 * @return ConnectionPolicy
	 * @throws LACSDDatabaseException
	 */
	protected static ConnectionPolicy getPolicy(String databaseType) throws LACSDDatabaseException {

		ConnectionPolicy policy = null;
		try {
			ApplicationContext env = ApplicationContext.getInstance();	// Singleton
			String policyName = env.get(databaseType + "." + POLICY);
			String serverType = env.get(LACSDWebConstants.SERVER_TYPE);

			if (policyName == null) {
				policyName = POLICY_CONNECTIONPOOL;
			}
			if (serverType == null) {
				serverType = LACSDWebConstants.SERVER_WEBSPHERE;
			}

			if (POLICY_CONNECTIONPOOL.equals(policyName)) {

				// 11-30-05 (MF) - tomcat applications and websphere applications both use 
				// connection pooling data sources, however, they use different JNDI Contexts
				LACSDInitialContext ctx = null;
				if (serverType.equalsIgnoreCase(LACSDWebConstants.SERVER_WEBSPHERE)) {
					ctx = LACSDWSADContext.getInstance();
				}
				else if (serverType.equalsIgnoreCase(LACSDWebConstants.SERVER_TOMCAT)) {
					ctx = LACSDTomcatContext.getInstance();
				}
				else {
					// DEFAULT
					ctx = LACSDWSADContext.getInstance();
				}
				policy = new ConnectionPoolPolicy(databaseType,env,ctx);			
			}
			else if (POLICY_ECI.equals(policyName)) {
				policy = new ConnectionECIPolicy(databaseType,env);
			}
			else if (POLICY_SIMPLE.equals(policyName)) {
				policy = new ConnectionSimplePolicy(databaseType,env);			
			}
			else {
				policy = new ConnectionPoolPolicy(databaseType,env, LACSDWSADContext.getInstance());
				// simple policy not used in J2EE APPS!
				// policy = new ConnectionSimplePolicy(databaseType,env);
			}
		}
		catch (Exception e) {
			throw new LACSDDatabaseException("Error initializing the policy for database type: " + databaseType, e);
		}
		return policy;
	}	
	/**
	 * Returns a connection policy based on particular database type
	 * @param String databaseType
	 * @return ConnectionPolicy
	 * @throws LACSDDatabaseException
	 */
	protected static ConnectionPolicy getPolicy(String databaseType,String userName,String password) throws LACSDDatabaseException {

		ConnectionPolicy policy = null;
		try {
			ApplicationContext env = ApplicationContext.getInstance();	// Singleton
			String policyName = env.get(databaseType + "." + POLICY);
			String serverType = env.get(LACSDWebConstants.SERVER_TYPE);

			if (policyName == null) {
				policyName = POLICY_CONNECTIONPOOL;
			}
			if (serverType == null) {
				serverType = LACSDWebConstants.SERVER_WEBSPHERE;
			}

			if (POLICY_CONNECTIONPOOL.equals(policyName)) {

				// 11-30-05 (MF) - tomcat applications and websphere applications both use 
				// connection pooling data sources, however, they use different JNDI Contexts
				LACSDInitialContext ctx = null;
				if (serverType.equalsIgnoreCase(LACSDWebConstants.SERVER_WEBSPHERE)) {
					ctx = LACSDWSADContext.getInstance();
				}
				else if (serverType.equalsIgnoreCase(LACSDWebConstants.SERVER_TOMCAT)) {
					ctx = LACSDTomcatContext.getInstance();
				}
				else {
					// DEFAULT
					ctx = LACSDWSADContext.getInstance();
				}
				policy = new ConnectionPoolPolicy(databaseType,env,ctx);			
			}
			else if (POLICY_ECI.equals(policyName)) {
				policy = new ConnectionECIPolicy(databaseType,env);
			}
			else if (POLICY_SIMPLE.equals(policyName)) {
				policy = new ConnectionSimplePolicy(databaseType,env,userName,password);			
			}
			else {
				policy = new ConnectionPoolPolicy(databaseType,env, LACSDWSADContext.getInstance());
				// simple policy not used in J2EE APPS!
				// policy = new ConnectionSimplePolicy(databaseType,env);
			}
		}
		catch (Exception e) {
			throw new LACSDDatabaseException("Error initializing the policy for database type: " + databaseType, e);
		}
		return policy;
	}	
}

