package test.com.greendev.pragma.download;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import main.com.greendev.pragma.download.HttpDownloader;

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.Before;

import org.junit.Test;

/**
 * JUnit test for HTTP Downloader Test Class 
 * @author josediaz
 *
 */
public class HttpDownloaderTest extends DownloaderTest {
	
	private static File expected;
	private static File downloaded;
	
	@Before
	public void setUp() throws Exception {
		downloader = getDownloader(HttpDownloader.class);
		System.out.println("Executing @Before Annotation");
	}

	@AfterClass
	public static void tearDown() throws Exception {
		System.out.println("Executing @AfterClass Annotation");
		FileUtils.deleteQuietly(downloaded);
		
	}
	
	@Test
	public void testExists() throws IOException {
		setDownload("download.json");
		System.out.println("Executing @TestExists Annotation");
		assertTrue(downloader.exists());
	}
	
	@Test
	public void testNotExits() throws FileNotFoundException{
		setDownload("downloadNotExists.json");
		System.out.println("Executing @TestNotExists Annotation");
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
		expected = new File("src/test/resources/expecteddownload.txt");
		downloaded = new File("src/test/resources/outdownload.txt");
		
		boolean result = false;
		try{
			result = FileUtils.contentEquals(expected, downloaded);
			System.out.println("Executing @TestDownload Annotation");
		} catch (IOException e){
			e.printStackTrace();
		}
	
		assertTrue(result);
		
	}
	
	

	

}
