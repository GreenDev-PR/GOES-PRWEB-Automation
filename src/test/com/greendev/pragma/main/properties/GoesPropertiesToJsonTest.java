package test.com.greendev.pragma.main.properties;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import main.com.greendev.pragma.main.properties.GoesPropertiesToJson;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * Tester for GoesPropertiesToJson
 * Used to create goesProperties.json file if it does not exist
 * @author miguelgd
 *
 */
public class GoesPropertiesToJsonTest {
	
	File propertiesFile;

	@Before
	public void setUp() throws Exception {

		propertiesFile = new File("src/test/com/greendev/pragma/main/properties/goesProperties.json");
		FileUtils.touch(propertiesFile);
		
	}

	@Test
	public void testCreateJsonFromGoesProperties() {

		try {
			GoesPropertiesToJson.main(null);
		} catch (IOException e) {
			System.out.println("Error creating goesProperties.json file");
			e.printStackTrace();
		}		
		assertTrue(propertiesFile.exists());
		
	}

}
