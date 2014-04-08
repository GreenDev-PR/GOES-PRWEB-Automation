package test.java.com.greendev.pragma.download;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;


import main.java.com.greendev.pragma.download.LastDigitsChangerDownloader;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class LastDigitsChangerDownloaderTest extends DownloaderTest {

	private static File downloaded;
	
	@Before
	public void setUp() throws Exception {
		System.out.println("@Before annotation execution");
		this.downloader = this.getDownloader(LastDigitsChangerDownloader.class);
	}

	@AfterClass
	public static void tearDown() throws Exception{
		//FileUtils.deleteQuietly(downloaded);
	}
	
	@Test
	public void testIfExists(){
		System.out.println("@TestIfExists annotation execution");
		setDownload("lastdigitschangerdownload.json");
	}
	
	@Test
	public void testNotExists(){
		System.out.println("@TestNotExists annotation execution");
		setDownload("downloadNotExists.json");
		assertFalse(downloader.downloadExists());
	}
	@Test
	public void testDownload() throws IOException{
		System.out.println("@TestDownload annotation execution");
		setDownload("lastdigitschangerdownload.json");
		downloader.download();
		downloaded = new File(download.getSaveLocation());
		System.out.println(downloaded.toString());
		assertTrue(downloaded.exists());
	}
	
	
	
	
}
