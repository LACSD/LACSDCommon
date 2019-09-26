package org.lacsd.common.struts2.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringEscapeUtils;

/******************************************************************************
//* Copyright (c) 2011 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		PagingPropertyTag.java
//* Revision: 		1.0
//* Author:			hho@lacsd.org
//* Created On: 	Apr 19, 2011
//* Modified By:    
//* Modified On:    
//* 
//* Description:	
/******************************************************************************/

public class PagingPropertyTag extends TagSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8362516577129391579L;
	private String value;
	private  boolean URLEncode= false;
	private boolean escapeHtml = false;
	private boolean escapeJavaScript = false;
	
	/**
	 * Return the value
	 * @return String
	 */
	public String getValue() {
		return value;
	}
	/**
	 * Set the value
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
	
	/**
	 * Return the URLEncode
	 * @return boolean
	 */
	public boolean isURLEncode() {
		return URLEncode;
	}
	/**
	 * Set the URLEncode
	 * @param encode the uRLEncode to set
	 */
	public void setURLEncode(boolean encode) {
		URLEncode = encode;
	}
	
	/**
	 * Return the escapeHtml
	 * @return boolean
	 */
	public boolean isEscapeHtml() {
		return escapeHtml;
	}
	/**
	 * Set the escapeHtml
	 * @param escapeHtml the escapeHtml to set
	 */
	public void setEscapeHtml(boolean escapeHtml) {
		this.escapeHtml = escapeHtml;
	}
	/**
	 * Return the escapeJavaScript
	 * @return boolean
	 */
	public boolean isEscapeJavaScript() {
		return escapeJavaScript;
	}
	/**
	 * Set the escapeJavaScript
	 * @param escapeJavaScript the escapeJavaScript to set
	 */
	public void setEscapeJavaScript(boolean escapeJavaScript) {
		this.escapeJavaScript = escapeJavaScript;
	}
	public int doEndTag() throws JspException {
		try {
			PagingTableTag pagingTag= (PagingTableTag) findAncestorWithClass(this, PagingTableTag.class);
			Object refObj= pagingTag.getRefObj();
			String fieldValue = BeanUtils.getProperty(refObj, value);
			if (refObj == null)
				throw new JspTagException("Cannot find any data");
			if (URLEncode)
				pageContext.getOut().write((fieldValue==null?"":java.net.URLEncoder.encode(fieldValue.trim(),"UTF-8")));
			else if (escapeHtml)
				pageContext.getOut().write((fieldValue==null?"":StringEscapeUtils.escapeHtml3(fieldValue.trim())));
			else if (escapeJavaScript)
				pageContext.getOut().write((fieldValue==null?"":StringEscapeUtils.escapeEcmaScript(fieldValue.trim())));
			else
				pageContext.getOut().write((fieldValue==null?"":fieldValue.trim()));

		} catch (Exception e) {
			throw new JspException(e.getMessage());
		}
		return SKIP_BODY;
	}
}
