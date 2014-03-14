package main.com.greendev.pragma.database.databaseModel;

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
	
	public GoesVariable(String variableName, DateTime dataDate, int row,
			int column, Double dataValue) {
		this.variableName = variableName;
		this.dataDate = dataDate;
		this.row = row;
		this.column = column;
		this.dataValue = dataValue;
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
	public int getRow() {
		return row;
	}
	public void setRow(int row) {
		this.row = row;
	}
	public int getColumn() {
		return column;
	}
	public void setColumn(int column) {
		this.column = column;
	}
	public Double getDataValue() {
		return dataValue;
	}
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
