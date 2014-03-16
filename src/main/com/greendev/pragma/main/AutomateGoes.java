package main.com.greendev.pragma.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.mail.EmailException;
import org.joda.time.DateTime;

import main.com.greendev.pragma.main.properties.GoesProperties;
/**
 * Automate 
 * @author josediaz
 *
 */
public class AutomateGoes {
	
	private GoesProperties properties;
	private DateTime date;
	private DateTime executionDate;
	private DirectoryManager manager;
	
	public AutomateGoes(String propertiesPath) throws IOException{
		this(propertiesPath, new DateTime());
	}

	public AutomateGoes(String propertiesPath, DateTime date2) throws IOException {
		loadProperties(propertiesPath);
		manager = new DirectoryManager(this.properties.getGoesDir());
		this.date = date2;
		executionDate = new DateTime();
	
		
	}
	
	public void configureFileAppender() throws IOException{
		
		
		
	}
	
	public void loadProperties(String properties) throws FileNotFoundException{
		
		
		
	}
	
	public void makeDirs(){
		
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

}
