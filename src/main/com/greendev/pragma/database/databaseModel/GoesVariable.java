package main.com.greendev.pragma.database.databaseModel;

/**
 * Single GOES-PRWEB variable (e.g. rainfall, ET, wind)
 * Includes the variable name, matrix of values corresponding to PR coordinates,
 * date, and the path to the map image
 * @author miguelgd
 */
public class GoesVariable {

	private String name;
	private Double[][] values;
	String date;
	private String imagePath;
		
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Double[][] getValues() {
		return values;
	}
	public void setValues(Double[][] values2) {
		this.values = values2;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	
}
