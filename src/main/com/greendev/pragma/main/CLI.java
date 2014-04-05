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

		//Loading option corresponding to path to properties file
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

		String[] params = {"-properties","/Users/miguelgd/Desktop/goesProperties.json",
				"-start","2014/03/03","-end","2014/03/03"};

		//Create options object to receive params from command line
		Options options = createOptions();

		//Create the command line parser, to extract parameters
		CommandLineParser parser = new PosixParser();
		//Extract parameters from args into options
		CommandLine cli = parser.parse(options, params);
		final String dateFormat = "yyyy/MM/dd";

		DateTime start = null;
		DateTime end = null;

		//Start a stopwatch to measure execution time
		StopWatch time = new StopWatch();
		time.start();

		//If either properties, start or end parameters are missing, finish execution with error code
		if(!cli.hasOption("properties")){
			logger.error("Missing required parameter for properties file.");
			System.exit(1);
			//If no start or end day is specified, finish execution with error code
		}else if((!cli.hasOption("start") && !cli.hasOption("end"))){
			logger.error("Missing start date and end date.");
			System.exit(1);
			//If an end date is not specified and start date is not today, finish execution with error code
		}else if((cli.hasOption("start") && !cli.hasOption("end"))){
			System.out.println("inside has today not end");
			if(!cli.getOptionValue("start").equalsIgnoreCase("today")){
				logger.error("Missing end date when specifying range.");
				System.exit(1);
			}
			//If a end date is specified without a start date, finish execution with error code
		}else if((!cli.hasOption("start") && cli.hasOption("end"))){
			logger.error("Missing start date when specifying range.");
			System.exit(1);
		}

		System.out.println("inside last else");
		//Get path to properties file
		String properties = cli.getOptionValue("properties");

		//If start date parameter is "today" set the actual start and end dates to the current date
		if(cli.getOptionValue("start").equalsIgnoreCase("today")){
			System.out.println("in today conditional");
			start = new DateTime();
			end = start;
		}else{

			SimpleDateFormat dateFormatter = new SimpleDateFormat(dateFormat);
			String startOptionValue = cli.getOptionValue("start");
			String endOptionValue = cli.getOptionValue("end");
			start = new DateTime(dateFormatter.parse(startOptionValue));
			end = new DateTime(dateFormatter.parse(endOptionValue));

			//Error out if either start or end date is after today
			if(start.isAfterNow() || end.isAfterNow()){
				logger.error("Invalid dates: after current date (today)");
				System.exit(1);
			}

			//Error out if start date is after end date
			if(start.isAfter(end)){
				logger.error("Invalid dates: start date is after end date ");
				System.exit(1);
			}

		}

		LogMF.info(logger, "Going to work from {0} to {1}", start, end);

		AutomateGoes goes = new AutomateGoes(properties);
		process(start.minusDays(1), end.minusDays(1), new CompleteProcess(goes), goes);

		time.stop();
		logger.info("Finished in: "+time.toString());
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

