//TODO: Verify <?> statement
package main.java.com.greendev.pragma.download;

import org.apache.log4j.Logger;

/**
 * Determines the type of downloader required for the specified download. 
 * It instantiates the correct downloader inferring the class type.
 * @author josediaz
 *
 */
public class DownloaderFactory {
	//Create logger for this class
	private static Logger logger = Logger.getLogger(Downloader.class);
	
	/**
	 * Determines the appropriate downloader for given download
	 * @param download The download supplied to determine its downloader
	 * @return The appropriate downloader
	 */
	public static Downloader getDownloader(Download download){
		Class<?> clazz;
		Downloader downloader = null;
		String downloaderName = download.getDownloadClass();
		
		try{
			//Get the Class object associated with the class with the given string name.
			clazz = Class.forName(download.getDownloadClass());
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
		
		return downloader;
	}
	
	
}
