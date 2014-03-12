package test.com.greendev.pragma.database;

import static org.junit.Assert.*;

import java.io.File;

import main.com.greendev.pragma.database.CsvToModel;
import main.com.greendev.pragma.database.databaseModel.GoesVariable;

import org.apache.commons.io.FilenameUtils;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

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
	
	
	@Test
	public void testCsvToModelParse() {
		
		GoesVariable result = CsvToModel.parse(variableName, inputCsv);
		
		assertEquals(result.getName(), variableName);
		assertEquals(result.getDate(), date);
		assertEquals(result.getValues().length*result.getValues()[0].length, matrixSize);
		assertEquals(result.getImagePath(), imagePath);
	
	}

}
