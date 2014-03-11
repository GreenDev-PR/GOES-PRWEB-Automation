package main.com.greendev.pragma.email;

import java.io.File;
import java.net.MalformedURLException;

import main.com.greendev.pragma.properties.EmailProperties;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;
import org.apache.commons.mail.SimpleEmail;

/**
 * Sends an email with properties specified from {Email Properties}
 * 
 * To send an email, construct the object and select from one of the two methods to send an email.
 * @author josediaz
 *
 */
public class Emailer {

	private EmailProperties emailProperties;
	
	public Emailer(EmailProperties emailProperties){
		this.emailProperties = emailProperties;
	}

	/**
	 * 
	 * @param subject
	 * @param message
	 * @throws EmailException
	 */
	public void sendEmail(String subject, String message) throws EmailException{
		Email email = initializeEmail(new SimpleEmail(), subject);
		email.setMsg(message);
		email.send();
	}
	
	/**
	 * 
	 * @param email
	 * @param subject
	 * @return
	 * @throws EmailException
	 */
	private Email initializeEmail(Email email, String subject)throws EmailException{
		if(subject == null){
			subject = "";
		}
		
		email.setHostName(emailProperties.getHostname());
		email.setSmtpPort(emailProperties.getPort());
		email.setAuthenticator(new DefaultAuthenticator(emailProperties.getFrom(), emailProperties.getPassword()));
		email.setSSL(true);
		email.setTLS(true);
		email.setFrom(emailProperties.getFrom());
		email.setSubject(subject);
		email.addTo(emailProperties.getTo());
		return email;
		
	}
	
	/**
	 * 
	 * @param subject
	 * @param message
	 * @param file
	 * @throws EmailException
	 * @throws MalformedURLException
	 */
	public void sendEmailWithAttached(String subject, String message, File file) throws EmailException, MalformedURLException{
		MultiPartEmail email = new MultiPartEmail();
		initializeEmail(email,subject);
		email.setMsg(message);
		email.attach(file.toURI().toURL(), file.getName(), "goes-file");
		email.send();
	}
	
	/**
	 * Gets email properties 
	 * @return
	 */
	public EmailProperties getEmailPropertiess(){
		return this.emailProperties;
	}
	
	/**
	 * Sets the email's properties
	 * @param emailProperties
	 */
	public void setEmailProperties(EmailProperties emailProperties){
		this.emailProperties = emailProperties;
	}
	
}
