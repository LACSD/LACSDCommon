package org.lacsd.common.comparitors;

/******************************************************************************
//* Copyright (c) 2004 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		DateSortAsc.java
//* Revision: 		1.0
//* Author:			M Feinberg
//* Created On: 	02-19-2004
//* Modified by:	
//* Modified On:	
//*					
//* Description:	Implementation of java.util.Comparitor to sort java Date()
//*					objects in ascending order
/******************************************************************************/

import java.util.Comparator;

public class DateSortAsc implements Comparator<Object> {

/**
 * Overriding Method to Compare Dates in ascending Order
 * @param Object o1
 * @param Object o2
 * @return int
*/
public int compare(Object o1, Object o2) {

	if ((o1 instanceof java.util.Date) && (o2 instanceof java.util.Date)) {
		
		java.util.Date date1 = (java.util.Date)o1;
		java.util.Date date2 = (java.util.Date)o2;
		
		long time1 = date1.getTime();
		long time2 = date2.getTime();
		
		/*
		 * Greater > Than = Farthest in Future First
		 * Less < Than =  Farthest in Past First
		*/
		if (time1 > time2) {
			return -1;
		}
		else {
			return 1;	
		}
	}
	else {
		throw new ClassCastException("Comparator only takes java.util.Date()");
	}
}
}
