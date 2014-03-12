//TODO: Implement test to validate that Grib files are returned for a given variable and that the files are read using Degrib executable

package test.com.greendev.pragma.degrib;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import main.com.greendev.pragma.degrib.DegribVariable;
import main.com.greendev.pragma.degrib.Degribber;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;

public class DegribTest {

	private static final int COUNT_OF_WIND_FILES = 1;
	Degribber degrib = new Degribber();
	DegribVariable variable = new DegribVariable();
	List<DegribVariable> variables = new ArrayList<DegribVariable>();
	File degribDirectory = new File("src/test/com/greendev/pragma/degrib/");
	File outputDirectory = degribDirectory;
	String variableName = "wind*";
	List<Integer> messages = new ArrayList<Integer>();
	String variableOutputName = "wind";
	String executable = "/Users/miguelgd/Downloads/degrib/bin/degrib";

	@Before
	public void setUp() throws Exception {	
		degrib.setDegribDirectory(degribDirectory);
		degrib.setOutputDirectory(outputDirectory);
		degrib.setExecutable(executable);
		for(int i=1; i<5; i++)
			messages.add(i);
		variable.setName(variableName);
		variable.setMessages(messages);
		variable.setOutputName(variableOutputName);
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
		degrib.degribVariables();
		String[] extensions = {"csv"};
		Collection<File> result = FileUtils.listFiles(outputDirectory, extensions, false);
		int actualCount = result.size();
		//System.out.println("Count of csv files: "+actualCount);
		
		assertEquals(messages.size(),actualCount);		
		for(Integer m : messages){
			String currentFile = outputDirectory.getPath()+"/"+variableOutputName+m+".csv";
			System.out.println(currentFile);
			try {
				assertTrue(FileUtils.directoryContains(outputDirectory, new File(outputDirectory,variableOutputName+m+".csv")));
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
