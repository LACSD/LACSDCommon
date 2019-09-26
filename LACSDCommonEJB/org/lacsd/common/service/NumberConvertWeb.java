package org.lacsd.common.service;

/******************************************************************************
//* Copyright (c) 2003 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		NumberConvertWeb.java
//* Revision: 		1.1
//* Author:			asrirochanakul@lacsd.org
//* Created On: 	01-06-2004
//* Modified By:	
//* Modified On:	
//*					
//* Description:	Formatting number representation for Web
//*                 such as phone, fax, zip code, social security no., etc.
/******************************************************************************/

public class NumberConvertWeb {

private static NumberConvertWeb _INSTANCE = new NumberConvertWeb();

public static final String FORMAT_PHONE_NO_EXT = "###-###-####";
public static final String FORMAT_PHONE = "###-###-#### x#####";
public static final String FORMAT_PHONE_NO_EXT_WITH_BRACKET = "(###)###-####";
public static final String FORMAT_PHONE_WITH_BRACKET = "(###)###-#### EXT. ####";
public static final String FORMAT_FAX = "###-###-####";
public static final String FORMAT_FAX_NO_EXT_WITH_BRACKET = "(###)###-####";
public static final String FORMAT_FAX_WITH_BRACKET = "(###)###-#### EXT. ####";
public static final String FORMAT_ZIP = "#####-####";



/**
 * Constructor - NumberConvertWeb
 */
public NumberConvertWeb() {
	super();
}

/**
 * Return Singleton Instance
 * @return NumberCovnertWeb
 */
public static NumberConvertWeb getInstance() {
    return _INSTANCE;
}

/**
 * Convert a set of digitals to phone with no extension format ###-###-####
 * @param String argDigits
 * @return String
 */
public String convertDigitsToPhoneNoExtFormat(String argDigits) {
  	
    if (argDigits == null || argDigits.trim().length() == 0)
    	return FORMAT_PHONE_NO_EXT;
    else 
	    return NumberConvert.getInstance().convertDigitsToCustomFormat(argDigits,FORMAT_PHONE_NO_EXT);
}

/**
 * Convert a set of digitals to phone format ###-###-#### x#####
 * @param String argDigits
 * @return String
 */
public String convertDigitsToPhoneFormat(String argDigits) {
  	
    if (argDigits == null || argDigits.trim().length() == 0)
    	return FORMAT_PHONE;
    else 
	    return NumberConvert.getInstance().convertDigitsToCustomFormat(argDigits,FORMAT_PHONE);
}

/**
 * Convert a set of digitals to specified phone format
 * @param String argDigits
 * @return String
 */
public String convertDigitsToPhoneFormat(String argDigits,String pattern) {
  	
    if (argDigits == null || argDigits.trim().length() == 0)
    	return pattern;
    else 
	    return NumberConvert.getInstance().convertDigitsToCustomFormat(argDigits,pattern);
}

/**
 * Convert a set of digitals to fax format ###-###-####
 * @param String argDigits
 * @return String
 */
public String convertDigitsToFaxFormat(String argDigits) {
    
    if (argDigits == null || argDigits.trim().length() == 0)
    	return FORMAT_FAX;
    else
	    return NumberConvert.getInstance().convertDigitsToCustomFormat(argDigits,FORMAT_FAX);
}

/**
 * Convert a set of digitals to specified fax format
 * @param String argDigits
 * @return String
 */
public String convertDigitsToFaxFormat(String argDigits,String pattern) {
    
    if (argDigits == null || argDigits.trim().length() == 0)
    	return pattern;
    else
	    return NumberConvert.getInstance().convertDigitsToCustomFormat(argDigits,pattern);
}

/**
 * Convert a set of digitals to zip code format #####-####
 * @param String argDigits
 * @return String
 */
public String convertDigitsToZipFormat(String argDigits) {
	if (argDigits == null || argDigits.trim().length() == 0)
    	return FORMAT_ZIP;
    else
    	return NumberConvert.getInstance().convertDigitsToCustomFormat(argDigits,FORMAT_ZIP);
}

public static void main(String[] args) {
}
}
