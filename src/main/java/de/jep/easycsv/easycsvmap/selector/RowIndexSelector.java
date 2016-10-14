package de.jep.easycsv.easycsvmap.selector;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import de.jep.easycsv.easycsvmap.core.CSVContext;
import de.jep.easycsv.easycsvmap.core.InvalidSelectorFormatException;
import de.jep.easycsv.easycsvmap.util.CSVMapUtil;


public class RowIndexSelector implements CSVSelector {

    private static final char FORMAT_SEPARATOR_CHARACTER = '.';

    protected static final Object ALL_INDEXES_WILCARD_CHARACTER = "*";

    private String selector;

    private List<Map<String, String>> csvMap;

    private CSVContext csvContext;

    private List<Integer> selectedRowIndexList;

    private boolean allRowIndexes;

    private String columnSpec;


    public RowIndexSelector(String selector, List<Map<String, String>> csvMap, CSVContext csvContext) {
        this.selector = selector;
        this.csvMap = csvMap;
        this.csvContext = csvContext;
    }

    @Override
    public void setSelector(String csvSelectorString) {
        this.selector = csvSelectorString;
    }

    @Override
    public void setCSVContext(CSVContext csvContext) {
        this.csvContext = csvContext;
    }

    @Override
    public void setCSVMap(List<Map<String, String>> csvMap) {
        this.csvMap = csvMap;
    }


    @Override
    public boolean isValid() {
        // try to parse the given selector string
        try {
            this.parse();
        } catch (InvalidSelectorFormatException e) {
            // ignore this as
            return false;
        }

        return true;
    }

    @Override
    public void parse() throws InvalidSelectorFormatException {
        this.validateFormat();

        String[] selectorFragments = this.getSelectorFragments();
        new RowIndexFormatValidator(selectorFragments[0]).validate();
        new ColumnSpecFormatValidator(selectorFragments[1]).validate();

        // first part of the selector is the row specification
        String[] rowSpec = CSVMapUtil.removeBracesFromString(selectorFragments[0]).split(",");
        this.allRowIndexes = (rowSpec.length == 1 && ALL_INDEXES_WILCARD_CHARACTER.equals(rowSpec[0]));
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
    public Map<Integer, String> getValues() {
        Map<Integer, String> result = new ConcurrentHashMap<>();
        int rowIndex = 0;
        for (Iterator<Map<String, String>> iter = this.csvMap.iterator(); iter.hasNext();) {
            Map<String, String> row = iter.next();
            if (this.isRowSelected(rowIndex)) {
                result.put(rowIndex, row.get(this.columnSpec));
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
            throw new RuntimeException("It is not allowed to change values of the header row (row index " + rowsIndex + ")");
        }
    }


}
