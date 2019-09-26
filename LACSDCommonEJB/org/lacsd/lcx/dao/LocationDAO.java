package org.lacsd.lcx.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.commons.lang3.StringEscapeUtils;




import org.lacsd.common.constants.LACSDSPConstants;
import org.lacsd.common.dao.LACSDSqlServerProcDAO;
import org.lacsd.common.exceptions.LACSDException;
import org.lacsd.common.values.LocationVO;


public class LocationDAO extends LACSDSqlServerProcDAO { 
	
	private String action; 
	private LocationVO locationVO; 
	private HashMap<String, ArrayList<LocationVO>> mapValues;
		
	private static final String LCX_ACTION_GET_FACILITIES = "F"; 
	private static final String LCX_ACTION_GET_BUILDINGS = "B"; 
	private static final String LCX_ACTION_GET_DEPARTMENTS = "Y"; 
	private static final String LCX_ACTION_GET_SECTIONS = "S"; 
	
	private static final String LCX_ACTION_GET_ACTIVE_FACILITIES = "AF"; 
	private static final String LCX_ACTION_GET_ACTIVE_BUILDINGS = "AB"; 
	private static final String LCX_ACTION_GET_ACTIVE_DEPARTMENTS = "AY"; 
	private static final String LCX_ACTION_GET_ACTIVE_SECTIONS = "AS";
	
	/**
	 * Constructor - Uses Stored Procedure [LCXLOCATION100SP]
	*/
	public LocationDAO() {
		super(LACSDSPConstants.LCXLOCATION100SP);
		locationVO = new LocationVO();
	}
	
	public LocationVO getFacilities() throws LACSDException{
		log.debug("ENTERING get facilities"); 
		super.setQueryType(EXECUTE_QUERY);
		this.action = LCX_ACTION_GET_FACILITIES;
		
		execute();
	    
		log.debug("EXITING get facilities");

	    return locationVO;
		
	}

	/**
	 * Get Hashmap between facility and building
	 * @return
	 * @throws LACSDException
	 */
	public HashMap<String, ArrayList<LocationVO>> getFacBuildingMaps() throws LACSDException{
		log.debug("ENTERING  getFacBuildingMaps"); 
		super.setQueryType(EXECUTE_QUERY);
		this.action = LCX_ACTION_GET_BUILDINGS;
		execute();
	    
		log.debug("EXITING getFacBuildingMaps");

	    return mapValues;
		
	}
	
	/**
	 * Get Hashmap between department and section
	 * @return
	 * @throws LACSDException
	 */
	public HashMap<String, ArrayList<LocationVO>> getDeptSectionMaps() throws LACSDException{
		log.debug("ENTERING  getDeptSectionMaps"); 
		super.setQueryType(EXECUTE_QUERY);
		this.action = LCX_ACTION_GET_SECTIONS;
		execute();
	    
		log.debug("EXITING getDeptSectionMaps");

	    return mapValues;
		
	}	
	
    
   public LocationVO getDepartments() throws LACSDException{ 
	   
	   log.debug("ENTERING get departments"); 
		super.setQueryType(EXECUTE_QUERY);
		this.action = LCX_ACTION_GET_DEPARTMENTS;
		
		execute();
	    log.debug("EXITING get departments");

	   return locationVO;
	}
  
   //The following methods retrieve locations that are not obsolete.
   public LocationVO getActiveFacilities() throws LACSDException{
		log.debug("ENTERING get facilities"); 
		super.setQueryType(EXECUTE_QUERY);
		this.action = LCX_ACTION_GET_ACTIVE_FACILITIES;
		
		execute();
	    
		log.debug("EXITING get facilities");

	    return locationVO;
		
	}

	/**
	 * Get Hashmap between facility and building
	 * @return
	 * @throws LACSDException
	 */
	public HashMap<String, ArrayList<LocationVO>> getActiveFacBuildingMaps() throws LACSDException{
		log.debug("ENTERING  getFacBuildingMaps"); 
		super.setQueryType(EXECUTE_QUERY);
		this.action = LCX_ACTION_GET_ACTIVE_BUILDINGS;
		execute();
	    
		log.debug("EXITING getFacBuildingMaps");

	    return mapValues;
		
	}
	
	/**
	 * Get Hashmap between department and section
	 * @return
	 * @throws LACSDException
	 */
	public HashMap<String, ArrayList<LocationVO>> getActiveDeptSectionMaps() throws LACSDException{
		log.debug("ENTERING  getDeptSectionMaps"); 
		super.setQueryType(EXECUTE_QUERY);
		this.action = LCX_ACTION_GET_ACTIVE_SECTIONS;
		execute();
	    
		log.debug("EXITING getDeptSectionMaps");

	    return mapValues;
		
	}	
	
   
  public LocationVO getActiveDepartments() throws LACSDException{ 
	   
	   log.debug("ENTERING get departments"); 
		super.setQueryType(EXECUTE_QUERY);
		this.action = LCX_ACTION_GET_ACTIVE_DEPARTMENTS;
		
		execute();
	    log.debug("EXITING get departments");

	   return locationVO;
	}
	
 	
	/**
	 * HANDLE OUTPUT TYPE 1:	Stored Procedure Returns an Open Cursor
	 * @param ResultSet rs
	 * @return void
	 * @throws SQLException, LACSDException
	*/
	
	protected void getResultsFromResultSet(ResultSet rs) throws SQLException, LACSDException {
		
		LocationVO location; 
		
		if (action.equals( LCX_ACTION_GET_FACILITIES)|| action.equals( LCX_ACTION_GET_ACTIVE_FACILITIES) ){ 
			ArrayList<LocationVO> locationList = new ArrayList<LocationVO>();
			while (rs.next()) {
				location = new LocationVO();
				location.setLocationID(rs.getString("FacilityID")); 
				location.setDescription(StringEscapeUtils.escapeHtml3(rs.getString("Descr")));
				locationList.add(location);
			}
			locationVO.setLocationList(locationList);
		}else if (action.equals(LCX_ACTION_GET_DEPARTMENTS)|| action.equals(LCX_ACTION_GET_ACTIVE_DEPARTMENTS)){ 
			ArrayList<LocationVO> locationList = new ArrayList<LocationVO>();
			while (rs.next()) {
				location = new LocationVO();
				location.setLocationID(rs.getString("DepartmentID")); 
				location.setDescription(StringEscapeUtils.escapeHtml3(rs.getString("Descr")));
				locationList.add(location);
			}
			locationVO.setLocationList(locationList);
			
		}else if (action.equals(LCX_ACTION_GET_BUILDINGS)|| action.equals(LCX_ACTION_GET_ACTIVE_BUILDINGS)){ 
			mapValues = new HashMap<String, ArrayList<LocationVO>>(); 
			ArrayList<LocationVO> buildingList  =  new ArrayList<LocationVO>();
			LocationVO buidingVO;
			String facID = "";
			while (rs.next()) {
				if (!facID.equalsIgnoreCase(rs.getString("FacilityID"))){
					if (facID.length() > 0) {
						mapValues.put(facID, buildingList);
					}
					buildingList = new ArrayList<LocationVO>();
				}
				buidingVO = new LocationVO();
				buidingVO.setLocationID(rs.getString("BuildingID")==null?"":rs.getString("BuildingID")); 
				buidingVO.setDescription(StringEscapeUtils.escapeHtml3(rs.getString("Descr")));
				buildingList.add(buidingVO);
				facID = rs.getString("FacilityID");
			}
			mapValues.put(facID, buildingList);
		}else if (action.equals(LCX_ACTION_GET_SECTIONS)|| action.equals(LCX_ACTION_GET_ACTIVE_SECTIONS)){ 
			mapValues = new HashMap<String, ArrayList<LocationVO>>(); 
			ArrayList<LocationVO> sectionList  =  new ArrayList<LocationVO>();
			LocationVO buidingVO;
			String deptID = "";
			while (rs.next()) {
				if (!deptID.equalsIgnoreCase(rs.getString("DepartmentID"))){
					if (deptID.length() > 0) {
						mapValues.put(deptID, sectionList);
					}
					sectionList = new ArrayList<LocationVO>();
				}
				buidingVO = new LocationVO();
				buidingVO.setLocationID(rs.getString("SectionID")==null?"":rs.getString("SectionID")); 
				//buidingVO.setDescription(StringEscapeUtils.escapeHtml(rs.getString("Descr"))); 
				//07/07/16 - Added the following change to unescape special characters like '&'
				buidingVO.setDescription(rs.getString("Descr"));
				buidingVO.setUnEscapeDescription(StringEscapeUtils.escapeEcmaScript (rs.getString("Descr")));
				
				sectionList.add(buidingVO);
				deptID = rs.getString("DepartmentID");
			}
			mapValues.put(deptID, sectionList);
		}		
		
	}
	
	/* (non-Javadoc)
	 * @see org.lacsd.common.dao.LACSDSqlServerProcDAO#getResultsFromString(java.lang.String[])
	 */
	
	protected void getResultsFromString(String[] output) throws LACSDException {
		// TODO Auto-generated method stub
		
		
	}
	
	/**
	 * SETUP STEP 1:  Register Input Parameters
	 * @return void
	 * @throws LACSDException
	*/
	
	protected void registerInputs() throws LACSDException {
			setInputParam(1, this.action); 
	}

	/**
	 * SETUP STEP 2:	Register Output Parameters
	 * @return void
	 * @throws LACSDException
	*/
	
	protected void registerOutputs() throws LACSDException { 
		//super.setOutput(1, Types.VARCHAR); 
	  //setOutput(1, Types.VARCHAR);
		

	}


}
