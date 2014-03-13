package main.com.greendev.pragma.main.properties;

import java.util.Collections;
import java.util.List;

import main.com.greendev.pragma.degrib.Degribber;
import main.com.greendev.pragma.download.Download;

/**
 * Collection of properties necessary for program execution
 * @author miguelgd
 *
 */

public class GoesProperties {

	private Degribber degribber;
	private List<Download> downloads;
	private FtpProperties ftp;
	private String goesDir;
	private String matlabCmd;
	private String matlabWorking;
	private long waitInterval;
	private int timesToWait;
	private String logLayout;
	private String finishedFileName;
	private List<String> variables;
	private EmailProperties email;
	
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



	public FtpProperties getFtp() {
		return ftp;
	}



	public void setFtp(FtpProperties ftp) {
		this.ftp = ftp;
	}



	public String getMatlabCmd() {
		return matlabCmd;
	}



	public void setMatlabCmd(String matlabCmd) {
		this.matlabCmd = matlabCmd;
	}



	public String getMatlabWorking() {
		return matlabWorking;
	}



	public void setMatlabWorking(String matlabWorking) {
		this.matlabWorking = matlabWorking;
	}



	public long getWaitInterval() {
		return waitInterval;
	}



	public void setWaitInterval(long waitInterval) {
		this.waitInterval = waitInterval;
	}



	public int getTimesToWait() {
		return timesToWait;
	}



	public void setTimesToWait(int timesToWait) {
		this.timesToWait = timesToWait;
	}



	public String getLogLayout() {
		return logLayout;
	}



	public void setLogLayout(String logLayout) {
		this.logLayout = logLayout;
	}



	public String getFinishedFileName() {
		return finishedFileName;
	}



	public void setFinishedFileName(String finishedFileName) {
		this.finishedFileName = finishedFileName;
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
	
	public void createGoesProperties(){
		this.degribber = new Degribber();
		this.downloads = Collections.emptyList();
		this.email = new EmailProperties();
		this.finishedFileName = "";
		this.ftp = new FtpProperties();
		this.variables = Collections.emptyList();
	}
	
	

}
