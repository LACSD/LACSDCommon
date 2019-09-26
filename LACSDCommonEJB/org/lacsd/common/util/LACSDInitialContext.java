package org.lacsd.common.util;

/******************************************************************************
//* Copyright (c) 2005 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		LACSDInitialContext.java
//* Revision: 		1.0
//* Author:			tnguyen@lacsd.org
//* Created On: 	06-06-2005
//* Modified by:	
//* Modified On:	
//*					
//* Description:	Naming Context interface
/******************************************************************************/

import javax.naming.Name;
import javax.naming.NamingException;

public interface LACSDInitialContext {

/**
 * Lookup the object for the specified key.
 * @return java.lang.Object
 * @param name java.lang.String
 * @exception javax.naming.NamingException
*/
public Object lookup(String name) throws NamingException;

/**
 * Lookup the object for the specified Name.
 * @return java.lang.Object
 * @param name java.lang.String
 * @exception javax.naming.NamingException
*/
public Object lookup(Name name) throws NamingException;

/**
 * Make name in namespace publicly available
 * @return String
 * @throws NamingException
*/
public String getNameInNamespace() throws NamingException;
}
