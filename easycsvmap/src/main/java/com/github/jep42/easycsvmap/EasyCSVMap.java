package com.github.jep42.easycsvmap;

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

import org.apache.commons.io.ByteOrderMark;

import com.github.jep42.easycsvmap.core.CSVContext;
import com.github.jep42.easycsvmap.core.CSVMapException;
import com.github.jep42.easycsvmap.csv.CSVFileFactory;
import com.github.jep42.easycsvmap.csv.api.CSVFileReader;
import com.github.jep42.easycsvmap.csv.api.CSVFileWriter;
import com.github.jep42.easycsvmap.csv.io.CSVFile;
import com.github.jep42.easycsvmap.selector.CSVSelectorFactory;
import com.github.jep42.easycsvmap.selector.api.CSVSelector;
import com.github.jep42.easycsvmap.util.CSVMapUtil;
import com.github.jep42.easycsvmap.util.FileUtil;

/**
 * EasyCSVMap allows parsing CSV files and accessing elements via name and/or index.
 */
public class EasyCSVMap {

    private static final String UNEXPECTED_EXCEPTION_MESSAGE = "An unexpected exception occured. See stacktrace for details about the root cause. message: %s";

    private CSVContext csvContext;

    /** ByteOrderMark from parsed CSV file. Might be <code>NULL</code> if the file did not contain any BOM or if the object was not parsed from a file. */
    private ByteOrderMark bom;

    private LinkedList<String> headerRow;

    private List<Map<String, String>> csvMap = new ArrayList<>();

    /**
     * Creates EasyCSVMap object.
     * By specifying a header row via its row index, it is possible to access columns of the CSV format by its column name.
     *
     * @param headerRowIndex An arbitrary row in the CSV can be used as header row.
     */
    public EasyCSVMap(int headerRowIndex) {
        this(new CSVContext(headerRowIndex));
    }

    /**
     * Creates EasyCSVMap object.
     * This constructor is appropriate if the CSV format does only contain data rows but not a header row. If a header row exists in the CSV format use
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
     * @return the parsed CSV
     */
    public List<Map<String, String>> parseCsvFromFile(String csvFilePath) {
        try {
            CSVFile file = FileUtil.loadFile(csvFilePath);

            this.bom = file.getBom();   // Store ByteOrderMark from input file so we can re-add it in the output file

            return this.parseCsv(file.getContent());
        } catch (IOException e) {
            throw new CSVMapException(String.format(UNEXPECTED_EXCEPTION_MESSAGE, e.getMessage()), e);
        }
    }

    /**
     * Parses the given CSV format
     *
     * @param csvString CSV format as String
     * @return the parsed CSV
     */
    public List<Map<String, String>> parseCsv(String csvString) {

        // find the header row or create a pseudo header row
        this.findHeaderRow(csvString);

        // parse rows and put values into internal map
        return this.processRows(csvString);
    }

    private void findHeaderRow(String csvString) {
        CSVFileReader reader = null;
        try {
            reader = this.getReader(csvString);
            String[] nextRow;
            int rowIndex = 0;

            while ((nextRow = reader.readNextLine()) != null) {
                if (this.processHeaderRow(nextRow, rowIndex++)) {

                    break;
                }
            }

            this.validateHeaderRow();
        } catch (IOException e) {
            throw new CSVMapException(String.format(UNEXPECTED_EXCEPTION_MESSAGE, e.getMessage()), e);
        } finally {
            this.closeCSVReader(reader);
        }
    }

    private void validateHeaderRow() {
        // check if header row exists...
        if (this.headerRow == null) {
            throw new CSVMapException("Failed to find the header row. Please check the specified header row index, maybe it does not exist in the given CSV format...");
        }

        // check for duplicate header columns:
        if (this.headerRowContainsDuplicates()) {
            throw new CSVMapException("The given header row is invalid as it contains duplicate column names");
        }
    }

    private boolean headerRowContainsDuplicates() {
        Set<String> colSet = new HashSet<>(this.headerRow);
        return this.headerRow.size() != colSet.size();
    }

    private CSVFileReader getReader(String csvString) {
        return CSVFileFactory.getReader(csvString, this.csvContext.getColumnSeparator(), this.csvContext.getQuoteCharacter());
    }

    private boolean processHeaderRow(String[] csvRow, int rowIndex) {
        if (csvContext.hasHeaderRow()) {
            if (rowIndex == this.csvContext.getHeaderRowIndex()) {
                this.headerRow = this.asLinkedList(csvRow);
                return true;
            }
        } else {
            this.headerRow = this.createStandardHeaderRow(csvRow.length);
            return true;
        }
        return false;
    }

    private LinkedList<String> asLinkedList(String[] csvRow) {
        LinkedList<String> l = new LinkedList<>();
        for (String v : csvRow) {
            l.add(v);
        }
        return l;
    }

    private List<Map<String, String>> processRows(String csvString) {
        CSVFileReader reader = null;
        try {
            reader = this.getReader(csvString);
            String[] nextRow;
            while ((nextRow = reader.readNextLine()) != null) {
            	//don't mind about empty lines, ignore them silently...
            	if (this.isEmptyLine(nextRow)) {
            		continue;
            	}
                this.processDataRow(nextRow);
            }
        } catch (IOException e) {
            throw new CSVMapException(String.format(UNEXPECTED_EXCEPTION_MESSAGE, e.getMessage()), e);
        } finally {
            this.closeCSVReader(reader);
        }

        return this.csvMap;
    }

    private boolean isEmptyLine(String[] row) {
		//for an empty line the OpenCSV reader returns an array with one empty element
		return row.length == 1 && CSVMapUtil.isEmpty(row[0]);
	}

	private void processDataRow(String[] csvRow) {

        this.validateDataRow(csvRow);

        Map<String, String> dataRow = new LinkedHashMap<>();
        int rowIndex = 0;
        for (String value : csvRow) {
            dataRow.put(this.headerRow.get(rowIndex++), value);
        }

        this.csvMap.add(dataRow);
    }

    /**
     * A row is considered to be invalid if the number of columns differs from the number of columns of the header row
     *
     * @param csvRow
     * @throws CSVMapException Thrown if the number of elements in the given array does not match the number of header rows
     */
    private void validateDataRow(String[] csvRow) {
        if (csvRow.length != this.headerRow.size()) {
            throw new CSVMapException("Number of elements of data row (" + csvRow.length + ") does not match the number of header columns (" + this.headerRow.size() + ").");
        }
    }

    private LinkedList<String> createStandardHeaderRow(int numberOfColumns) {
        LinkedList<String> standardHeaderRow = new LinkedList<>();
        int colIndex = 0;
        while (colIndex < numberOfColumns) {
            standardHeaderRow.add(Integer.toString(colIndex++));
        }

        return standardHeaderRow;
    }

    /**
     * Convenient method to retrieve the value of particular cells from the CSV structure. These cells can be selected via a selector expression.
     * The syntax of the selector expression is <b>&lt;rowSpecification&gt;.&lt;columnName&gt;|&lt;columnIndex&gt;</b> Examples:
     * <ul><li><b>{2}.name</b> => if a header row was specified then the columns can be access via its name. Hence, this expression would find the value of column 'name' of the third
     * row in the CSV (numeric row specification indicates a row index).</li>
     * <li><b>{2}.10</b> => if no header row was explicitly specified then the columns have to be accessed via index. Hence, this expression would find the value of the eleventh
     * column of the third row in the CSV.</li>
     * <p>
     * Beside simply using numeric row specifications it is also possible to use more complex ones, e.g. search a row via comparing column values with regular expressions.
     * Example:
     * <li><b>[name=^Foo.*$].name</b> => Would find all rows in which column 'name' has a value matching the given regular expression.
     * </ul><p>
     * When using regular expressions as row specification it is possible that it matches more than just one row. hence, the method may return more then just one value.
     *
     * @param csvPath
     * @return Map where the row index is used as Map-key and the cells value as Map-value
     */
    @Nonnull
    public Map<Integer, String> getValues(String csvSelectorString) {
        CSVSelector csvSelector = CSVSelectorFactory.getCSVSelector(csvSelectorString, this.csvMap, this.csvContext);

        return csvSelector.getValues();
    }

    /**
     * Convenient method to set the values of particular cells in the CSV structure. These cells can be selected via a selector expression, see
     * {@link EasyCSVMap#getValue(String)}. The given value is set for all cells matching the given selector expressions.
     * If a header row was specified the corresponding row is read-only. An exception is thrown when trying the change a value of the header row.
     *
     * @param csvPath
     */
    public void setValues(String csvSelectorString, String value) {
        CSVSelector csvSelector = CSVSelectorFactory.getCSVSelector(csvSelectorString, this.csvMap, this.csvContext);

        csvSelector.setValues(value);
    }

	/**
	 * An iterator over the rows of the CSV file, including a potentially existing header line.
	 *
	 * @return
	 */
    public Iterator<Map<String, String>> getRowIterator() {
    	return this.csvMap.iterator();
    }

    /**
     * Append a new row to the end of the CSV.
     *
     * @param values Column values from left to right
     * @throws CSVMapException Thrown if the number of elements in the given array does not match the number of header rows
     */
    public void addRow(String... values) {
    	this.processDataRow(values);
    }

    /**
     * Writes the CSV structure to file
     *
     * @param pathToCsv
     */
    public void saveToFile(String pathToCsv) {

        CSVFileWriter writer = null;
        try {
            writer = CSVFileFactory.getWriter(pathToCsv, bom, this.csvContext.getColumnSeparator(), this.csvContext.getQuoteCharacter(), this.csvContext.getLineEnd());

            Iterator<Map<String, String>> it = this.csvMap.iterator();
            while (it.hasNext()) {
                String[] values = it.next().values().toArray(new String[] {});
                writer.writeNextLine(values);
            }

        } catch (IOException e) {
            throw new CSVMapException(UNEXPECTED_EXCEPTION_MESSAGE, e);
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
            throw new CSVMapException(UNEXPECTED_EXCEPTION_MESSAGE, e);
        }
    }

    private void closeCSVReader(CSVFileReader reader) {
        try {
            if (reader != null) {
                reader.close();
            }
        } catch (IOException e) {
            throw new CSVMapException(UNEXPECTED_EXCEPTION_MESSAGE, e);
        }
    }

    /**
     * Retrieves the total number of rows of the parsed CSV structure (including data rows and header rows)
     *
     * @return
     */
    public int getNumberOfCSVRows() {
        return this.csvMap.size();
    }

    /**
     * Retrieves the total number of columns of the parsed CSV structure
     *
     * @return
     */
    public int getNumberOfCSVColumns() {
        return this.headerRow.size();
    }

}
