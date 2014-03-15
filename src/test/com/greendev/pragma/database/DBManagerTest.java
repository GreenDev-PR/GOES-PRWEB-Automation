package test.com.greendev.pragma.database;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import main.com.greendev.pragma.database.CsvToModel;
import main.com.greendev.pragma.database.DBManager;
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
 * Verifies that the CSVdata was inserted correctly to the database
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

	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		System.out.println("setUpBeforeClass executing...");
		varName = "variable";
		date = new DateTime();
		csvFile = new File("src/test/com/greendev/pragma/database/resources/variable20140309.csv");
		conn = createConnection();
		System.out.println("Succesfully connected to database..");
		//clear previous records
		dbManager = new DBManager(conn);
		System.out.println("Created dbManager instance");

		//DbUtils.close(conn);
		System.out.println("Initialization completed...");
	}

	/**
	 * Create a new db connection 
	 * @return
	 * @throws SQLException
	 */
	private static Connection createConnection() throws SQLException{
		return DriverManager.getConnection("jdbc:postgresql://localhost:5432/pragma_test",USERNAME,PASSWORD);
	}

	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		dbManager = null;
		varName = null;
		csvFile = null;
		
		date = null;
		//DbUtils.close(conn); //close db connection
		//System.out.println("Database connection for GoesMaps closed on {0}");
		
	}

	@Test
	public void storeGoesDataTest() throws SQLException, FileNotFoundException {
		
		System.out.println("Executing dbManager.storeGoesData method ");
		int dbInserts = dbManager.storeGoesData(varName, csvFile, date);
		
		System.out.println("db inserts: "+dbInserts);
		//Parse csv data to Goes Variable object
		List<GoesVariable> varList = CsvToModel.parse(varName, csvFile, date);

		int numericValuesInCsv = 0;

		for(int i=0; i < varList.size(); i++){
			if(!Double.isNaN(varList.get(i).getDataValue()))
				numericValuesInCsv++;
		}
		//It is expected that the number of numericValues in Csv 
		//should be equal to the number of inserts perfomred
		assertTrue(dbInserts == numericValuesInCsv);

	}
	
	@Test
	public void storeGoesMapsTest(){
		/*System.out.println("Executing dbManager.storeGoesMaps method");
		File dir = new File();
		int dbInserts = dbManager.storeGoesMap(variableList, directory, date)*/
	}


	@Before
	public void setUp() throws Exception {
		QueryRunner run = new QueryRunner();
		run.update(conn, DELETE_GOES_DATA_RECORDS_QUERY);
		run.update(conn, DELETE_GOES_MAPS_RECORDS_QUERY);
	}

	@After
	public void tearDown() throws Exception {
	}



}
