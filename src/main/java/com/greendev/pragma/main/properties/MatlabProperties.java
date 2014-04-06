package main.java.com.greendev.pragma.main.properties;

public class MatlabProperties {
	
	private String matlabCmd;
	private String matlabWorkingDirectory;
	private int retryAttempts;
	
	public MatlabProperties() {
		super();
	}
	
	public String getMatlabCmd() {
		return matlabCmd;
	}
	public void setMatlabCmd(String matlabCmd) {
		this.matlabCmd = matlabCmd;
	}
	public String getMatlabWorkingDirectory() {
		return matlabWorkingDirectory;
	}
	public void setMatlabWorkingDirectory(String matlabWorkingDirectory) {
		this.matlabWorkingDirectory = matlabWorkingDirectory;
	}
	public int getRetryAttempts() {
		return retryAttempts;
	}
	public void setRetryAttempts(int retryAttempts) {
		this.retryAttempts = retryAttempts;
	}

	@Override
	public String toString() {
		return "MatlabProperties [matlabCmd=" + matlabCmd
				+ ", matlabWorkingDirectory=" + matlabWorkingDirectory
				+ ", retryAttempts=" + retryAttempts + "]";
	}
	
	
}
