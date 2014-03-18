package test.com.greendev.pragma.main;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLException;

import main.com.greendev.pragma.main.AutomateGoes;
import main.com.greendev.pragma.main.properties.GoesProperties;
import main.com.greendev.pragma.main.properties.GoesPropertiesToJson;

import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.gson.Gson;
/**
 * Tests Automate Goes class
 * @author josediaz
 */
public class AutomateGoesTest {

	AutomateGoes goes;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}
	
	@Before
	public void setUp() throws Exception {
		DateTime date = new DateTime();
		goes = new AutomateGoes("src/test/com/greendev/pragma/main/properties/goesProperties.json",date);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void loadGoesPropertiesTest() throws FileNotFoundException, SQLException {
		goes.insertToDb();
		fail("Not yet implemented");
	}

}
