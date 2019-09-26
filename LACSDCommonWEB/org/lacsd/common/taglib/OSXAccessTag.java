package org.lacsd.common.taglib;

/******************************************************************************
//* Copyright (c) 2004 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		OSXAccessTag.java
//* Revision: 		1.0
//* Author:			dyip@lacsd.org
//* Created On: 	08/13/2004
//* Modified By:	
//* Modified On:	
//*					
//* Description:	An OSX tag for displaying wrapped "access" html content
/******************************************************************************/

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.lacsd.common.security.SecurityRoleMap;

public class OSXAccessTag extends TagSupport {
	
/**
	 * 
	 */
	private static final long serialVersionUID = -5574539301060981717L;
// osx tag attributes
private String id = "";
protected boolean show = true;

/**
 * Constructor
 */	
public OSXAccessTag() {
	super();
}

/**
 * Decides to display content or not.
 *
 */
public void processContent()  {
	
	OSXTagManager otm = OSXTagManager.getInstance();
	String roleID = otm.getRoleIDfromSession(pageContext.getSession());
	SecurityRoleMap map = SecurityRoleMap.getInstance();
	
	String key = this.getId() + roleID;
	String resp = (String) map.getSecurityFieldIDRoleMap().get(key);


    if (resp != null && resp.equals("disable")) {
    	show = !show;
    }
}
/**
 * Perform the test required for this particular tag, and either evaluate
 * or skip the body of this tag.
 *
 * @exception JspException if a JSP exception occurs
 */
public int doStartTag() throws JspException {

	this.processContent();

    if (!show) {
        return (SKIP_BODY);
    }   else {
        return (EVAL_BODY_INCLUDE);
    }

}


    /**
 * Evaluate the remainder of the current page normally.
 *
 * @exception JspException if a JSP exception occurs
 */
public int doEndTag() throws JspException {

    return (EVAL_PAGE);

}


/**
 * Release all allocated resources.
 */
public void release() {

	show = true;
	id = "";
    super.release();
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
