package main.java.com.greendev.pragma.main.process;

import main.java.com.greendev.pragma.main.AutomateGoes;

/**
 * GoesProcess abstract class
 * @author miguelgd
 *
 */
public abstract class GoesProcess implements Runnable {

	protected AutomateGoes goes;

	/**
	 * Creates an instance of the GoesProcess
	 * @param goes instance of AutomateGoes with implemented methods for each process stage
	 */
	public GoesProcess(AutomateGoes goes){
		this.goes = goes;
	}
	
	@Override
	/**
	 * Run method from the runnable interface. Implemented in the complete process class. 
	 */
	public abstract void run();

}
