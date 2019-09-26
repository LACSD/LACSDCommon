package org.lacsd.common.taglib;

/*****************************************************************************************************************
//* Copyright (c) 2010 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:       GLParametersTag.java
//* Revision:       1.0
//* Author:         hho@lacsd.org
//* Created On:     June 2, 2010
//* Modified By:
//* Modified On:
//*                 
//* Description:  Custom tag to display ArrayList of ReportParameterVO objects 
//*               to HTML format for GL Dashboard. Set attribute tableFormat=true, to display all parameters in a table.
//*               otherwise, each of them will be displayed in a table row with three columns
//* 
//*               Javascript validation is used to validate all the parameter inputs.  The validation script is 
//* 			   dowloaded from http://groups.yahoo.com/group/validation.  To use this script paste the following
//*               line after closing form tag (</form>) 
//* 			<script src="PATH_TO_SCRIPT/validation.js" type="text/javascript"></script>
//*	
//* 
/******************************************************************************************************************/

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.lacsd.common.constants.LACSDReportConstants;
import org.lacsd.reporting.values.ReportParameterVO;

public class GLParametersTag extends TagSupport {
  
   private static final long serialVersionUID = -5507352842327269841L;
   private final static String START_DATE_FIELD = "StartDate";
   private final static String END_DATE_FIELD = "EndDate";
   private final static String DATE_TIME = "DateTime";
   private final static String DATE = "Date";
   private Object collection;
   private String name;
   private String type;
   private String tableClassName; //css class name for parameters table
   private String headerClassName; //css class name for parameters table's header
   private String helpTextClass;
   private String width; //table width
   private String property;
   private String scope;
   private String webroot ="";
   private Collection<ReportParameterVO> parameters;
   private boolean tableFormat = true;
   private int sizeOfSelectBox = 4; //Size of multiple select box
   
  
   /**
    * @return Returns the tableFormat.
    */
   public boolean isTableFormat() {
       return tableFormat;
   }
   /**
    * @param tableFormat The tableFormat to set.
    */
   public void setTableFormat(boolean tableFormat) {
       this.tableFormat = tableFormat;
   }
   /**
    * @return Returns the tableClassName.
    */
   public String getTableClassName() {
       return tableClassName;
   }
   /**
    * @param tableClassName The tableClassName to set.
    */
   public void setTableClassName(String tableClassName) {
       this.tableClassName = tableClassName;
   }
   /**
    * @return Returns the headerClassName.
    */
   public String getHeaderClassName() {
       return headerClassName;
   }
   /**
    * @param headerClassName The headerClassName to set.
    */
   public void setHeaderClassName(String headerClassName) {
       this.headerClassName = headerClassName;
   }
   /**
    * @return Returns the helpTextClass.
    */
   public String getHelpTextClass() {
	return helpTextClass;
   }
   
   /**
    * @param helpTextClass The helpTextClass to set.
    */
   public void setHelpTextClass(String helpTextClass) {
	this.helpTextClass = helpTextClass;
   }
   /**
    * @return Returns the webroot.
    */
   public String getWebroot() {
       return webroot;
   }
   /**
    * @param webroot The webroot to set.
    */
   public void setWebroot(String webroot) {
       this.webroot = webroot;
   }
   /**
    * @return Returns the scope.
    */
   public String getScope() {
       return scope;
   }
   /**
    * @param scope The scope to set.
    */
   public void setScope(String scope) {
       this.scope = scope;
   }
   /**
    * @return Returns the property.
    */
   public String getProperty() {
       return property;
   }
   /**
    * @param property The property to set.
    */
   public void setProperty(String property) {
       this.property = property;
   }

   /**
    * @return Returns the collection.
    */
   public Object getCollection() {
       return collection;
   }
   /**
    * @param collection The collection to set.
    */
   public void setCollection(Object collection) {
       this.collection = collection;
   }

   /**
    * @return Returns the name.
    */
   public String getName() {
       return name;
   }
   /**
    * @param name The name to set.
    */
   public void setName(String name) {
       this.name = name;
   }
   /**
    * @return Returns the type.
    */
   public String getType() {
       return type;
   }
   /**
    * @param type The type to set.
    */
   public void setType(String type) {
       this.type = type;
   }
   /**
    * @return Returns the width.
    */
   public String getWidth() {
       return width;
   }
   /**
    * @param width The width to set.
    */
   public void setWidth(String width) {
       this.width = width;
   }
    
	public int getSizeOfSelectBox() {
		return sizeOfSelectBox;
	}
	
	public void setSizeOfSelectBox(int sizeOfSelectBox) {
		this.sizeOfSelectBox = sizeOfSelectBox;
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int doStartTag() throws JspException {
		try {
			JspWriter out= pageContext.getOut();
			if (collection != null) {
				parameters = (Collection)collection;
				if (parameters.size() > 0) {
				    ReportParameterVO[] paramsVOs = (ReportParameterVO[]) parameters.toArray(new ReportParameterVO[parameters.size()]);
				    if (tableFormat) {
				        out.println("<table width=\"" + width + "\"  class=\"" + tableClassName + "\" cellspacing=\"0\" cellpadding=\"0\">" );
					    out.println("<th class=\"" + headerClassName + "\" colspan=\"4\">Search and Routing Criteria</th>");
					}
					for (int i =0; i < paramsVOs.length; i++){
					    out.println("<tr>" + doFormatParameter(paramsVOs[i], i) + getHelpText(paramsVOs[i]) + "</tr>");
					}
					if (tableFormat) {
					    out.println("</table>");
					}
					out.println(generateCommonScript());
				    
				} else {
				    throw new JspException("LIST OF REPORT PARAMETERS IS EMPTY");
				}
			} else {
			    throw new JspException("LIST OF REPORT PARAMETERS IS NULL");
			}
		} catch (Exception e) {
			throw new JspException(e.getMessage());
		}
		return SKIP_BODY;
	}

	public int doEndTag() throws JspException {
		collection = null;
		return EVAL_PAGE;
	}
   
	/**
	 * Format parameter
	 * @param parameterVO
	 * @return
	 */
	private String doFormatParameter(ReportParameterVO parameterVO, int paramIndex) {
	    String strFormat = "<td valign=\"top\" nowrap>";
	    String required = "";
	    String fieldName = "";
       if (!parameterVO.isHidden()) {
   	    if (parameterVO.getDisplayName() != null && parameterVO.getDisplayName().length() > 0) {
   	        strFormat += parameterVO.getDisplayName();
   	        fieldName = parameterVO.getDisplayName();
   	    }else {
   	        strFormat += parameterVO.getName();
   	        fieldName = parameterVO.getName();
   	    }
   	    if (parameterVO.isRequired())  {
   	        strFormat += " <img src=\"" + webroot + "images/iconRequired.gif\" alt=\"Required field\">";
   	        required = " required=\"on\" message=\"Please provide a value for " + fieldName + " field\" ";
   	    }
       }
	    strFormat += "</td><td width=\"10\">&nbsp;</td>";
	    strFormat += "<input type=\"hidden\" name=\"reportParameterVO[" + paramIndex + "].name\" value=\"" + parameterVO.getName() + "\">";
	    if (parameterVO.getControlType() != null) {
		    if (parameterVO.getControlType().equalsIgnoreCase(ReportParameterVO.CHECKBOX)) {
		        strFormat += "<td colspan=\"2\">" + this.checkboxFormat(parameterVO, paramIndex, required) + "</td>";
			}else if (parameterVO.getControlType().equalsIgnoreCase(ReportParameterVO.RADIO_BOX)) {
			    strFormat += "<td colspan=\"2\">" + this.radioBoxFormat(parameterVO, paramIndex, required) + "</td>";
			}else {
				if (parameterVO.getName().equalsIgnoreCase("FROMBU")) {
					strFormat += "<td>" + this.dropdownBoxFormat(parameterVO, paramIndex, required) + "</td>" +
					             "<td rowspan=\"2\" id=\"BuFilterCell\">" + this.BUFilterOptions() + "</td>";
				}else {
					strFormat += "<td>" + this.dropdownBoxFormat(parameterVO, paramIndex, required) + "</td>";
				}
			}
	    }else {
	        strFormat += "<td colspan=\"2\">" + this.textBoxFormat(parameterVO, paramIndex, required)  + "</td>";
	    }
	    return strFormat;
	}
	
	/** 
	 * Display parameter help text
	 * @param parameterVO
	 * @return
	 */
	private String getHelpText(ReportParameterVO parameterVO) throws Exception{
		String result = "";
		if (parameterVO.getHelpText() != null && parameterVO.getHelpText().length() > 0) {
			result = "<td valign=\"top\"><img src=\"" + webroot + "images/helpIcon.gif\"  border=\"0\" onclick=\"displayHelpText('" + parameterVO.getHelpText().replaceAll("'","\\\'").replaceAll("\"", "\\\"") + "', this)\" ></td>";
		}
		
		return result;
	}
	
	private String generateCommonScript() {
	    String strFormat = "<script language=\"javascript\">"+
					        " var inputs = document.getElementsByTagName(\"input\");\n " +
					        " for (var i = 0; i < inputs.length; i++) {\n"+
					        "  if (inputs[i].getAttribute(\"type\").toLowerCase() == \"text\" && inputs[i].getAttribute(\"name\").indexOf(\"reportParameterVO\") != -1) {" +
					        "       inputs[i].focus();\nbreak;" +
					        "   }\n;" +
					        "}\n" +
	    				   "\nfunction getObjectLeft(obj) { oLeft = obj.offsetLeft ;  while(obj.offsetParent!=null) {   oParent = obj.offsetParent;    oLeft += oParent.offsetLeft ; obj = oParent; } return oLeft;}"+
	    				   "\nfunction getObjectTop(obj) { oTop = obj.offsetTop; while(obj.offsetParent!=null) { oParent = obj.offsetParent ; oTop += oParent.offsetTop ; obj = oParent; } return oTop;}" +
	    				   "\nfunction displayHelpText(message, obj){" +
	    				   "\n helpObj = document.getElementById('popupHelpDiv');" +
	    				   "\n helpObj.style.top = getObjectTop(obj);" +
	    				   "\n helpObj.style.width = document.body.clientWidth - getObjectLeft(obj.parentNode.parentNode) -10;"+
	    				   "\n helpObjWidth = helpObj.style.width; "+
	    				   "\n helpObj.style.left =  getObjectLeft(obj) - parseInt(helpObjWidth.replace('px',''))  ;" +
	    				   "\n document.getElementById('helpMessageCell').innerHTML = message;" +
	    				   "\n helpObj.style.display = '';" +
	    				   "};" ;
	    strFormat	+=  "</script>";
	    strFormat += "<div class=\"" + this.helpTextClass + "\" id=\"popupHelpDiv\" style=\"display:none;position:absolute;z-index:900\" onclick=\"this.style.display='none';\">" +
	    			 "<div align=\"right\"><img src=\"" + webroot + "images/closeSquare16x16.gif\" onclick=\"document.getElementById('popupHelpDiv').style.display='none';\"></div>" +
	    			 "<div id=\"helpMessageCell\" valign=\"top\"></div>" +
	    		     "</div>";
	    return strFormat;
	}
	/**
	 * Format checkbox parameter
	 * @param parameterVO
	 * @param paramIndex
	 * @return
	 */
	private String checkboxFormat(ReportParameterVO parameterVO, int paramIndex, String required) {
	    String strFormat = "";
	    if (parameterVO.isHidden()) {
	        strFormat = "<input style=\"display:none\" type=\"checkbox\" id=\"" + parameterVO.getName() + "\" name=\"reportParameterVO[" + paramIndex + "].value\" value=\""+ parameterVO.getValue() +"\" CHECKED disabled=\"disabled\">";
	    }else {
	        if (parameterVO.getName().equalsIgnoreCase(LACSDReportConstants.PARAM_TYPE_EMAIL)) {
	            strFormat = "<input tabindex=\"" + (paramIndex+1) + "\" type=\"checkbox\" name=\"\" value=\""+ parameterVO.getValue() +"\" onclick=\"if (this.checked) document.getElementById('parameters" + paramIndex + "').value='true' ;document.getElementById('isEmail').value='true' ;\" " + required + ">";
	            strFormat += "<input type=\"hidden\" name=\"reportParameterVO[" + paramIndex + "].value\" id=\"parameters" + paramIndex+ "\" value=\"false\">";
	            strFormat += "<input type=\"hidden\" name=\"reportVO.email\" id=\"isEmail\" value=\"false\">";
	            
	        } else if (parameterVO.getName().equalsIgnoreCase(LACSDReportConstants.PARAM_TYPE_EMDS)) {
	            strFormat = "<input tabindex=\"" + (paramIndex+1) + "\"  type=\"checkbox\" name=\"\" value=\""+ parameterVO.getValue() +"\" onclick=\"if (this.checked) document.getElementById('parameters" + paramIndex + "').value='true' ;document.getElementById('isEDMS').value='true' ;\" " + required + ">";
	            strFormat += "<input type=\"hidden\" name=\"reportParameterVO[" + paramIndex + "].value\" id=\"parameters" + paramIndex+ "\" value=\"false\">";
	            strFormat += "<input type=\"hidden\" name=\"reportVO.EDMS\" id=\"isEDMS\" value=\"false\">";
	            
	        }
	        else {
	            strFormat = "<input tabindex=\"" + (paramIndex+1) + "\" type=\"checkbox\" name=\"\" id=\""+ parameterVO.getName() +"\" value=\""+ parameterVO.getValue() +"\" onclick=\"if (this.checked) document.getElementById('parameters" + paramIndex + "').value='true' ;\" " + required  ;
	            if (parameterVO.getValue().equalsIgnoreCase("true")) {
	            	strFormat += " CHECKED ";
	            }
	            strFormat += "><input type=\"hidden\" name=\"reportParameterVO[" + paramIndex + "].value\" id=\"parameters" + paramIndex+ "\" value=\""+ parameterVO.getValue() +"\">";
	        }
	    }
	    return strFormat;
	}
	/**
	 * Format radio box parameter type
	 * @param parameterVO
	 * @param paramIndex
	 * @return
	 */
	private String radioBoxFormat(ReportParameterVO parameterVO, int paramIndex, String required) {
	    String strFormat = "";
	    String clickEvent = "toggleOptions(this, '" + parameterVO.getName() + "');";
	   ArrayList<ReportParameterVO> selectNameValueList = parameterVO.getParameterList();
       ReportParameterVO[] paramVOs = (ReportParameterVO[]) selectNameValueList.toArray(new ReportParameterVO[selectNameValueList.size()]);
       strFormat = "<table cellpadding=\"\0\" cellspacing=\"\0\">";
       for(int i=0; i < paramVOs.length; i++) {
    	   if ((i % 2) == 0) {
        	   strFormat+= "<tr>" ;
           }
           strFormat += "<td><input tabindex=\"" + (paramIndex+1) + "\" type=\"radio\" name=\"radio" +paramIndex+ "\"  value=\"" + paramVOs[i].getValue().trim() + "\"   onclick=\"if (this.checked) document.getElementById('reportParameterVO[" + paramIndex + "].value').value=this.value;" + clickEvent + "\"" ;
           if (parameterVO.getValue().trim().equalsIgnoreCase(paramVOs[i].getValue().trim())) {
               strFormat += " CHECKED ";
           }
           strFormat += ">" + paramVOs[i].getName().trim() + "&nbsp;&nbsp;&nbsp;</td>";
           if (((i+1) % 2) == 0) {
        	   strFormat+= "</tr>" ;
           }
       }
       if ((paramVOs.length % 2 ) != 0) {
    	   strFormat+= "</tr>" ;
       }
       strFormat += "</table>";
       strFormat += "<input type=\"hidden\" name=\"reportParameterVO[" + paramIndex + "].value\" id=\"reportParameterVO[" + paramIndex + "].value\" value=\"" + parameterVO.getValue().trim() + "\" >";

	    return strFormat;
	}
	

	private String dropdownBoxFormat(ReportParameterVO parameterVO, int paramIndex, String required) {
		String strFormat = "";
		HttpSession session = pageContext.getSession();
		strFormat = "<input type=\"hidden\" name=\"reportParameterVO[" + paramIndex + "].value\" id=\"reportParameterVO[" + paramIndex + "].value\" " + required + "/>";
		if (parameterVO.getName().equalsIgnoreCase("FROMBU")) {
			strFormat += "<SELECT  style=\"width:100\" id=\"FROMBU1\" tabindex=\"" + (paramIndex+1) + "\"  size=\"1\" onchange=\"document.getElementById('reportParameterVO[" + paramIndex + "].value').value = this.value;\">";
			strFormat += session.getAttribute("ListBUs").toString();
			strFormat += "</SELECT>";
			strFormat += "<SELECT style=\"display:none;width:100\" id=\"FROMBU2\" tabindex=\"" + (paramIndex+1) + "\" size=\"1\" onchange=\"document.getElementById('reportParameterVO[" + paramIndex + "].value').value = this.value;\">";
			strFormat += session.getAttribute("ListInitBUs").toString();
			strFormat += "</SELECT>";			
			strFormat += "<SELECT style=\"display:none;width:100\" id=\"FROMBU3\" tabindex=\"" + (paramIndex+1) + "\" size=\"10\" multiple=\"true\" onchange=\"setValueTo(this,document.getElementById('reportParameterVO[" + paramIndex + "].value'))\">";
			strFormat += session.getAttribute("ListBUs").toString();
			strFormat += "</SELECT>";
			strFormat += "<SELECT style=\"display:none;width:100\" id=\"BUGROUP\" tabindex=\"" + (paramIndex+1) + "\"  size=\"1\" onchange=\"document.getElementById('reportParameterVO[" + paramIndex + "].value').value = this.value;\">";
			strFormat += session.getAttribute("BUGroups").toString();
			strFormat += "</SELECT>";
		}else if (parameterVO.getName().equalsIgnoreCase("TOBU")) {
			strFormat += "<SELECT style=\"width:100\" id=\"TOBU1\" tabindex=\"" + (paramIndex+1) + "\" size=\"1\" onchange=\"document.getElementById('reportParameterVO[" + paramIndex + "].value').value = this.value;\">";
			strFormat += session.getAttribute("ListBUs").toString();
			strFormat += "</SELECT>";			
			strFormat += "<SELECT style=\"display:none;width:100\" id=\"TOBU2\" tabindex=\"" + (paramIndex+1) + "\" size=\"1\" onchange=\"document.getElementById('reportParameterVO[" + paramIndex + "].value').value = this.value;\">";
			strFormat += session.getAttribute("ListInitBUs").toString();
			strFormat += "</SELECT>";
		}else if (parameterVO.getName().equalsIgnoreCase("FROMACCOUNT")) {
			strFormat += "<SELECT style=\"width:100\"  id=\"FROMACCOUNT1\" tabindex=\"" + (paramIndex+1) + "\"  size=\"1\" onchange=\"document.getElementById('reportParameterVO[" + paramIndex + "].value').value = this.value;\">";
			strFormat += session.getAttribute("ListAccounts").toString();
			strFormat += "</SELECT>";
			strFormat += "<SELECT style=\"display:none;width:100\" id=\"FROMACCOUNT2\" tabindex=\"" + (paramIndex+1) + "\" size=\"10\" multiple=\"true\" onchange=\"setValueTo(this,document.getElementById('reportParameterVO[" + paramIndex + "].value'))\">";
			strFormat += session.getAttribute("ListAccounts").toString();
			strFormat += "</SELECT>";			
		
		}else if (parameterVO.getName().equalsIgnoreCase("TOACCOUNT")) {
			strFormat += "<SELECT style=\"width:100\" id=\"TOACCOUNT\" tabindex=\"" + (paramIndex+1) + "\"  size=\"1\" onchange=\"document.getElementById('reportParameterVO[" + paramIndex + "].value').value = this.value;\">";
			strFormat += session.getAttribute("ListAccounts").toString();
			strFormat += "</SELECT>";
		}
		return strFormat;
	}
	private String BUFilterOptions() {
		String str ="<table>" +
		            "<tr>" +
		            "<td><input type=radio name=\"BuFilterOptions\" value=\"All\" checked onclick=\"toggleBUFilter(this.value)\">&nbsp;All</td>" +
		            "<td><input type=radio name=\"BuFilterOptions\" value=\"Open\"  onclick=\"toggleBUFilter(this.value)\">&nbsp;Open</td>" +
		            "<td><input type=radio name=\"BuFilterOptions\" value=\"Closed\"  onclick=\"toggleBUFilter(this.value)\">&nbsp;Closed</td>" +
		            "<td><input type=radio name=\"BuFilterOptions\" value=\"Num\"  onclick=\"toggleBUFilter(this.value)\">&nbsp;Numeric</td>" +
		            "</tr>" +
		            "<tr><td></td>" +
		            "<td><input type=radio name=\"BuFilterOptions\" value=\"OM\"  onclick=\"toggleBUFilter(this.value)\">&nbsp;O&M</td>" +
		            "<td><input type=radio name=\"BuFilterOptions\" value=\"Capital\"  onclick=\"toggleBUFilter(this.value)\">&nbsp;Capital</td>" +
		            "<td><input type=radio name=\"BuFilterOptions\" value=\"Alpha\"  onclick=\"toggleBUFilter(this.value)\">&nbsp;Alpha</td>" +
		            "</tr>" +		            
		            "</table>";
		return str;
	}
	/**
	 * Format text box parameter type
	 * @param parameterVO
	 * @param paramIndex
	 * @return
	 */
	private String textBoxFormat (ReportParameterVO parameterVO, int paramIndex, String required) {
	    String strFormat = "";
        if (parameterVO.getType().equalsIgnoreCase(ReportParameterVO.DATE_ONLY_TYPE) ||
        	parameterVO.getType().equalsIgnoreCase(DATE) ||	
        	parameterVO.getType().equalsIgnoreCase(DATE_TIME) ) {
            strFormat = this.parameterDateFormat(parameterVO, paramIndex, required);
	    } else {
	        strFormat = "<input tabindex=\"" + (paramIndex+1) + "\" type=\"" + (parameterVO.isHidden()?"hidden":"text") +"\" size=\"11\" id=\"" + parameterVO.getName() + "\" name=\"reportParameterVO[" + paramIndex + "].value\" " + required;
	        if (parameterVO.getType().equalsIgnoreCase(ReportParameterVO.CURRENCY_TYPE)) {
	            strFormat += " signed=\"on\" currency=\"on\" ";
	        } else if (parameterVO.getType().equalsIgnoreCase(ReportParameterVO.DOUBLE_TYPE)) {
	            strFormat += " float=\"on\" ";
	        } else if (parameterVO.getType().equalsIgnoreCase(ReportParameterVO.INTEGER_TYPE)) {
	            strFormat += " integer=\"on\" filter=\"[0-9]\" ";
	        }
	        strFormat += "  value=\"" + parameterVO.getValue() + "\">";
	    }
	    return strFormat;   
	}

	private String parameterDateFormat(ReportParameterVO parameterVO, int paramIndex, String required) {
	    String strFormat = "";
	    String scriptFormat = "";
	    String value = parameterVO.getValue();
       strFormat = "<input tabindex=\"" + (paramIndex+1) + "\" type=\"text\" size=\"11\" id=\"" + parameterVO.getName() + "\" name=\"reportParameterVO[" + paramIndex + "].value\" " + required;
       if (parameterVO.getName().equalsIgnoreCase(LACSDReportConstants.MONTH_YEAR_FIELD) 
           || parameterVO.getName().equalsIgnoreCase(LACSDReportConstants.START_MONTH_YEAR_FIELD) 
           || parameterVO.getName().equalsIgnoreCase(LACSDReportConstants.END_MONTH_YEAR_FIELD)) {
    	   if (required.length() > 0) {
    		   strFormat += " date=\"MM/YYYY\" maxlength=\"7\"  value=\"" + (value.length() >0?value:"mm/yyyy")  +" \" ";
    	   }else {
    		   strFormat += " date=\"MM/YYYY\" maxlength=\"7\"  value=\"" + (value.length() >0?value:"")  +" \" ";
    	   }
           scriptFormat = ",'MM/yyyy'";
       }else {
    	   if (required.length() > 0) {
    		   strFormat += " date=\"MM/DD/YYYY\" maxlength=\"12\" value=\"" + (value.length() >0?value:"mm/dd/yyyy")  + "\" ";
    	   } else {
    		   strFormat += " date=\"MM/DD/YYYY\" maxlength=\"12\" value=\"" + (value.length() >0?value:"")  + "\" ";
    	   }
       }
       if (parameterVO.getName().equalsIgnoreCase(LACSDReportConstants.END_MONTH_YEAR_FIELD)) {
           strFormat += " onbeforevalidate=\"this.MIN=document.getElementById('" + LACSDReportConstants.START_MONTH_YEAR_FIELD + "').value;\" ";
       }
       else if (parameterVO.getName().equalsIgnoreCase(END_DATE_FIELD)) {
           strFormat += " onbeforevalidate=\"this.MIN=document.getElementById('" + START_DATE_FIELD + "').value;\" ";
       }        
       strFormat +=  " >";
       strFormat += "&nbsp;&nbsp;<img src=\"" + webroot + "images/iconCalendar.gif\" alt=\"\" style=\"cursor:pointer;\" title=\"Pick a date\" onclick=\"showCalendar('reportParameterVO[" + paramIndex + "].value', this " + scriptFormat + ");\">";
	    return strFormat;
	}

   /**
    * Release all allocated resources.
    */
   public void release() {
       super.release();
   }
}
