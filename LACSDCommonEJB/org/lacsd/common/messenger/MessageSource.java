package org.lacsd.common.messenger;

/******************************************************************************
//* Copyright (c) 2005 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:       MessageSource.java
//* Revision:       1.0
//* Author:         TNGUYEN@LACSD.ORG
//* Created On:     08-06-2005
//* Modified By:
//* Modified On:
//*
//* Description:    MessageSource acts as the source for new messages
//*                 MessageSinks observe this object
//* 
/******************************************************************************/

import java.util.Observable;

public class MessageSource extends Observable {

    /**
     * Notify new message to all the MessageSinks
     * @param String message
     * @return void
    */
    public void sendMessage( String message ) {
        setChanged();
        notifyObservers( message );
    }
}
