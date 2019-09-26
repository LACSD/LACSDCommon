package org.lacsd.common.values;

/******************************************************************************
//* Copyright (c) 2003 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		ActivityVO.java
//* Revision: 		1.0
//* Author:			YGORELIK@LACSD.ORG,
//* Created On: 	10-20-2003
//* Modified By:	MFEINBERG@LACSD.ORG
//* Modified On:	06-18-2004
//*					
//* Description:	Role Activity Value Object
/******************************************************************************/

public class ActivityVO implements java.io.Serializable {

private static final long serialVersionUID = 1764427976645343765L;

private int activityID;
private String activityDescription;
private String activityProperty;

private boolean selected;

private int formID;
private String formName;
private String formDescription;

/**
 * Returns the activityDescription.
 * @return String
 */
public String getActivityDescription() {
	return activityDescription;
}

/**
 * Returns the activityID.
 * @return int
 */
public int getActivityID() {
	return activityID;
}

/**
 * Returns the activityProperty.
 * @return String
 */
public String getActivityProperty() {
	return activityProperty;
}

/**
 * Returns the formDescription.
 * @return String
 */
public String getFormDescription() {
	return formDescription;
}

/**
 * Returns the formID.
 * @return int
 */
public int getFormID() {
	return formID;
}

/**
 * Returns the formName.
 * @return String
 */
public String getFormName() {
	return formName;
}

/**
 * Returns the selected.
 * @return boolean
 */
public boolean isSelected() {
	return selected;
}

/**
 * Sets the activityDescription.
 * @param activityDescription The activityDescription to set
 */
public void setActivityDescription(String activityDescription) {
	this.activityDescription = activityDescription;
}

/**
 * Sets the activityID.
 * @param activityID The activityID to set
 */
public void setActivityID(int activityID) {
	this.activityID = activityID;
}

/**
 * Sets the activityProperty.
 * @param activityProperty The activityProperty to set
 */
public void setActivityProperty(String activityProperty) {
	this.activityProperty = activityProperty;
}

/**
 * Sets the formDescription.
 * @param formDescription The formDescription to set
 */
public void setFormDescription(String formDescription) {
	this.formDescription = formDescription;
}

/**
 * Sets the formID.
 * @param formID The formID to set
 */
public void setFormID(int formID) {
	this.formID = formID;
}

/**
 * Sets the formName.
 * @param formName The formName to set
 */
public void setFormName(String formName) {
	this.formName = formName;
}

/**
 * Sets the selected.
 * @param selected The selected to set
 */
public void setSelected(boolean selected) {
	this.selected = selected;
}
}
