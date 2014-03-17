package main.com.greendev.pragma.download;

import org.apache.log4j.Logger;

//import apache

/**
 * Determines the type of downloader required for the specified download.
 * @author josediaz
 *
 */
public class DownloaderFactory {
	//Create logger for this class
	private static Logger logger = Logger.getLogger(Downloader.class);
	
	/**
	 * Determines the appropriate downlaoder for given downlaod
	 * @param download - the download supplied to determine its downloader
	 * @return the appropriate downloader
	 */
	public static Downloader getDownloader(Download download){
		Class<?> clazz;
		Downloader downloader = null;
		String downloaderName = download.getDownloadType();
		
		try{
			//Get the Class object associated with the class with the given string name.
			clazz = Class.forName(download.getDownloadType());
			//Instantiate the class
			downloader = (Downloader) clazz.newInstance();
			//Set the download 
			downloader.setDownload(download);
		}catch(ClassNotFoundException e){
			logger.error("Error trying to instantiate the following class " +downloaderName, e);
		}catch(InstantiationException e){
			logger.error("Error trying to instantiate the following class " +downloaderName, e);
		}catch(IllegalAccessException e){
			logger.error("Error trying to instantiate the following class " +downloaderName, e);
		}
		
		RetryDownloader retryDown = new RetryDownloader(downloader);
		
		return retryDown;
	}
	
	
}
