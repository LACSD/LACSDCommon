package org.lacsd.common.constants;

/******************************************************************************
//* Copyright (c) 2004 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		LACSDRMIConstants.java
//* Revision: 		1.1
//* Author:			MFEINBERG@LACSD.ORG
//* Created On: 	07-08-2004
//* Modified by:	
//* Modified On:	
//*					
//* Description:	Constants class containing all RMI-NODE Specific Constants.
//* 				These names are the "lookup" BINDINGS for remote RMIREGISTRY!
//* 
//* 				** WARNING ** Changing these constants will require that
//* 				an updated "OSXCommonEJB.jar" file be sent to corresponding
//* 				Remote RMI Node!!
/******************************************************************************/

public class LACSDRMIConstants {

public static final String RMI_REGISTRY_REPORTING 		= "ReportingEngine";
public static final String RMI_REGISTRY_EDMS 			= "JavaEDMS";
public static final String RMI_REGISTRY_PRINTSERVER 		= "JavaPrint";

public static final String NODE_REPORTING 				= "remote.actuate.node";
public static final String NODE_EDMS 					= "remote.edms.node";
public static final String NODE_PRINT 					= "remote.print.node";


}
