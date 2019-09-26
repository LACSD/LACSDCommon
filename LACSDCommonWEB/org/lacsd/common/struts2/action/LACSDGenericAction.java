package org.lacsd.common.struts2.action;

/******************************************************************************
//* Copyright (c) 2003 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:       LACSDGenericAction.java
//* Revision:       1.0
//* Author:         M Feinberg
//* Created On:     07-16-2003
//* Modified by:    HHO
//* Modified On:    05-13-2010. Migrate to Strut2 framework  
//*                 
//* Description:    Super Class for all LACSD Struts Actions-
//*                 Manages Security / Exception handling
/******************************************************************************/

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;
import org.apache.struts2.util.ServletContextAware;
import org.lacsd.common.authentication.UserProfile;
import org.lacsd.common.constants.LACSDPrintConstants;
import org.lacsd.common.constants.LACSDReportConstants;
import org.lacsd.common.constants.LACSDWebConstants;
import org.lacsd.common.exceptions.LACSDException;
import org.lacsd.common.service.DateConvert;
import org.lacsd.common.service.LACSDFileIO;
import org.lacsd.common.util.LACSDErrorAnalyzer;
import org.lacsd.common.values.ActivityVO;
import org.lacsd.common.values.PagingVO;
import org.lacsd.reporting.values.ReportVO;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.opensymphony.xwork2.ActionSupport;


public abstract class LACSDGenericAction extends ActionSupport implements 
			SessionAware, ServletRequestAware, ServletResponseAware, ServletContextAware{

private static final long serialVersionUID = -8867795736034377392L;
protected String actionName;		//	Control parameter/variable for all web screens
protected String multiValidate;		//	Forms with multiple input screens return properly
protected String callingPage;		//	JSP's can include the URL pattern for the calling page for automatic return to caller
protected Map<String, Object> session;
protected HttpServletRequest request;
protected HttpServletResponse response;
protected ServletContext servletContext;
protected HashMap<String, Object> errorForwardMap;
protected PagingVO pagingVO;
private LACSDErrorAnalyzer errorAnalyzer;
//Log4j
private final String CLASS_NAME = this.getClass().getName();
protected Logger log = LogManager.getLogger(CLASS_NAME);

// menu
public static final String MENU_TAG_NAME	= "menu";
public static final String NAME				= "name";
public static final String FORM_NAME		= "formName";
public static final String ACTION_NAME		= "actionName";

/**
 * Constructor initializes static controller forwards
 * 
*/
public LACSDGenericAction()  {
    super();
    errorAnalyzer = new LACSDErrorAnalyzer();
    this.errorForwardMap = errorAnalyzer.getErrorForwardMap();
    initErrControl();
}

/**
 * Execute With Result - Primary Execution Block for LACSD Struts Framework
 * <code>Subclasses of LACSD Framework must implement this method</code>
 * @param String actionName 
 * @throws LACSDException
 */
protected abstract String executeWithResult() throws LACSDException, Throwable;

/**
 * Similar to Execute With Result, SubSystem Applications like (RDXGenericAction)
 * will have the opportunity to activate this method before primary action processing
 * occurs.   This method is intended to contain code for global system lockout conditions
 * that might occur during scheduled maintenence, downtime, or durable batch processes.
 * @param String actionName 
 * @throws LACSDException
 
*/
protected abstract String executeApplicationGlobal() throws LACSDException, Throwable;

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
 * Primary (TOP LEVEL) Execute Method - Jakarta Struts Framework v2.1
 * @return String
 * @throws IOException, ServletException
*/
public String execute() throws Exception {
	String result = null;
    try {
        result = executeApplicationGlobal();
        	
        /** Comments out by Hoa Ho on Oct 25th, 2011
         ** For some case, it is not necessary to forward the result to the JSP page. 
         **   
        if (result == null){
            this.addError(LACSDWebConstants.ERROR_MESSAGE,LACSDWebConstants.MESSAGE_APP_FAILURE,LACSDWebConstants.MESSAGE_BAD_ACTIONFORWARD,true);
            result = LACSDWebConstants.FORWARD_APP_FAILURE;
        }**/
    }
    
    //   HANDLE ERROR CONDITIONS
    //****************************************************************************/
    catch (LACSDException e) {
        log.error(e.getMessage() + "\n" + e.fillInStackTrace(), e);
        
        return errorAnalyzer.examineLACSDException(e, request);
    }
    catch (Throwable t) {   //  This would happen as a result of EJB Failure throwing throwable
                            //  or any other non-expected runtime exception in the application
    	log.error(t.getMessage() + "\n" + t.fillInStackTrace(), t);
                            
        if (t instanceof LACSDException) {
            LACSDException converted = (LACSDException)t;
            return errorAnalyzer.examineLACSDException(converted, request);
        }
        else {
            this.addError(LACSDWebConstants.ERROR_MESSAGE,LACSDWebConstants.MESSAGE_APP_FAILURE,t);
            return LACSDWebConstants.FORWARD_APP_FAILURE;
        }
    }
    
    //  END)    RETURN RESULT OF "executeWithResult()"
    //****************************************************************************/
    return result;
}


/** ** ** ** ** ** **>>>>  END OF PUBLIC METHODS <<<<<** ** ** ** ** ** ** ** ** **/





/**
 * Add ActionErrors to request object
 * @param String errKey (for JSP Screen)
 * @param String propertyFileKey (for Text File)
 * @return void
*/
public void addError(String errKey, String propertyFileKey) {
    this.addError(errKey, propertyFileKey, null);
}

/**
 * Add ActionErrors to request object
 * @param String errKey (for JSP Screen)
 * @param String propertyFileKey (for Text File)
 * @param String dynamicMessage (for rootCause)
 * @return void
*/
public void addError(String errKey, String propertyFileKey, Object dynamicCause) {
    
    addError(errKey,propertyFileKey,dynamicCause,false);

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
protected void addError(String errKey, String propertyFileKey, Object dynamicCause, boolean dynaCauseIsKeyed) {
    
    ResourceBundle rb = ResourceBundle.getBundle(LACSDWebConstants.MESSAGE_RESOURCES_FILE, new Locale("en","US"));
    
    if (dynaCauseIsKeyed) {
        String cause = rb.getString(dynamicCause.toString());
        this.addFieldError(errKey, cause);
    }
    else {
    	this.addFieldError(errKey, propertyFileKey);
    }
    errorAnalyzer.addError(errKey, propertyFileKey, dynamicCause,  request, dynaCauseIsKeyed);
}

/**
 * Add ActionMessages to request object
 * @param String msgKey (for JSP Screen)
 * @param String propertyFileKey (for Text File)
 * @param HttpServletRequest req
 * @return void
*/
protected void addMessage(String msgKey, String propertyFileKey) {
    
    ResourceBundle rb = ResourceBundle.getBundle(LACSDWebConstants.MESSAGE_RESOURCES_FILE, new Locale("en","US"));
    String message = rb.getString(propertyFileKey);
    this.addActionMessage(message);
    this.addFieldError(msgKey, message);
    request.setAttribute(LACSDWebConstants.INFO_MESSAGE,message);
}



/**
 * Add a confirm message (which requires user's response) to request object
 * The presence of the message will generate JavaScript confirm box with the provided message
 * @param String msg (for JSP Screen)
 * @return void
 */
protected void addConfirm(String msg, String argActionName, String argAction) {

    ResourceBundle rb = ResourceBundle.getBundle(LACSDWebConstants.MESSAGE_RESOURCES_FILE, new Locale("en","US"));
    msg = rb.getString(msg);
    
    request.setAttribute(LACSDWebConstants.CONFIRM_MESSAGE, msg);
    request.setAttribute(LACSDWebConstants.ACTION, argAction);
    request.setAttribute(LACSDWebConstants.ACTION_NAME, argActionName);
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
    errorAnalyzer.regRecoverableErr(errCode,spName,errorMessage,forwardPage,false);
}


/**
 * Update the UserProfile object
 * This object lives in the User's HTTPSESSION
 * @param UserProfile profile
 * @return void
*/
protected void setUserProfile(UserProfile profile) {
    session.put(LACSDWebConstants.USER_PROFILE,profile);
}

/**
 * Return a copy of the UserProfile Object -
 * This object lives in the User's HTTPSESSION
 * @return UserProfile
*/
protected UserProfile getUserProfile() {
	UserProfile profile = (UserProfile)session.get(LACSDWebConstants.USER_PROFILE);
	//Profile is set to session from the interceptor
	if (profile == null) {
		HttpSession httpSession =  request.getSession (true);
		profile = (UserProfile)httpSession.getAttribute(LACSDWebConstants.USER_PROFILE);
	}
    return profile;
}



/**
 * Common method triggers the SHOW REPORT POPUP include file
 * @param ReportVO reportVO
 * @return void
 * @throws LACSDException
*/
protected String copyReportFile(byte[] actualReport, String fileExtension, String fileDir, String timestampString ) throws LACSDException {
   
    // 1)   Establish Temporary Directory AND URL Pattern for report File
    String tempdir = (servletContext.getRealPath("/") + fileDir);   //  local web directory 
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
 * @return
 * @throws LACSDException
 */
protected ReportVO generateReportLink(ReportVO reportVO) throws LACSDException {

    
UserProfile user = this.getUserProfile();
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
* @return void
* @throws LACSDException
*/
protected void showReportPopup(ReportVO reportVO) throws LACSDException {

 request.setAttribute("downloadURL",reportVO.getWebDownloadURL());
 request.setAttribute("showPopup","true");
}

/**
 * Hold Objects Temporarily In HttpSession
 * @param String key
 * @param Object value
 * @return void
*/
protected void setObjectToSession(String key, Object value) {
    session.put(key,value);
}

/**
 * Get Objects From HttpSession - returns a copy of the object
 * @param String key
 * @return object
*/
protected Object getObjectFromSession(String key) {
    return session.get(key);
}

/**
 * Release Objects From HttpSession - returns a copy of the object
 * @param String key
 * @return object
*/
protected Object releaseObjectFromSession(String key) {
    
    Object obj = session.get(key);
    session.remove(key);
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

	// get allowed actions
	ArrayList<ActivityVO> allowedActs = userProfile.getEmployeeVO().getEmployeeRoleVO().getActivities();

	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	dbf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", false); // allow <!DOCTYPE>
	DocumentBuilder db = dbf.newDocumentBuilder();

	// parse xml into document and loop through nodes
	Document doc = db.parse(new File(xmlMenuPath));
	NodeList nodeList = doc.getElementsByTagName(MENU_TAG_NAME);

	// 1st loop
	// remove any unallowed child <menu> actions
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
			i--; // decrement because the next node index becomes the node index that just got removed.
		}
	}

	// 2nd loop
	// remove any parent <menu> that became empty
	// parent menu has attribute name="subMenuName"
	for (int i=0; i<nodeList.getLength(); i++) {

		Node node = (Node) nodeList.item(i);

		// get each attribute of node
		if (node.hasAttributes()) {
			NamedNodeMap namedNodeMap = node.getAttributes();
			for (int j=0; j<namedNodeMap.getLength(); j++) {
				String attr = namedNodeMap.item(j) + "";
				// if attribute name="" exists, this is a parent node
				// if parent node has no more child nodes, remove the parent node too
				if (attr.startsWith(NAME)) {

					if (! node.hasChildNodes()) {
						node.getParentNode().removeChild(node);
						i--;
					} else {

						// a node may have child nodes
						// if all child nodes (sub-menu) are empty, remove the parent node (menu)
						NodeList childNodes = node.getChildNodes();

						boolean hasChildNode = false;

						for (int k=0; k<childNodes.getLength(); k++) {
							Node eachChildNode = childNodes.item(k);

							if (eachChildNode.hasAttributes()) {
								NamedNodeMap childNamedNodeMap = eachChildNode.getAttributes();
								for (int l=0; l<childNamedNodeMap.getLength();) {
									hasChildNode = true;
									break;
								}
							}
						}

						if (! hasChildNode) {
							node.getParentNode().removeChild(node);
							i--;
						}
					}
				}
			}
		}
	}

	return doc;
}

public String getActionName() {
	return actionName;
}

public void setActionName(String actionName) {
	this.actionName = actionName;
}

public String getCallingPage() {
	return callingPage;
}

public void setCallingPage(String callingPage) {
	this.callingPage = callingPage;
}

public String getMultiValidate() {
	return multiValidate;
}

public void setMultiValidate(String multiValidate) {
	this.multiValidate = multiValidate;
}

@SuppressWarnings({ "rawtypes", "unchecked" })
public void setSession(Map session) {
	this.session = session;
}

@SuppressWarnings("rawtypes")
public Map getSession() {
	return session;
}


public HttpServletRequest getServletRequest() {
	return request;
}

public void setServletRequest(HttpServletRequest request) {
	this.request = request;
} 

public void setServletResponse(HttpServletResponse response){
    this.response = response;
}

public HttpServletResponse getServletResponse(){
    return response;
}


public ServletContext getServletContext() {
	return servletContext;
}

public void setServletContext(ServletContext servletContext) {
	this.servletContext = servletContext;
}

/**
 * Return the pagingVO
 * @return PagingVO
 */
public PagingVO getPagingVO() {
	return pagingVO;
}

/**
 * Set the pagingVO
 * @param pagingVO the pagingVO to set
 */
public void setPagingVO(PagingVO pagingVO) {
	this.pagingVO = pagingVO;
}

}