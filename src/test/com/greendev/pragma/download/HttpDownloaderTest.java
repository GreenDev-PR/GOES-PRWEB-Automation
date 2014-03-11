package test.com.greendev.pragma.download;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import main.com.greendev.pragma.download.HttpDownloader;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit test for HTTP Downloader Test Class 
 * @author josediaz
 *
 */
public class HttpDownloaderTest extends DownloaderTest {

	@Before
	public void setUp() throws Exception {
		
		downloader = getDownloader(HttpDownloader.class);
	}

	@After
	public void tearDown() throws Exception {
		download = null;
		downloader = null;
	}

	@Test
	public void testExists() throws IOException {
		setDownload("download.json");
		assertTrue(downloader.exists());
	}
	
	@Test
	public void testNotExits() throws FileNotFoundException{
		setDownload("downloadNotExists.json");
		assertFalse(downloader.exists());
	}
	
	@Test
	public void testDownload(){
		setDownload("download.json");
		try{
			downloader.download();
		} catch (IOException e1){
			e1.printStackTrace();
		}
		File expected = new File("src/test/resources/expecteddownload.txt");
		File downloaded = new File("src/test/resources/outdownload.txt");
		
		boolean result = false;
		try{
			result = FileUtils.contentEquals(expected, downloaded);
		} catch (IOException e){
			e.printStackTrace();
		}
	
		assertTrue(result);
		
	}

	

}
