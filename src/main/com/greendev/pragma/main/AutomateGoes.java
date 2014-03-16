package main.com.greendev.pragma.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.mail.EmailException;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.joda.time.DateTime;

import com.google.gson.Gson;

import main.com.greendev.pragma.main.properties.GoesProperties;
import main.com.greendev.pragma.utils.GoesUtils;
/**
 * Automate 
 * @author josediaz
 *
 */
public class AutomateGoes {
	
	private GoesProperties properties;
	private DateTime fromDate;
	private DateTime executionDate;
	private DirectoryManager manager;
	private static final String LOG_NAME_FORMAT = "log_%tY%tm%td.log";
	private static final Logger logger = Logger.getLogger(AutomateGoes.class);
	
	/**
	 * Constructs an automate goes object from a supplied propertiesPath
	 * @param propertiesPath The location of the properties file
	 * @throws IOException 
	 */
	public AutomateGoes(String propertiesPath) throws IOException{
		this(propertiesPath, new DateTime());
	}

	/**
	 * Constructs an automate goes object from a supplied propertiesPath and a
	 * given run date.
	 * @param propertiesPath The location of the properties file
	 * @param date The date from which to start collecting data
	 * @throws IOException
	 */
	public AutomateGoes(String propertiesPath, DateTime date) throws IOException {
		//Load automation properties
		this.loadProperties(propertiesPath);
		this.manager = new DirectoryManager(this.properties.getGoesDir());
		logger.info("Working Directory " + manager.getRootDirectory().getCanonicalPath());
		this.fromDate = date;
		this.executionDate = new DateTime();
		this.configureFileAppender();
	}
	
	/**
	 * Configures log file management. 
	 * @throws IOException Error handling files.
	 */
	public void configureFileAppender() throws IOException{
		//Create log directory e.g. /LOG
		File logDir = manager.getLogDirectory();
		
		File log = new File(logDir, this.format(this.executionDate.toDate(), LOG_NAME_FORMAT));
		
		//Create a file appender to record log events
		FileAppender fa = new FileAppender(new PatternLayout(
				this.properties.getLogLayout()), log.getCanonicalPath());
		
		//Configure logger append level
		fa.setThreshold(Level.DEBUG);
		
		fa.setAppend(false);
		//File is actually opened
		fa.activateOptions();
		
		Logger.getRootLogger().addAppender(fa);
	}
	
	/**
	 * Loads automation properties values from external JSON file.
	 * @param propertiesPath The location of the external JSON automation properties file
	 * @throws FileNotFoundException The path supplied was not located.
	 */
	public void loadProperties(String propertiesPath) throws FileNotFoundException{
		File props = new File(propertiesPath);
		Gson gson = new Gson();
		//Read properties from external JSON file
		this.properties = gson.fromJson(new FileReader(props),GoesProperties.class);
	}
	
	/**
	 * Creates directory structure for supplied date
	 */
	public void makeDirs(){
		logger.info("working dir date " + fromDate);
		manager.createDirectoriesForDateTime(fromDate);
	}
	
	public void download(){
		
	}
	
	public void degrib(){
		
	}
	
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

	public DateTime getDate() {
		return date;
	}

	public void setDate(DateTime start) {
		this.date = start;
	}
	
	public File getWorkingDirectory(){
		return manager.getDirectory(date);
	}
	
	/**
	 * Helper method
	 * @param date
	 * @param format
	 * @return
	 */
	private String format(Date date, String format) {
		return GoesUtils.stringFormatTime(format, date);
	}
}
