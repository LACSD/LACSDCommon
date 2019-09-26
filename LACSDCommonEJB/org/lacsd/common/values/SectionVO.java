package org.lacsd.common.values; 

/******************************************************************************
//* Copyright (c) 2013 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		SectionVO
//* Revision: 		1.0
//* Author:			asrirochanakul
//* Created On: 	2/5/2013
//* Modified by:	
//* Modified On:	
//*					
//* Description:	LCX Section Value Object - LCX database
/******************************************************************************/

import java.io.Serializable;
import java.util.ArrayList;

public class SectionVO implements Serializable {
	 
private static final long serialVersionUID = 1L;

private String departmentID;
private String sectionID;
private String descr;
private boolean isObsolete;

//07/07 - String value for unescaping special characters like '&' in section names
private String unEscapeDescription;

private ArrayList<SectionVO> list;

public String getDepartmentID() {
	return departmentID;
}

public void setDepartmentID(String departmentID) {
	this.departmentID = departmentID;
}

public String getSectionID() {
	return sectionID;
}

public void setSectionID(String sectionID) {
	this.sectionID = sectionID;
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

public ArrayList<SectionVO> getList() {
	return list;
}

public void setList(ArrayList<SectionVO> list) {
	this.list = list;
}

/**
 * @return the description
 */
public String getUnEscapeDescription() {
	return unEscapeDescription;
}

/**
 * @param description the description to set
 */
public void setUnEscapeDescription(String unEscapeDescription) {
	this.unEscapeDescription = unEscapeDescription ;
}

}