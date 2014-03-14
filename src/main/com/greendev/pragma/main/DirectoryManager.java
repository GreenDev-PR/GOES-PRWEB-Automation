package main.com.greendev.pragma.main;

import java.io.File;

import org.joda.time.DateTime;

/**
 * Utility class that creates directories needed for program execution
 * @author miguelgd
 *
 */
public class DirectoryManager {

	private File rootDirectory;
	private static String DIRECTORY_FORMAT = "%tY/%tm/%td/";

	public DirectoryManager(String rootDir) {
		rootDirectory = new File(rootDir);
		create(rootDirectory);
		create(getLogDirectory());
	}

	/**
	 * Creates root, output and input directory for given date
	 * @param date
	 * @return returns the root directory
	 */
	public File createDirectoriesForDateTime(DateTime date){
		File dir = getDirectory(date);
		getInputDirectory(date);
		getOutputDirectory(date);
		return dir;	
	}	

	/**
	 * Creates output directory for given date
	 * @param date
	 * @return returns the output directory
	 */
	public File getOutputDirectory(DateTime date) {
		File output = new File(getDirectory(date), "/OUTPUT");
		create(output);
		return output;
	}

	/**
	 * Creates input directory for given date
	 * @param date
	 * @return returns the input directory
	 */
	public File getInputDirectory(DateTime date) {
		File dir = getDirectory(date);
		File input = new File(dir, "/INPUT");
		create(input);
		return input;

	}

	public File getLogDirectory() {
		return new File(getRootDirectory(), "/LOGS");
	}

	public File getRootDirectory() {
		return rootDirectory;
	}

	/**
	 * Private utility method used by others to create directories as needed
	 * @param dir
	 */
	private void create(File dir) {
		if(!dir.exists())
			dir.mkdirs();	
	}

	/**
	 * Creates root directory
	 * @param date
	 * @return returns the root directory based on date
	 */
	public File getDirectory(DateTime date) {
		String str = DIRECTORY_FORMAT.replaceAll("%t", "%1\\$t");
		String formatted =  String.format(str, date.toDate());
		File dir = new File(rootDirectory,formatted);
		create(dir);
		return dir;
	}
}

