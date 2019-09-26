package org.lacsd.common.values;

import java.util.ArrayList;

/******************************************************************************
//* Copyright (c) 2012 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		GPXDistrictCityVO.java
//* Revision: 		1.0
//* Author:			hho@lacsd.org
//* Created On: 	Nov 29, 2012
//* Modified By:    
//* Modified On:    
//* 
//* Description:	
/******************************************************************************/

public class GPXDistrictCityVO implements java.io.Serializable{

	private static final long serialVersionUID = -403233053425153028L;
	private int districtID;
	private int cityID;
	private String districtName;
	private String cityName;
	private String effective;
	private ArrayList<GPXDistrictCityVO> gpxDistrictCityVOs;
	
	/**
	 * Return the districtID
	 * @return int
	 */
	public int getDistrictID() {
		return districtID;
	}
	/**
	 * Set the districtID
	 * @param districtID the districtID to set
	 */
	public void setDistrictID(int districtID) {
		this.districtID = districtID;
	}
	/**
	 * Return the cityID
	 * @return int
	 */
	public int getCityID() {
		return cityID;
	}
	/**
	 * Set the cityID
	 * @param cityID the cityID to set
	 */
	public void setCityID(int cityID) {
		this.cityID = cityID;
	}
	/**
	 * Return the districtName
	 * @return String
	 */
	public String getDistrictName() {
		return districtName;
	}
	/**
	 * Set the districtName
	 * @param districtName the districtName to set
	 */
	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}
	/**
	 * Return the cityName
	 * @return String
	 */
	public String getCityName() {
		return cityName;
	}
	/**
	 * Set the cityName
	 * @param cityName the cityName to set
	 */
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	/**
	 * Return the effective
	 * @return String
	 */
	public String getEffective() {
		return effective;
	}
	/**
	 * Set the effective
	 * @param effective the effective to set
	 */
	public void setEffective(String effective) {
		this.effective = effective;
	}
	/**
	 * Return the gpxDistrictCityVOs
	 * @return ArrayList<GPXDistrictCityVO>
	 */
	public ArrayList<GPXDistrictCityVO> getGpxDistrictCityVOs() {
		return gpxDistrictCityVOs;
	}
	/**
	 * Set the gpxDistrictCityVOs
	 * @param gpxDistrictCityVOs the gpxDistrictCityVOs to set
	 */
	public void setGpxDistrictCityVOs(
			ArrayList<GPXDistrictCityVO> gpxDistrictCityVOs) {
		this.gpxDistrictCityVOs = gpxDistrictCityVOs;
	}
	
	
	
}
