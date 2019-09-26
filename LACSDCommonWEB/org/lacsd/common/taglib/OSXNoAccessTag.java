package org.lacsd.common.taglib;

/******************************************************************************
//* Copyright (c) 2004 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		OSXNoAccessTag.java
//* Revision: 		1.0
//* Author:			dyip@lacsd.org
//* Created On: 	08/13/2004
//* Modified By:	
//* Modified On:	
//*					
//* Description:	An OSX tag for displaying wrapped "no access" html content
/******************************************************************************/

import javax.servlet.jsp.JspException;

public class OSXNoAccessTag extends OSXAccessTag {
	
/**
	 * 
	 */
	private static final long serialVersionUID = -552884204136898550L;


/**
 * Constructor
 */	
public OSXNoAccessTag() {
	super();
}


/**
 * Perform the test required for this particular tag, and either evaluate
 * or skip the body of this tag.
 *
 * @exception JspException if a JSP exception occurs
 */
public int doStartTag() throws JspException {
	
	super.show = !super.show; // opposite
	return super.doStartTag();

}

}
