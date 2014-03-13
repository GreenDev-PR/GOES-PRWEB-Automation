package test.com.greendev.pragma.database;

import static org.junit.Assert.*;

import java.io.File;

import main.com.greendev.pragma.database.DBManager;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Verifies that the CSVdata was inserted correctly to the database
 * @author josediaz
 *
 */
public class DBManagerTest {
	
	private static DBManager manager;
	private static String varName;
	private static File csvFile;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		manager = new DBManager();
		varName = "variable";
		csvFile = new File("src/test/com/greendev/pragma/database/variable20140309.csv");
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		manager = null;
		varName = null;
		csvFile = null;
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		assertTrue(manager.storeInDB(varName, csvFile));
		
	}
	

}
