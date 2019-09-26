package org.lacsd.common.values;

/******************************************************************************
//* Copyright (c) 2003 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		EmployeeRoleVO.java
//* Revision: 		1.6
//* Author:			MFEINBERG@LACSD.ORG
//* Created On: 	9-29-2003
//* Modified By:	YGORELIK@LACSD.ORG
//* Modified On:	10-20-2003
//*					
//* Description:	Employee Role Value Object
/******************************************************************************/

import java.util.ArrayList;

public class EmployeeRoleVO implements java.io.Serializable {

private static final long serialVersionUID = -8555705212876371706L;

private int roleID;
private String roleDescription;

private String checkedIDs;		//	A String-concatenated list of selected IDs
private String updateUserID;	//	For auditing

private ArrayList<EmployeeRoleVO> roles;		//	Autonomous list of EmployeeRoleVO's for displaying ALL Roles
private ArrayList<ActivityVO> activities;	//	Child object - All activities for this particular role

private String applicationID;	//	Added 04-09-2004 by MFEINBERG (Applications must identify themselves)


/**
 * Returns the activities.
 * @return ArrayList
 */
public ArrayList<ActivityVO> getActivities() {
	return activities;
}

/**
 * Returns the checkedIDs.
 * @return String
 */
public String getCheckedIDs() {
	return checkedIDs;
}

/**
 * Returns the roleDescription.
 * @return String
 */
public String getRoleDescription() {
	return roleDescription;
}

/**
 * Returns the roleID.
 * @return int
 */
public int getRoleID() {
	return roleID;
}

/**
 * Returns the roles.
 * @return ArrayList
 */
public ArrayList<EmployeeRoleVO> getRoles() {
	return roles;
}

/**
 * Returns the updateUserID.
 * @return String
 */
public String getUpdateUserID() {
	return updateUserID;
}

/*
/**
 * Sets the activities.
 * @param activities The activities to set
 */
public void setActivities(ArrayList<ActivityVO> activities) {
	this.activities = activities;
}

/**
 * Sets the checkedIDs.
 * @param checkedIDs The checkedIDs to set
 */
public void setCheckedIDs(String checkedIDs) {
	this.checkedIDs = checkedIDs;
}

/**
 * Sets the roleDescription.
 * @param roleDescription The roleDescription to set
 */
public void setRoleDescription(String roleDescription) {
	this.roleDescription = roleDescription;
}

/**
 * Sets the roleID.
 * @param roleID The roleID to set
 */
public void setRoleID(int roleID) {
	this.roleID = roleID;
}

/**
 * Sets the roles.
 * @param roles The roles to set
 */
public void setRoles(ArrayList<EmployeeRoleVO> roles) {
	this.roles = roles;
}

/**
 * Sets the updateUserID.
 * @param updateUserID The updateUserID to set
 */
public void setUpdateUserID(String updateUserID) {
	this.updateUserID = updateUserID;
}
/**
 * Returns the applicationID.
 * @return String
 */
public String getApplicationID() {
	return applicationID;
}

/**
 * Sets the applicationID.
 * @param applicationID The applicationID to set
 */
public void setApplicationID(String applicationID) {
	this.applicationID = applicationID;
}
}
