package main.com.greendev.pragma.database;

import java.io.File;
import java.io.FileNotFoundException;
import org.apache.commons.io.FilenameUtils;

import csv.impl.CSVReader;
import main.com.greendev.pragma.database.databaseModel.GoesVariable;

/**
 * Utility class to construct GoesVariable objects from a specified CSV file
 * @author miguelgd
 *
 */

public class CsvToModel {

	private static final int ROW_NUMBER = 101;
	private static final int COLUMN_NUMBER = 210;
	private static CSVReader reader;

	
	/**
	 * Static method to parse a CSV file into a GoesVariable object
	 * @param variableName name of the variable being processed (e.g. rainfall, wind, etc.)
	 * @param csv the File for the .csv with the data
	 * @return GoesVariable instance loaded with the name of the variable, date, values matrix and map image path
	 */
	public static GoesVariable parse(String variableName, File csv){		

		
		//Read the csv file using CSVReader
		try {
			reader = new CSVReader(csv);
		} catch (FileNotFoundException e) {
			System.out.println("Invalid csv file");
			e.printStackTrace();
		}

		//Configuring reader to separate by comma ","
		reader.setColumnSeparator(',');
		
		//Bi-dimensional array to store data values read from csv
		Double[][] values = new Double[ROW_NUMBER][COLUMN_NUMBER];

		int rowNum = 0;
		
		//Read by row and then by column to obtain values from the matrix
		while(reader.hasNext()){

			Object[] row = reader.next();
			for(int i=0; i < row.length; i++){
				values[rowNum][i] = Double.parseDouble((String) row[i]);
				//if(Double.isNaN(values[rowNum][i])) ToDo: decide what to do with NaN 
			}
			System.out.println();
			rowNum++;
		}

		//GoesVariable object to return
		GoesVariable out = new GoesVariable();
		
		//Setting the name of the variable
		out.setName(variableName);
		
		//Setting the values matrix previously populated
		out.setValues(values);
		
		//Extracting the date from the file name with the following naming convention: variableYYYYMMDD.csv
		//Setting the date
		String date = csv.getName().substring(variableName.length());
		date = date.substring(0, 8);
		System.out.println(date);
		out.setDate(date);
		
		//Extracting the map image path from the file name
		//Located and named same as CSV but with different extension: variableYYYYMMDD.jpg
		//Setting the map image path
		String imagePath = FilenameUtils.removeExtension(csv.getAbsolutePath())+".jpg";
		out.setImagePath(imagePath);
		
		return out;
	}


}
