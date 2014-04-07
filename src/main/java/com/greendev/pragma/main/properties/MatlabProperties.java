package main.java.com.greendev.pragma.main.properties;
/**
 * This class is the object representation of the Matlab properties 
 * @author miguelgd
 */
public class MatlabProperties {
	
	private String matlabCmd;
	private String matlabWorkingDirectory;
	private int retryAttempts;
	
	/**
	 * Constructs an empty Matlab Properties object
	 */
	public MatlabProperties() {
		super();
	}
	/**
	 * Gets the string representation of the Matlab command to run
	 * @return The string representation of the Matlab command to run
	 */
	public String getMatlabCmd() {
		return matlabCmd;
	}
	/**
	 * Sets the Matlab command to run
	 * @param matlabCmd Matlab command to run  
	 */
	public void setMatlabCmd(String matlabCmd) {
		this.matlabCmd = matlabCmd;
	}
	/**
	 * Gets the Matlab working directory
	 * @return The name of the Matlab working directory.
	 */
	public String getMatlabWorkingDirectory() {
		return matlabWorkingDirectory;
	}
	/**
	 * Sets the name of Matlab working directory
	 * @param matlabWorkingDirectory The name of the Matlab working directory 
	 */
	public void setMatlabWorkingDirectory(String matlabWorkingDirectory) {
		this.matlabWorkingDirectory = matlabWorkingDirectory;
	}
	/**
	 * Gets the Matlab retry attempts.
	 * @return The number of attempts to retry Matlab 
	 */
	public int getRetryAttempts() {
		return retryAttempts;
	}
	/**
	 * Set Matlab retry attempts
	 * @param retryAttempts The number of times to attempt running matlab
	 */
	public void setRetryAttempts(int retryAttempts) {
		this.retryAttempts = retryAttempts;
	}
	
	/**
	 * String representation of a MatlabProperties object.
	 */
	@Override
	public String toString() {
		return "MatlabProperties [matlabCmd=" + matlabCmd
				+ ", matlabWorkingDirectory=" + matlabWorkingDirectory
				+ ", retryAttempts=" + retryAttempts + "]";
	}
	
	
}
