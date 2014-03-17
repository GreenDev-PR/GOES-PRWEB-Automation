package main.com.greendev.pragma.download;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;

/**
 * Decorator
 * @author josediaz
 *
 */
public class RetryDownloader implements Downloader {

	private static Logger logger = Logger.getLogger(RetryDownloader.class);
	private static final int ATTEMPTS = 3;
	private Downloader dl;
	
	public RetryDownloader(Downloader dl){	
		this.dl = dl;
	}
	
	@Override
	public boolean downloadExists() {
		return dl.downloadExists();
	}

	@Override
	public File download() throws IOException {
		//attemp to download
		
		Download result;
		
		int counter = 0;
		
		while(counter < ATTEMPTS){
			
			try{
				return dl.download();
			}catch(IOException e){
				//robustness
				counter++;
			}

		}
		
		//result.getUrl();
		//File yesterday = new File(path, 
		return null;
	}

	@Override
	public void setDownload(Download download) {
		dl.setDownload(download);
	}

	@Override
	public Download getDownload() {
		return dl.getDownload();
	}

}
