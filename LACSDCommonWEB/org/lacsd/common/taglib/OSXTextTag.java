package org.lacsd.common.taglib;

/******************************************************************************
//* Copyright (c) 2004 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		OSXTextTag.java
//* Revision: 		1.0
//* Author:			dyip@lacsd.org
//* Created On: 	08/11/2004
//* Modified By:	
//* Modified On:	
//*					
//* Description:	An OSX tag for input text field
/******************************************************************************/

import javax.servlet.jsp.JspException;

import org.apache.struts.taglib.html.TextTag;

public class OSXTextTag extends TextTag {

private static final long serialVersionUID = -98582718693290520L;
// osx tag attributes
private String id = "";

	
/**
 * Constructor
 */	
public OSXTextTag() {
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
