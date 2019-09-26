package org.lacsd.common.service;

/******************************************************************************
//* Copyright (c) 2003 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		DateConvert.java
//* Revision: 		1.0
//* Author:			M Feinberg
//* Created On: 	07-08-2003
//* Modified by:	
//* Modified On:	
//*					
//* Description:	Simple data converter for WEB-TO-MF /  MF-TO-WEB
/******************************************************************************/

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.lacsd.common.exceptions.LACSDException;

public class DateConvert {

	public final static String FORMAT_STATIC_MONTH 			= "MM";
	public final static String FORMAT_STATIC_YEAR 			= "yyyy";
    public final static String FORMAT_DATE_WEB            	= "MM/dd/yyyy";
    public final static String FORMAT_DATE_PRINT       		= "MMM dd, yyyy";
    public final static String FORMAT_DATE_FULL_PRINT  		= "MMMM dd, yyyy";
    
    public final static String FORMAT_DATE_SQLSP     		= "MM/dd/yyyy";
    
  	public final static String FORMAT_DATE_SQLSPv81     	= "yyyy-MM-dd";
        
	public final static String FORMAT_DATE_MAINFRAME   		= "yyyyMMdd";
    
    public final static String FORMAT_DATE_JAVASP    		= "yyyy-MM-dd";
    public final static String FORMAT_DATE_TIME_WEB    		= "MM/dd/yyyy HH:mm:ss";
    public final static String FORMAT_DATE_TIME_FRACTION_WEB    		= "MM/dd/yyyy HH:mm:ss.SSS";
    public final static String FORMAT_DATE_TIME_PRINT  		= "MMM dd, yyyy HH:mm:ss";
    
    public final static String FORMAT_DATE_TIME_EDMS 		= "yyyy-MM-dd HH:mm:ss";
    public final static String FORMAT_DATE_TIME_SQL_RS		= "yyyy-MM-dd HH:mm:ss.SSS";
    public final static String FORMAT_DATE_TIME2_SQL_RS		= "yyyy-MM-dd HH:mm:ss.SSSSSS";
    public final static String FORMAT_DATE_TIME2_SEVEN_SQL_RS = "yyyy-MM-dd HH:mm:ss.SSSSSSS";
	public final static String FORMAT_DATE_TIME_SQL_SP	    = "yyyy-MM-dd-HH.mm.ss.SSSSSS";
    public final static String FORMAT_DATE_TIME_H_MM_AM     = "MM/dd/yyyy h:mm a";
    public final static String FORMAT_DATE_TIME_H_MM        = "MM/dd/yyyy HH:mm";
    public final static String FORMAT_DATE_TIME_REPORT      = "yyyy-MM-dd-HH-mm-ss-SSS";
    public final static String FORMAT_DATE_TIME_FRACTION      = "yyyy-MM-dd-HH:mm:ss.SSS";
    public final static String FORMAT_TIME_HH_MM_AM			= "hh:mm a";
    public final static String FORMAT_TIME_HH_MM__SS_AM		= "hh:mm:ss a";
    public final static String FORMAT_TIME_HH_MM_SS_SSS		= "HH:mm:ss.SSS";
    public final static String FORMAT_TIME_MILITARY_TIME	= "HH:mm";
    public final static String FORMAT_TIME_MILITARY_HH_MM_SS  = "HH:mm:ss";
    
    private static DateConvert _INSTANCE = new DateConvert();
    
	public final static String DAY_SUNDAY 				    = "Sunday";
	public final static String DAY_MONDAY 				    = "Monday";
	public final static String DAY_TUESDAY 					= "Tuesday";
	public final static String DAY_WEDNESDAY 				= "Wednesday";
	public final static String DAY_THURSDAY 				= "Thursday";
	public final static String DAY_FRIDAY	 				= "Friday";
	public final static String DAY_SATURDAY 				= "Saturday";
	

	/**
	 * Private Constructor - Singleton Pattern
 	 */
    private DateConvert() {
        super();
    }

	/**
	 * Return Singleton Instance
	 * @return DateCovnert
	 */
    public static DateConvert getInstance() {
        return _INSTANCE;
    }

    /**
     * Convert String to Date Time Strings 
     * @param String inDateTime, String outFormat
     * @return String
     */
    public String convertDateTimeFormat(String inDateTime, String outFormat) {

        try {
            Timestamp time = Timestamp.valueOf(inDateTime);
            java.util.Date uDate = new java.util.Date(time.getTime());
            return convertDateTimeFormat(uDate,outFormat);
        } catch (Exception e) {
            return null;
        }
    }

	/**
	 * Convert java.util.Date to Date Time Strings 
	 * @param java.util.Date inDate, String outFormat
	 * @return String
	 * @throws LACSDException
	 */
    public String convertDateTimeFormat(java.util.Date inDateTime, String outFormat) throws LACSDException {

        try {
            SimpleDateFormat out = new SimpleDateFormat (outFormat); 

            return out.format(inDateTime);
        } catch (Exception e) {
            throw new LACSDException("Date Conversion Failed", e);
        }
    }

	/**
	 * Convert Date Time Strings 
	 * @param String inDateTime, String inFormat, String outFormat
	 * @return String
	 * @throws LACSDException
	 */
    public String convertDateTimeFormat(String inDateTime, String inFormat, String outFormat) throws LACSDException {

        try {
            SimpleDateFormat in = new SimpleDateFormat(inFormat); 
            SimpleDateFormat out = new SimpleDateFormat (outFormat); 

            java.util.Date date = in.parse(inDateTime);
            return out.format(date);
        } catch (Exception e) {
            throw new LACSDException("Date Conversion Failed", e);
        }
    }

	/**
	 * Convert Date Strings (yyyy-MM-dd-HH.mm.ss.SSSSSS to MM/dd/yyyy HH:mm:ss) 
	 * @param String mfFormatted
	 * @return String
	 * @throws LACSDException
	 */
    public String convertDateTimeDBSPtoWeb(String dbFormatted) throws LACSDException {

		return convertDateTimeFormat( dbFormatted, FORMAT_DATE_TIME_SQL_SP, FORMAT_DATE_TIME_WEB );
    }

	/**
	 * Convert DateTime Strings (yyyy-MM-dd HH:mm:ss.SSS to MM/dd/yyyy HH:mm:ss)
	 * @param String mfFormatted
	 * @return String
	 * @throws LACSDException
	*/
    public String convertDateTimeDBtoWeb(String dbFormatted) throws LACSDException {
    	
    	if ( dbFormatted.length() < 23 ) dbFormatted = dbFormatted + "000000";  // padding for zero nano secs

		return convertDateTimeFormat( dbFormatted.substring(0,23), FORMAT_DATE_TIME_SQL_RS, FORMAT_DATE_TIME_WEB );
    }

	/**
	 * Convert DateTime Strings (yyyy-MM-dd HH:mm:ss.SSS to MMM dd, yyyy HH:mm:ss)
	 * @param String mfFormatted
	 * @return String
	 * @throws LACSDException
	 */
    public String convertDateTimeDBtoWebPrint(String dbFormatted) throws LACSDException {
    	
    	if ( dbFormatted.length() < 23 ) dbFormatted = dbFormatted + "000000";  // padding for zero nano secs

		return convertDateTimeFormat( dbFormatted.substring(0,23), FORMAT_DATE_TIME_SQL_RS, FORMAT_DATE_TIME_PRINT );
    }

	/**
	 * Convert Date Strings (yyyy-MM-dd to MMMM dd, yyyy)
	 * i.e. "2007-04-11" to "April 11, 2007"
	 * @param String mfFormatted
	 * @return String
	 * @throws LACSDException
	 */
    public String convertSQLtoFullWebPrint(String dbFormatted) throws LACSDException {
		return convertDateTimeFormat( dbFormatted, FORMAT_DATE_JAVASP, FORMAT_DATE_FULL_PRINT );
    }

	/**
	 * Convert DateTime Strings (yyyy-MM-dd HH:mm:ss.SSS to MM/dd/yyyy)
	 * @param String mfFormatted
	 * @return String
	 * @throws LACSDException
	*/
    public String convertDateTimeDBtoDateWeb(String dbFormatted) throws LACSDException {
    	
    	if ( dbFormatted.length() < 23 ) dbFormatted = dbFormatted + "000000";  // padding for zero nano secs

		return convertDateTimeFormat( dbFormatted.substring(0,23), FORMAT_DATE_TIME_SQL_RS, FORMAT_DATE_WEB );
    }

	/**
	 * Convert Date Strings (MM/dd/yyyy or MM/dd/yyyy HH:mm:ss to yyyy-MM-dd-HH.mm.ss.SSSSSS)
	 * @param String mfFormatted
	 * @return String
	 * @throws LACSDException
	*/
    public String convertDateTimeWebtoDB(String webFormatted) throws LACSDException {

		String returnFormatted = "";
		if ( webFormatted.length() <= FORMAT_DATE_WEB.length() )
		{
			returnFormatted = convertDateTimeFormat( webFormatted, FORMAT_DATE_WEB, FORMAT_DATE_TIME_SQL_SP );
		}
		else if ( webFormatted.length() >= FORMAT_DATE_TIME_WEB.length() )
		{
			returnFormatted = convertDateTimeFormat( webFormatted, FORMAT_DATE_TIME_WEB, FORMAT_DATE_TIME_SQL_SP );
		}
		return returnFormatted;
    }
    
    /**
	 * Convert Date Strings (MM/dd/yyyy or MM/dd/yyyy HH:mm:ss to yyyy-MM-dd-HH.mm.ss.SSSSSS)
	 * @param String mfFormatted
	 * @return String
	 * @throws LACSDException
	*/
    public String convertDateTimeWebtoSQL(String webFormatted) throws LACSDException {

		String returnFormatted = "";
		if ( webFormatted.length() <= FORMAT_DATE_WEB.length() )
		{
			returnFormatted = convertDateTimeFormat( webFormatted, FORMAT_DATE_WEB, FORMAT_DATE_TIME2_SQL_RS );
		}
		else if ( webFormatted.length() >= FORMAT_DATE_TIME_WEB.length() )
		{
			returnFormatted = convertDateTimeFormat( webFormatted, FORMAT_DATE_TIME_WEB, FORMAT_DATE_TIME2_SQL_RS );
		}
		return returnFormatted;
    }
    
    /**
	 * Convert Date Strings (MM/dd/yyyy or MM/dd/yyyy HH:mm:ss to yyyy-MM-dd-HH.mm.ss.SSSSSSS)
	 * Conversion compatible with datetime2(7) datatype in SQL 2008
	 * @param String mfFormatted
	 * @return String
	 * @throws LACSDException
	*/
    public String convertDateTimeWebtoSQL1(String webFormatted) throws LACSDException {

		String returnFormatted = "";
		if ( webFormatted.length() <= FORMAT_DATE_WEB.length() )
		{
			returnFormatted = convertDateTimeFormat( webFormatted, FORMAT_DATE_WEB, FORMAT_DATE_TIME2_SEVEN_SQL_RS );
		}
		else if ( webFormatted.length() >= FORMAT_DATE_TIME_WEB.length() )
		{
			returnFormatted = convertDateTimeFormat( webFormatted, FORMAT_DATE_TIME_WEB, FORMAT_DATE_TIME2_SEVEN_SQL_RS );
		}
		return returnFormatted;
    }

	/**
	 * Convert Date Strings (MM/dd/yyyy or MM/dd/yyyy HH:mm:ss to yyyy-MM-dd HH:mm:ss.SSS)
	 * @param String mfFormatted
	 * @return String
	 * @throws LACSDException
	*/
    public String convertDateTimeWebtoSQLDB(String webFormatted) throws LACSDException {

		String returnFormatted = "";
		if ( webFormatted.length() <= FORMAT_DATE_WEB.length() )
		{
			returnFormatted = convertDateTimeFormat( webFormatted, FORMAT_DATE_WEB, FORMAT_DATE_TIME_SQL_RS );
		}
		else if ( webFormatted.length() >= FORMAT_DATE_TIME_WEB.length() )
		{
			returnFormatted = convertDateTimeFormat( webFormatted, FORMAT_DATE_TIME_WEB, FORMAT_DATE_TIME_SQL_RS );
		}
		return returnFormatted;
    }

    
	/**
	 * Convert Date Strings (MM/dd/yyyy HH:mm:ss to MM/dd/yyyy)
	 * @param String mfFormatted
	 * @return String
	 * @throws LACSDException
	*/
    public String convertDateTimeEdmstoDateWeb(String edmsFormatted) throws LACSDException {

		return convertDateTimeFormat(edmsFormatted, FORMAT_DATE_TIME_EDMS, FORMAT_DATE_WEB);

    }

	/**
	 * Convert Date/Time Strings (yyyy-MM-dd HH:mm:ss)
	 * @param String mfFormatted
	 * @return String
	 * @throws LACSDException
	*/
    public String convertDateTimeSQLToPrint(String sqlFormatted) throws LACSDException {
		return convertDateTimeFormat(sqlFormatted, FORMAT_DATE_TIME_SQL_RS, FORMAT_DATE_TIME_PRINT);
    }

	/**
	 * Convert Date/Time Strings SQL to Date (yyyy-MM-dd HH:mm:ss.SSS to MM/dd/yyyy)
	 * @param String mfFormatted
	 * @return String
	 * @throws LACSDException
	*/
    public String convertDateTimeSQLToDate(String sqlFormatted) throws LACSDException {
		return convertDateTimeFormat(sqlFormatted, FORMAT_DATE_TIME_SQL_RS, FORMAT_DATE_WEB);
    }

	/**
	 * Convert Date/Time Strings SQL to Date (yyyy-MM-dd HH:mm:ss.SSS to yyyy-MM-dd)
	 * @param String mfFormatted
	 * @return String
	 * @throws LACSDException
	*/
    public String convertDateTimeSQLToDBDate(String sqlFormatted) throws LACSDException {
		return convertDateTimeFormat(sqlFormatted, FORMAT_DATE_TIME_SQL_RS, FORMAT_DATE_JAVASP);
    }

	/**
	 * Convert Date Strings (yyyy-MM-dd to MM/dd/yyyy)
	 * @param String mfFormatted
	 * @return String
	 * @throws LACSDException
	*/
    public String convertDBtoWebforJavaSP(String dbFormatted) throws LACSDException {

		return convertDateTimeFormat( dbFormatted, FORMAT_DATE_JAVASP, FORMAT_DATE_WEB );
    }

	/**
	 * Convert Date Strings (DB to WEB)
	 * @param String mfFormatted
	 * @return String
	 * @throws LACSDException
	*/
    public String convertDBtoWebforSQLSP(String dbFormatted) throws LACSDException {

		return convertDateTimeFormat( dbFormatted, FORMAT_DATE_SQLSP, FORMAT_DATE_WEB );
    }
    
	/**
	 * Convert Date Strings (DB to WEB)
	 * @param String mfFormatted
	 * @return String
	 * @throws LACSDException
	*/
    public String convertDBtoWebforSQLSPv81(String dbFormatted) throws LACSDException {

		return convertDateTimeFormat( dbFormatted, FORMAT_DATE_SQLSPv81, FORMAT_DATE_WEB );
    }

	/**
	 * Convert Date Strings (Mainframe to WEB)
	 * @param String 
	 * @return String
	 * @throws LACSDException
  	 */
	public String convertMainframeToWeb(String mainFrameFormatted) throws LACSDException {
		return convertDateTimeFormat( mainFrameFormatted, FORMAT_DATE_MAINFRAME, FORMAT_DATE_WEB );
	}

	/**
	 * Convert Date Strings (WEB to Mainframe)
	 * @param String
	 * @return String
	 * @throws LACSDException
	 */
	public String convertWebToMainframe(String webFormatted) throws LACSDException {
		return convertDateTimeFormat( webFormatted, FORMAT_DATE_WEB, FORMAT_DATE_MAINFRAME );
	}

	/**
	 * Convert java.util.Date to formatted String for SQLSP (MM/dd/yyyy)
	 * @param java.util.Date date
	 * @return String
	 * @throws LACSDException
	*/
    public String converttoDBforSQLSP(java.util.Date date) throws LACSDException {

		return convertDateTimeFormat( date, FORMAT_DATE_SQLSP );
    }

	/**
	 * Convert java.util.Date to formatted String for WEB (MM/dd/yyyy)
	 * @param java.util.Date date
	 * @return String
	 * @throws LACSDException
	*/
    public String converttoWeb(java.util.Date date) throws LACSDException {

		return convertDateTimeFormat( date, FORMAT_DATE_WEB );
    }

	/**
	 * Convert Date Strings (MM/dd/yyyy to yyyy-MM-dd)
	 * @param String webFormatted
	 * @return String
	 * @throws LACSDException
	*/
    public String convertWebtoDBforJavaSP(String webFormatted) throws LACSDException {

		return convertDateTimeFormat( webFormatted, FORMAT_DATE_WEB, FORMAT_DATE_JAVASP );
    }

	/**
	 * Convert Date Strings (MM/dd/yyyy to MM/dd/yyyy)
	 * @param String webFormatted
	 * @return String
	 * @throws LACSDException
	*/
    public String convertWebtoDBforSQLSP(String webFormatted) throws LACSDException {

		return convertDateTimeFormat( webFormatted, FORMAT_DATE_WEB, FORMAT_DATE_SQLSP );
    }

	/**
	 * Gets a java.sql.Date and convert to a java.util.Date
	 * @param java.sql.Date sqlDate
	 * @return java.util.Date
	*/
    public java.util.Date convertSqlDate2UtilDate(java.sql.Date sqlDate) {
        return new java.util.Date(sqlDate.getTime());
    }            

	/**
	 * Gets a java.sql.Timestamp and convert to a java.util.Date
	 * @param java.sql.Timestamp sqlTimestamp
	 * @return java.util.Date
	*/
    public java.util.Date convertSqlTimestamp2UtilDate(java.sql.Timestamp sqlTimestamp) {
        return new java.util.Date(sqlTimestamp.getTime());
    } 

	/**
	 * Gets a java.util.Date and convert to a java.sql.Date
	 * @param java.util.Date utilDate
	 * @return java.sql.Date
	*/
    public java.sql.Date convertUtilDate2SqlDate(java.util.Date utilDate) {
        if (utilDate == null)
            return null;
        return new java.sql.Date(utilDate.getTime());
    }            

	/**
	 * Adds a number of days to the given date (or subtract if days is negative)
	 * @param java.util.Date utilDate
	 * @param int days
	 * @return java.util.Date
	*/
    public java.util.Date addDays2UtilDate(java.util.Date utilDate, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(utilDate);
        cal.add(Calendar.DATE, days);
        return cal.getTime();
    }            

	/**
	 * Adds a number of days to the given date (or subtract if days is negative)
	 * @param java.sql.Date sqlDate
	 * @param int days
	 * @return java.util.Date
	*/
    public java.sql.Date addDays2SQLDate(java.sql.Date sqlDate, int days) {
    	DateConvert dc = DateConvert.getInstance();
        return dc.convertUtilDate2SqlDate(dc.addDays2UtilDate(dc.convertSqlDate2UtilDate(sqlDate),days));
    }      

	/**
	 * Adds a number of months to the given date (or subtract if months is negative)
	 * @param java.util.Date utilDate
	 * @param int months
	 * @return java.util.Date
	*/
    public java.util.Date addMonths2UtilDate(java.util.Date utilDate, int months) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(utilDate);
        cal.add(Calendar.MONTH, months);
        return cal.getTime();
    }            

	/**
	 * Adds a number of days to the given date (or subtract if days is negative)
	 * @param java.sql.Date sqlDate
	 * @param int months
	 * @return java.util.Date
	*/
    public java.sql.Date addMonths2SQLDate(java.sql.Date sqlDate, int months) {
    	DateConvert dc = DateConvert.getInstance();
        return dc.convertUtilDate2SqlDate(dc.addMonths2UtilDate(dc.convertSqlDate2UtilDate(sqlDate),months));
    }      
    
	/**
	 * Adds a number of years to the given date (or subtract if years is negative)
	 * @param java.util.Date utilDate
	 * @param int years
	 * @return java.util.Date
	*/
    public java.util.Date addYears2UtilDate(java.util.Date utilDate, int years) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(utilDate);
        cal.add(Calendar.YEAR, years);
        return cal.getTime();
    }               


	/**
	 * Adds a number of days to the given date (or subtract if days is negative)
	 * @param java.sql.Date sqlDate
	 * @param int years
	 * @return java.util.Date
	*/
    public java.sql.Date addYears2SQLDate(java.sql.Date sqlDate, int years) {
    	DateConvert dc = DateConvert.getInstance();
        return dc.convertUtilDate2SqlDate(dc.addYears2UtilDate(dc.convertSqlDate2UtilDate(sqlDate),years));
    }      

	/**
	 * Gives a string representation of the given java.util.Date using a user define format,
	 * that follows the standards of java.text.SimpleDateFormat
	 *
	 * @param date java.util.Date
	 * @param simpleFormat String (i.e. MM/dd/yyyy)
	 * @return String
	 * @exception LACSDException
	 */
    public String getStringOfUtilDate(java.util.Date utilDate, String simpleFormat) throws LACSDException {
        try {
            return new SimpleDateFormat(simpleFormat).format(utilDate);
        } catch (Exception e) {
            throw new LACSDException( (utilDate==null)? "null" : utilDate.toString() + "Invalid date" + e);
        }
    }            

	/**
	 * Gives a string representation of the given java.sql.Date using a user define format,
	 * that follows the standards of java.text.SimpleDateFormat
	 *
	 * @param date java.sqk.Date
	 * @param simpleFormat String (i.e. MM/dd/yyyy)
	 * @return String
	 * @exception LACSDException
	 */
    public String getStringOfSQLDate(java.sql.Date sqlDate, String simpleFormat) throws LACSDException {
        return getStringOfUtilDate(convertSqlDate2UtilDate(sqlDate),simpleFormat);
    }            

	/**
	 * Gives a string representation of the given java.sql.Timestamp using a user define format,
	 * that follows the standards of java.text.SimpleDateFormat
	 *
	 * @param sqlTimestamp java.sql.Timestamp
	 * @param simpleFormat String (i.e. MM/dd/yyyy)
	 * @return String
	 * @exception LACSDException
	 */
    public String getStringOfSQLTimestamp(java.sql.Timestamp sqlTimestamp, String simpleFormat) throws LACSDException {
        return getStringOfUtilDate(convertSqlTimestamp2UtilDate(sqlTimestamp),simpleFormat);
    } 


	/**
	 * Get current time as java.util.Date
	 */
    public java.util.Date getUtilDateCurrentTime() {
        return Calendar.getInstance().getTime();
    }            

	/**
	 * Get current time as java.sql.Date
	 */
    public java.sql.Date getSQLDateCurrentTime() {
        return convertUtilDate2SqlDate(getUtilDateCurrentTime());
    }            

	/**
	 * Gets a java.util.Date representation of the given year, month and day
	 *
	 * @param yy int The year   (if year is smaller then 100, we add 1900).
	 * @param mm int The month  (value between 1 and 12)
	 * @param dd int The day
	 * @return java.util.Date
	 * @exception LACSDException
	 */
    public java.util.Date getUtilDate(int yy, int mm, int dd) throws LACSDException
    {
        if (yy < 100)
            yy += 1900;
        try {
            Calendar cal = Calendar.getInstance();
            cal.clear();
            cal.set(Calendar.DATE, dd);
            cal.set(Calendar.MONTH, mm - 1);
            cal.set(Calendar.YEAR, yy);
            return cal.getTime();
        } catch (Exception e) {
            StringBuffer msg = new StringBuffer(10);
            msg.append(yy);
            msg.append('/');
            msg.append(mm);
            msg.append('/');
            msg.append(dd);
            throw new LACSDException( "Invalid date: " + msg + "\n" + e);
        }
    }            

	/**
	 * Gets a java.sql.Date representation of the given year, month and day
	 *
	 * @param yy int The year   (if year is smaller then 100, we add 1900).
	 * @param mm int The month  (value between 1 and 12)
	 * @param dd int The day
	 * @return java.sql.Date
	 * @exception LACSDException
	 */
    public java.sql.Date getSQLDate(int yy, int mm, int dd) throws LACSDException
    {
        return convertUtilDate2SqlDate(getUtilDate(yy,mm,dd));
    }            

	/**
	 * Gets a java.util.Date representation of the given year, month and day.
	 * Parses the string values and then calls the <CODE>getDate(int, int, int)</CODE> method.
	 *
	 * @param yy String The year   (if year is smaller then 100, we add 1900).
	 * @param mm String The month  (value between 1 and 12)
	 * @param dd String The day
	 * @return java.util.Date
	 * @exception LACSDException
	 */
    public java.util.Date getUtilDate(String yy, String mm, String dd) throws LACSDException{
        try {
            int y = Integer.parseInt(yy);
            int m = Integer.parseInt(mm);
            int d = Integer.parseInt(dd);
            return getUtilDate(y,m,d);
        } catch (NumberFormatException e) {
            StringBuffer msg = new StringBuffer(10);
            msg.append(yy);
            msg.append('/');
            msg.append(mm);
            msg.append('/');
            msg.append(dd);
            throw new LACSDException( msg.toString() + "\n" + e);
        }
    }               
    
	/**
	 * Gets a java.sql.Date representation of the given year, month and day.
	 * Parses the string values and then calls the <CODE>getDate(int, int, int)</CODE> method.
	 *
	 * @param yy String The year   (if year is smaller then 100, we add 1900).
	 * @param mm String The month  (value between 1 and 12)
	 * @param dd String The day
	 * @return java.sql.Date
	 * @exception LACSDException
	 */
    public java.sql.Date getSQLDate(String yy, String mm, String dd) throws LACSDException{
        return convertUtilDate2SqlDate(getUtilDate(yy,mm,dd));
    }               
    
	/**
	 * Gets today's java.util.Date with given hours, min, and sec values.
	 *
	 * @param int hours
	 * @param int min
	 * @param int sec
	 * @return java.util.Date
	 * @exception LACSDException
	 */
    public java.util.Date getUtilDateWithTodaysTime(int hours, int min, int sec) throws LACSDException {
        try {
            Calendar cal = Calendar.getInstance();
            java.util.Date today = cal.getTime();
            int year = getCalendarYear(today);
            int month = getCalendarMonth(today) - 1;
            int date = getCalendarDay(today);
            cal.clear();
            cal.set(year, month, date, hours, min, sec);
            return cal.getTime();
        } catch (Exception e) {
            String input = "" + hours + ':' + min + ':'+ sec;
            throw new LACSDException( "Invalid time : " + input + "\n" + e);
        }
    }   
             
	/**
	 * Gets today's java.sql.Date with given hours, min, and sec values.
	 *
	 * @param int hours
	 * @param int min
	 * @param int sec
	 * @return java.sql.Date
	 * @exception LACSDException
	 */
    public java.util.Date getSQLDateWithTodaysTime(int hours, int min, int sec) throws LACSDException {
        return convertUtilDate2SqlDate(getUtilDateWithTodaysTime(hours,min,sec));
    }   
             
	/**
	 * Gets java.util.Date with given year, month, day, hours, min, and sec values.
	 *
	 * @param int year
	 * @param int month
	 * @param int day
	 * @param int hours
	 * @param int min
	 * @param int sec
	 * @return java.util.Date
	 * @exception LACSDException
	 */
    public java.util.Date getUtilDateWithTime( int year, int month, int day, int hours, int min, int sec) throws LACSDException {
        try {
            Calendar cal = Calendar.getInstance();
            cal.clear();
            cal.set(year, month-1, day, hours, min, sec);
            return cal.getTime();
        } catch (Exception e) {
            String input = "" + hours + ':' + min + ':' + sec;
            throw new LACSDException( "Invalid time : " + input + "\n" + e);
        }

    }            

	/**
	 * Gets java.sql.Date with given year, month, day, hours, min, and sec values.
	 *
	 * @param int year
	 * @param int month
	 * @param int day
	 * @param int hours
	 * @param int min
	 * @param int sec
	 * @return java.sql.Date
	 * @exception LACSDException
	 */
    public java.sql.Date getSQLDateWithTime( int year, int month, int day, int hours, int min, int sec) throws LACSDException {
        return convertUtilDate2SqlDate(getUtilDateWithTime(year,month,day,hours,min,sec));
    }            

    /**
     * Gives a date using a custom format,
     * that follows the standards of java.text.SimpleDateFormat
     * Avoid using this method.  Try to use the standard format throughout the whole application.
     *
     * @param date String
     * @param simpleFormat String
     * @return java.util.Date
     * @exception LACSDException
     */
    public java.util.Date getUtilDateFromStringFormat( String dateString, String simpleFormat ) throws LACSDException {
        try {
            return new SimpleDateFormat(simpleFormat).parse(dateString);
        } catch ( Exception e ) {
            throw new LACSDException( (dateString==null)?"null" : dateString.toString() + " Invalid date " + e);
        }
    } 
                  
    /**
     * Gives a date using a custom format,
     * that follows the standards of java.text.SimpleDateFormat
     * Avoid using this method.  Try to use the standard format throughout the whole application.
     *
     * @param date String
     * @param simpleFormat String
     * @return java.sql.Date
     * @exception LACSDException
     */
    public java.sql.Date getSQLDateFromStringFormat( String dateString, String simpleFormat ) throws LACSDException {
        return convertUtilDate2SqlDate(getUtilDateFromStringFormat(dateString,simpleFormat));
    }

    /**
     * Convert date from MM/dd/yyyy to MMM dd, yyyy
     * Ex. 12/31/2006 to Dec 31, 2006
     * @param String
     * @return String
     */
    public String convertWebToPrintFormat(String argWebFormatted) {
    	try {
    		return convertDateTimeFormat(argWebFormatted, FORMAT_DATE_WEB, FORMAT_DATE_PRINT);
    	} catch (Exception e) {
    		return null;
    	}
    }

    /**
     * Convert date from yyyy-MM-dd to MMM dd, yyyy
     * 
     * Ex. 2006-12-31 to Dec 31, 2006
     * @param String
     * @return String
     */
    public String convertSPToPrintFormat(String argWebFormatted) {
    	try {
    		return convertDateTimeFormat(argWebFormatted, FORMAT_DATE_JAVASP, FORMAT_DATE_PRINT);
    	} catch (Exception e) {
    		return null;
    	}
    }

	/**
	 * Gets the day from a given date (1 - 31) using the Calendar class.
	 *
	 * @param java.util.Date utilDate
	 * @return int
	 * @exception LACSDException
	 */
    public int getCalendarDay(java.util.Date utilDate) throws LACSDException{
        try {
            Calendar cal = Calendar.getInstance();
            cal.clear();
            cal.setTime(utilDate);
            return cal.get(Calendar.DATE);
        } catch (Exception e) {
            throw new LACSDException( (utilDate==null)?"null" : utilDate.toString() + " Invalid date " + e);
        }
    }            
    
	/**
	 * Gets the hour from a given date (0 - 23) using the Calendar class.
	 *
	 * @param java.util.Date utilDate
	 * @return int
	 * @exception LACSDException
	 */
    public int getCalendarHourOfDay(java.util.Date utilDate) throws LACSDException{
        try {
            Calendar cal = Calendar.getInstance();
            cal.clear();
            cal.setTime(utilDate);
            return cal.get(Calendar.HOUR_OF_DAY);
        } catch (Exception e) {
            throw new LACSDException( (utilDate==null)?"null" : utilDate.toString() + " Invalid date " + e);
        }
    }

	/**
	 * Get hour from a Timestamp (0-23) using the Calendar class.
	 * @param Timestamp
	 * @return int hour 
	 */
	public int getCalendarHourOfDay(Timestamp ts) throws LACSDException {
		try {
			Calendar c = Calendar.getInstance();
			c.setTime(new Date(ts.getTime()));
			int hour = c.get(Calendar.HOUR_OF_DAY);
			return hour;
		} catch (Exception e) {
			throw new LACSDException( (ts==null)?"null" : ts.toString() + " Invalid timestamp " + e);
		}
	}

	/**
	 * Gets the minutes from a given date (0 - 59) using the Calendar class.
	 *
	 * @param java.util.Date utilDate
	 * @return int
	 * @exception LACSDException
	 */
    public int getCalendarMinutes(java.util.Date utilDate) throws LACSDException{
        try {
            Calendar cal = Calendar.getInstance();
            cal.clear();
            cal.setTime(utilDate);
            return cal.get(Calendar.MINUTE);
        } catch (Exception e) {
            throw new LACSDException( (utilDate==null)?"null" : utilDate.toString() + " Invalid date " + e);
        }
    }            

	/**
	 * Gets the month from a given date (1 - 12) using the Calendar class.
	 *
	 * @param java.util.Date utilDate
	 * @return int
	 * @exception LACSDException
	 */
    public int getCalendarMonth(java.util.Date utilDate) throws LACSDException {
        try {
            Calendar cal = Calendar.getInstance();
            cal.clear();
            cal.setTime(utilDate);
            return cal.get(Calendar.MONTH) + 1;
        } catch (Exception e) {
            throw new LACSDException( (utilDate==null)?"null" : utilDate.toString() + " Invalid date " + e);
        }
    }

	/**
	 * Gets the month from a given date (1 - 12) -  ZERO PADS THE STRING for month's less than 10
	 * @param java.util.Date utilDate
	 * @return String
	 * @throws LACSDException
	 */
	public String getCalendarMonthString(java.util.Date utilDate) throws LACSDException {
        try {
            Calendar cal = Calendar.getInstance();
            cal.clear();
            cal.setTime(utilDate);
            int month = (cal.get(Calendar.MONTH) + 1);
            String returnString = null;
            if (month < 10) {
            	returnString = "0" + String.valueOf(month);
            }
            else {
            	returnString = String.valueOf(month);
            }
            return returnString;
        } catch (Exception e) {
            throw new LACSDException( (utilDate==null)?"null" : utilDate.toString() + " Invalid date " + e);
        }
	}
       
	/**
	 * Gets the year from a given date (1900 - 9999) using the Calendar class.
	 *
	 * @param java.util.Date utilDate
	 * @return int
	 * @exception LACSDException
	 */
    public int getCalendarYear(java.util.Date utilDate) throws LACSDException{
        try {
            Calendar cal = Calendar.getInstance();
            cal.clear();
            cal.setTime(utilDate);
            int yy = cal.get(Calendar.YEAR);
            if (yy < 100)
                yy += 1900;
            return yy;
        } catch (Exception e) {
            throw new LACSDException( (utilDate==null)?"null" : utilDate.toString() + " Invalid date " + e);
        }
    }
                
	/**
	 * Checks whether 2 dates are on the same day
	 * @return boolean
	 * @param java.util.Date date1
	 * @param java.util.Date date2
	 */
    public boolean isSameCalendarDay(java.util.Date date1, java.util.Date date2) {
        if ( date1 == null && date2 == null ) {
            return true;
        }
        if ( date1 == null || date2 == null ) {
            return false;
        }

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return(cal1.get(Calendar.DAY_OF_MONTH) ==
               cal2.get(Calendar.DAY_OF_MONTH)
               && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH)
               && cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR));
    }            
    
	/**
	 * Gets the days difference between two given java.util.Date
	 * ignore all hour, min, secs
	 *
	 * @param java.util.Date date1
	 * @param java.util.Date date2
	 * @return int
	 * @exception LACSDException
	 */
    public int daysDifference(java.util.Date date1, java.util.Date date2) {

		Calendar d1 = Calendar.getInstance();
		d1.setTime(date1);
		d1.clear(Calendar.HOUR);
		d1.clear(Calendar.HOUR_OF_DAY);
		d1.clear(Calendar.MINUTE);
		d1.clear(Calendar.SECOND);
		d1.clear(Calendar.MILLISECOND);

		Calendar d2 = Calendar.getInstance();
		d2.setTime(date2);
		d2.clear(Calendar.HOUR);
		d2.clear(Calendar.HOUR_OF_DAY);
		d2.clear(Calendar.MINUTE);
		d2.clear(Calendar.SECOND);
		d2.clear(Calendar.MILLISECOND);

        long diff = this.timeDifference(d1,d2);
        long ret = diff/86400000; ;        //  60 * 60 * 24 * 1000

        return new Long(ret).intValue();
    }

	/**
	 * Gets the time difference between two given java.util.Date
	 * in milliseconds
	 * 
	 *
	 * @param java.util.Date date1
	 * @param java.util.Date date2
	 * @return int
	 * @exception LACSDException
	 */
    public long timeDifference(java.util.Calendar d1, java.util.Calendar d2) {

        return d2.getTime().getTime() - d1.getTime().getTime();
    }


	/**
	 * Gets the Month relative to the current month.
	 * Example:  If input is "0", Current Month,
	 * 			 If input is "1", Next Month
	 *           If input is "2", 2 Months from Now
	 *           If input is "-1", Last Month
	 * 
	 * @param int offset
	 * @return String month
	*/
	public String getRelativeMonth(int offset) {

	    java.util.Calendar cal = java.util.Calendar.getInstance();
    	cal.add(Calendar.MONTH,offset);

		java.util.Date date = cal.getTime();
		java.text.SimpleDateFormat monthFormat = new java.text.SimpleDateFormat(FORMAT_STATIC_MONTH);
		return monthFormat.format(date);
	}

	/**
	 * Gets the Year relative to the current year.
	 * Example:  If input is "0", Current Year,
	 * 			 If input is "1", Next Year
	 *           If input is "2", 2 Years from Now
	 *           If input is "-1", Last Year
	 * 
	 * @param int offset
	 * @return String year
	*/
	public String getRelativeYear(int offset) {

	    java.util.Calendar cal = java.util.Calendar.getInstance();
    	cal.add(Calendar.YEAR,offset);
    	
		java.util.Date date = cal.getTime();
		java.text.SimpleDateFormat yearFormat = new java.text.SimpleDateFormat(FORMAT_STATIC_YEAR);
		return yearFormat.format(date);
	}


	/**
	 * Resolve last day of the given month
	 * @param String month (MM) format
	 * @param String year (yyyy) format
	 * @return String
	*/
	public String getLastDayOfMonth(String MM, String yyyy) throws LACSDException {
		
		java.util.Date date = this.getUtilDate(yyyy, MM, "01");
		
		java.util.Calendar cal = java.util.Calendar.getInstance();
		cal.clear();
		cal.setTime(date);

		int maxDays = cal.getActualMaximum(java.util.Calendar.DAY_OF_MONTH);
		return Integer.toString(maxDays);
	}

	/**
	 * Resolve the day of the week (int integer) of a given date
	 * @param int year 
	 * @param int month
	 * @param int day
	 * @return int dayOfWeek
	*/
	public int getDayOfWeek(java.util.Date date) {

		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
//		cal.setTime(cal.getTime());
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);  
		return dayOfWeek;
	}

	/**
	 * Resolve the day of the week (in string) of a given date
	 * @param int year 
	 * @param int month
	 * @param int day
	 * @return int dayOfWeek
	*/
	public String getDayOfWeekStr(java.util.Date date) {

		int dayOfWeek = getDayOfWeek(date);  
		String dayOfWeekStr = null;
		switch (dayOfWeek) {
			case 1:  dayOfWeekStr = DAY_SUNDAY; break;
			case 2:  dayOfWeekStr = DAY_MONDAY; break;
			case 3:  dayOfWeekStr = DAY_TUESDAY; break;
			case 4:  dayOfWeekStr = DAY_WEDNESDAY; break;
			case 5:  dayOfWeekStr = DAY_THURSDAY; break;
			case 6:  dayOfWeekStr = DAY_FRIDAY; break;
			case 7:  dayOfWeekStr = DAY_SATURDAY; break;
		}
				
		return dayOfWeekStr;
	}
	
	/**
	 * Resolve the day of the week (in string) of a given index
	*/
	public String getDayOfWeekStr(int dayOfWeek) {

		
		String dayOfWeekStr = null;
		switch (dayOfWeek) {
			case 1:  dayOfWeekStr = DAY_SUNDAY; break;
			case 2:  dayOfWeekStr = DAY_MONDAY; break;
			case 3:  dayOfWeekStr = DAY_TUESDAY; break;
			case 4:  dayOfWeekStr = DAY_WEDNESDAY; break;
			case 5:  dayOfWeekStr = DAY_THURSDAY; break;
			case 6:  dayOfWeekStr = DAY_FRIDAY; break;
			case 7:  dayOfWeekStr = DAY_SATURDAY; break;
		}
				
		return dayOfWeekStr;
	}
	
	/**
	 * Get java.sql.Timestamp String (1-2004-03-17-22-31-22-98)
	 * @param long
	 * @return String
	 */
	public String getStringOfSQLTimestamp(long timeMillis) {
		String timestampString = new java.sql.Timestamp(timeMillis).toString();
		return timestampString;
	}

	/**
	 * Convert to military time 
	 * Ex. 01:35 AM -> 01:35
	 * Ex. 02:28 PM -> 14:28
	 * @param String
	 * @return String
	 */
	public String convertTimeToMilitaryTime(String time) {

	    int colonPos = time.indexOf(":");
	    int hour = Integer.parseInt(time.substring(0, colonPos));
	    int minute = Integer.parseInt(time.substring(colonPos+1, colonPos+3));
	    int ampmPos = time.length()-2;
	    String am_pm = time.substring(ampmPos);

	    if (am_pm.startsWith("a") || am_pm.startsWith("A")) {
	        return hour==12 ? "00:"+(minute<10?"0"+minute:""+minute) 
	                		: (hour<10?"0"+hour:""+hour) + ":" + (minute<10?"0"+minute:""+minute); 
	    } else {
	        return (hour<12 ? (hour+12)+"" : hour+"") + ":" + (minute<10?"0"+minute:""+minute);
	    }
	}

	/**
	 * Convert to military time 
	 * Ex. 01:35:00 -> 01:35 AM
	 * Ex. 14:28:30 -> 02:28 PM
	 * @param String
	 * @return String
	 */
	public String convertMilitaryTimeToAM_PM(String strTime) throws LACSDException {
	    return convertDateTimeFormat(strTime, FORMAT_TIME_MILITARY_HH_MM_SS, FORMAT_TIME_HH_MM_AM);
	}

	 /**
     * Convert date from MM/dd/yyyy to XMLGregorianCalendar
     * @param String
     * @return XMLGregorianCalendar
     */
    public XMLGregorianCalendar convertWebToXMLGregorianCalendar(String argWebFormatted) {
    	try {
    	    XMLGregorianCalendar xmlGregCal = DatatypeFactory.newInstance().newXMLGregorianCalendar();
    	    Calendar cal = Calendar.getInstance();
    	    cal.setTime(Date.valueOf(convertWebtoDBforJavaSP(argWebFormatted)));
    	    xmlGregCal.setDay(cal.get(Calendar.DAY_OF_MONTH));
    	    xmlGregCal.setMonth(cal.get(Calendar.MONTH)+1); // Add 1, Jan = 0, Feb = 1, etc.
    	    xmlGregCal.setYear(cal.get(Calendar.YEAR));
    	    return xmlGregCal;
    	} catch (Exception ex) {
    		return null;
    	}
    }

    /**
     * Convert date from MM/dd/yyyy to XMLGregorianCalendar
     * @param String
     * @return XMLGregorianCalendar
     */
    public XMLGregorianCalendar convertSQLDateToXMLGregorianCalendar(Date sqlDate) {
    	try {
    	    XMLGregorianCalendar xmlGregCal = DatatypeFactory.newInstance().newXMLGregorianCalendar();
    	    Calendar cal = Calendar.getInstance();
    	    cal.setTime(sqlDate);
    	    xmlGregCal.setDay(cal.get(Calendar.DAY_OF_MONTH));
    	    xmlGregCal.setMonth(cal.get(Calendar.MONTH)+1); // Add 1, Jan = 0, Feb = 1, etc.
    	    xmlGregCal.setYear(cal.get(Calendar.YEAR));
    	    return xmlGregCal;
    	} catch (Exception ex) {
    		return null;
    	}
    }	
	/**
	 * Convert SQLDate to military time 
	 * @param String
	 * @return String
	 */
	public String convertSQLDateToMilitaryTime(Date date) throws LACSDException {
	    return convertDateTimeFormat(date, FORMAT_TIME_MILITARY_HH_MM_SS);
	}

	/**
	 * Convert SQLDate to mainframe 
	 * @param Date
	 * @return String
	 */
	public String convertSQLDateToMainframe(Date date) throws LACSDException {
	    return convertDateTimeFormat(date, FORMAT_DATE_MAINFRAME);
	}

	/**
	 * Get fiscal year from DB date format
	 * Ex. 2009-01-01 -> 2008
	 * Ex. 2008-12-31 -> 2008
	 * @param String
	 * @return String
	 */
	public String getFiscalYearFromDBDate(String argDate) throws LACSDException {

	    Date d = Date.valueOf(argDate);
	    Calendar c = Calendar.getInstance();
	    c.setTime(d);
	    
	    int fiscalYear = c.get(Calendar.YEAR);
	    
	    if ( c.get(Calendar.MONTH) < 6 ) {
	        fiscalYear--;
	    }

	    return Integer.toString( fiscalYear );
	}

	/**
	 * Calculate current fiscal year from current time
	 * i.e. 6/18/2014 10:04 am -> 2013
	 * @return int fiscalYear
	 */
	public int getCurrentFiscalYear() {
		Calendar c = Calendar.getInstance();
		int month = c.get(Calendar.MONTH);
		int fiscalYear = month < Calendar.JULY ? c.get(Calendar.YEAR)-1 : c.get(Calendar.YEAR);
		return fiscalYear;
	}

	/**
	 * Main method
	 */
    public static void main (String[] args) {

        try {
        	/*System.out.println( DateConvert.getInstance().convertDateTimeWebtoDB("11/11/2004 23:33:34") );
            SimpleDateFormat dateFormatter = new SimpleDateFormat(FORMAT_DATE_SQLSP);
            java.util.Date date1 = dateFormatter.parse("11/12/2004");
            System.out.println( date1 + " date1 : " + date1.getTime() );
            java.util.Date date2 = dateFormatter.parse("11/23/2004");
            System.out.println( date2 + " date2 : " + date2.getTime() );
            int diff = DateConvert.getInstance().daysDifference( date1, date2 );
            System.out.println( "Difference : " + diff );
            
            DateConvert dc = DateConvert.getInstance();
            System.out.println(dc.convertMainframeToWeb("19999099"));
			System.out.println(dc.convertWebToMainframe("05/80/2005"));
			
			System.out.println(dc.convertWebToPrintFormat("11/30/2006"));

			System.out.println();

			System.out.println(dc.convertTimeToMilitaryTime("01:38 PM"));
			System.out.println(dc.convertTimeToMilitaryTime("01:38 AM"));
			System.out.println(dc.convertTimeToMilitaryTime("1:38 PM"));
			System.out.println(dc.convertTimeToMilitaryTime("12:38 PM"));
			System.out.println(dc.convertTimeToMilitaryTime("12:38 AM"));

			System.out.println();

			System.out.println(dc.convertMilitaryTimeToAM_PM("01:38:23"));
			System.out.println(dc.convertMilitaryTimeToAM_PM("13:38:29"));
			System.out.println(dc.convertMilitaryTimeToAM_PM("18:38:30"));
			System.out.println(dc.convertMilitaryTimeToAM_PM("15:38:09"));
			System.out.println(dc.convertMilitaryTimeToAM_PM("00:38:23"));
			
			System.out.println(dc.convertSQLDateToMainframe(new Date(System.currentTimeMillis())));

			System.out.println(dc.convertSQLtoFullWebPrint("2011-01-11"));*/
        	java.text.MessageFormat phoneMsgFmt =new java.text.MessageFormat("({0})-{1}-{2}");
        	 DateConvert dc = DateConvert.getInstance();
        	System.out.println(dc.getUtilDateFromStringFormat("01/01/2018 00:00","MM/dd/yyyy HH:mm"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}