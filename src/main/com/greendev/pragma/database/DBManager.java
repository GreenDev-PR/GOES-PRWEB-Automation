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

	private String preparedStmt = "insert into goesdata (variableName,matrixRow,matrixColumn,dataValue,createdAt,updatedAt) values(?,?,?,?,?,?)";
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
	 * Constructs a db connection 
	 * @return new db connection
	 * @throws SQLException
	 */
	private Connection connStringHelper() throws SQLException{
		// jdbc:postgresql://host:port/database
		String connUrl = new String("jdbc:postgresql://localhost:5432/pragma_test");
		String username = "josediaz";
		String password = null;
		Connection conn = DriverManager.getConnection(connUrl,username,password);
		conn.setAutoCommit(true);
		return conn;
	}

	/**
	 * Stores  GOES variable records in the database.
	 * @param variableName 
	 * @param inputReader
	 * @return true if storing the data was successful
	 */
	public boolean storeInDB(String goesVariableName, File csvFile){
		boolean successStatus = false;
		//parse 
		GoesVariable var = this.goesVariableExtractor(goesVariableName, csvFile);

		QueryRunner run = new QueryRunner();

		 
		DateTime dateTime = new DateTime();
		Timestamp timeStamp = new Timestamp(dateTime.getMillis());
		
		int insertsCounter = 0;
		try{

			for(int i=0; i< var.getValues().length; i++){
				for(int j=0; j< var.getValues().length; j++){
					if(!Double.isNaN(var.getValues()[i][j])) //insert only values that are NOT NaN values
						insertsCounter = run.update(this.conn,this.preparedStmt,var.getName(),i,j,var.getValues()[i][j],timeStamp,timeStamp); 
				}
			}
		}
		catch(SQLException sqle){
			sqle.printStackTrace();
			System.out.println("INSERT EXPLOTO!!!");
		}
		successStatus = true;

		return successStatus;
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
