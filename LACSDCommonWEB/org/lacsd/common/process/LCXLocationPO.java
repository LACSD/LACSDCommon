package org.lacsd.common.process;

import java.util.ArrayList;
import java.util.HashMap;

import org.lacsd.common.exceptions.LACSDException;
import org.lacsd.common.values.LocationVO;
import org.lacsd.lcx.dao.LocationDAO;



public class LCXLocationPO extends LACSDGenericPO { 
	
	/**
	 * Get facility list, building, department and section 
	 * to populate the drop down lists. The maps contain the 
	 * parent ID alng with the description for each location type 
	 * as the key, value pair.
	 * @param LocationVO locationVO
	 * @return locationVO
	 * @throws LACSDException, Throwable 
	 */
	 public LocationVO getFacilities()throws LACSDException, Throwable {
		
		 LocationDAO locationDAO = new LocationDAO();
		 return locationDAO.getFacilities();
	 } 
	 
	 public LocationVO getDepartments()throws LACSDException, Throwable {
			
			 LocationDAO locationDAO = new LocationDAO();
			 return locationDAO.getDepartments();
	   } 
	  
	 public HashMap<String, ArrayList<LocationVO>> getFacBuildingMaps()throws LACSDException, Throwable {
		 LocationDAO locationDAO = new LocationDAO();
		 return locationDAO.getFacBuildingMaps();
	 }

	 public HashMap<String, ArrayList<LocationVO>> getDeptSectionMaps()throws LACSDException, Throwable {
		 LocationDAO locationDAO = new LocationDAO();
		 return locationDAO.getDeptSectionMaps();
	 } 
	 
	 /**
		 * Get facility list, building, department and section 
		 * that are not obsolete to populate the drop down lists. The maps contain the 
		 * parent ID alng with the description for each location type 
		 * as the key, value pair.
		 * @param LocationVO locationVO
		 * @return locationVO
		 * @throws LACSDException, Throwable 
		 */
	 
	 public LocationVO getActiveFacilities()throws LACSDException, Throwable {
			
		 LocationDAO locationDAO = new LocationDAO();
		 return locationDAO.getActiveFacilities();
	 } 
	 
	 public LocationVO getActiveDepartments()throws LACSDException, Throwable {
			
			 LocationDAO locationDAO = new LocationDAO();
			 return locationDAO.getActiveDepartments();
	   } 
	  
	 public HashMap<String, ArrayList<LocationVO>> getActiveFacBuildingMaps()throws LACSDException, Throwable {
		 LocationDAO locationDAO = new LocationDAO();
		 return locationDAO.getActiveFacBuildingMaps();
	 }

	 public HashMap<String, ArrayList<LocationVO>> getActiveDeptSectionMaps()throws LACSDException, Throwable {
		 LocationDAO locationDAO = new LocationDAO();
		 return locationDAO.getActiveDeptSectionMaps();
	 }  
		  
	

}
