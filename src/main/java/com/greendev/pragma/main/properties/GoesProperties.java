package main.java.com.greendev.pragma.main.properties;

import java.util.Collections;
import java.util.List;

import main.java.com.greendev.pragma.degrib.Degribber;
import main.java.com.greendev.pragma.download.Download;

/**
 * Collection of properties necessary for program execution
 * @author miguelgd
 */
public class GoesProperties {

	private Degribber degribber;
	private List<Download> downloads;
	private String goesDir;
	private String logLayout;
	private List<String> variables;
	private EmailProperties email;
	private FinishProperties finished;
	private DatabaseProperties database;
	private RetryProperties retry;
	private MatlabProperties matlab;


	/**
	 * Creates an empty instance of GoesProperties
	 */
	public GoesProperties(){

	}

	/**
	 * Returns the configured degribber
	 * @return Degribber instance
	 */
	public Degribber getDegribber() {
		return degribber;
	}


	/**
	 * Configure the Degribber instance for the GoesProperties
	 * @param degribber degribber object to use as model
	 */
	public void setDegribber(Degribber degribber) {
		this.degribber = degribber;
	}

	/**
	 * Returns a list of downloads for the corrent GoesProperties configuration
	 * @return list of Download objects
	 */
	public List<Download> getDownloads() {
		return downloads;
	}

	/**
	 * Set the list of downloads for the download stage of the process
	 * @param downloads list of Download objects
	 */
	public void setDownloads(List<Download> downloads) {
		this.downloads = downloads;
	}

	/**
	 * Returns the layout of the log filename (e.g. YYYYMMDD.log)
	 * @return string layout for the log file with placeholders for date fields
	 */
	public String getLogLayout() {
		return logLayout;
	}

	/**
	 * Configure the layout of the log filename
	 * @param logLayout string representation of the log layout
	 */
	public void setLogLayout(String logLayout) {
		this.logLayout = logLayout;
	}

	/**
	 * Returns the list of GOES-PRWEB variables.
	 * @return list of Strings with names of GOES-PRWEB variables
	 */
	public List<String> getVariable() {
		return variables;
	}

	/**
	 * Configure the list of GOES-PRWEB variables
	 * @param variables list of Strings with names of GOES-PRWEB variables
	 */
	public void setVariable(List<String> variables) {
		this.variables = variables;
	}

	/**
	 * Returns the properties to use for emailing the log file
	 * @return EmailProperties object populated with email properties
	 */
	public EmailProperties getEmail() {
		return email;
	}

	/**
	 * Configure the EmailProperties based on a model EmailProperties object
	 * @param email EmailProperties object
	 */
	public void setEmail(EmailProperties email) {
		this.email = email;
	}

	/**
	 * Configure the root directory for automation execution
	 * @param goesDir
	 */
	public void setGoesDir(String goesDir) {
		this.goesDir = goesDir;
	}

	/**
	 * Returns the root directory for automation execution
	 * @return Name of application root directory
	 */
	public String getGoesDir() {
		return goesDir;
	}
	
	/**
	 * Returns the properties configured to signal that the matlab has finished,
	 * re-attempts and time intervals
	 * @return populated FinishProperties object
	 */
	public FinishProperties getFinished() {
		return finished;
	}

	/**
	 * Configure the properties used in the logic to determine if matlab has finished
	 * Includes flag file name, re-attempt number and interval
	 * @param finished
	 */
	public void setFinished(FinishProperties finished) {
		this.finished = finished;
	}

	/**
	 * Returns the Database connection properties
	 * Includes hostname, port, database name, username and password
	 * @return populated instance of DatabaseProperties
	 */
	public DatabaseProperties getDatabase() {
		return database;
	}

	/**
	 * Configure the database connection properties
	 * @param database model object to configure database connection
	 */
	public void setDatabase(DatabaseProperties database) {
		this.database = database;
	}
	
	/**
	 * Returns the retry properties used as a download robustness mechanism
	 * @return populated instance of RetryProperties
	 */
	public RetryProperties getRetry() {
		return retry;
	}

	/**
	 * Configures the retry properties for the downloads
	 * @param retry model object to use in RetryProperties config
	 */
	public void setRetry(RetryProperties retry) {
		this.retry = retry;
	}

	/**
	 * Returns the matlab properties including working directory, command structure
	 * and max wait interval for execution
	 * @return populated MatlabProperties object
	 */
	public MatlabProperties getMatlab() {
		return matlab;
	}
	
	/**
	 * Configure the matlab properties used in the AutomateGoes matlab() implementation
	 * @param matlab
	 */
	public void setMatlab(MatlabProperties matlab) {
		this.matlab = matlab;
	}

	/**
	 * Instantiates all of the necessary GoesProperties including Degribber, Downloads,
	 * EmailProperties, Variables, FinishProperties, DatabaseProperties, RetryProperties,
	 * and MatlabProperties
	 */
	public void createGoesProperties(){
		this.degribber = new Degribber();
		this.downloads = Collections.emptyList();
		this.email = new EmailProperties();
		this.variables = Collections.emptyList();
		this.finished = new FinishProperties();
		this.database = new DatabaseProperties();
		this.retry = new RetryProperties();
		this.matlab = new MatlabProperties();
	}


}
