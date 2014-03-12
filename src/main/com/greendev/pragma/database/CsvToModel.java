package main.com.greendev.pragma.database;

import java.io.File;
import java.io.FileNotFoundException;
import csv.impl.CSVReader;
import main.com.greendev.pragma.database.databaseModel.GoesVariable;

public class CsvToModel {

	private static final int ROW_NUMBER = 101;
	private static final int COLUMN_NUMBER = 210;
	private static CSVReader reader;

	public static GoesVariable parse(String variableName, File csv){		

		try {
			reader = new CSVReader(csv);
		} catch (FileNotFoundException e) {
			System.out.println("Invalid csv file");
			e.printStackTrace();
		}

		reader.setColumnSeparator(',');
		Double[][] values = new Double[ROW_NUMBER][COLUMN_NUMBER];

		int rowNum = 0;
		while(reader.hasNext()){

			Object[] row = reader.next();
			for(int i=0; i < row.length; i++){
				values[rowNum][i] = Double.parseDouble((String) row[i]);
				//if(Double.isNaN(values[rowNum][i])) ToDo: decide what to do with NaN 
				//System.out.println("Row: "+rowNum+" Column: "+i+" Value: "+values[rowNum][i]);
			}
			System.out.println();
			rowNum++;
		}

		GoesVariable out = new GoesVariable();
		
		out.setName(variableName);
		out.setValues(values);
		//out.setDate(date); ToDo: Get date from file name and set out date
		//out.setImagePath(imagePath); ToDo: Get map image path based on variable and date
		
		return out;



	}


}
