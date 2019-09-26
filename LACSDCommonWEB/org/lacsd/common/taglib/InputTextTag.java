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

import java.math.BigDecimal;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import org.apache.struts.taglib.TagUtils;
import org.apache.struts.taglib.html.TextTag;
import org.lacsd.common.authentication.UserProfile;
import org.lacsd.common.constants.LACSDWebConstants;
import org.lacsd.common.security.SecurityRoleMap;
import org.lacsd.common.service.NumberConvertAccounting;
import org.lacsd.common.validation.LACSDValidator;

public class InputTextTag extends TextTag {
	
/**
	 * 
	 */
	private static final long serialVersionUID = 7754191727020738510L;

private boolean isCredit = false;

// custom tag attributes
private boolean isNumeric = false;
private String negativeSymbol = "-"; // negative signs '-', '()', and 'CR'.  '-' is default
private String prefix = "";
private String suffix = "";

	
/**
 * Constructor
 */	
public InputTextTag() {
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

    // Create an appropriate "input" element based on our parameters
    StringBuffer results = new StringBuffer("<input type=\"");
    results.append(type);
    results.append("\" name=\"");
    // * @since Struts 1.1
    if (indexed)
        prepareIndex(results, name);
    results.append(property);
    results.append("\"");
    if (accesskey != null) {
        results.append(" accesskey=\"");
        results.append(accesskey);
        results.append("\"");
    }
    if (accept != null) {
        results.append(" accept=\"");
        results.append(accept);
        results.append("\"");
    }
    if (maxlength != null) {
        results.append(" maxlength=\"");
        results.append(maxlength);
        results.append("\"");
    }
    if (cols != null) {
        results.append(" size=\"");
        results.append(cols);
        results.append("\"");
    }
    if (tabindex != null) {
        results.append(" tabindex=\"");
        results.append(tabindex);
        results.append("\"");
    }
    
/* ADDED CODE FOR SECURITY ROLE */
	HttpSession session = pageContext.getSession();
	UserProfile userProfile = (UserProfile)session.getAttribute(LACSDWebConstants.USER_PROFILE);
	int roleID = Integer.parseInt(userProfile.getEmployeeVO().getSecurityRoleID());

	SecurityRoleMap map = SecurityRoleMap.getInstance();
	int rule = map.getSecurityRoleRule(roleID);

	if (rule == 0) {
		// normal, do nothing
	}
	
	if (rule == 1 && ! this.getReadonly()) {
		// read only
		results.append(" readonly=\"readonly\" class=\"readOnly\"");
	}		
/* END ADDED CODE */

    results.append(" value=\"");
    if (value != null) {    	
        results.append(TagUtils.getInstance().filter(value));
    } else if (redisplay || !"password".equals(type)) {
        Object value = TagUtils.getInstance().lookup(pageContext, name, property, null);
        if (value == null)
            value = "";
            
		/* ADDED CODED - formatting number
		 */
		 
		else {			
			if (this.isNumeric) { 
				try {
					BigDecimal number = new BigDecimal(value.toString());
					
					if (number.compareTo(new BigDecimal(0.0)) < 0)
						this.isCredit = true;
					
					NumberConvertAccounting nca = NumberConvertAccounting.getInstance();

					if (this.prefix.equals(NumberConvertAccounting.SIGN_DOLLAR)) {
						LACSDValidator v = LACSDValidator.getInstance();
						if (v.isCurrency(value.toString(), true)) {
							if (this.negativeSymbol.equalsIgnoreCase(NumberConvertAccounting.SIGN_PAREN_LEFT))
								value = nca.getCurrencyWithParen(value.toString());
							else if (this.negativeSymbol.equalsIgnoreCase(NumberConvertAccounting.SIGN_CR))
								value = nca.getCurrencyWithCR(value.toString());
							else // default to minus sign
								value = nca.getCurrencyWithMinusSign(value.toString());
						} else {
							this.isCredit = false;
						}
					}
					else {
						if (this.negativeSymbol.equalsIgnoreCase(NumberConvertAccounting.SIGN_PAREN_LEFT))
							value = nca.getNumberWithParen(value.toString());
						else if (this.negativeSymbol.equalsIgnoreCase(NumberConvertAccounting.SIGN_CR))
							value = nca.getNumberWithCR(value.toString());
						else 
							value = nca.getNumberWithMinusSign(value.toString());
					}
				}
				catch(Exception ex) {
					// value is not a number, do nothing.
				}
			}
		}
		
		if (this.prefix.length() > 0 && ! this.prefix.equals("$"))
			value = this.prefix + value;
		if (this.suffix.length() > 0)
			value = value + this.suffix;
		/* END ADDED CODE */
            
        results.append(TagUtils.getInstance().filter(value.toString()));
    }    
    results.append("\"");

	/* CURRENCY FORMAT (for accounting)
	 * if amount is negative, turn text red
	 */
    if (this.isCredit) {
    	this.setStyle(this.getStyle() + "; color:#ff0000;");
    }
	/* END ADDED CODE */    
    
    results.append(prepareEventHandlers());
    results.append(prepareStyles());
    results.append(getElementClose());

    // Print this field to our output writer
    TagUtils.getInstance().write(pageContext, results.toString());

    // Continue processing this page
    return (EVAL_BODY_INCLUDE);
}

/**
 * Return isNumeric
 * @return boolean isNumeric
 */
public boolean getIsNumeric() {
	return this.isNumeric;
}

/**
 * Return negativeSymbol
 * @return String negativeSymbol
 */
public String getNegativeSymbol() {
	return this.negativeSymbol;
}

/**
 * Return prefix
 * @return String prefix
 */
public String getPrefix() {
	return this.prefix;
}

/**
 * Return suffix
 * @return String suffix
 */
public String getSuffix() {
	return this.suffix;
}

/**
 * Set isNumeric
 * @param boolean isNumeric
 * @return void
 */
public void setIsNumeric(boolean argIsNumeric) {
	this.isNumeric = argIsNumeric;
}

/**
 * Set negativeSymbol
 * @param String negativeSymbol
 * @return void
 */
public void setNegativeSymbol(String argNegativeSymbol) {
	this.negativeSymbol = argNegativeSymbol;
}

/**
 * Set prefix
 * @param String prefix
 * @return void
 */
public void setPrefix(String prefix) {
	this.prefix = prefix;
}

/**
 * Set suffix
 * @param String suffix
 * @return void
 */
public void setSuffix(String suffix) {
	this.suffix = suffix;
}
}
