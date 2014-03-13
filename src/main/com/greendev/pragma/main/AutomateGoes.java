package main.com.greendev.pragma.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.mail.EmailException;

import main.com.greendev.pragma.main.properties.GoesProperties;

public class AutomateGoes {
	
	private GoesProperties properties;
	private Date date;
	private Date executionDate;
	private DirectoryManager manager;
	
	public AutomateGoes(String propertiesPath) throws IOException{
		this(propertiesPath, new Date());
	}

	public AutomateGoes(String propertiesPath, Date date2) throws IOException {
		loadProperties(propertiesPath);
		manager = new DirectoryManager(this.properties.getGoesDir());
		this.date = date2;
		executionDate = new Date();
	
		
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

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	public File getWorkingDirectory(){
		return manager.getDirectory(date);
	}

}
