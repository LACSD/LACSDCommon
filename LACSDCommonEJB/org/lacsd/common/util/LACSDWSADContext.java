package org.lacsd.common.util;

/******************************************************************************
//* Copyright (c) 2003 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:       LACSDWSADContext.java
//* Revision:       1.0
//* Author:         M Feinberg
//* Created On:     07-08-2003
//* Modified by:    
//* Modified On:    
//*                 
//* Description:    This class wraps a single instance of:
//*                 <code>javax.naming.InitialContext</code> for the entire JVM.
/******************************************************************************/

import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.Name;
import javax.naming.NamingException;

public class LACSDWSADContext implements LACSDInitialContext {

private static LACSDWSADContext _INSTANCE;   //  Singleton
private InitialContext context;

/**
 * Singleton Accessor
 * @return LACSDInitialContext
*/
public static LACSDWSADContext getInstance() throws NamingException {
    
    if (_INSTANCE == null) {
        _INSTANCE = new LACSDWSADContext();
    }
    return _INSTANCE;
}

/**
 * Private Constructor - Singleton Pattern
*/
private LACSDWSADContext() throws NamingException {
    super();    
    init();
}

/**
 * Initialize JNDI Context
 * @return void
*/
private synchronized void init() throws NamingException {

    ApplicationContext environment = ApplicationContext.getInstance();

    Properties properties = new Properties();
    properties.put( Context.PROVIDER_URL,environment.get(ApplicationContext.PROVIDER_URL));
    properties.put( Context.INITIAL_CONTEXT_FACTORY,ApplicationContext.WAS_CONTEXT_FACTORY);

    context = new InitialContext(properties);
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
