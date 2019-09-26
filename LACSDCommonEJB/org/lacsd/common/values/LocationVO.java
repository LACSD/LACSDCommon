package org.lacsd.common.values; 

/******************************************************************************
//* Copyright (c) 2010 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		ProjectVO
//* Revision: 		1.0
//* Author:			vmanjunath
//* Created On: 	11/30/2010
//* Modified by:	
//* Modified On:	
//*					
//* Description:	Location Value Object - LCX database
/******************************************************************************/

import java.io.Serializable;
import java.util.ArrayList;



public class LocationVO implements Serializable { 
	private static final long serialVersionUID = -8276025666681953082L;
	/**
	private String facilityID; 
	private String facilityDescription; 
	private String buildingID; 
	private String buildingDescription; 
	private String deptID; 
	private String deptDescription; 
	private String sectionID; 
	private String sectionDescription; 
	**/
	
	//parent id for Building table is Facility ID and for Section, it is Department ID. 
	private String parentID; 
	//Refers to the ID of the specific location component like Facility ID or Building ID, etc. 
	private String locationID; 
	//Description column in the particular table
	private String description; 
	
	//07/07 - String value for unescaping special characters like '&' in section names
	private String unEscapeDescription;
	
	private ArrayList<LocationVO> locationList; 
	
	public String getParentID() {
		return parentID;
	}
	public void setParentID(String parentID) {
		this.parentID = parentID;
	}
	public String getLocationID() {
		return locationID;
	}
	public void setLocationID(String locationID) {
		this.locationID = locationID;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public ArrayList<LocationVO> getLocationList() {
		return locationList;
	}
	public void setLocationList(ArrayList<LocationVO> locationList) {
		this.locationList = locationList;
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
	/**
	
	public String getFacilityID() {
		return facilityID;
	}
	public void setFacilityID(String facilityID) {
		this.facilityID = facilityID;
	}
	public String getFacilityDescription() {
		return facilityDescription;
	}
	public void setFacilityDescription(String facilityDescription) {
		this.facilityDescription = facilityDescription;
	}
	public String getBuildingID() {
		return buildingID;
	}
	public void setBuildingID(String buildingID) {
		this.buildingID = buildingID;
	}
	public String getBuildingDescription() {
		return buildingDescription;
	}
	public void setBuildingDescription(String buildingDescription) {
		this.buildingDescription = buildingDescription;
	}
	public String getDeptID() {
		return deptID;
	}
	public void setDeptID(String deptID) {
		this.deptID = deptID;
	}
	public String getDeptDescription() {
		return deptDescription;
	}
	public void setDeptDescription(String deptDescription) {
		this.deptDescription = deptDescription;
	}
	public String getSectionID() {
		return sectionID;
	}
	public void setSectionID(String sectionID) {
		this.sectionID = sectionID;
	}
	public String getSectionDescription() {
		return sectionDescription;
	}
	public void setSectionDescription(String sectionDescription) {
		this.sectionDescription = sectionDescription;
	}
	**/
	
	
	
	
	

}
