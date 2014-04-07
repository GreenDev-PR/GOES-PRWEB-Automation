package main.java.com.greendev.pragma.main;

import java.io.File;

import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationObserver;

/**
 * Utility class to wait for a file to be created using the FileAlterationListener
 * When the file that is expected is found, a field is updated to reflect this
 * @author miguelgd
 *
 */

public class FileCreatedListener implements FileAlterationListener {

	private boolean foundFile = false;
	private String fileName;
	
	/**
	 * Constructs a FileCreatedListener with specified file
	 * @param filename The file to listen for.
	 */
	public FileCreatedListener(String filename){
		this.fileName = filename;
	};
	
	@Override
	public void onStart(FileAlterationObserver observer) {
	}

	@Override
	public void onDirectoryCreate(File directory) {
	}

	@Override
	public void onDirectoryChange(File directory) {
	}

	@Override
	public void onDirectoryDelete(File directory) {
	}

	@Override
	/**
	 * Implmentation of the interface method to configure the action
	 * that will be triggered when a file is created. Checks if the file created
	 * is the expected flag file to signal matlab execution completed.
	 */
	public void onFileCreate(File file) {
		if(file.getName().equals(fileName))
			foundFile = true;
	}

	@Override
	public void onFileChange(File file) {
	}

	@Override
	public void onFileDelete(File file) {
	}

	@Override
	public void onStop(FileAlterationObserver observer) {
	}
	
	/**
	 * Returns true if the flag file has been created, false otherwise
	 * @return boolean for flag file created
	 */
	public boolean isFileFound(){
		return foundFile;
	}

}
