package main.com.greendev.pragma.download;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import main.com.greendev.pragma.utils.HttpUtils;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.LogMF;
import org.apache.log4j.Logger;

/**
 * Http downloader 
 * @author josediaz
 *
 */
public class HttpDownloader implements Downloader {
	
	/**
	 * Download to perform
	 */
	private Download download;
	
	/**
	 * Log 
	 */
	private static Logger logger = Logger.getLogger(HttpDownloader.class);
	
	/**
	 * Default Constructor
	 */
	public HttpDownloader(){	
	}
	
	/**
	 * Constructs an HTTP downloader based on specified download
	 * @param download - HTTP download to perform
	 */
	public HttpDownloader(Download download){
		this.download = download;
	}
	
	//Check if the URL exits
	@Override
	public boolean downloadExists() {
		URL url = null;
		boolean exists = false; //By default the URL is presummed to not exist
		try{
			url = new URL(download.getUrl());
			LogMF.debug(logger,"Going to verify if the following url exists {0}",url);
			//verify if URL exists
			exists = HttpUtils.urlExists(url);
		} catch (MalformedURLException e){
			//URL does not exist
			LogMF.debug(logger,"Malformed url {0}",url);
		}
		return exists;
	}
	
	@Override
	public File download() throws IOException {
		File destination = new File(download.getSaveLocation());
		URL url = new URL(download.getUrl());
		LogMF.debug(
				logger,
				"Going to download the following url {0} saving it in {1}",
				url, destination.getAbsolutePath());
		//download the URL to specified file path
		FileUtils.copyURLToFile(url, destination);
		return destination;
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
