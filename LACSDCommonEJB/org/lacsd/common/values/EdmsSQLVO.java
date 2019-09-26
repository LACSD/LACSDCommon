package org.lacsd.common.values;

/******************************************************************************
 //* Copyright (c) 2009 Sanitation Districts of Los Angeles. All Rights Reserved.
 //* Filename:		EdmsSQLVO.java
 //* Revision: 		1.0
 //* Author:			hho
 //* Created On: 	Feb 25, 2009
 //* Modified by:	
 //* Modified On:	
 //*					
 //* Description:	
 /******************************************************************************/

import java.util.ArrayList;
import java.util.Hashtable;

public class EdmsSQLVO implements java.io.Serializable{

	static final long serialVersionUID = -2157475041644875202L;
	private String strSQL;
	private ArrayList<Hashtable<String,String>> searchResults;
	
	private EdmsAuthenticateVO edmsAuthenticateVO;	//	login security
	
	/**
	 * @return Returns the searchResults.
	 */
	public ArrayList<Hashtable<String,String>> getSearchResults() {
		return searchResults;
	}
	/**
	 * @param searchResults The searchResults to set.
	 */
	public void setSearchResults(ArrayList<Hashtable<String,String>> searchResults) {
		this.searchResults = searchResults;
	}
	/**
	 * @return Returns the strSQL.
	 */
	public String getStrSQL() {
		return strSQL;
	}
	/**
	 * @param strSQL The strSQL to set.
	 */
	public void setStrSQL(String strSQL) {
		this.strSQL = strSQL;
	}
	
	/**
	 * @return Returns the edmsAuthenticateVO.
	 */
	public EdmsAuthenticateVO getEdmsAuthenticateVO() {
		return edmsAuthenticateVO;
	}
	/**
	 * @param edmsAuthenticateVO The edmsAuthenticateVO to set.
	 */
	public void setEdmsAuthenticateVO(EdmsAuthenticateVO edmsAuthenticateVO) {
		this.edmsAuthenticateVO = edmsAuthenticateVO;
	}
}
