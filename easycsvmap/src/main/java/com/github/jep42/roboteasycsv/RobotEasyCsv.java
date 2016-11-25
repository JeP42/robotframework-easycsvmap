package com.github.jep42.roboteasycsv;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.github.jep42.easycsvmap.EasyCSVMap;

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
 * | ``{2}.10`` | If no header row was specified then the column has to be selected via its index. Therefore, this expression would find the value of 11th column of the third row in the CSV. |
 * | ``[city=^Ka.*$].name`` | The row selector supports regular expressions to select lines with particular values in particular columns. This expression would select all lines with a value starting with "Ka" in the column name' |
 *
 */
public class RobotEasyCsv {

	public static final String ROBOT_LIBRARY_VERSION = "0.1";

    public static final String ROBOT_LIBRARY_SCOPE = "GLOBAL";

    private EasyCSVMap easyCsvMap;


    /**
     * Parse a CSV file from the given path
     *
     * Arguments:
	 * - _pathToCsv_: path to the CSV file
     * - _headerLineIndex_: the line index of the header line within CSV. Set to -1 if no header line exists.
     *
     * Example:
	 * | Parse Csv From File | {TEMPDIR}/download.csv | 0 |
	 *
     */
    public void parseCsvFromFile(String pathToCsv, int headerLineIndex) {
        this.easyCsvMap = new EasyCSVMap(headerLineIndex);
        this.easyCsvMap.parseCsvFromFile(pathToCsv);
    }

    /**
     * Set the value of a the cell(s) matching the given selector expression.
     *
     * Arguments:
	 * - _selector_: a valid CSV selector expression
	 * - _value_: the value to be set
	 *
	 * Example:
	 * | Set Csv Values | {1}.name | Peter Pan |
	 *
     */
    public void setCsvValues(String selector, String value) {
        this.checkInitialized();
        this.easyCsvMap.setValues(selector, value);
    }

    private void checkInitialized() {
        if (this.easyCsvMap == null) {
            throw new RobotCsvException("EasyCsv has to be initialized via parseCsvFromFile first");
        }
    }

    /**
     * Get the value of the first cell matching the given selector expression.
     *
     * Arguments:
	 * - _selector_: a valid CSV selector expression
	 *
	 * Example:
	 * | {name}= | Get First Csv Value | {1}.name |
	 *
     */
    public String getFirstCsvValue(String selector) {
        this.checkInitialized();

        // cannot return Map to robot, so just return the value
        Iterator<String> it = this.easyCsvMap.getValues(selector).values().iterator();
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
	 * - _selector_: a valid CSV selector expression
	 *
	 * Example:
	 * | &{name}= | Parse Csv From File | [city=^Karlsruhe*$].name |
	 *
     */
    public List<String> getAllCsvValues(String selector) {
        this.checkInitialized();
        // cannot return Map to robot, so just return the values
        return new ArrayList<>(this.easyCsvMap.getValues(selector).values());
    }

    /**
     * Save the CSV to file.
     *
     * Arguments:
	 * - _filePath_: target path to the CSV file
	 *
	 * Example:
	 * | Save Csv To File | {TEMPDIR}/myFile.csv |
	 *
     */
    public void saveCsvToFile(String filePath) {
        this.checkInitialized();
        this.easyCsvMap.saveToFile(filePath);
    }

}
