package test.com.greendev.pragma.main;

import static org.junit.Assert.*;

import java.io.File;
import main.com.greendev.pragma.main.DirectoryManager;

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
	private static String rootDirectory = "src/test/com/greendev/pragma/main/temp";
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

}
