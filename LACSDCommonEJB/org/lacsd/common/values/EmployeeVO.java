package org.lacsd.common.values;

/******************************************************************************
//* Copyright (c) 2003 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		EmployeeVO.java
//* Revision: 		1.8
//* Author:			MFEINBERG@LACSD.ORG
//* Created On: 	09-29-03
//* Modified By:	VANI MANJUNATH
//* Modified On:	02-21-07 
 *                  private variables userName and newEmployeeID added.
 */
//*					
//* Description:	Employee Value Object
/******************************************************************************/

import java.util.ArrayList;

public class EmployeeVO implements java.io.Serializable {

private static final long serialVersionUID = -955262676147319614L;

private String securityRoleID;
private String employeeID;
private String firstName;
private String lastName;
private String middleName;
private String emailAddress;
private String roleDescr;

private String updateUserID;	//	For auditing

private ArrayList<EmployeeVO> employees;	//	Autonomous Collection
private ArrayList<?> activeDirectoryEmployees;
private EmployeeRoleVO employeeRoleVO;	//	Child Object 

private String userName;    //for new employee validation 
private String newEmployeeID; //employee Id of the new employee to be added to the application 

private String multiValidValue; 
private boolean userNameExists = true; //indicates if user name exists in Employee Ext table 
private boolean empIDExists = true; //indicates if employee ID exists in Employee Ext table  


/**
 * Returns the employeeID.
 * @return String
 */
public String getEmployeeID() {
	return employeeID;
}

/**
 * Returns the employees.
 * @return ArrayList
 */
public ArrayList<EmployeeVO> getEmployees() {
	return employees;
}

/**
 * Returns the firstName.
 * @return String
 */
public String getFirstName() {
	return firstName;
}

/**
 * Returns the lastName.
 * @return String
 */
public String getLastName() {
	return lastName;
}

/**
 * Returns the updateUserID.
 * @return String
 */
public String getUpdateUserID() {
	return updateUserID;
}

/**
 * Returns the roleDescr.
 * @return String
 */
public String getRoleDescr() {
	return roleDescr;
}

/**
 * Sets the employeeID.
 * @param employeeID The employeeID to set
 */
public void setEmployeeID(String employeeID) {
	this.employeeID = employeeID;
}

/**
 * Sets the employees.
 * @param employees The employees to set
 */
public void setEmployees(ArrayList<EmployeeVO> employees) {
	this.employees = employees;
}

/**
 * Sets the firstName.
 * @param firstName The firstName to set
 */
public void setFirstName(String firstName) {
	this.firstName = firstName;
}

/**
 * Sets the lastName.
 * @param lastName The lastName to set
 */
public void setLastName(String lastName) {
	this.lastName = lastName;
}

/**
 * Sets the updateUserID.
 * @param updateUserID The updateUserID to set
 */
public void setUpdateUserID(String updateUserID) {
	this.updateUserID = updateUserID;
}

/**
 * Sets the roleDescr.
 * @param roleDescr The roleDescr to set
 */
public void setRoleDescr(String roleDescr) {
	this.roleDescr = roleDescr;
}

/**
 * Returns the name.
 * @return String
 */
public String getName() {
	return (lastName + ", " + firstName);
}

/**
 * Returns the Employee Name.
 * @return String
 */
public String getEmployeeName() {
	return (firstName + " " + lastName);
}

/**
 * Returns the emailAddress.
 * @return String
 */
public String getEmailAddress() {
	return emailAddress;
}

/**
 * Sets the emailAddress.
 * @param emailAddress The emailAddress to set
 */
public void setEmailAddress(String emailAddress) {
	this.emailAddress = emailAddress;
}

/**
 * Returns the securityRoleID.
 * @return String
 */
public String getSecurityRoleID() {
	return securityRoleID;
}

/**
 * Sets the securityRoleID.
 * @param securityRoleID The securityRoleID to set
 */
public void setSecurityRoleID(String securityRoleID) {
	this.securityRoleID = securityRoleID;
}

/**
 * Returns the employeeRoleVO.
 * @return EmployeeRoleVO
 */
public EmployeeRoleVO getEmployeeRoleVO() {
	return employeeRoleVO;
}

/**
 * Sets the employeeRoleVO.
 * @param employeeRoleVO The employeeRoleVO to set
 */
public void setEmployeeRoleVO(EmployeeRoleVO employeeRoleVO) {
	this.employeeRoleVO = employeeRoleVO;
} 

/** 
 * Returns the username
 * @return userName
 */ 
public String getUserName() {
    return userName;
} 

/** 
 * Sets the value for username
 * @param userName The username to set
 */
public void setUserName(String userName) {
    this.userName = userName;
} 

/**
 * Returns the newEmployeeID.
 * @return String
 */
public String getNewEmployeeID() {
    return newEmployeeID;
} 

/**
 * Sets the newEmployeeID.
 * @param newEmployeeID The newEmployeeID to set
 */
public void setNewEmployeeID(String newEmployeeID) {
    this.newEmployeeID = newEmployeeID;
} 


/**
 * Returns the boolean value for userNameExists.
 * @return boolean
 */
public boolean isUserNameExists() {
    return userNameExists;
} 

/**
 * Sets the boolean value for userNameExists.
 * @param userNameExists The userNameExists to set
 */
public void setUserNameExists(boolean userNameExists){
    this.userNameExists = userNameExists;
} 

/**
 * Returns the boolean value for empIDExists.
 * @return boolean
 */
public boolean isEmpIDExists() {
    return empIDExists;
} 

/**
 * Sets the boolean value for empIDExists.
 * @param empIDExists The empIDExists to set
 */
public void setEmpIDExists(boolean empIDExists){
    this.empIDExists = empIDExists;
} 

/** 
 * Returns the value for multiValidValue 
 * @return String
 */
public String getMultiValidValue(){
    return multiValidValue;
} 

/** 
 * Sets the value for multiValidValue 
 * @param multiValidValue the multiValidValue to set 
 */ 
public void setMultiValidValue(String multiValidValue) {
    this.multiValidValue = multiValidValue;
}

/**
 * @return Returns the activeDirectoryEmployees.
 */
public ArrayList<?> getActiveDirectoryEmployees() {
	return activeDirectoryEmployees;
}
/**
 * @param activeDirectoryEmployees The activeDirectoryEmployees to set.
 */
public void setActiveDirectoryEmployees(ArrayList<?> activeDirectoryEmployees) {
	this.activeDirectoryEmployees = activeDirectoryEmployees;
}

public String getMiddleName() {
	return middleName;
}

public void setMiddleName(String middleName) {
	this.middleName = middleName;
}


}