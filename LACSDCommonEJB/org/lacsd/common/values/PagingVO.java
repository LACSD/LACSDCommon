package org.lacsd.common.values;

import java.io.Serializable;

/******************************************************************************
//* Copyright (c) 2011 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		PagingVO.java
//* Revision: 		1.0
//* Author:			hho@lacsd.org
//* Created On: 	Apr 19, 2011
//* Modified By:    
//* Modified On:    
//* 
//* Description:	paging value object
/******************************************************************************/

public class PagingVO  implements Serializable{
	private static final long serialVersionUID = 3664379495180772984L;
	public final static String ASC_SORT = "1";
	public final static String DESC_SORT = "2";
	private int totalRecords;
	private int pageSize = 20;
	private int pageNum = 1;
	private String sortField;
	private String sortType = ASC_SORT;
	private boolean dateType;
	
	/**
	 * Return the totalRecords
	 * @return int
	 */
	public int getTotalRecords() {
		return totalRecords;
	}
	/**
	 * Set the totalRecords
	 * @param totalRecords the totalRecords to set
	 */
	public void setTotalRecords(int totalRecords) {
		this.totalRecords = totalRecords;
	}
	/**
	 * Return the pageSize
	 * @return int
	 */
	public int getPageSize() {
		return pageSize;
	}
	/**
	 * Set the pageSize
	 * @param pageSize the pageSize to set
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	/**
	 * Return the pageNum
	 * @return int
	 */
	public int getPageNum() {
		return pageNum;
	}
	/**
	 * Set the pageNum
	 * @param pageNum the pageNum to set
	 */
	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}
	/**
	 * Return the sortFile
	 * @return String
	 */
	public String getSortField() {
		return sortField;
	}
	/**
	 * Set the sortFile
	 * @param sortFile the sortFile to set
	 */
	public void setSortField(String sortField) {
		this.sortField = sortField;
	}
	/**
	 * Return the sortType
	 * @return String
	 */
	public String getSortType() {
		return sortType;
	}
	/**
	 * Set the sortType
	 * @param sortType the sortType to set
	 */
	public void setSortType(String sortType) {
		this.sortType = sortType;
	}
	/**
	 * Return the dateType
	 * @return boolean
	 */
	public boolean isDateType() {
		return dateType;
	}
	/**
	 * Set the dateType
	 * @param dateType the dateType to set
	 */
	public void setDateType(boolean dateType) {
		this.dateType = dateType;
	}
	
	
}
