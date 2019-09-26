package org.lacsd.common.action;

/******************************************************************************
//* Copyright (c) 2003 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:       LACSDLogoutForm.java
//* Revision:       1.4
//* Author:         MFEINBERG@LACSD.ORG
//* Created On:     10-21-2003
//* Modified By:    tnguyen@lacsd.org
//* Modified On:    07-14-2004
//*                 Change to generic form for all applications
//*                 
//* Description:    Action Form for User Logout
/******************************************************************************/

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

public class LACSDLogoutForm extends LACSDGenericForm {


private static final long serialVersionUID = 6452537175055561077L;

/**
 * Reset Method
 * @param ActionMapping mapping
 * @param HttpServletRequest req
 * @return void
*/
public void reset(ActionMapping mapping, HttpServletRequest req) {
    // no implementation
}

/**
 * Validate Method
 * @return ActionErrors
 * @param ActionMapping mapping
 * @param HttpServletRequest req
*/
public ActionErrors validate(ActionMapping mapping, HttpServletRequest req) {

    //  no implementation - Struts Config Validation Deactivated
    return null;
}
}

