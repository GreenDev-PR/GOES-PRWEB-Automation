package main.com.greendev.pragma.utils;

import java.util.Date;

public class GoesUtils {

	private static final int MILLIS = 1000;
	
	public static long convertSecondsToMillis(long seconds){
		return MILLIS * seconds;
	}
	
	public static String stringformatTime(String str, Date date){
		str = str.replaceAll("%t","%1\\$t");
		return String.format(str, date);
	}

}
