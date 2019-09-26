package org.lacsd.common.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

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
	private static final long serialVersionUID = 8600133047886015031L;
	private PagingTableTag pagingTag;
	private String title;
	private String align= "left";
	private String width;
	private String cellStyle;
	private int rowNumber;

	

	/**
	 * Return the pagingTag
	 * @return PagingTag
	 */
	public PagingTableTag getPagingTag() {
		return pagingTag;
	}

	/**
	 * Set the pagingTag
	 * @param pagingTag the pagingTag to set
	 */
	public void setPagingTag(PagingTableTag pagingTag) {
		this.pagingTag = pagingTag;
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
	


	public int doStartTag() throws JspException {
		int eval= 0;
		try {
			pagingTag= (PagingTableTag) findAncestorWithClass(this, PagingTableTag.class);
			PagingRowTag rowTag = (PagingRowTag) findAncestorWithClass(this, PagingRowTag.class);
			rowNumber= pagingTag.getIndex();
			if (rowNumber == 1) {
				pageContext.getOut().write("<th align=\"" + align + "\"  class=\"" + rowTag.getHeaderStyle() + "\"" + (width==null?"":" width=\""+ width+"\"") +">");
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
			if (rowNumber == 1)
				pageContext.getOut().write( title + "</th>");
			else
				pageContext.getOut().write("</td>");
		} catch (Exception e) {
			throw new JspException(e.getMessage());
		}
		return EVAL_PAGE;
	}

}
