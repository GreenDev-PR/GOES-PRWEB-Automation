package main.com.greendev.pragma.main.process;

import org.apache.log4j.Logger;

import main.com.greendev.pragma.main.AutomateGoes;

/**
 * Goes process
 * @author josediaz
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
		try {
			goes.matlab();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("IOException while running Matlab");
			e.printStackTrace();
		}

		if(goes.waitForFinishedFile())
			goes.insertToDb();
	}
}
