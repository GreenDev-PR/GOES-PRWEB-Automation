package test.java.com.greendev.pragma.database;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.java.com.greendev.pragma.database.CsvToModel;
import main.java.com.greendev.pragma.database.DbManager;
import main.java.com.greendev.pragma.database.GoesImageFinder;
import main.java.com.greendev.pragma.database.databaseModel.GoesMap;
import main.java.com.greendev.pragma.database.databaseModel.GoesVariable;

import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.RowProcessor;
import org.apache.commons.io.FileUtils;
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
public class DbManagerTest {

	private static final String DELETE_GOES_DATA_RECORDS_QUERY = "DELETE FROM GoesData"; 
	private static final String DELETE_GOES_MAPS_RECORDS_QUERY = "DELETE FROM GoesMaps"; 

	private static final String READ_GOES_DATA = "SELECT * FROM GoesData";
	private static final String READ_GOES_MAPS = "SELECT * FROM GoesMaps";

	private static  Map<String,String> map;
	private static  BeanProcessor goesDataBeanProcessor;

	private static  Map<String,String> map2;
	private static  BeanProcessor goesMapsBeanProcessor;
	
	private static final String USERNAME = "postgres";
	private static final String PASSWORD = null;

	private static Connection conn;
	private static DbManager dbManager;

	private static String varName;
	private static File csvFile;
	private static DateTime date;

	private static List<String> varNameList;
	private static File mapDir;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		System.out.println("Executing @BeforeClass - setUpBeforeClass()");

		//initialize common test dependencies
		conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/pragma_test",USERNAME,PASSWORD);
		System.out.println("Database connection succesfully established");
		dbManager = new DbManager(conn);
		System.out.println("Created dbManager instance");

		//initialize GoesDataIntegrityTest dependencies
		initializeGoesDataIntegrityDependencies();
		
		//initialize GoesMapsIntegrityTest dependencies
		initializeGoesMapsIntegrityDependencies();
		
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
	private static void initializeGoesDataTestDependancies(){
		//assign variable name: MUST BE A VALID FOREIGN KEY
		varName = "Bowen_Ratio";
		//create fake date to append the csv values
		//int year, int monthOfYear, int dayOfMonth, int hourOfDay,
		//int minuteOfHour, int secondOfMinute, int millisOfSecond
		date = new DateTime(1999,11,8,15,31,2,50);
		csvFile = new File("src/test/java/com/greendev/pragma/database/resources/variable20140309.csv");
	}

	private static void initializeGoesMapsTestDependencies() {
		varNameList = new ArrayList<String>();
		//Test variables
		varNameList.add("wind_speed");
		varNameList.add("rainfall");
		varNameList.add("actual_ET");
		//Test directory, current directory + /OUTPUT/
		mapDir = new File("src/test/java/com/greendev/pragma/database/OUTPUT/");
	}

	private static void initializeGoesDataIntegrityDependencies(){
		//Setup Bean Processor
		map = new HashMap<String,String>();
		map.put("variablename", "variableName");
		map.put("matrixrow", "row");
		map.put("matrixcolumn", "column");
		map.put("datavalue", "dataValue");
		map.put("datadate",  "dataDate");
		goesDataBeanProcessor = new BeanProcessor(map);
	}
	
	private static void initializeGoesMapsIntegrityDependencies(){
		//Setup Map Bean Processor
		map2 = new HashMap<String,String>();
		map2.put("variablename", "variableName");
		map2.put("imagepath", "imagePath");
		map2.put("datadate",  "dataDate");
		goesMapsBeanProcessor = new BeanProcessor(map2);
	}

	/************* Testing methods **************/
	@Test 
	public void readGoesDataTest() throws SQLException{
		System.out.println("Executing @Test - readGoesVariableTest()");

		List<String> resultList = dbManager.readGoesVariables();
		assertNotNull(resultList);
		for(String str: resultList)
			assertTrue(resultList.contains(str));
	}

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

	@Test
	public void verifyGoesDataDbIntegrity() throws FileNotFoundException, SQLException{
		System.out.println("Executing @Test - verifyGoesDataDbIntegrity");
		//Resultset Handler
		ResultSetHandler<Object[]> h = new ResultSetHandler<Object[]>(){
			Object[] result = new Object[dbManager.storeGoesData(varName, csvFile, date)];

			public Object[] handle(ResultSet rs) throws SQLException {

				int i = 0;
				GoesVariableBean row = null;
				//Create JavaBeans of each GoesVariable
				while(rs.next()){
					row = goesDataBeanProcessor.toBean(rs, GoesVariableBean.class);
					result[i] = row;
					i++;
				}   
				return result;
			}
		};

		QueryRunner run = new QueryRunner();
		//Run READ_GOES_DATA query
		//The data is read to bean array
		Object[] beanArray = run.query(conn, READ_GOES_DATA, h);

		/****************************************************************************
		 * Collect the insertion method data extracted directly from CSV file
		 ****************************************************************************/
		//Read data from csv is compared Vs. data inserted in db.
		//However, data inserted by the DbManager class, is
		//inserted if the dataValue is NOT a NaN value
		//therefore we simulate the same effect as in the
		//method by filtering the data to a list.

		//Extract all values from CSV 
		List<GoesVariable> dataInCsvList = CsvToModel.parse(varName, csvFile, date);
		List<GoesVariableBean> NaNFilteredCSVData = new ArrayList<GoesVariableBean>();

		//Filter NaN values from CSV data
		for(GoesVariable variable : dataInCsvList){
			if(!Double.isNaN(variable.getDataValue()))
				NaNFilteredCSVData.add(new GoesVariableBean(variable));
		}
		/***************************************************************************
		 * Query result Comparison Vs. Filtered CSV data
		 ***************************************************************************/

		//Compare for member filtered CSV data to query result data.
		//This verifies data integrity
		for(int i=0; i<NaNFilteredCSVData.size(); i++){
			//	System.out.println("Bean: "+beanArray[i]);
			//	System.out.println("CSV: "+NaNFilteredCSVData.get(i));
			assertTrue(NaNFilteredCSVData.contains(beanArray[i]));
		}
	}

	@Test
	public void verifyGoesMapsDbDataIntegrity() throws SQLException{
		System.out.println("Executing @Test - verifyGoesMapsDbdataIntegrity");
		//Resultset Handler
		ResultSetHandler<Object[]> h = new ResultSetHandler<Object[]>(){
			Object[] result = new Object[dbManager.storeGoesMap(varNameList, mapDir, date)];

			public Object[] handle(ResultSet rs) throws SQLException {

				int i = 0;
				GoesMapBean row = null;
				//Create JavaBeans of each GoesVariable
				while(rs.next()){
					row = goesMapsBeanProcessor.toBean(rs, GoesMapBean.class);
					result[i] = row;
					i++;
				}   
				return result;
			}
		};
		
		QueryRunner run = new QueryRunner();
		Object[] readMaps = run.query(conn, READ_GOES_MAPS, h);
		
		GoesImageFinder mapFinder = new GoesImageFinder();
		
		//Collect maps from directory
		List<GoesMap> fsMaps = mapFinder.getMapsForDate(date, mapDir, varNameList);
		//Convert maps from dirtory to Bean Representation
		List<GoesMapBean> fsMapsBean = new ArrayList<GoesMapBean>();
		for(GoesMap map : fsMaps){
			fsMapsBean.add(new GoesMapBean(map));
		}
		//Comparison
		for(int i=0; i<readMaps.length; i++){
			assertTrue(fsMapsBean.contains(readMaps[i]));
		}
	}

	@Before
	public void setUp() throws Exception {

	}

	@After
	public void tearDown() throws Exception {
		//Clean up after testing
		QueryRunner run = new QueryRunner();
		run.update(conn, DELETE_GOES_DATA_RECORDS_QUERY);
		run.update(conn, DELETE_GOES_MAPS_RECORDS_QUERY);
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
