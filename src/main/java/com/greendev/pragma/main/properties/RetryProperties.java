package main.java.com.greendev.pragma.main.properties;

/**
 * This class represents a retry properties object. It contains 
 * the specifications used by a retry Downloader.
 * @author miguelgd
 */
public class RetryProperties {

	private long interval;
	private int attempts;
	private int lastDownloadAttemptTime;	

	/**
	 * Constructs a RetryProperties object
	 */
	public RetryProperties(){
		super();
	}
	/**
	 * Gets the retry attempt time interval in millis.
	 * @return The time in millis to re-attempt a download.
	 */
	public long getInterval() {
		return interval;
	}
	/**
	 * Sets the re-attempt time interval  
	 * @param interval in millis
	 */
	public void setInterval(int interval) {
		this.interval = interval;
	}
	/**
	 * Gets the number of attempts to be made.
	 * @return The number of attempts to be made
	 */
	public int getAttempts() {
		return attempts;
	}
	/**
	 * Sets the number of attempts to be made.
	 * @param attempts The number of attempts to be made
	 */
	public void setAttempts(int attempts) {
		this.attempts = attempts;
	}
	/**
	 * Gets the last download attempt time. 
	 * @return The last download attempt time.
	 */
	public int getLastDownloadAttemptTime() {
		return lastDownloadAttemptTime;
	}
	/**
	 * Sets the Last download attempt time.
	 * @param lastDownloadAttemptTime The last download attempt.
	 */
	public void setLastDownloadAttemptTime(int lastDownloadAttemptTime) {
		this.lastDownloadAttemptTime = lastDownloadAttemptTime;
	}
	/**
	 * The string representation of a retry properties object 
	 */
	@Override
	public String toString() {
		return "RetryProperties [interval=" + interval + ", attempts="
				+ attempts + ", lastDownloadAttemptTime="
				+ lastDownloadAttemptTime + "]";
	}
}
