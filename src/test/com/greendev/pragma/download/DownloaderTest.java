package test.com.greendev.pragma.download;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

import main.com.greendev.pragma.download.Download;
import main.com.greendev.pragma.download.Downloader;




import com.google.gson.Gson;

/**
 * Generic downloader test. Specific Downloaders should have their respective tests.
 * @author josediaz
 *
 */
public class DownloaderTest {
	
	/**
	 * The download to test
	 */
	protected Download download;
	/**
	 * The corresponding downloader
	 */
	protected Downloader downloader;
	
	/**
	 * Constructs an empty downloader test
	 */
	public DownloaderTest(){	
	}
	
	/**
	 * Reads a file in the /src/test/resources/download folder
	 * @param name - name of the file to read
	 * @return a Reader object of the read.
	 * @throws FileNotFoundException
	 */
	protected Reader getResourceFromDownloadsFolder(String name) throws FileNotFoundException{
		String resource = "src/test/resources/download/" + name;
		return new FileReader(new File(resource));
	}
	
	/**
	 * Extracts the download object from a JSON file
	 * @param name
	 * @return
	 * @throws FileNotFoundException
	 */
	protected Download getDownload(String name) throws FileNotFoundException{
		Gson gson = new Gson();
		Reader reader = getResourceFromDownloadsFolder(name);
		return gson.fromJson(reader, Download.class);
	}
	
	/**
	 * Determines the downloader to use for the download.
	 * @param clazz
	 * @return
	 */
	protected Downloader getDownloader( Class <? extends Downloader> clazz){
		Downloader downloader;
		try{
			downloader = clazz.newInstance();
			return downloader;
		}catch(InstantiationException e){
			e.printStackTrace();
		}catch(IllegalAccessException e){
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * 
	 * @param name
	 */
	protected void setDownload(String name){
		try{
			setDownload(getDownload(name));
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param download
	 */
	protected void setDownload(Download download){
		this.download = download;
		downloader.setDownload(download);
	}
	
	
	
}
