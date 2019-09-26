package org.lacsd.common.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.jsp.JspWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.lang3.StringEscapeUtils;
import org.lacsd.common.exceptions.LACSDException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/******************************************************************************
//* Copyright (c)2017 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:      LACSDXml.java
//* Revision:	   1.0
//* Author:         hho@lacsd.org
//* Created On:     May 10, 2017
//* Modified By:
//* Modified On:
//*                 
//* Description: process XML from a variety of sources and write the transformation output to a variety of sinks.
/******************************************************************************/

public class LACSDXml {
	private  String currentDate = new SimpleDateFormat("yyyyMMddHH_mm_ss").format(new Date());
	private  static LACSDXml _INSTANCE = new LACSDXml();
	private LACSDFileIO fileIO = LACSDFileIO.getInstance();
	/**
	 * Private Constructor - Singleton Pattern
 	 */
    private LACSDXml() {
        super();
    }

	/**
	 * Return Singleton Instance
	 * @return LACSDXml
	 */
    public  static LACSDXml getInstance() {
        return _INSTANCE;
    }
    
    /**
	* Tranform an XML Node  to html using XSLT tranformer and write output to jsp file.
	* @param node Node
	* @param xmlFileName path to xsl file name
	* @param out JspWriter to display html result on jsp file
	*/
	public  boolean transformToHTML(Node node, String xslFileName, JspWriter out) throws LACSDException {
		boolean success= false;
		try {
			if (node != null) {  
				StreamSource xsl= new StreamSource(new File(xslFileName));
				StreamResult result= new StreamResult(out);
				TransformerFactory factory= TransformerFactory.newInstance();
				Transformer transformer= factory.newTransformer(xsl);
				transformer.setOutputProperty(OutputKeys.ENCODING, "iso-8859-1");
				transformer.setOutputProperty(OutputKeys.METHOD, "html");
				transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "3");
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");
				DOMSource source= new DOMSource(node);
				transformer.transform(source, result);
				success= true;
			} else
				success= false;
		} catch (TransformerException ee) {
			success= false;
			throw new LACSDException("Error in LACSDXml - transformToHTML: " + ee.toString());
		}
		return success;
	}

	/**
	  * Tranform an XML Node  to html using XSLT tranformer and write output to jsp file.
	  * @param node Node
	  * @param xmlFileName path to xsl file name
	  * @param out PrintWriter to display html result on jsp file
	  */
	public  boolean transformToHTML(Node node, String xslFileName, PrintWriter out) throws LACSDException {
		boolean success= false;
		try {
			if (node != null) {
				StreamSource xsl= new StreamSource(new File(xslFileName));
				StreamResult result= new StreamResult(out);
				TransformerFactory factory= TransformerFactory.newInstance();
				Transformer transformer= factory.newTransformer(xsl);
				transformer.setOutputProperty(OutputKeys.ENCODING, "iso-8859-1");
				transformer.setOutputProperty(OutputKeys.METHOD, "html");
				transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "3");
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");
				DOMSource source= new DOMSource(node);
				transformer.transform(source, result);
				success= true;
			} else
				success= false;
		} catch (TransformerException ee) {
			success= false;
			throw new LACSDException("Error in LACSDXml - transformToHTML: " + ee.toString());
		}
		return success;
	}

	/**
	* Tranform an XML document (DOM format) to html using XSLT tranformer and write output to jsp file.
	* @param xmlFileName path to xml file name
	* @param xmlFileName path to xsl file name
	* @param out JspWriter to display html result on jsp file
	*/
	public  boolean transformToHTML(String xmlFileName, String xslFileName, JspWriter out) throws LACSDException {
		boolean success= false;
		try {
			Document doc= getDOM(xmlFileName);
			if (doc != null) {
				StreamSource xsl= new StreamSource(new File(xslFileName));
				StreamResult result= new StreamResult(out);
				TransformerFactory factory= TransformerFactory.newInstance();
				Transformer transformer= factory.newTransformer(xsl);
				transformer.setOutputProperty(OutputKeys.ENCODING, "iso-8859-1");
				transformer.setOutputProperty(OutputKeys.METHOD, "html");
				transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "3");
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");
				DOMSource source= new DOMSource(doc);
				transformer.transform(source, result);
				success= true;
			} else
				success= false;
		} catch (TransformerException ee) {
			success= false;
			throw new LACSDException("Error in LACSDXml - transformToHTML(" + xmlFileName + "): " + ee.toString());
		}
		return success;
	}

	/**
	* Tranform an XML document (DOM format) to html using XSLT tranformer and write output to a new file.
	* @param xmlFileName path to xml file name
	* @param xmlFileName path to xsl file name
	* @param pw PrintWriter to write html output to a new file
	 */
	public  void transformToHTML(String xmlFileName, String xslFileName, PrintWriter  pw) throws LACSDException {
		//boolean success= false;
		try {
			Document doc= getDOM(xmlFileName);
			if (doc != null) {
				StreamSource xsl= new StreamSource(new File(xslFileName));
				StreamResult result= new StreamResult(pw);
				TransformerFactory factory= TransformerFactory.newInstance();
				Transformer transformer= factory.newTransformer(xsl);
				transformer.setOutputProperty(OutputKeys.METHOD, "html");
				transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "3");
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");
				DOMSource source= new DOMSource(doc);
				transformer.transform(source, result);
			} 
		} catch (TransformerException ee) {
			throw new LACSDException("Error in LACSDXml - transformToHTML(" + xmlFileName + "): " + ee.toString());
		}
	}

	/**
	* Transform DOM to XML file
	* @param xmlFile path to xml file
	*/
	public  boolean transformToXML(Document doc, String xmlFile) throws LACSDException {
		boolean success= false;
		try {
			if (doc != null) {
				TransformerFactory tFactory= TransformerFactory.newInstance();
				Transformer transformer= tFactory.newTransformer();
				transformer.setOutputProperty(OutputKeys.METHOD, "xml");
				transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");
				DOMSource source= new DOMSource(doc);
				StreamResult result= new StreamResult(new PrintWriter(new BufferedWriter(new FileWriter(xmlFile))));
				transformer.transform(source, result);
				success= true;
			} else
				success= false;
		} catch (Exception ee) {
			success= true;
			throw new LACSDException("Error in LACSDXml - transformToXML: " + ee.toString());
		}
		return success;
	}
	
	/**
	* Transform DOM to XML file
	* @param xmlFile path to xml file
	*/
	public  boolean transformToXML(Document doc, String xmlFile, String tempDir) throws LACSDException {
		boolean success= false;
		String errorMsg = "";
		String fileName = new File(xmlFile).getName();
		String tempFile = tempDir + "/" + fileName.substring(0, fileName.length() - 4) + "_" + currentDate + ".xml" ;
		try {
			if (doc != null) {
				TransformerFactory tFactory= TransformerFactory.newInstance();
				Transformer transformer= tFactory.newTransformer();
				transformer.setOutputProperty(OutputKeys.ENCODING, "iso-8859-1");
				transformer.setOutputProperty(OutputKeys.METHOD, "xml");
				transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");
				DOMSource source= new DOMSource(doc);
				StreamResult result= new StreamResult(new PrintWriter(new BufferedWriter(new FileWriter(tempFile))));
				transformer.transform(source, result);
				success= true;
			} else
				success= false;
		} catch (IOException ioe) {
			success= false;
			errorMsg = ioe.toString();
		} catch (TransformerException e) {
			success= false;
			errorMsg = e.toString();
		}finally {
		    if (success && isWellForm(tempFile)) {
		    	fileIO.copyFile(new File(tempFile), new File(xmlFile));
		    }else {
		    	throw new LACSDException("Error in LACSDXml - transformToXML(" + xmlFile + "): " + errorMsg);
		    }
		}
		
		return success;
	}

	/**
	* Transform DOM to XML file and adding a DOCTYPE declaration
	* @param inputFile path to input xml file
	* @param outoutFile path to output xml file
	* @param xslFile String xsl stylesheet file
	*/
	public  boolean transformToXML(String inputFile, String outputFile, String xslFile) throws LACSDException {
		boolean success= false;
		try {
			Document doc= getDOM(inputFile);
			if (doc != null) {
				TransformerFactory tFactory= TransformerFactory.newInstance();
				Transformer transformer= tFactory.newTransformer(new StreamSource(xslFile));
				transformer.setOutputProperty(OutputKeys.ENCODING, "iso-8859-1");
				transformer.setOutputProperty(OutputKeys.METHOD, "xml");
				transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");
				DOMSource source= new DOMSource(doc);
				StreamResult result= new StreamResult(new PrintWriter(new BufferedWriter(new FileWriter(outputFile))));
				transformer.transform(source, result);
				success = true;
			} else
				success= false;
		} catch (IOException ioe) {
			success= false;
			throw new LACSDException("Error in LACSDXml - transformToXML width adding a DOCTYPE declaration: " + ioe.toString());
		} catch (TransformerException e) {
			success= false;
			throw new LACSDException("Error in LACSDXml - transformToXML width adding a DOCTYPE declaration: " + e.toString());
		}
		return success;
	}

	/**
	* Transform DOM to XML file and adding a DOCTYPE declaration
	* @param xmlFile path to xml file
	* @param dtd path to dtd file
	*/
	public  boolean transformToXML(Document doc, String xmlFile, String dtd, String xslFile, String tempDir) throws LACSDException {
		boolean success= false;
		String fileName = new File(xmlFile).getName();
		String tempFile = tempDir + "/" + fileName.substring(0, fileName.length() - 4) + "_" + currentDate + ".xml" ;
		String errorMsg = "";
		try {
			if (doc != null) {
				removeAll(doc, Node.TEXT_NODE, null);
				doc.getDocumentElement().normalize();
				TransformerFactory tFactory= TransformerFactory.newInstance();
				Transformer transformer= tFactory.newTransformer(new StreamSource(xslFile));
				transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, dtd);
                transformer.setOutputProperty(OutputKeys.ENCODING, "iso-8859-1");
				transformer.setOutputProperty(OutputKeys.METHOD, "xml");
				transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");
				DOMSource source= new DOMSource(doc);
				StreamResult result= new StreamResult(new PrintWriter(new BufferedWriter(new FileWriter(tempFile))));
				transformer.transform(source, result);
				success= true;
			} else
				success= false;
		} catch (IOException ioe) {
			success= false;
			errorMsg = ioe.toString();
		} catch (TransformerException ee) {
			success= false;
			errorMsg = ee.toString();
		}finally {
		    if (success && isWellForm(tempFile)) {
		    	fileIO.copyFile(new File(tempFile), new File(xmlFile));
		    }else {
		    	throw new LACSDException("Error in LACSDXml - transformToXML width adding a DOCTYPE declaration: " + errorMsg);
		    }
		}
		return success;
	}

	/**
	* Transform DOM to XML file and adding a DOCTYPE declaration
	* @param xmlFile path to xml file
	* @param dtd path to dtd file
	*/
	public  boolean transformToXML(Document doc, String xmlFile,String xslFile, String tempDir) throws LACSDException {
		boolean success= false;
		String errorMsg = "";
		String fileName = new File(xmlFile).getName();
		String tempFile = tempDir + "/" + fileName.substring(0, fileName.length() - 4) + "_" + currentDate + ".xml" ;
		try {
			if (doc != null) {
				removeAll(doc, Node.TEXT_NODE, null);
				doc.getDocumentElement().normalize();
				TransformerFactory tFactory= TransformerFactory.newInstance();
				Transformer transformer= tFactory.newTransformer(new StreamSource(xslFile));
				transformer.setOutputProperty(OutputKeys.ENCODING, "iso-8859-1");
				transformer.setOutputProperty(OutputKeys.METHOD, "xml");
				transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");
				DOMSource source= new DOMSource(doc);
				StreamResult result= new StreamResult(new PrintWriter(new BufferedWriter(new FileWriter(tempFile))));
				transformer.transform(source, result);
				success= true;
			} else
				success= false;
		} catch (IOException ioe) {
			success= false;
			errorMsg = ioe.toString();
		} catch (TransformerException ee) {
			success= false;
			errorMsg = ee.toString();
		}finally {
		    if (success && isWellForm(tempFile)) {
		    	fileIO.copyFile(new File(tempFile), new File(xmlFile));
		    }else {
		    	throw new LACSDException("Error in LACSDXml - transformToXML : " + errorMsg);
		    }
		}
		return success;
	}
	
	/**
	* Return DOM from given XML file
	* @param xmlFile path to xml file
	*/
	public  Document getDOM(String xmlFile) throws LACSDException{
		Document doc = null;
		try {
			DocumentBuilderFactory dbf= DocumentBuilderFactory.newInstance();
			dbf.setValidating(false);
			dbf.setNamespaceAware(true);
			DocumentBuilder db= dbf.newDocumentBuilder();
			doc= db.parse(xmlFile);
		} catch (Exception e) {
		   throw new LACSDException("Error occurs in getDOM: " + e.getMessage());
		}
		return doc;
	}

	public  void removeAll(Node node, short nodeType, String name) {
		if (node.getNodeType() == nodeType && (name == null || node.getNodeName().equals(name))) {
			node.getParentNode().removeChild(node);
		} else {
			// Visit the children
			NodeList list= node.getChildNodes();
			for (int i= 0; i < list.getLength(); i++) {
				removeAll(list.item(i), nodeType, name);
			}
		}
	}
	public  void removeAll(Node node) {
		if (node.getNodeType() == Node.TEXT_NODE) {
			node.getParentNode().removeChild(node);
		} else {
			NodeList list= node.getChildNodes();
			for (int i= 0; i < list.getLength(); i++) {
				removeAll(list.item(i));
			}
		}
	}
	public  Document toDocument(ResultSet rs) throws LACSDException {
	    Document doc = null;
		try {
		    DocumentBuilderFactory factory= DocumentBuilderFactory.newInstance();
			DocumentBuilder builder= factory.newDocumentBuilder();
			doc = builder.newDocument();
			Element results= doc.createElement("root");
			doc.appendChild(results);
			ResultSetMetaData rsmd= rs.getMetaData();
			int colCount= rsmd.getColumnCount();
			while (rs.next()) {
				Element row= doc.createElement("row");
				for (int i= 1; i <= colCount; i++) {
					String columnName= rsmd.getColumnName(i);
					Object value= rs.getObject(i);
					String sValue= (value == null ? "" : value.toString());
					row.setAttribute(columnName, sValue);
				}
				results.appendChild(row);
			}
			rs.close();

		} catch (SQLException sqle) {
			throw new LACSDException("Error in LACSDXml - toDocument: " + sqle.toString());
		} catch (Exception e) {
			throw new LACSDException("Error in LACSDXml - toDocument: " + e.toString());
		}
		return doc;
	}

	/**
	 * Convert xml string to DOM
	 * @param xmlString
	 * @return
	 * @throws LACSDException
	 */
	public  Document toDocument(String xmlString) throws LACSDException {
	    Document doc = null;
	    try {
	    	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    	factory.setValidating(true);
	    	factory.setNamespaceAware(true);
	    	DocumentBuilder builder = factory.newDocumentBuilder();
	    	doc= builder.parse(new InputSource(new StringReader(xmlString)));
	    } catch (Exception e) {
			throw new LACSDException("Error in LACSDXml - toDocument: " + e.toString());
		}
		return doc;
	}
	
	/**
	 * Check if XML file is valid
	 * @param xmlString
	 * @return
	 * @throws LACSDException
	 */
	public  boolean isWellForm(String xmlString) throws LACSDException {
	    boolean result = true;
	    try {
	    	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    	factory.setValidating(false);
	    	factory.setNamespaceAware(true);
	    	factory.setFeature("http://xml.org/sax/features/namespaces", false);
	    	factory.setFeature("http://xml.org/sax/features/validation", false);
	    	factory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
	    	factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

	    	DocumentBuilder builder = factory.newDocumentBuilder();

	    	Document document = builder.parse(new InputSource(xmlString));
	    	
	    	if (document == null) {
	    		result = false;
	    	}
	    } catch (Exception e) {
			result = false;
		}
		return result;
	}

	public  String toXMLString(ResultSet rs) throws LACSDException {
		String xml= "";
		try {
			ResultSetMetaData rsmd= rs.getMetaData();
			int colCount= rsmd.getColumnCount();
			while (rs.next()) {
				xml += "<row ";
				for (int i= 1; i <= colCount; i++) {
					String columnName= rsmd.getColumnName(i);
					Object value= rs.getObject(i);
					String sValue= (value == null ? "" : value.toString().trim());
					xml += " " + columnName + "=\"" + StringEscapeUtils.escapeXml10(sValue) + "\"";
				}
				xml += " />";
			}
			rs.close();
		}catch(SQLException e) {
		    throw new LACSDException("Could not conver XML dataset to String: " + e.getMessage());
		}
		return xml;
	}
	
	public  void removeChild(Node parentNode, Node childNode){
		parentNode.removeChild(childNode);
		removeAll(parentNode, Node.TEXT_NODE, null);
	}
}
