package test.java.com.greendev.pragma.main;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import main.java.com.greendev.pragma.main.FileCreatedListener;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.junit.Before;
import org.junit.Test;

/**
 * Test listener for a given file
 * Used to determine when flag file is created, indicating
 * that Matlab has finished executing
 * @author miguelgd
 */


public class FileCreatedListenerTest {

	private FileAlterationObserver observer;
	private FileCreatedListener listener;
	private static final File WATCH_DIRECTORY = new File("src/test/java/com/greendev/pragma/main/resources/");
	private static final String FINISH_FILE = "finish.txt";
	private static final String FINISH_FILE_INCORRECT = "finish2.txt";

	@Before
	public void setUp() throws Exception {
		observer = new FileAlterationObserver(WATCH_DIRECTORY);
		listener = new FileCreatedListener(FINISH_FILE);
		observer.addListener(listener);
	}

	@Test
	public void testFileCreatedListenerIncorrect() {
		observer.checkAndNotify();
		File file = new File(WATCH_DIRECTORY, FINISH_FILE_INCORRECT);
		try {
			FileUtils.touch(file);
		} catch (IOException e) {
			System.out.println("Could not create file");
		}		
		observer.checkAndNotify();
		assertFalse(listener.isFileFound());
		FileUtils.deleteQuietly(file);
	}
	
	@Test
	public void testFileCreatedListenerCorrect() {
		observer.checkAndNotify();
		File file = new File(WATCH_DIRECTORY, FINISH_FILE);
		try {
			FileUtils.touch(file);
		} catch (IOException e) {
			System.out.println("Could not create file");
		}		
		observer.checkAndNotify();
		assertTrue(listener.isFileFound());
		FileUtils.deleteQuietly(file);
	}


}
