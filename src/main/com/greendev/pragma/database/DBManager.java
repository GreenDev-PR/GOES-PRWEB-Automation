package main.com.greendev.pragma.database;

import java.io.File;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.apache.commons.dbutils.QueryRunner;
import org.joda.time.DateTime;

import main.com.greendev.pragma.database.databaseModel.GoesVariable;

/**
 * DBMananger: Provides communication with database.   
 * Postgres JDBC driver required
 * @author josediaz
 *
 */
public class DBManager {

	private static final String QUERY = "insert into goesdata (variableName,matrixRow,matrixColumn,dataValue,createdAt,updatedAt) values(?,?,?,?,?,?)";
	private static final String USERNAME = "josediaz";
	private static final String PASSWORD = null;
	private Connection conn;
	/**
	 * Constructrs a DBManager object.
	 */
	public DBManager(){

		try {
			this.conn = this.connStringHelper();
			System.out.println("connection built successfully!!");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Constructs a db connection instance 
	 * @return new db connection
	 * @throws SQLException
	 */
	private Connection connStringHelper() throws SQLException{
		// jdbc:postgresql://host:port/database
		String connUrl = new String("jdbc:postgresql://localhost:5432/pragma_test");
		Connection conn = DriverManager.getConnection(connUrl,USERNAME,PASSWORD);
		conn.setAutoCommit(true);
		return conn;
	}

	/**
	 * Stores  GOES variable records in the database.
	 * @param variableName 
	 * @param inputReader
	 * @return true if storing the data was successful
	 */
	public long storeInDB(String goesVariableName, File csvFile){
	

		//Parse csv data to Goes Variable object
		GoesVariable var = this.goesVariableExtractor(goesVariableName, csvFile);

		//Create QueryRunner instance
		QueryRunner run = new QueryRunner();

		//Create timestamp of insertion
		DateTime dateTime = new DateTime();
		Timestamp timeStamp = new Timestamp(dateTime.getMillis());

		long insertsCounter = 0;
		try{

			for(int i=0; i< var.getValues().length; i++){
				for(int j=0; j< var.getValues().length; j++){
					if(!Double.isNaN(var.getValues()[i][j])) //insert only values that are NOT NaN values
						insertsCounter = run.update(this.conn,QUERY,var.getName(),i,j,var.getValues()[i][j],timeStamp,timeStamp); 
				}
			}
		}
		catch(SQLException sqle){
			sqle.printStackTrace();
			System.out.println("SQL insert failed.");
		}

		return insertsCounter;
	}

	/**
	 * 
	 * @param goesVariableName
	 * @param csvFile
	 * @return
	 */
	private GoesVariable goesVariableExtractor(String goesVariableName, File csvFile){
		return CsvToModel.parse(goesVariableName, csvFile);
	}

}
