package org.lacsd.gpx;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.lacsd.common.constants.LACSDSPConstants;
import org.lacsd.common.dao.LACSDSqlServerProcDAO;
import org.lacsd.common.exceptions.LACSDException;
import org.lacsd.common.values.GPXInfoVO;

/******************************************************************************
//* Copyright (c) 2016 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		GPXDistrictCityDAO.java
//* Revision: 		1.0
//* Author:			asrirochanakul@lacsd.org
//* Created On: 	Dec 14, 2016
//* Modified By:    
//* Modified On:    
//* 
//* Description:	
/******************************************************************************/

public class GPXDistrictCityDAO extends LACSDSqlServerProcDAO{

	private String action; 
	private GPXInfoVO gpxInfoVO;
	private final static String GET_DISTRICT_BY_CITY	= "I";
	private final static String GET_CITIES_BY_DISTRICT	= "J";
	
	public GPXDistrictCityDAO() {
		super(LACSDSPConstants.GPXINFO002SP);
	}

	/**
	 * Get district by city ID
	 * @return
	 * @throws LACSDException
	 */
	public GPXInfoVO getDistrictByCity(short cityID) throws LACSDException {
		super.setQueryType(EXECUTE_QUERY);
		this.action = GET_DISTRICT_BY_CITY;
		gpxInfoVO = new GPXInfoVO();
		this.gpxInfoVO.setId(cityID);
		execute();
		return this.gpxInfoVO;
	}	

	/**
	 * Get cities by district
	 * @return
	 * @throws LACSDException
	 */
	public GPXInfoVO getCitiesByDistrictID(short distID) throws LACSDException {
		super.setQueryType(EXECUTE_QUERY);
		this.action = GET_CITIES_BY_DISTRICT;
		gpxInfoVO = new GPXInfoVO();
		this.gpxInfoVO.setId(distID);
		execute();
		return this.gpxInfoVO;
	}	

	/**
	 * HANDLE OUTPUT TYPE 1:	Stored Procedure Returns an Open Cursor
	 * @param ResultSet rs
	 * @return void
	 * @throws SQLException, LACSDException
	*/
	protected void getResultsFromResultSet(ResultSet rs) throws SQLException, LACSDException {
		if (action.equals(GET_DISTRICT_BY_CITY)) {
			getDistrictFromResultSet(rs);
		}
		else if (action.equals(GET_CITIES_BY_DISTRICT)) {
			getCityFromResultSet(rs);
		}
	}
	
	/**
	 * Get city info from result set
	 * @param rs
	 * @throws SQLException
	 * @throws LACSDException
	 */
	private void getCityFromResultSet(ResultSet rs) throws SQLException, LACSDException {
		ArrayList<GPXInfoVO> infoVOs = new ArrayList<GPXInfoVO>();
		GPXInfoVO infoVO = null;
		while (rs.next()) {
			infoVO = new GPXInfoVO();
			infoVO.setId(rs.getInt("CITYID"));
			infoVO.setDescription(rs.getString("NAME"));
			infoVOs.add(infoVO);
		}
		this.gpxInfoVO.setGpxInfoVOs(infoVOs);
	}
	
	/**
	 * Get district info from result set
	 * @param rs
	 * @throws SQLException
	 * @throws LACSDException
	 */
	private void getDistrictFromResultSet(ResultSet rs) throws SQLException, LACSDException {
		ArrayList<GPXInfoVO> infoVOs = new ArrayList<GPXInfoVO>();
		GPXInfoVO infoVO = null;
		while (rs.next()) {
			infoVO = new GPXInfoVO();
			infoVO.setId(rs.getInt("SANDISTRICTID"));
			infoVO.setDescription(rs.getString("NAME"));
			infoVOs.add(infoVO);
		}
		this.gpxInfoVO.setGpxInfoVOs(infoVOs);
	}

	protected void getResultsFromString(String[] output) throws LACSDException {
		// TODO Auto-generated method stub
	}
	
	/**
	 * SETUP STEP 1:  Register Input Parameters
	 * @return void
	 * @throws LACSDException
	 */
	protected void registerInputs() throws LACSDException {
		setInputParam(1, this.action);
		setInputParam(2, this.gpxInfoVO.getId());
	}

	/**
	 * SETUP STEP 2:	Register Output Parameters
	 * @return void
	 * @throws LACSDException
	 */
	protected void registerOutputs() throws LACSDException { 
		//super.setOutput(1, Types.VARCHAR); 
	  //setOutput(1, Types.VARCHAR);
	}
}