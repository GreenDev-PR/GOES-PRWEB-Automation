package main.com.greendev.pragma.database;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Reader;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.log4j.LogMF;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import main.com.greendev.pragma.database.databaseModel.GoesMap;
import main.com.greendev.pragma.database.databaseModel.GoesVariable;
import main.com.greendev.pragma.download.HttpDownloader;

/**
 * DBMananger: Provides communication with database.   
 * Postgres JDBC driver required
 * @author josediaz
 *
 */
public class DBManager {

	private static final String GOES_DATA_QUERY = "INSERT INTO " +
			"goesdata (variableName,matrixRow,matrixColumn,dataValue) " +
			"VALUES (?,?,?,?)";
	
	private static final int GOES_VARIABLE_TABLE_INSERT_REQUIRED_COLUMNS = 4;
	
	private static final String GOES_MAP_QUERY = "INSERT INTO goesmaps" +
			" (variableName,imagePath,dataDate) VALUES (?,?,?)";
	private static final int GOES_MAPS_TABLE_INSERT_REQUIRED_COLUMNS = 3;
	private static final int GOES_MAPS_TABLE_INSERT_REQUIRED_ROWS = 25;
	

	/**
	 * The connection to the database
	 */
	private Connection conn;

	/**
	 * Create a Logger 
	 */
	private static Logger logger = Logger.getLogger(DBManager.class);

	/**
	 * Constructs a DBManager object.
	 */
	public DBManager(Connection conn){
		this.conn = conn;
	}

	/**
	 * Stores  GOES variable records in the database.
	 * @param variableName 
	 * @param inputReader
	 * @return true if storing the data was successful
	 * @throws SQLException 
	 * @throws FileNotFoundException 
	 */
	public int storeGoesData(String goesVariableName, File csvFile, DateTime date) throws SQLException, FileNotFoundException{

		//Parse csv data to Goes Variable object
		//Received collection 
		LogMF.debug(logger,"Going to parse CsvToModel on the following date {0}",date);
		List<GoesVariable> varList = CsvToModel.parse(goesVariableName, csvFile, date);

		LogMF.debug(logger,"Successfully completed CsvToModel parsing on the following date {0}",date);

		//Counts the number if insertions to be performed based on values !NaN.
		int insertsCounter = 0;

		//Prepare batch parameter object matrix
		//Each row corresponds to a set a parameters to replace
		//The insert statement has 4 placeholder ('?') 
		//Each row must contain 4 values
		for(int i=0; i<varList.size(); i++){
			if( !Double.isNaN( varList.get(i).getDataValue()) ) //insert only values that are NOT NaN values()
				insertsCounter++;
		}
		LogMF.debug(logger,"The number if inserts to perform is {0}",insertsCounter);

		//Initialize parameter matrix with NOT NaN
		Object[][] params = new Object[insertsCounter][GOES_VARIABLE_TABLE_INSERT_REQUIRED_COLUMNS];


		int row=0;
		for(GoesVariable var : varList){
			//insert only values that are NOT NaN values
			if(!Double.isNaN(var.getDataValue()) )
			{	
				params[row][0] = var.getVariableName();
				params[row][1] = var.getRow();
				params[row][2] = var.getColumn();
				params[row][3] = var.getDataValue();

				row++;
			}
		}

		//Create query runner
		QueryRunner runner = new QueryRunner();
		int[] result;
		//Execute batch inserts
		try{
			LogMF.debug(logger,"Database connection for GoesData started at {0}",DateTime.now());
			LogMF.debug(logger,"Going to execute GoesData batch insertion query",null);
			//Run batch query
			result = runner.batch(this.conn,GOES_DATA_QUERY,params); 

			LogMF.debug(logger,"Successfully completed GoesData insertion query",null);

			DbUtils.close(conn); //close db connection

			LogMF.debug(logger,"Database connection for GoesData closed on {0}",DateTime.now());
		}
		catch(SQLException sqle){
			sqle.printStackTrace();
			//logg Erro
			LogMF.debug(logger,"Error executing batch inserts",null);
			throw sqle;
		}

		return result.length;
	}

	/**
	 * Insert GoesMaps int database
	 * @return
	 */
	public int storeGoesMap(List<String> variableList, File directory, DateTime date) throws SQLException{

		GoesImageFinder mapFinder = new GoesImageFinder();

		LogMF.debug(logger,"Going to find GoesMaps for the following date {0}",date);
		List<GoesMap> mapList = mapFinder.getMapsForDate(date, directory, variableList);
		LogMF.debug(logger,"Succesfully found GoesMaps with the following date {0}",date);

		Object[][] params = new Object[GOES_MAPS_TABLE_INSERT_REQUIRED_ROWS][GOES_MAPS_TABLE_INSERT_REQUIRED_COLUMNS];
		int row = 0;
		for(GoesMap map: mapList){
			
			params[row][0] = map.getVariableName();
			params[row][1] = map.getImagePath();
			params[row][2] = map.getDataDate(); //OJOJjJOjOJojOJOJOJOJ
			
			row++;
		}
		
		//Create query runner
		QueryRunner runner = new QueryRunner();
		int[] result;
		try{
			LogMF.debug(logger,"Database connection for GoesMaps started at {0}",DateTime.now());
			LogMF.debug(logger,"Going to execue GoesMap batch insertion query",null);
			
			result = runner.batch(this.conn, GOES_MAP_QUERY, params);
			LogMF.debug(logger,"Successfully completed GoesMap batch insertion query",null);
			DbUtils.close(conn); //close db connection
			LogMF.debug(logger,"Database connection for GoesMaps closed on {0}",DateTime.now());
		}catch(SQLException sqle){
			sqle.printStackTrace();
			LogMF.debug(logger, "Error inserting Goes Maps into database",null);
			throw sqle;
		}

		return result.length;
	}

}
