package test.java.com.greendev.pragma.download;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import main.java.com.greendev.pragma.download.Download;
import main.java.com.greendev.pragma.download.Downloader;
import main.java.com.greendev.pragma.download.DownloaderFactory;
import main.java.com.greendev.pragma.download.RetryDownloader;
import main.java.com.greendev.pragma.main.AutomateGoes;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
/**
 * This test requires the use of a Node File Server called dummyServer.js 
 * included in the working directory. The server serves static files located
 * src/test/java/com/greendev/pragma/main/resource/ directory
 * @author josediaz
 */
public class RetryDownloaderTest {
	
	private static final Logger logger = Logger.getLogger(RetryDownloaderTest.class);
	private static final int ATTEMPTS = 3;
	private static final long INTERVAL = 10*1000; // 10 sec
	private static final int END_OF_DAY = 792000;
	private static Downloader retryDownloader;
	
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		//Creates non-existent download
		Download download = new Download();
		download.setUrl("http://localhost:3000/RetryDownloaderStaticFile.txt");
		download.setSaveLocation("src/test/java/com/greendev/pragma/main/resources/RetryDownloaderStaticFile.txt");
		download.setDownloadClass("main.java.com.greendev.pragma.download.HttpDownloader");
		download.setDateOffset(0);
		
		Downloader downloader = DownloaderFactory.getDownloader(download);
		retryDownloader = new RetryDownloader(downloader,ATTEMPTS, INTERVAL, END_OF_DAY);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	/**
	 * This method tests whether or not the retryDownloader can find a download after
	 * it was not available on a previous attempt 
	 * @throws IOException 
	 */
	@Test
	public void test() throws IOException {
		boolean success = false; 
		success = retryDownloader.downloadExists();
		System.out.println("Download exists: "+success);
		
		System.out.println("found the download");
		System.out.println("downloading the specified download");
		retryDownloader.download();	
		
		File fileToDownload = new File("src/test/resources/download/RetryDownloaderStaticFile.txt");
		File expectedDownload = new File("src/test/java/com/greendev/pragma/main/resources/RetryDownloaderStaticFile.txt");
		
		assertTrue(FileUtils.contentEquals(fileToDownload,expectedDownload));
	}

}
