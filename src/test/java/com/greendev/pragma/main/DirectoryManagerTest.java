package test.java.com.greendev.pragma.main;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import main.java.com.greendev.pragma.main.DirectoryManager;

import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

/**
 * Tester for DirectoryManager class
 * Tests the correct creation of root (e.g. /YYYY/MM/DD/), INPUT, OUTPUT
 * and LOGS directories
 * @author miguelgd
 */

public class DirectoryManagerTest {

	private static DirectoryManager manager;
	private static String rootDirectory = "src/test/java/com/greendev/pragma/main/temp";
	private DateTime date;


	@AfterClass
	public static void tearDownAfterClass() throws Exception {	
		manager = null;
		FileUtils.deleteQuietly(new File(rootDirectory));
	}

	@Before
	public void setUp() throws Exception {
		date = new DateTime();
		manager = new DirectoryManager(rootDirectory);
	}

	@Test
	public void testRootDirectory() {
		File file = manager.getDirectory(date);
		assertTrue(file.exists());
	}
	
	@Test
	public void testLogDirectory() {
		File file = manager.getLogDirectory();
		assertTrue(file.exists());
	}
	
	@Test
	public void testInputDirectory() {
		File file = manager.getInputDirectory(date);
		assertTrue(file.exists());
	}
	
	@Test
	public void testOutputDirectory() {
		File file = manager.getOutputDirectory(date);
		assertTrue(file.exists());
	}
	
	
	@Test
	public void testArchiving() {
		FileUtils.deleteQuietly(new File(rootDirectory));
		manager = new DirectoryManager(rootDirectory);
		try {
			assertFalse(manager.archiveDataForCurrentDate(date));
		} catch (IOException e) {
			System.out.println("Error: Could not create archive");
			e.printStackTrace();
		}
		manager.createDirectoriesForDateTime(date);
		try {
			boolean archived = manager.archiveDataForCurrentDate(date);
			assertTrue(archived);
		} catch (IOException e) {
			System.out.println("Error: Could not create archive");
			e.printStackTrace();
		}
	}
	
	@Test 
	public void testCleanup(){
		//Prepare root directory and create input and output dirs
		FileUtils.deleteQuietly(new File(rootDirectory));
		manager = new DirectoryManager(rootDirectory);
		manager.createDirectoriesForDateTime(date);	
		File input = manager.getInputDirectory(date);
		File output = manager.getOutputDirectory(date);
		//Create temp files inside both input and output dirs
		try {
			FileUtils.touch(new File(input,"temp"));
			FileUtils.touch(new File(output, "temp"));
		} catch (IOException e1) {
			System.out.println("Error creating temp file for cleanup test");
		}
		//Assert that the input and output dirs are not empty before running cleanup
		assertTrue(!(input.list().length == 0));	
		assertTrue(!(output.list().length == 0));	
		//Perform cleanup of input and output dirs
		try {
			manager.cleanUp(date);
		} catch (IOException e) {
			System.out.println("Error cleaning up input and outpur directories");
		}
		//Assert that the input and output dirs are empty after cleanup
		assertTrue(input.list().length == 0);	
		assertTrue(output.list().length == 0);
	}

}
