package org.lacsd.common.taglib;

/******************************************************************************
//* Copyright (c) 2003 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		LabelTag.java
//* Revision: 		1.0
//* Author:			ASRIROCHANAKUL@LACSD.ORG
//* Created On: 	12-29-2003
//* Modified By:	
//* Modified On:	
//*					
//* Description:	A custom tag to write out text field label
/******************************************************************************/

import java.util.Iterator;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.taglib.TagUtils;
import org.apache.struts.taglib.bean.WriteTag;

public class LabelTag extends WriteTag {


private static final long serialVersionUID = 7433730629949710999L;
private String id = "";
private boolean isValid = true;
private String value = "";

/**
 * Write out label.  If invalid, label will be in red.
 * @return int SKIP_BODY
 * @throws JspException
 */
@SuppressWarnings("rawtypes")
public int doStartTag() throws JspException {

	ServletRequest req = pageContext.getRequest();
	
	ActionMessages errors = (ActionMessages) req.getAttribute("org.apache.struts.action.ERROR");
	
	if (errors != null) {
		Iterator it = errors.get();
		
		while (it.hasNext()) {
			
			ActionMessage error = (ActionMessage) it.next();
			
			String strutsErrorLabel = "";
            if (error.getValues() != null) {
                strutsErrorLabel = (error.getValues()[0]) + "";
            }
			
			// If key is "", use label to compare
			String jspErrorLabel = id.length() > 0 ? id : value;
			
			if (jspErrorLabel.equalsIgnoreCase(strutsErrorLabel))
				isValid = false;
		}
	}
		
    if (! isValid)
    	value = "<span style='color: #ff0000;'>" + value + "</span>";

	TagUtils.getInstance().write(pageContext, value);

    // Continue processing this page
    return (SKIP_BODY);
}

/**
 * Return id
 * @return String id
 */
public String getId() {
	return this.id;
}

/**
 * Return isValid
 * @return boolean isValid
 */
public boolean getIsValid() {
	return this.isValid;
}

/**
 * Return value
 * @return String value
 */
public String getValue() {
	return this.value;
}

/**
 * Set id
 * @param String argId
 * @return void
 */
public void setId(String argId) {
	this.id = argId;
}

/**
 * Set isValid
 * @param boolean argIsValid
 * @return void
 */
public void setIsValid(boolean argIsValid) {
	this.isValid = argIsValid;
}

/**
 * Set value
 * @param String argValue
 * @return void
 */
public void setValue(String argValue) {
	this.value = argValue;
}
}
