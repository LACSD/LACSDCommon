package org.lacsd.common.helper;

/******************************************************************************
//* Copyright (c) 2004 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:       SQLTypeNullObject.java
//* Revision:       1.0
//* Author:         dyip@lacsd.org
//* Created On:     01-08-2004
//* Modified By:    
//* Modified On:    
//*                 
//* Description:    SQL Types Null Object used in LACSD Prod DAO's
//* 
/******************************************************************************/

public class SQLTypeNullObject {
    
    /**
     * Constructor
     * -----------------------------------------------------------------------
     * @param String spname
    */
    public SQLTypeNullObject(int sqlType) {
        this.setSqlType(sqlType);
    }

    private int sqlType;

	/**
	 * Returns the sqlType.
	 * @return int
	 */
	public int getSqlType() {
		return sqlType;
	}

	/**
	 * Sets the sqlType.
	 * @param sqlType The sqlType to set
	 */
	public void setSqlType(int sqlType) {
		this.sqlType = sqlType;
	}

}
