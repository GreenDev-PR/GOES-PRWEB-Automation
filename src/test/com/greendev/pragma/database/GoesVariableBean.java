package test.com.greendev.pragma.database;

import java.sql.Timestamp;

import main.com.greendev.pragma.database.databaseModel.GoesVariable;

/**
 * GOES-PRWEB variable bean wrapper 
 * Includes the variable name, date, matrix row, matrix column, value
 * @author josediaz
 */
public class GoesVariableBean {
	
	private String variableName;
	private Timestamp dataDate;
	private int row;
	private int column;
	private Double dataValue;	
	
	public GoesVariableBean(){
	}
	public GoesVariableBean(GoesVariable var){
		this.variableName = var.getVariableName();
		this.dataDate = new Timestamp(var.getDataDate().getMillis());
		this.row = var.getRow();
		this.column = var.getColumn();
		this.dataValue = var.getDataValue();
	}
	
	public String getVariableName() {
		return variableName;
	}
	public void setVariableName(String variableName) {
		this.variableName = variableName;
	}
	public Timestamp getDataDate() {
		return dataDate;
	}
	public void setDataDate(Timestamp dataDate) {
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
		return "GoesDataBean [variableName=" + variableName + ", dataDate="
				+ dataDate + ", row=" + row + ", column=" + column
				+ ", dataValue=" + dataValue + "]";
	}
	
	@Override
	public boolean equals(Object other){

		if(other == null){
			return false;
		}
		if(this.getClass() != other.getClass()){
			return false;
		}
		return this.variableName.equals( ((GoesVariableBean)other).getVariableName()) && 
			this.row == ((GoesVariableBean)other).getRow() 
				&& this.column == ((GoesVariableBean)other).getColumn() && 
				this.dataValue.equals( ((GoesVariableBean)other).getDataValue() );
		
	}
	
}
