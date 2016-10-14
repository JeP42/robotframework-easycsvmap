package de.jep.easycsv.easycsvmap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;

import de.jep.easycsv.easycsvmap.core.CSVContext;
import de.jep.easycsv.easycsvmap.csv.CSVFileFactory;
import de.jep.easycsv.easycsvmap.csv.CSVFileReader;
import de.jep.easycsv.easycsvmap.csv.CSVFileWriter;
import de.jep.easycsv.easycsvmap.selector.CSVSelector;
import de.jep.easycsv.easycsvmap.selector.CSVSelectorFactory;
import de.jep.easycsv.easycsvmap.util.FileUtil;

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
        CSVFileReader reader = null;
        try {
            reader = this.getReader(csvString);
            String[] nextLine;
            int lineIndex = 0;

            while ((nextLine = reader.readNextLine()) != null) {
                if (this.processHeaderLine(nextLine, lineIndex++)) {

                    break;
                }
            }

            this.validateHeaderLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            this.closeCSVReader(reader);
        }
    }

    private void validateHeaderLine() {
        // check if headerline exists...
        if (this.headerLine == null) {
            throw new RuntimeException("Failed to find the header line. Please check the specified header line index, maybe it does not exist in the given CSV format...");
        }

        // check for duplicate header columns:
        if (this.headerLineContainsDuplicates()) {
            throw new RuntimeException("The given header line is invalid as it contains duplicate column names");
        }
    }

    private boolean headerLineContainsDuplicates() {
        Set<String> colSet = new HashSet<String>(this.headerLine);
        return this.headerLine.size() != colSet.size();
    }

    private CSVFileReader getReader(String csvString) {
        return CSVFileFactory.getReader(csvString, this.csvContext.getColumnSeparator(), this.csvContext.getQuoteCharacter());
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
        CSVFileReader reader = null;
        try {
            reader = this.getReader(csvString);
            String[] nextLine;
            while ((nextLine = reader.readNextLine()) != null) {
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
            throw new RuntimeException("Number of elements of data line (" + csvLine.length + ") does not match the number of header columns (" + this.headerLine.size() + ").");
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
     * Convenient method to retrieve the value of particular cells from the CSV structure. These cells can be selected via a selector expression.
     * The syntax of the selector expression is <b>&lt;lineSpecification&gt;.&lt;columnName&gt;|&lt;columnIndex&gt;</b> Examples:
     * <li><b>2.name</b> => if a header line was specified then the columns can be access via its name. Hence, this expression would find the value of column 'name' of the third
     * line in the CSV (numeric line specification indicates a line index).</li>
     * <li><b>2.10</b> => if no header line was explicitly specified then the columns have to be accessed via index. Hence, this expression would find the value of the eleventh
     * column of the third line in the CSV.</li>
     * <p>
     * Beside simply using numeric line specifications it is also possible to use more complex ones, e.g. search a line via comparing column values with regular expressions.
     * Example:
     * <li><b>[name=^Foo.*$].name</b> => Would find all lines in which column 'name' has a value matching the given regular expression.
     * <p>
     * When using regular expressions as line specification it is possible that it matches more than just one line. hence, the method may return more then just one value.
     *
     * @param csvPath
     * @return Map where the line index is used as Map-key and the cells value as Map-value
     */
    @Nonnull
    public Map<Integer, String> getValues(String csvSelectorString) {
        CSVSelector csvSelector = CSVSelectorFactory.getCSVSelector(csvSelectorString, this.csvMap, this.csvContext);

        return csvSelector.getValues();
    }

    /**
     * Convenient method to set the values of particular cells in the CSV structure. These cells can be selected via a selector expression, see
     * {@link EasyCSVMap#getValue(String)}. The given value is set for all cells matching the given selector expressions.
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

        CSVFileWriter writer = null;
        try {
            writer = CSVFileFactory.getWriter(pathToCsv, this.csvContext.getColumnSeparator());

            Iterator<Map<String, String>> it = this.csvMap.iterator();
            while (it.hasNext()) {
                String[] values = it.next().values().toArray(new String[] {});
                writer.writeNextLine(values);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            closeCSVWriter(writer);
        }
    }

    private void closeCSVWriter(CSVFileWriter writer) {
        try {
            if (writer != null) {
                writer.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void closeCSVReader(CSVFileReader reader) {
        try {
            if (reader != null) {
                reader.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieves the total number of lines of the parsed CSV structure (including data lines and header lines)
     *
     * @return
     */
    public int getNumberOfCSVLines() {
        return this.csvMap.size();
    }

    /**
     * Retrieves the total number of columns of the parsed CSV structure
     *
     * @return
     */
    public int getNumberOfCSVColumns() {
        return this.headerLine.size();
    }

}
