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
import org.joda.time.DateTime;

import main.com.greendev.pragma.database.databaseModel.GoesVariable;

/**
 * DBMananger: Provides communication with database.   
 * Postgres JDBC driver required
 * @author josediaz
 *
 */
public class DBManager {

	private static final String QUERY = "INSERT INTO " +
			"goesdata (variableName,matrixRow,matrixColumn,dataValue) " +
			"VALUES (?,?,?,?)";
	
	private Connection conn;

	/**
	 * Constructrs a DBManager object.
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
		//recieved collection 
		List<GoesVariable> varList = CsvToModel.parse(goesVariableName, csvFile, date);
		System.out.println("used CSVtoModel");
		//Create QueryRunner instance
		QueryRunner runner = new QueryRunner();

		int insertsCounter = 0;

		//Prepare batch parameter object matrix
		//Each row corresponds to a set a parameters to replace
		//The insert statement has 4 placeholders e.g.'?' 
		//Each row must contain 4 values
		for(int i=0; i<varList.size(); i++){
			if( !Double.isNaN( varList.get(i).getDataValue()) ) //insert only values that are NOT NaN values()
			 insertsCounter++;
		}
		
		System.out.println("inserts counter is: "+insertsCounter);
		Object[][] params = new Object[insertsCounter][4];
		System.out.println("varList size: "+varList.size());

		System.out.println("Printing params[][] size: "+params.length);
		
		int row=0;
		for(GoesVariable var : varList){
		
			if(!Double.isNaN(var.getDataValue()) ) //insert only values that are NOT NaN values
			{	
				params[row][0] = var.getVariableName();
				params[row][1] =var.getRow();
				params[row][2] = var.getColumn();
				params[row][3] = var.getDataValue();
				
				row++;
			}
		}
		System.out.println("printing row value: "+row);
		System.out.println("Parameters intialized!");
		//execute batch inserts
		
		try{

			runner.batch(this.conn,QUERY,params); 
			System.out.println("RAN BATCH");

			DbUtils.close(conn);
		}
		catch(SQLException sqle){
			sqle.printStackTrace();
			//logg Error
			throw sqle;
		}

		return insertsCounter;
	}

	/**
	 * 
	 * @return
	 */
	public int storeGoesMap(){
		return 0;
	}

}
