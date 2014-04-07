package main.java.com.greendev.pragma.database.databaseModel;

import org.joda.time.DateTime;

/**
 * Single GOES-PRWEB variable value (e.g. rainfall, ET, wind)
 * Includes the variable name, date, matrix row, matrix column, value
 * @author miguelgd
 */
public class GoesVariable {
	
	private String variableName;
	private DateTime dataDate;
	private int row;
	private int column;
	private Double dataValue;	
	
	public GoesVariable(){};
	
	/**
	 * Creates an instance of the model object GoesVariable, corresponds to a table row
	 * @param variableName name of the variable for the data 
	 * @param dataDate date of the data value to store
	 * @param row matrix row that maps to coordinates
	 * @param column matrix row that maps to coordinates
	 * @param dataValue actual value for the given variable, location, and date
	 */
	public GoesVariable(String variableName, DateTime dataDate, int row,
			int column, Double dataValue) {
		this.variableName = variableName;
		this.dataDate = dataDate;
		this.row = row;
		this.column = column;
		this.dataValue = dataValue;
	}
	
	/**
	 * Returns the configured GOES-PRWEB variable name
	 * @return variable name
	 */
	public String getVariableName() {
		return variableName;
	}
	/**
	 * Configure the name of the variable for the object
	 * @param variableName
	 */
	public void setVariableName(String variableName) {
		this.variableName = variableName;
	}
	/**
	 * Returns the configured date for the data
	 * @return date for the data
	 */
	public DateTime getDataDate() {
		return dataDate;
	}
	/**
	 * Configures the date for the data to store
	 * @param dataDate
	 */
	public void setDataDate(DateTime dataDate) {
		this.dataDate = dataDate;
	}
	/**
	 * Returns the matrix row, mapped to a coordinate 
	 * @return row number, zero based
	 */
	public int getRow() {
		return row;
	}
	/**
	 * Sets the row number for the current data point
	 * @param row Row number, zero based
	 */
	public void setRow(int row) {
		this.row = row;
	}
	/**
	 * Returns the matrix column, mapped to a coordinate 
	 * @return column number, zero based
	 */
	public int getColumn() {
		return column;
	}
	/**
	 * Sets the column number for the current data point
	 * @param column Column number, zero based
	 */
	public void setColumn(int column) {
		this.column = column;
	}
	/**
	 * Returns the actual value as a double number for the given location, date, and variable name
	 * @return data value
	 */
	public Double getDataValue() {
		return dataValue;
	}
	/**
	 * Configures the value for the combination of location, date, and variable name
	 * @param dataValue
	 */
	public void setDataValue(Double dataValue) {
		this.dataValue = dataValue;
	}
	@Override
	public String toString() {
		return "GoesVariable [variableName=" + variableName + ", dataDate="
				+ dataDate + ", row=" + row + ", column=" + column
				+ ", dataValue=" + dataValue + "]";
	}
}
