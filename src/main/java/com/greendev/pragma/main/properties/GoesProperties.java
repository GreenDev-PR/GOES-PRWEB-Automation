package main.java.com.greendev.pragma.main.properties;

import java.util.Collections;
import java.util.List;

import main.java.com.greendev.pragma.degrib.Degribber;
import main.java.com.greendev.pragma.download.Download;

/**
 * Collection of properties necessary for program execution
 * @author miguelgd
 *
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


	public GoesProperties(){

	}

	public Degribber getDegribber() {
		return degribber;
	}



	public void setDegribber(Degribber degribber) {
		this.degribber = degribber;
	}



	public List<Download> getDownloads() {
		return downloads;
	}



	public void setDownloads(List<Download> downloads) {
		this.downloads = downloads;
	}



	public String getLogLayout() {
		return logLayout;
	}



	public void setLogLayout(String logLayout) {
		this.logLayout = logLayout;
	}


	public List<String> getVariable() {
		return variables;
	}



	public void setVariable(List<String> variables) {
		this.variables = variables;
	}



	public EmailProperties getEmail() {
		return email;
	}



	public void setEmail(EmailProperties email) {
		this.email = email;
	}



	public void setGoesDir(String goesDir) {
		this.goesDir = goesDir;
	}



	public String getGoesDir() {
		return goesDir;
	}

	public FinishProperties getFinished() {
		return finished;
	}


	public void setFinished(FinishProperties finished) {
		this.finished = finished;
	}

	public DatabaseProperties getDatabase() {
		return database;
	}

	public void setDatabase(DatabaseProperties database) {
		this.database = database;
	}
	

	public RetryProperties getRetry() {
		return retry;
	}

	public void setRetry(RetryProperties retry) {
		this.retry = retry;
	}

	public MatlabProperties getMatlab() {
		return matlab;
	}

	public void setMatlab(MatlabProperties matlab) {
		this.matlab = matlab;
	}

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
