package main.com.greendev.pragma.main;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import main.com.greendev.pragma.main.process.CompleteProcess;
import main.com.greendev.pragma.main.process.GoesProcess;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.commons.mail.EmailException;
import org.apache.log4j.LogMF;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;

/**
 * Command line interface for the GOES-PRWEB automation
 * Takes in the following parameter for execution:
 * @author miguelgd
 *
 */

public class CLI {

	private static final Logger logger = Logger.getLogger(CLI.class);

	@SuppressWarnings("static-access")
	private static Options createOptions(){
		Options options = new Options();
		
		//Loading option corresponding to properties file
		Option properties = OptionBuilder.withArgName("propertiesFile")
				.hasArg().withDescription("Properties file **required").create("properties");
		
		//Loading option corresponding to start date
		Option start = OptionBuilder.withArgName("startDate")
				.hasArg().withDescription("specify the start date as yyyy/MM/dd").create("start");
		
		//Loading option corresponding to end date
		Option end = OptionBuilder.withArgName("endDate")
				.hasArg().withDescription("specify the end date as yyyy/MM/dd").create("end");
		
		options.addOption(properties);
		options.addOption(start);
		options.addOption(end);
		
		return options;
	}

	public static void main(String[] args)
			throws ParseException, IOException, org.apache.commons.cli.ParseException, EmailException {

		//Create options object to receive params from command line
		Options options = createOptions();
		
		//Create the command line parser, to extract parameters
		CommandLineParser parser = new PosixParser();
		//Extract parameters from args into options
		CommandLine cli = parser.parse(options, args);
		final String dateFormat = "yyyy/MM/dd";

		//Start a stopwatch to measure execution time
		StopWatch time = new StopWatch();
		time.start();
		
		//If either properties, start or end parameters are missing, finish execution with error code
		if(!cli.hasOption("properties") || !cli.hasOption("start") || !cli.hasOption("end")){
			logger.error("Missing required parameters: properties, start or end");
			System.exit(1);
		}else{
			DateTime start = null;
			DateTime end = null;
			String properties = cli.getOptionValue("properties");
			SimpleDateFormat dateFormatter = new SimpleDateFormat(dateFormat);
			String startOptionValue = cli.getOptionValue("start");

			if(startOptionValue.equalsIgnoreCase("today")){
				start = new DateTime(dateFormatter.parse(startOptionValue));
				end = new DateTime(dateFormatter.parse(cli.getOptionValue("end")));
			}

			LogMF.info(logger, "Going to work from {0} to {1}", start, end);

			AutomateGoes goes = new AutomateGoes(properties);
			process(start, end, new CompleteProcess(goes), goes);

			time.stop();
			logger.info("Finished in: "+time.toString());
		}
	}

	/**
	 * Starts the specified GoesProcess
	 * @param start start date for execution
	 * @param end end date for execution
	 * @param proc GoesProcess to execute (e.g. CompleteProcess)
	 * @param goes Instance of AutomateGoes
	 */
	private static void process(DateTime start, DateTime end, GoesProcess proc, AutomateGoes goes){
		while(!start.isAfter(end)){	
			logger.info("Working for date: "+start);
			goes.setDate(start);
			proc.run();
			start = start.plusDays(1);
			logger.info("Finished working for date: " + start);
		}
	}
}

