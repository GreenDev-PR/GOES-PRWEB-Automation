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

import main.com.greendev.pragma.database.CsvToModel;
import main.com.greendev.pragma.database.DBManager;
import main.com.greendev.pragma.database.databaseModel.GoesVariable;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
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

	private static final String VERIFY_DATA_QUERY = "SELECT * FROM goesdata";
	private static final String DELETE_TABLE_RECORDS_QUERY = "DELETE FROM goesdata";
	private static final String USERNAME = "josediaz";
	private static final String PASSWORD = null;
	
	private static Connection conn;
	private static DBManager dbManager;
	private static String varName;
	private static File csvFile;
	private static DateTime date;

	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		System.out.println("hereeee");
		varName = "variable";
		date = new DateTime();
		csvFile = new File("src/test/com/greendev/pragma/database/resources/variable20140309.csv");
		conn = createConnection();
		//clear previous records
		dbManager = new DBManager(conn);
		QueryRunner run = new QueryRunner();
		run.update(conn, DELETE_TABLE_RECORDS_QUERY);
		System.out.println("initialization complete!");
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
		conn = null;
		date = null;
	}

	@Test
	public void storeInDbTest() throws SQLException, FileNotFoundException {
		System.out.println("storeInDb!!!");
		int dbInserts = dbManager.storeGoesData(varName, csvFile, date);
		System.out.println("afterstoee!!!");
		/*//Parse csv data to Goes Variable object
		GoesVariable var = CsvToModel.parse(variableName, csv);

		long numericValuesInCsv = 0;
		for(int i = 0; i < var.getValues().length; i++){
			for(int j=0; j < var.getValues().length; j++){
				if(!Double.isNaN(var.getValues()[i][j]))
					numericValuesInCsv++;
			}
		}
		System.out.println("Numeric values found in CSV: "+numericValuesInCsv);
		System.out.println("Number of insertions performed to the database: "+dbInserts);

		assertTrue(dbInserts == numericValuesInCsv);

		/*******************************************/
		/********* Verify the data inserted ********/
		/*******************************************/
		/*
		System.out.println("Starting verify Test");
		QueryRunner run = new QueryRunner();
		
		ResultSetHandler<GoesVariable> h = new BeanHandler<GoesVariables>(GoesVariable.class);
	

		
		
		for(Object obj: result){
			System.out.println("Printing: "+obj.toString());
		}

		assertTrue(true);
		 */
	}


	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}



}
