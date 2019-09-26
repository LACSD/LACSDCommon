package org.lacsd.common.process;

/******************************************************************************
 * Copyright (c) 2013 Sanitation Districts of Los Angeles. All Rights Reserved.
 * Filename:	LCXDepartmentPO.java
 * Revision: 	1.0
 * @author 		asrirochanakul
 * Created On: 	2/1/13
 * Modified by:	
 * Modified On:	
 *					
 * Description:	LCX Department proxy object 
 *               
 ******************************************************************************/

import org.lacsd.common.exceptions.LACSDException;
import org.lacsd.common.values.DepartmentVO;
import org.lacsd.lcx.dao.DepartmentDAO;

public class LCXDepartmentPO extends LACSDGenericPO { 

/**
 * Get department detail by employee ID
 * @param empID
 * @return 
 * @throws LACSDException
 * @throws Throwable
 */
public DepartmentVO getDepartmentByEmployeeID(String empID)throws LACSDException, Throwable {

	DepartmentDAO deptDAO = new DepartmentDAO();
	return deptDAO.getDepartmentByEmployeeID(empID);
} 
 
}