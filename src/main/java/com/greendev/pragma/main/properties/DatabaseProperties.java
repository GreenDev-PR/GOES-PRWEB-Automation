package main.java.com.greendev.pragma.main.properties;

/**
 * Houses the properties necessary to create the database connection
 * @author miguelgd
 */
public class DatabaseProperties {
	
	private String username;
	private String password;
	private String host;
	private int port;
	private String database;
	
	/**
	 * Constructs a database properties object
	 */
	public DatabaseProperties(){
		super();
	}
	/**
	 * Gets the username of the database properties
	 * @return The string representation of the username
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * Sets the username of the database properties
	 * @param username The string representation of the username
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * Gets the password of the database properties
	 * @return The string representation of the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * Sets the password of the database properties
	 * @param password The string representaton of the password
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * Gets the host's address
	 * @return The string representation of the host address
	 */
	public String getHost() {
		return host;
	}
	/**
	 * Sets the host of the database properties
	 * @param host The string representation of the database properties
	 */
	public void setHost(String host) {
		this.host = host;
	}
	/**
	 * Gets the port the database is running at.
	 * @return Numeric representation of the port where the database is running
	 */
	public int getPort() {
		return port;
	}
	/**
	 * Sets the port address where the database is running
	 * @param port The numeric representation of the port address 
	 */
	public void setPort(int port) {
		this.port = port;
	}
	/**
	 * Gets the name of the database
	 * @return The name of the database
	 */
	public String getDatabase() {
		return database;
	}
	/**
	 * Sets the name of the database
	 * @param database The name of the database
	 */
	public void setDatabase(String database) {
		this.database = database;
	}
}
