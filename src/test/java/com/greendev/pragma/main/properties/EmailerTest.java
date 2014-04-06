package test.java.com.greendev.pragma.main.properties;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.net.MalformedURLException;

import main.java.com.greendev.pragma.email.Emailer;
import main.java.com.greendev.pragma.main.properties.EmailProperties;
import main.java.com.greendev.pragma.main.properties.GoesProperties;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.gson.Gson;

/**
 * Tests sending an email both with and without attachments.
 * This test requires complete key value pairs for email object in configuration JSON.
 * @author josediaz
 *
 */
public class EmailerTest {

	private static final String CONFIGURATION_PATH = "src/test/com/greendev/pragma/main/properties/goesProperties.json";
	private static GoesProperties goesProp;
	private static EmailProperties emailProp;
	private static final String ATTACHMENT = CONFIGURATION_PATH;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Gson gson = new Gson();
		Reader reader = new FileReader(new File(CONFIGURATION_PATH));
		goesProp = gson.fromJson(reader, GoesProperties.class);
		emailProp = goesProp.getEmail();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void sendSimpleEmailtest() throws EmailException, MalformedURLException {
		boolean sent = false;
		Emailer emailer = new Emailer(emailProp);
		emailer.sendEmail("Dummy Subject", "Testing email without attachement");
		System.out.println("Sending email...");
		sent = true;
		assertTrue(sent);
	}
	
	@Test
	public void sendAttachmentEmailTest() throws MalformedURLException, EmailException{
		boolean sent = false;
		Emailer emailer = new Emailer(emailProp);
		emailer.sendEmailWithAttachment("Dummy Subject2", "Dummy body", new File(ATTACHMENT));
		System.out.println("Sending email...");
		sent = true;
		assertTrue(sent);
	}

}
