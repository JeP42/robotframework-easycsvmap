package com.github.jep42.easycsvmap.robot;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywords;

import com.github.jep42.easycsvmap.EasyCSVMap;

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

    @RobotKeyword("Set Csv Values")
    @ArgumentNames({ "csvSelector", "value" })
    public void setCsvValues(String csvSelector, String value) {
        this.checkInitialized();
        this.easyCsvMap.setValues(csvSelector, value);
    }

    private void checkInitialized() {
        if (this.easyCsvMap == null) {
            throw new RobotCsvException("EasyCsv has to be initialized via parseCsvFromFile first");
        }
    }

    @RobotKeyword("Get First Csv Value")
    @ArgumentNames({ "csvSelector" })
    public String getFirstCsvValue(String csvSelector) {
        this.checkInitialized();

        // cannot return Map to robot, so just return the value
        Iterator<String> it = this.easyCsvMap.getValues(csvSelector).values().iterator();
        if (it.hasNext()) {
            return it.next();
        } else {
            return null;
        }
    }

    @RobotKeyword("Get All Csv Values")
    @ArgumentNames({ "csvSelector" })
    public List<String> getAllCsvValues(String csvSelector) {
        this.checkInitialized();
        // cannot return Map to robot, so just return the values
        return new ArrayList<>(this.easyCsvMap.getValues(csvSelector).values());
    }

    @RobotKeyword("Save Csv To File")
    @ArgumentNames({ "csvSelector" })
    public void saveCsvToFile(String filePath) {
        this.checkInitialized();
        this.easyCsvMap.saveToFile(filePath);
    }

}
