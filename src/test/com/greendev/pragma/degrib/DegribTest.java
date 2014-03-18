//TODO: Implement test to validate that Grib files are returned for a given variable and that the files are read using Degrib executable

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
import org.junit.Test;

public class DegribTest {

	private static final int COUNT_OF_WIND_FILES = 1;
	private Degribber degrib;
	private DegribVariable variable;
	private List<DegribVariable> variables;
	private File degribDirectory;
	private File outputDirectory;
	private final String VARIABLE_NAME = "wind*";
	private List<Integer> messages;
	private final String OUTPUT_VARIABLE_NAME = "wind";
	private String executable = "/usr/local/bin/degrib/bin/degrib";

	@Before
	public void setUp() throws Exception {	
		degrib = new Degribber();
		variable = new DegribVariable();
		degribDirectory = new File("src/test/com/greendev/pragma/degrib/resources/");
		outputDirectory = degribDirectory;
		messages = new ArrayList<Integer>();
		variables = new ArrayList<DegribVariable>();
		degrib.setDegribDirectory(degribDirectory);
		degrib.setOutputDirectory(outputDirectory);
		degrib.setExecutable(executable);
		for(int i=1; i<5; i++)
			messages.add(i);
		variable.setName(VARIABLE_NAME);
		variable.setMessages(messages);
		variable.setOutputName(OUTPUT_VARIABLE_NAME);
		variables.add(variable);
		degrib.setVariables(variables);

	}


	@Test
	public void testFilesForVariableFound(){
		Collection<File> results = degrib.getGribFiles(variable);
		//System.out.println("Count of wind grib files: "+results.size());
		assertEquals(COUNT_OF_WIND_FILES, results.size());
	}

	@Test
	public void testDegribVariables(){
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
			String currentFile = outputDirectory.getPath()+"/"+OUTPUT_VARIABLE_NAME+m+".csv";
			System.out.println(currentFile);
			try {
				assertTrue(FileUtils.directoryContains(outputDirectory, new File(outputDirectory,OUTPUT_VARIABLE_NAME+m+".csv")));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
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
