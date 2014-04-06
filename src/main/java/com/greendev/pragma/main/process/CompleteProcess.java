package main.java.com.greendev.pragma.main.process;

import org.apache.log4j.Logger;

import main.java.com.greendev.pragma.main.AutomateGoes;

/**
 * Goes process, extends GoesProcess which implements runnable interface
 * @author miguelgd
 *
 */
public class CompleteProcess extends GoesProcess {

	/**
	 * Creates an instance of the complete GOES-PRWEB Automation process
	 * @param goes instance of automate goes with implementation for each process stage
	 */
	public CompleteProcess(AutomateGoes goes){
		super(goes);
	}

	@Override
	/**
	 * Implementation of the Runnable interface method run()
	 * Calls the AutomateGoes for each stage of the process
	 * Includes making directories, downloading data, transforming data,
	 * executing matlab and inserting to the database when matlab is finished
	 */
	public void run() {

		final Logger logger = Logger
				.getLogger(CompleteProcess.class);

		goes.makeDirs();
		goes.download();
		goes.degrib();
		goes.matlab();
		if(goes.waitForFinishedFile())
			goes.insertToDb();
	}
}
