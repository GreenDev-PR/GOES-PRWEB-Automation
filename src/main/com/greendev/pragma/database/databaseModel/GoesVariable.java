package main.com.greendev.pragma.database.databaseModel;

import java.sql.Timestamp;

/**
 * Single GOES-PRWEB variable (e.g. rainfall, ET, wind)
 * Includes the variable name, matrix of values corresponding to PR coordinates,
 * and date
 * @author miguelgd
 */
public class GoesVariable {

	private String variableName;
	private int row;
	private int column;
	private Double value;
	private Timestamp createdTime;
	private Timestamp updatedTime;
	
	
	public GoesVariable(String variableName, int row, int column,
			Double value, Timestamp createdTime, Timestamp updatedTime) {
		super();
		this.variableName = variableName;
		this.row = row;
		this.column = column;
		this.value = value;
		this.createdTime = createdTime;
		this.updatedTime = updatedTime;
	}
	
	public String getVariableName() {
		return variableName;
	}
	public void setVariableName(String variableName) {
		this.variableName = variableName;
	}
	public Timestamp getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Timestamp createdTime) {
		this.createdTime = createdTime;
	}

	public Timestamp getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Timestamp updatedTime) {
		this.updatedTime = updatedTime;
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
	public Double getValue() {
		return value;
	}
	public void setValue(Double value) {
		this.value = value;
	}
}
