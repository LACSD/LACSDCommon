package org.lacsd.common.service;

/******************************************************************************
//* Copyright (c) 2011 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		LACSDFile.java
//* Revision: 		1.0
//* Author:			asrirochanakul
//* Created On: 	07-22-2011
//* Modified by:	
//* Modified On:	
//*					
//* Description:	
/******************************************************************************/

import java.security.CodeSource;
import java.security.ProtectionDomain;

public class LACSDDir {

private static LACSDDir _INSTANCE = new LACSDDir();		// Singleton Instance

/**
 * Private Constructor - Singleton Pattern
*/
private LACSDDir() {
	super();
}

/**
 * Return Singleton Instance
 * @return LACSDFileIO
*/
public static LACSDDir getInstance() {
	return _INSTANCE;
}

/**
 * Retrieve path of a class in "c:/dir" format
 * @param Class c
 * @return String rootPath
 */
@SuppressWarnings({ "rawtypes" })
public String getRootPath(Class c) {
	
	ProtectionDomain pd = c.getProtectionDomain();
	if (pd == null) {
		return null;
	}
	CodeSource cs = pd.getCodeSource();
	if (cs == null || cs.getLocation() == null) {
		return null;
	}
	
	String rootPath = cs.getLocation()+"";
	rootPath = rootPath.replaceFirst("file:/", "");

	return rootPath;
}

/**
 * Main Execution Block
 * @param args
 */
public static void main(String[] args) {

	LACSDDir f = new LACSDDir();
	
	System.out.println(f.getRootPath(f.getClass()));
	
	System.exit(0);
}

}