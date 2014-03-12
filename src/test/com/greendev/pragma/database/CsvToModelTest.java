package test.com.greendev.pragma.database;

import static org.junit.Assert.*;

import java.io.File;

import main.com.greendev.pragma.database.CsvToModel;
import main.com.greendev.pragma.database.databaseModel.GoesVariable;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

public class CsvToModelTest {
	
	File inputCsv = new File("src/test/com/greendev/pragma/database/GOES-PRWEB Sample Data.csv");
	private String variableName = "wind";
	
	@Test
	public void test() {
		GoesVariable result = CsvToModel.parse(variableName, inputCsv);
		
		assertEquals(result.getName(), variableName);
		//assertEquals(result.getDate(), ) ToDo: validate date
		//assertEquals(result.getValues()) ToDo: validate correct values
		//assertEquals(result.getImagePath() ) Todo: validate correct map image path
	
	}

}
