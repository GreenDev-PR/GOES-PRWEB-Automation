package main.java.com.greendev.pragma.utils;

import java.util.Date;

/**
 * Provides utility functions to GoesAutomation class
 * @author josediaz
 */
public class GoesUtils {

	private static final int MILLIS = 1000;
	
	/**
	 * Converts seconds to milli seconds.
	 * @param seconds Value to convert.
	 * @return the value in milliseconds.
	 */
	public static long convertSecondsToMillis(long seconds){
		return MILLIS * seconds;
	}
	
	/**
	 * Provides formatting to given string. 
	 * @param str The string to be formatted.
	 * @param date The date include in the string.
	 * @return The formatted string. 
	 */
	public static String stringFormatTime(String str, Date date){
		str = str.replaceAll("%t","%1\\$t");
		return String.format(str, date);
	}

}
