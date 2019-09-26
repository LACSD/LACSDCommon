package org.lacsd.common.constants;

/******************************************************************************
 //* Copyright (c)   2008 Sanitation Districts of Los Angeles. All Rights Reserved.
 //* Filename:		LACSDReportConstants.java
 //* Revision: 		version 1.0
 //* Author:			hho@lacsd.org
 //* Created On: 	Sep 16, 2008
 //* Modified By:
 //* Modified On:
 //*
 //* Description:	Constants class containing all IServer Reports
 //*
 /*****************************************************************************/

public class LACSDReportConstants {

	public final static String IPORTAL_PASSWORD                     = "5Xebw4dz/TmAYJTMbe3bPQ==";
    public static final String AMX_FOLDER                           = "/EAM Custom Reports";
    public static final String SRP_FOLDER                           = "/Service Request";
    public static final String RDX_FOLDER                           = "/Refuse Disposal";
    public static final String LHX_FOLDER                           = "/Local Sewer House Connection";
    public static final String IWL_FOLDER                           = "/Hauled Liquid Waste Disposal Program";
    public static final String IWC_FOLDER                           = "/Industrial Waste Supplemental Sample System";
    public static final String WAT_FOLDER                           = "/WATS Executive Information";
    public static final String WSX_FOLDER                           = "/Wastewater Services";
//    public static final String WSX_FOLDER                           = "/WSX";
    public static final String WSXLEGACY_FOLDER                     = "/Wastewater Services/Legacy";
//    public static final String WSXLEGACY_FOLDER                     = "/WSX/Legacy";
    public static final String SSX_FOLDER                           = "/Skire Synchronization";
    
    public static final String WORKGROUP_AUTHID                     = "workgroup";
    
    public static final String BIRT_REPORT                          = "RPTDESIGN";
    public static final String ESPREADSHEET_REPORT                  = "SOX";
    public final static String E_CASCADING_PARAM                    = "eCascading";
    public final static String B_CASCADING_PARAM                    = "bCascading";

    public static final String PDF_FORMAT                           = "PDF";
    public static final String EXCEL_FORMAT                         = "XLS";
    public static final String DAT_FORMAT                           = "DAT";
    public static final String EXPORT_BIRT_TO_EXCEL                 = "EXPORT_BIRT_TO_EXCEL";
    public static final String EXPORT_TO_EXCEL_PARAM                = "ConvertToExcel";
    public static final String SPREADSHEETML_FORMAT                 = "SPREADSHEETML_FORMAT";
    public static final String EXCEL_CONTENT_PARAM                  = "ExcelContent";
 
    public static final String PARAM_TYPE_EMDS                      = "EDMS";
    public static final String PARAM_TYPE_EMAIL                     = "Email";
    public static final String PARAM_TYPE_DUNNING_MSG               = "DunningMessages";
    public static final String PARAM_TYPE_ACCOUNT_ID                = "AccountID";
    public final static String START_MONTH_YEAR_FIELD               = "StartMonthYear";
    public final static String END_MONTH_YEAR_FIELD                 = "EndMonthYear";
    public final static String MONTH_YEAR_FIELD                     = "MonthYear";

    public final static String ISERVER_UNAUTHORIZED_CODE            = "3072";
    public final static String ISERVER_INVALID_USER_CODE            = "3009";
    
    public static final String JOB_SUCCESS       = "Succeeded";
    public static final String JOB_CANCELLED       = "Cancelled";
    public static final String JOB_FAILED       = "Failed";
    
    public static final String NEW_CALPER_START_DATE       = "01/01/2011 08 AM"; //Date starts to run calper job. It is not really actually date 
                                                                           //It is  just used to check when the last time the Calper data is refresh

    public static final String DM_DOC_TYPE_FINANCIAL_MGMT_SC	= "FIN-M-SC"; // Financial Management Service Charge
    public static final String DM_DOC_TYPE_FINANCIAL_MGMT_CF	= "FIN-M-CF"; // Financial Management Connection Fee
}
