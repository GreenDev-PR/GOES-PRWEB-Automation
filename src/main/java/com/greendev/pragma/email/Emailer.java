package main.java.com.greendev.pragma.email;

import java.io.File;
import java.net.MalformedURLException;

import main.java.com.greendev.pragma.main.properties.EmailProperties;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;
import org.apache.commons.mail.SimpleEmail;

/**
 * Sends an email with properties specified from {Email Properties}
 * To send an email, construct the object and select from one of the two methods to send an email.
 * @author josediaz
 */
public class Emailer {

	/**
	 *Email properties, supplies important configuration options.
	 */
	private EmailProperties emailProperties;
	
	/**
	 * Constructs an Emailer object with specified email properties. 
	 * @param emailProperties The email properties to apply.
	 */
	public Emailer(EmailProperties emailProperties){
		this.emailProperties = emailProperties;
	}

	/**
	 * Send an email with the supplied subject and message.
	 * @param subject The email's subject.
	 * @param message The body of the email message.
	 * @throws EmailException Error sending the email.
	 */
	public void sendEmail(String subject, String message) throws EmailException{
		Email email = initializeEmail(new SimpleEmail(), subject);
		email.setMsg(message);
		email.send();
	}
	
	/**
	 * Initializes an email with supplied properties.
	 * @param email The email object to extract all properties from e.g. hostname, port etc.
	 * @param subject The email's subject.
	 * @return an email object with specified properties from the emailProperties class.
	 * @throws EmailException Error creating the email.
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
	 * Send an email with an attachment.
	 * @param subject The email's subject.
	 * @param message The text of the body in the email.
	 * @param file Desired file attachment.
	 * @throws EmailException Error sending email.
	 * @throws MalformedURLException Error creating the URL.
	 */
	public void sendEmailWithAttachment(String subject, String message, File file) throws EmailException, MalformedURLException{
		MultiPartEmail email = new MultiPartEmail();
		this.initializeEmail(email,subject);
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
