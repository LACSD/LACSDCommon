package org.lacsd.common.values;

/******************************************************************************
//* Copyright (c) 2004 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		ApplicationVO.java
//* Revision: 		1.1
//* Author:			asrirochanakul@LACSD.ORG
//* Created On: 	06-23-2004
//* Modified By:	
//* Modified On:	
//*					
//* Description:	Application Value Object
/******************************************************************************/

import java.util.ArrayList;

public class ApplicationVO implements java.io.Serializable {

private static final long serialVersionUID = -6665764070908027184L;

private String applicationID;
private String descr;
private String employeeAdminID;  
private String activeDirAuthenitcate ;

private ArrayList<ApplicationVO> applications;	//	Autonomous Collection

/**
 * Returns the applicationID.
 * @return String
 */
public String getApplicationID() {
	return applicationID;
}

/**
 * Returns the applications.
 * @return ArrayList
 */
public ArrayList<ApplicationVO> getApplications() {
	return applications;
}

/**
 * Returns the descr.
 * @return String
 */
public String getDescr() {
	return descr;
}

/**
 * Sets the applicationID.
 * @param applicationID The applicationID to set
 */
public void setApplicationID(String applicationID) {
	this.applicationID = applicationID;
}

/**
 * Sets the applications.
 * @param applications The applications to set
 */
public void setApplications(ArrayList<ApplicationVO> applications) {
	this.applications = applications;
}

/**
 * Sets the descr.
 * @param descr The descr to set
 */
public void setDescr(String descr) {
	this.descr = descr;
}

/**
 * Sets the employeeAdminID.
 * @param employeeAdminID The employeeAdminID to set
 */

public String getEmployeeAdminID() {
	return employeeAdminID;
}

/**
 * Sets the employeeAdminID.
 * @param employeeAdminID The employeeAdminID to set
 */
public void setEmployeeAdminID(String employeeAdminID) {
	this.employeeAdminID = employeeAdminID;
}

public String getActiveDirAuthenitcate() {
	return activeDirAuthenitcate;
}

public void setActiveDirAuthenitcate(String activeDirAuthenitcate) {
	this.activeDirAuthenitcate = activeDirAuthenitcate;
}



}