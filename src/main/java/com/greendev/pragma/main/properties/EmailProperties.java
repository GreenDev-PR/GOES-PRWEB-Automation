package main.java.com.greendev.pragma.main.properties;
/**
 * This class is represents email properties.
 * @author josediaz
 */
public class EmailProperties {

	private String from;
	private String to;
	private String password;
	private String hostname;
	private int port;
	
	/**
	 * Constructs an email property object
	 */
	public EmailProperties() {
	}
	/**
	 * Gets the senders email address
	 * @return The senders email address
	 */
	public String getFrom() {
		return from;
	}
	/**
	 * Sets the sender of the email
	 * @param from The sender email address
	 */
	public void setFrom(String from) {
		this.from = from;
	}
	/**
	 * Gets the destination email
	 * @return The destination email
	 */
	public String getTo() {
		return to;
	}
	/**
	 * Sets the destination email
	 * @param to The destination email
	 */
	public void setTo(String to) {
		this.to = to;
	}
	/**
	 * Gets the sender's email password
	 * @return The sender's email password
	 */
	public String getPassword() {
		return password;
	}
	/** 
	 * Sets the sender's email password
	 * @param password The sender's email password
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * Gets the port of the email server
	 * @return The port of the email server
	 */
	public int getPort() {
		return port;
	}
	/**
	 * Sets the port of the email server
	 * @param port The email server port number
	 */
	public void setPort(int port) {
		this.port = port;
	}
	/**
	 * Gets the host's name
	 * @return The string representation of the host name
	 */
	public String getHostname() {
		return hostname;
	}
	/**
	 * Sets the host's name 
	 * @param hostname The string representation of the host name.
	 */
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

}

