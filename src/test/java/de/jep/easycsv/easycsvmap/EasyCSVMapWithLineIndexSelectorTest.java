package de.jep.easycsv.easycsvmap;

import java.util.Map;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

import de.jep.easycsv.easycsvmap.core.InvalidSelectorValueException;

public class EasyCSVMapWithLineIndexSelectorTest {


    @Test(expected = RuntimeException.class)
    public void validation_invalidFormat() {
        // line with index=0 is the header line
        EasyCSVMap csvMap = new EasyCSVMap(0);
        String csvFilePath = ClassLoader.getSystemResource("de/jep/easycsvmap/header-0-five-lines.csv").getFile();

        csvMap.parseCsvFromFile(csvFilePath);

        csvMap.getValues("{1}");
    }

    @Test(expected = RuntimeException.class)
    public void validation_invalidColumnSpec() {
        // line with index=0 is the header line
        EasyCSVMap csvMap = new EasyCSVMap(0);
        String csvFilePath = ClassLoader.getSystemResource("de/jep/easycsvmap/header-0-five-lines.csv").getFile();

        csvMap.parseCsvFromFile(csvFilePath);

        csvMap.getValues("{1}.");
    }

    @Test(expected = InvalidSelectorValueException.class)
    public void getValues_invalidColumnSpec() {
        EasyCSVMap csvMap = new EasyCSVMap(0);
        String csvFilePath = ClassLoader.getSystemResource("de/jep/easycsvmap/header-0-five-lines.csv").getFile();

        csvMap.parseCsvFromFile(csvFilePath);

        csvMap.getValues("{1}.NotExisingColumnSpec");
    }

    @Test(expected = RuntimeException.class)
    public void validation_missingLineSpec() {
        // line with index=0 is the header line
        EasyCSVMap csvMap = new EasyCSVMap(0);
        String csvFilePath = ClassLoader.getSystemResource("de/jep/easycsvmap/header-0-five-lines.csv").getFile();

        csvMap.parseCsvFromFile(csvFilePath);

        csvMap.getValues(".Col0-Header");
    }

    @Test(expected = RuntimeException.class)
    public void validation_emptyLineSpec() {
        // line with index=0 is the header line
        EasyCSVMap csvMap = new EasyCSVMap(0);
        String csvFilePath = ClassLoader.getSystemResource("de/jep/easycsvmap/header-0-five-lines.csv").getFile();

        csvMap.parseCsvFromFile(csvFilePath);

        csvMap.getValues("{}.Col0-Header");
    }

    @Test
    public void getValue_viaColName() {
        // line with index=0 is the header line
        EasyCSVMap csvMap = new EasyCSVMap(0);
        String csvFilePath = ClassLoader.getSystemResource("de/jep/easycsvmap/header-0-five-lines.csv").getFile();

        csvMap.parseCsvFromFile(csvFilePath);

        // the first line
        assertEquals("datacol0-line0", csvMap.getValues("{1}.Col0-Header").values().iterator().next());
        assertEquals("datacol1-line0", csvMap.getValues("{1}.Col1-Header").values().iterator().next());
        assertEquals("datacol2-line0", csvMap.getValues("{1}.Col2-Header").values().iterator().next());

        // the last line
        assertEquals("datacol0-line4", csvMap.getValues("{5}.Col0-Header").values().iterator().next());
        assertEquals("datacol1-line4", csvMap.getValues("{5}.Col1-Header").values().iterator().next());
        assertEquals("datacol2-line4", csvMap.getValues("{5}.Col2-Header").values().iterator().next());
    }

    @Test
    public void getValue_indexListViaColName() {
        // line with index=0 is the header line
        EasyCSVMap csvMap = new EasyCSVMap(0);
        String csvFilePath = ClassLoader.getSystemResource("de/jep/easycsvmap/header-0-five-lines.csv").getFile();

        csvMap.parseCsvFromFile(csvFilePath);

        // the first line
        Map<Integer, String> values = csvMap.getValues("{1,5}.Col0-Header");
        assertEquals(2, values.size());

        assertEquals("datacol0-line0", values.get(1));
        assertEquals("datacol0-line4", values.get(5));
    }

    @Test
    public void getValue_wildcardSelectionViaColName() {
        // line with index=0 is the header line
        EasyCSVMap csvMap = new EasyCSVMap(0);
        String csvFilePath = ClassLoader.getSystemResource("de/jep/easycsvmap/header-0-five-lines.csv").getFile();

        csvMap.parseCsvFromFile(csvFilePath);

        // the first line
        Map<Integer, String> values = csvMap.getValues("{*}.Col0-Header");
        assertEquals(6, values.size()); // header line also included!

        assertEquals("datacol0-line0", values.get(1));
        assertEquals("datacol0-line1", values.get(2));
        assertEquals("datacol0-line2", values.get(3));
        assertEquals("datacol0-line3", values.get(4));
        assertEquals("datacol0-line4", values.get(5));
    }

    @Test(expected = RuntimeException.class)
    public void getValue_missingLineSpec() {
        // line with index=0 is the header line
        EasyCSVMap csvMap = new EasyCSVMap(0);
        String csvFilePath = ClassLoader.getSystemResource("de/jep/easycsvmap/header-0-five-lines.csv").getFile();

        csvMap.parseCsvFromFile(csvFilePath);

        csvMap.getValues("{}.Col0-Header");
    }

    @Test(expected = RuntimeException.class)
    public void getValue_invalidLineSpec1() {
        // line with index=0 is the header line
        EasyCSVMap csvMap = new EasyCSVMap(0);
        String csvFilePath = ClassLoader.getSystemResource("de/jep/easycsvmap/header-0-five-lines.csv").getFile();

        csvMap.parseCsvFromFile(csvFilePath);

        csvMap.getValues("{1,2.Col0-Header");
    }

    @Test(expected = RuntimeException.class)
    public void getValue_invalidLineSpec2() {
        // line with index=0 is the header line
        EasyCSVMap csvMap = new EasyCSVMap(0);
        String csvFilePath = ClassLoader.getSystemResource("de/jep/easycsvmap/header-0-five-lines.csv").getFile();

        csvMap.parseCsvFromFile(csvFilePath);

        csvMap.getValues("1,2}.Col0-Header");
    }

    @Test(expected = RuntimeException.class)
    public void getValue_invalidIndexInList() {
        // line with index=0 is the header line
        EasyCSVMap csvMap = new EasyCSVMap(0);
        String csvFilePath = ClassLoader.getSystemResource("de/jep/easycsvmap/header-0-five-lines.csv").getFile();

        csvMap.parseCsvFromFile(csvFilePath);

        csvMap.getValues("{1,2,aa,3}.Col0-Header");
    }

    @Test(expected = RuntimeException.class)
    public void getValue_indexListMixedWithWilcard() {
        // line with index=0 is the header line
        EasyCSVMap csvMap = new EasyCSVMap(0);
        String csvFilePath = ClassLoader.getSystemResource("de/jep/easycsvmap/header-0-five-lines.csv").getFile();

        csvMap.parseCsvFromFile(csvFilePath);

        csvMap.getValues("{1,2,*,4}.Col0-Header");
    }

    @Test
    public void getValue_dontFailIfLineIndexDoesNotExist() {
        // line with index=0 is the header line
        EasyCSVMap csvMap = new EasyCSVMap(0);
        String csvFilePath = ClassLoader.getSystemResource("de/jep/easycsvmap/header-0-five-lines.csv").getFile();

        csvMap.parseCsvFromFile(csvFilePath);

        assertEquals(0, csvMap.getValues("{42}.Col0-Header").values().size());
    }

    @Test
    public void getValue_viaColIndex() {
        // line with index=0 is the header line
        EasyCSVMap csvMap = new EasyCSVMap();
        String csvFilePath = ClassLoader.getSystemResource("de/jep/easycsvmap/no-header-five-lines.csv").getFile();

        csvMap.parseCsvFromFile(csvFilePath);

        // the first line
        assertEquals("datacol0-line0", csvMap.getValues("{0}.0").values().iterator().next());
        assertEquals("datacol1-line0", csvMap.getValues("{0}.1").values().iterator().next());
        assertEquals("datacol2-line0", csvMap.getValues("{0}.2").values().iterator().next());

        // the last line
        assertEquals("datacol0-line4", csvMap.getValues("{4}.0").values().iterator().next());
        assertEquals("datacol1-line4", csvMap.getValues("{4}.1").values().iterator().next());
        assertEquals("datacol2-line4", csvMap.getValues("{4}.2").values().iterator().next());
    }

    @Test
    public void setValue_viaColName() {
        EasyCSVMap csvMap = new EasyCSVMap(0);
        String csvFilePath = ClassLoader.getSystemResource("de/jep/easycsvmap/header-0-five-lines.csv").getFile();

        csvMap.parseCsvFromFile(csvFilePath);

        csvMap.setValues("{1}.Col1-Header", "4711");
        csvMap.setValues("{5}.Col1-Header", "0815");

        assertEquals("4711", csvMap.getValues("{1}.Col1-Header").values().iterator().next());
        assertEquals("0815", csvMap.getValues("{5}.Col1-Header").values().iterator().next());
    }

    @Test
    public void setValue_viaColIndex() {
        EasyCSVMap csvMap = new EasyCSVMap();
        String csvFilePath = ClassLoader.getSystemResource("de/jep/easycsvmap/no-header-five-lines.csv").getFile();

        csvMap.parseCsvFromFile(csvFilePath);

        csvMap.setValues("{0}.1", "4711");
        csvMap.setValues("{2}.2", "4712");
        csvMap.setValues("{4}.3", "0815");

        assertEquals("4711", csvMap.getValues("{0}.1").values().iterator().next());
        assertEquals("4712", csvMap.getValues("{2}.2").values().iterator().next());
        assertEquals("0815", csvMap.getValues("{4}.3").values().iterator().next());
    }

    @Test(expected = RuntimeException.class)
    public void setValue_forHeaderLine() {
        EasyCSVMap csvMap = new EasyCSVMap(0);
        String csvFilePath = ClassLoader.getSystemResource("de/jep/easycsvmap/header-0-five-lines.csv").getFile();

        csvMap.parseCsvFromFile(csvFilePath);

        csvMap.setValues("{0}.Col0-Header", "never-change-header-cells");
    }

}
