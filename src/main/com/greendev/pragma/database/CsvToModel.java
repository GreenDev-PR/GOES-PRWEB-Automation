package main.com.greendev.pragma.database;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import csv.impl.CSVReader;
import main.com.greendev.pragma.database.databaseModel.GoesVariable;

/**
 * Utility class to construct GoesVariable objects from a specified CSV file
 * @author miguelgd
 *
 */

public class CsvToModel {

	private static final int MAXIMUM_ROW_NUMBER = 100;
	private static CSVReader reader;


	/**
	 * Static method to parse a CSV file into a list of GoesVariable objects
	 * @param variableName name of the variable being processed (e.g. rainfall, wind, etc.)
	 * @param csv the File for the .csv with the data
	 * @return GoesVariable list
	 */
	public static List<GoesVariable> parse(String variableName, File csv, DateTime dataDate){		


		//Read the csv file using CSVReader
		try {
			reader = new CSVReader(csv);
		} catch (FileNotFoundException e) {
			System.out.println("Invalid csv file");
			e.printStackTrace();
		}

		//Configuring reader to separate by comma ","
		reader.setColumnSeparator(',');

		//List of GoesVariable with an element for each matrix cell, mapped to table rows
		List<GoesVariable> output = new ArrayList<GoesVariable>();

		int rowNum = MAXIMUM_ROW_NUMBER;

		//Read by row and then by column to obtain values from the matrix
		while(reader.hasNext()){

			Object[] row = reader.next();
			for(int column = 0; column < row.length; column++){			
				Double value = (Double) Double.parseDouble((String) row[column]);
				GoesVariable element = new GoesVariable(variableName, dataDate, rowNum,column,value);
				output.add(element);
			}
			rowNum--;
		}

		return output;
	}


}
