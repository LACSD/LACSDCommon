package org.lacsd.common.process;

import org.lacsd.common.constants.LACSDJNDIConstants;

/******************************************************************************
//* Copyright (c) 2005 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		EdmsPO.java
//* Revision: 		1.0
//* Author:			MFEINBERG@LACSD.ORG
//* Created On: 	06-16-2005
//* Modified by:	
//* Modified On:	
//*					
//* Description:	WSX Proxy object for Remote EDMS System
/******************************************************************************/

import org.lacsd.common.exceptions.LACSDException;
import org.lacsd.common.helper.LACSDEJBFinder;
import org.lacsd.common.util.ApplicationContext;
import org.lacsd.common.values.EdmsGetVO;
import org.lacsd.common.values.EdmsPutVO;
import org.lacsd.common.values.EdmsSQLVO;
import org.lacsd.common.values.EdmsSearchVO;
import org.lacsd.report.business.ejb.view.EdmsBOBeanRemote;

public class EdmsPO extends LACSDGenericPO {

	private String 	remoteURL;
		
	public EdmsPO() {
		ApplicationContext appctx = ApplicationContext.getInstance();
		remoteURL = appctx.get(LACSDJNDIConstants.REMOTE_IIOP_URL_RESOURCE);
	}
	
	/**
	 * Public Accessor method for performing document SEARCH FUNCTION
	 * Note: There is no synchronization or load-compensating logic
	 * @param EdmsSearchVO edmsSearchVO
	 * @return EdmsSearchVO
	 * @throws LACSDException
	*/
	public EdmsSearchVO doSearch(EdmsSearchVO edmsSearchVO) throws LACSDException {
		return this.getEdmsBOBean().doSearch(edmsSearchVO);
	}
	
	/**
	 * Public Accessor method for performing document GET FUNCTION
	 * Note: There is no synchronization or load-compensating logic
	 * @param EdmsGetVO edmsGetVO
	 * @return EdmsGetVO
	 * @throws LACSDException
	*/
	public EdmsGetVO doGet(EdmsGetVO edmsGetVO) throws LACSDException {
		return this.getEdmsBOBean().doGet(edmsGetVO);
	}
	
	/**
	 * Public Accessor method for performing document PUT FUNCTION
	 * Note: There is no synchronization or load-compensating logic
	 * @param EdmsPutVO edmsPutVO
	 * @return EdmsPutVO
	 * @throws LACSDException
	 * @throws Throwable
	*/
	public void doPutDocument(EdmsPutVO edmsPutVO) throws LACSDException {
		this.getEdmsBOBean().doPutDocument(edmsPutVO);
	}
	
	/**
	 * 
	 * @param edmsSQLVO
	 * @return
	 * @throws LACSDException
	 */
	public EdmsSQLVO doExecute(EdmsSQLVO edmsSQLVO) throws LACSDException {
		return this.getEdmsBOBean().doExecute(edmsSQLVO);
	}
	
	/**
	 * Public Accessor method for performing document PUT FUNCTION
	 * Note: There is no synchronization or load-compensating logic
	 * @param EdmsPutVO edmsPutVO
	 * @return EdmsPutVO
	 * @throws LACSDException
	 * @throws Throwable
	*/
	public EdmsPutVO doInsertDocument(EdmsPutVO edmsPutVO) throws LACSDException {
		return this.getEdmsBOBean().doInsertDocument(edmsPutVO);
	}
	
	private EdmsBOBeanRemote getEdmsBOBean () throws LACSDException{
		 Object remoteObj = LACSDEJBFinder.getRemoteEJB(remoteURL,LACSDJNDIConstants.JNDI_EDMSBO);
		 return ((EdmsBOBeanRemote)remoteObj);
	}
}
