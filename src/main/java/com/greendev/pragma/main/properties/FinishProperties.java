package main.java.com.greendev.pragma.main.properties;

/**
 * Properties used to determine that matlab
 * is finished, including retries and interval
 * @author miguelgd
 */
public class FinishProperties {
	
	/**
	 * Filename to wait for
	 */
	private String fileName;
	/**
	 * Number of times to poll the file
	 */
	private int tries;
	
	/**
	 * Seconds to wait between each poll
	 */
	private int seconds;
	
	public FinishProperties() {
		super();
	}
	/**
	 * Gets the finished file flag name.
	 * @return the finished file string name 
	 */
	public String getFileName() {
		return fileName;
	}
	/**
	 * Sets the finished file name flag name
	 * @param fileName The name of the file to set.
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	/**
	 * Gets the number of tries to run Matlab process
	 * @return The number of tries.
	 */
	public int getTries() {
		return tries;
	}
	/**
	 * Sets the number of tries to perform Matlab
	 * @param tries Number of tries to perform Matlab process
	 */
	public void setTries(int tries) {
		this.tries = tries;
	}
	/**
	 * Get the seconds to wait for Matlab execution
	 * @return Time to wait for Matlab execution
	 */
	public int getSeconds() {
		return seconds;
	}
	/**
	 * Sets the seconds to wait for MATLAB
	 * @param seconds
	 */
	public void setSeconds(int seconds) {
		this.seconds = seconds;
	}
	
}
