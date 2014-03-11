package main.com.greendev.pragma.download;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import main.com.greendev.pragma.utils.HttpUtils;

import org.apache.log4j.LogMF;
import org.apache.log4j.Logger;

/**
 * Downloader which changes  the last two digits of the URL, trying to match which file is on the server.
 * e.g. The file on the server is http://goes.edu/goes1233
 * The client of this class will supply http://goes.edu/goes12 as the URL.
 * Then the class will iterate from 0 to 60 trying to identify which is the correct URL.
 * @author josediaz
 *
 */
public class LastDigitsChangerDownloader implements Downloader {
	
	/**
	 * Stores information relevant to the download.
	 */
	private Download download;
	/**
	 * The URL to download data sets from.
	 */
	private URL url;
	/**
	 * Log instance field provides. 
	 */
	private static Logger logger = Logger.getLogger(LastDigitsChangerDownloader.class);
	
	/**
	 * Default constructor
	 */
	public LastDigitsChangerDownloader(){
		
	}
	
	/**
	 * Constructs a LastDigitChangerDownloader
	 * @param downlaod - the desired download to perform
	 */
	public LastDigitsChangerDownloader(Download downlaod){
		this.download = downlaod;
	}
	
	@Override
	public boolean exists() {
		URL url = getValidUrl();
		if(url != null){
			//URL is good
			this.url = url;
		}
		//URL is null
		return false;
	}
	
	@Override
	public File download() throws IOException {
		//Check if URL exists 
		if(this.url == null){
			if(!exists()){ 
				return null;
			}
		}
		File destination = new File(download.getSaveLocation()); 
		LogMF.info(logger, "Downloading from url {0}", url.toExternalForm());
		HttpUtils.downloadToFile(url, destination);
		this.url = null;
		return destination;
	}
	
	/**
	 * Collects the URL from the supplied download object. 
	 * This method verifies the last digits of the URL. 
	 * @return - the URL to download.
	 */
	private URL getValidUrl(){
		String url = download.getUrl(); //extract the URL to download
		LogMF.debug(logger, "Got the following url to validate {0}", url); //log 
		if(url == null){ 
			//URL is not provided in download
			return null;
		}
		URL workingUrl = null; //The final version URL to download.
		for(int i=20; i <= 60; i++){ 
			//Check which minute is iteratively 
			
			String tempUrl = url + String.format("%02d", i); //Append minute URL portion of the string.
			
			try{
				workingUrl = new URL(tempUrl);
			}catch(MalformedURLException e){
				// do nothing when malformed
			}
			LogMF.debug(logger, "Going to verify the following url {0}", workingUrl.toExternalForm());
			
			//verify that the current, not malformed, URL actually exists
			if(HttpUtils.urlExists(workingUrl)){ 
				return workingUrl;
			}
		}
		return null; 
	}
	
	@Override
	public void setDownload(Download download) {
		this.download = download;
	}

	@Override
	public Download getDownload() {
		return this.download;
	}

}
