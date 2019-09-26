package org.lacsd.common.threads;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lacsd.common.process.LACSDReportPO;
import org.lacsd.reporting.values.ReportVO;

/******************************************************************************
//* Copyright (c) 2009 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		ReportEmailThread.java
//* Revision: 		1.0
//* Author:			HHO
//* Created On: 	09-23-2009
//* Modified by:	
//* Modified On:	
//*					
//* Description:	
/******************************************************************************/

public class ReportEmailThread implements Runnable {

    private final String CLASS_NAME = this.getClass().getName();
    protected Logger log = LogManager.getLogger(CLASS_NAME);

    private ReportVO reportVO;

    /**
     * Default Constructor
    */
    public ReportEmailThread(ReportVO reportVO) {
    	super();
    	this.reportVO = reportVO;
    }

    /**
     * TRANSACTION THREAD - RUN REPORTING PROCESS
    */
    public void run() {
    	
        log.debug("ENTERING Report Generation Thread RUN()");
    	
        LACSDReportPO reportPO = new LACSDReportPO();
    	
    	try {
            reportVO = reportPO.doGenerateReport(reportVO);    
        }
        catch (Throwable t) {
        	log.error(t.getMessage());
        } // close catch block

    	log.info("Thread Terminated: [Report Process]");
    }
}

