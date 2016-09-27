package de.jep.easycsvmap;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EasyCSVMapTest {


    @Test
    public void parseCsv_noHeaderLine() {

        EasyCSVMap csvMap = new EasyCSVMap();
        String csvFilePath = ClassLoader.getSystemResource("de/jep/easycsvmap/no-header-five-lines.csv").getFile();

        csvMap.parseCsvFromFile(csvFilePath);

        // expect a standard header with indexes to be available ==> access columns via index
        assertEquals("datacol0-line0", csvMap.getValue("0.0"));
        assertEquals("datacol1-line0", csvMap.getValue("0.1"));
        assertEquals("datacol2-line0", csvMap.getValue("0.2"));
    }

    @Test
    public void parseCsv_nonStandardSeparator() {

        CSVContext csvContext = new CSVContext(CSVContext.NO_HEADER_INDEX);
        csvContext.setColumnSeparator(',');

        EasyCSVMap csvMap = new EasyCSVMap(csvContext);
        String csvFilePath = ClassLoader.getSystemResource("de/jep/easycsvmap/no-header-five-lines_sep1.csv").getFile();

        csvMap.parseCsvFromFile(csvFilePath);

        // expect a standard header with indexes to be available ==> access columns via index
        assertEquals("datacol0-line0", csvMap.getValue("0.0"));
        assertEquals("datacol1-line0", csvMap.getValue("0.1"));
        assertEquals("datacol2-line0", csvMap.getValue("0.2"));
    }

    @Test
    public void parseCsv_quotedValues() {

        EasyCSVMap csvMap = new EasyCSVMap(0);
        String csvFilePath = ClassLoader.getSystemResource("de/jep/easycsvmap/header-0-five-lines_quotedValues.csv").getFile();

        csvMap.parseCsvFromFile(csvFilePath);

        // expect a standard header with indexes to be available ==> access columns via index
        assertEquals("a;a", csvMap.getValue("1.Col0-Header"));
        assertEquals("b;b", csvMap.getValue("1.Col1-Header"));
        assertEquals("c;c", csvMap.getValue("1.Col2-Header"));
    }

    @Test
    public void parseCsv_nonStandardQuoteChar() {
        CSVContext csvContext = new CSVContext(42);
        csvContext.setHeaderLine(0);
        csvContext.setQuoteCharacter('$');

        EasyCSVMap csvMap = new EasyCSVMap(csvContext);

        String csvFilePath = ClassLoader.getSystemResource("de/jep/easycsvmap/header-0-five-lines_nonStdQuoteChar.csv").getFile();

        csvMap.parseCsvFromFile(csvFilePath);

        // expect a standard header with indexes to be available ==> access columns via index
        assertEquals("a;a", csvMap.getValue("1.Col0-Header"));
        assertEquals("b;b", csvMap.getValue("1.Col1-Header"));
        assertEquals("c;c", csvMap.getValue("1.Col2-Header"));
    }

    @Test
    public void parseCsv_headerLine1() {

        // line with index=0 is the header line
        EasyCSVMap csvMap = new EasyCSVMap(0);
        String csvFilePath = ClassLoader.getSystemResource("de/jep/easycsvmap/header-0-five-lines.csv").getFile();

        csvMap.parseCsvFromFile(csvFilePath);

        // expect a header with column names to be available ==> access columns via name
        assertEquals("datacol0-line0", csvMap.getValue("1.Col0-Header"));
        assertEquals("datacol1-line0", csvMap.getValue("1.Col1-Header"));
        assertEquals("datacol2-line0", csvMap.getValue("1.Col2-Header"));

        // be aware the the header line is also part of the internal representation of the CSV so this has to considered in the index
        assertEquals("Col0-Header", csvMap.getValue("0.Col0-Header"));
    }

    @Test(expected = RuntimeException.class)
    public void parseCsv_invalidHeaderLineIndex() throws Exception {

        EasyCSVMap csvMap = new EasyCSVMap(42);
        String csvFilePath = ClassLoader.getSystemResource("de/jep/easycsvmap/header-0-five-lines.csv").getFile();

        csvMap.parseCsvFromFile(csvFilePath);
    }

    @Test(expected = RuntimeException.class)
    public void parseCsv_invalidDataLine() throws Exception {

        EasyCSVMap csvMap = new EasyCSVMap(0);
        String csvFilePath = ClassLoader.getSystemResource("de/jep/easycsvmap/header-0-five-lines-one-invalid.csv").getFile();

        csvMap.parseCsvFromFile(csvFilePath);
    }


    @Test
    public void getNumberOfCSVLines() {
        EasyCSVMap csvMap = new EasyCSVMap();
        String csvFilePath = ClassLoader.getSystemResource("de/jep/easycsvmap/no-header-five-lines.csv").getFile();

        csvMap.parseCsvFromFile(csvFilePath);

        assertEquals(5, csvMap.getNumberOfCSVLines());
    }


    @Test
    public void getNumberOfCSVColumns() {
        EasyCSVMap csvMap = new EasyCSVMap();
        String csvFilePath = ClassLoader.getSystemResource("de/jep/easycsvmap/no-header-five-lines.csv").getFile();
        csvMap.parseCsvFromFile(csvFilePath);
        assertEquals(3, csvMap.getNumberOfCSVColumns());
    }

    @Test
    public void getNumberOfCSVColumns_separatorLastChar() {
        EasyCSVMap csvMap = new EasyCSVMap();
        String csvFilePath = ClassLoader.getSystemResource("de/jep/easycsvmap/header-0-five-lines.csv").getFile();
        csvMap.parseCsvFromFile(csvFilePath);
        assertEquals(4, csvMap.getNumberOfCSVColumns());
    }

    @Test
    public void saveToFile() {
        EasyCSVMap csvMap = new EasyCSVMap(0);
        String csvFilePath = ClassLoader.getSystemResource("de/jep/easycsvmap/header-0-five-lines.csv").getFile();
        csvMap.parseCsvFromFile(csvFilePath);
        for (int i = 0; i < 3; i++) {
            csvMap.setValues("1.Col" + i + "-Header", "4711-" + i);
            csvMap.setValues("5.Col" + i + "-Header", "0815-" + i);
        }

        String tempFilePath = this.createTempFile();
        try {
            csvMap.saveToFile(tempFilePath);

            csvMap = new EasyCSVMap(0);
            csvMap.parseCsvFromFile(tempFilePath);
            for (int i = 0; i < 3; i++) {
                assertEquals("4711-" + i, csvMap.getValue("1.Col" + i + "-Header"));
                assertEquals("0815-" + i, csvMap.getValue("5.Col" + i + "-Header"));
            }
        } finally {
            this.deleteTempFile(tempFilePath);
        }

    }

    private void deleteTempFile(String tempFilePath) {
        File file = new File(tempFilePath);
        if (file.exists()) {
            file.delete();
        }
    }

    private String createTempFile() {
        try {
            return File.createTempFile("junit", "").getAbsolutePath();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
