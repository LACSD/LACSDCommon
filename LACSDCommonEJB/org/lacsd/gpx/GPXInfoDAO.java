package org.lacsd.gpx;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.lacsd.common.constants.LACSDSPConstants;
import org.lacsd.common.dao.LACSDSqlServerProcDAO;
import org.lacsd.common.exceptions.LACSDException;
import org.lacsd.common.values.GPXDistrictCityVO;
import org.lacsd.common.values.GPXInfoVO;

/******************************************************************************
//* Copyright (c) 2012 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		GPXInfoDAO.java
//* Revision: 		1.0
//* Author:			hho@lacsd.org
//* Created On: 	Nov 29, 2012
//* Modified By:    
//* Modified On:    
//* 
//* Description:	
/******************************************************************************/

public class GPXInfoDAO extends LACSDSqlServerProcDAO{

	private String action; 
	private GPXDistrictCityVO gpxDistrictCityVO;
	private GPXInfoVO gpxInfoVO;
	private final static String GET_STATE_ACTION = "A";
	private final static String GET_CARTYPE_ACTION = "B";
	private final static String GET_CITY_ACTION = "C";
	private final static String GET_DISTRICT_ACTION = "D";
	private final static String GET_PAYMENT_ACTION = "E";
	private final static String GET_DIRECTION_ACTION = "F";
	private final static String GET_SUFFIX_ACTION = "G";
	private final static String GET_DISTRICT_CITY_ACTION = "H";
	private final static String GET_DISTRICT_CITIES_ACTION = "I";
	
	public GPXInfoDAO() {
		super(LACSDSPConstants.GPXINFO001SP);
		gpxDistrictCityVO = new GPXDistrictCityVO();
		gpxInfoVO = new GPXInfoVO();
	}
	
	/**
	 * Get State Information
	 * @return
	 * @throws LACSDException
	 */
	public GPXInfoVO getStateInfo() throws LACSDException{
		super.setQueryType(EXECUTE_QUERY);
		this.action = GET_STATE_ACTION;
		
		execute();
		return this.gpxInfoVO;
	}
	
	/**
	 * Get Card Type Information
	 * @return
	 * @throws LACSDException
	 */
	public GPXInfoVO getCardTypeInfo() throws LACSDException{
		super.setQueryType(EXECUTE_QUERY);
		this.action = GET_CARTYPE_ACTION;
		
		execute();
		return this.gpxInfoVO;
	}

	/**
	 * Get city Information
	 * @return
	 * @throws LACSDException
	 */
	public GPXInfoVO getCityInfo() throws LACSDException{
		super.setQueryType(EXECUTE_QUERY);
		this.action = GET_CITY_ACTION;
		
		execute();
		return this.gpxInfoVO;
	}
	/**
	 * Get district Information
	 * @return
	 * @throws LACSDException
	 */
	public GPXInfoVO getDistrictInfo() throws LACSDException{
		super.setQueryType(EXECUTE_QUERY);
		this.action = GET_DISTRICT_ACTION;
		
		execute();
		return this.gpxInfoVO;
	}	
	
	/**
	 * Get payment Information
	 * @return
	 * @throws LACSDException
	 */
	public GPXInfoVO getPaymentInfo() throws LACSDException{
		super.setQueryType(EXECUTE_QUERY);
		this.action = GET_PAYMENT_ACTION;
		
		execute();
		return this.gpxInfoVO;
	}
	
	/**
	 * Get direction Information
	 * @return
	 * @throws LACSDException
	 */
	public GPXInfoVO getDirectionInfo() throws LACSDException{
		super.setQueryType(EXECUTE_QUERY);
		this.action = GET_DIRECTION_ACTION;
		
		execute();
		return this.gpxInfoVO;
	}
	
	/**
	 * Get suffix Information
	 * @return
	 * @throws LACSDException
	 */
	public GPXInfoVO getSuffixInfo() throws LACSDException{
		super.setQueryType(EXECUTE_QUERY);
		this.action = GET_SUFFIX_ACTION;
		
		execute();
		return this.gpxInfoVO;
	}
	
	/**
	 * Get suffix Information
	 * @return
	 * @throws LACSDException
	 */
	public GPXDistrictCityVO getDistrictCityInfo() throws LACSDException{
		super.setQueryType(EXECUTE_QUERY);
		this.action = GET_SUFFIX_ACTION;
		
		execute();
		return this.getDistrictCityInfo();
	}

	/**
	 * Get cities belonging to the Districts
	 * @return
	 * @throws LACSDException
	 */
	public GPXInfoVO getDistrictCities() throws LACSDException{
		super.setQueryType(EXECUTE_QUERY);
		this.action = GET_DISTRICT_CITIES_ACTION;
		
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
		if (action.equals(GET_STATE_ACTION)) {
			getStateFromResultSet(rs);
		}else if (action.equals(GET_CARTYPE_ACTION)) {
			getCartTypeFromResultSet(rs);
		}else if (action.equals(GET_CITY_ACTION) || action.equals(GET_DISTRICT_CITIES_ACTION)) {
			getCityFromResultSet(rs);
		}else if (action.equals(GET_DISTRICT_ACTION)) {
			getDistrictFromResultSet(rs);
		}else if (action.equals(GET_PAYMENT_ACTION)) {
			getPaymentFromResultSet(rs);
		}else if (action.equals(GET_DIRECTION_ACTION)) {
			getDirectionFromResultSet(rs);
		}else if (action.equals(GET_SUFFIX_ACTION)) {
			getSuffixFromResultSet(rs);
		}else if (action.equals(GET_DISTRICT_CITY_ACTION)) {
			getDistrictCityFromResultSet(rs);
		}
	}
	
	/**
	 * Get state info from result set
	 * @param rs
	 * @throws SQLException
	 * @throws LACSDException
	 */
	private void getStateFromResultSet(ResultSet rs) throws SQLException, LACSDException {
		ArrayList<GPXInfoVO> infoVOs = new ArrayList<GPXInfoVO>();
		GPXInfoVO infoVO = null;
		while (rs.next()) {
			infoVO = new GPXInfoVO();
			infoVO.setCode(rs.getString("STATE"));
			infoVO.setDescription(rs.getString("DESCR"));
			infoVOs.add(infoVO);
		}
		this.gpxInfoVO.setGpxInfoVOs(infoVOs);
	}
	
	/**
	 * Get card type info from result set
	 * @param rs
	 * @throws SQLException
	 * @throws LACSDException
	 */
	private void getCartTypeFromResultSet(ResultSet rs) throws SQLException, LACSDException {
		ArrayList<GPXInfoVO> infoVOs = new ArrayList<GPXInfoVO>();
		GPXInfoVO infoVO = null;
		while (rs.next()) {
			infoVO = new GPXInfoVO();
			infoVO.setCode(rs.getString("CARDTYPE"));
			infoVO.setDescription(rs.getString("DESCR"));
			infoVOs.add(infoVO);
		}
		this.gpxInfoVO.setGpxInfoVOs(infoVOs);
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
	
	/**
	 * Get payment info from result set
	 * @param rs
	 * @throws SQLException
	 * @throws LACSDException
	 */
	private void getPaymentFromResultSet(ResultSet rs) throws SQLException, LACSDException {
		ArrayList<GPXInfoVO> infoVOs = new ArrayList<GPXInfoVO>();
		GPXInfoVO infoVO = null;
		while (rs.next()) {
			infoVO = new GPXInfoVO();
			infoVO.setCode(rs.getString("PAYMENTMETHOD"));
			infoVO.setDescription(rs.getString("DESCR"));
			infoVOs.add(infoVO);
		}
		this.gpxInfoVO.setGpxInfoVOs(infoVOs);
	}
	
	/**
	 * Get direction info from result set
	 * @param rs
	 * @throws SQLException
	 * @throws LACSDException
	 */
	private void getDirectionFromResultSet(ResultSet rs) throws SQLException, LACSDException {
		ArrayList<GPXInfoVO> infoVOs = new ArrayList<GPXInfoVO>();
		GPXInfoVO infoVO = null;
		while (rs.next()) {
			infoVO = new GPXInfoVO();
			infoVO.setCode(rs.getString("DIRECTION"));
			infoVO.setDescription(rs.getString("DESCR"));
			infoVOs.add(infoVO);
		}
		this.gpxInfoVO.setGpxInfoVOs(infoVOs);
	}
	 
	/**
	 * Get suffix info from result set
	 * @param rs
	 * @throws SQLException
	 * @throws LACSDException
	 */
	private void getSuffixFromResultSet(ResultSet rs) throws SQLException, LACSDException {
		ArrayList<GPXInfoVO> infoVOs = new ArrayList<GPXInfoVO>();
		GPXInfoVO infoVO = null;
		while (rs.next()) {
			infoVO = new GPXInfoVO();
			infoVO.setCode(rs.getString("SUFFIX"));
			infoVO.setDescription(rs.getString("DESCR"));
			infoVOs.add(infoVO);
		}
		this.gpxInfoVO.setGpxInfoVOs(infoVOs);
	}
	
	/**
	 * Get district city info from result set
	 * @param rs
	 * @throws SQLException
	 * @throws LACSDException
	 */
	private void getDistrictCityFromResultSet(ResultSet rs) throws SQLException, LACSDException {
		ArrayList<GPXDistrictCityVO> infoVOs = new ArrayList<GPXDistrictCityVO>();
		GPXDistrictCityVO infoVO = null;
		while (rs.next()) {
			infoVO = new GPXDistrictCityVO();
			infoVO.setCityID(rs.getInt("CITYID"));
			infoVO.setDistrictID(rs.getInt("SANDISTRICTID"));
			infoVO.setCityName(rs.getString("CITYNAME"));
			infoVO.setDistrictName(rs.getString("DISTRICTNAME"));
			infoVO.setEffective(rs.getString("ISEFFECTIVE"));
			infoVOs.add(infoVO);
		}
		this.gpxDistrictCityVO.setGpxDistrictCityVOs(infoVOs);
	}
	/* (non-Javadoc)
	 * @see org.lacsd.common.dao.LACSDSqlSrvr2KProcDAO#getResultsFromString(java.lang.String[])
	 */
	
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
