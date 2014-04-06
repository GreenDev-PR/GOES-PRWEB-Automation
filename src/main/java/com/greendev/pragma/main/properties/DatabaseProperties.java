package main.java.com.greendev.pragma.main.properties;

/**
 * Houses the properties necessary to create the database connection
 * @author miguelgd
 *
 */

public class DatabaseProperties {
	
	private String username;
	private String password;
	private String host;
	private int port;
	private String database;
	
	public DatabaseProperties(){
		super();
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getDatabase() {
		return database;
	}
	public void setDatabase(String database) {
		this.database = database;
	}
}
