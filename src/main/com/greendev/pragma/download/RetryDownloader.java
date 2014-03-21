package main.com.greendev.pragma.download;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.LogMF;
import org.apache.log4j.Logger;

/**
 * Decorator
 * @author josediaz
 *
 */
public class RetryDownloader implements Downloader {

	private static Logger logger = Logger.getLogger(RetryDownloader.class);
	//60*60*1000;  
	private long WAIT_TIME;
	private int ATTEMPTS;
	private Downloader downloader;
	
	/**
	 * Constructor
	 * @param downloader
	 * @param attempts
	 * @param waitTime
	 */
	public RetryDownloader(Downloader downloader, int attempts, long waitTime){	
		this.downloader = downloader;
		this.ATTEMPTS = attempts;
		this.WAIT_TIME = waitTime;
	}

	@Override
	public boolean downloadExists() {
		boolean exists = false;
		int counter = 0;

		LogMF.info(logger,"Going to check if download exists for the {0} time", counter);

		while(counter < ATTEMPTS){
			if(!this.downloader.downloadExists()){
				counter++;	
				try {
					LogMF.info(logger,"Thread going to sleep, counter value {0}",counter);
					
					Thread.sleep(WAIT_TIME);
					
					LogMF.info(logger,"Thread woke up, counter value: {0}",counter);
				} catch (InterruptedException e) {
					LogMF.error(logger,"Thread sleep was interrupted",null);
					e.printStackTrace();
				}
				
			}else{
				LogMF.info(logger, "Found download located at #{0} try",counter);
				//return exists = this.downloadExists();
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
