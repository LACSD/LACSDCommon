package org.lacsd.common.service;

/******************************************************************************
//* Copyright (c) 2004 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		LACSDEmail.java
//* Revision: 		1.0
//* Author:			M Feinberg
//* Created On: 	03-25-2004
//* Modified by:	Hoa Ho
//* Modified On:	09/15/2015.  Add method to send email with in-memory binary attachment
//*					
//* Description:	Convenience wrapper for LACSD Email Services
/******************************************************************************/
import java.util.Properties;
import java.util.StringTokenizer;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.lacsd.common.constants.LACSDWebConstants;
import org.lacsd.common.exceptions.LACSDException;

public class LACSDEmail {

private static final String HOST_PROP 	= "mail.host";			// for javamail api


private static LACSDEmail _INSTANCE = new LACSDEmail();			// Singleton Instance



/**
 * Return Singleton Instance
 * @return LACSDEmail
*/
public static LACSDEmail getInstance() {
	return _INSTANCE;
}

/**
 * Use JavaMail API to Send Email
 * Local Email resources such as SMTP Server will be derived
 * from Environment_Properties file.
 * @param String[] mailTo - String array of recipient email addresses
 * @param String mailFrom - Sender's Email Address
 * @param String subject - Email Subject Line
 * @param String body - Body Message Text
 * @return void
 * @throws LACSDException
*/
public void sendHTMLMail(String[] mailTo, String mailFrom, String subject, String body) throws LACSDException {
	sendMail(mailTo, mailFrom, subject, body, true);
}

/**
 * Use JavaMail API to Send Email
 * Local Email resources such as SMTP Server will be derived
 * from Environment_Properties file.
 * @param mailTo
 * @param mailCc
 * @param mailFrom
 * @param subject
 * @param body
 * @throws LACSDException
 */
public void sendHTMLMail(String[] mailTo, String[] mailCc, String mailFrom, String subject, String body)throws LACSDException {
	sendMail(mailTo,mailCc,mailFrom, subject, body, true);
}

/**
 * Use JavaMail API to Send Email
 * Local Email resources such as SMTP Server will be derived
 * from Environment_Properties file.
 * @param mailTo
 * @param mailCc
 * @param mailFrom
 * @param subject
 * @param body
 * @throws LACSDException
 */
public void sendMail(String[] mailTo, String[] mailCc, String mailFrom, String subject, String body)throws LACSDException {
	sendMail(mailTo,mailCc,mailFrom, subject, body, false);
}
/**
 * Use JavaMail API to Send Email
 * Local Email resources such as SMTP Server will be derived
 * from Environment_Properties file.
 * @param String[] mailTo - String array of recipient email addresses
 * @param String mailFrom - Sender's Email Address
 * @param String subject - Email Subject Line
 * @param String body - Body Message Text
 * @return void
 * @throws LACSDException
*/
public void sendMail(String[] mailTo, String mailFrom, String subject, String body) throws LACSDException {
	sendMail(mailTo, mailFrom, subject, body, false);
}

/**
 * Use JavaMail API to Send Email
 * Local Email resources such as SMTP Server will be derived
 * from Environment_Properties file.
 * @param String[] mailTo - String array of recipient email addresses
 * @param String mailFrom - Sender's Email Address
 * @param String subject - Email Subject Line
 * @param String body - Body Message Text
 * @return void
 * @throws LACSDException
 */
public void sendMail(String[] mailTo, String mailFrom, String subject, String body, boolean isHTMLMail) throws LACSDException {

	try {
		
		Properties props = new Properties();

		props.put(HOST_PROP, LACSDWebConstants.LACSD_EMAIL_HOST);

        Session mailSession = Session.getInstance(props, null);
 
		Message msg = new MimeMessage(mailSession);
		
		msg.setFrom(new InternetAddress(mailFrom));
		
		InternetAddress[] address = new InternetAddress[mailTo.length];
		for (int i=0; i<address.length; i++) {
			address[i] = new InternetAddress(mailTo[i]);
		}
		
		msg.setRecipients(Message.RecipientType.TO, address);
		
		
		msg.setSubject(subject);
		
		msg.setSentDate(new java.util.Date());
		
		// Feb 19, 2013 added by asrirochanakul 
		// to allow HTML format email
		if (isHTMLMail) {
			msg.setContent(body, "text/html");
		} else {
			msg.setText(body);
		}
	
		Transport.send(msg);
		
	}
	catch (Exception e) {
		throw new LACSDException(e.getMessage());
	}
}

/**
 * Use JavaMail API to Send Email
 * Local Email resources such as SMTP Server will be derived
 * from Environment_Properties file.
 * @param String[] mailTo - String array of recipient email addresses
 * @param String[] mailCc - String array of Cc email addresses
 * @param String mailFrom - Sender's Email Address
 * @param String subject - Email Subject Line
 * @param String body - Body Message Text
 * @return void
 * @throws LACSDException
*/
public void sendMail(String[] mailTo, String[] mailCc, String mailFrom, String subject, String body, boolean isHTMLMail) throws LACSDException {

	try {
		
		Properties props = new Properties();

		props.put(HOST_PROP, LACSDWebConstants.LACSD_EMAIL_HOST);

        Session mailSession = Session.getInstance(props, null);

		Message msg = new MimeMessage(mailSession);
	
		msg.setFrom(new InternetAddress(mailFrom));
		
		InternetAddress[] address = new InternetAddress[mailTo.length];
		for (int i=0; i<address.length; i++) {
			address[i] = new InternetAddress(mailTo[i]);
		}		
		msg.setRecipients(Message.RecipientType.TO, address);
		
		
		// Cc email address
        InternetAddress[] addressCc = new InternetAddress[mailCc.length];
        for (int i=0; i<addressCc.length; i++) {
            addressCc[i] = new InternetAddress(mailCc[i]);
        }
        msg.setRecipients(Message.RecipientType.CC, addressCc);
		
		msg.setSubject(subject);
		
		msg.setSentDate(new java.util.Date());
		
		if (isHTMLMail) {
			msg.setContent(body, "text/html");
		} else {
			msg.setText(body);
		}
	
		Transport.send(msg);
		
	}
	catch (Exception e) {
		throw new LACSDException(e.getMessage());
	}
}

/**
 * Use JavaMail API to Send Email
 * Local Email resources such as SMTP Server will be derived
 * from Environment_Properties file.
 * @param String[] mailTo - String array of recipient email addresses
 * @param String mailFrom - Sender's Email Address
 * @param String subject - Email Subject Line
 * @param String body - Body Message Text
 * @param String[] attachments = String array of attachment files
 * @param boolean isHTMLMail
 * @return void
 * @throws LACSDException
*/
public void sendMail(String[] mailTo, String mailFrom, String subject, String body, String[] attachments, boolean isHTMLMail) throws LACSDException {

    try {
        
        Properties props = new Properties();

        props.put(HOST_PROP, LACSDWebConstants.LACSD_EMAIL_HOST);

        Session mailSession = Session.getInstance(props, null);

        Message msg = new MimeMessage(mailSession);
    
        msg.setFrom(new InternetAddress(mailFrom));
        
        InternetAddress[] address = new InternetAddress[mailTo.length];
        for (int i=0; i<address.length; i++) {
            address[i] = new InternetAddress(mailTo[i]);
        }
        
        msg.setRecipients(Message.RecipientType.TO, address);
        
        msg.setSubject(subject);
        
        msg.setSentDate(new java.util.Date());
        
        // create the message part
        MimeBodyPart messageBodyPart= new MimeBodyPart();
        //fill message
        if (isHTMLMail) {
            messageBodyPart.setContent(body, "text/html; charset=utf-8");
        } else {
            messageBodyPart.setText(body);
        }
        Multipart multipart= new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);

        //attachments
        if ( attachments != null ) {
            for ( int i = 0; i < attachments.length; i++ ) {
                messageBodyPart= new MimeBodyPart();
                String fileName= attachments[i];
                DataSource source= new FileDataSource(fileName);
                messageBodyPart.setDataHandler(new DataHandler(source));
                messageBodyPart.setFileName(fileName.substring(fileName.lastIndexOf("\\") + 1));
                multipart.addBodyPart(messageBodyPart);
            }
        }
        msg.setContent(multipart);
        Transport.send(msg);
        
    }
    catch (Exception e) {
        throw new LACSDException(e.getMessage());
    }
}

/**
 * Send email with in-memory binary as attachment
 * @param mailTo
 * @param mailFrom
 * @param subject
 * @param body
 * @param attachment
 * @param mineType  - mine type for attachment file. ie: "application/pdf" is PDF mineType
 * @param attachFileName
 * @throws LACSDException
 */
public void sendMail(String[] mailTo, String mailFrom, String subject, String body, byte[] attachment, String mineType, String attachFileName) throws LACSDException {

    try {
        
        Properties props = new Properties();

        props.put(HOST_PROP, LACSDWebConstants.LACSD_EMAIL_HOST);

        Session mailSession = Session.getInstance(props, null);

        Message msg = new MimeMessage(mailSession);
    
        msg.setFrom(new InternetAddress(mailFrom));
        
        InternetAddress[] address = new InternetAddress[mailTo.length];
        for (int i=0; i<address.length; i++) {
            address[i] = new InternetAddress(mailTo[i]);
        }
        
        msg.setRecipients(Message.RecipientType.TO, address);
        
        msg.setSubject(subject);
        
        msg.setSentDate(new java.util.Date());
        
        // create the message part
        MimeBodyPart messageBodyPart= new MimeBodyPart();
        messageBodyPart.setContent(body, "text/html; charset=utf-8");
        Multipart multipart= new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);
        messageBodyPart= new MimeBodyPart();
        messageBodyPart.setDataHandler(new DataHandler(attachment,mineType));
        messageBodyPart.setFileName(attachFileName);
        multipart.addBodyPart(messageBodyPart);
        msg.setContent(multipart);
        Transport.send(msg);
        
    }
    catch (Exception e) {
        throw new LACSDException(e.getMessage());
    }
}

/**
 * Use JavaMail API to Send Email
 * Local Email resources such as SMTP Server will be derived
 * from Environment_Properties file.
 * @param String[] mailTo - String array of recipient email addresses
 * @param String mailFrom - Sender's Email Address
 * @param String subject - Email Subject Line
 * @param String body - Body Message Text
 * @param MimeBodyPart imagePart = embeded image
 * @param boolean isHTMLMail
 * @return void
 * @throws LACSDException
*/
public void sendMailWithEmbed(String[] mailTo, String mailFrom, String subject, String body, MimeBodyPart imagePart, String priority) throws LACSDException {

    try {
        
        Properties props = new Properties();

        props.put(HOST_PROP, LACSDWebConstants.LACSD_EMAIL_HOST);

        Session mailSession = Session.getInstance(props, null);

        Message msg = new MimeMessage(mailSession);
    
        msg.setFrom(new InternetAddress(mailFrom));
        
        InternetAddress[] address = new InternetAddress[mailTo.length];
        for (int i=0; i<address.length; i++) {
            address[i] = new InternetAddress(mailTo[i]);
        }
        
        msg.setRecipients(Message.RecipientType.TO, address);
        
        msg.setSubject(subject);
        
        msg.setSentDate(new java.util.Date());
        msg.addHeader("X-Priority", priority);
        
        // create the message part
        MimeMultipart content = new MimeMultipart("related"); 
        MimeBodyPart textPart = new MimeBodyPart();    
        textPart.setText(body,       "US-ASCII", "html");    
        content.addBodyPart(textPart);    // Image part 
        msg.setContent(content);
        content.addBodyPart(imagePart);
        Transport.send(msg);
    }
    catch (Exception e) {
        throw new LACSDException(e.getMessage());
    }
}


/**
 * Use JavaMail API to Send Email
 * Local Email resources such as SMTP Server will be derived
 * from Environment_Properties file.
 * @param String[] mailTo - String array of recipient email addresses
 * @param String mailFrom - Sender's Email Address
 * @param String subject - Email Subject Line
 * @param String body - Body Message Text
 * @param String[] attachments
 * @return void
 * @throws LACSDException
 */
public void sendHTMLMail(String[] mailTo, String mailFrom, String subject, String body, String[] attachments) throws LACSDException {
	sendMail(mailTo, mailFrom, subject, body, attachments, true);
}

/**
 * Use JavaMail API to Send Email
 * Local Email resources such as SMTP Server will be derived
 * from Environment_Properties file.
 * @param String[] mailTo - String array of recipient email addresses
 * @param String mailFrom - Sender's Email Address
 * @param String subject - Email Subject Line
 * @param String body - Body Message Text
 * @param String[] attachments = String array of attachment files
 * @return void
 * @throws LACSDException
 */
public void sendMail(String[] mailTo, String mailFrom, String subject, String body, String[] attachments) throws LACSDException {
	sendMail(mailTo, mailFrom, subject, body, attachments, false);
}

/**
 * Return the Email Prefix
 * @param String emailAddress
 * @return String
 * throws LACSDException
*/
public String stripEmailPrefix(String emailAddress) {

	StringTokenizer st = new StringTokenizer(emailAddress,"@");
	String prefix = st.nextToken();
	return prefix;	
}
}
