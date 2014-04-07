package main.java.com.greendev.pragma.main.properties;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.io.IOUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Creates a goesProperties.json file using a GoesProperties object
 * This properties file can be modified to reflect actual system
 * configuration. This file is consumed by AutomateGoes (loadProperties).
 * @author miguelgd
 */
public class GoesPropertiesToJson {
	
	public static void main(String[] args) throws IOException{
		
		GoesProperties properties = new GoesProperties();
		properties.createGoesProperties();
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.setPrettyPrinting().serializeNulls().create();
		
		File output = new File("src/test/com/greendev/pragma/main/properties/goesProperties.json");
		FileWriter writer = new FileWriter(output);
		gson.toJson(properties,writer);
		IOUtils.closeQuietly(writer);
		
	}
	
	
}
