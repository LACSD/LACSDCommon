package org.lacsd.common.messenger;

/******************************************************************************
//* Copyright (c) 2005 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:       MessageServerRMIRemote.java
//* Revision:       1.0
//* Author:         TNGUYEN@LACSD.ORG
//* Created On:     08-06-2005
//* Modified By:
//* Modified On:
//*
//* Description:    RMI Remote Interface Message server
//* 
/******************************************************************************/

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MessageServerRMIRemote extends Remote {

/**
 * Public Accessor method for broadcasting message
 * @param String message
 * @return void
 * @throws RemoteException
*/
public void broadcastMessage(String message) throws RemoteException;
    

}
