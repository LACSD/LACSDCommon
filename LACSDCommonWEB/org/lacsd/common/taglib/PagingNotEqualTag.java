package org.lacsd.common.taglib;
/******************************************************************************
//* Copyright (c) 2011 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		PagingNotEqualTag.java
//* Revision: 		1.0
//* Author:			hho@lacsd.org
//* Created On: 	Sep 1, 2011
//* Modified By:    
//* Modified On:    
//* 
//* Description:	
/******************************************************************************/

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.beanutils.BeanUtils;

public class PagingNotEqualTag extends TagSupport{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5649903852747920381L;
	private String value;
	private String property;
	
	public void setValue(String value) {
		this.value= value;
	}

	public void setProperty(String property) {
		this.property = property;
	}
	
	public int doStartTag() throws JspException {
		int eval= 0;
		try {
			PagingTableTag pagingTag= (PagingTableTag) findAncestorWithClass(this, PagingTableTag.class);
			Object refObj= pagingTag.getRefObj();
			String fieldValue = "null";
			if (BeanUtils.getProperty(refObj, property)!= null) {
			    fieldValue = BeanUtils.getProperty(refObj, property);
			}
			
			if (fieldValue.trim().equals(value.trim())) {
				eval= SKIP_BODY;
			} else {
				eval= EVAL_BODY_INCLUDE ;
			}
		} catch (Exception e) {
			throw new JspException(e.getMessage());
		}
		return eval;
	}

	public int doEndTag() throws JspException {
		return EVAL_PAGE;
	}	
}

