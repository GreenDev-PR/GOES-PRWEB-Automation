package main.java.com.greendev.pragma.download;

import java.io.File;
import java.io.IOException;

/**
 * This class defines the interface Downloader object. Every downloader
 * should contain a mechanism for download existance verification, a 
 * mechanism to actually perform a download along with pertinent setters/
 * getters.
 * @author josediaz
 *
 */
public interface Downloader {
	
	/**
	 * Verify if the download exists
	 * @return true if the download exists
	 */
	public boolean downloadExists();
	/**
	 * Perform a download
	 * @return The downloaded file
	 * @throws IOException
	 */
	public File download() throws IOException;
	
	/**
	 * Sets the download to perform
 	 * @param download The download to perform 
	 */
	public void setDownload(Download download);
	
	/**
	 * Get the download
	 * @return The download to be performed
	 */
	public Download getDownload();
}
