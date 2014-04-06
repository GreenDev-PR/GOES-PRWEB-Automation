package test.com.greendev.pragma.degrib;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import main.com.greendev.pragma.degrib.DegribVariable;
import main.com.greendev.pragma.degrib.Degribber;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests the Degribber and DegribVariable classes
 * @author miguelgd
 *
 */

public class DegribTest {
	
	//Actual number of files for the given variable
	private static final int COUNT_OF_WIND_FILES = 1;
	private static final int COUNT_OF_SUN_FILES = 0;
	private Degribber degrib;
	private DegribVariable variable;
	private List<DegribVariable> variables;
	private File degribDirectory;
	private File outputDirectory;
	
	//Existing variable windYYYYMMDD.bin
	private final String VARIABLE_NAME = "wind*";
	private final String OUTPUT_VARIABLE_NAME = "wind";

	//Non existing variable sunYYYYMMDD.bin
	private final String NON_EXISTING_VARIABLE_NAME = "sun*";
	private final String NON_EXISTING_OUTPUT_VARIABLE_NAME = "sun";
	
	private List<Integer> messages;
	
	//Path to the degrib (NOAA) executable
	private String executable = "/usr/local/bin/degrib/bin/degrib";

	@Before
	public void setUp() throws Exception {	
		degrib = new Degribber();
		variable = new DegribVariable();
		//Location of bin files to degrib
		degribDirectory = new File("src/test/com/greendev/pragma/degrib/resources/");
		//Output the degrib results in the same folder
		outputDirectory = degribDirectory;
		messages = new ArrayList<Integer>();
		degrib.setDegribDirectory(degribDirectory);
		degrib.setOutputDirectory(outputDirectory);
		degrib.setExecutable(executable);
		
		//Create sample messages to degrib (1-4)
		for(int i=1; i<5; i++)
			messages.add(i);
		
	}

	/**
	 * Tests that the files for a given variable are returned
	 */
	@Test
	public void testFilesForVariableFound(){
		variable.setName(VARIABLE_NAME);
		variable.setMessages(messages);
		variable.setOutputName(OUTPUT_VARIABLE_NAME);
		variables = new ArrayList<DegribVariable>();
		variables.add(variable);
		degrib.setVariables(variables);
		
		Collection<File> results = degrib.getGribFiles(variable);
		assertEquals(COUNT_OF_WIND_FILES, results.size());
	}

	/**
	 * Tests that the variables are degribed to csv and that the 
	 * csv contains the actual values
	 */
	@Test
	public void testDegribVariables(){
		variable.setName(VARIABLE_NAME);
		variable.setMessages(messages);
		variable.setOutputName(OUTPUT_VARIABLE_NAME);
		variables = new ArrayList<DegribVariable>();
		variables.add(variable);
		degrib.setVariables(variables);

		try {
			degrib.degribVariables();
		} catch (IOException e1) {
			System.out.println("Error running degrib executable");
			e1.printStackTrace();
		}
		String[] extensions = {"csv"};
		Collection<File> result = FileUtils.listFiles(outputDirectory, extensions, false);
		int actualCount = result.size();

		assertEquals(messages.size(),actualCount);
		for(Integer m : messages){
			System.out.println(m);
			String currentFile = outputDirectory.getPath()+"/"+OUTPUT_VARIABLE_NAME+m+".csv";
			System.out.println(currentFile);
			try {
				assertTrue(FileUtils.directoryContains(outputDirectory, new File(outputDirectory,OUTPUT_VARIABLE_NAME+m+".csv")));
			} catch (IOException e) {
				System.out.println("Error accessing directory");
			}
		}

	}

	/**
	 * Tests that for a variable that does not exist, the degriber
	 * does not stop and nothing is created in the output directory
	 * Useful in the case of downloads that fail (will be estimated with
	 * older data) that should not be degribed. 
	 */
	@Test
	public void testDegribVariableNotExists(){
		variable.setName(NON_EXISTING_VARIABLE_NAME);
		variable.setMessages(messages);
		variable.setOutputName(NON_EXISTING_OUTPUT_VARIABLE_NAME);
		variables = new ArrayList<DegribVariable>();
		variables.add(variable);
		degrib.setVariables(variables);

		try {
			degrib.degribVariables();
		} catch (IOException e1) {
			System.out.println("Error running degrib executable");
			e1.printStackTrace();
		}
		String[] extensions = {"csv"};
		Collection<File> result = FileUtils.listFiles(outputDirectory, extensions, false);
		assertEquals(result.size(), COUNT_OF_SUN_FILES);	
	}


	@After
	public void cleanUp(){
		String[] extensions = {"csv", "txt"};
		Collection<File> toDelete = FileUtils.listFiles(outputDirectory, extensions, false);
		for(File file : toDelete){
			FileUtils.deleteQuietly(file);
		}
	}

}
