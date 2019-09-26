package org.lacsd.common.util;

import java.io.File;
import java.io.FilenameFilter;


/**
 * @author katielee
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FileExtensionFilterUtil implements FilenameFilter {
	
	private String extension;
	
	public FileExtensionFilterUtil(String ext) {
		this.extension = ext;
	}
	
	/* (non-Javadoc)
	 * @see java.io.FileFilter#accept(java.io.File)
	 */
	public boolean accept(File directory, String filename) {
		
		if (filename != null && filename.endsWith(this.extension)) {
			return true;	
		} else {
			return false;
		} 
		
	}
	

}
