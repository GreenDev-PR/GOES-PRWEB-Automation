package main.com.greendev.pragma.main.properties;

public class RetryProperties {

	private int interval;
	private long attempts;
	private int lastDownloadAttemptTime;	

	public RetryProperties(){
		super();
	}
	
	public int getInterval() {
		return interval;
	}
	
	public void setInterval(int interval) {
		this.interval = interval;
	}
	
	public long getAttempts() {
		return attempts;
	}
	
	public void setAttempts(long attempts) {
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
