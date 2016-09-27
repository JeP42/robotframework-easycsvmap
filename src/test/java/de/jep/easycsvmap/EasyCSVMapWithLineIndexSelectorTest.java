package de.jep.easycsvmap;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EasyCSVMapWithLineIndexSelectorTest {

    @Test
    public void getValue_viaColName() {
        // line with index=0 is the header line
        EasyCSVMap csvMap = new EasyCSVMap(0);
        String csvFilePath = ClassLoader.getSystemResource("de/jep/easycsvmap/header-0-five-lines.csv").getFile();

        csvMap.parseCsvFromFile(csvFilePath);

        // the first line
        assertEquals("datacol0-line0", csvMap.getValue("1.Col0-Header"));
        assertEquals("datacol1-line0", csvMap.getValue("1.Col1-Header"));
        assertEquals("datacol2-line0", csvMap.getValue("1.Col2-Header"));

        // the last line
        assertEquals("datacol0-line4", csvMap.getValue("5.Col0-Header"));
        assertEquals("datacol1-line4", csvMap.getValue("5.Col1-Header"));
        assertEquals("datacol2-line4", csvMap.getValue("5.Col2-Header"));
    }

    @Test
    public void getValue_viaColIndex() {
        // line with index=0 is the header line
        EasyCSVMap csvMap = new EasyCSVMap();
        String csvFilePath = ClassLoader.getSystemResource("de/jep/easycsvmap/no-header-five-lines.csv").getFile();

        csvMap.parseCsvFromFile(csvFilePath);

        // the first line
        assertEquals("datacol0-line0", csvMap.getValue("0.0"));
        assertEquals("datacol1-line0", csvMap.getValue("0.1"));
        assertEquals("datacol2-line0", csvMap.getValue("0.2"));

        // the last line
        assertEquals("datacol0-line4", csvMap.getValue("4.0"));
        assertEquals("datacol1-line4", csvMap.getValue("4.1"));
        assertEquals("datacol2-line4", csvMap.getValue("4.2"));
    }

    @Test
    public void setValue_viaColName() {
        EasyCSVMap csvMap = new EasyCSVMap(0);
        String csvFilePath = ClassLoader.getSystemResource("de/jep/easycsvmap/header-0-five-lines.csv").getFile();

        csvMap.parseCsvFromFile(csvFilePath);

        for (int i = 0; i < 3; i++) {
            csvMap.setValues("1.Col" + i + "-Header", "4711-" + i);
            csvMap.setValues("5.Col" + i + "-Header", "0815-" + i);
        }

        for (int i = 0; i < 3; i++) {
            assertEquals("4711-" + i, csvMap.getValue("1.Col" + i + "-Header"));
            assertEquals("0815-" + i, csvMap.getValue("5.Col" + i + "-Header"));
        }
    }

    @Test
    public void setValue_viaColIndex() {
        EasyCSVMap csvMap = new EasyCSVMap();
        String csvFilePath = ClassLoader.getSystemResource("de/jep/easycsvmap/no-header-five-lines.csv").getFile();

        csvMap.parseCsvFromFile(csvFilePath);

        for (int i = 0; i < 3; i++) {
            csvMap.setValues("0." + i, "4711-" + i);
            csvMap.setValues("4." + i, "0815-" + i);
        }

        for (int i = 0; i < 3; i++) {
            assertEquals("4711-" + i, csvMap.getValue("0." + i));
            assertEquals("0815-" + i, csvMap.getValue("4." + i));
        }
    }

    @Test(expected = RuntimeException.class)
    public void setValue_forHeaderLine() {
        EasyCSVMap csvMap = new EasyCSVMap(0);
        String csvFilePath = ClassLoader.getSystemResource("de/jep/easycsvmap/header-0-five-lines.csv").getFile();

        csvMap.parseCsvFromFile(csvFilePath);

        csvMap.setValues("0.Col0-Header", "never-change-header-cells");
    }

}
