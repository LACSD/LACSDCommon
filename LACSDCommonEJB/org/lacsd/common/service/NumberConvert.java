package org.lacsd.common.service;

/******************************************************************************
//* Copyright (c) 2003 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		NumberConvert.java
//* Revision: 		1.3
//* Author:			asrirochanakul@lacsd.org
//* Created On: 	01-06-2004
//* Modified By:	
//* Modified On:	
//*					
//* Description:	Formatting numbers for DB and Web
/******************************************************************************/

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.lacsd.common.exceptions.LACSDException;

public class NumberConvert {
	
private static NumberConvert _INSTANCE = new NumberConvert();

public static final int SIGN_LOCATION_LEFT	= 1;
public static final int SIGN_LOCATION_RIGHT	= 2;
public static final int SIGN_LOCATION_BOTH	= 3;

/**
 * Constructor - NumberConvert
 */
public NumberConvert() {
	super();
}

/**
 * Return Singleton Instance
 * @return NumberCovnert
 */
public static NumberConvert getInstance() {
    return _INSTANCE;
}

/**
 * Strip all characters off excepted for digits.
 * i.e. strip off '-' or 'x' from phone/fax number to save into database
 * 
 * @param String argString
 * @return String
 */
public String getDigitsOnly(String argString) {
	
	if ( argString == null ) return null;
	
	char[] cArr = argString.toCharArray();
    
    StringBuffer sb = new StringBuffer();
    
    for( int i=0; i<cArr.length; i++) {
        if ( Character.isDigit(cArr[i]) ) sb.append(cArr[i]);
    }
	return sb.toString();
}

/**
 * Get a number from a given currency string.
 * i.e.   $#,###.##  --> ####.##
 * i.e.  ($#,###.##) --> ####.##
 * 
 * @param String argCurrency
 * @return String
 */
public String getCurrencyAsNumber(String argCurrency) throws LACSDException {

    try {
        if ( argCurrency == null || argCurrency.trim().length() == 0 ) return null;

        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
        String parsedNumber = nf.parse(argCurrency).toString();
        return new BigDecimal(parsedNumber).toString();
    } catch (ParseException e) {
        throw new LACSDException("Convert currency to number string failed.");
    }
}

/**
 * Get a currency number from a given number string.
 * i.e.  ####.## -->  $#,###.##
 * i.e. -####.## --> ($#,###.##)
 * 
 * @param String argNumber
 * @return String
 */
public String getNumberAsCurrency(String argNumber) throws LACSDException {

    try {
        if ( argNumber == null || argNumber.trim().length() == 0 ) return null;
        
        double amount = Double.parseDouble(argNumber);
        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
        return nf.format(amount);
    } catch (NumberFormatException e) {
        throw new LACSDException("Convert number string to currency failed.");
    }
}

/**
 * Get a custom formatted number from a given number string.
 * i.e.  ####.## --> [prefix]#,###.##[suffix]
 * i.e.  ####.## --> $#,###.##CR
 * i.e.  ####.## --> ($#,###.##)
 * i.e.  ####.## --> -$#,###.##
 * i.e.  ####.## --> #,###.##CR
 * 
 * @param String argNumber
 * @param String argPrefix
 * @param String argSignType
 * @return String
 */
public String getNumberAsCustomFormat(String argNumber,String argPrefix,
                 String argNegativeSignSymbol, int argNegativeSignLocation) throws LACSDException {
    try {
    	
        if ( argNumber == null || argNumber.trim().length() == 0 ) return null;

		String num = this.getNumberAsCurrency(argNumber);
		num = num.replace('$',' ').replace('(',' ').replace(')',' ').trim();
		
		num = argPrefix+num;
		
		// check to see if number is negative
		if ( argNumber.indexOf("-") > -1) {
			
			if ( argNegativeSignLocation == SIGN_LOCATION_LEFT) {
				num = argNegativeSignSymbol+num;
			} if ( argNegativeSignLocation == SIGN_LOCATION_RIGHT) {
				num = num + argNegativeSignSymbol;
			} if ( argNegativeSignLocation == SIGN_LOCATION_BOTH) {
				num = argNegativeSignSymbol + num + argNegativeSignSymbol;
			}
		}
		
		return num;
		
    } catch (NumberFormatException e) {
        throw new LACSDException("Convert number string to currency failed.");
    }
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
 * @param String argSignType
 * @return String
 */
public String getCustomFormatAsNumber(String argNumber, String argSignType) throws LACSDException {

	char[] cArr = argNumber.toCharArray();
    StringBuffer sb = new StringBuffer();
	
	sb.append(this.getSignOfCustomNumber(argNumber,argSignType));
    
    for( int i=0; i<cArr.length; i++) {
        if ( Character.isDigit(cArr[i]) || cArr[i] == '.' ) {
        	sb.append(cArr[i]);
        }
    }
	return new BigDecimal(sb.toString()).toString();
}

/**
 * Determine if a number is negative or not given
 * a certain accounting negative sign convention.
 * i.e.  (##.##), -#,###.00, and #.##CR are negative numbers
 * 
 * @param String argNumber
 * @param String argSignType
 * @return String
 */
public String getSignOfCustomNumber(String argNumber, String argSignType) throws LACSDException {

	if ( (argNumber.indexOf(argSignType) > -1) ) {
		return "-";
	}
	return "";
}

/**
 * Get a Round off number string from
 * a number string and a number of decimal places.
 * The number are round off based on BigDecimal.ROUND_HALF_EVEN
 * i.e.  ######.#### with 2 decimal places--> ######.##
 * 
 * Note:  return null if String argNumber is null
 * 
 * @param String argNumber
 * @param int argDecimalPlaces
 * @return String
 */
public String getRoundOffNumber(String argNumber, int argDecimalPlaces) {
	
	if ( argNumber == null ) return null;
    
    String s = new BigDecimal( argNumber ).setScale(argDecimalPlaces,BigDecimal.ROUND_HALF_EVEN).toString();
    return s;    
}

/**
 * Get a Round off comma separated number string from
 * a number string and a number of decimal places.
 * i.e.  ######.#### with 2 decimal places--> ###,###.##
 * 
 * @param String argNumber
 * @param int argDecimalPlaces
 * @return String
 */
public String getRoundOffCommaNumber(String argNumber, int argDecimalPlaces) {
	
	if (argNumber == null || argNumber.trim().length() == 0) return null;
    
    StringBuffer pattern = new StringBuffer("#,##0");
    if ( argDecimalPlaces > 0 ) {
    	pattern.append(".");

    	String afterDot = "0";
    	if (argNumber.indexOf(".") > -1)
	    	afterDot = argNumber.substring(argNumber.indexOf(".")+1, argNumber.length());

		BigInteger afterDot_int = new BigInteger(afterDot);
		
		if (afterDot_int.compareTo(new BigInteger("0")) == 0)
			for ( int i = 0; i < argDecimalPlaces; i++) pattern.append("0");
		else 
			for ( int i = 0; i < argDecimalPlaces; i++) pattern.append("#");
    }

    DecimalFormat df = new DecimalFormat(pattern.toString());
    String sreturn = df.format(Double.parseDouble(argNumber));
    
    int leng = sreturn.substring( sreturn.lastIndexOf(".")+1).length();
    while ( leng < argDecimalPlaces )  {
    	sreturn = sreturn + "0";
    	leng++;
    }
    return sreturn;
}

/**
 * Convert a set of digits to a specified format.
 * i.e.  ###-###-#### x#### where '#' are the digits.
 * It will populate from left to right of the specified format
 *       ( 123456789 = 123-456-789 )
 * If input contains "non-numerics", it will be ignored.
 *       ( 333AA4a56xx789x = 333-456-789 )
 * 
 * @param String argDigits
 * @param String argFormat
 * @return String
 */
public String convertDigitsToCustomFormat(String argDigits, String argFormat) {
    
    if ( argDigits == null || argDigits.trim().length() == 0 ) return null;

	String digits = this.getDigitsOnly(argDigits);	

	StringBuffer formattedDigits = new StringBuffer();

	char[] digitArr = digits.toCharArray();
	char[] formatArr = argFormat.toCharArray();

    int digitIndex = 0;

	for (int formatIndex=0; formatIndex<formatArr.length; formatIndex++) {

		if (digitIndex < digitArr.length) {
			if (formatArr[formatIndex] == '#') {
				formattedDigits.append(digitArr[digitIndex]);
				digitIndex++;
			}
			else {
				formattedDigits.append(formatArr[formatIndex]);
			}
		}
	}

	return formattedDigits.toString();
}

/**
 * Appending leading zeroes to number
 * i.e.  123 with 6 digits -> 000123
 * i.e.  1,234 with 8 digits -> 00001234 
 * i.e.  abc1,234.00 with 8 digits -> 00123400
 * i.e.  1234567890 with 8 digits -> 12345678 
 * @param String argNumber
 * @param int argNumberOfDigits
 * @return String
 */
public String appendLeadingZeros(String argNumber, int argNumberOfDigits) {

    argNumber = getDigitsOnly(argNumber);

    while (argNumber.length() < argNumberOfDigits) {
    	argNumber = "0" + argNumber;
    }

    return argNumber.substring(0, argNumberOfDigits);
}

/**
 * Removing trailing zeros of decimal digits
 * i.e.  ##.####000 --> ##.#####
 * 
 * @param String argNumber
 * @return String
 */
public BigDecimal removeTrailingZeros(BigDecimal argNumber) {

    if ( argNumber == null ) return argNumber;
    try {
        while ( argNumber.scale() > 1 ) {
            argNumber = argNumber.setScale(argNumber.scale()-1);    
        }
    }
    catch ( ArithmeticException ignored ) { /* exite the loop */ }
    return argNumber;
}

/**
 * Removing leading zeroes
 * i.e. 000##.#### --> ##.####
 * @param argNumber
 * @return String
 */
public String removeLeadingZeroes(String argNumber) {

    BigDecimal number = new BigDecimal(argNumber); 
    return number+"";
}

public static void main(String[] args) throws LACSDException, Exception {
	
	NumberConvert nc = NumberConvert.getInstance();
	String unformatted = "562ab   c699def7 411ghi 10  7---8vvv";
	String number = "-3424324233328.2900029823";
	//String blank = "";
	
	System.out.println("unformatted: " + unformatted);
	System.out.println("unformatted after getDigitsOnly(): " + nc.getDigitsOnly(unformatted));
	System.out.println();
		
	System.out.println("Number: " + number);
	System.out.println("Number after getRoundOffNumber(number, 3): " + nc.getRoundOffNumber(number, 3));
    
    String scurr = nc.getNumberAsCurrency(number);
    
	System.out.println("Number after getNumberAsCurrency(): " + scurr);
    System.out.println("Number reverse back getCurrencyAsNumber(): " + nc.getCurrencyAsNumber(scurr));
    
	System.out.println();
		
	System.out.println("Unformatted number = -12345.67");
	System.out.println("Blank after getNumberAsCurrency(): " + nc.getNumberAsCurrency("-12345.67"));

	System.out.println("Unformatted number");
	System.out.println("Blank after getCurrencyAsNumber(): " + nc.getCurrencyAsNumber("($12345.67)"));
	
	Date d = new Date();
	Calendar c = Calendar.getInstance();
	c.setTime(d);
	System.out.println("getFirstDayOfWeek " + c.getFirstDayOfWeek());
	System.out.println(c.get(Calendar.DAY_OF_WEEK));
	System.out.println(c.get(Calendar.DAY_OF_WEEK_IN_MONTH));

	System.out.println(nc.getRoundOffCommaNumber("3043.8400000000006",2));
//	System.out.println(nc.getRoundOffCommaNumber("1023402342.0125000",3));
//	System.out.println(nc.getRoundOffCommaNumber("1023402342.0124900",3));
//	System.out.println(nc.getRoundOffCommaNumber("1023402342.0120000",5));
//	System.out.println(nc.getRoundOffCommaNumber("0001023402342.01300",4));
	
	System.out.println(nc.getRoundOffNumber("402342",2));
	
	System.out.println(nc.appendLeadingZeros("123453367.890", 8));
	
	System.out.println(nc.removeLeadingZeroes("00001.03000"));
}
}