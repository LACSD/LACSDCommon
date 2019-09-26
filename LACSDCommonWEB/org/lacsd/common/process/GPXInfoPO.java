package org.lacsd.common.process;

import org.lacsd.common.exceptions.LACSDException;
import org.lacsd.common.values.GPXDistrictCityVO;
import org.lacsd.common.values.GPXInfoVO;
import org.lacsd.gpx.GPXDistrictCityDAO;
import org.lacsd.gpx.GPXInfoDAO;

/******************************************************************************
//* Copyright (c) 2012 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		GPXInfoPO.java
//* Revision: 		1.0
//* Author:			hho@lacsd.org
//* Created On: 	Nov 29, 2012
//* Modified By:    
//* Modified On:    
//* 
//* Description:	
/******************************************************************************/

public class GPXInfoPO extends LACSDGenericPO {
	
	private GPXInfoDAO gpxInfoDAO = new GPXInfoDAO();
	private GPXDistrictCityDAO gpxDistCityDAO = new GPXDistrictCityDAO();
	
	/**
	 * Get state information
	 * @return
	 * @throws LACSDException
	 */
	public GPXInfoVO getStateInfo() throws LACSDException{
		return gpxInfoDAO.getStateInfo();
	}

	/**
	 * Get city information
	 * @return
	 * @throws LACSDException
	 */
	public GPXInfoVO getCityInfo() throws LACSDException{
		return gpxInfoDAO.getCityInfo();
	}

	/**
	 * Get card type information
	 * @return
	 * @throws LACSDException
	 */
	public GPXInfoVO getCardTypeInfo() throws LACSDException{
		return gpxInfoDAO.getCardTypeInfo();
	}
	
	/**
	 * Get district information
	 * @return
	 * @throws LACSDException
	 */
	public GPXInfoVO getDistrictInfo() throws LACSDException{
		return gpxInfoDAO.getDistrictInfo();
	}	
	
	/**
	 * Get payment information
	 * @return
	 * @throws LACSDException
	 */
	public GPXInfoVO getPaymentInfo() throws LACSDException{
		return gpxInfoDAO.getPaymentInfo();
	}	
	
	/**
	 * Get direction information
	 * @return
	 * @throws LACSDException
	 */
	public GPXInfoVO getDirectionInfo() throws LACSDException{
		return gpxInfoDAO.getDirectionInfo();
	}	
	
	/**
	 * Get suffix information
	 * @return
	 * @throws LACSDException
	 */
	public GPXInfoVO getSuffixInfo() throws LACSDException{
		return gpxInfoDAO.getSuffixInfo();
	}	
	
	/**
	 * Get district city information
	 * @return
	 * @throws LACSDException
	 */
	public GPXDistrictCityVO getDistrictCityInfo() throws LACSDException{
		return gpxInfoDAO.getDistrictCityInfo();
	}

	/**
	 * Get district by city
	 * @return
	 * @throws LACSDException
	 */
	public GPXInfoVO getDistrictByCity(short cityID) throws LACSDException{
		return gpxDistCityDAO.getDistrictByCity(cityID);
	}

	/**
	 * Get cities by district
	 * @return
	 * @throws LACSDException
	 */
	public GPXInfoVO getCitiesByDistrictID(short districtID) throws LACSDException{
		return gpxDistCityDAO.getCitiesByDistrictID(districtID);
	}

	/**
	 * Get cities belonging to the Districts
	 * @return
	 * @throws LACSDException
	 */
	public GPXInfoVO getDistrictCities() throws LACSDException{
		return gpxInfoDAO.getDistrictCities();
	}

	/**
	 * Find district name by ID
	 * @return
	 * @throws LACSDException
	 */
	public String findDistrictNameByID(short distID) throws LACSDException{
		GPXInfoVO dists = getDistrictInfo();
		String distName = "";
		for (GPXInfoVO dist: dists.getGpxInfoVOs()) {
			if (dist.getId() == distID) {
				distName = dist.getDescription();
				break;
			}
		}
		return distName;
	}

}