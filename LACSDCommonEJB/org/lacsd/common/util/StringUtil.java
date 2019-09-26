package org.lacsd.common.util;

/******************************************************************************
//* Copyright (c) 2016 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		StringUtil.java
//* Revision: 		1.0
//* Author:			asrirochanakul
//* Created On: 	05-18-2016
//* Modified by:	
//* Modified On:	
//*					
//* Description:	Functions to handle String that are not readily available
//* 				in Java String
//*
/******************************************************************************/

public class StringUtil {

private static StringUtil _INSTANCE = new StringUtil();

/**
 * StringUtil constructor.
 */
public StringUtil() {
}

/**
 * Return Singleton Instance
 * @return NumberCovnertWeb
 */
public static StringUtil getInstance() {
    return _INSTANCE;
}

/**
 * Convert ALL CAPS WORDS to mixed case (capitalize the first letter of each word)
 * @param s
 * @return
 */
public static String allCapsToDisplayCase(String s) {
	
	boolean isAllCaps = true;
	for (Character c : s.toCharArray()) {
		if (Character.isLowerCase(c)) {
			isAllCaps = false;
			break;
		}
	}
	
	if (isAllCaps) {
		return toDisplayCase(s);
	} else {
		return s;
	}
}

/**
 * Convert to mixed case (capitalize the first letter of each word)
 * Ex.	"a string" 			-> "A String"
 * 		"maRTin o'maLLEY" 	-> "Martin O'Malley"
 * 		"john wilkes-booth"	-> "John Wilkes-Booth"
 *		"YET ANOTHER STRING"-> "Yet Another String"
 * @param s
 * @return
 */
public static String toDisplayCase(String s) {

	if (s == null) {
		return "";
	}

    final String ACTIONABLE_DELIMITERS = " '-/,"; // these cause the character following
                                                 // to be capitalized

    StringBuilder sb = new StringBuilder();
    boolean capNext = true;

    for (char c : s.toCharArray()) {
        c = (capNext)
                ? Character.toUpperCase(c)
                : Character.toLowerCase(c);
        sb.append(c);
        capNext = (ACTIONABLE_DELIMITERS.indexOf((int) c) >= 0); // explicit cast not needed
    }
    return sb.toString();
}
public static void main(String[] arg) {

	System.out.println(StringUtil.toDisplayCase(""));
	System.out.println(StringUtil.toDisplayCase("a string"));
	System.out.println(StringUtil.toDisplayCase("maRTin o'maLLEY"));
	System.out.println(StringUtil.toDisplayCase("john wilkes-booth"));
	System.out.println(StringUtil.toDisplayCase("YET ANOTHER STRING"));
	System.out.println(StringUtil.toDisplayCase("SMITH,JOHN"));
	System.out.println(StringUtil.toDisplayCase("1955 Workman Mill Road, Whittier, CA  90601"));
	System.out.println(StringUtil.toDisplayCase("1955 WORKMAN MILL ROAD, WHITTIER, CA  90601"));

	// convert only all CAPS
	System.out.println(StringUtil.allCapsToDisplayCase(""));
	System.out.println(StringUtil.allCapsToDisplayCase("a string"));
	System.out.println(StringUtil.allCapsToDisplayCase("maRTin o'maLLEY"));
	System.out.println(StringUtil.allCapsToDisplayCase("john wilkes-booth"));
	System.out.println(StringUtil.allCapsToDisplayCase("YET ANOTHER STRING"));
	System.out.println(StringUtil.allCapsToDisplayCase("SMITH,JOHN"));
	System.out.println(StringUtil.allCapsToDisplayCase("1955 Workman Mill Road, Whittier, CA  90601"));
	System.out.println(StringUtil.allCapsToDisplayCase("1955 WORKMAN MILL ROAD, WHITTIER, CA  90601"));
}

}