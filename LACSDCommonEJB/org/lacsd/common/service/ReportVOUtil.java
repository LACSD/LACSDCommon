package org.lacsd.common.service;

/******************************************************************************
 //* Copyright (c)   2008 Sanitation Districts of Los Angeles. All Rights Reserved.
 //* Filename:		ReportingVOUtil.java
 //* Revision: 		version 1.0
 //* Author:		hho@lacsd.org
 //* Created On: 	Nov 24, 2008
 //* Modified By:
 //* Modified On:
 //*
 //* Description:	Utility to manipulate reportingVO object
 //*
 /*****************************************************************************/
import java.util.ArrayList;

import org.lacsd.reporting.values.ReportParameterVO;
import org.lacsd.reporting.values.ReportVO;

public class ReportVOUtil {
	
	private static ReportVOUtil _INSTANCE = new ReportVOUtil();

	/**
	 * Constructor - ReportVOUtil
	 */
	private ReportVOUtil() {
	    super();
	}

	/**
	 * Return Singleton Instance
	 * @return ReportVOUtil
	 */
	public static ReportVOUtil getInstance() {
	    return _INSTANCE;
	}
	
	/**
	 * Set report parameter
	 * @param reportingVO
	 * @param paramName
	 * @param paramValue
	 * @return
	 */
	public ReportVO setParamValue( ReportVO reportVO, String paramName, String paramValue ) {
		ArrayList<ReportParameterVO> parameters = reportVO.getReportParamsList();
		if (parameters == null ) {
			parameters = new ArrayList<ReportParameterVO>();
		}
		ReportParameterVO parameterVO =new ReportParameterVO();
		parameterVO.setName(paramName);
		parameterVO.setValue(paramValue);
		parameters.add(parameterVO);
		reportVO.setReportParamsList(parameters);
		return reportVO;
	}

}
