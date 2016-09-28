package de.jep.easycsvmap;

import java.util.Map;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EasyCSVMapwithRegExpLineSelectorTest {

    @Test(expected = RuntimeException.class)
    public void validation_invalidFormat() {
        // line with index=0 is the header line
        EasyCSVMap csvMap = new EasyCSVMap(0);
        String csvFilePath = ClassLoader.getSystemResource("de/jep/easycsvmap/header-0-five-lines.csv").getFile();

        csvMap.parseCsvFromFile(csvFilePath);

        csvMap.getValues("[1]");
    }

    @Test(expected = RuntimeException.class)
    public void validation_invalidFormat1() {
        // line with index=0 is the header line
        EasyCSVMap csvMap = new EasyCSVMap(0);
        String csvFilePath = ClassLoader.getSystemResource("de/jep/easycsvmap/header-0-five-lines.csv").getFile();

        csvMap.parseCsvFromFile(csvFilePath);

        csvMap.getValues("Col0-Header=^datacol0-line.*$].Col1-Header");
    }

    @Test(expected = RuntimeException.class)
    public void validation_invalidFormat2() {
        // line with index=0 is the header line
        EasyCSVMap csvMap = new EasyCSVMap(0);
        String csvFilePath = ClassLoader.getSystemResource("de/jep/easycsvmap/header-0-five-lines.csv").getFile();

        csvMap.parseCsvFromFile(csvFilePath);

        csvMap.getValues("[Col0-Header=^datacol0-line.*$.Col1-Header");
    }

    @Test(expected = RuntimeException.class)
    public void validation_invalidLineSpecMissingEqual() {
        // line with index=0 is the header line
        EasyCSVMap csvMap = new EasyCSVMap(0);
        String csvFilePath = ClassLoader.getSystemResource("de/jep/easycsvmap/header-0-five-lines.csv").getFile();

        csvMap.parseCsvFromFile(csvFilePath);

        csvMap.getValues("[Col0-Header^aaa$].Col1-Header");
    }

    @Test(expected = RuntimeException.class)
    public void validation_invalidLineSpecMissingCol() {
        // line with index=0 is the header line
        EasyCSVMap csvMap = new EasyCSVMap(0);
        String csvFilePath = ClassLoader.getSystemResource("de/jep/easycsvmap/header-0-five-lines.csv").getFile();

        csvMap.parseCsvFromFile(csvFilePath);

        csvMap.getValues("[=^aaa$].Col1-Header");
    }

    @Test(expected = RuntimeException.class)
    public void validation_invalidLineSpecMissingRegExp() {
        // line with index=0 is the header line
        EasyCSVMap csvMap = new EasyCSVMap(0);
        String csvFilePath = ClassLoader.getSystemResource("de/jep/easycsvmap/header-0-five-lines.csv").getFile();

        csvMap.parseCsvFromFile(csvFilePath);

        csvMap.getValues("[Col0-Header=].Col1-Header");
    }

    @Test(expected = RuntimeException.class)
    public void validation_invalidColSpec() {
        // line with index=0 is the header line
        EasyCSVMap csvMap = new EasyCSVMap(0);
        String csvFilePath = ClassLoader.getSystemResource("de/jep/easycsvmap/header-0-five-lines.csv").getFile();

        csvMap.parseCsvFromFile(csvFilePath);

        csvMap.getValues("[Col0-Header=1.*$].");
    }

    @Test
    public void getValue_viaColName() {
        // line with index=0 is the header line
        EasyCSVMap csvMap = new EasyCSVMap(0);
        String csvFilePath = ClassLoader.getSystemResource("de/jep/easycsvmap/header-0-five-lines.csv").getFile();

        csvMap.parseCsvFromFile(csvFilePath);

        // select column "Col1-Header" of all lines having a value in column "Col0-Header" matching ^datacol0-line.*$
        Map<Integer, String> values = csvMap.getValues("[Col0-Header=^datacol0-line.*$].Col1-Header");
        assertEquals(5, values.size());

        assertEquals("datacol1-line0", values.get(1));
        assertEquals("datacol1-line1", values.get(2));
        assertEquals("datacol1-line2", values.get(3));
        assertEquals("datacol1-line3", values.get(4));
        assertEquals("datacol1-line4", values.get(5));
    }

    @Test
    public void getValue_viaColIndex() {
        // line with index=0 is the header line
        EasyCSVMap csvMap = new EasyCSVMap();
        String csvFilePath = ClassLoader.getSystemResource("de/jep/easycsvmap/no-header-five-lines.csv").getFile();

        csvMap.parseCsvFromFile(csvFilePath);

        // select column "1" of all lines having a value in column "0" matching ^datacol0-line.*$
        Map<Integer, String> values = csvMap.getValues("[0=^datacol0-line.*$].1");
        assertEquals(5, values.size());

        assertEquals("datacol1-line0", values.get(0));
        assertEquals("datacol1-line1", values.get(1));
        assertEquals("datacol1-line2", values.get(2));
        assertEquals("datacol1-line3", values.get(3));
        assertEquals("datacol1-line4", values.get(4));
    }

    @Test
    public void setValue_viaColName() {
        EasyCSVMap csvMap = new EasyCSVMap(0);
        String csvFilePath = ClassLoader.getSystemResource("de/jep/easycsvmap/header-0-five-lines.csv").getFile();

        csvMap.parseCsvFromFile(csvFilePath);

        csvMap.setValues("[Col0-Header=^datacol0-line.*$].Col1-Header", "42");

        Map<Integer, String> values = csvMap.getValues("[Col0-Header=^datacol0-line.*$].Col1-Header");
        assertEquals(5, values.size());

        assertEquals("42", values.get(1));
        assertEquals("42", values.get(2));
        assertEquals("42", values.get(3));
        assertEquals("42", values.get(4));
        assertEquals("42", values.get(5));
    }

    @Test
    public void setValue_viaColIndex() {
        EasyCSVMap csvMap = new EasyCSVMap();
        String csvFilePath = ClassLoader.getSystemResource("de/jep/easycsvmap/no-header-five-lines.csv").getFile();

        csvMap.parseCsvFromFile(csvFilePath);

        csvMap.setValues("[0=^datacol0-line.*$].1", "42");

        Map<Integer, String> values = csvMap.getValues("[0=^datacol0-line.*$].1");
        assertEquals(5, values.size());

        assertEquals("42", values.get(0));
        assertEquals("42", values.get(1));
        assertEquals("42", values.get(2));
        assertEquals("42", values.get(3));
        assertEquals("42", values.get(4));
    }

    @Test(expected = RuntimeException.class)
    public void setValue_forHeaderLine() {
        EasyCSVMap csvMap = new EasyCSVMap(0);
        String csvFilePath = ClassLoader.getSystemResource("de/jep/easycsvmap/header-0-five-lines.csv").getFile();

        csvMap.parseCsvFromFile(csvFilePath);

        csvMap.setValues("[Col0-Header=^Col0.*$].Col1-Header", "never-change-header-cells");
    }

}
