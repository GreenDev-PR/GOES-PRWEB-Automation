package test.java.com.greendev.pragma.download;

import static org.junit.Assert.*;

import main.java.com.greendev.pragma.download.Download;
import main.java.com.greendev.pragma.download.Downloader;
import main.java.com.greendev.pragma.download.DownloaderFactory;
import main.java.com.greendev.pragma.download.RetryDownloader;
import main.java.com.greendev.pragma.main.AutomateGoes;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class RetryDownloaderTest {
	
	private static final Logger logger = Logger.getLogger(RetryDownloaderTest.class);
	private static final int ATTEMPTS = 3;
	private static final long INTERVAL = 10*1000; // 10 sec
	private static final int END_OF_DAY = 77;
	private static Downloader retryDownloader;
	
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		//Creates non-existent download
		Download download = new Download();
		download.setUrl("http://10.21.35.50:8080");
		download.setSaveLocation("src/");
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

	@Test
	public void test() {
		
		retryDownloader.downloadExists();
		
		//fail("Not yet implemented");
	}

}
