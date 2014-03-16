package test.com.greendev.pragma.main;

import static org.junit.Assert.*;

import java.io.IOException;
import java.text.ParseException;

import main.com.greendev.pragma.main.CLI;

import org.apache.commons.mail.EmailException;
import org.junit.Before;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;

public class CLITest {
	
	private final ExpectedSystemExit exit = ExpectedSystemExit.none();

	@Before
	public void setUp() throws Exception {
				
	}

	@Test
	public void testMisingProperties() {
		try {
			exit.expectSystemExitWithStatus(1);
			CLI.main(null);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (org.apache.commons.cli.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (EmailException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
//	@Test
//	public void testMisingStart() {
//		fail("Not yet implemented");
//	}
//	
//	@Test
//	public void testMisingEnd() {
//		fail("Not yet implemented");
//	}
//	
//	@Test
//	public void testProcessStart() {
//		fail("Not yet implemented");
//	}

}
