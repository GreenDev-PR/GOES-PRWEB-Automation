package main.com.greendev.pragma.main.process;

import main.com.greendev.pragma.main.AutomateGoes;

public abstract class GoesProcess implements Runnable {

	protected AutomateGoes goes;

	public GoesProcess(AutomateGoes goes){
		this.goes = goes;
	}
	
	@Override
	public abstract void run();

}
