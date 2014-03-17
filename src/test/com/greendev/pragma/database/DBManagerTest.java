package test.com.greendev.pragma.database;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import main.com.greendev.pragma.database.CsvToModel;
import main.com.greendev.pragma.database.DBManager;
import main.com.greendev.pragma.database.GoesImageFinder;
import main.com.greendev.pragma.database.databaseModel.GoesVariable;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.log4j.LogMF;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * This class provides testing for two 
 * @author josediaz
 *
 */
public class DBManagerTest {

	private static final String DELETE_GOES_DATA_RECORDS_QUERY = "DELETE FROM GoesData"; 
	private static final String DELETE_GOES_MAPS_RECORDS_QUERY = "DELETE FROM GoesMaps"; 
	
	private static final String USERNAME = "postgres";
	private static final String PASSWORD = null;
	
	private static Connection conn;
	private static DBManager dbManager;
	
	private static String varName;
	private static File csvFile;
	private static DateTime date;
	
	private static List<String> varNameList;
	private static File mapDir;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		System.out.println("Executing @BeforeClass - setUpBeforeClass()");
		
		//initialize common test dependencies
		conn = createConnection();
		System.out.println("Database connection succesfully established");
		dbManager = new DBManager(conn);
		System.out.println("Created dbManager instance");

		//initialize GoesDataTest dependencies
		initializeGoesDataTestDependancies();
		
		//initialize GoesMapsTest dependencies
		initializeGoesMapsTestDependencies();
		System.out.println("SetupBeforeClass successfull");
		
		//Clean up before testing
		QueryRunner run = new QueryRunner();
		run.update(conn, DELETE_GOES_DATA_RECORDS_QUERY);
		run.update(conn, DELETE_GOES_MAPS_RECORDS_QUERY);
	}

	/****** Initialization methods ********/
	
	/**
	 * Create a new db connection 
	 * @return
	 * @throws SQLException
	 */
	private static Connection createConnection() throws SQLException{
		System.out.println("Executing @HelperMethod - createConnection()");
		return DriverManager.getConnection("jdbc:postgresql://localhost:5432/pragma_test",USERNAME,PASSWORD);
	}
	
	private static void initializeGoesDataTestDependancies(){
		//assign variable name: MUST BE A VALID FOREIGN KEY
		varName = "Bowen_Ratio";
		//create fake date to append the csv values
		//int year, int monthOfYear, int dayOfMonth, int hourOfDay,
		//int minuteOfHour, int secondOfMinute, int millisOfSecond
		date = new DateTime(1999,11,8,15,31,2,50);
		csvFile = new File("src/test/com/greendev/pragma/database/resources/variable20140309.csv");
	}

	private static void initializeGoesMapsTestDependencies() {
		varNameList = new ArrayList<String>();
		//Test variables
		varNameList.add("wind_speed");
		varNameList.add("rainfall");
		varNameList.add("actual_ET");
		//Test directory, current directory + /OUTPUT/
		mapDir = new File("src/test/com/greendev/pragma/database/OUTPUT/");
	}
	
	/******** Testing methods **********/
	
	@Test
	public void storeGoesDataTest() throws SQLException, FileNotFoundException {
		System.out.println("Executing @Test - storeGoesDataTest()");
		//Parse csv data to Goes Variable object
		List<GoesVariable> varList = CsvToModel.parse(varName, csvFile, date);

		int numericValuesInCsv = 0;

		for(int i=0; i < varList.size(); i++){
			if(!Double.isNaN(varList.get(i).getDataValue()))
				numericValuesInCsv++;
		}
		
		System.out.println("Executing dbManager.storeGoesData method ");
		int dbInserts = dbManager.storeGoesData(varName, csvFile, date);
	
		System.out.println("Expected inserts: "+numericValuesInCsv);
		System.out.println("Actual inserts: "+dbInserts);
		//It is expected that the number of numericValues in Csv 
		//should be equal to the number of inserts perfomred
		assertTrue(dbInserts == numericValuesInCsv);

	}
	
	@Test
	public void storeGoesMapsTest() throws SQLException{
		System.out.println("Executing @Test - storeGoesMapsTest()");
		int inserts = dbManager.storeGoesMap(varNameList, mapDir, date);
		
		assertTrue(inserts == varNameList.size());
	}


	@Before
	public void setUp() throws Exception {

	}

	@After
	public void tearDown() throws Exception {
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		System.out.println("Executing @AfterClass - tearDownAfterClass()");
		dbManager = null;
		varName = null;
		csvFile = null;
		date = null;
		DbUtils.close(conn); //close db connection
		conn = null;
		System.out.println("Database connection for GoesMaps closed on: "+new Timestamp(System.currentTimeMillis()));
		
	}


}
