package main.com.greendev.pragma.database;

import java.io.Reader;

/**
 * DBMananger: Provides communication with database.   
 * Postgres JDBC driver required
 * @author josediaz
 *
 */
public class DBManager {
	
	private CsvToModel cvsUtility; 

	
	public DBManager(CsvToModel csv){
		this.cvsUtility = csv;
	}
	
	/**
	 * Stores a new GOES variable record in the database.
	 * @param variableName 
	 * @param inputReader
	 * @return true if storing the data was successful
	 */
	public boolean store(String variableName, Reader inputReader){
		boolean successStatus = false;
		
		//TODO: implement
		
	
		return successStatus;
	}
	
}
