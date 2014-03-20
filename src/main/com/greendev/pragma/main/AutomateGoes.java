package main.com.greendev.pragma.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.apache.commons.lang3.time.DateUtils;
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
import main.com.greendev.pragma.main.properties.EmailProperties;
import main.com.greendev.pragma.utils.GoesUtils;
/**
 * AutomateGoes
 * @author josediaz
 *
 */
public class AutomateGoes {

	private static final String LOG_NAME_FORMAT = "log_%tY%tm%td.log";
	private static final String CSV_DATE_FORMAT = "%tY%tm%td";
	private static final Logger logger = Logger.getLogger(AutomateGoes.class);
	private static final int ATTEMPTS = 3;
	private static final long WAIT_TIME = 60*1000; // 1 minute
	private GoesProperties goesProperties;
	private DateTime fromDate;
	private DateTime executionDate;
	private DirectoryManager dirManager;
	private DbManager dbManager;

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
		//this.dbManager = new DbManager();
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
		//If data exists for given date, archive it.
		try {
			dirManager.archiveDataForCurrentDate(fromDate);
		} catch (IOException e) {
			LogMF.info(logger, "Error archiving old files", null);
			e.printStackTrace();
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
	 * @throws IOException 
	 * @throws ExecuteException 
	 */
	public void matlab(){
		CommandLine cmd = CommandLine.parse(goesProperties.getMatlabCmd());
		
		//Delete file used as flag for matlab completion
		File finishedMatFile = new File(this.dirManager.getOutputDirectory(fromDate),
				this.goesProperties.getFinished().getFileName());
		FileUtils.deleteQuietly(finishedMatFile);
		
		DefaultExecutor executor = new DefaultExecutor();
		executor.setWorkingDirectory(new File(goesProperties.getMatlabWorkingDirectory()));
		try {
			executor.execute(cmd);
		} catch (ExecuteException e) {
			logger.error("Error executing matlab");
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("Error getting matlab working directory");
			e.printStackTrace();
		}
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
		
		observer.checkAndNotify();
		while (!listener.isFileFound()) {
			LogMF.debug(logger, "Listener value: {0}", listener.isFileFound());
			LogMF.debug(logger, "Waiting for {0} file, tries_variable value = {1}",fileName,tries);
			tries++;
			if (tries >= numberOfTries) {
				return false;
			}
			try {
				Thread.sleep(timeToWait);
			} catch (InterruptedException ignore) {}

			observer.checkAndNotify();
		}
		return true;
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
			LogMF.info(logger, "Error reading goesVariable List from database",null);
			e.printStackTrace();
		}

		File outFolder = dirManager.getOutputDirectory(this.fromDate);

		for(String variable: goesVariableNameList){
			String csvName = GoesUtils.stringFormatTime(variable+CSV_DATE_FORMAT, 
					this.fromDate.toDate())+".csv";
			File csvFile = FileUtils.getFile(outFolder,csvName);

			try {
				this.dbManager.storeGoesData(variable, csvFile, this.fromDate);
			} catch (FileNotFoundException e) {
				LogMF.info(logger, "Error locating csv file",null);
				e.printStackTrace();
			} catch (SQLException e) {
				LogMF.info(logger, "Error storing does data in database",null);
				e.printStackTrace();
			}
		}

		try {
			this.dbManager.storeGoesMap(goesVariableNameList, outFolder, this.fromDate);
		} catch (SQLException e) {
			e.printStackTrace();
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
