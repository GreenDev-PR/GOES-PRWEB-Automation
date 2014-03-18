package test.com.greendev.pragma.main;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;

import main.com.greendev.pragma.main.AutomateGoes;
import main.com.greendev.pragma.main.properties.GoesProperties;
import main.com.greendev.pragma.main.properties.GoesPropertiesToJson;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
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

	AutomateGoes goes;
	public static final String PROPERTIES_PATH = "src/test/com/greendev/pragma/main/properties/goesProperties.json";
	private static final String PROPERTIES_PATH_TEST_RESULT = "src/test/com/greendev/pragma/main/properties/" +
																						"goesProperties2.json";
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}
	
	@Before
	public void setUp() throws Exception {
		DateTime date = new DateTime();
		goes = new AutomateGoes(PROPERTIES_PATH,date);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void loadGoesPropertiesTest() throws SQLException, IOException {
		System.out.println("Properties: "+goes.getGoesProperties().getGoesDir());
		//goes.getGoesProperties().createGoesProperties();
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.setPrettyPrinting().serializeNulls().create(); 
		
		File file2 = new File(PROPERTIES_PATH_TEST_RESULT);
		FileWriter writer = new FileWriter(file2);
		gson.toJson(goes.getGoesProperties(),writer);
		IOUtils.closeQuietly(writer);
		
		File file1 = new File(PROPERTIES_PATH);
		assertTrue(FileUtils.contentEquals(file1, file2));
	}

}
