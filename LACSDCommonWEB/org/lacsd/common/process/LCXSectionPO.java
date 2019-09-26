package org.lacsd.common.process;

/******************************************************************************
 * Copyright (c) 2013 Sanitation Districts of Los Angeles. All Rights Reserved.
 * Filename:	LCXDepartmentPO.java
 * Revision: 	1.0
 * @author 		asrirochanakul
 * Created On: 	2/5/13
 * Modified by:	
 * Modified On:	
 *					
 * Description:	LCX Section proxy object 
 *               
 ******************************************************************************/

import org.lacsd.common.exceptions.LACSDException;
import org.lacsd.common.values.SectionVO;
import org.lacsd.lcx.dao.SectionDAO;

public class LCXSectionPO extends LACSDGenericPO { 

/**
 * Get section detail by employee ID
 * @param empID
 * @return 
 * @throws LACSDException
 * @throws Throwable
 */
public SectionVO getSectionByEmployeeID(String empID)throws LACSDException, Throwable {

	SectionDAO sectionDAO = new SectionDAO();
	return sectionDAO.getSectionByEmployeeID(empID);
} 
 
}