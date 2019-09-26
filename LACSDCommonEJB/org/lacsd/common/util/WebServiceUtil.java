package org.lacsd.common.util;
/******************************************************************************
 //* Copyright (c) 2007 Sanitation Districts of Los Angeles. All Rights Reserved.
 //* Filename:       WebServiceUtil.java
 //* Revision:       1.0
 //* Author:         mfeinberg
 //* Created On:     Aug 21, 2007
 //* Modified By:
 //* Modified On:
 //*                 
 //* Description:	 Helpful SOAP Web Services Utilities
/******************************************************************************/
import java.lang.reflect.Method;

public class WebServiceUtil {

/**
 * Helpful utility to prevent against a "Not Nillable" Exception (Apache Axis Fault)
 * Set NULL java.lang.String values to ""
 * @param Object valueObject
 * @return Object
 * @throws Exception
*/
public static Object removeNills(Object valueObject) throws Exception {
    
	Method[] methods = valueObject.getClass().getMethods();
	outer: for (int i=0; i<methods.length; i++) {
		if (methods[i].getName().startsWith("get")) {
			Object obj = methods[i].invoke(valueObject, new Object[] { });
			boolean fixNeeded = false;
			if (obj == null) {
				fixNeeded = true;
			}
			else if(obj instanceof String) {
				String strObj = (String)obj;
				if (strObj.length() < 1) {
					fixNeeded = true;
				}
			}
			if (fixNeeded) {
				String setterSuffix = methods[i].getName().substring(3,methods[i].getName().length());
				fixer: for (int j=0; j<methods.length; j++) {
					if ((methods[j].getName().startsWith("set")) && (methods[j].getName().substring(3,methods[j].getName().length()).equalsIgnoreCase(setterSuffix))) {
						Class<?>[] paramTypes = methods[j].getParameterTypes();
						Object[] args = new Object[1];
						for (int k=0; k<paramTypes.length; k++) {
							if (paramTypes[k] == Class.forName("java.lang.String")) {
								args[0] = "";
							}
							else if (paramTypes[k] == Class.forName("java.lang.Integer")) {
								args[0] = new Integer(0);
							}
							else if (paramTypes[k] == Class.forName("java.lang.Long")) {
								args[0] = new Long(0);
							}
							else if (paramTypes[k] == Class.forName("java.lang.Double")) {
								args[0] = new Double(0);
							}
							else if (paramTypes[k] == Class.forName("java.util.Date")) {
								//args[0] = new java.util.Date();  // null dates become "now"
								continue outer; // do not override date elements - leave them as null
							}
							else if (paramTypes[k] == Class.forName("java.util.ArrayList")) {
								continue outer; // do not override ArrayList elements - leave them as null
							}
							else {
								args[0] = "";
							}
						}
						methods[j].invoke(valueObject, args);
						break fixer;
					}
				}
			}
		}
	}
	return valueObject;
}
}

