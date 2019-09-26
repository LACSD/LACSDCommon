package org.lacsd.common.comparitors;

import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.Date;

import org.apache.commons.beanutils.BeanUtils;
import org.lacsd.common.service.DateConvert;

/******************************************************************************
//* Copyright (c) 2011 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		ObjectDesc.java
//* Revision: 		1.0
//* Author:			hho@lacsd.org
//* Created On: 	May 5, 2011
//* Modified By:    
//* Modified On:    
//* 
//* Description:	
/******************************************************************************/

public class ObjectDesc implements Comparator<Object>{

	private String fieldSort;
	private boolean isDate = false;
	private boolean ignoreCase = false;
	
	public ObjectDesc(String fieldSort, boolean isDate) {
		this.fieldSort = fieldSort;
		this.isDate = isDate;
	}
	
	public ObjectDesc(String fieldSort, boolean isDate, boolean ignoreCase) {
		this.fieldSort = fieldSort;
		this.isDate = isDate;
		this.ignoreCase = ignoreCase;
	}
	
	/**
	 * Overriding Method to Compare Dates in Descending Order
	 * @param Object o1
	 * @param Object o2
	 * @return int
	*/
	public int compare(Object o1, Object o2){
		String value1 = "";
		String value2 = "";
		String type = "";
		try {
		     value1 = BeanUtils.getProperty(o1, fieldSort);
		     value2 = BeanUtils.getProperty(o2, fieldSort);
		     if (isDate) {
		    	 DateConvert dc = DateConvert.getInstance();
		    	 boolean success = true;
		    	 Date date1 = null;
		    	 Date date2 = null;
		    	 try {
		    		 date1 = dc.getUtilDateFromStringFormat(value1,"MM/dd/yyyy HH:mm");
		    		 date2 = dc.getUtilDateFromStringFormat(value2,"MM/dd/yyyy HH:mm");
		    		
		    	 }catch(Exception e) {
		    		 success = false;
		    	 }
		    	//try the short format
		    	 if (!success) {
		    		 try {
		    	    		date1 = dc.getUtilDateFromStringFormat(value1,"MM/dd/yyyy");
				    		date2 = dc.getUtilDateFromStringFormat(value2,"MM/dd/yyyy");
				    	 }catch(Exception e) {
				    		 success = false;
				    	 }
		    	 }
		    	 if (success) {
		    		 return date2.compareTo(date1);
		    	 }
		     }
			 Class<? extends Object> cls  = o1.getClass();
			 Field fieldlist[] = cls.getDeclaredFields();
			 for (int i = 0; i < fieldlist.length; i++) {
				 Field fld = fieldlist[i];
                 if (fld.getName().equals(fieldSort)) {
                   type = fld.getType().getSimpleName();
                   break;
                 }
             }
	
		}catch(Exception e) {
			//take no action
		}
		if (type.equals("int") || type.equalsIgnoreCase("short") || type.equalsIgnoreCase("byte") 
			|| type.equals("Integer")) {
			return new Integer(value2).compareTo(new Integer(value1));
		}else if (type.equalsIgnoreCase("double")|| type.equalsIgnoreCase("float")) {
			return new Double(value2).compareTo(new Double(value1));
		}else if (type.equalsIgnoreCase("long")) {
			return new Long(value2).compareTo(new Long(value1));
		}else {
			return new AlphanumericSorting(false,ignoreCase ).compareString(value1, value2);
		}
	}
}

