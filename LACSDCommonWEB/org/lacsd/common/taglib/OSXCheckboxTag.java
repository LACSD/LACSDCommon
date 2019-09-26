package org.lacsd.common.taglib;

/******************************************************************************
//* Copyright (c) 2004 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		OSXCheckboxTag.java
//* Revision: 		1.0
//* Author:			dyip@lacsd.org
//* Created On: 	08/12/2004
//* Modified By:	
//* Modified On:	
//*					
//* Description:	An OSX tag for checkbox
/******************************************************************************/

import javax.servlet.jsp.JspException;

import org.apache.struts.taglib.html.CheckboxTag;

public class OSXCheckboxTag extends CheckboxTag {
	
/**
	 * 
	 */
	private static final long serialVersionUID = 7253598163558758208L;
// osx tag attributes
private String id = "";

	
/**
 * Constructor
 */	
public OSXCheckboxTag() {
	super();
}

/**
 * Generate the required input tag.
 * <p>
 * Support for indexed property since Struts 1.1
 *
 * @exception JspException if a JSP exception has occurred
 */
public int doStartTag() throws JspException {
	
	OSXTagManager otm = OSXTagManager.getInstance();
	otm.processTag(pageContext.getSession(),this);
	return super.doStartTag();
}



/**
 * Returns the id.
 * @return String
 */
public String getId() {
	return id;
}

/**
 * Sets the id.
 * @param id The id to set
 */
public void setId(String id) {
	this.id = id;
}

}
