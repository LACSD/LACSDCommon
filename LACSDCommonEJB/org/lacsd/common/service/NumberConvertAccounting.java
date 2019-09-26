package org.lacsd.common.service;

/******************************************************************************
//* Copyright (c) 2003 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		NumberConvertAccounting.java
//* Revision: 		1.1
//* Author:			asrirochanakul@lacsd.org
//* Created On: 	01-06-2004
//* Modified By:	
//* Modified On:	
//*					
//* Description:	Formatting accounting numbers for DB and Web
/******************************************************************************/

import java.math.BigDecimal;

import org.lacsd.common.exceptions.LACSDException;

public class NumberConvertAccounting {

private static NumberConvertAccounting _INSTANCE = new NumberConvertAccounting();

public static final String SIGN_DOLLAR      	= "$";
public static final String SIGN_MINUS       	= "-";
public static final String SIGN_PAREN_LEFT  	= "(";
public static final String SIGN_PAREN_RIGHT 	= ")";

public static final String SIGN_CR			= "CR";

/**
 * Constructor - NumberConvertAccounting
 */
public NumberConvertAccounting() {
	super();
}

/**
 * Return Singleton Instance
 * @return NumberCovnertAccounting
 */
public static NumberConvertAccounting getInstance() {
    return _INSTANCE;
}

/**
 * Format number into currency format (with minus sign if value is negative)
 * i.e.  -####.## --> -$#,###.##
 * 
 * @param String argNumber
 * @return String
 */
public String getCurrencyWithMinusSign(String argNumber) throws LACSDException {
	
	NumberConvert nc = NumberConvert.getInstance();
	
	return nc.getNumberAsCustomFormat(argNumber, SIGN_DOLLAR, SIGN_MINUS, NumberConvert.SIGN_LOCATION_LEFT);
}

/**
 * Format number into currency format (with () if value is negative)
 * i.e.  -####.## --> ($#,###.##)
 * 
 * @param String argNumber
 * @return String
 */
public String getCurrencyWithParen(String argNumber) throws LACSDException {
	
	NumberConvert nc = NumberConvert.getInstance();
	
	return nc.getNumberAsCurrency(argNumber);
}

/**
 * Format number into currency format (with CR if value is negative)
 * i.e.  -####.## --> $#,###.##CR
 * 
 * @param String argNumber
 * @return String
 */
public String getCurrencyWithCR(String argNumber) throws LACSDException {
	
	NumberConvert nc = NumberConvert.getInstance();
	
	return nc.getNumberAsCustomFormat(argNumber, SIGN_DOLLAR, SIGN_CR, NumberConvert.SIGN_LOCATION_RIGHT);
}

/**
 * Format number into #,###,###.#### format (with minus sign if value is negative)
 * i.e.  -####.#### --> -#,###.####
 * 
 * @param String argNumber
 * @return String
 */
public String getNumberWithMinusSign(String argNumber) throws LACSDException {
	
	if (argNumber == null || argNumber.trim().length() == 0) return null;
	
	NumberConvert nc = NumberConvert.getInstance();
	return nc.getRoundOffCommaNumber(argNumber, this.getNumberOfDecimalPlaces(argNumber));
}

/**
 * Format number into #,###.#### format (with () if value is negative)
 * i.e.  -####.#### --> (#,###.####)
 * 
 * @param String argNumber
 * @return String
 */
public String getNumberWithParen(String argNumber) throws LACSDException {
	
	if (argNumber == null || argNumber.trim().length() == 0) return null;
	
	NumberConvert nc = NumberConvert.getInstance();
	
	String s = nc.getRoundOffCommaNumber(argNumber, this.getNumberOfDecimalPlaces(argNumber));

	// replace "-" with ()
	if (s.indexOf("-") > -1)
		s = "(" + s.substring(1) + ")";

	return s;
}

/**
 * Format number into #,###.#### format (with CR if value is negative)
 * i.e.  -####.#### --> #,###.####CR
 * 
 * @param String argNumber
 * @return String
 */
public String getNumberWithCR(String argNumber) throws LACSDException {
	
	if (argNumber == null || argNumber.trim().length() == 0) return null;
	
	NumberConvert nc = NumberConvert.getInstance();
	
	String s = nc.getRoundOffCommaNumber(argNumber, this.getNumberOfDecimalPlaces(argNumber));

	// replace "-" with CR
	if (s.indexOf("-") > -1)
		s = s.substring(1) + SIGN_CR;

	return s;
}

/**
 * Get number of decimal places
 * i.e.  -####.#### --> return 4
 * 
 * @param String argNumber
 * @return int
 */
public int getNumberOfDecimalPlaces(String argNumber) {
	
	if (argNumber == null || argNumber.trim().length() == 0) return 0;
	
	int numOfDecimalPlaces = 0;
	
	if (argNumber.indexOf(".") > -1) {
		String afterDot = argNumber.substring(argNumber.indexOf(".")+1);
		
		for (int i=0; i < afterDot.length(); i++) {
			numOfDecimalPlaces = i+1;
		}
	}
	return numOfDecimalPlaces;
}

/**
 * Get a number value from custom formatted number string
 * i.e.  $####.##   -->  ####.##
 * i.e. ($####.##)  --> -####.##
 * i.e.  $####.##CR --> -####.##
 * i.e. -$####.##   --> -####.##
 * i.e.   ####.##CR --> -####.##
 * 
 * @param String argNumber
 * @return String
 */
public String getCustomFormatAsNumber(String argNumber) throws LACSDException {
	
	if (argNumber == null || argNumber.trim().length() == 0) return null;

	char[] cArr = argNumber.toCharArray();
    StringBuffer sb = new StringBuffer();

	if (argNumber.indexOf(SIGN_PAREN_LEFT) > -1) {
		sb.append("-");
	} else if (argNumber.indexOf(SIGN_MINUS) > -1) {
		sb.append("-");
	} else if (argNumber.indexOf(SIGN_CR) > -1) {
		sb.append("-");
	}
    
    for (int i=0; i<cArr.length; i++) {
        if ( Character.isDigit(cArr[i]) || cArr[i] == '.' ) {
        	sb.append(cArr[i]);
        }
    }

	return new BigDecimal(sb.toString()).toString();
}
                 
public static void main(String[] args) throws LACSDException {

	NumberConvertAccounting nca = NumberConvertAccounting.getInstance();
	//NumberConvert nc = NumberConvert.getInstance();
	
	System.out.println(nca.getCurrencyWithMinusSign("-0001234.349"));
	System.out.println(nca.getCurrencyWithParen("-00066234.34323"));
	System.out.println(nca.getCurrencyWithCR("-000066234.34333"));

	System.out.println("\n");

	System.out.println(nca.getNumberWithMinusSign("-00066234.2948238"));	
	System.out.println(nca.getNumberWithParen("-066234"));
	System.out.println(nca.getNumberWithParen("066234"));
	System.out.println(nca.getNumberWithCR("-006234.099"));
}
}
