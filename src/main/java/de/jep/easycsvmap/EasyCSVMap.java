package de.jep.easycsvmap;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

/**
 * EasyCSVMap allows parsing CSV files and accessing elements via name and/or index.
 */
public class EasyCSVMap {

    private CSVContext csvContext;

    private LinkedList<String> headerLine;

    private List<Map<String, String>> csvMap = new ArrayList<>();

    /**
     * Creates EasyCSVMap object.
     * By specifying a header line via its line index, it is possible to access columns of the CSV format by its column name.
     *
     * @param headerLineIndex An arbitrary line in the CSV can be used as header line.
     */
    public EasyCSVMap(int headerLineIndex) {
        this(new CSVContext(headerLineIndex));
    }

    /**
     * Creates EasyCSVMap object.
     * This constructor is appropriate if the CSV format does only contain data lines but not a header line. If a header line exists in the CSV format use
     * {@link EasyCSVMap#EasyCSVMap(int)} to construct the EasyCSVMap object.
     */
    public EasyCSVMap() {
        this(new CSVContext(CSVContext.NO_HEADER_INDEX));
    }

    /**
     * Creates EasyCSVMap object.
     * This constructor allows custom format settings via CSVContext instance
     *
     */
    public EasyCSVMap(CSVContext csvContext) {
        super();
        this.csvContext = csvContext;
    }

    /**
     * Parses the given CSV file
     *
     * @param csvFilePath path to CSV file
     * @return
     */
    public List<Map<String, String>> parseCsvFromFile(String csvFilePath) {
        return this.parseCsv(FileUtil.loadFile(csvFilePath));
    }

    /**
     * Parses the given CSV format
     *
     * @param csvString CSV format as String
     * @return
     */
    public List<Map<String, String>> parseCsv(String csvString) {

        // find the header line or create a pseudo header line
        this.findHeaderLine(csvString);

        // parse lines and put values into internal map
        return this.processLines(csvString);
    }

    private void findHeaderLine(String csvString) {
        CSVReader reader = null;
        try {
            reader = this.getReader(csvString);
            String[] nextLine;
            int lineIndex = 0;

            while ((nextLine = reader.readNext()) != null) {
                if (this.processHeaderLine(nextLine, lineIndex++)) {
                    return;
                }
            }

            throw new RuntimeException("Failed to find the header line. Please check the specified header line index, maybe it does not exist in the given CSV format...");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            this.closeCSVReader(reader);
        }
    }

    private CSVReader getReader(String csvString) {
        return new CSVReader(new StringReader(csvString), this.csvContext.getColumnSeparator(), this.csvContext.getQuoteCharacter());
    }

    private boolean processHeaderLine(String[] csvLine, int lineIndex) {
        if (csvContext.hasHeaderLine()) {
            if (lineIndex == this.csvContext.getHeaderLineIndex()) {
                this.headerLine = this.asLinkedList(csvLine);
                return true;
            }
        } else {
            this.headerLine = this.createStandardHeaderLine(csvLine.length);
            return true;
        }
        return false;
    }

    private LinkedList<String> asLinkedList(String[] csvLine) {
        LinkedList<String> l = new LinkedList<>();
        for (String v : csvLine) {
            l.add(v);
        }
        return l;
    }

    private List<Map<String, String>> processLines(String csvString) {
        CSVReader reader = null;
        try {
            reader = this.getReader(csvString);
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                this.processDataLine(nextLine);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            this.closeCSVReader(reader);
        }

        return this.csvMap;
    }

    private void processDataLine(String[] csvLine) {

        this.validateDataLine(csvLine);

        Map<String, String> dataLine = new LinkedHashMap<>();
        int lineIndex = 0;
        for (String value : csvLine) {
            dataLine.put(this.headerLine.get(lineIndex++), value);
        }

        this.csvMap.add(dataLine);
    }

    /**
     * A line is considered to be invalid if the number of columns differs from the number of columns of the header line
     *
     * @param csvLine
     */
    private void validateDataLine(String[] csvLine) {
        if (csvLine.length != this.headerLine.size()) {
            throw new RuntimeException("Number of elements of data line (" + csvLine.length + ")does not match the number of header columns (" + this.headerLine.size() + ").");
        }
    }

    private LinkedList<String> createStandardHeaderLine(int numberOfColumns) {
        LinkedList<String> standardHeaderLine = new LinkedList<>();
        int colIndex = 0;
        while (colIndex < numberOfColumns) {
            standardHeaderLine.add("" + (colIndex++));
        }

        return standardHeaderLine;
    }

    /**
     * Convenient method to retrieve the value of a particular element from the CSV structure. This element can be selected via a selector expression.
     * The syntax of the selector expression is <b>&lt;lineSpecification&gt;.&lt;columnName&gt;|&lt;columnIndex&gt;</b> Examples:
     * <li><b>2.name</b> => if a header line was specified then the columns can be access via its name. Hence, this expression would find the value of column 'name' of the third
     * line in the CSV (numeric line specification indicates a line index).</li>
     * <li><b>2.10</b> => if no header line was explicitly specified then the columns have to be accessed via index. Hence, this expression would find the value of the eleventh
     * column of the third line in the CSV.</li>
     * <p>
     * Beside simply using numeric line specifications it is also possible to use more complex ones, e.g. search line via comparing column values with regular expressions
     * <li><b>[name=^Foo.*$].name</b> => Would find the line in which column 'name' has a value matching the given regular expression.
     *
     *
     * @param csvPath
     * @return
     */
    @Deprecated
    public String getValue(String csvSelector) {
        Map<Integer, String> values = this.getValues(csvSelector);
        return values.values().iterator().next();
    }

    public Map<Integer, String> getValues(String csvSelectorString) {
        CSVSelector csvSelector = CSVSelectorFactory.getCSVSelector(csvSelectorString, this.csvMap, this.csvContext);

        return csvSelector.getValues();
    }

    /**
     * Convenient method to set the value of a particular element from the CSV structure. This element can be selected via a selector expression, see
     * {@link EasyCSVMap#getValue(String)}
     * If a header line was specified the corresponding line is read-only. An exception is thrown when trying the change a value of the header line.
     *
     * @param csvPath
     * @return
     */
    public void setValues(String csvSelectorString, String value) {
        CSVSelector csvSelector = CSVSelectorFactory.getCSVSelector(csvSelectorString, this.csvMap, this.csvContext);

        csvSelector.setValues(value);
    }

    /**
     * Writes the CSV structure to file
     *
     * @param pathToCsv
     */
    public void saveToFile(String pathToCsv) {
        CSVWriter writer = null;
        try {
            writer = new CSVWriter(new FileWriter(pathToCsv), this.csvContext.getColumnSeparator());

            Iterator<Map<String, String>> it = this.csvMap.iterator();
            while (it.hasNext()) {
                String[] values = it.next().values().toArray(new String[] {});
                writer.writeNext(values);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            closeCSVWriter(writer);
        }
    }

    private void closeCSVWriter(CSVWriter writer) {
        try {
            if (writer != null) {
                writer.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void closeCSVReader(CSVReader reader) {
        try {
            if (reader != null) {
                reader.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int getNumberOfCSVLines() {
        return this.csvMap.size();
    }

    public int getNumberOfCSVColumns() {
        return this.headerLine.size();
    }

}
