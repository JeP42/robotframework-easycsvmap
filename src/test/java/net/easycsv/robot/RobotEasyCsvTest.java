package net.easycsv.robot;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Test;

public class RobotEasyCsvTest {

    @Test
    public void parseCsvFromFileAndGetAllValues() {

        String csvFilePath = ClassLoader.getSystemResource("de/jep/easycsvmap/header-0-five-lines.csv").getFile();

        RobotEasyCsv easyCsv = new RobotEasyCsv();
        easyCsv.parseCsvFromFile(csvFilePath, 0);
        // intention of this test is to verify the robot keyword, actually not the underlying CSVMap. Hence this easy test is sufficient...
        List<String> allCsvValues = easyCsv.getAllCsvValues("{*}.Col0-Header");

        assertEquals("Col0-Header", allCsvValues.get(0).toString());
        assertEquals("datacol0-line0", allCsvValues.get(1).toString());
        assertEquals("datacol0-line1", allCsvValues.get(2).toString());
        assertEquals("datacol0-line2", allCsvValues.get(3).toString());
        assertEquals("datacol0-line3", allCsvValues.get(4).toString());
        assertEquals("datacol0-line4", allCsvValues.get(5).toString());
    }

    @Test
    public void setCsvValues() {
        String csvFilePath = ClassLoader.getSystemResource("de/jep/easycsvmap/header-0-five-lines.csv").getFile();

        RobotEasyCsv easyCsv = new RobotEasyCsv();
        easyCsv.parseCsvFromFile(csvFilePath, 0);
        // intention of this test is to verify the robot keyword, actually not the underlying CSVMap. Hence this easy test is sufficient...
        easyCsv.setCsvValues("{1,2,3,4,5}.Col0-Header", "42");

        List<String> allCsvValues = easyCsv.getAllCsvValues("{*}.Col0-Header");

        assertEquals("Col0-Header", allCsvValues.get(0).toString());
        assertEquals("42", allCsvValues.get(1).toString());
        assertEquals("42", allCsvValues.get(2).toString());
        assertEquals("42", allCsvValues.get(3).toString());
        assertEquals("42", allCsvValues.get(4).toString());
        assertEquals("42", allCsvValues.get(5).toString());
    }

    @Test
    public void saveCsvToFile() {
        String csvFilePath = ClassLoader.getSystemResource("de/jep/easycsvmap/header-0-five-lines.csv").getFile();

        RobotEasyCsv easyCsv = new RobotEasyCsv();
        easyCsv.parseCsvFromFile(csvFilePath, 0);
        // intention of this test is to verify the robot keyword, actually not the underlying CSVMap. Hence this easy test is sufficient...
        easyCsv.setCsvValues("{1,2,3,4,5}.Col0-Header", "42");


        String tempFilePath = this.createTempFile();
        try {
            easyCsv.saveCsvToFile(tempFilePath);
            easyCsv.parseCsvFromFile(tempFilePath, 0);
            List<String> allCsvValues = easyCsv.getAllCsvValues("{*}.Col0-Header");

            assertEquals("Col0-Header", allCsvValues.get(0).toString());
            assertEquals("42", allCsvValues.get(1).toString());
            assertEquals("42", allCsvValues.get(2).toString());
            assertEquals("42", allCsvValues.get(3).toString());
            assertEquals("42", allCsvValues.get(4).toString());
            assertEquals("42", allCsvValues.get(5).toString());
        } finally {
            this.deleteTempFile(tempFilePath);
        }
    }

    @Test
    public void getFirstCsvValue() {
        String csvFilePath = ClassLoader.getSystemResource("de/jep/easycsvmap/header-0-five-lines.csv").getFile();

        RobotEasyCsv easyCsv = new RobotEasyCsv();
        easyCsv.parseCsvFromFile(csvFilePath, 0);
        // intention of this test is to verify the robot keyword, actually not the underlying CSVMap. Hence this easy test is sufficient...
        String v = easyCsv.getFirstCsvValue("{1}.Col0-Header");

        assertEquals("datacol0-line0", v);
    }

    @Test(expected = RobotCsvException.class)
    public void setCsvValues_NotInitialized() {
        new RobotEasyCsv().setCsvValues("blub", "42");
    }

    @Test(expected = RobotCsvException.class)
    public void getFirstCsvValue_NotInitialized() {
        new RobotEasyCsv().getFirstCsvValue("blub");
    }

    @Test(expected = RobotCsvException.class)
    public void getAllCsvValues_NotInitialized() {
        new RobotEasyCsv().getAllCsvValues("blub");
    }

    @Test(expected = RobotCsvException.class)
    public void saveCsvToFile_NotInitialized() {
        new RobotEasyCsv().saveCsvToFile("blub");
    }

    private String createTempFile() {
        try {
            return File.createTempFile("junit", "").getAbsolutePath();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void deleteTempFile(String tempFilePath) {
        File file = new File(tempFilePath);
        if (file.exists()) {
            file.delete();
        }
    }

}
