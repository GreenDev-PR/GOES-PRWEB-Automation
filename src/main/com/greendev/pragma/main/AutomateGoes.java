package main.com.greendev.pragma.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.mail.EmailException;
import org.apache.log4j.Logger;

import main.com.greendev.pragma.degrib.DegribVariable;
import main.com.greendev.pragma.degrib.Degribber;
import main.com.greendev.pragma.main.properties.GoesProperties;
/**
 * Automate GOES-PRWEB algorithm 
 * @author josediaz
 *
 */
public class AutomateGoes {
	
	private GoesProperties properties;
	private Date fromDate;
	private Date executionDate;
	private DirectoryManager dirManager;
	private static final Logger logger = Logger.getLogger(AutomateGoes.class);
	private static final String LOG_NAME_FORMAT = "log_%tY%tm%td.log";
	
	/**
	 * Construct an AutomateGoes object with specified properties
	 * @param propertiesPath The properties for which to perform the automation
	 * @throws IOException
	 */
	public AutomateGoes(String propertiesPath) throws IOException{
		this(propertiesPath, new Date());
	}
	
	/**
	 * Constructs an AutomateGoes object with supplied properties and execution date
	 * @param propertiesPath The properties
	 * @param fromDate 
	 * @throws IOException
	 */
	public AutomateGoes(String propertiesPath, Date fromDate) throws IOException {
		this.loadProperties(propertiesPath);
		this.dirManager = new DirectoryManager(this.properties.getGoesDir());
		this.fromDate = fromDate;
		executionDate = new Date();
	}
	
	/**
	 * 
	 * @throws IOException
	 */
	public void configureFileAppender() throws IOException{
		
	}
	
	/**
	 * 
	 * @param properties
	 * @throws FileNotFoundException
	 */
	public void loadProperties(String properties) throws FileNotFoundException{
		
	}
	
	/**
	 * Creates directory structure: Input & Output directories
	 */
	public void makeDirs(){
		logger.info("working directory date "+this.fromDate);
		this.dirManager.createDirectoriesForDate(this.fromDate);
	}
	
	/**
	 * Performs downloads of data sets
	 * Robustness
	 */
	public void download(){
		
	}
	
	/**
	 * Degribs downloaded data sets into CSV format
	 */
	public void degrib(){
		File dir = getWorkingDirectory();
		Degribber degrib = properties.getDegribber();
		
		degrib.setDegribDirectory(dir);
		degrib.setOutputDirectory(dir);
		
		for(DegribVariable var: degrib.getVariables()){
			//TODO implement
		}
	}
	
	/**
	 * Executes the GOES-PRWEB algorithm Matlab process
	 */
	public void matlab(){
		
		//Delete file used as flag for matlab completion
		
	}
	
	public boolean waitForFinishedFile(){
		return false;
	}
	
	public boolean waitForFile(){
		return false;
	}
	
	public void insertToDb(){
		
	}
	
	public void emailLog() throws IOException, EmailException{
		
	}

	/**
	 * Gets the date for which to start collecting datasets
	 * @return
	 */
	public Date getDate() {
		return fromDate;
	}
	
	/**
	 * Sets the date for which to start collecting datasets 
	 * @param date 
	 */
	public void setDate(Date date) {
		this.fromDate = date;
	}
	
	/**
	 * Get the working directory 
	 * @return
	 */
	public File getWorkingDirectory(){
		return dirManager.getDirectory(fromDate);
	}

}
