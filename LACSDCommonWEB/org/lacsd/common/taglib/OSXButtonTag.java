package org.lacsd.common.taglib;

/******************************************************************************
//* Copyright (c) 2004 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		OSXButtonTag.java
//* Revision: 		1.0
//* Author:			dyip@lacsd.org
//* Created On: 	08/12/2004
//* Modified By:	
//* Modified On:	
//*					
//* Description:	An OSX tag for button
/******************************************************************************/

import javax.servlet.jsp.JspException;

import org.apache.struts.taglib.TagUtils;
import org.apache.struts.taglib.html.ButtonTag;

public class OSXButtonTag extends ButtonTag {
	
/**
	 * 
	 */
	private static final long serialVersionUID = 661595202472140310L;
// osx tag attributes
private String id = "";
private String classbutton = "";

/**
 * Constructor
 */	
public OSXButtonTag() {
	super();
}

/**
 * Overwrite the super class method.
 *
 * @exception JspException if a JSP exception has occurred
 */
public int doStartTag() throws JspException {
	
	OSXTagManager otm = OSXTagManager.getInstance();
	otm.processTag(pageContext.getSession(),this);
	return super.doStartTag();
}

/**
 * Overwrite the super class method.
 *
 * @exception JspException if a JSP exception has occurred
 */
public int doEndTag() throws JspException {

    // Acquire the label value we will be generating
    String label = value;
    if ((label == null) && (text != null))
        label = text;
    if ((label == null) || (label.trim().length() < 1))
        label = "Click";

    // Generate an HTML element
    StringBuffer results = new StringBuffer();
    results.append("<input type=\"button\"");
    if (property != null) {
        results.append(" name=\"");
        results.append(property);
        // * @since Struts 1.1
        if( indexed )
            prepareIndex( results, null );
        results.append("\"");
    }
    if (accesskey != null) {
        results.append(" accesskey=\"");
        results.append(accesskey);
        results.append("\"");
    }
    if (tabindex != null) {
        results.append(" tabindex=\"");
        results.append(tabindex);
        results.append("\"");
    }
    results.append(" value=\"");
    results.append(label);
    results.append("\"");
    
    
    // modified code to struts
    results.append(" class=\"");
    results.append(this.classbutton);
    results.append("\"");
    
    results.append(prepareEventHandlers());
    results.append(prepareStyles());
    results.append(getElementClose());

    // Render this element to our writer
    TagUtils.getInstance().write(pageContext, results.toString());

    // Evaluate the remainder of this page
    return (EVAL_PAGE);

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

/**
 * Returns the classbutton.
 * @return String
 */
public String getClassbutton() {
	return classbutton;
}

/**
 * Sets the classbutton.
 * @param classbutton The classbutton to set
 */
public void setClassbutton(String classbutton) {
	this.classbutton = classbutton;
}

}
