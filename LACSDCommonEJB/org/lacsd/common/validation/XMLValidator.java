package org.lacsd.common.validation;

import java.io.File;

import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.Validator;

import org.apache.xerces.jaxp.validation.XMLSchemaFactory;

/******************************************************************************
//* Copyright (c) 2011 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		XMLValidator.java
//* Revision: 		1.0
//* Author:			ASRIROCHANAKUL@LACSD.ORG
//* Created On: 	5-4-2011
//* Modified By:	
//* Modified On:	
//*					
//* Description:	A validator object for XML document
/******************************************************************************/

public class XMLValidator {

private static XMLValidator _INSTANCE = new XMLValidator();

public static final String JAXP_SCHEMA_LANGUAGE	= "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
public static final String JAXP_SCHEMA_SOURCE 	= "http://java.sun.com/xml/jaxp/properties/schemaSource";
public static final String W3C_XML_SCHEMA 		= "http://www.w3.org/2001/XMLSchema";

/**
 * Constructor - XMLValidator
 */
private XMLValidator() {
	super();
}

/**
 * Return Singleton Instance
 * @return XMLValidator
 */
public static XMLValidator getInstance() {
	return _INSTANCE;
}

/**
 * Validate XML against XSDs
 * @param xmlFilePath
 * @param xsdFiles
 * @return boolean isValid
 * @throws Exception
 */
public boolean validateAgainstXSD(String xmlFilePath, String[] xsdFiles) throws Exception {

	XMLSchemaFactory xmlSchemaFactory = new XMLSchemaFactory();
	StreamSource[] xsds = new StreamSource[xsdFiles.length];
	
	for (int i=0; i<xsdFiles.length; i++) {
		xsds[i] = new StreamSource(this.getClass().getResourceAsStream(xsdFiles[i]), xsdFiles[i]);
	}

	Schema schema = xmlSchemaFactory.newSchema(xsds);      
	Validator validator = schema.newValidator();
	try {
		validator.validate(new StreamSource(new File(xmlFilePath)));
		return true;
	} catch (Exception ex) {
		ex.printStackTrace();
		return false;
	}
}

public static void main(String[] args) throws Exception {
	
	XMLValidator v = XMLValidator.getInstance();
	String xmlFilePath = "C:/CalPers/bad-sample.xml";
	String[] xsdFiles = { 	"C:/CalPers/CommonUtilitiesV1.xsd",
							"C:/CalPers/PayrollRetirementV1.xsd",
							"C:/CalPers/SoapEnvelope.xsd"
						};

	boolean isValid = v.validateAgainstXSD(xmlFilePath, xsdFiles);

	System.out.println("isValid = " + isValid);
}

}