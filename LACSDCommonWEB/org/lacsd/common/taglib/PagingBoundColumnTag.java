package org.lacsd.common.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.beanutils.BeanUtils;
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
	private static final long serialVersionUID = -4639981276119098075L;
	private String property;
	private String title;
	private String align= "left";
	private String cellStyle;
	private String width;
	private boolean sortable;
	private boolean isBoolean;
	private boolean dateFormat = false;

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


	public int doEndTag() throws JspException {
		try {
			PagingTableTag pagingTag= (PagingTableTag) findAncestorWithClass(this, PagingTableTag.class);
			PagingRowTag rowTag = (PagingRowTag) findAncestorWithClass(this, PagingRowTag.class);
			int rowNumber= pagingTag.getIndex();

			if (rowNumber == 1)
				if (sortable) {
					String str = "<th align=\"" + align + "\"" + (width==null?"":" width=\""+ width+"\"") + " class=\"" + rowTag.getHeaderStyle() + "\">" +
					             "<table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"><tr>" +
							     "<td width=\"100%\" " + " class=\"" + rowTag.getHeaderStyle() + "\">" + (title==null?property:title) + "</td>" +
							     "<td width=\"1\" >" +
							     "<a onclick=\"document.all['pagingVO.dateType'].value='"+ dateFormat + "';document.all['pagingVO.dateType'].value='"+ dateFormat + "';document.all['pagingVO.sortField'].value='"+ property+ "'; document.all['pagingVO.sortType'].value='"+ PagingVO.ASC_SORT+ "'; document."+ pagingTag.getFormName()+ ".submit();\" ><span style=\"font-family:Webdings;font-size:10pt;text-decoration:none;cursor:pointer;\" class=\"" + rowTag.getHeaderStyle() + "\">5</span></a>" +
							     "<a onclick=\"document.all['pagingVO.dateType'].value='"+ dateFormat + "';document.all['pagingVO.dateType'].value='"+ dateFormat+ "'; document.all['pagingVO.sortType'].value='"+ PagingVO.DESC_SORT+ "'; document."+ pagingTag.getFormName()+ ".submit();\" ><span style=\"font-family:Webdings;font-size:10pt;text-decoration:none;cursor:pointer;\" class=\"" + rowTag.getHeaderStyle() + "\">6</span></a>" +
							     "</td>" +
							     "</tr></table></th>";
					pageContext.getOut().write(str);		
				}else {
					pageContext.getOut().write("<th align=\"" + align + "\"" + (width==null?"":" width=\""+ width+"\"") +  " class=\"" + rowTag.getHeaderStyle() + "\">" + (title==null?property:title) + "</th>");
				}
			else {
				Object refObj = pagingTag.getRefObj();
				if (refObj == null)
					throw new JspTagException("Cannot find any data");
				else {
					String value= BeanUtils.getProperty(refObj, property);
					
					if(isBoolean){
						value = (value == "true"? "Y":"N");
					}
					
					pageContext.getOut().write("<td align=\"" + align + "\" " + (cellStyle==null?"":"class=\""+ cellStyle +"\"") + (width==null?"":" width=\""+ width+"\"") +">" + (value==null?"":value.trim()) + "</td>");
				}
			}

		} catch (Exception e) {
			throw new JspException(e.getMessage());
		}

		return SKIP_BODY;
	}

	

}

