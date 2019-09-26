package org.lacsd.common.struts2.taglib;


import java.text.DecimalFormat;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.lacsd.common.service.NumberConvert;
import org.lacsd.common.values.PagingVO;

/******************************************************************************
//* Copyright (c) 2011 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		BoundColumnTag.java
//* Revision: 		1.0
//* Author:			hho@lacsd.org
//* Created On: 	Apr 19, 2011
//* Modified By:    
//* Modified On:    
//* 
//* Description:	
/******************************************************************************/

public class PagingBoundColumnTag extends TagSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2013852879527736202L;
	private String property;
	private String title;
	private String align= "left";
	private String cellStyle;
	private String width;
	private boolean sortable;
	private boolean isBoolean;
	private boolean dateFormat = false;
	private boolean isCurrency;
	private boolean commaNumber;
	private int decimalPlace;
	private String formatPattern;
	private String htmlAttribute;

	/**
	 * Return the property
	 * @return String
	 */
	public String getProperty() {
		return property;
	}

	/**
	 * Set the property
	 * @param property the property to set
	 */
	public void setProperty(String property) {
		this.property = property;
	}

	/**
	 * Return the title
	 * @return String
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * Set the title
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Return the align
	 * @return String
	 */
	public String getAlign() {
		return align;
	}

	/**
	 * Set the align
	 * @param align the align to set
	 */
	public void setAlign(String align) {
		this.align = align;
	}


	/**
	 * Return the cellStyle
	 * @return String
	 */
	public String getCellStyle() {
		return cellStyle;
	}



	/**
	 * Set the cellStyle
	 * @param cellStyle the cellStyle to set
	 */
	public void setCellStyle(String cellStyle) {
		this.cellStyle = cellStyle;
	}

	/**
	 * Return the width
	 * @return String
	 */
	public String getWidth() {
		return width;
	}

	/**
	 * Set the width
	 * @param width the width to set
	 */
	public void setWidth(String width) {
		this.width = width;
	}

	
	/**
	 * Return the sortTable
	 * @return boolean
	 */
	public boolean isSortable() {
		return sortable;
	}

	/**
	 * Set the sortTable
	 * @param sortTable the sortTable to set
	 */
	public void setSortable(boolean sortable) {
		this.sortable = sortable;
	}
	
	
	/**
	 * Return if the column value is boolean
	 * @return boolean
	 */
	public boolean isBoolean() {
		return isBoolean;
	}
	
	/**
	 * Set the if the column value is boolean
	 * @param is boolean to set
	 */
	public void setIsBoolean(boolean isBoolean) {
		this.isBoolean = isBoolean;
	}
	

	/**
	 * Return the dateFormat
	 * @return boolean
	 */
	public boolean isDateFormat() {
		return dateFormat;
	}
	
	/**
	 * Set the dateFormat
	 * @param dateFormat the dateFormat to set
	 */
	public void setDateFormat(boolean dateFormat) {
		this.dateFormat = dateFormat;
	}
	
	
	/**
	 * Return if the column is currency
	 * @return
	 */
	public boolean getIsCurrency() {
		return isCurrency;
	}

	/**
	 * Set if the column is currency
	 * @param isCurrency
	 */
	public void setIsCurrency(boolean isCurrency) {
		this.isCurrency = isCurrency;
	}

	/**
	 * @return the commaNumber
	 */
	public boolean isCommaNumber() {
		return commaNumber;
	}

	/**
	 * @param commaNumber the commaNumber to set
	 */
	public void setCommaNumber(boolean commaNumber) {
		this.commaNumber = commaNumber;
	}

	/**
	 * @return the decimalPlace
	 */
	public int getDecimalPlace() {
		return decimalPlace;
	}

	/**
	 * @param decimalPlace the decimalPlace to set
	 */
	public void setDecimalPlace(int decimalPlace) {
		this.decimalPlace = decimalPlace;
	}
	

	public String getFormatPattern() {
		return formatPattern;
	}

	public void setFormatPattern(String formatPattern) {
		this.formatPattern = formatPattern;
	}
	
	public String getHtmlAttribute() {
		return htmlAttribute;
	}

	public void setHtmlAttribute(String htmlAttribute) {
		this.htmlAttribute = htmlAttribute;
	}

	@SuppressWarnings("rawtypes")
	public int doEndTag() throws JspException {
		try {
			PagingTableTag pagingTag= (PagingTableTag) findAncestorWithClass(this, PagingTableTag.class);
			PagingRowTag rowTag = (PagingRowTag) findAncestorWithClass(this, PagingRowTag.class);
			int rowNumber= pagingTag.getIndex();

			if (rowNumber == 0)
				if (sortable) {
					String str = "<th align=\"" + align + "\"" + (width==null?"":" width=\""+ width+"\"") + " class=\"" + rowTag.getHeaderStyle() + "\">" +
					             "<table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"  ><tr height=\"100%\">" +
							     "<td width=\"100%\">" + (title==null?property:title) + "</td>" +
							     "<td width=\"1\" >" +
							     "<a onclick=\"document.all['" + pagingTag.getPagingProperty() + ".dateType'].value='"+ dateFormat + "';document.all['" + pagingTag.getPagingProperty() + ".sortField'].value='"+ property+ "'; document.all['" + pagingTag.getPagingProperty() + ".sortType'].value='"+ PagingVO.ASC_SORT+ "'; document."+ pagingTag.getFormName()+ ".submit();\" ><span style=\"font-family:Webdings;font-size:10pt;text-decoration:none;cursor:pointer;\" class=\"" + rowTag.getHeaderStyle() + "\">5</span></a>" +
							     "<a onclick=\"document.all['" + pagingTag.getPagingProperty() + ".dateType'].value='"+ dateFormat+ "';document.all['" + pagingTag.getPagingProperty() + ".sortField'].value='"+ property+ "'; document.all['" + pagingTag.getPagingProperty() + ".sortType'].value='"+ PagingVO.DESC_SORT+ "'; document."+ pagingTag.getFormName()+ ".submit();\" ><span style=\"font-family:Webdings;font-size:10pt;text-decoration:none;cursor:pointer;\" class=\"" + rowTag.getHeaderStyle() + "\">6</span></a>" +
							     "</td>" +
							     "</tr></table></th>";
					pageContext.getOut().write(str);		
				}else {
					pageContext.getOut().write("<th align=\"" + align + "\"" + (width==null?"":" width=\""+ width+"\"") + " class=\"" + rowTag.getHeaderStyle() + "\">" + (title==null?property:title) + "</th>");
				}
			else {
				Object refObj = pagingTag.getRefObj();
				if (refObj == null)
					throw new JspTagException("Cannot find any data");
				else {
					Class type = PropertyUtils.getPropertyType(refObj, property);
					String value= BeanUtils.getProperty(refObj, property);
					
					if(value != null && isBoolean){
						value = (value == "true"? "Y":"N");
					}
					//Adding by Hoa Ho 2016/6/10 
					if (this.formatPattern != null && this.formatPattern.length() > 0 && value != null) {
						if (type.getName().equals("java.lang.Integer") || type.getName().equals("java.lang.Double") || type.getName().equals("java.lang.Float")) {
							DecimalFormat df = new DecimalFormat(formatPattern );
							value = df.format(new Double(value).doubleValue());
						}else {
							value = String.format(formatPattern, value);
						}
					}
					
					//adding by George
					if(value != null && isCurrency == true){
						
						NumberConvert nc = NumberConvert.getInstance();
						if(value.equals("NaN")){
							value = null;
						}else{
							value = nc.getRoundOffNumber(value,decimalPlace);
							value = nc.getNumberAsCustomFormat(value,"$","-",NumberConvert.SIGN_LOCATION_LEFT);
						}
						
					}
					
					if(value != null && commaNumber == true){
						NumberConvert nc = NumberConvert.getInstance();
						if(value.equals("NaN")){
							value = null;
						}else{
							value =nc.getRoundOffCommaNumber(value,decimalPlace);
						}
						
					}
					
					pageContext.getOut().write("<td align=\"" + align + "\" " + (cellStyle==null?"":"class=\""+ cellStyle +"\"") + (width==null?"":" width=\""+ width+"\"") + " "+ (htmlAttribute==null?"":htmlAttribute) +">" + (value==null?"":value.trim()) + "</td>");
				}
			}

		} catch (Exception e) {
			throw new JspException(e.getMessage());
		}

		return SKIP_BODY;
	}

	
}

