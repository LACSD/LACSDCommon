package org.lacsd.common.action;

/******************************************************************************
//* Copyright (c) 2003 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:       LACSDGenericAction.java
//* Revision:       1.0
//* Author:         M Feinberg
//* Created On:     07-16-2003
//* Modified by:    HHO
//* Modified On:    09-21-2008.  Adding two overload methods: generateReportLink and showReportPopup
//*                 
//* Description:    Super Class for all LACSD Struts Actions-
//*                 Manages Security / Exception handling
/******************************************************************************/
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.lacsd.common.authentication.LACSDAuthenticationPO;
import org.lacsd.common.authentication.LACSDUser;
import org.lacsd.common.authentication.UserProfile;
import org.lacsd.common.constants.LACSDPrintConstants;
import org.lacsd.common.constants.LACSDReportConstants;
import org.lacsd.common.constants.LACSDWebConstants;
import org.lacsd.common.exceptions.LACSDDb2Exception;
import org.lacsd.common.exceptions.LACSDEciException;
import org.lacsd.common.exceptions.LACSDException;
import org.lacsd.common.exceptions.LACSDOracle10Exception;
import org.lacsd.common.exceptions.LACSDSqlSrvr2KException;
import org.lacsd.common.process.LACSDActivityPO;
import org.lacsd.common.security.SecurityRoleMap;
import org.lacsd.common.service.DateConvert;
import org.lacsd.common.service.LACSDFileIO;
import org.lacsd.common.util.ApplicationContext;
import org.lacsd.common.util.LACSDErrorMapping;
import org.lacsd.common.values.ActivityVO;
import org.lacsd.common.values.EmployeeRoleVO;
import org.lacsd.common.values.EmployeeVO;
import org.lacsd.reporting.values.ReportVO;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public abstract class LACSDGenericAction extends Action {

private final String CLASS_NAME = this.getClass().getName();
protected Logger log = LogManager.getLogger(CLASS_NAME);

private HashMap<String, LACSDErrorMapping> errorForwardMap;    //  Each Action will register it's error forwards / msgs

public static final String ACTION_NAME_MULTI_VALIDATE     =     "multiValidate";

public static final String FORWARD_LOGIN_SCREEN           =     "login-screen";
public static final String FORWARD_LOGIN_ACTION           =     "login-action";
public static final String FORWARD_WELCOME_PAGE           =     "welcome-page";

public static final String FORWARD_APP_FAILURE            =     "app-failure";
public static final String FORWARD_DB2_FAILURE            =     "db2-failure";
public static final String FORWARD_SQL2K_FAILURE          =     "sql2k-failure";
public static final String FORWARD_ORACLE_FAILURE         =     "oracle-failure";
public static final String FORWARD_ECI_FAILURE            =     "eci-failure";

public static final String MESSAGE_APP_FAILURE            =     "error.app.failure";
public static final String MESSAGE_DB2_FAILURE            =     "error.db2.failure";
public static final String MESSAGE_SQL2K_FAILURE          =     "error.sql2k.failure";
public static final String MESSAGE_ORACLE_FAILURE         =     "error.oracle.failure";
public static final String MESSAGE_ECI_FAILURE            =     "error.eci.failure";

public static final String MESSAGE_FAILED_MULTIVALIDATE   =     "error.failed.multivalidate";
public static final String MESSAGE_FRAMEWORK_NOTUSED      =     "error.framework.notused";
public static final String MESSAGE_BAD_ACTIONFORWARD      =     "error.badactionfwd";
public static final String MESSAGE_RECOVERABLE_NULL       =     "error.recoverable.notfound";

public static final String MESSAGE_SECURITY_FAILURE       =     "error.security.failure";
public static final String MESSAGE_SECURITY_NO_PERMIT     =     "error.security.notauthorized";

// for menu
public static final String MENU_TAG_NAME	= "menu";
public static final String FORM_NAME		= "formName";
public static final String ACTION_NAME		= "actionName";

/**
 * Constructor initializes static controller forwards
 * 
*/
public LACSDGenericAction() {
    super();
    errorForwardMap = new HashMap<String, LACSDErrorMapping>();
    initErrControl();
}

/**
 * Execute With Result - Primary Execution Block for LACSD Struts Framework
 * <code>Subclasses of LACSD Framework must implement this method</code>
 * @param String actionName 
 * @param ActionMapping mapping
 * @param ActionForm form
 * @param HttpServletRequest req
 * @param HttpServletReponse res
 * @return ActionForward
 * @throws LACSDException
 * @throws Throwable
*/
protected abstract ActionForward executeWithResult( String actionName,
                                                        ActionMapping mapping,
                                                        ActionForm form,
                                                        HttpServletRequest req,
                                                        HttpServletResponse res) 
                                                        throws LACSDException, Throwable;

/**
 * Similar to Execute With Result, SubSystem Applications like (RDXGenericAction)
 * will have the opportunity to activate this method before primary action processing
 * occurs.   This method is intended to contain code for global system lockout conditions
 * that might occur durign scheduled maintenence, downtime, or durable batch processes.
 * @param String actionName 
 * @param ActionMapping mapping
 * @param ActionForm form
 * @param HttpServletRequest req
 * @param HttpServletReponse res
 * @return ActionForward
 * @throws LACSDException
 * @throws Throwable
*/
protected abstract ActionForward executeApplicationGlobal(String actionName,
                                                        ActionMapping mapping,
                                                        ActionForm form,
                                                        HttpServletRequest req,
                                                        HttpServletResponse res) 
                                                        throws LACSDException, Throwable;

/**
 * Register use-case specific error codes when the Action Class is instantiated.
 * Error codes are used by the controller to properly map error dialog boxes
 * and forward JSP pages when a recoverable exeception is thrown by the DAO tier.
 * 
 * Note: Implementors of this method should use the "regRecoverableErr()" feature
 * of this class to register error codes, messages, and forwards.
 * 
 * <code>Subclasses of LACSD Framework must implement this method</code>
 * @return void
*/
protected abstract void initErrControl();


/**
 * Primary (TOP LEVEL) Execute Method - Jakarta Struts Framework v1.1
 * @param ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletReponse res
 * @return ActionForward
 * @throws IOException, ServletException
*/
public ActionForward execute(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res) throws IOException, ServletException {
	ActionForward result = null;
    String actionName = null;
    
    //  1)  PARSE GENERIC FORM
    //****************************************************************************/
    LACSDGenericForm genericForm = null;
    if (form instanceof LACSDGenericForm) {
         genericForm = (LACSDGenericForm)form;
         actionName = genericForm.getActionName();
    }
    else {
        this.addError(LACSDWebConstants.ERROR_MESSAGE,MESSAGE_APP_FAILURE,MESSAGE_FRAMEWORK_NOTUSED,req,true);
        return mapping.findForward(FORWARD_APP_FAILURE);
    }
    
    //  2)  ONLY ALLOW VALID USER SESSIONS -
    //****************************************************************************/

    UserProfile profile = getUserProfile(req);
    
    if (profile != null) {

    	try {
    		
    		// Check against the database to make sure 
    		// that the session is really still active
    	    LACSDAuthenticationPO authenticationPO = new LACSDAuthenticationPO();
        	if (!profile.isBypassSecurity()) {
        	    profile = authenticationPO.verifyLogin(profile);
        	    
        	}

            //  A)  Check if UserProfile is Logged In
            if (!profile.isLoggedIN()) {
                req.getSession().invalidate();  //  Kill HTTPSession
                return mapping.findForward(FORWARD_LOGIN_SCREEN);
            }
    	} catch (Throwable t) {
            if (t instanceof LACSDException) {
                LACSDException converted = (LACSDException)t;
                return examineLACSDException(converted,mapping,req);
            }
            else {
                log.error(t.getMessage(),t);
                this.addError(LACSDWebConstants.ERROR_MESSAGE,MESSAGE_APP_FAILURE,t.getMessage() + "\n" + t.fillInStackTrace(),req);
                return mapping.findForward(FORWARD_APP_FAILURE);
            }
    	}
    }
    else {

    	// if profile is null,
    	// verify log-in from DB2
        HttpSession session = req.getSession(true);
        String ipaddress = req.getRemoteAddr();
        if (ipaddress.equals("127.0.0.1")) {
            ipaddress = java.net.InetAddress.getLocalHost().getHostAddress();
        }
        // set up variables to verify log-in
        profile = new LACSDUser();
        profile.setIPAddress(ipaddress);
        profile.setHttpSessionID(session.getId());

        EmployeeVO employeeVO = new EmployeeVO();
        ApplicationContext appCtx = ApplicationContext.getInstance();
        EmployeeRoleVO employeeRoleVO = new EmployeeRoleVO();
        employeeRoleVO.setApplicationID(appCtx.get(LACSDWebConstants.APPLICATION_ID));
        employeeVO.setEmployeeRoleVO(employeeRoleVO);
        profile.setEmployeeVO(employeeVO);

        LACSDAuthenticationPO authenticationPO = new LACSDAuthenticationPO();
        try {
            profile = authenticationPO.verifyLogin(profile);
        } catch (Throwable t) {
            if (t instanceof LACSDException) {
                LACSDException converted = (LACSDException)t;
                return examineLACSDException(converted,mapping,req);
            }
            else {
                log.error(t.getMessage(),t);
                this.addError(LACSDWebConstants.ERROR_MESSAGE,MESSAGE_APP_FAILURE,t.getMessage() + "\n" + t.fillInStackTrace(),req);
                return mapping.findForward(FORWARD_APP_FAILURE);
            }
        }

        if (! profile.isLoggedIN()) {
            req.getSession().invalidate();  //  Kill HTTPSession
            //  B)  UserProfile object not found in memory - Forward to Login Action
            return mapping.findForward(FORWARD_LOGIN_ACTION);
//            return mapping.findForward(FORWARD_LOGIN_SCREEN);
        }
    }
    
    setUserProfile(profile, req);
    //  3)  HANDLE MULTI-VALIDATION REDIRECT - 
    //****************************************************************************/
    //      -> If form validation can return to multiple pages, the 
    //      -> actionName attribute should be set to "multiValidate"
    if ((actionName != null) && (actionName.equalsIgnoreCase(ACTION_NAME_MULTI_VALIDATE))) { 
                
        String multiValidate = genericForm.getMultiValidate();
        
        if ((multiValidate != null) && (multiValidate.length() > 0)) {
            return mapping.findForward(multiValidate);
        }
        else {
            this.addError(LACSDWebConstants.ERROR_MESSAGE,MESSAGE_APP_FAILURE,MESSAGE_FAILED_MULTIVALIDATE,req,true);
            return mapping.findForward(FORWARD_APP_FAILURE);
        }
        
    }
    
    //  4)  ENFORCE ROLE-BASED SECURITY FOR A PARTICULAR ACTIVITY
    //****************************************************************************/

    try {
        //  Acquire Security Role Map - Compare this user's ROLEID against the role map!
        String servletPath = req.getServletPath();
        if (servletPath.startsWith("/")) {
    		servletPath = servletPath.substring(1);
    	}
        String applicationID = profile.getEmployeeVO().getEmployeeRoleVO().getApplicationID();
        SecurityRoleMap securityRoleMap = SecurityRoleMap.getInstance();
           
        boolean permitted = false;
        if (profile.isBypassSecurity()) {
            permitted = true;
        } else {
            permitted = securityRoleMap.isPermittedStruts(servletPath, actionName, profile.getEmployeeVO().getEmployeeRoleVO());
        }

        if ((!permitted) && (actionName != null) && (!actionName.equalsIgnoreCase("legacy"))) {
            
            String activityDesc = new LACSDActivityPO().getActivitieDescription(applicationID,servletPath,actionName);
            
            this.addError(LACSDWebConstants.ERROR_MESSAGE,MESSAGE_SECURITY_NO_PERMIT,activityDesc,req);

            if ( genericForm.getCallingPage() != null ) {
                return new ActionForward(genericForm.getCallingPage(),false);
            } else {
                return mapping.findForward(FORWARD_WELCOME_PAGE);
            }
        }
    }
    catch(Throwable t) {
        if (t instanceof LACSDException) {
            LACSDException converted = (LACSDException)t;
            return examineLACSDException(converted,mapping,req);
        }
        else {
            log.error(t.getMessage(),t);
            this.addError(LACSDWebConstants.ERROR_MESSAGE,MESSAGE_APP_FAILURE,t.getMessage() + "\n" + t.fillInStackTrace(),req);
            return mapping.findForward(FORWARD_APP_FAILURE);
        }
    }
    
    //  5)  ACTIVATE SUB-SYSTEM APPLICATION 
    //****************************************************************************/
    //      -> Example: RDXGenericAction will lock-out RDX Application during
    //      -> scheduled batch processes
    try {
        result = executeApplicationGlobal(actionName,mapping,form,req,res);

        if (result == null){
            this.addError(LACSDWebConstants.ERROR_MESSAGE,MESSAGE_APP_FAILURE,MESSAGE_BAD_ACTIONFORWARD,req,true);
            result = mapping.findForward(FORWARD_APP_FAILURE);
        }
    }
    
    //  6)  HANDLE ERROR CONDITIONS
    //****************************************************************************/
    catch (LACSDException e) {
        
        return examineLACSDException(e,mapping,req);
    }
    catch (Throwable t) {   //  This would happen as a result of EJB Failure throwing throwable
                            //  or any other non-expected runtime exception in the application
                            
        if (t instanceof LACSDException) {
            
            LACSDException converted = (LACSDException)t;
            return examineLACSDException(converted,mapping,req);
        }
        else {
            log.error(t.getMessage(),t);
            this.addError(LACSDWebConstants.ERROR_MESSAGE,MESSAGE_APP_FAILURE,t.getMessage() + "\n" + t.fillInStackTrace(),req);
            return mapping.findForward(FORWARD_APP_FAILURE);
        }
    }
    
    //  END)    RETURN RESULT OF "executeWithResult()"
    //****************************************************************************/
    return result;
}


/** ** ** ** ** ** **>>>>  END OF PUBLIC METHODS <<<<<** ** ** ** ** ** ** ** ** **/

//This method was made public so that the subclass(OSXGenericAction) could access it
/**
 * Examine LACSDException at runtime - Determine if Fatal or Recoverable
 * @param LACSDException e
 * @param ActionMapping mapping
 * @param HttpServletRequest req
 * @return ActionForward
 * @throws Throwable
*/
public ActionForward examineLACSDException(LACSDException e, ActionMapping mapping, HttpServletRequest req) {

    // determine if recoverable or not
    if (e.isRecoverable()) {
            
        //  12-17-2003 -> Get recoverable message key and forward pages from Action Registry
        LACSDErrorMapping eMapping = getErrorMapping(e);

        //  This exception will occur if the developer neglected to register a recoverable error code
        //  in the initialization sequence of his struts action.
        if (eMapping == null) {
            
            // error was originally deemed recoverable, but has not been registered - 
            // if this error was sent from a stored procedure, give a "special" message with SP Info
            if (e instanceof LACSDDb2Exception) {
                this.addError(LACSDWebConstants.ERROR_MESSAGE,MESSAGE_APP_FAILURE,e.getUnhandledLACSDErrorCode(),req);
                return mapping.findForward(FORWARD_DB2_FAILURE);
            }
            else if (e instanceof LACSDSqlSrvr2KException) {
                this.addError(LACSDWebConstants.ERROR_MESSAGE,MESSAGE_APP_FAILURE,e.getUnhandledLACSDErrorCode(),req);
                return mapping.findForward(FORWARD_SQL2K_FAILURE);
            }
            //03-27-2007 ->  Added Oracle Exception Handling
            else if (e instanceof LACSDOracle10Exception) {
                this.addError(LACSDWebConstants.ERROR_MESSAGE,MESSAGE_APP_FAILURE,e.getUnhandledLACSDErrorCode(),req);
                return mapping.findForward(FORWARD_ORACLE_FAILURE);
            }                        
            else {
                return processNonRecoverableException(e, mapping, req);
            }
        }
                
        String errorMessageKey = eMapping.getErrorMessageKey();
        this.addError(LACSDWebConstants.ERROR_MESSAGE,errorMessageKey,e.getRootCause(),req);
                
        //  REVISED 12-17-2003 ->   3% of Actions have Dynamic FWD Key (derived from VO)
        //                          These forwards are set into LACSDException at the DAO Tier
        //                          Example:  RDXSwitchAccount Use Case
        if (e.isDynamicForward()) {
            return new ActionForward(e.getDynamicForwardURL());
        }

        
        //  REVISED 06-10-2004 ->   Just as above, some actions will have Dynamic FWD Key (derived)
        //                          from the Form itself (not DAO Tier)
        //                          Example:    RDXReport Use Case
        if (eMapping.isForwardFullyQualifiedURI()) {
            return new ActionForward(eMapping.getForwardPageKey()); // note this is not a KEY.  it is the URI.
        }
        else {
            String recoverableFwd  = eMapping.getForwardPageKey();
            if (recoverableFwd == null) {
                this.addError(LACSDWebConstants.ERROR_MESSAGE,MESSAGE_APP_FAILURE,MESSAGE_RECOVERABLE_NULL,req,true);
                return mapping.findForward(FORWARD_APP_FAILURE);
            }
            return mapping.findForward(recoverableFwd);
        }
    }
    else {
        return processNonRecoverableException(e, mapping, req);
    }
}


/**
 * Handle exceptions that are deemed "Non-Recoverable" and forward 
 * to a generic system "splat" page
 * @param LACSDException e
 * @param ActionMapping mapping
 * @param HttpServletRequest req
 * @return ActionForward
 * @throws Throwable
*/
private ActionForward processNonRecoverableException(LACSDException e, ActionMapping mapping, HttpServletRequest req) {

    log.error(e.getMessage(),e.getRootCause());

    // DB2 Splat Page
    if (e instanceof LACSDDb2Exception) {
        this.addError(LACSDWebConstants.ERROR_MESSAGE,MESSAGE_DB2_FAILURE,(e.getMessage() + "\n" + e.getRootCause()),req);
        return mapping.findForward(FORWARD_DB2_FAILURE);
    }

    // SQL Server 2000 Splat Page
    else if (e instanceof LACSDSqlSrvr2KException) {
        this.addError(LACSDWebConstants.ERROR_MESSAGE,MESSAGE_SQL2K_FAILURE,(e.getMessage() + "\n" + e.getRootCause()),req);
        return mapping.findForward(FORWARD_SQL2K_FAILURE);
    }

    // 03/27/2007 -> Added Oracle Handling
    // Oracle Splat Page
    else if (e instanceof LACSDOracle10Exception) {
        this.addError(LACSDWebConstants.ERROR_MESSAGE,MESSAGE_ORACLE_FAILURE,(e.getMessage() + "\n" + e.getRootCause()),req);
        return mapping.findForward(FORWARD_ORACLE_FAILURE);
    }

    // ECI Splat Page
    else if (e instanceof LACSDEciException) {
        this.addError(LACSDWebConstants.ERROR_MESSAGE,MESSAGE_ECI_FAILURE,(e.getMessage() + "\n" + e.getRootCause()),req);
        return mapping.findForward(FORWARD_ECI_FAILURE);
    }

    // BY DEFAULT ->> All remaining unknown / unhandled exceptions are JAVA APPLICATION SPLAT PAGE!
    else {
    	if (e.getRootCause() != null) {
            this.addError(LACSDWebConstants.ERROR_MESSAGE,MESSAGE_APP_FAILURE,(e.getMessage() + "\n" + e.getRootCause()),req);
    	} else {
    		this.addError(LACSDWebConstants.ERROR_MESSAGE,MESSAGE_APP_FAILURE,(e.getMessage() + "\n"),req);
    	}
        return mapping.findForward(FORWARD_APP_FAILURE);
    }
}


/**
 * Add ActionErrors to request object
 * @param String errKey (for JSP Screen)
 * @param String propertyFileKey (for Text File)
 * @param HttpServletRequest req
 * @return void
*/
protected void addError(String errKey, String propertyFileKey, HttpServletRequest req) {
    this.addError(errKey, propertyFileKey, null, req);
}

/**
 * Add ActionErrors to request object
 * @param String errKey (for JSP Screen)
 * @param String propertyFileKey (for Text File)
 * @param String dynamicMessage (for rootCause)
 * @param HttpServletRequest req
 * @return void
*/
protected void addError(String errKey, String propertyFileKey, Object dynamicCause, HttpServletRequest req) {
    
    addError(errKey,propertyFileKey,dynamicCause,req,false);

}

/**
 * Add ActionErrors to request object
 * - Get Cause Message from Message Key from error properties file (if boolean is true)
 * otherwise, use the message sent in from caller (@param dynamicMessage)
 * 
 * @param String errKey (for JSP Screen)
 * @param String propertyFileKey (for Text File)
 * @param String dynamicMessage (for rootCause)
 * @param HttpServletRequest req
 * @param boolean dynaCauseIsKeyed
 * @return void
*/
protected void addError(String errKey, String propertyFileKey, Object dynamicCause, HttpServletRequest req, boolean dynaCauseIsKeyed) {
    
    ResourceBundle rb = ResourceBundle.getBundle(LACSDWebConstants.MESSAGE_RESOURCES_FILE, new Locale("en","US"));
    
    ActionMessages errors = new ActionMessages();
    ActionMessage error = null;
    if (dynaCauseIsKeyed) {
        String cause = rb.getString(dynamicCause.toString());
        error = new ActionMessage(propertyFileKey,cause);
    }
    else {
        error = new ActionMessage(propertyFileKey,dynamicCause);
    }
    
    errors.add(errKey,error);       // struts
    this.saveErrors(req,errors);    // struts
    
    /** LACSD FLAVOUR **/
    String message = rb.getString(propertyFileKey);
    req.setAttribute(LACSDWebConstants.ERROR_MESSAGE,message);

    if (dynamicCause != null) {
        
        if (dynaCauseIsKeyed) {
            String cause = rb.getString(dynamicCause.toString());
            req.setAttribute(LACSDWebConstants.ERROR_ROOT_CAUSE,cause);
        }
        else {
            req.setAttribute(LACSDWebConstants.ERROR_ROOT_CAUSE,dynamicCause.toString());
        }
    }
}

/**
 * Add ActionMessages to request object
 * @param String msgKey (for JSP Screen)
 * @param String propertyFileKey (for Text File)
 * @param HttpServletRequest req
 * @return void
*/
protected void addMessage(String msgKey, String propertyFileKey, HttpServletRequest req) {

    ActionMessages errors = new ActionMessages();
    ActionMessage error = new ActionMessage(propertyFileKey);
    errors.add(msgKey,error);
    this.saveMessages(req,errors);
    
    ResourceBundle rb = ResourceBundle.getBundle(LACSDWebConstants.MESSAGE_RESOURCES_FILE, new Locale("en","US"));
    String message = rb.getString(propertyFileKey);
    req.setAttribute(LACSDWebConstants.INFO_MESSAGE,message);
}

/**
 * Add ActionMessages to request object
 * @param String msgKey (for JSP Screen)
 * @param String propertyFileKey (for Text File)
 * @param HttpServletRequest req
 * @return void
*/
protected void addMessage(String msgKey, String propertyFileKey, String[] dynamicMessages, HttpServletRequest req) {

    ActionMessages errors = new ActionMessages();
    ActionMessage error = new ActionMessage(propertyFileKey,dynamicMessages);
    errors.add(msgKey,error);
    this.saveMessages(req,errors);
    
    ResourceBundle rb = ResourceBundle.getBundle(LACSDWebConstants.MESSAGE_RESOURCES_FILE, new Locale("en","US"));
    String message = rb.getString(propertyFileKey);
    
    // parse {N} and replace with dynamicMessages[]
    StringBuffer sb = new StringBuffer();
    StringTokenizer st = new StringTokenizer(message," ", true);
    while (st.hasMoreTokens()) {
        String token = st.nextToken();
        String index = null;
        String dynamicmessage = null;
        int arrayPos = 0;
        if ((token != null) && (token.startsWith("{")) && (token.endsWith("}"))) {
            index = token.substring(1,2);
        }
        if (index != null) {
            try {
                arrayPos = Integer.parseInt(index);
                dynamicmessage = dynamicMessages[arrayPos];
            }
            catch (NumberFormatException nfe) {
                continue;
            }
            catch (ArrayIndexOutOfBoundsException aoe) {
                dynamicmessage = token;
            }
        }
        if (dynamicmessage != null) {
            sb.append(dynamicmessage);
        }
        else {
            sb.append(token);
        }
    }
    req.setAttribute(LACSDWebConstants.INFO_MESSAGE,sb.toString());
}


/**
 * Add a confirm message (which requires user's response) to request object
 * The presence of the message will generate JavaScript confirm box with the provided message
 * @param String msg (for JSP Screen)
 * @param HttpServletRequest req
 * @return void
 */
protected void addConfirm(String msg, String argActionName, String argAction, HttpServletRequest req) {

    ResourceBundle rb = ResourceBundle.getBundle(LACSDWebConstants.MESSAGE_RESOURCES_FILE, new Locale("en","US"));
    msg = rb.getString(msg);
    
    req.setAttribute(LACSDWebConstants.CONFIRM_MESSAGE, msg);
    req.setAttribute(LACSDWebConstants.ACTION, argAction);
    req.setAttribute(LACSDWebConstants.ACTION_NAME, argActionName);
}

/**
 * Return a copy of the UserProfile Object -
 * This object lives in the User's HTTPSESSION
 * @param HttpServletRequest req
 * @return UserProfile
*/
protected UserProfile getUserProfile(HttpServletRequest req) {

    HttpSession session = req.getSession(false);
    UserProfile profile = (UserProfile)session.getAttribute(LACSDWebConstants.USER_PROFILE);
    return profile;
}

/**
 * Allow subclass to register parameters for error messages 
 * and page forwarding constants..
 * 
 * @see standard usage signature overloaded below
 * 
 * ADDED 06/10/2004 
 * Overloaded version of regRecoverableErr per Reporting requirement to return to a 
 * dynamically registered input screen.  Extra boolean value in eMapping class will
 * indicate that the fully qualified URI String, eg "bonds.do?actionName=setup" should
 * be used by the controller to redirect, instead of the standard actionName string,
 * eg: "setup".
 * 
 * @param String errCode
 * @param String spName
 * @param String errorMessage
 * @param String forwardPage
 * @param boolean useDynamicInputFwd
 * @return void
*/
protected synchronized void regRecoverableErr(String errCode, String spName, String errorMessage, String forwardPage, boolean useDynamicInputFwd) {
    
    LACSDErrorMapping eMapping = new LACSDErrorMapping();
    
    eMapping.setErrCode(errCode);
    eMapping.setStoredProcedureName(spName);
    eMapping.setErrorMessageKey(errorMessage);
    eMapping.setForwardPageKey(forwardPage);

    //  06-10-2004 - added boolean parameter to eMapping to determine "standard" action Fwd or "fully qualified" forward
    if (useDynamicInputFwd) {
        eMapping.setForwardFullyQualifiedURI(useDynamicInputFwd);
    }

    //  05-11-04 - expanded error mapping to hold multiple stored procedures
    String errKey = (spName + errCode);
    if (!errorForwardMap.containsKey(errKey)) {
        errorForwardMap.put(errKey,eMapping);
    }
    else {
        if (useDynamicInputFwd) {  // herein lies a possible race-condition when erroring from the same report, but coming from different inputs
            errorForwardMap.remove(errKey); // this is dynamic (per reporting screens, etc) so remove old one
            errorForwardMap.put(errKey,eMapping);
        }
    }
}

/**
 * Overloaded version of regRecoverableErr per code-review findings of RDX Switch Accounts.
 * Actions that use dynamic forwarding and custom recoverable error handling do not need
 * to specify a "forward" page, as it is presumed to be encapsulated in LACSDException object.
 * Forward page is set to null here.
 * @param String errCode
 * @param String spName
 * @param String errorMessage
 * @return void
*/
protected void regRecoverableErr(String errCode, String spName, String errorMessage) {
    regRecoverableErr(errCode,spName,errorMessage,null);
}

/**
 * Allow subclass to register parameters for error messages 
 * and page forwarding constants - Standard Usage
 * @param String errCode
 * @param String spName
 * @param String errorMessage
 * @param String forwardPage
 * @return void
*/
protected synchronized void regRecoverableErr(String errCode, String spName, String errorMessage, String forwardPage) {
    regRecoverableErr(errCode,spName,errorMessage,forwardPage,false);
}

/**
 * Convenience method for the error-handling section of
 * the Execute() method to derive the appropriate error
 * mapping from an LACSD Recoverable exception
 * @param LACSDException
 * @return LACSDErrorMapping
*/
private LACSDErrorMapping getErrorMapping(LACSDException e) {

    LACSDErrorMapping theMapping = null;
    
    Iterator<String> it = errorForwardMap.keySet().iterator();
    while (it.hasNext()) {
        String key = (String)it.next();
        LACSDErrorMapping map = (LACSDErrorMapping)errorForwardMap.get(key);
        String errCode = map.getErrCode();
        String spcName = map.getStoredProcedureName();
        
        // patch 09-16-04 (MF) - e.getProcName may have bug introduced from old WBR admin screen
        e.setStoredProcedureName(e.getStoredProcedureName().trim());
        
        if (errCode != null && spcName != null) {
            // patch 11-19-04 (MF) - db2 8 causes error codes to have white space at the end - trim() added
            if ((errCode.equalsIgnoreCase(e.getErrorCode().trim())) && (spcName.equalsIgnoreCase(e.getStoredProcedureName()))) {
                theMapping = map;
                break;
            }
        }
    }
    return theMapping;
}

/**
 * Update the UserProfile object
 * This object lives in the User's HTTPSESSION
 * @param UserProfile profile
 * @param HttpServletRequest req
 * @return void
*/
private void setUserProfile(UserProfile profile, HttpServletRequest req) {

    HttpSession session = req.getSession(true);
    session.setAttribute(LACSDWebConstants.USER_PROFILE,profile);
}


/**
 * Common method triggers the SHOW REPORT POPUP include file
 * @param ReportVO reportVO
 * @param HttpServletRequest req
 * @return void
 * @throws LACSDException
*/
protected String copyReportFile(byte[] actualReport, String fileExtension, String fileDir, String timestampString ) throws LACSDException {
   
    // 1)   Establish Temporary Directory AND URL Pattern for report File
    ServletContext sc = this.getServlet().getServletContext();
    String tempdir = (sc.getRealPath("/") + fileDir);   //  local web directory 
    File dir = new File(tempdir);
    if (!dir.exists()) {
        dir.mkdir();
    }
    
    String tempFile = new String(timestampString + fileExtension);
    
    String tempCanonicalFile = new String(tempdir + tempFile);

    // 2)   Marshall byte[] array to Local Temporary PDF File    
    if (actualReport == null) {
        throw new LACSDException("Cannot open Report - No Data Available");
    }
    LACSDFileIO fileIO = LACSDFileIO.getInstance();
    fileIO.setBytesToFile(new File(tempCanonicalFile),actualReport, false);
    
    // 3)   Establish Temporary URL for client to access PDF via HTTP   
    String tempURL = new String(fileDir + tempFile);
    
    return tempURL;
    
    
}



/**
 * Generate link for reports
 * @param reportVO
 * @param req
 * @return
 * @throws LACSDException
 */
protected ReportVO generateReportLink(ReportVO reportVO, HttpServletRequest req) throws LACSDException {

    
UserProfile user = this.getUserProfile(req);
String email = user.getEmployeeVO().getEmailAddress();


DateConvert dc = DateConvert.getInstance();
String timestampString = dc.getStringOfSQLTimestamp(System.currentTimeMillis());
timestampString = ("-" + timestampString.replace(' ','-').replace(':','-').replace('.','-'));

// 1)   Dynamically Name TEMP PDF File :  example -> "CustomerStatement1-2004-03-17-22-31-22-98.pdf"
if ( email == null )
{
 email = "";
 timestampString = (reportVO.getReportName() + timestampString);
}
else {
timestampString = (email + "_" + reportVO.getReportName() + timestampString);
}

String tempURL;

if (reportVO.getOutputFormat().equalsIgnoreCase(LACSDReportConstants.EXCEL_FORMAT)) {
    tempURL = copyReportFile(reportVO.getCompletedFile(),LACSDPrintConstants.MS_EXCEL,LACSDPrintConstants.MS_EXCEL_WEBDIR,timestampString);
    reportVO.setWebDownloadURL( tempURL );
}else if (reportVO.getOutputFormat().equalsIgnoreCase(LACSDReportConstants.PDF_FORMAT)){
    tempURL = copyReportFile(reportVO.getCompletedFile(),LACSDPrintConstants.ADOBE_PDF,LACSDPrintConstants.ADOBE_PDF_WEBDIR,timestampString); 
    reportVO.setWebDownloadURL( tempURL );
}else {
}
return reportVO;
}


/**
* Common method triggers the SHOW REPORT POPUP include file
* @param ReportVO reportVO
* @param HttpServletRequest req
* @return void
* @throws LACSDException
*/
protected void showReportPopup(ReportVO reportVO, HttpServletRequest req) throws LACSDException {

 req.setAttribute("downloadURL",reportVO.getWebDownloadURL());
 req.setAttribute("showPopup","true");
}

/**
 * Hold Objects Temporarily In HttpSession
 * @param String key
 * @param Object value
 * @param HttpServletRequest req
 * @return void
*/
protected void setObjectToSession(String key, Object value, HttpServletRequest req) {

    HttpSession session = req.getSession();
    session.setAttribute(key,value);
}

/**
 * Get Objects From HttpSession - returns a copy of the object
 * @param String key
 * @param HttpServletRequest req
 * @return object
*/
protected Object getObjectFromSession(String key, HttpServletRequest req) {
    
    HttpSession session = req.getSession();
    Object obj = session.getAttribute(key);
    return obj;
}

/**
 * Release Objects From HttpSession - returns a copy of the object
 * @param String key
 * @param HttpServletRequest req
 * @return object
*/
protected Object releaseObjectFromSession(String key, HttpServletRequest req) {
    
    HttpSession session = req.getSession();
    Object obj = session.getAttribute(key);
    session.removeAttribute(key);
    return obj;
}

/**
 * Regenerate menu.xml into DOM based on user's role and allowed activities and save into Session
 * @param UserProfile
 * @param String xmlMenuPath
 * @throws LACSDException
 * @throws IOException
 * @throws SAXException
 */
protected Document regenerateMenuDOMBasedOnRole(UserProfile userProfile, String xmlMenuPath) throws LACSDException, IOException, SAXException, ParserConfigurationException {

	// get allowed access
	ArrayList<ActivityVO> allowedActs = userProfile.getEmployeeVO().getEmployeeRoleVO().getActivities();

	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	dbf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", false); // allow <!DOCTYPE>
	DocumentBuilder db = dbf.newDocumentBuilder();

	// parse xml into document and loop through nodes
	Document doc = db.parse(new File(xmlMenuPath));
	NodeList nodeList = doc.getElementsByTagName(MENU_TAG_NAME);

	for (int i=0; i<nodeList.getLength(); i++) {

		Node node = (Node) nodeList.item(i);
		
		boolean isShowOnMenu = false;

		// get each attribute of node
		if (node.hasAttributes()) {
			NamedNodeMap namedNodeMap = node.getAttributes();
			String formName = "";
			String actName = "";
			for (int j=0; j<namedNodeMap.getLength(); j++) {
				String attr = namedNodeMap.item(j) + "";
				int pos = attr.length()-1; 					// -1 for the ending "
				// extract form name
				if (attr.startsWith(FORM_NAME)) {			// i.e. formName="CalPers.do"
					int length = FORM_NAME.length() + 2;	// Add 2 for '="'
					formName = attr.substring(length, pos); // i.e. now CalPers.do
				}
				// extract action name
				else if (attr.startsWith(ACTION_NAME)) {
					int length = ACTION_NAME.length() + 2;
					actName = attr.substring(length, pos);
				}
			}

			if (formName.equals("") || actName.equals("")) {
				continue; // do not remove node
			} else {

				// loop through list of user's allowed activities
				for (int k=0; k<allowedActs.size(); k++) {
					ActivityVO eachAct = (ActivityVO) allowedActs.get(k);

					// compare form name/action name of each node against list of allowed activities
					if (eachAct.getFormName().equals(formName) && eachAct.getActivityProperty().equals(actName)) {
						isShowOnMenu = true;
						break;
					}
				}
			}
		}

		// remove node that's not on the list of allowed activities
		if (! isShowOnMenu) {
			node.getParentNode().removeChild(node);
		}
	}

	return doc;
}

}