package main.java.com.greendev.pragma.database;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import main.java.com.greendev.pragma.database.databaseModel.GoesMap;

/**
 * Creates a list of GoesMap objects containing the maps for each variable
 * with the map image paths inside the OUTPUT directory
 * @author miguelgd
 *
 */
public class GoesImageFinder {

	List<GoesMap> maps;
	
	private static final Logger logger = Logger.getLogger(GoesImageFinder.class);

	/**
	 * Generates the list of populated GoesMap objects
	 * @param date the automation execution date
	 * @param directory OUTPUT directory for the given date
	 * @param variables list of all GOES-PRWEB variables
	 * @return list of GoesMap objects
	 */
	public List<GoesMap> getMapsForDate(DateTime date, File directory, List<String> variables){

		maps = new ArrayList<GoesMap>();
		
		for(String variable : variables){
			String imagePath = FileUtils.getFile(directory, variable).getAbsolutePath()+".jpg";
			GoesMap map = new GoesMap(variable,date,imagePath);
			maps.add(map);
		}
		
		logger.info("Finished generating list of GoesMaps, for database insertion size: "+maps.size());
		
		return maps;
	}

}
