package main.java.com.greendev.pragma.download;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import main.java.com.greendev.pragma.utils.HttpUtils;

import org.apache.log4j.LogMF;
import org.apache.log4j.Logger;

/**
 * Downloader which changes  the last two digits of the url, trying to match which file is on the server.
 * e.g. The file on the server is http://goes.edu/goes1233
 * The client of this class will supply http://goes.edu/goes12 as the url.
 * Then the class will iterate from 0 to 60 trying to identify which is the correct url.
 * @author josediaz
 *
 */
public class LastDigitsChangerDownloader implements Downloader {

	private Download download;
	private URL url;
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
			//url is good
			this.url = url;
		}
		//url is null
		return false;
	}
	
	@Override
	public File download() throws IOException {
		//check if url exists 
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
	
	private URL getValidUrl(){
		String url = download.getUrl();
		LogMF.debug(logger, "Got the following url to validate {0}", url); 
		if(url == null){
			return null;
		}
		URL workingUrl = null;
		for(int i=20; i <= 60; i++){
			String tempUrl = url + String.format("%02d", i);
		
			try{
				workingUrl = new URL(tempUrl);
			}catch(MalformedURLException e){
				// do nothing when malformed
			}
			LogMF.debug(logger, "Going to verify the following url {0}", workingUrl.toExternalForm());
			
			if(HttpUtils.urlExists(workingUrl)){
				return workingUrl;
			}
		}
		return null; 
	}

	@Override
	public void setDownload(Download download) {
		// TODO Auto-generated method stub

	}

	@Override
	public Download getDownload() {
		// TODO Auto-generated method stub
		return null;
	}

}
