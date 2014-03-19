package test.com.greendev.pragma.database;

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

import main.com.greendev.pragma.database.CsvToModel;
import main.com.greendev.pragma.database.DbManager;
import main.com.greendev.pragma.database.GoesImageFinder;
import main.com.greendev.pragma.database.databaseModel.GoesVariable;

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
	private static  BeanProcessor processor;
	
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
		map = new HashMap<String,String>();
		map.put("variablename", "variableName");
		map.put("matrixrow", "row");
		map.put("matrixcolumn", "column");
		map.put("datavalue", "dataValue");
		map.put("datadate",  "(DateTime) dataDate");
		//map.put("createdat", "createdAt");
		//map.put("updatedat", "updatedAt");
	
		
		processor = new BeanProcessor(map);
		//initialize common test dependencies
		conn = createConnection();
		System.out.println("Database connection succesfully established");
		dbManager = new DbManager(conn);
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
	
	/************* Testing methods **************/
	
	
	//@Test 
	public void readGoesDataTest() throws SQLException{
		System.out.println("Executing @Test - readGoesVariableTest()");
		
		
		List<String> resultList = dbManager.readGoesVariables();
		assertNotNull(resultList);
		for(String str: resultList)
			assertTrue(resultList.contains(str));
		
	}
	
	//@Test
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
	
	//@Test
	public void storeGoesMapsTest() throws SQLException{
		System.out.println("Executing @Test - storeGoesMapsTest()");
		int inserts = dbManager.storeGoesMap(varNameList, mapDir, date);
		
		assertTrue(inserts == varNameList.size());
	}
	
	@Test
	public void verifyDataInserted() throws FileNotFoundException, SQLException{
		
		Object[] beanArray;
		QueryRunner run = new QueryRunner();
		
		//Resultset Handler
		ResultSetHandler<Object[]> h = new ResultSetHandler<Object[]>(){
			Object[] result = new Object[dbManager.storeGoesData(varName, csvFile, date)];
			
			public Object[] handle(ResultSet rs) throws SQLException {
		    	
				int i = 0;
		    	GoesVariable row = null;
		    	//Create JavaBeans of each GoesVariable
		    	while(rs.next()){
		    		row = processor.toBean(rs, GoesVariable.class);
		    		System.out.println("Bean: "+row);
		    		result[i] = row;
		        	i++;
		    	}   
		    	return result;
		    }
		};
		
		//Run READ_GOES_DATA query
		//The data is read to bean array
		beanArray = run.query(conn, READ_GOES_DATA, h);
	
		/****************************************************************************
		 * Simulate the insertion method with data extracted directly from CSV file
		 ****************************************************************************/
		//Read data from csv is compared Vs. data inserted in db.
		//However, data inserted by the DbManager class, is
		//inserted if the dataValue is NOT a NaN value
		//therefore we simulate the same effect as in the
		//method by filtering the data to a list.
		
		//Extract all values from CSV 
		List<GoesVariable> dataInCsvList = CsvToModel.parse(varName, csvFile, date);
		List<GoesVariable> NaNFilteredCSVData = new ArrayList<GoesVariable>();
		
		//Filter NaN values from CSV data
		for(GoesVariable variable : dataInCsvList){
			if(!Double.isNaN(variable.getDataValue()))
			NaNFilteredCSVData.add(variable);
		}
		/***************************************************************************
		 * Query result Comparison Vs. Filtered CSV data
		 ***************************************************************************/
		
		//Compare for member filtered CSV data to query result data.
		for(int i=0; i<NaNFilteredCSVData.size(); i++){
			assertTrue(NaNFilteredCSVData.contains(beanArray[i]));
		}
		/*Compare for data integritiy
		for(int i=0; i<NaNFilteredCSVData.size(); i++){
			assertTrue(NaNFilteredCSVData.get(i).getVariableName().equals(beanArray[i])); 
			
		}
		End of test*/
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
