package main.com.greendev.pragma.download;

import java.io.File;
import java.io.IOException;
import org.apache.log4j.LogMF;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;

/**
 * RetryDownloader provides re-attempt feature for apparent non existent downloads.
 * The re-attempt mechanism is blocking. Follows Decorator Design Pattern.
 * @author josediaz
 *
 */
public class RetryDownloader implements Downloader {

	private static Logger logger = Logger.getLogger(RetryDownloader.class);  
	private long INTERVAL;
	private int ATTEMPTS;
	private int LAST_DOWNLOAD_ATTEMPT_TIME;
	private Downloader downloader;
	
	/**
	 * Constructor
	 * @param downloader The Downloader to utilize for downloads.
	 * @param attempts Number of attempts to make.
	 * @param interval The time to wait until next re-try attempt.
	 * @param endOfDay Time at which the algorithm needs to be executed.
	 */
	public RetryDownloader(Downloader downloader, int attempts, long interval, int endOfDay){	
		this.downloader = downloader;
		this.ATTEMPTS = attempts;
		this.INTERVAL = interval;
		this.LAST_DOWNLOAD_ATTEMPT_TIME = endOfDay;
	}

	@Override
	public boolean downloadExists() {
		boolean exists = false;
		int counter = 0;

		LogMF.info(logger,"Going to check if download exists for the {0} time", counter);
		
		while(counter < ATTEMPTS && new DateTime().getSecondOfDay() < LAST_DOWNLOAD_ATTEMPT_TIME){
			if(!this.downloader.downloadExists()){
				counter++;	
				try {
					LogMF.info(logger,"Thread going to sleep, counter value {0}",counter);
					
					Thread.sleep(INTERVAL);
					
					LogMF.info(logger,"Thread woke up, counter value: {0}",counter);
				} catch (InterruptedException e) {
					logger.error("Retry Downlaoder thread sleep was interrupted ",e);
					
				}
				
			}else{
				LogMF.info(logger, "Found download located at #{0} try",counter);
			
				exists = true;
				return exists;
			}
		}

		return exists;
	}
	
	@Override
	public File download() throws IOException {
		//attempt to download
		return downloader.download();
	}

	@Override
	public void setDownload(Download download) {
		downloader.setDownload(download);
	}

	@Override
	public Download getDownload() {
		return downloader.getDownload();
	}

}
