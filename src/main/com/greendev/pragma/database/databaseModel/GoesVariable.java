package main.com.greendev.pragma.database.databaseModel;

import java.util.Date;

/**
 * Single GOES-PRWEB variable (e.g. rainfall, ET, wind)
 * Includes the variable name, matrix of values corresponding to PR coordinates,
 * date, and the path to the map image
 * @author miguelgd
 */
public class GoesVariable {

	private String name;
	private double[][] values;
	private Date date;
	private String imagePath;
		
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double[][] getValues() {
		return values;
	}
	public void setValues(double[][] values) {
		this.values = values;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	
}
