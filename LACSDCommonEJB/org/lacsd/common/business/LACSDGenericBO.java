package org.lacsd.common.business;

/******************************************************************************
//* Copyright (c) 2003 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:       LACSDGenericBO.java
//* Revision:       1.0
//* Author:         tnguyen@lacsd.org
//* Created On:     01-12-2004
//* Modified By:    
//* Modified On:    
//*                 
//* Description:    Abstract core class for BO objects
/******************************************************************************/

import org.apache.logging.log4j.LogManager;

import org.apache.logging.log4j.Logger;

public abstract class LACSDGenericBO {

private final String CLASS_NAME = this.getClass().getName();
protected Logger log = LogManager.getLogger(CLASS_NAME);

}
