package org.lacsd.common.messenger;

/******************************************************************************
//* Copyright (c) 2005 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:       MessageSink.java
//* Revision:       1.0
//* Author:         TNGUYEN@LACSD.ORG
//* Created On:     08-06-2005
//* Modified By:
//* Modified On:
//*
//* Description:    MessageSink acts as the receiver of new messages
//*                 It listens to the MessageSource
//* 
/******************************************************************************/

import java.util.Observable;
import java.util.Observer;

public class MessageSink implements Observer {

    String message = null; // set by update() and read by getNextMessage()

    /**
     * Called by the message source when it gets a new message
     * @param Observable o
     * @param Object arg
     * @return void
    */
    synchronized public void update(Observable o, Object arg) {
        // Get the new message
        message = (String)arg;
        
        // Wake up our waiting thread
        notify();
    }

    /**
     * Gets the next message sent out from the message source
     * @param MessageSource source
     * @return String
    */
    synchronized public String getNextMessage( MessageSource source ) {
        // Tell source we want to be told about new messages
        source.addObserver(this);
        
        // Wait until our update() method receives a message
        // or 10 minutes has elapsed
        while ( message == null ) {
            try {
                wait(60000*10);
            } catch ( Exception ignored ) { }
        }
        
        // Tell source to stop telling us about new messages
        source.deleteObserver(this);
        
        // Now return the message we received
        // But first set the message instance variable to null
        // so update() and getNextMessage() can be called again.
        String messageCopy = message;
        return messageCopy;
    }
}
