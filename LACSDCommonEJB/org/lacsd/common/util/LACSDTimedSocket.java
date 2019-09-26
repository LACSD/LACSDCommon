package org.lacsd.common.util;

/******************************************************************************
//* Copyright (c) 2005 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		LACSDTimedSocket.java
//* Revision: 		1
//* Author:			MFEINBERG@LACSD.ORG
//* Created On: 	08-17-2005
//* Modified by:	
//* Modified On:	
//*					
//* Description:	This class offers a timeout feature on socket connections.
//* 				A maximum length of time allowed for a connection can be 
//* 				specified, along with a host and port.
//* 
//* 				A socket factory can be specified, to support secure sockets
//* 				by permitting an SSL socket factory to be used. The factory
//* 				needs to be set up before calling getSocket and passing it
//* 				in as a parameter.
//*
/******************************************************************************/
import java.io.*;
import java.net.*;

import javax.net.SocketFactory;

public class LACSDTimedSocket {

private static final int POLL_DELAY = 100;
private static SocketFactory SOCKETFACTORY = null;


/**
  * Attempts to connect to a service at the specified address
  * and port, for a specified maximum amount of time.
  *
  *	@param	host	Hostname of machine
  *	@param	port	Port of service
  * @param	delay	Delay in milliseconds
*/
public static Socket getSocket(String host,int port,int delay) throws InterruptedIOException, IOException {
	if (SOCKETFACTORY == null) {
		init();
	}
	return getSocket ( host, port, delay, SOCKETFACTORY );
}

/**
 * Attempts to connect to a service at the specified address
 * and port, for a specified maximum amount of time,
 * using a particular socket factory.
 *
 *	@param	addr	Address of host
 *	@param	port	Port of service
 * @param	delay	Delay in milliseconds
*/
public static Socket getSocket(InetAddress addr, int port, int delay) throws InterruptedIOException, IOException {
	if (SOCKETFACTORY == null) {
		init();
	}
	return getSocket (addr,port,delay,SOCKETFACTORY);
}

/**
 * Attempts to connect to a service at the specified address
 * and port, for a specified maximum amount of time.
 *
 *	@param	addr	Address of host
 *	@param	port	Port of service
 * @param	delay	Delay in milliseconds
 * @socket sf      Socket factory to use to create socket
*/
public static Socket getSocket(InetAddress addr,int port,int delay,SocketFactory sf) throws InterruptedIOException, IOException {
	
	if (SOCKETFACTORY == null) {
		init();
	}

	// Create a new socket thread, and start it running
	SocketThread st = new SocketThread( addr, port, sf );
	st.start();

	int timer = 0;
	Socket sock = null;

	for (;;) {
		
		// Check to see if a connection is established
		if (st.isConnected()) {
			// Yes ...  assign to sock variable, and break out of loop
			sock = st.getSocket();
			break;
		}
		else {
			// Check to see if an error occurred
			if (st.isError()) {
				// No connection could be established
				throw (st.getException());
			}

			try {
				// Sleep for a short period of time
					Thread.sleep (POLL_DELAY);
			}
			catch (InterruptedException ie) {}

			// Increment timer
			timer += POLL_DELAY;

			// Check to see if time limit exceeded
			if (timer > delay) {
				
				// Can't connect to server
				throw new InterruptedIOException("Could not connect for " + delay + " milliseconds");
			}
		}
	}
	return sock;
}

/**
 * Return Socket Connection
 * @param host
 * @param port
 * @param delay
 * @param sf
 * @return
 * @throws InterruptedIOException
 * @throws IOException
*/
public static Socket getSocket(String host,int port,int delay,SocketFactory sf) throws InterruptedIOException, IOException {

	if (SOCKETFACTORY == null) {
			init();
	}
	// Convert host into an InetAddress, and call getSocket method
	InetAddress inetAddr = InetAddress.getByName(host);
	return getSocket (inetAddr,port,delay,sf);
}
	
/**
 * Insert the method's description here.
 * Creation date: (9/25/00 1:47:39 PM)
 */
private static synchronized void init() {
	SOCKETFACTORY = SocketFactory.getDefault();
}

/**
 * INNER CLASS
 * ***********************************************************
 * @author mfeinberg
*/
static class SocketThread extends Thread {

	volatile private Socket m_connection = null;
	private String m_host = null;
	private InetAddress m_inet = null;
	private int m_port = 0;
	private IOException m_exception = null;
	
	SocketFactory m_socketFactory = null;

	
	/**
	 * Default Constructor takes host and port
	 * @param host
	 * @param port
	*/
	public SocketThread(String host, int port) {
		this(host, port, SOCKETFACTORY);
	}

	/**
	 * Overridden Constructor also takes SocketFactory
	 * @param host
	 * @param port
	 * @param sf
	*/
	public SocketThread(String host,int port,SocketFactory sf) {
		m_host = host;
		m_port = port;
		m_socketFactory = sf;
	}
	
	/**
	 * Overridden Constructor allows use of InetAddress
	 * @param inetAddr
	 * @param port
	 * @param sf
	 */
	public SocketThread(InetAddress inetAddr,int port,SocketFactory sf) {

		m_inet = inetAddr;
		m_port = port;
		m_socketFactory = sf;
	}
	
	/**
	 * Overridden Constructor - Default
	 * @param inetAddr
	 * @param port
	 */
	public SocketThread(InetAddress inetAddr,int port) {
		this(inetAddr, port, SOCKETFACTORY);
	}

	/**
	 * Primary Execution Block for Thread
	*/
	public void run() {
		Socket sock = null;
		try {
			if (m_host != null) {
				sock = m_socketFactory.createSocket(m_host, m_port);
			}
			else {
				sock = m_socketFactory.createSocket(m_inet, m_port);
			}
		}
		catch (IOException ioe) {
			m_exception = ioe;
			return;
		}
		m_connection = sock;
	}

	/**
	 * IS CONNECTED
	 * @return boolean
	*/
	public boolean isConnected() {
		if (m_connection == null) {
			return false;
		}
		else {
			return true;
		}
	}

	/**
	 * IS ERROR 
	 * @return boolean
	*/
	public boolean isError() {
		if (m_exception == null) {
			return false;
		}
		else {
			return true;
		}
	}

	/**
	 * Returns open socket
	 * @return Socket
	*/
	public Socket getSocket() {
		return m_connection;
	}

	/**
	 * Decipher Exception
	 * @return
	*/
	public IOException getException() {
		return m_exception;
	}
}
}
