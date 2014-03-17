package main.com.greendev.pragma.download;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

/**
 * Decorator
 * @author josediaz
 *
 */
public class RetryDownloader implements Downloader {

	private static Logger logger = Logger.getLogger(RetryDownloader.class);
	//60*60*1000;  
	private static long WAIT_TIME;
	private static int ATTEMPTS;
	private Downloader downloader;
	
	
	public RetryDownloader(Downloader downloader, int attempts, long waitTime){	
		this.downloader = downloader;
		ATTEMPTS = attempts;
		WAIT_TIME = waitTime;
	}

	@Override
	public boolean downloadExists() {
		boolean exists = false;
		int counter = 0;

		logger.debug("Going to check if download exists");

		while(counter < ATTEMPTS){
			if(this.downloadExists() == false){

				try {
					Thread.sleep(WAIT_TIME);
				} catch (InterruptedException e) {
					logger.debug("Thread sleep was interrupted");
					e.printStackTrace();
				}
				counter++;	
			}else{
				return exists = this.downloadExists();
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
