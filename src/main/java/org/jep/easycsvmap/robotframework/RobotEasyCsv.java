package org.jep.easycsvmap.robotframework;

import org.jep.easycsvmap.EasyCSVMap;
import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywords;

@RobotKeywords
public class RobotEasyCsv {


	public static final String ROBOT_LIBRARY_SCOPE = "GLOBAL";

	private  EasyCSVMap easyCsvMap;


	@RobotKeyword("Parse Csv From File")
	@ArgumentNames({"pathToCsv", "headerLineIndex"})
	public void parseCsvFromFile(String pathToCsv, int headerLineIndex) {
		this.easyCsvMap = new EasyCSVMap(headerLineIndex);
		this.easyCsvMap.parseCsvFromFile(pathToCsv);
	}


	@RobotKeyword("Set Csv Value")
	@ArgumentNames({"csvSelector", "value"})
	public void setCsvValue(String csvSelector, String value) {
		this.easyCsvMap.setValue(csvSelector, value);
	}


	@RobotKeyword("Get Csv Value")
	@ArgumentNames({"csvSelector"})
	public String getCsvValue(String csvSelector) {
		return this.easyCsvMap.getValue(csvSelector);
	}


	@RobotKeyword("Save Csv To File")
	@ArgumentNames({"csvSelector"})
	public void saveCsvToFile(String filePath) {
		this.easyCsvMap.saveToFile(filePath);
	}


}
