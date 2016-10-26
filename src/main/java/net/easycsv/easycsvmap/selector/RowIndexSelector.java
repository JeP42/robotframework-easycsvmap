package net.easycsv.easycsvmap.selector;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Nonnull;

import net.easycsv.easycsvmap.core.CSVContext;
import net.easycsv.easycsvmap.core.CSVMapException;
import net.easycsv.easycsvmap.core.InvalidSelectorFormatException;
import net.easycsv.easycsvmap.util.CSVMapUtil;


public class RowIndexSelector extends AbstractCSVSelector {

    private static final char FORMAT_SEPARATOR_CHARACTER = '.';

    protected static final Object ALL_INDEXES_WILCARD_CHARACTER = "*";

    private List<Integer> selectedRowIndexList;

    private boolean allRowIndexes;

    private String columnSpec;


    public RowIndexSelector(String selector, List<Map<String, String>> csvMap, CSVContext csvContext) {
        super(selector, csvMap, csvContext);
    }


    @Override
    public void parse() throws InvalidSelectorFormatException {
        this.validateFormat();

        String[] selectorFragments = this.getSelectorFragments();

        new RowIndexFormatValidator(selectorFragments[0]).validate();
        new ColumnSpecFormatValidator(selectorFragments[1]).validate();

        // first part of the selector is the row specification
        String[] rowSpec = CSVMapUtil.removeBracesFromString(selectorFragments[0]).split(",");
        this.allRowIndexes = rowSpec.length == 1 && ALL_INDEXES_WILCARD_CHARACTER.equals(rowSpec[0]);
        this.selectedRowIndexList = this.getRowIndexListFromStringArray(rowSpec);

        // second part of the selector is the column specification
        this.columnSpec = selectorFragments[1];
    }

    /*
     * expect existence of a FORMAT_SEPARATOR_CHARACTER (.) in the selector expression
     */
    private void validateFormat() throws InvalidSelectorFormatException {
        int guessedSeparatorIdx = this.selector.lastIndexOf(FORMAT_SEPARATOR_CHARACTER);
        if (guessedSeparatorIdx == -1) {
            throw new InvalidSelectorFormatException("The given format " + this.selector + " does not match the expected format.");
        }
    }

    private String[] getSelectorFragments() {
        int guessedSeparatorIdx = this.selector.lastIndexOf(FORMAT_SEPARATOR_CHARACTER);
        return new String[] { this.selector.substring(0, guessedSeparatorIdx), this.selector.substring(guessedSeparatorIdx + 1) };
    }

    private List<Integer> getRowIndexListFromStringArray(String[] rowSpec) {
        List<Integer> indexList = new ArrayList<>();
        for (String idx : rowSpec) {
            if (!idx.equals(ALL_INDEXES_WILCARD_CHARACTER)) {
                indexList.add(Integer.parseInt(idx));
            }
        }
        return indexList;
    }


    @Override
    @Nonnull
    public Map<Integer, String> getValues() {
        Map<Integer, String> result = new TreeMap<>();
        int rowIndex = 0;
        for (Iterator<Map<String, String>> iter = this.csvMap.iterator(); iter.hasNext();) {
            Map<String, String> row = iter.next();
            if (this.isRowSelected(rowIndex)) {
                result.put(rowIndex, this.getValueFromRow(row, this.columnSpec));
            }
            rowIndex++;
        }

        return result;
    }



    private boolean isRowSelected(int rowIndex) {
        return this.allRowIndexes || this.selectedRowIndexList.contains(rowIndex);
    }

    @Override
    public int setValues(String value) {
        int affectedRows = 0;
        int rowsIndex = 0;
        for (Iterator<Map<String, String>> iter = this.csvMap.iterator(); iter.hasNext();) {
            Map<String, String> row = iter.next();
            if (this.isRowSelected(rowsIndex)) {
                this.validateWriteOperation(rowsIndex);
                row.put(this.columnSpec, value);
                affectedRows++;
            }
            rowsIndex++;
        }
        return affectedRows;
    }



    private void validateWriteOperation(int rowsIndex) {
        if (rowsIndex == this.csvContext.getHeaderRowIndex()) {
            throw new CSVMapException("It is not allowed to change values of the header row (row index " + rowsIndex + ")");
        }
    }


}
