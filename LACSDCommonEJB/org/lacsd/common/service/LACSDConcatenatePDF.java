package org.lacsd.common.service;
/******************************************************************************
//* Copyright (c) 2006 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:       LACSDConcatenatePDF.java
//* Revision:       
//* Author:         MFEINBERG@LACSD.ORG
//* Created On:     02-07-2006
//* Modified By:
//* Modified On:
//*                 
//* Description:	PDF File Concatenator - Java based on Lowagie ITEXT 1.3
/******************************************************************************/
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lacsd.common.exceptions.LACSDException;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PRAcroForm;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.SimpleBookmark;

public class LACSDConcatenatePDF {

/**
 * Concatenate various PDF Files into a new single PDF file
 * @param String inputFiles[]
 * @param String outfile
 * @throws LACSDException
*/
@SuppressWarnings({ "unchecked", "rawtypes"})
public void concatenatePDF(String inputFiles[], String outfile) throws LACSDException {
	
	try {
		int pageOffset = 0;
		ArrayList master = new ArrayList();
		Document document = null;
		PdfCopy  writer = null;
		for (int i=0; i<inputFiles.length; i++) {
			//	Create a PDF Reader for each input file
			PdfReader reader = new PdfReader(inputFiles[i]);
			reader.consolidateNamedDestinations();
			
			int inputPages = reader.getNumberOfPages();
			List bookmarks = SimpleBookmark.getBookmark(reader);
	        if (bookmarks != null) {
	            if (pageOffset != 0)
	                SimpleBookmark.shiftPageNumbers(bookmarks, pageOffset, null);
	            master.addAll(bookmarks);
	        }
	        pageOffset += inputPages;
	        if (i == 0) {
	        	//	Step 1: creation of a document-object
	            document = new Document(reader.getPageSizeWithRotation(1));
	            //	Step 2: we create a writer that listens to the document
	            writer = new PdfCopy(document, new FileOutputStream(outfile));
	            //	Step 3: we open the document
	            document.open();
	        }
	        //	Step 4: add content
	        PdfImportedPage page;
	        for (int j=0; j<inputPages;) {
	        	++j; // clever way to make pages start at "1" and not "0"
	        	page = writer.getImportedPage(reader, j);
	        	writer.addPage(page);
	        }
	        PRAcroForm form = reader.getAcroForm();
	        if (form != null) {
	        	writer.copyAcroForm(reader);
	        }
		}
		if (master.size() > 0) {
			writer.setOutlines(master);
		}
		//	Step 5: close the document
		document.close();
	}
	catch(Exception e) {
		throw new LACSDException(e.getMessage(),e.fillInStackTrace());
	}
}

/**
 * Return the total number of pages in an Adobe PDF file
 * @param pdfFile
 * @return
 * @throws LACSDException
*/
public int getTotalPages(File pdfFile) throws LACSDException {
	
	if (!pdfFile.exists()) {
		throw new LACSDException("PDF FILE DOES NOT EXIST: " + pdfFile.getAbsolutePath());
	}
	int totalPages = 0;
	try {
		PdfReader reader = new PdfReader(pdfFile.getAbsolutePath());
		reader.consolidateNamedDestinations();
		totalPages = reader.getNumberOfPages();
	}
	catch(IOException ioe) {
		throw new LACSDException(ioe.getMessage(),ioe.fillInStackTrace());
	}
	return totalPages;
}
}
