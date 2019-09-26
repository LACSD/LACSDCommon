package org.lacsd.common.taglib;

/******************************************************************************
//* Copyright (c) 2004 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		WriteTag.java
//* Revision: 		1.1
//* Author:			ASRIROCHANAKUL@LACSD.ORG
//* Created On: 	2-11-2004
//* Modified By:	
//* Modified On:	
//*					
//* Description:	A custom tag extending <bean:write> tag
//*                 Added functionalities include formatting number, turning
//*                 text red
/******************************************************************************/

import java.math.BigDecimal;

import javax.servlet.jsp.JspException;

import org.apache.struts.taglib.TagUtils;
import org.apache.struts.taglib.bean.WriteTag;
import org.lacsd.common.service.NumberConvertAccounting;

public class WriteTextTag extends WriteTag {

private static final long serialVersionUID = -7333104926933147148L;

private boolean isCredit = false;

// custom tag attributes
private boolean isNumeric = false;
private String negativeSymbol = "-"; // negative signs '-', '()', and 'CR'.  '-' is default
private String prefix = "";
private String suffix = "";

/**
 * Constructor
 */	
public WriteTextTag() {
	super();
}

/**
 * Process the start tag.
 *
 * @exception JspException if a JSP exception has occurred
 */
public int doStartTag() throws JspException {

    // Look up the requested bean (if necessary)
    if (ignore) {
        if (TagUtils.getInstance().lookup(pageContext, name, scope) == null)
            return (SKIP_BODY);  // Nothing to output
    }

    // Look up the requested property value
    Object value = TagUtils.getInstance().lookup(pageContext, name, property, scope);
    if (value == null)
        return (SKIP_BODY);  // Nothing to output

    // Convert value to the String with some formatting
    String output = formatValue( value );
    
    /**
     * MODIFIED CODE
     */
// ORIGINAL CODE
    // Print this property value to our output writer, suitably filtered
//    if (filter)
//        ResponseUtils.write(pageContext, ResponseUtils.filter(output));
//    else
//        ResponseUtils.write(pageContext, output);
    
// MODIFIED
    if (filter)
        output = TagUtils.getInstance().filter(output);
        
    /**
     * END MODIFIED CODE
     */
        
    /**
     * ADDED CODE HERE
     */
    
	if (this.isNumeric) { 
		
		try {
			BigDecimal number = new BigDecimal(output);
			
			if (number.compareTo(new BigDecimal(0.0)) < 0)
				this.isCredit = true;
			
			NumberConvertAccounting nca = NumberConvertAccounting.getInstance();

			if (this.prefix.equals(NumberConvertAccounting.SIGN_DOLLAR)) {
				if (this.negativeSymbol.equalsIgnoreCase(NumberConvertAccounting.SIGN_PAREN_LEFT))
					output = nca.getCurrencyWithParen(output);
				else if (this.negativeSymbol.equalsIgnoreCase(NumberConvertAccounting.SIGN_CR))
					output = nca.getCurrencyWithCR(output);
				else // default to minus sign
					output = nca.getCurrencyWithMinusSign(output);
			}
			else {
				if (this.negativeSymbol.equalsIgnoreCase(NumberConvertAccounting.SIGN_PAREN_LEFT))
					output = nca.getNumberWithParen(output);
				else if (this.negativeSymbol.equalsIgnoreCase(NumberConvertAccounting.SIGN_CR))
					output = nca.getNumberWithCR(output);
				else
					output = nca.getNumberWithMinusSign(output);
			}
			
			if (this.isCredit)
				output = "<span style='color:#ff0000;'>" + output + "</span>";
		}
		catch(Exception ex) {
			// value is not a number, do nothing.
		}
	}

	if (this.prefix.length() > 0 && ! this.prefix.equals("$"))
		output = this.prefix + output;
	if (this.suffix.length() > 0)
		output = output + this.suffix;
    
    /**
     * END ADDED CODE
     */
	TagUtils.getInstance().write(pageContext, output);

    // Continue processing this page
    return (SKIP_BODY);
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
