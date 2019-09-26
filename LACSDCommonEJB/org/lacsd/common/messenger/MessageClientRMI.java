package org.lacsd.common.messenger;

/******************************************************************************
//* Copyright (c) 2005 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:       MessageClientRMI.java
//* Revision:       1.0
//* Author:         TNGUYEN@LACSD.ORG
//* Created On:     08-06-2005
//* Modified By:
//* Modified On:
//*
//* Description:    MessageClientRMI connects to MessageServerRMI to broadcast
//*                 messages
//* 
/******************************************************************************/

import java.rmi.RemoteException;

import org.lacsd.common.exceptions.LACSDException;

public class MessageClientRMI {

//private MessageServerRMIRemote messageServer;

/**
 * Explicitly defined constructor
 * @param String serverHost
 * @param String serverName
 * @throws RemoteException, Throwable
*/
public MessageClientRMI(String serverHost, String serverName) throws LACSDException {
    super();        
    
    // Get the remote message server
    //messageServer = getMessageServer(serverHost, serverName);
    
}

/**
 * Explicitly defined constructor, used to connect to local MessageServerRMI
 * @param String serverName
 * @throws RemoteException, Throwable
*/
public MessageClientRMI(String serverName) throws LACSDException {
    super();        
    
    // Get the remote message server
    //messageServer = getMessageServer("", serverName);
}

/**
 * Get instance of RMI Server
 * @param String serverHost
 * @param String serverName
 * @return MessageServerRMIRemote
 * @throws Throwable
*/
//private MessageServerRMIRemote getMessageServer(String serverHost, String serverName) throws LACSDException {
//    MessageServerRMIRemote remote = null;
//    try {    
//        String uri = new String("rmi://" + serverHost + "/" + serverName);
//        remote = (MessageServerRMIRemote)Naming.lookup(uri);     //  Connect to remote RMI server
//    }
//    catch ( Exception ex ) {
//        throw new LACSDException("Failed to connect to MessageServerRMI");
//    }
//    return remote;
//}


/**
 * Broadcast new message
 * @param String message
 * @return void
 * @throws Throwable
*/
public void broadcastMessage( String message ) {
//    try {
        //messageServer.broadcastMessage( message );
        System.out.println( message );
//    } catch ( RemoteException ignored ) { }
}

}
