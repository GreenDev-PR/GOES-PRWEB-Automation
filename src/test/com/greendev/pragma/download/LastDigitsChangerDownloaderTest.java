package test.com.greendev.pragma.download;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;


import main.com.greendev.pragma.download.LastDigitsChangerDownloader;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class LastDigitsChangerDownloaderTest extends DownloaderTest {

	@Before
	public void setUp() throws Exception {
		this.downloader = this.getDownloader(LastDigitsChangerDownloader.class);
	}

	@After
	public void tearDown() throws Exception {
		
	}
	
	@Test
	public void testIfExists(){
		setDownload("lastdigitschangerdownload.json");
	}
	
	@Test
	public void testNotExists(){
		setDownload("downloadNotExists.json");
		assertFalse(downloader.exists());
	}
	@Test
	public void testDownload() throws IOException{
		setDownload("lastdigitschangerdownload.json");
		downloader.download();
		File downloaded = new File(download.getSaveLocation());
		assertTrue(downloaded.exists());
	}

}
