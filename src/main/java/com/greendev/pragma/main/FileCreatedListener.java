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
	
	public FileCreatedListener(String filename){
		this.fileName = filename;
	};
	
	@Override
	public void onStart(FileAlterationObserver observer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDirectoryCreate(File directory) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDirectoryChange(File directory) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDirectoryDelete(File directory) {
		// TODO Auto-generated method stub

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
		// TODO Auto-generated method stub

	}

	@Override
	public void onFileDelete(File file) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStop(FileAlterationObserver observer) {
		// TODO Auto-generated method stub

	}
	
	/**
	 * Returns true if the flag file has been created, false otherwise
	 * @return boolean for flag file created
	 */
	public boolean isFileFound(){
		return foundFile;
	}

}
