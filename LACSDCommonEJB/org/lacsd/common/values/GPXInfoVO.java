package org.lacsd.common.values;

import java.util.ArrayList;

/******************************************************************************
//* Copyright (c) 2012 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		GPXInfoVO.java
//* Revision: 		1.0
//* Author:			hho@lacsd.org
//* Created On: 	Nov 29, 2012
//* Modified By:    
//* Modified On:    
//* 
//* Description:	hold information from GPX tables
/******************************************************************************/

public class GPXInfoVO implements java.io.Serializable{

	private static final long serialVersionUID = 215644469704898583L;
	private int id;
	private String code;
	private String description;
	private ArrayList<GPXInfoVO> gpxInfoVOs;
	
		
	/**
	 * Return the id
	 * @return int
	 */
	public int getId() {
		return id;
	}
	/**
	 * Set the id
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * Return the code
	 * @return String
	 */
	public String getCode() {
		return code;
	}
	/**
	 * Set the code
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * Return the description
	 * @return String
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * Set the description
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * Return the gpxInfoVOs
	 * @return ArrayList<GPXInfoVO>
	 */
	public ArrayList<GPXInfoVO> getGpxInfoVOs() {
		return gpxInfoVOs;
	}
	/**
	 * Set the gpxInfoVOs
	 * @param gpxInfoVOs the gpxInfoVOs to set
	 */
	public void setGpxInfoVOs(ArrayList<GPXInfoVO> gpxInfoVOs) {
		this.gpxInfoVOs = gpxInfoVOs;
	}


}
