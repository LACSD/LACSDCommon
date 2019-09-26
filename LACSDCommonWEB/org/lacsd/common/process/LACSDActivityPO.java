package org.lacsd.common.process;

import org.lacsd.common.constants.LACSDJNDIConstants;
import org.lacsd.common.helper.LACSDEJBFinder;
import org.lacsd.common.util.ApplicationContext;
import org.lacsd.osx.dao.ejb.view.ActivityDAOBeanRemote;
import org.lacsd.osx.values.ActivityVO;


/******************************************************************************
//* Copyright (c) 2012 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		LACSDActivityPO
//* Revision: 		1.0
//* Author:			HHHO@LACSD.ORG;  
//* Created On: 	03-13-2012
//* Modified By:    
//* Modified On:    
//*                 
//*					
//* Description:	Process object for Activity Use Case
/******************************************************************************/

public class LACSDActivityPO  extends LACSDGenericPO {
    private static final String JNDI_ACTIVITYDA0      = "ejb/ActivityDAO";
 

    /**
     * Get activity description with give form name and formID
     * @param EmployeeRoleVO employeeRoleVO
     * @return EmployeeRoleVO
     * @throws Throwable
    */

    public String getActivitieDescription(String applicationID,String servletPath,String actionName) throws Throwable {
        ApplicationContext appctx = ApplicationContext.getInstance();
        String remoteURL = appctx.get(LACSDJNDIConstants.REMOTE_IIOP_URL_RESOURCE);
        Object remoteObj = LACSDEJBFinder.getRemoteEJB(remoteURL,JNDI_ACTIVITYDA0);
        ActivityDAOBeanRemote activityDAO = (ActivityDAOBeanRemote)remoteObj;
     	ActivityVO activityVO = new ActivityVO();
    	activityVO.setApplicationID(applicationID);
    	activityVO.setFormID(servletPath);
    	activityVO.setProperty(actionName);
       	activityVO = activityDAO.getActivitieDescription(activityVO);
        return activityVO.getDescr();

    }

}
