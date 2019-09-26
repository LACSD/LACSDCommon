package org.lacsd.common.service;

/******************************************************************************
//* Copyright (c) 2011 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		LACSDFTPClient.java
//* Revision: 		1.0
//* Author:			asrirochanakul
//* Created On: 	07-22-2011
//* Modified by:	
//* Modified On:	
//*					
//* Description:	FTP Client
/******************************************************************************/

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.net.ftp.FTPClient;

public class LACSDFTPClient {

private static LACSDFTPClient _INSTANCE = new LACSDFTPClient();			// Singleton Instance

/**
 * Return Singleton Instance
 * @return LACSDEmail
*/
public static LACSDFTPClient getInstance() {
	return _INSTANCE;
}

/**
 * Send files through FTP
 * @param hostName
 * @param userName
 * @param password
 * @param targetDir
 * @param files
 * @param int fileType org.apache.commons.net.ftp.FTPClient.CONSTANT (i.e. FTPClient.BINARY_FILE_TYPE)
 * @return boolean isSuccess
 */
@SuppressWarnings("rawtypes")
public boolean sendFTP(String hostName, String userName, String password, String targetDir, HashMap files, int fileType) {

	FTPClient ftpClient = new FTPClient();
	FileInputStream fis = null;
	boolean isSuccess = false;
	short numberFailed = 0;

	try {
		ftpClient.connect(hostName);
		
		if (ftpClient.login(userName, password)) {

			ftpClient.changeWorkingDirectory(targetDir);
			ftpClient.setFileType(fileType);

			Set set = files.entrySet();
			Iterator i = set.iterator();

			while (i.hasNext()) {
				Map.Entry me = (Map.Entry) i.next();
				String filePath = me.getKey()+"";
				String fileName = me.getValue()+"";
				fis = new FileInputStream(filePath);
				
				if (! ftpClient.storeFile(fileName, fis)) {
					System.err.println(ftpClient.getReplyString());
					System.err.println(fileName + " ftp failed.");
					numberFailed++;
				}
			}

			if (numberFailed == 0) {
				isSuccess = true;
			}

		} else {
			System.err.println(ftpClient.getReplyString());
		}

		ftpClient.logout();
	} catch (IOException ioe) {
		ioe.printStackTrace();
	} finally {
		try { 
			if (fis != null) { 
				fis.close(); 
			} 
			ftpClient.disconnect(); 
		} catch (IOException e) { 
			e.printStackTrace(); 
		}
	}
	return isSuccess;
}

@SuppressWarnings({ "rawtypes", "unchecked" })
public static void main(String[] args) throws Exception {

	LACSDFTPClient ftp = LACSDFTPClient.getInstance();

	String hostName = "calftpemc.calpers.ca.gov";
	String userName = "lsd10006-t";
	String password = "c#ky2feB";
	String directory = "test-out";

	HashMap hm = new HashMap();
	
	String filePath = "C:/openpgp/20110728125233_000_10006.pgp";
	String fileName = "20110728125233_000_10006.pgp";

	hm.put(filePath, fileName);
	
	String filePath2 = "C:/openpgp/20110728125233_000_10006.sem";
	String fileName2 = "20110728125233_000_10006.sem";

	hm.put(filePath2, fileName2);

	ftp.sendFTP(hostName, userName, password, directory, hm, FTPClient.ASCII_FILE_TYPE);
}

}