package org.lacsd.common.struts2.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.lacsd.common.values.PagingVO;

/******************************************************************************
//* Copyright (c) 2011 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		PagingColumnTag.java
//* Revision: 		1.0
//* Author:			hho@lacsd.org
//* Created On: 	Apr 19, 2011
//* Modified By:    
//* Modified On:    
//* 
//* Description:	
/******************************************************************************/

public class PagingColumnTag extends TagSupport {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6955376957560435763L;
	private String title;
	private String align= "left";
	private String width;
	private String cellStyle;
	private boolean sortable;
	private String property;
	private boolean dateFormat = false;

	

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
	
	

	public boolean isSortable() {
		return sortable;
	}

	public void setSortable(boolean sortable) {
		this.sortable = sortable;
	}

	
	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public boolean isDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(boolean dateFormat) {
		this.dateFormat = dateFormat;
	}

	public int doStartTag() throws JspException {
		int eval= 0;
		try {
			PagingTableTag pagingTag= (PagingTableTag) findAncestorWithClass(this, PagingTableTag.class);
			PagingRowTag rowTag = (PagingRowTag) findAncestorWithClass(this, PagingRowTag.class);
			int rowNumber= pagingTag.getIndex();
			if (rowNumber == 0) {
				pageContext.getOut().write("<th align=\"" + align + "\" class=\""+ rowTag.getHeaderStyle() +"\"" + (width==null?"":" width=\""+ width+"\"") +">");
				eval= SKIP_BODY;
			} else {
				pageContext.getOut().write("<td align=\"" + align + "\" " + (cellStyle==null?"":"class=\""+ cellStyle+"\"") + (width==null?"":" width=\""+ width+"\"") +">");
				eval= EVAL_BODY_INCLUDE;
			}
		} catch (Exception e) {
			throw new JspException(e.getMessage());
		}
		return eval;
	}

	public int doEndTag() throws JspException {
		try {
			PagingTableTag pagingTag= (PagingTableTag) findAncestorWithClass(this, PagingTableTag.class);
			PagingRowTag rowTag = (PagingRowTag) findAncestorWithClass(this, PagingRowTag.class);
			int rowNumber= pagingTag.getIndex();
			if (rowNumber == 0) {
				if (sortable) {
					String str = "<table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"  ><tr height=\"100%\">" +
							     "<td width=\"100%\" class=\""+ rowTag.getHeaderStyle() +"\">" + title + "</td>" +
							     "<td width=\"1\" >" +
							     "<a onclick=\"document.all['" + pagingTag.getPagingProperty() + ".dateType'].value='"+ dateFormat + "';document.all['" + pagingTag.getPagingProperty() + ".sortField'].value='"+ property+ "'; document.all['" + pagingTag.getPagingProperty() + ".sortType'].value='"+ PagingVO.ASC_SORT+ "'; document."+ pagingTag.getFormName()+ ".submit();\" ><span style=\"font-family:Webdings;font-size:10pt;text-decoration:none;cursor:pointer;\" class=\"" + rowTag.getHeaderStyle() + "\">5</span></a>" +
							     "<a onclick=\"document.all['" + pagingTag.getPagingProperty() + ".dateType'].value='"+ dateFormat+ "';document.all['" + pagingTag.getPagingProperty() + ".sortField'].value='"+ property+ "'; document.all['" + pagingTag.getPagingProperty() + ".sortType'].value='"+ PagingVO.DESC_SORT+ "'; document."+ pagingTag.getFormName()+ ".submit();\" ><span style=\"font-family:Webdings;font-size:10pt;text-decoration:none;cursor:pointer;\" class=\"" + rowTag.getHeaderStyle() + "\">6</span></a>" +
							     "</td>" +
							     "</tr></table></th>";
					pageContext.getOut().write(str);	
				}else {	
					pageContext.getOut().write(title + "</th>");
				}
			}
			else {
				pageContext.getOut().write("</td>");
			}
		} catch (Exception e) {
			throw new JspException(e.getMessage());
		}
		return EVAL_PAGE;
	}

}
