package org.lacsd.common.values; 

/******************************************************************************
//* Copyright (c) 2013 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		DepartmentVO
//* Revision: 		1.0
//* Author:			asrirochanakul
//* Created On: 	2/1/2013
//* Modified by:	
//* Modified On:	
//*					
//* Description:	LCX Department Value Object - LCX database
/******************************************************************************/

import java.io.Serializable;
import java.util.ArrayList;

public class DepartmentVO implements Serializable {
	 
private static final long serialVersionUID = 1L;

private String departmentID;
private String descr;
private boolean isObsolete;

private ArrayList<DepartmentVO> list;

public String getDepartmentID() {
	return departmentID;
}

public void setDepartmentID(String departmentID) {
	this.departmentID = departmentID;
}

public String getDescr() {
	return descr;
}

public void setDescr(String descr) {
	this.descr = descr;
}

public boolean isObsolete() {
	return isObsolete;
}

public void setObsolete(boolean isObsolete) {
	this.isObsolete = isObsolete;
}

public ArrayList<DepartmentVO> getList() {
	return list;
}

public void setList(ArrayList<DepartmentVO> list) {
	this.list = list;
}

}