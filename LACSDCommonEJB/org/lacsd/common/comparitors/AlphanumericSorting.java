package org.lacsd.common.comparitors;

import java.math.BigInteger;

/******************************************************************************
//* Copyright (c) 2017 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		AlphanumericSorting.java
//* Revision: 		1.0
//* Author:			hho@lacsd.org
//* Created On: 	May 31, 2017
//* Modified By:    
//* Modified On:    
//* 
//* Description:	
/******************************************************************************/

public class AlphanumericSorting {
	private boolean isASC = true;
	private boolean isCaseIgnore = false;
	
	public AlphanumericSorting(boolean isASC, boolean isCaseIgnore) {
		this.isASC = isASC;
		this.isCaseIgnore = isCaseIgnore;
	}
	
	public int compareString(String firstString, String secondString) {
		int result = 0;
		firstString = firstString == null?"":firstString;
		secondString = secondString == null?"":secondString;
 
        int lengthFirstStr = firstString.length();
        int lengthSecondStr = secondString.length();
 
        int index1 = 0;
        int index2 = 0;
 
        while (index1 < lengthFirstStr && index2 < lengthSecondStr) {
            char ch1 = firstString.charAt(index1);
            char ch2 = secondString.charAt(index2);
 
            char[] space1 = new char[lengthFirstStr];
            char[] space2 = new char[lengthSecondStr];
 
            int loc1 = 0;
            int loc2 = 0;
 
            do {
                space1[loc1++] = ch1;
                index1++;
 
                if (index1 < lengthFirstStr) {
                    ch1 = firstString.charAt(index1);
                } else {
                    break;
                }
            } while (Character.isDigit(ch1) == Character.isDigit(space1[0]));
 
            do {
                space2[loc2++] = ch2;
                index2++;
 
                if (index2 < lengthSecondStr) {
                    ch2 = secondString.charAt(index2);
                } else {
                    break;
                }
            } while (Character.isDigit(ch2) == Character.isDigit(space2[0]));
 
            String str1 = new String(space1);
            String str2 = new String(space2);
 
            if (Character.isDigit(space1[0]) && Character.isDigit(space2[0])) {
                BigInteger firstNumberToCompare = new BigInteger(str1.trim());
                BigInteger secondNumberToCompare = new BigInteger(str2.trim());
                if (this.isASC) {
                	result = firstNumberToCompare.compareTo(secondNumberToCompare);
                }else {
                	result = secondNumberToCompare.compareTo(firstNumberToCompare);
                }
            } else {
            	if (this.isASC) {
            		if (isCaseIgnore) {
            			result = str1.compareToIgnoreCase(str2);
            		}else {
            			result = str1.compareTo(str2);
            		}
            	}else {
            		if (isCaseIgnore) {
            			result = str2.compareToIgnoreCase(str1);
            		}else {
            			result = str2.compareTo(str1);
            		}
            	}
            }
 
            if (result != 0) {
                return result;
            }
        }
        if (this.isASC) {
        	return lengthFirstStr - lengthSecondStr;
        }else {
        	return lengthSecondStr - lengthFirstStr;
        }
  	}
}
