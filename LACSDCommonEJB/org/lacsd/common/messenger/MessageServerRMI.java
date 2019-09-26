package org.lacsd.common.messenger;

/******************************************************************************
//* Copyright (c) 2005 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:       MessageServerRMI.java
//* Revision:       1.0
//* Author:         TNGUYEN@LACSD.ORG
//* Created On:     08-06-2005
//* Modified By:
//* Modified On:
//*
//* Description:    MessageServer
//* 
/******************************************************************************/

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class MessageServerRMI extends UnicastRemoteObject implements MessageServerRMIRemote {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = -5512555804075773689L;
	private Registry registry;
    private MessageSource source = new MessageSource();
    private String serverName;
    
    private long prevBroadcastTime = System.currentTimeMillis();
        

    /**
     * Explicitly defined constructor (for RMI),
     * @throws RemoteException
    */
    public MessageServerRMI(String serverName) throws RemoteException, Exception {
        super();
        this.serverName = serverName;
        this.initialize();      //  Initialize this RMI Server Class
    }

    /**
     * Initializes the ReportingEngine
     * @return void
    */
    private void initialize() throws Exception {

        try {
            // Try to find the appropriate registry already running
            registry = LocateRegistry.getRegistry(1099);
            registry.list();    // Verify it's alive and well
        }
        catch (Exception ex) {
            // Couldn't get a valid registry
            registry = null;
        }
        
        // If we couldn't find it, we need to create it.
        // (Equivalent to running "rmiregistry"
        if ( registry == null ) {
            registry = LocateRegistry.createRegistry(1099);
        }
        
        // If we get here, we must have a valid registry.
        // Now register this server instance with that registry.
        // "Rebind" to replace any other objects using our name.
        registry.rebind(this.serverName, this);
                        
    }
    
    /**
     * Unbind the server instance from the registry
     * @return void
     * @throws Exception
    */
    public void unbind() throws Exception {
        if ( registry != null ) registry.unbind(this.serverName);
    }

    /**
     * This method informs all currently listening clients that there
     * is a new message.  Causes all calls to getNextMessage() to unblock.
     * @param String message
     * @return void
     * @throws RemoteException
    */
    public synchronized void broadcastMessage(String message) {

        while ( System.currentTimeMillis() - prevBroadcastTime < 500 ) {
        }
        prevBroadcastTime = System.currentTimeMillis();
        
        // Send the messsage to all the recipients (MessageSinks) by giving the
        // message to the message source
        source.sendMessage( message );
    }

    /**
     * This method returns the next new message.
     * It blocks until there is one.
     * @return String
    */
    public String getNextMessage() {
        // Create a message sink to wait for a new message from the
        // message source
        return new MessageSink().getNextMessage(source);
    }

}
