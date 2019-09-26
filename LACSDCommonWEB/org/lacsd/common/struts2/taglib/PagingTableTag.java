package org.lacsd.common.struts2.taglib;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.lacsd.common.comparitors.ObjectAsc;
import org.lacsd.common.comparitors.ObjectDesc;
import org.lacsd.common.values.PagingVO;

import com.opensymphony.xwork2.ActionContext;

/******************************************************************************
//* Copyright (c) 2011 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		PagingTableTag.java
//* Revision: 		1.0
//* Author:			hho@lacsd.org
//* Created On: 	Apr 19, 2011
//* Modified By:    
//* Modified On:    
//* 
//* Description:	Paging tag library
/******************************************************************************/

public class PagingTableTag extends BodyTagSupport{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7147432557776709972L;
	private String property;
	private String tableStyle;
	private String navStyle;
	private String navPos= "top";
	private String formName;
	private String pagingProperty;
	private boolean allowChangeSize = true;
	private boolean serverPaging = false;
	@SuppressWarnings("rawtypes")
	private Collection list;
	private PagingVO pagingVO;
	private int index = 0;
	private Object refObj;
	private boolean printOption;
	private String pageCssURL; //To keep page style for printing option
	private int totalPages= 0;
	private boolean extraRow = false;
	private boolean scrollable = false;
	private int scrollHeight ;
	private String width;
	private String id;

	/**
	 * Return the property
	 * @return String
	 */
	public String getProperty() {
		return property;
	}
	/**
	 * Set the property
	 * @param property the property to set
	 */
	public void setProperty(String property) {
		this.property = property;
	}

	
	/**
	 * Return the pagingProperty
	 * @return String
	 */
	public String getPagingProperty() {
		return pagingProperty;
	}
	/**
	 * Return the tableStyle
	 * @return String
	 */
	public String getTableStyle() {
		return tableStyle;
	}
	/**
	 * Set the tableStyle
	 * @param tableStyle the tableStyle to set
	 */
	public void setTableStyle(String tableStyle) {
		this.tableStyle = tableStyle;
	}
	/**
	 * Set the pagingProperty
	 * @param pagingProperty the pagingProperty to set
	 */
	public void setPagingProperty(String pagingProperty) {
		this.pagingProperty = pagingProperty;
	}
	/**
	 * Return the formName
	 * @return String
	 */
	public String getFormName() {
		return formName;
	}
	/**
	 * Set the formName
	 * @param formName the formName to set
	 */
	public void setFormName(String formName) {
		this.formName = formName;
	}
	/**
	 * Return the navPos
	 * @return String
	 */
	public String getNavPos() {
		return navPos;
	}
	/**
	 * Set the navPos
	 * @param navPos the navPos to set
	 */
	public void setNavPos(String navPos) {
		this.navPos = navPos;
	}

	/**
	 * Return the navStyle
	 * @return String
	 */
	public String getNavStyle() {
		return navStyle;
	}
	/**
	 * Set the navStyle
	 * @param navStyle the navStyle to set
	 */
	public void setNavStyle(String navStyle) {
		this.navStyle = navStyle;
	}

	/**
	 * Return the allowChangeSize
	 * @return boolean
	 */
	public boolean isAllowChangeSize() {
		return allowChangeSize;
	}
	/**
	 * Set the allowChangeSize
	 * @param allowChangeSize the allowChangeSize to set
	 */
	public void setAllowChangeSize(boolean allowChangeSize) {
		this.allowChangeSize = allowChangeSize;
	}
	
	
	/**
	 * Return the index
	 * @return int
	 */
	public int getIndex() {
		return index;
	}
	/**
	 * Set the index
	 * @param index the index to set
	 */
	public void setIndex(int index) {
		this.index = index;
	}
	
	/**
	 * Return the printOption
	 * @return boolean
	 */
	public boolean isPrintOption() {
		return printOption;
	}
	
	/**
	 * Set the printOption
	 * @param printOption the printOption to set
	 */
	public void setPrintOption(boolean printOption) {
		this.printOption = printOption;
	}
	
	/**
	 * Return the serverPaging
	 * @return boolean
	 */
	public boolean isServerPaging() {
		return serverPaging;
	}
	/**
	 * Set the serverPaging
	 * @param serverPaging the serverPaging to set
	 */
	public void setServerPaging(boolean serverPaging) {
		this.serverPaging = serverPaging;
	}
	/**
	 * Return the pageCssURL
	 * @return String
	 */
	public String getPageCssURL() {
		return pageCssURL;
	}
	/**
	 * Set the pageCssURL
	 * @param pageCssURL the pageCssURL to set
	 */
	public void setPageCssURL(String pageCssURL) {
		this.pageCssURL = pageCssURL;
	}
	
	public String getWidth() {
		return width;
	}
	
	public void setWidth(String width) {
		this.width = width;
	}
	
	public String getId() {
		return id;
	}
	 
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * Get collection iterator
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Iterator getIterator() {
		return list.iterator();
	}
	
	/**
	 * Return the refObj
	 * @return Object
	 */
	public Object getRefObj() {
		return refObj;
	}
	/**
	 * Set the refObj
	 * @param refObj the refObj to set
	 */
	public void setRefObj(Object refObj) {
		this.refObj = refObj;
	}
	
	/**
	 * Return the extraRow
	 * @return boolean
	 */
	public boolean isExtraRow() {
		return extraRow;
	}
	
	
	public boolean isScrollable() {
		return scrollable;
	}
	public void setScrollable(boolean scrollable) {
		this.scrollable = scrollable;
	}
	public int getScrollHeight() {
		return scrollHeight;
	}
	public void setScrollHeight(int scrollHeight) {
		this.scrollHeight = scrollHeight;
	}
	/**
	 * Set the extraRow
	 * @param extraRow the extraRow to set
	 */
	public void setExtraRow(boolean extraRow) {
		this.extraRow = extraRow;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public int doStartTag() throws JspException {
		int eval= EVAL_BODY_INCLUDE;
		try {
			JspWriter out= pageContext.getOut();
			ActionContext a = ActionContext.getContext();
			pagingVO = (PagingVO)a.getValueStack().findValue(pagingProperty);
			if (pagingVO == null) pagingVO = new PagingVO();
			list  = (Collection)a.getValueStack().findValue(property);
			if (!serverPaging) {
				list = getDisplayList((ArrayList)list);
			}else {
				if (pagingVO.getSortField() != null  && pagingVO.getSortField().trim().length() > 0) {
					if (pagingVO.getSortType().equals(PagingVO.ASC_SORT)) {
						Collections.sort((ArrayList)list, new ObjectAsc(pagingVO.getSortField(), pagingVO.isDateType()));
					} else {
						Collections.sort((ArrayList)list, new ObjectDesc(pagingVO.getSortField(), pagingVO.isDateType()));
					}
				}
			}
			if (list.size() > 0) {
				if (printOption) {
					out.println("<script type=\"text/javascript\">\n<!--\n" +
							    " function printListingTable(tableid) {\n" +
							    " var str_buffer = '<html><head>" + (this.pageCssURL==null?"":"<link rel=\"stylesheet\" type=\"text/css\" href=\""+pageCssURL+"\">") +
					            "</head><body>' + document.getElementById(tableid).innerHTML + '</body></html>';\n" +
								"  var props = 'scrollBars=yes,resizable=yes,toolbar=yes,menubar=yes,location=no,directories=no,top=100,left=200'; " +
								"\n newwindow=window.open('','Print_version', props); "+
								"\n newdocument=newwindow.document; "+
								"\n newdocument.write(str_buffer); " +
								" \n newdocument.close(); "+
							    "\n }\n"+
							    "-->\n</script>");
				}
				if (navPos.equalsIgnoreCase("top") || navPos.equalsIgnoreCase("both"))
					out.print("<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"1\"><tr><td >" + navigator() + "</td></tr><tr><td id=\"listing_"+property+"\" ><table width=\"" + (width==null?"100%":width) + "\"" +(tableStyle==null?"":"class=\"" + tableStyle+ "\"") +  " \">");
				else
					out.print("<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"1\"><tr><td id=\"listing_"+property+"\" ><table id=\"" + id + "\" width=\"" + (width==null?"100%":width) + "\"" + (tableStyle==null?"":"class=\"" + tableStyle+ "\"") +  ">");
				this.setRefObj(list.iterator().next());	
				eval= EVAL_BODY_INCLUDE;
			} else {
				out.print("<table " + (tableStyle==null?"":"class=\"" + tableStyle+ "\"") +  " align=\"center\" cellpadding=\"4\"><tr><td align=\"center\" bgcolor=\"#F7F7F7\"><b>No Record Found.</b></td></tr>");
				eval= SKIP_BODY;
			}
		} catch (Exception e) {
			throw new JspException(e.getMessage());
		}
		return eval;
	}

	public int doAfterBody() throws JspException {
		boolean hasItem= false;
		try {
			if (index == 0) index++;
			if (list.iterator().hasNext()) {
				Object next = list.iterator().next();
				this.setRefObj(list.iterator().next());
				if (index > 0) {
					list.remove(next);
				}
				index++;
				hasItem= true;
				extraRow = false;
			} else
				hasItem= false;

		} catch (Exception e) {
			throw new JspException(e.getMessage());
		}
		return (hasItem) ? EVAL_BODY_AGAIN : SKIP_BODY;

	}
	
	public int doEndTag() throws JspException {
		try {
			JspWriter out= pageContext.getOut();
			
			if (index > 0) {
				if (this.isScrollable()) {
					if (navPos.equalsIgnoreCase("bottom") || navPos.equalsIgnoreCase("both"))
						out.print("</table></div></td></tr></table></td></tr><tr><td>" + navigator() + "</td></tr></table>");
					else
						out.print("</table></div></td></tr></table></td></tr></table>");
				}else {
					if (navPos.equalsIgnoreCase("bottom") || navPos.equalsIgnoreCase("both"))
						out.print("</table></td></tr><tr><td>" + navigator() + "</td></tr></table>");
					else
						out.print("</table></td></tr></table>");
				}
			} else
				out.print("</table>");
			out.print(getHiddenFileds());
		} catch (Exception e) {
			throw new JspException(e.getMessage());
		}
		return EVAL_PAGE;
	}
	private String navigator() throws JspException {
		String result= "";
		try {
			String str1= "";
			String str2= "";
			

			//calculate number of pages
			if (pagingVO.getTotalRecords() % pagingVO.getPageSize() == 0)
				totalPages= pagingVO.getTotalRecords() / pagingVO.getPageSize();
			else
				totalPages= pagingVO.getTotalRecords() / pagingVO.getPageSize() + 1;
			if (totalPages > 1 && allowChangeSize) {
				int[] pageSizeArray= { 10, 20, 30, 40, 50, 100, 200, 400, 600, 800, 1000 };
				str1= "Records per page: <select  onchange=\"document.all['" + this.pagingProperty + ".pageSize'].value=this.value;document.all['" + this.pagingProperty + ".pageNum'].value=1;document." + formName + ".submit()\" style=\"width=100\">";
				for (int i= 0; i < pageSizeArray.length; i++) {
					str1 += "<option value=\"" + pageSizeArray[i] + "\"";
					if (pagingVO.getPageSize() == pageSizeArray[i])
						str1 += " selected";
					str1 += " >" + pageSizeArray[i] + "</option>";
				}
				str1 += "</selected>";
			} 
			if (totalPages > 1) {
				if (pagingVO.getPageNum() == 1) {
					str2=   str2
							+ "<a onclick=\"javaScript:void(0)\"><span style=\"font-family:Webdings;font-size:11pt;text-decoration:none;cursor:pointer;color:#666666;\">9</span></a>&nbsp;&nbsp;"
							+ "<a onclick=\"javaScript:void(0)\" ><span style=\"font-family:Webdings;font-size:11pt;text-decoration:none;cursor:pointer;color:#666666;\">7</span></a>&nbsp;&nbsp;";
				}
				else {
					str2=   str2
					        + "<a onclick=\"document.all['" + this.pagingProperty + ".pageNum'].value=1; document."+ formName+ ".submit();\" ><span style=\"font-family:Webdings;font-size:11pt;text-decoration:none;cursor:pointer;\">9</span></a>&nbsp;&nbsp;"
							+ "<a onclick=\"document.all['" + this.pagingProperty + ".pageNum'].value="+ (pagingVO.getPageNum() - 1)+ "; document."+ formName+ ".submit();\" ><span style=\"font-family:Webdings;font-size:11pt;text-decoration:none;cursor:pointer;\">7</span></a>&nbsp;&nbsp;";
				}
				if (pagingVO.getPageNum() == totalPages) {
					str2=	str2
							+ "<a onclick=\"javaScript:void(0)\"><span style=\"font-family:Webdings;font-size:11pt;text-decoration:none;cursor:pointer;color:#666666;\">8</span></a>&nbsp;&nbsp;"
							+ "<a onclick=\"javaScript:void(0)\" ><span style=\"font-family:Webdings;font-size:11pt;text-decoration:none;cursor:pointer;color:#666666;\">:</span></a>&nbsp;&nbsp;";
				}
				else {
					str2=	str2
							+ "<a onclick=\"document.all['" + this.pagingProperty + ".pageNum'].value=" + (pagingVO.getPageNum() + 1) + "; document."+ formName + ".submit();\" ><span style=\"font-family:Webdings;font-size:11pt;text-decoration:none;cursor:pointer;\">8</span></a>&nbsp;&nbsp;"
							+ "<a onclick=\"document.all['" + this.pagingProperty + ".pageNum'].value="+ totalPages+ "; document."+ formName	+ ".submit();\" ><span style=\"font-family:Webdings;font-size:11pt;text-decoration:none;cursor:pointer;\">:</span></a>&nbsp;&nbsp;";
				}
				str2= str2 + "&nbspPage:&nbsp;" + "<select onchange=\"document.all['" + this.pagingProperty + ".pageNum'].value=this.value;document." + formName + ".submit()\">";
				for (int i= 1; i <= totalPages; i++) {
					str2= str2 + "<option value=" + i;
					if (i == pagingVO.getPageNum())
						str2= str2 + " selected";
					str2= str2 + " >" + i + "</option>";
				}
				str2= str2 + "</select> of " + totalPages + " (" + pagingVO.getTotalRecords() +" records found)";
			}
			if (str1.length() > 0) {
				result= "<table width=\"" + (width==null?"100%":width) + "\" " + (navStyle==null?"":"class=\""+ navStyle+" \"") + "><tr>";
				if (printOption) {
					result += "<td nowrap><span onclick=\"javascript:printListingTable('listing_" +property +"')\" style=\"font-family:'Wingdings 2';font-size:14pt;text-decoration:none;cursor:pointer;\">6</span>&nbsp;<span onclick=\"javascript:printListingTable('listing_" +property +"')\" style=\"cursor:pointer;\"><u>Print</u></span></td><td width=\"5\"></td>";
				}
				if (str1.length() > 0)
					result += "<td>" + str1 + "</td>";
				result += "<td align=\"right\">" + str2 + "</td></tr></table>";
			}else {
				if (printOption) {
					result= "<table width=\"" + (width==null?"100%":width) + "\" " + (navStyle==null?"":"class=\""+ navStyle+" \"") + "><tr>" +
					        "<td nowrap><span onclick=\"javascript:printListingTable('listing_" +property +"')\" style=\"font-family:'Wingdings 2';font-size:14pt;text-decoration:none;cursor:pointer;\">6</span>&nbsp;<span onclick=\"javascript:printListingTable('listing_" +property +"')\" style=\"cursor:pointer;\"><u>Print</u></span></td><td width=\"5\"></td>" +
					        "</tr></table>";
				}
			}

		} catch (Exception e) {
			throw new JspException(e.getMessage());
		}
		return result;
	}
	
	/**
	 * Return all hidden fields
	 * @return
	 */
	private String getHiddenFileds() {
		String result = "<input type=\"hidden\" name=\"" + this.pagingProperty + ".sortField\" value=\"" + (pagingVO.getSortField()==null?"":pagingVO.getSortField()) + "\">" +
		                "<input type=\"hidden\" name=\"" + this.pagingProperty + ".dateType\" value=\"" + pagingVO.isDateType() + "\">" +
                        "<input type=\"hidden\" name=\"" + this.pagingProperty + ".sortType\" value=\"" + (pagingVO.getSortType() ==null?"":pagingVO.getSortType()) + "\">" +
                        "<input type=\"hidden\" name=\"" + this.pagingProperty + ".pageSize\" value=\"" +pagingVO.getPageSize() + "\">";
		if (totalPages <= 1)
			result += "<input type=\"hidden\" name=\"" + this.pagingProperty + ".pageNum\" value=\"1\">";
		else 
			result += "<input type=\"hidden\" name=\"" + this.pagingProperty + ".pageNum\" value=\"" + pagingVO.getPageNum() + "\">";
		
        return result;
	}
	/**
	 * Get display record
	 * @param inputList
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Collection getDisplayList(ArrayList inputList) {
		
		if (pagingVO.getSortField() != null  && pagingVO.getSortField().trim().length() > 0) {
			if (pagingVO.getSortType().equals(PagingVO.ASC_SORT)) {
				Collections.sort(inputList, new ObjectAsc(pagingVO.getSortField(), pagingVO.isDateType()));
			} else {
				Collections.sort(inputList, new ObjectDesc(pagingVO.getSortField(), pagingVO.isDateType()));
			}
		}
		int totalRecords = inputList.size();
		pagingVO.setTotalRecords(totalRecords);

	    int startRow = (pagingVO.getPageNum() * pagingVO.getPageSize()) - pagingVO.getPageSize() + 1;
	    if (startRow > totalRecords) startRow = 1;

	    int endRow = startRow + pagingVO.getPageSize() - 1;
	    if (endRow > totalRecords) endRow = totalRecords;
	    Collection displayList = new ArrayList();
	    for ( int i = startRow; i <= endRow; i++ ) {
	    	displayList.add(inputList.get(i-1));
	    }
	    return displayList;
	 
	}
	
}
