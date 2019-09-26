package org.lacsd.common.values;

/******************************************************************************
//* Copyright (c) 2004 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		LoadBalanceVO.java
//* Revision: 		1.1
//* Author:			MFEINBERG@LACSD.ORG
//* Created On: 	07-08-2004
//* Modified By:	
//* Modified On:	
//*					
//* Description:	Load Balancer Value Object
/******************************************************************************/

public class LoadBalanceVO implements java.io.Serializable {

private static final long serialVersionUID = -4023618848584070175L;

private String remoteURI;
private String nodeType;


/**
 * Returns the nodeType.
 * @return String
 */
public String getNodeType() {
	return nodeType;
}

/**
 * Returns the remoteURI.
 * @return String
 */
public String getRemoteURI() {
	return remoteURI;
}

/**
 * Sets the nodeType.
 * @param nodeType The nodeType to set
 */
public void setNodeType(String nodeType) {
	this.nodeType = nodeType;
}

/**
 * Sets the remoteURI.
 * @param remoteURI The remoteURI to set
 */
public void setRemoteURI(String remoteURI) {
	this.remoteURI = remoteURI;
}

}
