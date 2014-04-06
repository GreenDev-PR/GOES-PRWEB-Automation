package main.java.com.greendev.pragma.degrib;

import java.util.List;

public class DegribVariable {

	/**
	 * Container class for a variable inside a file in grib format
	 * @author miguelgd
	 */
	
	/**
	 * Name of the variable
	 */
	private String name;
	private String outputName;
	
	/**
	 * Error messages for a given variable
	 */
	private List<Integer> messages;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOutputName() {
		return outputName;
	}
	public void setOutputName(String outputName) {
		this.outputName = outputName;
	}
	public List<Integer> getMessages() {
		return messages;
	}
	public void setMessages(List<Integer> messages) {
		this.messages = messages;
	}
	@Override
	public String toString() {
		return "DegribVariable [name=" + name + ", outputName=" + outputName
				+ ", messages=" + messages + "]";
	}
	
	
	
}
