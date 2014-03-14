package main.com.greendev.pragma.database.databaseModel;

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

	public GoesMap(String variableName, DateTime dataDate, String imagePath) {
		this.variableName = variableName;
		this.dataDate = dataDate;
		this.imagePath = imagePath;
	}

	public String getVariableName() {
		return variableName;
	}

	public void setVariableName(String variableName) {
		this.variableName = variableName;
	}

	public DateTime getDataDate() {
		return dataDate;
	}

	public void setDataDate(DateTime dataDate) {
		this.dataDate = dataDate;
	}

	public String getImagePath() {
		return imagePath;
	}

	@Override
	public String toString() {
		return "GoesMap [variableName=" + variableName + ", dataDate="
				+ dataDate + ", imagePath=" + imagePath + "]";
	}
	
	
}
