package org.lacsd.common.struts2.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

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

public class PagingRowTag extends TagSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5019872767554415464L;
	private PagingTableTag pagingTag;
	private String headerStyle;
	private String evenRowStyle;
	private String oddRowStyle;
	private String htmlAttribute;
	private int rowNumber;

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

	public String getHtmlAttribute() {
		return htmlAttribute;
	}

	public void setHtmlAttribute(String htmlAttribute) {
		this.htmlAttribute = htmlAttribute;
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
			rowNumber= pagingTag.getIndex();
			if (pagingTag.isExtraRow()) {
				if (rowNumber > 0) {
					if (rowNumber % 2 == 0)
						pageContext.getOut().println("<tr " + (evenRowStyle==null?"":"class=\"" +evenRowStyle+ "\"")+ " "+ (htmlAttribute==null?"":htmlAttribute) +">");
					else
						pageContext.getOut().println("<tr " + (oddRowStyle ==null?"":"class=\"" +oddRowStyle+ "\"")+ " "+ (htmlAttribute==null?"":htmlAttribute) +">");
				} else {
					return SKIP_BODY;
				}
			}else {
			    if (rowNumber == 0)
					pageContext.getOut().println("<tr " +(headerStyle==null?"":"class=\"" + headerStyle + "\"") + ">");
				else if (rowNumber % 2 == 0)
					pageContext.getOut().println("<tr " + (evenRowStyle==null?"":"class=\"" +evenRowStyle+ "\"")+ " "+ (htmlAttribute==null?"":htmlAttribute) +">");
				else
					pageContext.getOut().println("<tr " + (oddRowStyle ==null?"":"class=\"" +oddRowStyle+ "\"")+ " "+ (htmlAttribute==null?"":htmlAttribute) +">");
			}

		} catch (Exception e) {
			throw new JspException(e.getMessage());
		}
		return EVAL_BODY_INCLUDE;
	}

	

	public int doEndTag() throws JspException {
		try {
			if (pagingTag.isExtraRow() ) {
				if (pagingTag.getIndex() > 0) {
					pageContext.getOut().println("</tr>");
				}
			}else {
				if (pagingTag.isScrollable() && rowNumber == 0) {
					int numColums = pagingTag.getRefObj().getClass().getDeclaredFields().length;
					pageContext.getOut().println("</tr>");
					pageContext.getOut().println("<tr><td colspan=\""+ numColums + "\"><div id=\"scrollable_"+pagingTag.getProperty()+"\" style=\"overflow:auto;height:" + pagingTag.getScrollHeight() + "px;\"><table width=\"100%\">");
				}else {
					pageContext.getOut().println("</tr>");
				}
			}
			pagingTag.setExtraRow(true);
		} catch (Exception e) {
			throw new JspException(e.getMessage());
		}
		return EVAL_PAGE;
	}

	
}
