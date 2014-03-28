package test.com.greendev.pragma.main;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;

import main.com.greendev.pragma.main.AutomateGoes;
import main.com.greendev.pragma.main.properties.GoesProperties;
import main.com.greendev.pragma.main.properties.GoesPropertiesToJson;
import main.com.greendev.pragma.utils.GoesUtils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.LogMF;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
/**
 * Tests Automate Goes class
 * @author josediaz
 */
public class AutomateGoesTest {
	private static final Logger logger = Logger.getLogger(AutomateGoesTest.class);
	private static final String PROPERTIES_PATH = "src/test/com/greendev/pragma/main/properties/goesProperties.json";
	private static final String PROPERTIES_PATH_TEST_RESULT = "src/test/com/greendev/pragma/main/properties/" +
																					"goesProperties2.json";
	private static final String WAIT_FOR_FILE_DIR = "src/test/resources/";
	private static final String WAIT_FOR_FILE_FILENAME = "waitTest.txt";
	private static int WAIT_FOR_FILE_SECONDS;
	private static AutomateGoes goes;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		goes = new AutomateGoes(PROPERTIES_PATH, new DateTime());
		WAIT_FOR_FILE_SECONDS = goes.getGoesProperties().getFinished().getSeconds();
		LogMF.debug(logger, "Wait for file time value: ", WAIT_FOR_FILE_SECONDS);
		
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		//Delete goesProperties2.json
		FileUtils.deleteQuietly(new File(PROPERTIES_PATH_TEST_RESULT));
		//Delete watTest.txt
		FileUtils.deleteQuietly(new File(WAIT_FOR_FILE_DIR+WAIT_FOR_FILE_FILENAME));
	}
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void loadGoesPropertiesTest() throws SQLException, IOException {
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.setPrettyPrinting().serializeNulls().create(); 
		
		File file2 = new File(PROPERTIES_PATH_TEST_RESULT);
		FileWriter writer = new FileWriter(file2);
		gson.toJson(goes.getGoesProperties(),writer);
		IOUtils.closeQuietly(writer);
		
		File file1 = new File(PROPERTIES_PATH);
		FileUtils.copyFile(file1, file2);
		assertTrue(FileUtils.contentEquals(file1, file2));
		//assertTrue(true);
	}
	
	@Test
	public void waitForFileTest() throws InterruptedException{
		boolean result = false;
		
		String directory = WAIT_FOR_FILE_DIR;
		String fileName = WAIT_FOR_FILE_FILENAME;
		LogMF.debug(logger, "Running AutomateGoes - waitForFile({0},{1}). \n" +
				"Properties - waitTime: {2}ms, tries: {3}",directory,fileName,GoesUtils.convertSecondsToMillis(WAIT_FOR_FILE_SECONDS),
				goes.getGoesProperties().getFinished().getTries());
		
		long taskTimer = 5000;
		Timer timer = new Timer();
		//Anonymous TimerTask
		timer.schedule(new TimerTask(){

			@Override
			public void run() {
				File file = new File(WAIT_FOR_FILE_DIR + WAIT_FOR_FILE_FILENAME);
				try {
					FileUtils.touch(file);
					LogMF.debug(logger, "TASK - Writting file...",null);
				} catch (IOException e1) {
					LogMF.debug(logger, "ERROR on TimerTask - Writting file.",null);
					e1.printStackTrace();
				}
			}
			
		}, taskTimer);	
		
		//This should run for at least 20seconds.
		result = goes.waitForFile(directory, fileName);
		//Timer should activate after 5seconds.
		assertTrue(result);
	}
	
	//@Test
	public void emailLogTest(){
		
	}
	
	public void downloadTest(){
		
	}

}
