package main.java.com.greendev.pragma.degrib;

import java.util.List;

/**
 * Container class for a variable inside a file in grib format
 * @author miguelgd
 */
public class DegribVariable {

	/**
	 * Name of the variable
	 */
	private String name;
	private String outputName;
	
	/**
	 * Messages to degrib for a given variable
	 */
	private List<Integer> messages;
	
	/**
	 * Returns the name of the variable to degrib
	 * @return variable name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Configures the name of the variable to degrib
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Returns the output to give the variable when saving it, after degribing
	 * @return output name of the variable
	 */
	public String getOutputName() {
		return outputName;
	}
	
	/**
	 * Configure the output to give the variable when saving it, after degribing
	 * @param outputName
	 */
	public void setOutputName(String outputName) {
		this.outputName = outputName;
	}
	
	/**
	 * Returns the list of messages to degrib for the given variable
	 * @return list of integers corresponding to messages to degrib
	 */
	public List<Integer> getMessages() {
		return messages;
	}
	
	/**
	 * Configures the list of messages to degrib for the given variable
	 * @param messages
	 */
	public void setMessages(List<Integer> messages) {
		this.messages = messages;
	}
	
	@Override
	public String toString() {
		return "DegribVariable [name=" + name + ", outputName=" + outputName
				+ ", messages=" + messages + "]";
	}
	
	
	
}
