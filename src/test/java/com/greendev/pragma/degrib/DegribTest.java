//TODO: Implement test to validate that Grib files are returned for a given variable and that the files are read using Degrib executable

package test.java.com.greendev.pragma.degrib;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileReader;
import java.util.Collection;
import java.util.List;

import main.java.com.greendev.pragma.degrib.DegribVariable;
import main.java.com.greendev.pragma.degrib.Degribber;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;

public class DegribTest {

	Degribber degrib;
	private File degribFile = new File("/Users/miguelgd/Desktop/degribber.json");
	private File workingDirectory = new File("/");
	private List<DegribVariable> variables;

	@Before
	public void setUp() throws Exception {
		Gson gson = new Gson();
		degrib = gson.fromJson(new FileReader(degribFile), Degribber.class);
		System.out.println(degrib.toString());
		variables = degrib.getVariables();
		degrib.setDegribDirectory(workingDirectory);
		degrib.setOutputDirectory(workingDirectory);		
	}


	@Test
	public void testFilesReturned(){

	}
	
	@Test
	public void testDegribVariables(){

	}
	

}
