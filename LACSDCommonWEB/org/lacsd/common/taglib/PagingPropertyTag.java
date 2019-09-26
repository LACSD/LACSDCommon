package org.lacsd.common.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.beanutils.BeanUtils;

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
	private static final long serialVersionUID = 2782113040339973384L;
	private String value;
	private  boolean URLEncode= false;
	
	
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
	public int doEndTag() throws JspException {
		try {
			PagingTableTag pagingTag= (PagingTableTag) findAncestorWithClass(this, PagingTableTag.class);
			Object refObj= pagingTag.getRefObj();
			String fieldValue = BeanUtils.getProperty(refObj, value);
			if (refObj == null)
				throw new JspTagException("Cannot find any data");
			if (URLEncode)
				pageContext.getOut().write((fieldValue==null?"":java.net.URLEncoder.encode(fieldValue.trim(),"UTF-8")));
			else
				pageContext.getOut().write((fieldValue==null?"":fieldValue.trim()));

		} catch (Exception e) {
			throw new JspException(e.getMessage());
		}
		return SKIP_BODY;
	}
}
