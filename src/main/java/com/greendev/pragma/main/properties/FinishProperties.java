package main.java.com.greendev.pragma.main.properties;

/**
 * Properties used to determine that matlab
 * is finished, including retries and interval
 * @author miguelgd
 *
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

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public int getTries() {
		return tries;
	}

	public void setTries(int tries) {
		this.tries = tries;
	}

	public int getSeconds() {
		return seconds;
	}

	public void setSeconds(int seconds) {
		this.seconds = seconds;
	}
	
}
