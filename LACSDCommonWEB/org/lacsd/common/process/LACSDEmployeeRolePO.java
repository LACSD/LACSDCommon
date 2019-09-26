package org.lacsd.common.process;

/******************************************************************************
//* Copyright (c) 2003 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:       LACSDEmployeeRolePO.java
//* Revision:       1.8
//* Author:         MFEINBERG@LACSD.ORG
//* Created On:     9-29-03
//* Modified By:    TNGUYEN@LACSD.ORG
//* Modified On:    09-22-04
//*                 Move to OSX to be used by all applications 
//*                 
//* Description:    Process object for Employee-Role Use Case
/******************************************************************************/

import org.lacsd.common.authentication.UserProfile;
import org.lacsd.common.constants.LACSDJNDIConstants;
import org.lacsd.common.exceptions.LACSDException;
import org.lacsd.common.helper.LACSDEJBFinder;
import org.lacsd.common.util.ApplicationContext;
import org.lacsd.common.values.EmployeeVO;
import org.lacsd.osx.business.ejb.view.ADBOBeanRemote;
import org.lacsd.osx.dao.ejb.view.EmployeeRoleDAOBeanRemote;

public class LACSDEmployeeRolePO extends LACSDGenericPO {
   private static final String JNDI_EMPLOYEEROLEDA0      = "ejb/EmployeeRoleDAO";
   private static final String JNDI_ADBO                   = "ejb/ADBO";

   private String 	remoteURL;
	
	public LACSDEmployeeRolePO() {
		ApplicationContext appctx = ApplicationContext.getInstance();
		remoteURL = appctx.get(LACSDJNDIConstants.REMOTE_IIOP_URL_RESOURCE);
	}
/**
 * Returns a collection of all employees
 * EmployeeVO holds an ArrayList of itself
 * @param EmployeeVO employeeVO
 * @return EmployeeVO
 * @throws LACSDException
*/
public EmployeeVO getAllEmployees(EmployeeVO employeeVO) throws LACSDException, Throwable {
	return this.getEmployeeRoleDAOBean().getAllEmployees(employeeVO);
}

/**
 * Returns a single employee record
 * @param EmployeeVO employeeVO
 * @return EmployeeVO
 * @throws LACSDException
*/
public UserProfile getEmployeeDetail(EmployeeVO employeeVO) throws LACSDException, Throwable {
	return this.getEmployeeRoleDAOBean().getEmployeeDetail(employeeVO);
}

/**
 * Returns a single employee record from the view table
 * @param EmployeeVO employeeVO
 * @return EmployeeVO
 * @throws LACSDException
*/
public EmployeeVO getViewRecord(EmployeeVO employeeVO) throws LACSDException, Throwable {
	return this.getEmployeeRoleDAOBean().getViewRecord(employeeVO);
}

/**
 * Returns a single employee record (Regardless if part of System)
 * @param EmployeeVO employeeVO
 * @return EmployeeVO
 * @throws LACSDException
*/
public EmployeeVO getLookupEmployee(EmployeeVO employeeVO) throws LACSDException, Throwable {
	return this.getEmployeeRoleDAOBean().getLookupEmployee(employeeVO);
}


/**
 * Add a new LACSD Employee to the system
 * @param UserProfile userProfile
 * @return void
 * @throws LACSDException
*/
public void addEmployee(UserProfile userProfile) throws LACSDException, Throwable {
	this.getEmployeeRoleDAOBean().addEmployee(userProfile);
}

/**
 * Remove a single LACSD Employee from the system
 * @param EmployeeVO employeeVO
 * @return void
 * @throws LACSDException
*/
public void deleteEmployee(EmployeeVO employeeVO) throws LACSDException, Throwable {
	this.getEmployeeRoleDAOBean().deleteEmployee(employeeVO);
}

/**
 * Update a single Employee Record
 * @param EmployeeVO employeeVO
 * @return void
 * @throws LACSDException
*/
public void updateEmployee(UserProfile userProfile) throws LACSDException, Throwable {
	this.getEmployeeRoleDAOBean().updateEmployee(userProfile);
}

/**
 * Return a collection of all Roles and their descriptions
 * @param EmployeeVO employeeVO
 * @return EmployeeRoleVO
 * @throws LACSDException
*/
public EmployeeVO getAllRoles(EmployeeVO employeeVO) throws LACSDException, Throwable {
	return this.getEmployeeRoleDAOBean().getAllRoles(employeeVO);
}

/**
 * Look up email address by employee ID
 * @param employeeVO
 * @return EmployeeVO
 * @throws LACSDException
 * @throws Throwable
 */
public EmployeeVO lookUpEmailByEmployeeID(EmployeeVO employeeVO) throws LACSDException, Throwable {
	return this.getEmployeeRoleDAOBean().lookUpEmailByEmployeeID(employeeVO);
}

/**
 * Get employee info from Active Directory by LAN ID
 * @param lanID
 * @return EmployeeVO
 * @throws LACSDException
 * @throws Throwable
 */
public EmployeeVO getEmployeeByLANID(String lanID) throws LACSDException, Throwable {

	EmployeeVO employeeVO = new EmployeeVO();
    try {
        employeeVO = this.getADBOBean().lookUpEmployeeByLANID(lanID);
    }
    catch (Throwable t) {
        throw (LACSDEJBFinder.evaluateRemoteExceptions(t,remoteURL));
    }
    
    return employeeVO;
}

/**
 * Check if employee email existed in AD system
 * @param employeeID
 * @return boolean
 * @throws LACSDException
 * @throws Throwable
 */
public boolean isValidLACSDEmail(String email ) throws LACSDException, Throwable {

	return this.getADBOBean().isValidEmail(email);
}
/**
 * Check if employee existed in AD system
 * @param employeeID
 * @return boolean
 * @throws LACSDException
 * @throws Throwable
 */
public boolean isEmployeeExistInAD(String employeeID ) throws LACSDException, Throwable {
     return this.getADBOBean().isEmployeeExistInAD(employeeID); 
}


private EmployeeRoleDAOBeanRemote getEmployeeRoleDAOBean () throws LACSDException{
	 Object remoteObj = LACSDEJBFinder.getRemoteEJB(remoteURL,JNDI_EMPLOYEEROLEDA0);
	 return ((EmployeeRoleDAOBeanRemote)remoteObj);
}

private ADBOBeanRemote getADBOBean () throws LACSDException{
	 Object remoteObj = LACSDEJBFinder.getRemoteEJB(remoteURL,JNDI_ADBO);
	 return ((ADBOBeanRemote)remoteObj);
}
}