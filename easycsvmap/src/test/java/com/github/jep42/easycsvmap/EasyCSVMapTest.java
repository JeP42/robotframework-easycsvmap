package com.github.jep42.easycsvmap;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import com.github.jep42.easycsvmap.core.CSVContext;
import com.github.jep42.easycsvmap.core.CSVMapException;

public class EasyCSVMapTest {


    @Test
    public void parseCsv_noHeaderLine() {

        EasyCSVMap csvMap = new EasyCSVMap();
        String csvFilePath = ClassLoader.getSystemResource("com/github/jep42/easycsvmap/no-header-five-lines.csv").getFile();

        csvMap.parseCsvFromFile(csvFilePath);

        // expect a standard header with indexes to be available ==> access columns via index
        assertEquals("datacol0-line0", csvMap.getValues("{0}.0").values().iterator().next());
        assertEquals("datacol1-line0", csvMap.getValues("{0}.1").values().iterator().next());
        assertEquals("datacol2-line0", csvMap.getValues("{0}.2").values().iterator().next());
    }


    @Test
    public void parseCsv_headerLineWithBlanks() {

        EasyCSVMap csvMap = new EasyCSVMap(0);
        String csvFilePath = ClassLoader.getSystemResource("com/github/jep42/easycsvmap/header-with-blanks-five-lines.csv").getFile();

        csvMap.parseCsvFromFile(csvFilePath);

        // expect a standard header with indexes to be available ==> access columns via index
        assertEquals("datacol0-line0", csvMap.getValues("{1}.Col0 Header").values().iterator().next());
        assertEquals("datacol1-line0", csvMap.getValues("{1}.Col1 Header").values().iterator().next());
        assertEquals("datacol2-line0", csvMap.getValues("{1}.Col2-Header").values().iterator().next());
    }


    @Test
    public void parseCsv_nonStandardSeparator() {

        CSVContext csvContext = new CSVContext(CSVContext.NO_HEADER_INDEX);
        csvContext.setColumnSeparator(',');

        EasyCSVMap csvMap = new EasyCSVMap(csvContext);
        String csvFilePath = ClassLoader.getSystemResource("com/github/jep42/easycsvmap/no-header-five-lines_sep1.csv").getFile();

        csvMap.parseCsvFromFile(csvFilePath);

        // expect a standard header with indexes to be available ==> access columns via index
        assertEquals("datacol0-line0", csvMap.getValues("{0}.0").values().iterator().next());
        assertEquals("datacol1-line0", csvMap.getValues("{0}.1").values().iterator().next());
        assertEquals("datacol2-line0", csvMap.getValues("{0}.2").values().iterator().next());
    }

    @Test
    public void parseCsv_quotedValues() {

        EasyCSVMap csvMap = new EasyCSVMap(0);
        String csvFilePath = ClassLoader.getSystemResource("com/github/jep42/easycsvmap/header-0-five-lines_quotedValues.csv").getFile();

        csvMap.parseCsvFromFile(csvFilePath);

        // expect a standard header with indexes to be available ==> access columns via index
        assertEquals("a;a", csvMap.getValues("{1}.Col0-Header").values().iterator().next());
        assertEquals("b;b", csvMap.getValues("{1}.Col1-Header").values().iterator().next());
        assertEquals("c;c", csvMap.getValues("{1}.Col2-Header").values().iterator().next());
    }

    @Test
    public void parseCsv_nonStandardQuoteChar() {
        CSVContext csvContext = new CSVContext(42);
        csvContext.setHeaderRow(0);
        csvContext.setQuoteCharacter('$');

        EasyCSVMap csvMap = new EasyCSVMap(csvContext);

        String csvFilePath = ClassLoader.getSystemResource("com/github/jep42/easycsvmap/header-0-five-lines_nonStdQuoteChar.csv").getFile();

        csvMap.parseCsvFromFile(csvFilePath);

        // expect a standard header with indexes to be available ==> access columns via index
        assertEquals("a;a", csvMap.getValues("{1}.Col0-Header").values().iterator().next());
        assertEquals("b;b", csvMap.getValues("{1}.Col1-Header").values().iterator().next());
        assertEquals("c;c", csvMap.getValues("{1}.Col2-Header").values().iterator().next());
    }

    @Test
    public void parseCsv_headerLine1() {

        // line with index=0 is the header line
        EasyCSVMap csvMap = new EasyCSVMap(0);
        String csvFilePath = ClassLoader.getSystemResource("com/github/jep42/easycsvmap/header-0-five-lines.csv").getFile();

        csvMap.parseCsvFromFile(csvFilePath);

        // expect a header with column names to be available ==> access columns via name
        assertEquals("datacol0-line0", csvMap.getValues("{1}.Col0-Header").values().iterator().next());
        assertEquals("datacol1-line0", csvMap.getValues("{1}.Col1-Header").values().iterator().next());
        assertEquals("datacol2-line0", csvMap.getValues("{1}.Col2-Header").values().iterator().next());

        // be aware the the header line is also part of the internal representation of the CSV so this has to considered in the index
        assertEquals("Col0-Header", csvMap.getValues("{0}.Col0-Header").values().iterator().next());
    }

    @Test(expected = RuntimeException.class)
    public void parseCsv_invalidHeaderLineIndex() throws Exception {

        EasyCSVMap csvMap = new EasyCSVMap(42);
        String csvFilePath = ClassLoader.getSystemResource("com/github/jep42/easycsvmap/header-0-five-lines.csv").getFile();

        csvMap.parseCsvFromFile(csvFilePath);
    }

    @Test(expected = RuntimeException.class)
    public void parseCsv_headerLineContainsDuplicates() throws Exception {

        EasyCSVMap csvMap = new EasyCSVMap(0);
        String csvFilePath = ClassLoader.getSystemResource("com/github/jep42/easycsvmap/header-0-with-duplicates.csv").getFile();

        csvMap.parseCsvFromFile(csvFilePath);
    }

    @Test(expected = RuntimeException.class)
    public void parseCsv_invalidDataLine() throws Exception {

        EasyCSVMap csvMap = new EasyCSVMap(0);
        String csvFilePath = ClassLoader.getSystemResource("com/github/jep42/easycsvmap/header-0-five-lines-one-invalid.csv").getFile();

        csvMap.parseCsvFromFile(csvFilePath);
    }


    @Test
    public void getNumberOfCSVLines() {
        EasyCSVMap csvMap = new EasyCSVMap();
        String csvFilePath = ClassLoader.getSystemResource("com/github/jep42/easycsvmap/no-header-five-lines.csv").getFile();

        csvMap.parseCsvFromFile(csvFilePath);

        assertEquals(5, csvMap.getNumberOfCSVRows());
    }


    @Test
    public void getNumberOfCSVColumns() {
        EasyCSVMap csvMap = new EasyCSVMap();
        String csvFilePath = ClassLoader.getSystemResource("com/github/jep42/easycsvmap/no-header-five-lines.csv").getFile();
        csvMap.parseCsvFromFile(csvFilePath);
        assertEquals(3, csvMap.getNumberOfCSVColumns());
    }

    @Test
    public void parseCsv_emptyLines() {
        EasyCSVMap csvMap = new EasyCSVMap(0);
        String csvFilePath = ClassLoader.getSystemResource("com/github/jep42/easycsvmap/header-0-five-lines_emptyRows.csv").getFile();
        csvMap.parseCsvFromFile(csvFilePath);
        assertEquals(6, csvMap.getNumberOfCSVRows());
    }

    @Test
    public void saveToFile() {
        EasyCSVMap csvMap = new EasyCSVMap(0);
        String csvFilePath = ClassLoader.getSystemResource("com/github/jep42/easycsvmap/header-0-five-lines.csv").getFile();
        csvMap.parseCsvFromFile(csvFilePath);
        for (int i = 0; i < 3; i++) {
            csvMap.setValues("{1}.Col" + i + "-Header", "4711-" + i);
            csvMap.setValues("{5}.Col" + i + "-Header", "0815-" + i);
        }

        String tempFilePath = this.createTempFile();
        try {
            csvMap.saveToFile(tempFilePath);

            csvMap = new EasyCSVMap(0);
            csvMap.parseCsvFromFile(tempFilePath);
            for (int i = 0; i < 3; i++) {
                assertEquals("4711-" + i, csvMap.getValues("{1}.Col" + i + "-Header").values().iterator().next());
                assertEquals("0815-" + i, csvMap.getValues("{5}.Col" + i + "-Header").values().iterator().next());
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

    @Test
    public void addRow() {
        EasyCSVMap csvMap = new EasyCSVMap(0);
        String csvFilePath = ClassLoader.getSystemResource("com/github/jep42/easycsvmap/header-0-five-lines.csv").getFile();
        csvMap.parseCsvFromFile(csvFilePath);

        String[] values= new String[]{"datacol0-line5", "datacol1-line5", "datacol2-line5"};
        csvMap.addRow(values);

        //five data rows + one header row + new row
        assertEquals(7, csvMap.getNumberOfCSVRows());
        assertEquals("datacol0-line5", csvMap.getValues("{6}.Col0-Header").values().iterator().next());
        assertEquals("datacol1-line5", csvMap.getValues("{6}.Col1-Header").values().iterator().next());
        assertEquals("datacol2-line5", csvMap.getValues("{6}.Col2-Header").values().iterator().next());
    }

    @Test(expected = CSVMapException.class)
    public void addRow_tooManyArgs() {
        EasyCSVMap csvMap = new EasyCSVMap(0);
        String csvFilePath = ClassLoader.getSystemResource("com/github/jep42/easycsvmap/header-0-five-lines.csv").getFile();
        csvMap.parseCsvFromFile(csvFilePath);

        String[] values= new String[]{"datacol0-line5", "datacol1-line5", "datacol2-line5", "datacol3-line5"};
        csvMap.addRow(values);
    }

    @Test(expected = CSVMapException.class)
    public void addRow_notEnoughArgs() {
        EasyCSVMap csvMap = new EasyCSVMap(0);
        String csvFilePath = ClassLoader.getSystemResource("com/github/jep42/easycsvmap/header-0-five-lines.csv").getFile();
        csvMap.parseCsvFromFile(csvFilePath);

        String[] values= new String[]{"datacol0-line5", "datacol1-line5"};
        csvMap.addRow(values);
    }


}
