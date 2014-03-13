package main.com.greendev.pragma.main.properties;


/**
 * Container class for the ftp download properties
 * @author miguelgd
 *
 */
public class FtpProperties {
	
	private String host;
	private String user;
	private String password;
	private int port;
	private String rootDir;
	
	public FtpProperties(){
	
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getRootDir() {
		return rootDir;
	}

	public void setRootDir(String rootDir) {
		this.rootDir = rootDir;
	}
	
	

}
