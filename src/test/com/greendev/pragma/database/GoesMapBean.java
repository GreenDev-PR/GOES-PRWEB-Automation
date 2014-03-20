package test.com.greendev.pragma.database;

import java.sql.Timestamp;

import main.com.greendev.pragma.database.databaseModel.GoesMap;

import org.joda.time.DateTime;

public class GoesMapBean {

	private String variableName;
	private Timestamp dataDate;
	private String imagePath;

	public GoesMapBean(){
	}
	
	public GoesMapBean(GoesMap map) {
		this.variableName = map.getVariableName();
		this.dataDate = new Timestamp(map.getDataDate().getMillis());
		this.imagePath = map.getImagePath();
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

	public String getImagePath() {
		return imagePath;
	}
	
	public void setImagePath(String imagePath){
		this.imagePath = imagePath;
	}
	@Override
	public String toString() {
		return "GoesMap [variableName=" + variableName + ", dataDate="
				+ dataDate + ", imagePath=" + imagePath + "]";
	}
	
	@Override
	public boolean equals(Object other){

		if(other == null){
			return false;
		}
		if(this.getClass() != other.getClass()){
			return false;
		}
		return this.variableName.equals( ((GoesMapBean)other).getVariableName() ) 
				&& this.imagePath.equals( ((GoesMapBean)other).getImagePath() )
				&& this.dataDate.equals( ((GoesMapBean)other).getDataDate() ) ;  
				
	}
}
