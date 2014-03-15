package test.com.greendev.pragma.database;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import main.com.greendev.pragma.database.CsvToModel;
import main.com.greendev.pragma.database.databaseModel.GoesVariable;

import org.joda.time.DateTime;
import org.junit.Test;

import csv.impl.CSVReader;

public class CsvToModelTest {

	/**
	 * JUnit tester for utility class CsvToModel
	 * Uses a CSV file following the naming scheme: variableYYYYMMDD.csv
	 * Calls the static method CsvToModel.parse with the file and variable name
	 * @author miguelgd
	 */

	File inputCsv = new File("src/test/com/greendev/pragma/database/resources/variable20140309.csv");
	private String variableName = "variable";	
	private final DateTime dataDate= new DateTime("2014-03-09");
	private CSVReader reader;


	@Test
	public void testCsvToModelParse() {

		System.out.println(dataDate);
				
		List<GoesVariable> expected = null;
		try {
			expected = CsvToModel.parse(variableName, inputCsv, dataDate);
		} catch (FileNotFoundException e1) {
			System.out.println("Csv file not found");
			e1.printStackTrace();
		}
		List<GoesVariable> actual = new ArrayList<GoesVariable>();

		//assertEquals(result.getVariableName(), variableName);

		//Read the csv file using CSVReader
		try {
			reader = new CSVReader(inputCsv);
		} catch (FileNotFoundException e) {
			System.out.println("Invalid csv file");
			e.printStackTrace();
		}
		//Configuring reader to separate by comma ","
		reader.setColumnSeparator(',');

		//Populating values matrix with values from CSV
		int rowNum=100;
		while(reader.hasNext()){
			Object[] row = reader.next();
			for(int column=0; column < row.length; column++){

				Double value = (Double) Double.parseDouble((String) row[column]);
				GoesVariable element = new GoesVariable(variableName,dataDate,rowNum,column,value);
				actual.add(element);


			}
			System.out.println();
			rowNum--;
		}


		//Verify that size of result list is equal to matrix size (row*column)
		assertEquals(expected.size(),actual.size());

		//Verify each entry in the returned GoesVariable list
		for(int i=0; i<actual.size(); i++){
			assertEquals(expected.get(i).toString(), actual.get(i).toString());
		}



	}

}
