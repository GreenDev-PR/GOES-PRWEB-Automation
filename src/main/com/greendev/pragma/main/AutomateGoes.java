package main.com.greendev.pragma.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.mail.EmailException;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.joda.time.DateTime;

import com.google.gson.Gson;

import main.com.greendev.pragma.degrib.DegribVariable;
import main.com.greendev.pragma.degrib.Degribber;
import main.com.greendev.pragma.download.Download;
import main.com.greendev.pragma.download.Downloader;
import main.com.greendev.pragma.download.DownloaderFactory;
import main.com.greendev.pragma.download.RetryDownloader;
import main.com.greendev.pragma.main.properties.GoesProperties;
import main.com.greendev.pragma.utils.GoesUtils;
/**
 *  
 * @author josediaz
 *
 */
public class AutomateGoes {
	
	private GoesProperties goesProperties;
	private DateTime fromDate;
	private DateTime executionDate;
	private DirectoryManager dirManager;
	private static final String LOG_NAME_FORMAT = "log_%tY%tm%td.log";
	private static final Logger logger = Logger.getLogger(AutomateGoes.class);
	private static final int ATTEMPTS = 3;
	private static final long WAIT_TIME = 60*1000; // 1 minute
	
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
		this.dirManager = new DirectoryManager(this.goesProperties.getGoesDir());
		logger.info("Working Directory " + dirManager.getRootDirectory().getCanonicalPath());
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
		File logDir = this.dirManager.getLogDirectory();
		
		File log = new File(logDir, this.format(this.executionDate.toDate(), LOG_NAME_FORMAT));
		
		//Create a file appender to record log events
		FileAppender fa = new FileAppender(new PatternLayout(
				this.goesProperties.getLogLayout()), log.getCanonicalPath());
		
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
		this.goesProperties = gson.fromJson(new FileReader(props),GoesProperties.class);
	}
	
	/**
	 * Creates directory structure for supplied date
	 */
	public void makeDirs(){
		logger.info("working dir date " + fromDate);
		dirManager.createDirectoriesForDateTime(fromDate);
	}
	
	/**
	 * Performed the downloads
	 */
	public void download(){
	
		String absolute = this.getWorkingDirectory().getAbsolutePath();
		
		for(Download download : goesProperties.getDownloads()){
			//ojo
			DateTime workDate = fromDate.plusDays(download.getDateOffset());
			
			Download tempDownload = new Download(download);
			
			tempDownload.setUrl(this.format(workDate.toDate(), download.getUrl()) );
			
			tempDownload.setSaveLocation(FilenameUtils.concat(absolute, 
				this.format(workDate.toDate(), download.getSaveLocation())) );
			
			Downloader downloader = DownloaderFactory.getDownloader(tempDownload);
			
			if(downloader != null){
				Downloader retryDownloader = new RetryDownloader(downloader, ATTEMPTS, WAIT_TIME);
				if(retryDownloader.downloadExists()){
					try{
						retryDownloader.download();
					} catch (IOException e){
						logger.error("Error download "+ tempDownload, e);
					}
				}
			}else{
				logger.error("Couldn't find a downloader for the following download "
			+ download);
			}
		}
	}
	
	/**
	 * Degribs the downloaded data
	 */
	public void degrib(){
		File dir = getWorkingDirectory();
		
		Degribber degrib = this.goesProperties.getDegribber();		
		degrib.setDegribDirectory(dir);
		degrib.setOutputDirectory(dir);
		
		for (DegribVariable variable : degrib.getVariables()) {
			variable.setOutputName(this.format(this.fromDate.toDate(), variable.getOutputName()) );
		}
		//Degrib each variable using Degribber class
		try {
			degrib.degribVariables();
		} catch (IOException e) {	
			logger.error("Error degribing variables");
			e.printStackTrace();
		}

	}
	
	/**
	 * Run matlab 
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
	 * Gets the date from which to start collecting data.
	 * @return The date from which to start collecting data.
	 */
	public DateTime getDate() {
		return fromDate;
	}

	/**
	 * Sets the date from which to start collecting data.
	 * @param start The date from which to start collecting data.
	 */
	public void setDate(DateTime start) {
		this.fromDate = start;
	}
	
	/**
	 * Gets the path to the working directory.
	 * @return The path the working directory.
	 */
	public File getWorkingDirectory(){
		return dirManager.getDirectory(this.fromDate);
	}
	
	/**
	 * Provides string formatting 
	 * @param date 
	 * @param format
	 * @return The string formatted
	 */
	private String format(Date date, String format) {
		return GoesUtils.stringFormatTime(format, date);
	}
}
