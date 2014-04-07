package main.java.com.greendev.pragma.database.databaseModel;

import org.joda.time.DateTime;

/**
 * Single GOES-PRWEB map image path container
 * Includes variable name, date and map image path
 * @author miguelgd
 */
public class GoesMap{

	private String variableName;
	private DateTime dataDate;
	private String imagePath;
	
	/**
	 * Creates an instance of the model object GoesMap
	 * @param variableName name of the GOES-PRWEB variable that the map is for
	 * @param dataDate date of the data used to generate the map
	 * @param imagePath absolute path to the local or remote directory where the image is stored
	 */
	public GoesMap(String variableName, DateTime dataDate, String imagePath) {
		this.variableName = variableName;
		this.dataDate = dataDate;
		this.imagePath = imagePath;
	}

	/**
	 * Returns the name of the configured GOES-PRWEB variable
	 * @return variable name
	 */
	public String getVariableName() {
		return variableName;
	}

	/**
	 * Sets the name of the configured GOES-PRWEB variable
	 * @param variableName Name of variable corresponding to the map
	 */
	public void setVariableName(String variableName) {
		this.variableName = variableName;
	}

	/**
	 * Returns the date configured for the map image
	 * @return date of the map image data
	 */
	public DateTime getDataDate() {
		return dataDate;
	}

	/**
	 * Sets the date for the map image
	 * @param dataDate
	 */
	public void setDataDate(DateTime dataDate) {
		this.dataDate = dataDate;
	}

	/**
	 * Returns the local or remote absolute path to the map image
	 * @return image path as string
	 */
	public String getImagePath() {
		return imagePath;
	}

	@Override
	public String toString() {
		return "GoesMap [variableName=" + variableName + ", dataDate="
				+ dataDate + ", imagePath=" + imagePath + "]";
	}
	
	
}
