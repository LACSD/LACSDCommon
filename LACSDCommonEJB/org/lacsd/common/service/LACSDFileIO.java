package org.lacsd.common.service;

/******************************************************************************
//* Copyright (c) 2004 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		LACSDFileIO.java
//* Revision: 		1.0
//* Author:			M Feinberg
//* Created On: 	03-19-2004
//* Modified by:	
//* Modified On:	
//*					
//* Description:	Load / Save binary data to local file system
/******************************************************************************/

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.security.Security;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;

import org.lacsd.common.exceptions.LACSDException;
import org.lacsd.common.util.LACSDTimedSocket;


public class LACSDFileIO {

private static LACSDFileIO _INSTANCE = new LACSDFileIO();		// Singleton Instance

private static final String EOF = "%%EOF\n";

/**
 * Private Constructor - Singleton Pattern
*/
private LACSDFileIO() {
	super();
}

/**
 * Return Singleton Instance
 * @return LACSDFileIO
*/
public static LACSDFileIO getInstance() {
	return _INSTANCE;
}

/**
 * Copy a single File from one java.io.File to another java.io.File
 * @param inFile
 * @param outFile
 * @throws Exception
 */
@SuppressWarnings("resource")
public void copyFile(File inFile, File outFile) throws LACSDException {

	if (!inFile.exists()) {
		throw new LACSDException("Could not find file " + inFile.getName());
	}
	
	FileChannel in = null;
	FileChannel out = null;
	
	
	try {
        in = new FileInputStream(inFile).getChannel();
        out = new FileOutputStream(outFile).getChannel();

        // the transferTo() does not transfer files > than 2^31-1 bytes.
        // reduce the number of bytes transferred at one time
        long count = 0;
        long byteTransfered = 1;
        while ( byteTransfered > 0 ) {
            byteTransfered = in.transferTo( count, 1024 * 1024, out);
            count = count + byteTransfered;
        }
	}
	catch (Exception e) {
		throw new LACSDException(e.getMessage(),e.fillInStackTrace());
	}
	finally {
		try {
            if ( in != null )
                in.close();
            if ( out != null )
                out.close();
		}
		catch (Exception e) {
            e.printStackTrace();
			throw new LACSDException("CANNOT RELEASE FILE I/O RESOURECES!!! > " + e.getMessage(),e.fillInStackTrace());
		}
	}
}

/**
 * Returns the contents of a File as a byte array
 * @param File file
 * @return byte[]
 * @throws LACSDException
*/
@SuppressWarnings("resource")
public byte[] getBytesFromFile(File file) throws LACSDException {

	try {
		
		if (!file.exists()) {
			throw new LACSDException("Could not find file " + file.getName());
		}
		
		InputStream is = new FileInputStream(file);
		long length = file.length();


		// Ensure that file is not larger than Integer.MAX_VALUE.
		if (length > Integer.MAX_VALUE) {
			throw new LACSDException("File to large to read");
		}

		// Create the byte array to hold the data
		byte[] bytes = new byte[(int)length];

		// Read in the bytes
		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
			offset += numRead;
		}

		// Ensure all the bytes have been read in
		if (offset < bytes.length) {
			throw new LACSDException("Could not completely read file " + file.getName());
		}

		// Close the input stream and return bytes
		is.close();
		
		return bytes;
		
	}
	catch (IOException ioe) {
		throw new LACSDException(ioe.getMessage());
	}
}

/**
 * Archive the BYTE[] array input to specified File
 * ->> DEFAULT SETTING WILL PARSE END OF BINARY FILE FOR CLEAN %%EOF
 * @param File file
 * @param byte[] binaryData
 * @return void
 * @throws LACSDException
*/
public void setBytesToFile(File file, byte[] binaryData) throws LACSDException {
	this.setBytesToFile(file,binaryData,true);
}

/**
 * Archive the BYTE[] array input to specified File
 * @param File file
 * @param byte[] binaryData
 * @return void
 * @throws LACSDException
*/
public void setBytesToFile(File file, byte[] binaryData, boolean cleanEOF) throws LACSDException {

	String fileName = file.getAbsolutePath();
	
	try {
		OutputStream out = new FileOutputStream(fileName);
		out.write(binaryData);
		out.close();
		out.flush();
		
		//	PATCH (6-28-2005) - remove whitespace from binarys with [%%EOF/n] marker
		//  PATCH (4-37-2006) - make this conditional - causes OutOfMemory 512MB Upper Bound for large files over 70MB
			if (cleanEOF) {
				byte[] cleanBinary = doCleanEOF(fileName);
				if (cleanBinary != null) {
					file.delete();
					out = new FileOutputStream(new File(fileName));
					out.write(cleanBinary);
					out.close();
					out.flush();
				}
			}
	}
	catch (IOException ioe) {
		throw new LACSDException(ioe.getMessage(), ioe.fillInStackTrace());
	}

}

/**
 * Archive a java.io.Serializable object to specified File
 * @param File file
 * @param Object serializableObject
 * @return void
 * @throws LACSDException
*/
public void setObjectToFile(File file, Object serializableObject) throws LACSDException {

	try {
		
		FileOutputStream fos = new FileOutputStream(file);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		
		oos.writeObject(serializableObject);
		
		fos.flush();
		fos.close();
		
	}
	catch (Exception e) {
		throw new LACSDException(e.getMessage(),e.fillInStackTrace());
	}
}

/**
 * Returns the contents of a File as a java.io.Serializable object (if possible)
 * @param File file
 * @return Object
 * @throws LACSDException
*/
public Object getObjectFromFile(File file) throws LACSDException {

	Object returnObject = null;

	if (!file.exists()) {
		throw new LACSDException("Could not find file " + file.getName());
	}
	
	try {
		FileInputStream fis = new FileInputStream(file);
		ObjectInputStream ois = new ObjectInputStream(fis);
		
		returnObject = ois.readObject();
		fis.close();
	}
	catch (Exception e) {
		throw new LACSDException(e.getMessage(),e.fillInStackTrace());
	}
	return returnObject;
}


/**
 * Archive the BYTE[] array input to specified File 
 * ... and save it in PKZIP format!!
 * @param File file
 * @param byte[] binaryData
 * @return File - the zip file that was just created
 * @throws LACSDException
*/
public File setBytesToZIPFile(File file, byte[] binaryData) throws LACSDException {

	String fileDirectory = null;
	String fileExactName = null;
	String fileExtension = null;

	File zipFile = null;
	
	try {
		
		//	STEP 1)	Parse filename and rename to ".zip"
		fileDirectory = file.getParent();
		String name = file.getName();
		StringTokenizer st = new StringTokenizer(name,".");
		try {
			fileExactName = st.nextToken();
			fileExtension = st.nextToken();
		}
		catch (Exception e) {
			throw new LACSDException(e.getMessage(),e.fillInStackTrace());
		}
		if ((fileExactName == null) || (fileExtension == null)) {
			throw new LACSDException("Cannot write bytes to file!");
		}
		zipFile = new File(fileDirectory + "/" + fileExactName + ".zip");
		
		//	STEP 2)	Serialize output file from binary array
		//byte[] buf = new byte[10000];
		OutputStream fout = new FileOutputStream(zipFile);
		ZipOutputStream zout = new ZipOutputStream(fout);
		zout.putNextEntry(new ZipEntry(file.getName()));			
		zout.write(binaryData);
		
		zout.close();
	}
	catch (IOException ioe) {
		throw new LACSDException(ioe.getMessage(), ioe.fillInStackTrace());
	}
	return zipFile;
}

/**
 * Create zipfile
 * @param File[] files - the files to add to zip
 * @param String name - name of the zip file
 * @return File
 * @throws LACSDException
*/
public File createZipFile(File[] files, String name) throws LACSDException {
	
	File zipFile = new File(name);
	
	try {
		//byte[] buf = new byte[10000];
		OutputStream fout = new FileOutputStream(zipFile);
		ZipOutputStream zout = new ZipOutputStream(fout);
		
		for (int i=0; i<files.length; i++) {	
			zout.putNextEntry(new ZipEntry(files[i].getName()));			
			byte[] binaryData = getBytesFromFile(files[i]);
			zout.write(binaryData);
		}
		zout.close();
	}
	catch (IOException ioe) {
		throw new LACSDException(ioe.getMessage(), ioe.fillInStackTrace());
	}
	return zipFile;
}

/**
 * Unzip a zipfile into the same directory that the file lives
 * @param File zipFile
 * @return ArrayList
 * @throws LACSDException
*/
@SuppressWarnings("rawtypes")
public ArrayList doUnZipFile(File file) throws LACSDException {

	ArrayList<String> unzippedFileNames = new ArrayList<String>();

	if (!file.exists()) {
		throw new LACSDException("Could not find file " + file.getName());
	}
	
	String baseDir = (file.getParent() + "/");
	
	try {
		ZipFile zipFile = new ZipFile(file.getAbsolutePath());
		Enumeration entries = zipFile.entries();
		while (entries.hasMoreElements()) {
			ZipEntry zipEntry = (ZipEntry)entries.nextElement();
			String name = zipEntry.getName();
			unzippedFileNames.add(baseDir + name);

			// note:  this is a temporary solution and will not correctly put files into
			// subdirectories recursively!
			if (zipEntry.isDirectory()) {
				(new File(baseDir + zipEntry.getName())).mkdir();
				continue;
			}
			// if this file is in a nested folder structure, we need to create it first
			File outputFile = new File(baseDir + name);
			if (!outputFile.exists()) {
				File parent = outputFile.getParentFile();
				parent.mkdirs();
				outputFile.createNewFile();
			}
			InputStream in = zipFile.getInputStream(zipEntry);
			OutputStream out = new BufferedOutputStream(new FileOutputStream(baseDir + name));
			byte[] buffer = new byte[1024];
			int len;
			while ((len = in.read(buffer)) >= 0) {
				out.write(buffer,0,len);
			}
			in.close();
			out.close();
		}
		zipFile.close();
	}
	catch (IOException ioe) {
		throw new LACSDException(ioe.getMessage(), ioe.fillInStackTrace());
	}
	
	return unzippedFileNames;
}

/**
 * Unzip a zipfile into the same directory that the file lives
 * @param File zipFile
 * @return ArrayList
 * @throws LACSDException
*/
@SuppressWarnings({ "unchecked", "rawtypes" })
public ArrayList doRecursiveUnZipFile(File file) throws LACSDException {

	ArrayList unzippedFileNames = new ArrayList();

	if (!file.exists()) {
		throw new LACSDException("Could not find file " + file.getName());
	}
	
	String baseDir = (file.getParent() + "/");
	
	try {
		ZipFile zipFile = new ZipFile(file.getAbsolutePath());
		Enumeration entries = zipFile.entries();
		outer: while (entries.hasMoreElements()) {
			ZipEntry zipEntry = (ZipEntry)entries.nextElement();
			String name = zipEntry.getName();
			if(name.endsWith(".ZIP") || name.endsWith(".Zip") || name.endsWith(".zip")) {
			    ArrayList subFiles = doRecursiveUnZipFile(new File(baseDir + zipEntry.getName()));
			    Iterator subit = subFiles.iterator();
			    while (subit.hasNext()) {
			        unzippedFileNames.add(subit.next());
			    }
			    continue outer;
			}
			else {
			    unzippedFileNames.add(baseDir + name); 
			}
			if (zipEntry.isDirectory()) {
				(new File(baseDir + zipEntry.getName())).mkdirs();
				continue;
			}
			
			// if this file is in a nested folder structure, we need to create it first
			File outputFile = new File(baseDir + name);
			if (!outputFile.exists()) {
			    File parent = outputFile.getParentFile();
			    parent.mkdirs();
			    outputFile.createNewFile();
			}
			InputStream in = zipFile.getInputStream(zipEntry);
			OutputStream out = new BufferedOutputStream(new FileOutputStream(outputFile));
			byte[] buffer = new byte[1024];
			int len;
			while ((len = in.read(buffer)) >= 0) {
				out.write(buffer,0,len);
			}
			in.close();
			out.close();
		}
		zipFile.close();
	}
	catch (IOException ioe) {
		throw new LACSDException(ioe.getMessage(), ioe.fillInStackTrace());
	}
	
	return unzippedFileNames;
}

/**
 * Delete a file from the file system
 * @param File file
 * @return void
 * @throws LACSDException
*/
public void deleteFile(File file) throws LACSDException {

	try {
		boolean deleted = false;
		if (file.isFile()) {
			if (file.exists() ) {
				deleted = file.delete();
			}
			if (!deleted) {
				throw new LACSDException("Could not delete file!: " + file.getCanonicalPath());
			}
		}
	}
	catch (Exception e) {
		throw new LACSDException(e.getMessage(), e.fillInStackTrace());
	}
}

/**
 * Clean up the whitespace that may be tacked-on after certain %%EOF Markers
 * @param File file
 * @return void
 * @throws LACSDException
*/
@SuppressWarnings({ "unchecked", "rawtypes" })
public byte[] doCleanEOF(String absolutePath) throws LACSDException {

	ArrayList memFile = new ArrayList();		
	StringBuffer charWords = new StringBuffer();
	byte[] newBinary = null;
	RandomAccessFile raf = null;
	
	try {
		raf = new RandomAccessFile(absolutePath,"r");
		int filesize = (int)raf.length();
		byte binary[] = new byte[filesize];
		raf.seek(0);
		
		while (true) {
			try {
				raf.readFully(binary);	
			}
			catch (EOFException eofe) {
				break;
			}
		}
		
		
		//	WARNING: MEMFILE ARRAY CAN BLOW OUT JAVA HEAP ON LARGE FILES OVER 100MB!!
		//int newLineTicker = 0;
		for (int i=0; i < binary.length; i++) {
			Byte bytes = new Byte(binary[i]);
			char c = (char)bytes.byteValue();
			charWords.append(c);
			if (c == '\n') {
				String txt = charWords.toString();
				if (txt.equalsIgnoreCase(EOF)) {
					memFile.add(bytes);
					break;
				}
				else {
					charWords = new StringBuffer();
				}
			}
			memFile.add(bytes);
		}
		
		newBinary = new byte[memFile.size()];
		Iterator it = memFile.iterator();
		int pos = 0;
		while (it.hasNext()) {
			Byte bytes = (Byte)it.next();
			newBinary[pos] = bytes.byteValue();
			pos++;
		}
	}
	catch (FileNotFoundException fnfe) {
		throw new LACSDException("Could not clean whitespace off binary file - File Not Found", fnfe.fillInStackTrace());
	}
	catch (IOException ioe) {
		throw new LACSDException("Could not clean whitespace off binary file - IO Exception", ioe.fillInStackTrace());
	}
	finally {
		memFile = null;
		charWords = null;
		try {
			raf.close();
		}
		catch (IOException ioe2) {
			throw new LACSDException("Could not release handle file while stripping EOF characters!", ioe2.fillInStackTrace());
		}
	}
	
	return newBinary;
}

/**
 * Recursive delete
 * @param File path
 * @return void
*/
public void recursiveDelete(File path) throws Exception {
	File[] files = path.listFiles();
	for(int i=0; i<files.length; ++i) {
		if(files[i].isDirectory()) {
			recursiveDelete(files[i]);
		}
		files[i].delete();
	}
	path.delete();
}

/**
 * Returns the contents of a File as a byte array
 * @param URL url
 * @return byte[]
 * @throws LACSDException
*/
@SuppressWarnings("resource")
public byte[] getBytesFromURL(URL url) throws LACSDException {

	byte[] returnData = null;

	String protocol = null;
	int port;
	
	SocketFactory socketFactory = null;
	SSLSocketFactory sslSocketFactory = null;
	
	Socket server = null;
	OutputStreamWriter out = null;
	BufferedInputStream in = null;
	OutputStream localOut = null;
	BufferedReader localBufferIn = null;
	RandomAccessFile raf = null;
	
	try {
		
		// STEP 1) INITIALIZE SOCKETS
		Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
		System.setProperty("java.protocol.handler.pkgs", "com.sun.net.ssl.internal.www.protocol");
		
		socketFactory = SocketFactory.getDefault();
		sslSocketFactory = (SSLSocketFactory)SSLSocketFactory.getDefault();
		
		// protocol (https) and port (80 or 443) will now automatically set themselves after parsing the URL string
		String temp = url.toString();				// 0 1 2 3 4             0 1 2 3 4
		char differentiator = temp.charAt(4);		// h t t p s :     or    h t t p :
		temp = String.valueOf(differentiator);
		if (temp.equalsIgnoreCase("s")) {
			protocol = "https";
			port = 443;
		}
		else if (temp.equalsIgnoreCase(":")) {
			protocol = "http";
			port = url.getPort();		// updated 4/08/02 <- to accept manually entered HOST Strings in the URL
			if (port == -1) {
				port = 80;
			}
		}
		else {
			protocol = "http";
			port = 80;
		}
		
		
		// STEP 2) CONNECT TO REMOTE
		int connectTimeout = 5000; // 5 seconds connect timeout
		int responseTimeout = 10000; // 10 seconds response timeout
		
		if (protocol.equals("http")) {
			server = LACSDTimedSocket.getSocket(url.getHost(), port, connectTimeout, socketFactory);
		}
		else if (protocol.equals("https")) {
			server = LACSDTimedSocket.getSocket(url.getHost(), port, connectTimeout, sslSocketFactory);
		}
		server.setSoTimeout(responseTimeout);


		
		// STEP 3) GET REMOTE FILE
		out = new OutputStreamWriter(server.getOutputStream());
		in = new BufferedInputStream(server.getInputStream());
		out.write("GET " + "http://" + url.getHost() + ":" + port + url.getFile() + " HTTP/1.0\n\n");
		out.flush();
		

		
		File tempFile = new File(System.getProperty("java.io.tmpdir") + "tmp.dat");
		localOut = new FileOutputStream(tempFile);
		byte[] buf = new byte[4 * 1024]; // 4K Buffer
		int bytesRead;
		while ((bytesRead = in.read(buf)) != -1) {			
			localOut.write(buf,0,bytesRead);
		}
		localOut.close();
		out.close();
		in.close();

		
		// STEP 4) ANALYZE HTTP HEADERS
		localBufferIn = new BufferedReader(new FileReader(tempFile));
		int contentLength = 0;
		String s;
		while ((s = localBufferIn.readLine()) != null) {
		   if (s.equals("")) {
				break;
		   }
		   if (s.startsWith("HTTP")) {
			   StringTokenizer st = new StringTokenizer(s," ");
			   while(st.hasMoreTokens()) {
				   if (st.nextToken().equalsIgnoreCase("404")) {
					   throw new LACSDException("HTTP 404 - File Not Found!");
				   }
			   }
		   }
		   if (s.startsWith("Content-Length")) {
			   StringTokenizer st = new StringTokenizer(s," ");
			   st.nextToken();
			   contentLength = Integer.valueOf(st.nextToken()).intValue();
		   }
		}

		
		// STEP 5) LOAD BINARY FILE INTO MEM & STRIP HTTP HEADERS
		raf = new RandomAccessFile(tempFile.getAbsolutePath(),"r");
		int actualFilesize = (int)raf.length();
		returnData = new byte[contentLength];	// note: contentLength came from HTTP-HEADER
		raf.seek((actualFilesize - contentLength));
		while (true) {
			try {
				raf.readFully(returnData);	
			}
			catch (EOFException eofe) {
				break;
			}
		}
		raf.close();
		tempFile.delete();
		
	}
	catch (Exception e) {
		throw new LACSDException(e.getMessage(),e.fillInStackTrace());
	}
	finally {
		try {
			if (in != null) { in.close();}
			if (out != null) { out.close();}
			if (server != null) { server.close();}
			if (localOut != null) { localOut.close();}
			if (localBufferIn != null) { localBufferIn.close();}
			if (raf != null) { raf.close(); }
		}
		catch (Exception noclose) {
			// digest
		}
	}
	return returnData;
}

/**
 * Unzip a zipfile into the other directory (extractLoc)
 * @param File zipFile
 * @param String extractLoc
 * @return ArrayList
 * @throws LACSDException
*/
@SuppressWarnings("rawtypes")
public ArrayList<String> doUnZipFile(File file, String extractLoc) throws Exception {

	ArrayList<String> unzippedFileNames = new ArrayList<String>();
	if (!file.exists()) {
		throw new Exception("Could not find file " + file.getAbsolutePath());
	}

	//String baseDir = (file.getParent() + "/" );

	try {
		ZipFile zipFile = new ZipFile(file.getAbsolutePath());
		Enumeration entries = zipFile.entries();
		while (entries.hasMoreElements()) {
			ZipEntry zipEntry = (ZipEntry)entries.nextElement();
			String name = zipEntry.getName();
			unzippedFileNames.add(extractLoc + "/" +  name);
			if (zipEntry.isDirectory()) {
				(new File(extractLoc + "/" + zipEntry.getName())).mkdir();
				continue;
			}
			// if this file is in a nested folder structure, we need to create it first
			File outputFile = new File(extractLoc + "/" + name);
			if (!outputFile.exists()) {
				File parent = outputFile.getParentFile();
				parent.mkdirs();
				outputFile.createNewFile();
			}
			InputStream in = zipFile.getInputStream(zipEntry);
			OutputStream out = new BufferedOutputStream(new FileOutputStream(outputFile));
			byte[] buffer = new byte[1024];
			int len;
			while ((len = in.read(buffer)) >= 0) {
				out.write(buffer,0,len);
			}
			in.close();
			out.close();
		}
		zipFile.close();
	}
	catch (IOException ioe) {
		throw new Exception(ioe.getMessage(), ioe.fillInStackTrace());
	}
	return unzippedFileNames;
}

/**
* Compressed file to zip format
* @param strSource - Source file
* @param strTarget - Zip file name
*/
public File zip(File sourceFile, String strTarget) throws LACSDException {
    File zipfile = new File(strTarget);
	try {
	    ZipOutputStream cpZipOutputStream= null;
		String strNewSource= "";
		if (!sourceFile.isFile() && !sourceFile.isDirectory()) {
		    throw new LACSDException("Could not find file " + sourceFile.getName());
		}
		if (sourceFile.isDirectory()) {
			strNewSource= sourceFile.getAbsolutePath();
		} else {
			strNewSource= "";
		}
		cpZipOutputStream= new ZipOutputStream(new FileOutputStream(zipfile));
		cpZipOutputStream.setLevel(9);
		zipFiles(sourceFile, strNewSource, cpZipOutputStream);
		cpZipOutputStream.finish();
		cpZipOutputStream.close();
	}catch (IOException ioe) {
	   throw new LACSDException(ioe.getMessage(), ioe.fillInStackTrace());
	}
	return zipfile;
}

/**
* Recursive compressed all files to a zip file
*/
private  void zipFiles(File cpFile, String strSource, ZipOutputStream zout) throws LACSDException {

	try {
	    if (cpFile.isDirectory()) {
			File[] fList= cpFile.listFiles();
			for (int i= 0; i < fList.length; i++) {
				zipFiles(fList[i].getCanonicalFile() , strSource, zout);
			}
		} else {
				String filePath = cpFile.getCanonicalPath();
				String strZipEntryName= "";
				if (!strSource.equals("")) {
					strZipEntryName= filePath.substring(strSource.length(), filePath.length());
				} else {
					strZipEntryName= cpFile.getName();
				}
				if (strZipEntryName.startsWith("\\")) {
					strZipEntryName = strZipEntryName.substring(1,strZipEntryName.length());
				}
				zout.putNextEntry(new ZipEntry(strZipEntryName ));
				byte[] binaryData = getBytesFromFile(cpFile);
				zout.write(binaryData);
		}
	 }
	 catch (IOException ioe) {
	    throw new LACSDException(ioe.getMessage(), ioe.fillInStackTrace());
	 }
}



/**
 * Tester Main Execution Block
 * @param args
*/
public static void main(String[] args) {
    //LACSDFileIO io = LACSDFileIO.getInstance();
    
    try {
       
        String[] cvsFiles = new String[2];
        cvsFiles[0] = "c:/temp/example0.csv";
        cvsFiles[1] = "c:/temp/example1.csv";
        //io.convertCSVtoExcel(cvsFiles, new File("c:/temp/newExcel.xls"));
        System.out.println("DONE");
    }
    catch(Exception e) {
        e.printStackTrace();
    }
}
}
