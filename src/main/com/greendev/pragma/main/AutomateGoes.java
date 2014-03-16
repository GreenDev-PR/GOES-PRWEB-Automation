package main.com.greendev.pragma.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.mail.EmailException;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.joda.time.DateTime;

import com.google.gson.Gson;

import main.com.greendev.pragma.main.properties.GoesProperties;
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
	
	
	public AutomateGoes(String propertiesPath) throws IOException{
		this(propertiesPath, new DateTime());
	}

	public AutomateGoes(String propertiesPath, DateTime date) throws IOException {
		//Load automation properties
		this.loadProperties(propertiesPath);
		this.manager = new DirectoryManager(this.properties.getGoesDir());
		this.fromDate = date;
		this.executionDate = new DateTime();
		configureFileAppender();
	}
	
	public void configureFileAppender() throws IOException{
		//Create log directory
		File logDir = manager.getLogDirectory();
		
		File log = new File(logDir, format(this.executionDate, LOG_NAME_FORMAT));
		FileAppender fa = new FileAppender(new PatternLayout(properties.getLogLayout(), log.getCanonicalPath()));
		fa.setThreshold(level.DEBUG);
		fa.setAppend(false);
		fa.activateOptions();
	}
	
	/**
	 * Loads automation properties values.
	 * @param propertiesPath The location of the .json automation properties file
	 * @throws FileNotFoundException The path supplied was not located.
	 */
	public void loadProperties(String propertiesPath) throws FileNotFoundException{
		File props = new File(propertiesPath);
		Gson gson = new Gson();
		this.properties = gson.fromJson(new FileReader(props),GoesProperties.class);
	}
	
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
	
	private String format(Date date, String format) {
		return GoesUtils.stringFormatTime(format, date);
	}
}
