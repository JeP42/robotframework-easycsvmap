package com.github.jep42.roboteasycsv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Test;

import com.github.jep42.easycsvmap.util.FileUtil;

public class RobotEasyCsvTest {

    @Test
    public void parseCsvFromFileAndGetAllValues() {

        String csvFilePath1 = FileUtil.getSystemResourcePath("com/github/jep42/easycsvmap/header-0-five-lines.csv");
        String csvFilePath2 = FileUtil.getSystemResourcePath("com/github/jep42/easycsvmap/header-0-five-lines.csv");

        RobotEasyCsv easyCsv = new RobotEasyCsv();
        easyCsv.parseCsvFromFile(1, csvFilePath1, 0);
        easyCsv.parseCsvFromFile(2, csvFilePath2, 0);

        for (int i=1; i<=2; i++) {
            List<String> allCsvValues = easyCsv.getAllCsvValues(i, "{*}.Col0-Header");

            assertEquals("Col0-Header", allCsvValues.get(0).toString());
            assertEquals("datacol0-line0", allCsvValues.get(1).toString());
            assertEquals("datacol0-line1", allCsvValues.get(2).toString());
            assertEquals("datacol0-line2", allCsvValues.get(3).toString());
            assertEquals("datacol0-line3", allCsvValues.get(4).toString());
            assertEquals("datacol0-line4", allCsvValues.get(5).toString());
        }
    }

    @Test
    public void setCsvValues() {

        String csvFilePath1 = FileUtil.getSystemResourcePath("com/github/jep42/easycsvmap/header-0-five-lines.csv");
        String csvFilePath2 = FileUtil.getSystemResourcePath("com/github/jep42/easycsvmap/header-0-five-lines.csv");

        RobotEasyCsv easyCsv = new RobotEasyCsv();
        easyCsv.parseCsvFromFile(1, csvFilePath1, 0);
        easyCsv.parseCsvFromFile(2, csvFilePath2, 0);

        easyCsv.setCsvValues(1, "{1,2,3,4,5}.Col0-Header", "42");
        easyCsv.setCsvValues(2, "{1,2,3,4,5}.Col0-Header", "43");

        this.assertValues(easyCsv, 1, "42");
        this.assertValues(easyCsv, 2, "43");
    }

     private void assertValues(RobotEasyCsv easyCsv, Integer sessionId, String value) {
        List<String> allCsvValues = easyCsv.getAllCsvValues(sessionId, "{*}.Col0-Header");

        assertEquals("Col0-Header", allCsvValues.get(0).toString());
        assertEquals(value, allCsvValues.get(1).toString());
        assertEquals(value, allCsvValues.get(2).toString());
        assertEquals(value, allCsvValues.get(3).toString());
        assertEquals(value, allCsvValues.get(4).toString());
        assertEquals(value, allCsvValues.get(5).toString());
    }

    @Test
    public void saveCsvToFile() {
        String csvFilePath1 = FileUtil.getSystemResourcePath("com/github/jep42/easycsvmap/header-0-five-lines.csv");
        String csvFilePath2 = FileUtil.getSystemResourcePath("com/github/jep42/easycsvmap/header-0-five-lines.csv");

        RobotEasyCsv easyCsv = new RobotEasyCsv();
        easyCsv.parseCsvFromFile(1, csvFilePath1, 0);
        easyCsv.parseCsvFromFile(2, csvFilePath2, 0);

        // intention of this test is to verify the robot keyword, actually not the underlying CSVMap. Hence this easy test is sufficient...
        easyCsv.setCsvValues(1, "{1,2,3,4,5}.Col0-Header", "42");
        easyCsv.setCsvValues(2, "{1,2,3,4,5}.Col0-Header", "43");

        String tempFilePath = this.createTempFile();
        try {
            easyCsv.saveCsvToFile(1, tempFilePath + "_1");
            easyCsv.saveCsvToFile(2, tempFilePath + "_2");

            this.assertValues(easyCsv, 1, "42");
            this.assertValues(easyCsv, 2, "43");
        } finally {
            this.deleteTempFile(tempFilePath + "_1");
            this.deleteTempFile(tempFilePath + "_2");
        }
    }


    @Test
    public void specialLineEndSequenceProperlyConsidered() throws Exception {
        String csvFilePath1 = FileUtil.getSystemResourcePath("com/github/jep42/easycsvmap/header-0-five-lines.csv");

        String specialLineEndSequence = "\\r\\n";

        RobotEasyCsv easyCsv = new RobotEasyCsv();
        easyCsv.parseCsvFromFile(1, csvFilePath1, 0, ";", "\"", specialLineEndSequence);

        String tempFilePath = this.createTempFile();
        try {
            easyCsv.saveCsvToFile(1, tempFilePath);

            //read file and test on the special string
            String csvFile = FileUtil.loadFile(tempFilePath);
            String[] lines = csvFile.split(specialLineEndSequence);
            assertEquals(6, lines.length);
        } finally {
            this.deleteTempFile(tempFilePath);
        }
    }


    @Test
    public void getFirstCsvValue() {
        String csvFilePath1 = FileUtil.getSystemResourcePath("com/github/jep42/easycsvmap/header-0-five-lines.csv");
        String csvFilePath2 = FileUtil.getSystemResourcePath("com/github/jep42/easycsvmap/header-0-five-lines.csv");

        RobotEasyCsv easyCsv = new RobotEasyCsv();
        easyCsv.parseCsvFromFile(1, csvFilePath1, 0);
        easyCsv.parseCsvFromFile(2, csvFilePath2, 0);

        // intention of this test is to verify the robot keyword, actually not the underlying CSVMap. Hence this easy test is sufficient...
        String v1 = easyCsv.getFirstCsvValue(1, "{1}.Col0-Header");
        String v2 = easyCsv.getFirstCsvValue(2, "{1}.Col0-Header");

        assertEquals("datacol0-line0", v1);
        assertEquals("datacol0-line0", v2);
    }

    @Test
    public void addRow() {
        String csvFilePath1 = FileUtil.getSystemResourcePath("com/github/jep42/easycsvmap/header-0-five-lines.csv");
        String csvFilePath2 = FileUtil.getSystemResourcePath("com/github/jep42/easycsvmap/header-0-five-lines.csv");

        RobotEasyCsv easyCsv = new RobotEasyCsv();
        easyCsv.parseCsvFromFile(1, csvFilePath1, 0);
        easyCsv.parseCsvFromFile(2, csvFilePath2, 0);

        easyCsv.addRow(1, "Wendy", "Darling", "wendy@home.com");
        easyCsv.addRow(2, "Peter", "Pan", "pp@home.com");

        // intention of this test is to verify the robot keyword, actually not the underlying CSVMap. Hence this easy test is sufficient...
        String v1 = easyCsv.getFirstCsvValue(1, "{6}.Col0-Header");
        String v2 = easyCsv.getFirstCsvValue(2, "{6}.Col0-Header");

        assertEquals("Wendy", v1);
        assertEquals("Peter", v2);
    }

    @Test
    public void removeSession() {
        String csvFilePath1 = FileUtil.getSystemResourcePath("com/github/jep42/easycsvmap/header-0-five-lines.csv");
        String csvFilePath2 = FileUtil.getSystemResourcePath("com/github/jep42/easycsvmap/header-0-five-lines.csv");

        RobotEasyCsv easyCsv = new RobotEasyCsv();
        easyCsv.parseCsvFromFile(1, csvFilePath1, 0);
        easyCsv.parseCsvFromFile(2, csvFilePath2, 0);

        easyCsv.removeCsvSession(2);

        try {
            easyCsv.addRow(2, "Peter", "Pan", "pp@home.com");
            fail("Seems the CSV session with ID 2 was not properly removed");
        } catch (RobotCsvException e) {
            //nothing to do....this is expected
        }
    }



    @Test(expected = RobotCsvException.class)
    public void setCsvValues_NotInitializedDefaultInstance() {
        new RobotEasyCsv().setCsvValues(42, "blub", "42");
    }

    @Test(expected = RobotCsvException.class)
    public void getFirstCsvValue_NotInitialized() {
        new RobotEasyCsv().getFirstCsvValue(42, "blub");
    }

    @Test(expected = RobotCsvException.class)
    public void getAllCsvValues_NotInitialized() {
        new RobotEasyCsv().getAllCsvValues(42, "blub");
    }

    @Test(expected = RobotCsvException.class)
    public void saveCsvToFile_NotInitialized() {
        new RobotEasyCsv().saveCsvToFile(42, "blub");
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
