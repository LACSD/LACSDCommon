package org.lacsd.common.process;

/******************************************************************************
 //* Copyright (c) 2009 Sanitation Districts of Los Angeles. All Rights Reserved.
 //* Filename:       LACSDReportingPO.java
 //* Revision:       1.0
 //* Author:         hho@lacsd.org
 //* Created On:     Jul 15, 2009
 //* Modified By:
 //* Modified On:
 //*                 
 //* Description:	<CLASS DESCRIPTION HERE>
 /******************************************************************************/


import org.lacsd.common.constants.LACSDJNDIConstants;
import org.lacsd.common.exceptions.LACSDException;
import org.lacsd.common.helper.LACSDEJBFinder;
import org.lacsd.common.util.ApplicationContext;
import org.lacsd.report.transaction.ejb.view.ReportTXOBeanRemote;
import org.lacsd.reporting.values.ReportVO;


public class LACSDReportPO extends LACSDGenericPO {
    private static final String JNDI_REPORTTXO      = "ejb/ReportTXO";
    
    private String 	remoteURL;
    
    public LACSDReportPO() {
    	ApplicationContext appctx = ApplicationContext.getInstance();
		remoteURL = appctx.get(LACSDJNDIConstants.REMOTE_IIOP_URL_RESOURCE);
    }
	/**
     * Check if user has iServer license
     * @param userName
     * @return
     * @throws LACSDException
     */
    public boolean isEnterpriseUser(String userName) throws LACSDException {
    	return this.getReportTXOBean().isEnterpriseUser(userName);
    }

    /**
     * Generate report
     * @param reportVO
     * @return
     * @throws LACSDException
     */
    public ReportVO doGenerateReport(ReportVO reportVO) throws LACSDException{
        try {
            reportVO = this.getReportTXOBean().doGenerateReport(reportVO);
        }
        catch (Throwable t) {
            throw (LACSDEJBFinder.evaluateRemoteExceptions(t,remoteURL));
        }
        return reportVO;
	}
    
    /**
     * Get application Reports
     * @param reportVO
     * @return
     * @throws LACSDException
     */
    public ReportVO getApplicationReports (ReportVO reportVO) throws LACSDException{
    	return this.getReportTXOBean().getApplicationReports(reportVO);
    }

    /**
     * Get report parameters
     * @param reportVO
     * @return
     * @throws LACSDException
     */
    public ReportVO getReportParameters(ReportVO reportVO) throws LACSDException{
    	return this.getReportTXOBean().getReportParameters(reportVO);
    }

    /**
     * Get application Reports
     * @param reportVO
     * @return
     * @throws LACSDException
     */
    public ReportVO getAppEdmsArchivedReport (ReportVO reportVO) throws LACSDException{
    	return this.getReportTXOBean().getAppEdmsArchivedReport(reportVO);
    }
 
    /**
     * Get report parameters and exclude EDMS and Email options
     * @param reportVO
     * @return
     * @throws LACSDException
     */
    public ReportVO getArchivedReportParameters(ReportVO reportVO) throws LACSDException{
    	return this.getReportTXOBean().getArchivedReportParameters(reportVO);
    }  
    
    /**
     * Upload a file to iServer
     * @param reportVO
     * @return
     * @throws LACSDException
     */
    public String doUploadFile (ReportVO reportVO) throws LACSDException{
    	return this.getReportTXOBean().doUploadFile(reportVO);
    }

    /**
     * Submit job to iServer and update job status to client until the job is completed.  
     * @param ReportVO
     * @param statusSequence millisecond value to get status from server
     * @throws LACSDException
     */
    public String doSubmitSyncJob(ReportVO ReportVO, int statusSequence) throws LACSDException {
    	return this.getReportTXOBean().doSubmitSyncJob(ReportVO, statusSequence);
    }
    
    /**
     * Search the last time when a certain job is submitted and completed.
     * @param ReportVO
     * @return
     */
    public String  jobLastestCompleteDate(ReportVO ReportVO) throws  Exception {
    	return this.getReportTXOBean().jobLastestCompleteDate(ReportVO);
    }
    
    private ReportTXOBeanRemote getReportTXOBean () throws LACSDException{
	   	 Object remoteObj = LACSDEJBFinder.getRemoteEJB(remoteURL,JNDI_REPORTTXO);
	   	 return ((ReportTXOBeanRemote)remoteObj);
   }
}

