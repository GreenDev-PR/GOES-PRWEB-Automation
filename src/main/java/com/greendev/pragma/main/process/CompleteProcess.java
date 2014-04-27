package main.java.com.greendev.pragma.main.process;

import java.io.IOException;

import org.apache.commons.mail.EmailException;
import org.apache.log4j.LogMF;
import org.apache.log4j.Logger;

import main.java.com.greendev.pragma.main.AutomateGoes;

/**
 * Goes process, extends GoesProcess which implements runnable interface
 * @author miguelgd
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
//		LogMF.info(logger,"Making directory structure. ","SYSTEM");
//		goes.makeDirs();
//		LogMF.info(logger,"Downloading hydro-climate data. ","SYSTEM");
//		goes.download();
//		LogMF.info(logger,"Degribbing hydro-climate data. ","SYSTEM");
//		goes.degrib();
		LogMF.info(logger,"Executing MATLAB GOES-PRWEB algorithm ","SYSTEM");
		goes.matlab();
		LogMF.info(logger,"Storing OUTPUT data to database ","SYSTEM");
		if(goes.waitForFinishedFile())
			goes.insertToDb();
		try {
			LogMF.info(logger,"Emaling LOG file ","SYSTEM");
			goes.emailLog();
		} catch (IOException e) {
			logger.error("Could not locate log directory ", e);
		} catch (EmailException e) {
			logger.error("Could not send log file via email", e);
		}
	}
}
