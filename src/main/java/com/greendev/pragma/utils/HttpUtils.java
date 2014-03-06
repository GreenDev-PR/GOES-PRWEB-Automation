package main.java.com.greendev.pragma.utils;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.LogMF;
import org.apache.log4j.Logger;

/**
 * Provides http utilitiy methods such as verifying the existance of an URL
 * or downloading data from a URL.
 * @author josediaz
 *
 */
public class HttpUtils {
	
	private static Logger logger = Logger.getLogger(HttpUtils.class);
	
	/**
	 * Determine if url exists.
	 * @param url - the url to test 
	 * @return true if the url exists
	 */
	public static boolean urlExists(URL url){
		if(url == null){
			return false; //if url is not supplied return false
		}
		
		HttpURLConnection conn = null;
		int code = -1; //response code
		try{
			//Attempt to connect to the url 
			conn = (HttpURLConnection) url.openConnection();
			//request headers
			conn.setRequestMethod("HEAD");
			//store response code
			code = conn.getResponseCode();
			
		}catch(IOException e){
			//write to log
			LogMF.debug(logger, "Exception trying to verify the url {0}", e);
			
		}finally{
			if(conn != null){
				conn.disconnect(); //close connection
			}
		}
		return code == HttpURLConnection.HTTP_OK;
		
	}
	
	/**
	 * Perform downlaod of data located at specified URL
	 * @param url - location of data to download
	 * @param file - file to download the data to.
	 * @throws IOException - thrown if I/O Exception occurs
	 */
	public static void downloadToFile(URL url, File file) throws IOException{
		LogMF.debug(logger, "Going to download from {0}", url.toExternalForm());
		FileUtils.copyURLToFile(url, file);
		logger.debug("Downloaded");
	}
}
