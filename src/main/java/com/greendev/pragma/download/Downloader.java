package main.java.com.greendev.pragma.download;

import java.io.File;
import java.io.IOException;

/**
 * This class can be implemented to download from any source.
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
	 * @return the file download 
	 * @throws IOException
	 */
	public File download() throws IOException;
	
	/**
	 * Sets the download to perform
 	 * @param download - the download to perform 
	 */
	public void setDownload(Download download);
	
	/**
	 * Get the download
	 * @return the download to be performed
	 */
	public Download getDownload();
}
