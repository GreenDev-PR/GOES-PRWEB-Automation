package main.java.com.greendev.pragma.main.properties;

public class RetryProperties {

	private long interval;
	private int attempts;
	private int lastDownloadAttemptTime;	

	public RetryProperties(){
		super();
	}
	
	public long getInterval() {
		return interval;
	}
	
	public void setInterval(int interval) {
		this.interval = interval;
	}
	
	public int getAttempts() {
		return attempts;
	}
	
	public void setAttempts(int attempts) {
		this.attempts = attempts;
	}
	
	public int getLastDownloadAttemptTime() {
		return lastDownloadAttemptTime;
	}
	
	public void setLastDownloadAttemptTime(int lastDownloadAttemptTime) {
		this.lastDownloadAttemptTime = lastDownloadAttemptTime;
	}

	@Override
	public String toString() {
		return "RetryProperties [interval=" + interval + ", attempts="
				+ attempts + ", lastDownloadAttemptTime="
				+ lastDownloadAttemptTime + "]";
	}
}
