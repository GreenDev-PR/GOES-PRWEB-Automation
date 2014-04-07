package main.java.com.greendev.pragma.main;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.joda.time.DateTime;

/**
 * Utility class that creates directories needed for program execution
 * @author miguelgd
 *
 */
public class DirectoryManager {

	private File rootDirectory;
	private static String DIRECTORY_FORMAT = "%tY/%tm/%td/";

	/** 
	 * Creates an instance of the DirectoryManager
	 * creates the root (day) directory, INPUT, OUPUT and LOG directories
	 * @param rootDir root directory where the folder structure YYYY/MM/DD will be created
	 */
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

	/**
	 * Returns the log directory
	 * @return file object with the path to the LOGS directory
	 */
	public File getLogDirectory() {
		return new File(getRootDirectory(), "/LOGS");
	}

	/**
	 * Returns the root directory (day)
	 * @return Root directory for the given day
	 */
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
	/**
	 * Creates an archive folder with the name Archive+Timestamp
	 * if there was data already in the date's directory
	 * @param date date for which algorithm is running
	 * @return true if an archive was created, false otherwise
	 * @throws IOException
	 */
	public boolean archiveDataForCurrentDate(DateTime date) throws IOException{
		File currentDir = getDirectory(date);
		DateTime currentDate = new DateTime();
		boolean createArchive = currentDir.list().length > 0;
		if(createArchive){	
			File newArchive = new File(currentDir,"Archive "+currentDate.getMillis());
			FileUtils.copyDirectory(currentDir, newArchive);
			String[] toDelete = newArchive.list(new WildcardFileFilter("Archive*"));
			for(int i = 0; i < toDelete.length; i++){
				FileUtils.deleteQuietly(new File(newArchive,toDelete[i]));
			}
		}
		
		return createArchive;
		
	}
	
	/**
	 * Cleans up the input and output directory
	 * Can be used if matlab execution is not successful
	 * @param date
	 */
	public void cleanUp(DateTime date) throws IOException{
		File input = getInputDirectory(date);
		File output = this.getOutputDirectory(date);	
		FileUtils.cleanDirectory(input);
		FileUtils.cleanDirectory(output);
		
	}
}

