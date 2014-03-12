package test.com.greendev.pragma.database;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;

import main.com.greendev.pragma.database.CsvToModel;
import main.com.greendev.pragma.database.databaseModel.GoesVariable;

import org.apache.commons.io.FilenameUtils;
import org.junit.Test;

import csv.impl.CSVReader;

public class CsvToModelTest {

	/**
	 * JUnit tester for utility class CsvToModel
	 * Uses a CSV file following the naming scheme: variableYYYYMMDD.csv
	 * Calls the static method CsvToModel.parse with the file and variable name
	 * Validates name of file, date, values matrix, and map image path
	 * @author miguelgd
	 */

	File inputCsv = new File("src/test/com/greendev/pragma/database/variable20140309.csv");
	private String variableName = "variable";
	private String date = "20140309";
	private int matrixSize = 210*101;
	private String imagePath = FilenameUtils.removeExtension(inputCsv.getAbsolutePath())+".jpg";
	
	private final int ROW_NUMBER = 101;
	private final int COLUMN_NUMBER = 210;
	private CSVReader reader;


	@Test
	public void testCsvToModelParse() {

		GoesVariable result = CsvToModel.parse(variableName, inputCsv);

		assertEquals(result.getName(), variableName);
		assertEquals(result.getDate(), date);

		assertEquals(result.getValues().length*result.getValues()[0].length, matrixSize);
		//Read the csv file using CSVReader
		try {
			reader = new CSVReader(inputCsv);
		} catch (FileNotFoundException e) {
			System.out.println("Invalid csv file");
			e.printStackTrace();
		}
		//Configuring reader to separate by comma ","
		reader.setColumnSeparator(',');
		//Bi-dimensional array to store data values read from csv
		Double[][] values = new Double[ROW_NUMBER][COLUMN_NUMBER];
		
		//Populating values matrix with values from CSV
		int rowNum=0;
		while(reader.hasNext()){

			Object[] row = reader.next();
			for(int i=0; i < row.length; i++){
				values[rowNum][i] = Double.parseDouble((String) row[i]);
				//if(Double.isNaN(values[rowNum][i])) ToDo: decide what to do with NaN 
			}
			System.out.println();
			rowNum++;
		}
		
		//Comparing values retrieved with actual csv values
		for(int i = 0; i<values.length; i++){
			for(int j=0; j<values[0].length; j++){
				assertEquals(result.getValues()[i][j], values[i][j]);
			}	
		}

		assertEquals(result.getImagePath(), imagePath);

	}

}
