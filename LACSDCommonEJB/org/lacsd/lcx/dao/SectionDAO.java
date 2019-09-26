package org.lacsd.lcx.dao;

/******************************************************************************
 * Copyright (c) 2013 Sanitation Districts of Los Angeles. All Rights Reserved.
 * Filename:	SectionDAO.java
 * Revision: 	1.0
 * @author 		asrirochanakul
 * Created On: 	2/5/13
 * Modified by:	
 * Modified On:	
 *					
 * Description:	LCX Section data access object
 *               
 ******************************************************************************/

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.lang3.StringEscapeUtils;
import org.lacsd.common.constants.LACSDSPConstants;
import org.lacsd.common.dao.LACSDSqlServerProcDAO;
import org.lacsd.common.exceptions.LACSDException;
import org.lacsd.common.values.SectionVO;

public class SectionDAO extends LACSDSqlServerProcDAO { 
	
private String action; 
private SectionVO sectionVO;
private String employeeID;
	
private static final String LCX_ACTION_GET_SECTION_BY_EMPLOYEEID = "E"; 

/**
 * Constructor - Uses Stored Procedure [LCXSECT100SP]
*/
public SectionDAO() {
	super(LACSDSPConstants.LCXSECT100SP);
	sectionVO = new SectionVO();
}

/**
 * Get section by employee ID
 * @param empID
 * @return
 * @throws LACSDException
 */
public SectionVO getSectionByEmployeeID(String empID) throws LACSDException{
	log.debug("ENTERING getSectionByEmployeeID"); 
	super.setQueryType(EXECUTE_QUERY);
	this.action = LCX_ACTION_GET_SECTION_BY_EMPLOYEEID;
	
	this.employeeID = empID;
	
	execute();
    
	log.debug("EXITING getSectionByEmployeeID");

    return sectionVO;
}

/**
 * HANDLE OUTPUT TYPE 1:	Stored Procedure Returns an Open Cursor
 * @param ResultSet rs
 * @return void
 * @throws SQLException, LACSDException
 */
protected void getResultsFromResultSet(ResultSet rs) throws SQLException, LACSDException {
	
	if (action.equals(LCX_ACTION_GET_SECTION_BY_EMPLOYEEID)) {
		if (rs.next()) {
			sectionVO.setDepartmentID(rs.getString("DepartmentID")); 
			sectionVO.setSectionID(rs.getString("SectionID")); 
			sectionVO.setDescr(rs.getString("Descr")); 
			//07/07/16 - Added the following change to unescape special characters like '&'
			sectionVO.setUnEscapeDescription(StringEscapeUtils.escapeEcmaScript(rs.getString("Descr")));
			sectionVO.setObsolete(rs.getString("IsObsolete").equals("Y") ? true : false);
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
	setInputParam(3, this.sectionVO.getDepartmentID()); 
	setInputParam(4, this.sectionVO.getSectionID()); 
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