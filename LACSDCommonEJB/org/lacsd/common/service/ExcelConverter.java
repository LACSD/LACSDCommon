package org.lacsd.common.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import nl.fountain.xelem.excel.Row;
import nl.fountain.xelem.excel.Table;
import nl.fountain.xelem.excel.Workbook;
import nl.fountain.xelem.excel.Worksheet;
import nl.fountain.xelem.lex.ExcelReader;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.lacsd.common.exceptions.LACSDException;
import org.lacsd.common.util.ExcelUtil;
import org.xml.sax.InputSource;

/******************************************************************************
 //* Copyright (c) 2009 Sanitation Districts of Los Angeles. All Rights Reserved.
 //* Filename:       ExcelConverter.java
 //* Revision:       1.0
 //* Author:         hho@lacsd.org
 //* Created On:     Jun 3, 2009
 //* Modified By:
 //* Modified On:
 //*                 
 //* Description:	<CLASS DESCRIPTION HERE>
 /******************************************************************************/

public class ExcelConverter {

   // private static ExcelConverter _INSTANCE = new ExcelConverter();		// Singleton Instance

   

    /**
     * Private Constructor - Singleton Pattern
    */
    public ExcelConverter() {
    	//super();
    }

    /**
     * Return Singleton Instance
     * @return LACSDFileIO
    */
    /*public static ExcelConverter getInstance() {
    	return _INSTANCE;
    }*/

    
    /**
     * 
     * @param csvFiles
     * @return
     * @throws LACSDException
     */

    public void  convertCSVtoExcel (String[] csvFiles, File excelFile) throws LACSDException{
        ArrayList<ArrayList<String>> arList=null;
        ArrayList<String> al=null;
        try {
    	    HSSFWorkbook hwb = new HSSFWorkbook();
    	    BufferedReader br = null;
    	    HSSFSheet sheet = null;
    	    for (int i = 0; i < csvFiles.length; i++) {
    			br= new BufferedReader(new FileReader(csvFiles[i]));
    			arList = new ArrayList<ArrayList<String>>();
    			String thisLine; 
    			while ((thisLine = br.readLine()) != null){
    			    al = new ArrayList<String>();
    			    String strar[] = thisLine.split(",");
    			    for(int j=0;j<strar.length;j++){
    			        al.add(strar[j]);
    			    }
    			    arList.add(al);
    			    //i++;
    			} 
    		    br.close();
    		    sheet = hwb.createSheet("sheet" + i);
    		    for(int k=0;k<arList.size();k++){
    		        ArrayList<String> ardata = (ArrayList<String>)arList.get(k);
    		        HSSFRow row = sheet.createRow((short) 0+k);
    		        for(int p=0;p<ardata.size();p++){
    		            HSSFCell cell = row.createCell(p);
    		            cell.setCellValue(new HSSFRichTextString(ardata.get(p).toString()));
    		        }
    		    } 
    		}
    	    //returnData = hwb.getBytes();
    	    FileOutputStream fileOut = new FileOutputStream(excelFile);
    	    hwb.write(fileOut);
    	    fileOut.close();
    	  
        }catch(IOException e) {
            throw new LACSDException("Unable to convert CSV to Excel: " + e.getMessage());
        }
    }

    @SuppressWarnings("rawtypes")
	public void  convertSpreadsheetMLtoExcel (InputStream is, File excelFile) throws LACSDException{
        try {
            ExcelReader reader = new ExcelReader();
            InputSource inputsource = new InputSource(is);
            inputsource.setEncoding("ISO-8859-1");
			Workbook wb = reader.getWorkbook(inputsource);
			List wsList = wb.getWorksheets();
			HSSFWorkbook hwb = new HSSFWorkbook();
			HSSFSheet sheet = null;
			for (int i = 0; i < wsList.size(); i++) {
				sheet = hwb.createSheet("sheet" + i);
				Worksheet ws = (Worksheet)wsList.get(i);
				Table table = ws.getTable();
				Iterator rows = table.rowIterator();
				int rowIndex = 0;
				while (rows.hasNext()) {
					Row row = (Row) rows.next();
					HSSFRow fRow = sheet.createRow(rowIndex);
					int columIndex = 0;
					Iterator columns = table.columnIterator();
					while (columns.hasNext()) {
						 HSSFCell cell = fRow.createCell(columIndex);
					     cell.setCellValue(new HSSFRichTextString(row.getCellAt(columIndex+1).getData$()));
						 columIndex++;
					}
					rowIndex++;
				}
			}
		    FileOutputStream fileOut = new FileOutputStream(excelFile);
			hwb.write(fileOut);
			fileOut.close();
			
        }catch(Exception e) {
            throw new LACSDException("Unable to SpreadsheetML to Excel: " + e.getMessage());
        }
        
    }
    
    /**
     * Check if there is data in excel sheet
     * @param excelFile
     * @return
     * @throws LACSDException
     */
    public boolean excelHasData(String excelFile) throws LACSDException  {
        boolean result = true;
        try {
            HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(excelFile));
            HSSFSheet sheet = wb.getSheetAt(0);
            int rowNum = sheet.getPhysicalNumberOfRows();
            if(rowNum > 4){
                for(int j = 0; j < 4; j++){
                    HSSFRow row = sheet.getRow(j);
                    if(row != null){
                        int cellNum = row.getPhysicalNumberOfCells();
                        for(int i = 0; i <= cellNum; i++){
                            String cellValue = "";
                            HSSFCell cell = row.getCell(i);
                            if(cell == null){
                                continue;
                            }
                            cellValue = cell.getStringCellValue().trim();
                            if(!cellValue.equalsIgnoreCase("NO RECORD FOUND")){
                                continue;
                            }
                            result = false;
                            break;
                        }

                    }
                    if(!result) {
                        break;
                    }
                }

            }
            wb = null;
            sheet = null;
        }
        catch(IOException e)
        {
            result = false;
        }
        return result;
    }
    
    /**
     * Combine two excels into one file
     * @param masterFile
     * @param copyFile
     * @throws LACSDException
     */
    public void combineTwoExcels(String masterFile, String copyFile)throws LACSDException {
        try{
            File mFile = new File(masterFile);
            HSSFWorkbook masterWB = null;
            if(!mFile.exists()){
                masterWB = new HSSFWorkbook();
            } else{
                masterWB = new HSSFWorkbook(new FileInputStream(masterFile));
            }
            int numSheet = masterWB.getNumberOfSheets();
            HSSFWorkbook copyWB = new HSSFWorkbook(new FileInputStream(copyFile));
            int numCopySheeet = copyWB.getNumberOfSheets();
            for (int s = 0; s < numCopySheeet; s++) {//loop through all the sheets in the copy file
	            HSSFSheet copySheet = copyWB.getSheetAt(s);
	            HSSFSheet newSheet = masterWB.createSheet("Sheet" + (++numSheet));
	            ExcelUtil.copySheets(newSheet, copySheet);
	            /*int numMerge = copySheet.getNumMergedRegions();
	            
	            for(int i = 0; i < numMerge; i++){
	            	org.apache.poi.hssf.util.CellRangeAddress cellRangeAddress = copySheet.getMergedRegion(i);
	                newSheet.addMergedRegion(cellRangeAddress);
	            }
	
	            int rowNum = copySheet.getPhysicalNumberOfRows();
	            for(int i = 0; i <= rowNum; i++){
	                HSSFRow copyRow = copySheet.getRow(i);
	                HSSFRow newRow = newSheet.createRow(i);
	                if(copyRow != null){
	                    newRow.setHeight(copyRow.getHeight());
	                    int cellNum = copyRow.getPhysicalNumberOfCells();
	                    HSSFCell copyCell = null;
	                    HSSFCell newCell = null;
	                    for(int j = 0; j <= cellNum; j++){
	                        newSheet.setColumnWidth(j, copySheet.getColumnWidth(j));
	                        copyCell = copyRow.getCell(j);
	                        newCell = newRow.createCell(j);
	                        if(copyCell != null){
	                            int type = copyCell.getCellType();
	                            newCell.setCellType(type);
	                            if(type == HSSFCell.CELL_TYPE_STRING){
	                                newCell.setCellValue(copyCell.getRichStringCellValue());
	                            } else if(type == HSSFCell.CELL_TYPE_NUMERIC){
	                                newCell.setCellValue(copyCell.getNumericCellValue());
	                            } else if(type == HSSFCell.CELL_TYPE_BOOLEAN) {
	                                newCell.setCellValue(copyCell.getBooleanCellValue());
	                            } else if(type == HSSFCell.CELL_TYPE_FORMULA){
	                                newCell.setCellFormula(copyCell.getCellFormula());
	                                newCell.setCellValue(copyCell.getNumericCellValue());
	                            }
	                            HSSFCellStyle copyStyle = copyCell.getCellStyle();
	                            HSSFCellStyle newStyle = null;
	                            boolean found = false;
	                            for(int t = 0; t < masterWB.getNumCellStyles(); t++){
	                                if(!compareStyle(masterWB, masterWB.getCellStyleAt((short)t), copyWB, copyStyle)) {
	                                    continue;
	                                }
	                                newStyle = masterWB.getCellStyleAt((short)t);
	                                found = true;
	                                break;
	                            }
	
	                            if(!found) {
	                                newStyle = masterWB.createCellStyle();
	                                newStyle.cloneStyleFrom(copyStyle);
	                            }
	                            newStyle.setDataFormat(copyStyle.getDataFormat());
	                            newCell.setCellStyle(newStyle);
	                        }
	                    }
	
	                }
	            }*/
	        } //End of loop through all the sheets in the copy file
            FileOutputStream fileOut = new FileOutputStream(masterFile);
            masterWB.write(fileOut);
            fileOut.close();
        }
        catch(IOException e)  {
            throw new LACSDException("Unable to combine multiple Excels: " + e.getMessage());
        }
    }
    

   
    
    /**
     * Tester Main Execution Block
     * @param args
    */
    public static void main(String[] args) {
        ExcelConverter io = new ExcelConverter();//.getInstance();
        
        try {
           
            String[] cvsFiles = new String[2];
            cvsFiles[0] = "c:/temp/test1.xls";
            cvsFiles[1] = "c:/temp/GLTest.xls";
            io.combineTwoExcels(cvsFiles[0],  cvsFiles[1]);
            System.out.println("DONE");
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}

