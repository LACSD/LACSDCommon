package org.lacsd.common.values;

/******************************************************************************
//* Copyright (c) 2004 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		EdmsSearchVO.java
//* Revision: 		1.0
//* Author:			M Feinberg
//* Created On: 	04-13-2004
//* Modified by:	
//* Modified On:	
//*					
//* Description:	Value Object abstraction retrieving a single "Document" 
//* 				or collections of "Document" objects from Java-EDMS Bridge
/******************************************************************************/

import java.util.ArrayList;
import java.util.Hashtable;

public class EdmsSearchVO implements java.io.Serializable {

private static final long serialVersionUID = 5928062185504962261L;

private String searchCYD;						//	Hummingbird defined by: RSARUT@LACSD.ORG
private String versionCYD;						//	Hummingbird Common Form to find Versions

private Hashtable<String, String> criteriaList;				//	Dynamic set of NAME/VALUE pairs as META DATA for Search Criteria
private Hashtable<String, String> returnPropList;				//	Dynamic set of NAME/VALUE pairs for Search Result Set

private ArrayList<EdmsSearchVO> searchResults;				//	Homogenous ArrayList with all documents found by a single search
private ArrayList<EdmsSearchVO> versionResults;				//	Homogenous ArrayList with all versions for a particular document

private EdmsAuthenticateVO edmsAuthenticateVO;	//	login security

/**
 * Returns the criteriaList.
 * @return Hashtable
 */
public Hashtable<String,String> getCriteriaList() {
	return criteriaList;
}

/**
 * Returns the edmsAuthenticateVO.
 * @return EdmsAuthenticateVO
 */
public EdmsAuthenticateVO getEdmsAuthenticateVO() {
	return edmsAuthenticateVO;
}

/**
 * Returns the returnPropList.
 * @return Hashtable
 */
public Hashtable<String, String> getReturnPropList() {
	return returnPropList;
}

/**
 * Returns the searchCYD.
 * @return String
 */
public String getSearchCYD() {
	return searchCYD;
}

/**
 * Returns the searchResults.
 * @return ArrayList
 */
public ArrayList<EdmsSearchVO> getSearchResults() {
	return searchResults;
}

/**
 * Returns the versionCYD.
 * @return String
 */
public String getVersionCYD() {
	return versionCYD;
}

/**
 * Returns the versionResults.
 * @return ArrayList
 */
public ArrayList<EdmsSearchVO> getVersionResults() {
	return versionResults;
}

/**
 * Sets the criteriaList.
 * @param criteriaList The criteriaList to set
 */
public void setCriteriaList(Hashtable<String,String> criteriaList) {
	this.criteriaList = criteriaList;
}

/**
 * Sets the edmsAuthenticateVO.
 * @param edmsAuthenticateVO The edmsAuthenticateVO to set
 */
public void setEdmsAuthenticateVO(EdmsAuthenticateVO edmsAuthenticateVO) {
	this.edmsAuthenticateVO = edmsAuthenticateVO;
}

/**
 * Sets the returnPropList.
 * @param returnPropList The returnPropList to set
 */
public void setReturnPropList(Hashtable<String, String> returnPropList) {
	this.returnPropList = returnPropList;
}

/**
 * Sets the searchCYD.
 * @param searchCYD The searchCYD to set
 */
public void setSearchCYD(String searchCYD) {
	this.searchCYD = searchCYD;
}

/**
 * Sets the searchResults.
 * @param searchResults The searchResults to set
 */
public void setSearchResults(ArrayList<EdmsSearchVO> searchResults) {
	this.searchResults = searchResults;
}

/**
 * Sets the versionCYD.
 * @param versionCYD The versionCYD to set
 */
public void setVersionCYD(String versionCYD) {
	this.versionCYD = versionCYD;
}

/**
 * Sets the versionResults.
 * @param versionResults The versionResults to set
 */
public void setVersionResults(ArrayList<EdmsSearchVO> versionResults) {
	this.versionResults = versionResults;
}
}
