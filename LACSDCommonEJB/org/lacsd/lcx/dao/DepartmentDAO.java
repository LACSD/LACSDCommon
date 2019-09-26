package org.lacsd.lcx.dao;

/******************************************************************************
 * Copyright (c) 2013 Sanitation Districts of Los Angeles. All Rights Reserved.
 * Filename:	DepartmentDAO.java
 * Revision: 	1.0
 * @author 		asrirochanakul
 * Created On: 	2/1/13
 * Modified by:	
 * Modified On:	
 *					
 * Description:	LCX Department data access object 
 *               
 ******************************************************************************/

import java.sql.ResultSet;
import java.sql.SQLException;
import org.lacsd.common.constants.LACSDSPConstants;
import org.lacsd.common.dao.LACSDSqlServerProcDAO;
import org.lacsd.common.exceptions.LACSDException;
import org.lacsd.common.values.DepartmentVO;

public class DepartmentDAO extends LACSDSqlServerProcDAO { 
	
private String action; 
private DepartmentVO departmentVO;
private String employeeID;
	
private static final String LCX_ACTION_GET_DEPT_BY_EMPLOYEEID = "E"; 

/**
 * Constructor - Uses Stored Procedure [LCXDEPT100SP]
*/
public DepartmentDAO() {
	super(LACSDSPConstants.LCXDEPT100SP);
	departmentVO = new DepartmentVO();
}

/**
 * Get department by employee ID
 * @param empID
 * @return
 * @throws LACSDException
 */
public DepartmentVO getDepartmentByEmployeeID(String empID) throws LACSDException{
	log.debug("ENTERING getDepartmentByEmployeeID"); 
	super.setQueryType(EXECUTE_QUERY);
	this.action = LCX_ACTION_GET_DEPT_BY_EMPLOYEEID;
	
	this.employeeID = empID;
	
	execute();
    
	log.debug("EXITING getDepartmentByEmployeeID");

    return departmentVO;
}

/**
 * HANDLE OUTPUT TYPE 1:	Stored Procedure Returns an Open Cursor
 * @param ResultSet rs
 * @return void
 * @throws SQLException, LACSDException
 */
protected void getResultsFromResultSet(ResultSet rs) throws SQLException, LACSDException {
	
	if (action.equals(LCX_ACTION_GET_DEPT_BY_EMPLOYEEID)) {
		if (rs.next()) {
			departmentVO.setDepartmentID(rs.getString("DepartmentID")); 
			departmentVO.setDescr(rs.getString("Descr"));
			departmentVO.setObsolete(rs.getString("IsObsolete").equals("Y") ? true : false);
		}
	}
}

/* (non-Javadoc)
 * @see org.lacsd.common.dao.LACSDSqlServerProcDAO#getResultsFromString(java.lang.String[])
 */
protected void getResultsFromString(String[] output) throws LACSDException {
}

/**
 * SETUP STEP 1:  Register Input Parameters
 * @return void
 * @throws LACSDException
 */
protected void registerInputs() throws LACSDException {
	setInputParam(1, this.action); 
	setInputParam(2, this.employeeID); 
	setInputParam(3, this.departmentVO.getDepartmentID()); 
}

/**
 * SETUP STEP 2:	Register Output Parameters
 * @return void
 * @throws LACSDException
 */
protected void registerOutputs() throws LACSDException { 
	//super.setOutput(1, Types.VARCHAR); 
}

}