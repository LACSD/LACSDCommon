package org.lacsd.common.util;

/******************************************************************************
//* Copyright (c) 2005 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:       LACSDTomcatContext.java
//* Revision:       1.0
//* Author:         tnguyen@lacsd.org
//* Created On:     06-06-2005
//* Modified by:    
//* Modified On:    
//*                 
//* Description:    This class wraps a single instance of:
//*                 <code>javax.naming.Context</code> for Tomcat
/******************************************************************************/

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.Name;
import javax.naming.NamingException;

public class LACSDTomcatContext implements LACSDInitialContext {

private static LACSDTomcatContext _INSTANCE;   //  Singleton
private Context context;

/**
 * Singleton Accessor
 * @return LACSDInitialContext
*/
public static LACSDTomcatContext getInstance() throws NamingException {
    
    if (_INSTANCE == null) {
        _INSTANCE = new LACSDTomcatContext();
    }
    return _INSTANCE;
}

/**
 * Private Constructor - Singleton Pattern
*/
private LACSDTomcatContext() throws NamingException {
    super();    
    init();
}

/**
 * Initialize JNDI Context
 * @return void
*/
private synchronized void init() throws NamingException {

    Context initCtx = new InitialContext();
    context = (Context) initCtx.lookup("java:comp/env");
}

/**
 * Make name in namespace publicly available
 * @return String
 * @throws NamingException
*/
public String getNameInNamespace() throws NamingException {
	return context.getNameInNamespace();
}

/**
 * Lookup the object for the specified key.
 * @return java.lang.Object
 * @param name java.lang.String
 * @exception javax.naming.NamingException
*/
public Object lookup(String name) throws NamingException {
    return this.context.lookup(name);
}

/**
 * Lookup the object for the specified Name.
 * @return java.lang.Object
 * @param name java.lang.String
 * @exception javax.naming.NamingException
*/
public Object lookup(Name name) throws NamingException {
    return this.context.lookup(name);
}
}
