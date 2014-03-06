package main.java.com.greendev.pragma.degrib;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteStreamHandler;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.log4j.LogMF;
import org.apache.log4j.Logger;

public class Degribber {

/**
 * Degribs Grib files using provided executable.
 * Requires the specification of the input and output directories.
 * 
 * @author miguelgd	
 */
	
	private List<DegribVariable> variables;
	private String executeLocation;
	private File outputDirectory;
	private File degribDirectory;
	private Map map = new HashMap();

	private static Logger logger = Logger.getLogger(Degribber.class);

	private static final String INPUT_FILE = "inputFile",
			OUTPUT_FILE = "outputFile",
			MESSAGE = "message";

	/**
	 * Array of arguments for the command line. Input, output and message
	 * are expanded (substituted) at runtime. 
	 */
	private static String[] ARGUMENTS= {
		"${inputFile}", "-out", "${outputFile}"
		,"-C","-msg","${message}","-Csv","-Unit", "m", "-Decimal", "2"
	};

	public Degribber(){

	}

	public List<DegribVariable> getVariables() {
		return variables;
	}

	public void setVariables(List<DegribVariable> variables) {
		this.variables = variables;
	}

	public File getOutputDirectory() {
		return outputDirectory;
	}

	public void setOutputDirectory(File outputDirectory) {
		this.outputDirectory = outputDirectory;
	}

	public File getDegribDirectory() {
		return degribDirectory;
	}

	public void setDegribDirectory(File degribDirectory) {
		this.degribDirectory = degribDirectory;
	}

	public void degribVariables(){		
		for(DegribVariable variable : variables)
			degrib(variable);	
	}

	/**
	 * Gets files for the given variable and iterates through them.
	 * Calls degribFile with the current file, the message and the variable.
	 * @param variable variable to degrib from file
	 */
	private void degrib(DegribVariable variable) {
		Collection<File> gribFiles = getGribFiles(variable);
		for(File file : gribFiles){
			for(Integer message: variable.getMessages()){
				int returnCode;
				try{
					returnCode = degribFile(file, message, variable);
					if(returnCode != 0)
							logger.error("Error degribbing file, error code not 0 for file: " + file.getAbsolutePath());
				} catch (IOException e){
					logger.error("IOException while degribing file: " + file.getAbsolutePath(),e);
				}
			}
		}
	}

	/**
	 * Degribs a single file for a given variable using the executable specified.
	 * @param inputFile file to degrib
	 * @param message error message
	 * @param variable
	 * @return exit code from the command execution, for error detection
	 * @throws ExecuteException
	 * @throws IOException
	 */
	private int degribFile(File inputFile, Integer message, DegribVariable variable) throws ExecuteException, IOException{
		map.put(INPUT_FILE, inputFile);
		map.put(MESSAGE, message);
		String output = variable.getOutputName();
		output += message;
		File outputFile = new File(outputDirectory, output);
		map.put(OUTPUT_FILE, outputFile);
		
		//Creates the command line to execute GRIB executable
		CommandLine cmd = new CommandLine(executeLocation);
		//Passes arguments array to the command line
		cmd.addArguments(ARGUMENTS);
		//Expands input, message and output arguments in the command line
		cmd.setSubstitutionMap(map);
		logger.debug(cmd.toString());
		DefaultExecutor executor = new DefaultExecutor();
		final byte[] buffer = new byte[1024];
		ByteArrayInputStream inStream = new ByteArrayInputStream(buffer);
		ExecuteStreamHandler streamHandler = executor.getStreamHandler();
		streamHandler.setProcessOutputStream(inStream);
		executor.setStreamHandler(streamHandler);
		int exitCode = executor.execute(cmd);
		
		LogMF.info(logger, "Degrib output {0}", IOUtils.toString(inStream));
		IOUtils.closeQuietly(inStream);
		return exitCode;
		
	}

	/**
	 * Gets the GRIB files for the specified variables
	 * @param variable name of variable to search for
	 * @return collection of files that match the given variable name
	 */
	private Collection<File> getGribFiles(DegribVariable variable) {
		return FileUtils.listFiles(degribDirectory, new WildcardFileFilter(variable.getName()), null);
	};






}
