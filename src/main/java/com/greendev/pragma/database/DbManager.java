//TODO: Verify insertion of updatedAt & createdAt values
//TODO: Inserting NaN values???
package main.java.com.greendev.pragma.database;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.log4j.LogMF;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import main.java.com.greendev.pragma.database.databaseModel.GoesMap;
import main.java.com.greendev.pragma.database.databaseModel.GoesVariable;


/**
 * DBMananger: Provides communication with database.   
 * Postgres JDBC driver required
 * @author josediaz
 *
 */
public class DbManager {
	
	/**
	 * Create a Logger 
	 */
	private static Logger logger = Logger.getLogger(DbManager.class);

	private static final String READ_GOES_VARIABLES_QUERY = "SELECT * FROM GoesVariables";
	
	
	private static final String INSERT_GOES_DATA_QUERY = "INSERT INTO " +
			"goesdata (variableName,matrixRow,matrixColumn,dataValue,dataDate,createdAt,updatedAt) " +
			"VALUES (?,?,?,?,?,?,?)";
	private static final int GOES_DATA_TABLE_INSERT_REQUIRED_COLUMNS = 7;
	
	private static final String INSERT_GOES_MAP_QUERY = "INSERT INTO GoesMaps" +
			" (variableName,imagePath,dataDate,createdAt,updatedAt) VALUES (?,?,?,?,?)";
	private static final int GOES_MAPS_TABLE_INSERT_REQUIRED_COLUMNS = 5;
	

	/**
	 * The connection to the database
	 */
	private Connection conn;

	/**
	 * Constructs a DBManager object.
	 */
	public DbManager(Connection conn){
		this.conn = conn;
	}
	
	/**
	 * Reads goes variables from the GoesVariable table.
	 * This should be the first method to call when using 
	 * dbManager class
	 * @return The list of goes variables found in the database.
	 * @throws SQLException 
	 */
	public List<String> readGoesVariables() throws SQLException{
		
		//define resultset handler
		ResultSetHandler<Object[]> h = new ResultSetHandler<Object[]>() {
		    public Object[] handle(ResultSet rs) throws SQLException {
		
		       List<Object> list = new ArrayList<Object>();
		    	while (rs.next()) {
		        	list.add(rs.getObject("variablename"));	
		    	}
		    	
		    	Object[] result = new Object[list.size()];
		    	int i =0;
		    	for(Object obj: list){
		    		result[i] = obj;
		    		i++;
		    	}
		    	list = null;
		        return result;
		    }
		};
		//System.out.println("Hanlder creation");
		QueryRunner run = new QueryRunner();
		
		logger.info("Reading GoesVariable TABLE");
		Object[] res = run.query(conn, READ_GOES_VARIABLES_QUERY, h);
		
		List<String> resList = new ArrayList<String>();
		
		for(Object obj: res){
			resList.add(""+obj);
		}
		logger.info("Succesfully read " +resList.size() + " variables from GoesVariable Table");
		return resList;
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
		LogMF.info(logger,"Going to parse CsvToModel on the following date {0}",date);
		List<GoesVariable> varList = CsvToModel.parse(goesVariableName, csvFile, date);

		LogMF.info(logger,"Successfully completed CsvToModel parsing on the following date {0}",date);

		//Counts the number if insertions to be performed based on values !NaN.
		int insertsCounter = 0;

		//Determine number of values to insert in order
		//to prepare batch parameter object matrix.
		//Each row corresponds to a set a parameters to replace.
		//If insert statement has 4 placeholder ('?') then
		//each row must contain 4 values
		for(int i=0; i<varList.size(); i++){
			if( !Double.isNaN( varList.get(i).getDataValue()) ) //insert only values that are NOT NaN values()
				insertsCounter++;
		}

		//Initialize parameter matrix with NOT NaN
		Object[][] params = new Object[insertsCounter][GOES_DATA_TABLE_INSERT_REQUIRED_COLUMNS];

		int row=0;
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		
		for(GoesVariable var : varList){
			//insert only values that are NOT NaN values
			if(!Double.isNaN(var.getDataValue()) )
			{	
				params[row][0] = var.getVariableName();
				params[row][1] = var.getRow();
				params[row][2] = var.getColumn();
				params[row][3] = var.getDataValue();
				params[row][4] = new Timestamp(var.getDataDate().getMillis());
				params[row][5] = timestamp;
				params[row][6] = timestamp;

				row++;
			}
		}

		//Create query runner
		QueryRunner runner = new QueryRunner();
		int[] result;
		//Execute batch inserts
		LogMF.info(logger,"Going to execute GoesData batch insertion query",null);
		try{
			LogMF.info(logger,"Database connection for GoesData started at {0}",DateTime.now());
			
			//Run batch query
			result = runner.batch(this.conn,INSERT_GOES_DATA_QUERY,params); 

			LogMF.info(logger,"Successfully completed GoesData insertion query with {0} new entries",result.length);
		}
		catch(SQLException sqle){
			LogMF.error(logger,"Error executing batch inserts",null);
			throw sqle;
		} 

		return result.length;
	}

	/**
	 * Insert GoesMaps in database. 
	 * @param variableList Variables for which maps will be inserted into the database
	 * @param directory The directory path to the map images
	 * @param date The date of the insertion
	 * @return The number of records inserted to the database
	 * @throws SQLException
	 */
	public int storeGoesMap(List<String> variableList, File directory, DateTime date) throws SQLException{
		
		GoesImageFinder mapFinder = new GoesImageFinder();

		LogMF.info(logger,"Going to find GoesMaps for the following date {0}",date);
		List<GoesMap> mapList = mapFinder.getMapsForDate(date, directory, variableList);
		LogMF.info(logger,"Succesfully found GoesMaps with the following date {0}",date);

		Object[][] params = new Object[mapList.size()][GOES_MAPS_TABLE_INSERT_REQUIRED_COLUMNS];
		int row = 0;
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		
		for(GoesMap map: mapList){
			
			params[row][0] = map.getVariableName();
			params[row][1] = map.getImagePath();
			params[row][2] = new Timestamp(map.getDataDate().getMillis());
			params[row][3] = timestamp;
			params[row][4] = timestamp;
		
			row++;
		}
		
		//Create query runner
		QueryRunner runner = new QueryRunner();
		int[] result;
		
		try{
			LogMF.info(logger,"Database connection for GoesMaps started at {0}",DateTime.now());
			LogMF.info(logger,"Going to execue GoesMap batch insertion query",null);
			
			result = runner.batch(this.conn, INSERT_GOES_MAP_QUERY, params);
			LogMF.info(logger,"Successfully completed GoesMap batch insertion query",null);
			
		}catch(SQLException sqle){
			LogMF.error(logger, "Error inserting Goes Maps into database",null);
			throw sqle;
			
		}
			
		return result.length;
	}
	
}
