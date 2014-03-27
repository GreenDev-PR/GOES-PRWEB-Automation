package test.com.greendev.pragma.database;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import main.com.greendev.pragma.database.GoesImageFinder;
import main.com.greendev.pragma.database.databaseModel.GoesMap;

import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the behavior of GoesImageFinder and getMapsForDate method
 * @author miguelgd
 *
 */
public class GoesImageFinderTest {

	GoesImageFinder finder;
	DateTime date;
	List<String> variables;
	File directory;

	@Before
	public void setUp() throws Exception{
		//Test date
		date = new DateTime("2014-03-09");
		variables = new ArrayList<String>();
		//Test variables
		variables.add("wind_speed");
		variables.add("rainfall");
		variables.add("actual_ET");
		//Test directory, current directory + /OUTPUT/
		directory = new File("src/test/com/greendev/pragma/database/OUTPUT/");
		finder = new GoesImageFinder();
	}

	//Gets the list of map paths using finder.getMapsForDate
	//Compares the list with the list of files in the test directory
	@Test
	public void testGetMapsForDate() {
		List<GoesMap> output = finder.getMapsForDate(date, directory, variables);
		
		for(GoesMap map : output){
			File expectedFile = FileUtils.getFile(directory, map.getVariableName());
			String expectedPath = expectedFile.getAbsolutePath()+".jpg";
			assertEquals(expectedPath,map.getImagePath());
		}

	}

}