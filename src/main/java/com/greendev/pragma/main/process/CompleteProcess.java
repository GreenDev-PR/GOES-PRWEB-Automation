package main.java.com.greendev.pragma.main.process;

import org.apache.log4j.Logger;

import main.java.com.greendev.pragma.main.AutomateGoes;

/**
 * Goes process
 * @author miguelgd
 *
 */
public class CompleteProcess extends GoesProcess {

	public CompleteProcess(AutomateGoes goes){
		super(goes);
	}

	@Override
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
