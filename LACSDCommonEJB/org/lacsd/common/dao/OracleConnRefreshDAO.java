package org.lacsd.common.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.lacsd.common.exceptions.LACSDException;

/******************************************************************************
//* Copyright (c) 2017 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:       OracleConnRefreshDAO.java
//* Revision:	    1.0
//* Author:         hho@lacsd.org
//* Created On:     Aug 28, 2017
//* Modified By:
//* Modified On:
//*                 
//* Description: Refresh Oracle connection
/******************************************************************************/

public class OracleConnRefreshDAO extends LACSDOracleDAO{

	public OracleConnRefreshDAO(String datasourceName) {
		super(datasourceName);
	}
	
	public void doRefreshDB() throws LACSDException {
		Connection conn = null;
		try {
			conn = super.getConnection();
			PreparedStatement ps = conn.prepareStatement("SELECT SysDate from Dual");
			ps.executeQuery();
		} catch (SQLException e) {
		    throw new LACSDException(e.toString());
		} finally {
			try {
	            if (conn != null) {
	                super.returnConnection(conn);
	            }
	        }
	        catch (Exception e) {
	            throw new LACSDException("Error 007 - Database Connection Failed to Close", e.fillInStackTrace());
	        }
		}		
	}
}
