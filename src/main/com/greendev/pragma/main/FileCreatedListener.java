package main.com.greendev.pragma.main;

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

	public boolean foundFile = false;
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
	
	public boolean isFileFound(){
		return foundFile;
	}

}
