package main.com.greendev.pragma.main.properties;
/**
 * Email properties
 * @author josediaz
 *
 */
public class EmailProperties {

	private String from;
	private String to;
	private String password;
	private String hostname;
	private int port;

	public EmailProperties() {
		super();
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
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

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public static void main(String[] args) {

	}

}

