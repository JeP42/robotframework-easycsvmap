package de.jep.easycsvmap.robotframework;

import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywords;

import de.jep.easycsvmap.EasyCSVMap;

@RobotKeywords
public class RobotEasyCsv {

    public static final String ROBOT_LIBRARY_SCOPE = "GLOBAL";

    private EasyCSVMap easyCsvMap;

    @RobotKeyword("Parse Csv From File")
    @ArgumentNames({ "pathToCsv", "headerLineIndex" })
    public void parseCsvFromFile(String pathToCsv, int headerLineIndex) {
        this.easyCsvMap = new EasyCSVMap(headerLineIndex);
        this.easyCsvMap.parseCsvFromFile(pathToCsv);
    }

    @RobotKeyword("Set Csv Value")
    @ArgumentNames({ "csvSelector", "value" })
    public void setCsvValue(String csvSelector, String value) {
        this.easyCsvMap.setValues(csvSelector, value);
    }

    @RobotKeyword("Get Csv Value")
    @ArgumentNames({ "csvSelector" })
    public String getCsvValue(String csvSelector) {
        return this.easyCsvMap.getValue(csvSelector);
    }

    @RobotKeyword("Save Csv To File")
    @ArgumentNames({ "csvSelector" })
    public void saveCsvToFile(String filePath) {
        this.easyCsvMap.saveToFile(filePath);
    }

}
