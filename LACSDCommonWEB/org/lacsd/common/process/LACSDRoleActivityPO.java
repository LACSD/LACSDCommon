package org.lacsd.common.process;

/******************************************************************************
//* Copyright (c) 2003 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		LACSDRoleActivityPO.java
//* Revision: 		1.6
//* Author:			YGORELIK@LACSD.ORG;  MFEINBERG@LACSD.ORG;
//* Created On: 	9-29-03
//* Modified By:    TNGUYEN@LACSD.ORG
//* Modified On:    09-22-04
//*                 Move to OSX to be used by all applications 
//*					
//* Description:	Process object for Role Activities Use Case
/******************************************************************************/

import org.lacsd.common.constants.LACSDJNDIConstants;
import org.lacsd.common.exceptions.LACSDException;
import org.lacsd.common.helper.LACSDEJBFinder;
import org.lacsd.common.util.ApplicationContext;
import org.lacsd.common.values.EmployeeRoleVO;
import org.lacsd.osx.dao.ejb.view.RoleActivityDAOBeanRemote;

public class LACSDRoleActivityPO extends LACSDGenericPO {

    private static final String JNDI_ROLEACTIVITYDA0      = "ejb/RoleActivityDAO";
  
    private String 	remoteURL;
    
    public LACSDRoleActivityPO() {
    	ApplicationContext appctx = ApplicationContext.getInstance();
		remoteURL = appctx.get(LACSDJNDIConstants.REMOTE_IIOP_URL_RESOURCE);
    }
	//	bypass DAO and use Dummy Data Object, RoleActivityTestDataDO
	
	/**
	 * Returns a collection of all roles
	 * EmployeeRoleVO holds an ArrayList of itself
	 * @param EmployeeRoleVO employeeRoleVO
	 * @return EmployeeRoleVO
	 * @throws Throwable
	*/
	
	public EmployeeRoleVO getAllRoles(EmployeeRoleVO employeeRoleVO) throws Throwable {
		return this.getRoleActivityDAOBean().getAllRoles(employeeRoleVO);
	 }
	
	/**
	 * Returns a collection of all activities (granted or not) with specified RoleId
	 * EmployeeRoleVO holds an ArrayList of itself
	 * @param EmployeeRoleVO employeeRoleVO
	 * @return EmployeeRoleVO
	 * @throws Throwable
	*/
	public EmployeeRoleVO getRoleDetail(EmployeeRoleVO employeeRoleVO) throws Throwable {
		return this.getRoleActivityDAOBean().getRoleDetail(employeeRoleVO);
	}
	
	/**
	 * Returns a collection of all granted activities  with specified RoleId
	 * EmployeeRoleVO holds an ArrayList of itself
	 * @param EmployeeRoleVO employeeRoleVO
	 * @return EmployeeRoleVO
	 * @throws Throwable
	*/
	public EmployeeRoleVO getRoleGrantedActivities(EmployeeRoleVO employeeRoleVO) throws Throwable {
	    return 	this.getRoleActivityDAOBean().getRoleGrantedActivities(employeeRoleVO);
	}
	
	/**
	 * Insert or delete a record in OSX granted Activity table
	 * @param EmployeeVO employeeVO
	 * @return void
	 * @throws Throwable
	*/
	public void updateRole(EmployeeRoleVO employeeRoleVO) throws Throwable {
	    	this.getRoleActivityDAOBean().updateRole(employeeRoleVO);
	}
	
	private RoleActivityDAOBeanRemote getRoleActivityDAOBean () throws LACSDException{
	  	 Object remoteObj = LACSDEJBFinder.getRemoteEJB(remoteURL,JNDI_ROLEACTIVITYDA0);
	  	 return ((RoleActivityDAOBeanRemote)remoteObj);
	}
}
