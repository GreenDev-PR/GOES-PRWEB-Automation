//TODO: Test download()
package main.com.greendev.pragma.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.apache.commons.mail.EmailException;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.LogMF;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.joda.time.DateTime;

import com.google.gson.Gson;

import main.com.greendev.pragma.database.DbManager;
import main.com.greendev.pragma.degrib.DegribVariable;
import main.com.greendev.pragma.degrib.Degribber;
import main.com.greendev.pragma.download.Download;
import main.com.greendev.pragma.download.Downloader;
import main.com.greendev.pragma.download.DownloaderFactory;
import main.com.greendev.pragma.download.RetryDownloader;
import main.com.greendev.pragma.email.Emailer;
import main.com.greendev.pragma.main.properties.GoesProperties;
import main.com.greendev.pragma.utils.GoesUtils;
import main.com.greendev.pragma.main.DirectoryManager;
/**
 * AutomateGoes class is a top level class that leverages individual packages
 * to provide functionality of the program. 
 * @author josediaz
 *
 */
public class AutomateGoes {

	private static final String LOG_NAME_FORMAT = "log_%tY%tm%td.log";
	private static final String CSV_DATE_FORMAT = "%tY%tm%td";
	private static final String MATLAB_CMD_FORMAT = "date='%tm/%td/%tY';";
	private static final Logger logger = Logger.getLogger(AutomateGoes.class);
	private static final int ATTEMPTS = 3;
	private static final long WAIT_TIME = 60*1000; // 1 minute
	private static final int MATLAB_ATTEMPTS = 2; //Number of times to attempt to run Matlab
	private GoesProperties goesProperties;
	private DateTime fromDate;
	private DateTime executionDate;
	private DirectoryManager dirManager;
	private DbManager dbManager;
	private Connection conn;

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
		
		//Create database connection
		try {
			String url = "jdbc:postgresql://"
					+goesProperties.getDatabase().getHost() + ":" 
					+goesProperties.getDatabase().getPort() + "/"
					+goesProperties.getDatabase().getDatabase();
			String username = goesProperties.getDatabase().getUsername();
			String password = goesProperties.getDatabase().getPassword();
			
			this.conn = DriverManager.getConnection(url,username,password);
			logger.info("Created database connection for url: "+url);
		} catch (SQLException e) {
			logger.error("Errror trying to connect to database", e);
		}
		
		this.dbManager = new DbManager(conn);
		this.dirManager = new DirectoryManager(this.goesProperties.getGoesDir());

		logger.info("Working Directory " + dirManager.getRootDirectory().getCanonicalPath());
		this.fromDate = date;
		this.executionDate = new DateTime();
		this.configureFileAppender();
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
	 * Configures log file management. 
	 * @throws IOException Error handling files.
	 */
	public void configureFileAppender() throws IOException{
		//Create log directory e.g. /LOG
		File logDir = this.dirManager.getLogDirectory();

		File log = new File(logDir, this.format(this.executionDate.toDate(), LOG_NAME_FORMAT));

		//Create a file appender to record log events
		FileAppender fileAppender = new FileAppender(new PatternLayout(
				this.goesProperties.getLogLayout()), log.getCanonicalPath());

		//Configure logger append level
		fileAppender.setThreshold(Level.DEBUG);

		fileAppender.setAppend(false);
		//File is actually opened
		fileAppender.activateOptions();

		Logger.getRootLogger().addAppender(fileAppender);
	}

	/**
	 * Creates directory structure for supplied date
	 */
	public void makeDirs(){
		logger.info("working dir date " + fromDate);
		//If data exists for given date, archive it.
		try {
			dirManager.archiveDataForCurrentDate(fromDate);
		} catch (IOException e) {
			logger.error("Error archiving old files", e);
		}
		//If directory hierarchy is absent, created. If not do nothing.
		dirManager.createDirectoriesForDateTime(fromDate);
	}

	/**
	 * Performed the downloads
	 */
	public void download(){

		String absolute = this.getWorkingDirectory().getAbsolutePath();

		for(Download download : goesProperties.getDownloads()){
			//By convention one must add the offset of the download
			DateTime workDate = this.fromDate.plusDays(download.getDateOffset());

			Download tempDownload = new Download(download);

			tempDownload.setUrl(this.format(workDate.toDate(), download.getUrl()) );

			tempDownload.setSaveLocation(FilenameUtils.concat(absolute, 
					this.format(workDate.toDate(), download.getSaveLocation())) );

			Downloader downloader = DownloaderFactory.getDownloader(tempDownload);

			if(downloader != null){
				//RetryDownloader integrates retry attempts mechanisms
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
		//Which directory contains the files to degrib 
		degrib.setDegribDirectory(dir);
		//To which directory output the processed files
		degrib.setOutputDirectory(this.dirManager.getInputDirectory(fromDate));

		for (DegribVariable variable : degrib.getVariables()) {
			variable.setOutputName(this.format(this.fromDate.toDate(), variable.getOutputName()) );
		}
		//Degrib each variable using Degribber class
		try {
			degrib.degribVariables();
		} catch (IOException e) {	
			logger.error("Error degribing variables ",e);
		}

	}

	/**
	 * Executes maltlab command. This method provides fallback
	 * mechanism in case matlab execution end prematurely. If
	 * the endFileFlag is not found, it will re-attempt to execute
	 * matlab. 
	 * @return true if Matlab completed.
	 */
	public boolean matlab(){
		boolean finished = false;
		int matlabCounter = 0;
		
		String command = goesProperties.getMatlabCmd() +  '"'+this.format(this.fromDate.toDate(),MATLAB_CMD_FORMAT)+'"';
		CommandLine cmd = CommandLine.parse(command);
		
		DefaultExecutor executor = new DefaultExecutor();
		executor.setWorkingDirectory(new File(goesProperties.getMatlabWorkingDirectory()));
		
		//  In case matlab processing is interrupted
		//	Fallback mechanism is in place.
		while( matlabCounter < MATLAB_ATTEMPTS && !finished)
		{
			//clean up previous attempted data.
			try {
				logger.info("Performing matlab output directory cleanup");
				this.dirManager.cleanUp(fromDate);
			} catch (IOException e1) {
				logger.error("Error trying to clean up /OUTPUT directory ",e1);
			}
			
			try {
				//execute matlab 
				logger.info("Going to try to execute Matlab commond: "+cmd);
				executor.execute(cmd);
			} catch (ExecuteException e) {
				logger.error("Error executing matlab ",e);
			} catch (IOException e) {
				logger.error("Error getting matlab working directory ",e);
			}
			//Wait for matlab to finish
			finished = waitForFinishedFile();
			//if not finished, it will re-attempt the download until 
			//max number of tries is reached.
		}
		logger.info("Matlab completed with successStatus: "+finished);
		
		return finished;
	}

	/**
	 * Waits until a specified file is found in the supplied directory. 
	 * @param directory The directory in which to look for the file
	 * @param fileName The file to look for
	 * @return True, if the file was found
	 */
	public boolean waitForFile(String directory, String fileName){

		LogMF.info(logger, "Waiting for file, looking for {0} in {1}",fileName, directory);

		FileAlterationObserver observer = new FileAlterationObserver(new File(directory));
		FileCreatedListener listener = new FileCreatedListener(fileName);
		observer.addListener(listener);

		long timeToWait = GoesUtils.convertSecondsToMillis(goesProperties.getFinished().getSeconds());
		int tries = 0;
		int numberOfTries = goesProperties.getFinished().getTries();
		boolean found = false;

		while (!found && numberOfTries >= tries) {
			observer.checkAndNotify(); 		//verify for file existance
			found = listener.isFileFound(); //update found value
			LogMF.info(logger, "Matlab end file isFoundValue: {0}, attempValue: {1}",found,tries);
			tries++;
			try {
				Thread.sleep(timeToWait);
			} catch (InterruptedException ignore) {}
		}
		
		return found;
	}

	/**
	 * Wait for the finished file.
	 * @return True, if the finished file is found.
	 */
	public boolean waitForFinishedFile(){
		File workingDir = dirManager.getOutputDirectory(this.fromDate);

		boolean result = this.waitForFile(workingDir.getAbsolutePath(),
				goesProperties.getFinished().getFileName());
		if (!result) {
			logger.error("Couldn't find the matlab file ");
		}else{
			logger.info("Matlab end file found!");
		} 
		return result;
	}

	/**
	 * Insert to database goes data and
	 * goes map images
	 * @throws SQLException 
	 * @throws FileNotFoundException 
	 */
	public void insertToDb(){

		List<String> goesVariableNameList = null;

		try {
			goesVariableNameList = this.dbManager.readGoesVariables();
		} catch (SQLException e) {
			logger.error("Error reading goesVariable List from database",e);
		}

		File outFolder = dirManager.getOutputDirectory(this.fromDate);

		for(String variable: goesVariableNameList){
			String csvName = GoesUtils.stringFormatTime(variable+CSV_DATE_FORMAT, 
					this.fromDate.toDate())+".csv";
			File csvFile = FileUtils.getFile(outFolder,csvName);

			try {
				this.dbManager.storeGoesData(variable, csvFile, this.fromDate);
			} catch (FileNotFoundException e) {
				logger.error("Error locating csv file",e);
			} catch (SQLException e) {
				logger.error("Error storing does data in database",e);
			}
		}

		try {
			this.dbManager.storeGoesMap(goesVariableNameList, outFolder, this.fromDate);
		} catch (SQLException e) {
			logger.error("Error trying to inserGoesMaps ",e);
		}
	}

	/**
	 * Emails log files to properties specified in the GoesProperties JSON file
	 * @throws IOException
	 * @throws EmailException
	 */
	public void emailLog() throws IOException, EmailException{
		File logDirectory = dirManager.getLogDirectory();
		Emailer emailer = new Emailer(this.goesProperties.getEmail());
		emailer.sendEmailWithAttachment(
				"GOES-PRWEB LOG for " + executionDate,
				"Log",FileUtils.getFile(logDirectory,
						this.format(executionDate.toDate(), LOG_NAME_FORMAT)));
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
	 * Gets the GoesProperties object.
	 * @return The generated from JSON GoesProperties .
	 */
	public GoesProperties getGoesProperties(){
		return this.goesProperties;
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
