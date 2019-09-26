package org.lacsd.common.values;

/******************************************************************************
//* Copyright (c) 2006 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:      MailVO.java
//* Revision:	   1.0
//* Author:         hho@lacsd.org
//* Created On:     Mar 7, 2006
//* Modified By:
//* Modified On:
//*                 
//* Description: 
/******************************************************************************/

public class MailVO implements java.io.Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1894074968496238732L;
	private String sender = "";
	private String subject = "";
	private String body = "";
	private String toList = "";//emails must separate by semicolon ";"
	private String ccList = "";//emails must separate by semicolon ";"
	private String bccList = "";//emails must separate by semicolon ";"
	private String attachmentFiles[];
	
	/**
	 * @return String bccList
	 */
	public String getBccList() {
		return bccList;
	}

	/**
	 * @return String body
	 */
	public String getBody() {
		return body;
	}

	/**
	 * @return String ccList
	 */
	public String getCcList() {
		return ccList;
	}

	/**
	 * @return String sender
	 */
	public String getSender() {
		return sender;
	}

	/**
	 * @return String subject
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * @return String toList
	 */
	public String getToList() {
		return toList;
	}

	/**
	 * @param bccList String
	 */
	public void setBccList(String bccList) {
		this.bccList = bccList;
	}

	/**
	 * @param body String
	 */
	public void setBody(String body) {
		this.body = body;
	}

	/**
	 * @param ccList String
	 */
	public void setCcList(String ccList) {
		this.ccList = ccList;
	}

	/**
	 * @param sender String
	 */
	public void setSender(String sender) {
		this.sender = sender;
	}

	/**
	 * @param subject String
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * @param string
	 */
	public void setToList(String toList) {
		this.toList = toList;
	}

	/**
	 * @return String[]
	 */
	public String[] getAttachmentFile() {
		return attachmentFiles;
	}

	/**
	 * @param attachmentFiles
	 */
	public void setAttachmentFile(String[] attachmentFiles) {
		this.attachmentFiles = attachmentFiles;
	}

}
