package org.lacsd.common.service;

/******************************************************************************
//* Copyright (c) 2003 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		TimeMarker.java
//* Revision: 		1.0
//* Author:			M Feinberg
//* Created On: 	07-03-2003
//* Modified by:	
//* Modified On:	
//*					
//* Description:	Utility class used for timing internal method calls
/******************************************************************************/

import java.util.Date;
import java.util.HashMap;

public class TimeMarker {

private HashMap<Integer, Date> timemap;


/**
 * Constructor - default
*/
public TimeMarker() {
	super();
	timemap = new HashMap<Integer, Date>();
}


/**
 * Internal method for indexing Timestamps
 * ----------------------------------------------------
 * @param int index
 * @return void
*/
public void setTimeMark(int index) {
	
	java.util.Date now = new java.util.Date();
	Integer key = new Integer(index);
		
	timemap.put(key,now);
}

/**
 * Internal method for comparing timestamps
 * USAGE: (index1 - index2)
 * ----------------------------------------------------
 * @param int index1
 * @param int index2
 * @return int
*/
public int compareTimeSec(int index1, int index2) {

	Integer key1 = new Integer(index1);
	Integer key2 = new Integer(index2);

	if ((timemap.containsKey(key1)) && (timemap.containsKey(key2))) {
		
		java.util.Date time1 = (java.util.Date)timemap.get(key1);
		java.util.Date time2 = (java.util.Date)timemap.get(key2);
	
		long mili1 = Math.abs(time1.getTime());
		long mili2 = Math.abs(time2.getTime());
		long miliresult = (mili1 - mili2);
		
		int difference = (int)(miliresult / 1000);
		
		return Math.abs(difference);
	}
	else {
		return 0;
	}
}

/**
 * Internal method for comparing timestamps
 * USAGE: (index1 - index2)
 * ----------------------------------------------------
 * @param int index1
 * @param int index2
 * @return int
*/
public long compareTimeMilli(int index1, int index2) {

	Integer key1 = new Integer(index1);
	Integer key2 = new Integer(index2);

	if ((timemap.containsKey(key1)) && (timemap.containsKey(key2))) {
		
		java.util.Date time1 = (java.util.Date)timemap.get(key1);
		java.util.Date time2 = (java.util.Date)timemap.get(key2);
	
		long mili1 = Math.abs(time1.getTime());
		long mili2 = Math.abs(time2.getTime());
		long miliresult = (mili1 - mili2);
		
		return Math.abs(miliresult);
	}
	else {
		return 0;
	}
}

/**
 * Internal method for comparing timestamps that returns
 * a well-formatted String in DAYS - HOURS - MINS - SECS
 * 
 * USAGE: (index1 - index2)
 * ----------------------------------------------------
 * @param int index1
 * @param int index2
 * @return int
*/
public String compareTimeFormattedString(int index1, int index2) {

	//String returnString = null;

	int timeSec = this.compareTimeSec(index1,index2);
	int timeMin = 0;
	int xtraSec = 0;
	int timeHrs = 0;
	int xtraMin = 0;
	int timeDay = 0;
	int xtraHrs = 0;

	boolean minutes = false;
	boolean hours   = false;
	boolean days    = false;
	
	if (timeSec > 60) {
		timeMin = timeSec/60;
		xtraSec = timeSec%60;
		minutes = true;
	}
	if (timeMin > 60) {
		timeHrs = timeMin/60;
		xtraMin = timeMin%60;
		hours = true;
	}	
	if (timeHrs > 24) {
		timeDay = timeHrs/24;
		xtraHrs = timeHrs%24;
		days = true;
	}
	
	if (days) {
		return (timeDay + " day(s), " + xtraHrs + " hour(s), " + xtraMin + " minutes(s), " + xtraSec + " second(s)");
	}
	else if (hours) {
		return (timeHrs + " hour(s), " + xtraMin + " minutes(s), " + xtraSec + " second(s)");
	}
	else if (minutes) {
		return (timeMin + " minutes(s), " + xtraSec + " second(s)");
	}
	else {
		return (timeSec + "second(s)");
	}
}
}
