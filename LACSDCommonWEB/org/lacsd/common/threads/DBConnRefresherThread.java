package org.lacsd.common.threads;

/******************************************************************************
//* Copyright (c) 2004 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		DBConnRefresherThread.java
//* Revision: 		1.1
//* Author:			MFEINBERG@LACSD.ORG
//* Created On: 	09-07-2004
//* Modified by:	
//* Modified On:	
//*					
//* Description:	Thread Wrapper for Refreshing DB2 Connection Pool
/******************************************************************************/
import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lacsd.common.dao.LACSDSqlServerDAO;
import org.lacsd.common.dao.LACSDSqlSrvrConnRefresherDAO;
import org.lacsd.common.dao.OracleConnRefreshDAO;
import org.lacsd.common.util.ApplicationContext;

public class DBConnRefresherThread implements Runnable {

private final String CLASS_NAME = this.getClass().getName();
protected Logger log = LogManager.getLogger(CLASS_NAME);
private static final int CLEANUP_INTERVAL_MINUTES 	= 60;	// query a connection from DBPool every hour


/**
 * MAINTENENCE THREAD - REFRESH STALE CONNECTIONS
*/
public void run() {
	
	log.debug("INITIALIZING DB Connection Refresher Thread");
	
	while (true) {
		try {Thread.sleep(CLEANUP_INTERVAL_MINUTES * 60000);} catch(InterruptedException ioe) {}
		System.out.println("DB Connection Refresher Thread - Scheduled Maintenance");
		triggerConnection();
	}
}


/**
 * Attempt to refresh DB2 Connection Pool
 * @return void
*/
private void triggerConnection() {

	try {
		ApplicationContext env = ApplicationContext.getInstance();	// Singleton
		Iterator<String> keys = env.getKeys();
		//loop through resources to refresh all database connections
		while(keys.hasNext()) {
			String key = keys.next();
			if (key.indexOf(LACSDSqlServerDAO.DATA_SOURCE_NAME) != - 1 ) {
				String datasource = key.substring(0, key.indexOf(LACSDSqlServerDAO.DATA_SOURCE_NAME) -1 );
				if (key.toLowerCase().indexOf("oracle") != -1) {
					log.debug("Refresh Oracle datasource: " + datasource) ;
					try {
						OracleConnRefreshDAO oracleConnRefreshDAO = new OracleConnRefreshDAO(datasource);
						oracleConnRefreshDAO.doRefreshDB();
					}catch(Exception e) {
						log.debug("CANNOT Refresh Oracle datasource: " + datasource + ".  Reason: " + e.getMessage()) ;
					}
					
				}else {
					log.debug("Refresh SQL Server datasource: " + datasource) ;
					try {
						LACSDSqlSrvrConnRefresherDAO refresherSqlSrvrDAO = new LACSDSqlSrvrConnRefresherDAO(datasource);
						refresherSqlSrvrDAO.doRefreshDB();
					}catch (Exception e) {
						log.debug("CANNOT Refresh SQL Server datasource: " + datasource + ".  Reason: " + e.getMessage()) ;
					}
				}
			}
		}
		
	
	}
	catch (Throwable t) {
		log.debug("Could not refresh DB Connection: " + t.getMessage(),t);
	}
}
}
