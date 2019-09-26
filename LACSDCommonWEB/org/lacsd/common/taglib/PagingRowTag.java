package org.lacsd.common.taglib;

import java.util.Iterator;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

/******************************************************************************
//* Copyright (c) 2011 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		PagingRowTag.java
//* Revision: 		1.0
//* Author:			hho@lacsd.org
//* Created On: 	Apr 19, 2011
//* Modified By:    
//* Modified On:    
//* 
//* Description:	
/******************************************************************************/

public class PagingRowTag extends BodyTagSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3947757893351167089L;
	private PagingTableTag pagingTag;
	private String headerStyle;
	private String evenRowStyle;
	private String oddRowStyle;
	
	

	/**
	 * Return the headerStyle
	 * @return String
	 */
	public String getHeaderStyle() {
		return headerStyle;
	}

	/**
	 * Set the headerStyle
	 * @param headerStyle the headerStyle to set
	 */
	public void setHeaderStyle(String headerStyle) {
		this.headerStyle = headerStyle;
	}

	/**
	 * Return the evenRowStyle
	 * @return String
	 */
	public String getEvenRowStyle() {
		return evenRowStyle;
	}

	/**
	 * Set the evenRowStyle
	 * @param evenRowStyle the evenRowStyle to set
	 */
	public void setEvenRowStyle(String evenRowStyle) {
		this.evenRowStyle = evenRowStyle;
	}

	/**
	 * Return the oddRowStyle
	 * @return String
	 */
	public String getOddRowStyle() {
		return oddRowStyle;
	}

	/**
	 * Set the oddRowStyle
	 * @param oddRowStyle the oddRowStyle to set
	 */
	public void setOddRowStyle(String oddRowStyle) {
		this.oddRowStyle = oddRowStyle;
	}

	public int doStartTag() throws JspException {
		try {
			pagingTag= (PagingTableTag) findAncestorWithClass(this, PagingTableTag.class);
		} catch (Exception e) {
			throw new JspException(e.getMessage());
		}
		return incrementRow();
	}

	public int doAfterBody() throws JspException {
		try {
			pageContext.getOut().println("</tr>");
		} catch (Exception e) {
			throw new JspException(e.getMessage());
		}
		return incrementRow();

	}

	public int doEndTag() throws JspException {
		try {
			pageContext.getOut().print(bodyContent.getString());
		} catch (Exception e) {
			throw new JspException(e.getMessage());
		}
		return EVAL_PAGE;
	}

	@SuppressWarnings("rawtypes")
	private int incrementRow() throws JspException {
		boolean hasItem= false;
		try {
			int rowNumber= pagingTag.getIndex();
			Iterator iterator= pagingTag.getIterator();
			if (iterator.hasNext()) {
				if (rowNumber == 0)
					pageContext.getOut().println("<tr " +(headerStyle==null?"":"class=\"" + headerStyle + "\"")+">");
				else if (rowNumber % 2 == 0)
					pageContext.getOut().println("<tr " + (evenRowStyle==null?"":"class=\"" +evenRowStyle+ "\"")+">");
				else
					pageContext.getOut().println("<tr " + (oddRowStyle ==null?"":"class=\"" +oddRowStyle+ "\"")+">");
				pagingTag.setRefObj(iterator.next());
				if (rowNumber > 0)
					iterator.remove();
				rowNumber++;
				pagingTag.setIndex(rowNumber);
				hasItem= true;
			} else
				hasItem= false;

		} catch (Exception e) {
			throw new JspException(e.getMessage());
		}
		return (hasItem) ? EVAL_BODY_AGAIN : SKIP_BODY;
	}
}
