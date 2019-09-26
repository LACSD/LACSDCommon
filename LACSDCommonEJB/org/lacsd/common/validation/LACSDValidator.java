package org.lacsd.common.validation;

/******************************************************************************
//* Copyright (c) 2004 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		LACSDValidator.java
//* Revision: 		1.0
//* Author:			ASRIROCHANAKUL@LACSD.ORG
//* Created On: 	2-10-2004
//* Modified By:	
//* Modified On:	
//*					
//* Description:	A validator object for generic fields
/******************************************************************************/

import java.util.Locale;

import org.apache.commons.validator.GenericValidator;

public class LACSDValidator {
	
private static LACSDValidator _INSTANCE = new LACSDValidator();

public static final String STRUTS_ACTION_ERROR_KEY = "org.apache.struts.action.ERROR";
								//  Attribute name in request

public static final String PATTERN_DIGITS 		= "^\\d+$";
public static final String PATTERN_CURRENCY 		= "^-?\\$?((\\d{1,3}(\\,\\d{3})*|\\d+)(\\.\\d{0,2})?)$|^-?\\$?\\.\\d{1,2}?$";
public static final String PATTERN_DATE 			= "^\\d{1,2}/\\d{1,2}/\\d{4}$";
public static final String PATTERN_FAX			= "^\\d{3}-\\d{3}-\\d{4}|\\d{10}$";
public static final String PATTERN_FAX_FORMAT	= "^###-###-####$";
public static final String PATTERN_FAX_WITH_BRACKET			= "^(\\(\\d{3}\\)\\d{3}-\\d{4})(\\sEXT.\\s\\d{1,4})?$";
public static final String PATTERN_FAX_FORMAT_WITH_BRACKET	= "^(###)###-#### EXT. ####?$";
public static final String PATTERN_NUMBER 		= "^-?((\\d{1,3}(\\,\\d{3})*|\\d+)(\\.\\d*)?)$|^-?\\.\\d+?$";
public static final String PATTERN_NON_NEGATIVE_NUMBER 		= "^(\\d+(\\.\\d*)?)$";
public static final String PATTERN_NON_NEGATIVE_INTEGER_NUMBER = "^(\\d+)$";
public static final String PATTERN_PHONE			= "^(\\d{3}-\\d{3}-\\d{4}|\\d{10})( ?[xX]\\d{1,5})?$";
public static final String PATTERN_PHONE_FORMAT	= "^###-###-####( x#####)?$";
public static final String PATTERN_PHONE_WITH_BRACKET		= "^(\\(\\d{3}\\)\\d{3}-\\d{4})(\\sEXT.\\s\\d{1,4})?$";
public static final String PATTERN_PHONE_FORMAT_WITH_BRACKET	= "^(###)###-#### EXT. ####?$";
public static final String PATTERN_ZIP			= "^\\d{5}(-?\\d{4})?$";
public static final String PATTERN_MONTH_YEAR	= "^(0?[1-9]|1[0-2])/\\d{4}$";
public static final String PATTERN_MONTH			= "^0?[1-9]$|^1[0-2]$";
public static final String PATTERN_YEAR			= "^\\d{4}$";
public static final String PATTERN_ALPHA_NUMERIC	= "^[a-zA-Z0-9 ]+$";
public static final String PATTERN_TIME         = "^([0-1][0-9]|[2][0-3]):[0-5][0-9]:[0-5][0-9]$";

/**
 * Constructor - LACSDValidator
 */
private LACSDValidator() {
	super();
}

/**
 * Return Singleton Instance
 * @return LACSDValidator
 */
public static LACSDValidator getInstance() {
	return _INSTANCE;
}

/**
 * Validate digits
 * @param String argString
 * @param boolean argIsRequired
 * @return boolean isValid
 */
public boolean isDigits(String argString, boolean argIsRequired) {
	if (argIsRequired)
		return this.validateCustom(argString, PATTERN_DIGITS);
	else
		return this.isBlankOrNull(argString) || this.validateCustom(argString, PATTERN_DIGITS);
}

/**
 * Validate fax number
 * @param String argFax
 * @param boolean argIsRequired
 * @return boolean isValid
 */
public boolean isFax(String argFax, boolean argIsRequired) {
	if (argIsRequired)
		return this.validateCustom(argFax, PATTERN_FAX);
	else
		return this.isBlankOrNull(argFax) || 
				this.validateCustom(argFax, PATTERN_FAX) ||
				this.validateCustom(argFax, PATTERN_FAX_FORMAT);
}

/**
 * Validate phone number
 * @param String argPhone
 * @param boolean argIsRequired
 * @return boolean isValid
 */
public boolean isPhone(String argPhone,boolean argIsRequired) {
	if (argIsRequired)
		return this.validateCustom(argPhone, PATTERN_PHONE);
	else
		return this.isBlankOrNull(argPhone) || 
				this.validateCustom(argPhone, PATTERN_PHONE) ||
				this.validateCustom(argPhone, PATTERN_PHONE_FORMAT);
}

/**
 * Validate fax number
 * @param String argFax
 * @param boolean argIsRequired
 * @return boolean isValid
 */
public boolean isFax(String argFax, String pattern, String patternFormat, boolean argIsRequired) {
	if (argIsRequired)
		return this.validateCustom(argFax, pattern);
	else
		return this.isBlankOrNull(argFax) || 
				this.validateCustom(argFax, pattern) ||
				this.validateCustom(argFax, patternFormat);
}

/**
 * Validate phone number
 * @param String argPhone
 * @param boolean argIsRequired
 * @return boolean isValid
 */
public boolean isPhone(String argPhone,String pattern, String patternFormat, boolean argIsRequired) {
	if (argIsRequired)
		return this.validateCustom(argPhone, pattern);
	else
		return this.isBlankOrNull(argPhone) || 
				this.validateCustom(argPhone, pattern) ||
				this.validateCustom(argPhone, patternFormat);
}

/**
 * Validate zip code
 * @param String argZip
 * @param boolean argIsRequired
 * @return boolean isValid
 */
public boolean isZip(String argZip, boolean argIsRequired) {
	if (argIsRequired)
		return this.validateCustom(argZip, PATTERN_ZIP);
	else
		return this.isBlankOrNull(argZip) || this.validateCustom(argZip, PATTERN_ZIP);
}

/**
 * Validate time
 * @param String argTime
 * @param boolean isRequired
 * @return boolean isValid
 */
public boolean isTime(String argTime, boolean argIsRequired) {
	if (argIsRequired)
		return !this.isBlankOrNull(argTime) && this.validateCustom(argTime, PATTERN_TIME) ;
	else
		return this.validateCustom(argTime, PATTERN_TIME); 
}


/**
 * Validate blank or null (if value is blank or null, return true)
 * @param String argString
 * @return boolean
 */
public boolean isBlankOrNull(String argString) {
	return GenericValidator.isBlankOrNull(argString);
}

/**
 * Validate date
 * @param String argDate
 * @param boolean isRequired
 * @return boolean isValid
 */
public boolean isDate(String argDate, boolean argIsRequired) {
	if (argIsRequired)
		//return GenericValidator.isDate(argDate, Locale.US) && this.validateCustom(argDate, PATTERN_DATE); 
		return this.isBlankOrNull(argDate) || 
		( GenericValidator.isDate(argDate, Locale.US) && this.validateCustom(argDate, PATTERN_DATE) );
	else
		//return this.isBlankOrNull(argDate) || 
				//( GenericValidator.isDate(argDate, Locale.US) && this.validateCustom(argDate, PATTERN_DATE) ); 
		return GenericValidator.isDate(argDate, Locale.US) && this.validateCustom(argDate, PATTERN_DATE); 
}




/**
 * Validate date allowing mainframe format (ddMMyyyy)
 * @param String argDate
 * @param boolean isRequired
 * @return boolean isValid
 */
public boolean isDate(String argDate, boolean argIsRequired, boolean argIsMainFrameFormatAllowed) {

	// If allow mainframe format, check the format and convert to MM/dd/yyyy
	if (argIsMainFrameFormatAllowed) {
		// mainframe format must not contain "/" and be exactly 8 positions
		if (argDate.indexOf("/") == -1 && argDate.length() == 8) {
			argDate = argDate.substring(0,2) + "/" + argDate.substring(2,4) + "/" + argDate.substring(4);
		}
	} 

	return isDate(argDate, argIsRequired);
}

/**
 * Validate month/year against format MM/yyyy
 * @param String argString
 * @param boolean isRequired
 * @return boolean
 */
public boolean isMonthYear(String argString, boolean argIsRequired) {
	if (argIsRequired)
		return this.validateCustom(argString, PATTERN_MONTH_YEAR);
	else
		return this.isBlankOrNull(argString) || this.validateCustom(argString, PATTERN_MONTH_YEAR);
}

/**
 * Validate month (01-12)
 * @param String argString
 * @param boolean isRequired
 * @return boolean
 */
public boolean isMonth(String argString, boolean argIsRequired) {
	if (argIsRequired)
		return this.validateCustom(argString, PATTERN_MONTH);
	else
		return this.isBlankOrNull(argString) || this.validateCustom(argString, PATTERN_MONTH);
}

/**
 * Validate year yyyy
 * @param String argString
 * @param boolean isRequired
 * @return boolean
 */
public boolean isYear(String argString, boolean argIsRequired) {
	if (argIsRequired)
		return this.validateCustom(argString, PATTERN_YEAR);
	else
		return this.isBlankOrNull(argString) || this.validateCustom(argString, PATTERN_YEAR);
}

/**
 * Validate email
 * @param String argEmail
 * @param boolean isRequired
 * @return boolean isValid
 */
public boolean isEmail(String argEmail, boolean argIsRequired) {
	if (argIsRequired)
		return GenericValidator.isEmail(argEmail);
	else
		return this.isBlankOrNull(argEmail) || GenericValidator.isEmail(argEmail);
}

/**
 * Validate number
 * Formats:  -#####.###  -##,###.###
 * @param String argNumber
 * @param boolean argIsRequired
 * @return boolean isValid
 */	
public boolean isNumber(String argNumber, boolean argIsRequired) {
	if (argIsRequired)
		return this.validateCustom(argNumber, PATTERN_NUMBER);
	else
		return this.isBlankOrNull(argNumber) || this.validateCustom(argNumber, PATTERN_NUMBER);
}
/**
 * Validate number
 * Formats:  #####.###
 * @param String argNumber
 * @param boolean argIsRequired
 * @return boolean isValid
 */	
public boolean isNonNegNumber(String argNumber, boolean argIsRequired) {
	if (argIsRequired)
		return this.validateCustom(argNumber, PATTERN_NON_NEGATIVE_NUMBER);
	else
		return this.isBlankOrNull(argNumber) || this.validateCustom(argNumber, PATTERN_NON_NEGATIVE_NUMBER);
}

/**
 * Validate currency
 * @param String argCurrency
 * @param boolean argIsRequired
 * @return boolean isValid
 */	
public boolean isCurrency(String argCurrency, boolean argIsRequired) {
	if (argIsRequired)
		return this.validateCustom(argCurrency, PATTERN_CURRENCY);
	else
		return this.isBlankOrNull(argCurrency) || this.validateCustom(argCurrency, PATTERN_CURRENCY);
}

/**
 * Validate alphanumeric (a-z, 1-9, and blank spaces only)
 * @param String argString
 * @param boolean argIsRequired
 * @return boolean isValid
 */	
public boolean isAlphaNumeric(String argString, boolean argIsRequired) {
	if (argIsRequired)
		return this.validateCustom(argString, PATTERN_ALPHA_NUMERIC);
	else
		return this.isBlankOrNull(argString) || this.validateCustom(argString, PATTERN_ALPHA_NUMERIC);
}

/**
 * Validate double
 * @param String argDouble
 * @param boolean argIsRequired
 * @return boolean isValid
 */	
public boolean isDouble(String argDouble, boolean argIsRequired) {

	argDouble = (argDouble == null) ? "" : argDouble.trim();
	
	try {
		if (argIsRequired) {
			if (argDouble.length() == 0) {
				return false;
			} else {
				Double.parseDouble(argDouble);
				return true;
			}
		} else {
			if (argDouble.length() != 0) {
				Double.parseDouble(argDouble);
			}
			return true;
		}
	}
	catch (NumberFormatException nfe) {
		return false;
	}
}

/**
 * Validate integer
 * @param String argInteger
 * @param boolean argIsRequired
 * @return boolean isValid
 */	
public boolean isNonNegInteger(String argInteger, boolean argIsRequired) {
	if (argIsRequired)
		return this.validateCustom(argInteger, PATTERN_NON_NEGATIVE_INTEGER_NUMBER);
	else
		return this.isBlankOrNull(argInteger) || this.validateCustom(argInteger, PATTERN_NON_NEGATIVE_INTEGER_NUMBER);
	
}

/**
 * Validate custom:  validate a value against your own defined pattern.
 * Pattern must be written in a valid regular expression.
 * @param String argString
 * @param String argRegularExpressionPattern
 * @return boolean
 */
public boolean validateCustom(String argString, String argRegularExpressionPattern) {
	return GenericValidator.matchRegexp(argString, argRegularExpressionPattern);
}

public static void main(String[] args) {
	
	LACSDValidator v = LACSDValidator.getInstance();
	System.out.println(v.isCurrency(".9", true));
	System.out.println(v.isDate("", true));
	System.out.println(v.isEmail("", true));
	System.out.println(v.isNumber("", true));
	System.out.println(v.isBlankOrNull(""));
	
	System.out.println();
	
	System.out.println("Value null = " + v.isBlankOrNull(null));
	System.out.println("Value ''   = " + v.isBlankOrNull(""));
	System.out.println("Value ' '  = " + v.isBlankOrNull(" "));
	
	String phone = "###-###-####";
	String inValidPhone = "123-456-7890 x 112";
	System.out.println(v.isPhone(phone, true));
	System.out.println(v.isPhone(phone, false));
	System.out.println(v.isPhone(inValidPhone, true));
	System.out.println(v.isPhone(inValidPhone, false));
	
	System.out.println();
	
	System.out.println(v.isMonthYear("01/2003", false));
	System.out.println(v.isMonthYear("01/2003", true));
	System.out.println(v.isMonthYear("00/0000", false));
	System.out.println(v.isMonthYear("00/0000", true));

	System.out.println(v.isMonth("1", true));
	System.out.println(v.isMonth("15", true));
	
	System.out.println();
	
	System.out.println(v.isZip("902101234",true));
	
	System.out.println(v.isDate("1131004", true, true));
	
	System.out.println(v.isDouble("1.222", false));

	System.out.println();

	System.out.println(v.isAlphaNumeric("asldkj dk 98", true));
	System.out.println(v.isAlphaNumeric("", true));
	System.out.println(v.isAlphaNumeric(" ", true));
	System.out.println(v.isAlphaNumeric("", false));
	System.out.println(v.isAlphaNumeric("sadkk 98", false));
	System.out.println(v.isAlphaNumeric("@dkjf-sdk", true));
}
}