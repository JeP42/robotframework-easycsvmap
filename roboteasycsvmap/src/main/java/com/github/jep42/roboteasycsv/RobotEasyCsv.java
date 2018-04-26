package com.github.jep42.roboteasycsv;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.github.jep42.easycsvmap.EasyCSVMap;
import com.github.jep42.easycsvmap.core.CSVContext;

/**
 *
 * This library provides keywords to read / write values from CSV files using selector expressions.
 * Basically, the selector expression consists of two elements: a *row selector* and a *column selector* in the format <rowSelector>.<columnSelector>
 *
 * *Row Selector*
 *
 * Rows can be selected directly via row index or via a regular expression. Obviously, a row index always matches exactly one row in the CSV whereas a regular expression may match several rows.
 *
 * *Column Selector*
 *
 * The column can be selected via its name as long as the the CSV contains a header row and the row index of the header row was properly set when parsing the CSV file via `Parse Csv From File`.
 * If the CSV does not contain a header row the column index has to be used as selector.
 *
 * Example selector expressions:
 *
 * | *Selector* | *Comment* |
 * | ``{2}.name`` | If a header row was specified then columns can be accessed via their names. Hence, this expression would find the value of column 'name' of the third row in the CSV. |
 * | ``{*}.name`` | The row selector supports wildcard, so this expression would select all rows and therefore finds all values of column 'name' |
 * | ``{2,3,4}.name`` | A list of row indexes can be used to select particular rows by index. This expression would select column 'name' of rows with index 2, 3 and 4 |
 * | ``{2}.10`` | If no header row was specified then the column has to be selected via its index. Therefore, this expression would find the value of 11th column of the third row in the CSV. |
 * | ``[city=^Ka.*$].name`` | The row selector supports regular expressions to select lines with particular values in particular columns. This expression would select all lines with a value starting with "Ka" in the column name' |
 *
 */
public class RobotEasyCsv {

    public static final String ROBOT_LIBRARY_VERSION = "0.2";

    //ROBOT_LIBRARY_SCOPE is actually not supported for java remote libraries, scope is always GLOBAL in such a scenario...
    public static final String ROBOT_LIBRARY_SCOPE = "GLOBAL";

    private Map<Integer, EasyCSVMap> csvMaps = new ConcurrentHashMap<Integer, EasyCSVMap>();


    /**
     * Parse a CSV file from the given path and creates a new CSV session object identified by the given Session ID.
     * The Session ID has to be unique and is used in subsequent calls to specify the CSV session.
     *
     * Arguments:
     * - _sessionId_: unique ID for new CSV session
     * - _pathToCsv_: path to the CSV file
     * - _headerLineIndex_: the line index of the header line within CSV. Set to -1 if no header line exists.
     *
     * Example:
     * | Parse Csv From File | 4711 | {TEMPDIR}/download.csv | 0 |
     *
     */
    public void parseCsvFromFile(Integer sessionId, String pathToCsv, int headerLineIndex) {
        EasyCSVMap easyCSVMap = new EasyCSVMap(headerLineIndex);
        easyCSVMap.parseCsvFromFile(pathToCsv);
        this.csvMaps.put(sessionId, easyCSVMap);
    }

    /**
     * Parse a CSV file from the given path and creates a new CSV session object identified by the given Session ID.
     * The Session ID has to be unique and is used in subsequent calls to specify the CSV session.
     *
     * Arguments:
     * - _sessionId_: unique ID for new CSV session
     * - _pathToCsv_: path to the CSV file
     * - _headerLineIndex_: the line index of the header line within CSV. Set to -1 if no header line exists.
     * - _columnSeparator_: the column separator character
     * - _quoteCharacter_: the character used to quote values
     * - _lineEnd_: Specifies the character sequence used to terminate a line, this parameter is only used for save operation. In control characters backslashes have to be escaped (e.g. for new line character use \\n )
     *
     * Example:
     * | Parse Csv From File | 4711 | {TEMPDIR}/download.csv | 0 | ; | " | \\r\\n
     *
     */
    public void parseCsvFromFile(Integer sessionId, String pathToCsv, int headerLineIndex, String columnSeparator, String quoteCharacter, String lineEndEscaped) {
        EasyCSVMap easyCSVMap = new EasyCSVMap(
                this.getCsvContext(headerLineIndex, columnSeparator.toCharArray()[0], quoteCharacter.toCharArray()[0], this.removeEscapeCharactersFrom(lineEndEscaped)));
        easyCSVMap.parseCsvFromFile(pathToCsv);
        this.csvMaps.put(sessionId, easyCSVMap);
    }

    private String removeEscapeCharactersFrom(String lineEndEscaped) {
        lineEndEscaped = lineEndEscaped.replaceAll("\\\\r", "\r");
        lineEndEscaped = lineEndEscaped.replaceAll("\\\\n", "\n");

        return lineEndEscaped;
    }

    private CSVContext getCsvContext(int headerLineIndex, char columnSeparator, char quoteCharacter, String lineEnd) {
        CSVContext csvContext = new CSVContext(headerLineIndex);
        csvContext.setColumnSeparator(columnSeparator);
        csvContext.setQuoteCharacter(quoteCharacter);
        csvContext.setLineEnd(lineEnd);

        return csvContext;
    }

    /**
     * Set the value of a the cell(s) matching the given selector expression.
     *
     * Arguments:
     * - _sessionId_:  ID of a previously created CSV session
     * - _selector_: a valid CSV selector expression
     * - _value_: the value to be set
     *
     * Example:
     * | Set Csv Values | 4711 | {1}.name | Peter Pan |
     *
     */
    public void setCsvValues(Integer sessionId, String selector, String value) {
        this.checkInitialized(sessionId);
        this.csvMaps.get(sessionId).setValues(selector, value);
    }

    private void checkInitialized(Integer sessionId) {
         if (this.csvMaps.get(sessionId) == null) {
             throw new RobotCsvException(String.format("EasyCSV was not properly initialized for the given session ID %s", sessionId));
         }
    }

    /**
     * Get the value of the first cell matching the given selector expression.
     *
     * Arguments:
     * - _sessionId_:  ID of a previously created CSV session
     * - _selector_: a valid CSV selector expression
     *
     * Example:
     * | {name}= | Get First Csv Value | 4711 | {1}.name |
     *
     */
    public String getFirstCsvValue(Integer sessionId, String selector) {
        this.checkInitialized(sessionId);

        // cannot return Map to robot, so just return the value
        Iterator<String> it = this.csvMaps.get(sessionId).getValues(selector).values().iterator();
        if (it.hasNext()) {
            return it.next();
        } else {
            return null;
        }
    }

    /**
     * Get the values of all cells matching the given selector expression.<p>
     * The keyword returns list of values without further information about the corresponding source cells.
     *
     * Arguments:
     * - _sessionId_:  ID of a previously created CSV session
     * - _selector_: a valid CSV selector expression
     *
     * Example:
     * | &{name}= | Get All Csv Values | 4711 | [city=^Karlsruhe*$].name |
     *
     */
    public List<String> getAllCsvValues(Integer sessionId, String selector) {
        this.checkInitialized(sessionId);
        // cannot return Map to robot, so just return the values
        return new ArrayList<>(this.csvMaps.get(sessionId).getValues(selector).values());
    }

    /**
     * Save the CSV to file.
     *
     * Arguments:
     * - _sessionId_:  ID of a previously created CSV session
     * - _filePath_: target path to the CSV file
     *
     * Example:
     * | Save Csv To File | 4711 | {TEMPDIR}/myFile.csv |
     *
     */
    public void saveCsvToFile(Integer sessionId, String filePath) {
        this.checkInitialized(sessionId);
        this.csvMaps.get(sessionId).saveToFile(filePath);
    }

    /**
     * Add a row to CSV.
     *
     * Arguments:
     * - _sessionId_:  ID of a previously created CSV session
     * - _values_: The values of the new row
     *
     * Example:
     * | Add Row | 4711 | col1value | col2value | col3value |
     *
     */
    public void addRow(Integer sessionId, String... values) {
        this.checkInitialized(sessionId);
        this.csvMaps.get(sessionId).addRow(values);
    }

    /**
     * Removes the CSV session identified by the given session ID.
     *
     * Arguments:
     * - _sessionId_:  ID of a previously created CSV session
     *
     * Example:
     * | Remove CSV Session | 4711 |
     *
     */
    public void removeCsvSession(Integer sessionId) {
        this.checkInitialized(sessionId);
        this.csvMaps.remove(sessionId);
    }

}
