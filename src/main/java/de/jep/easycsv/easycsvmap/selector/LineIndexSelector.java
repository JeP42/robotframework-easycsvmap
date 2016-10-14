package de.jep.easycsv.easycsvmap.selector;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import de.jep.easycsv.easycsvmap.core.CSVContext;
import de.jep.easycsv.easycsvmap.core.InvalidSelectorFormatException;
import de.jep.easycsv.easycsvmap.util.CSVMapUtil;


public class LineIndexSelector implements CSVSelector {

    private static final char FORMAT_SEPARATOR_CHARACTER = '.';

    protected static final Object ALL_INDEXES_WILCARD_CHARACTER = "*";

    private String selector;

    private List<Map<String, String>> csvMap;

    private CSVContext csvContext;

    private List<Integer> selectedLineIndexList;

    private boolean allLineIndexes;

    private String columnSpec;


    public LineIndexSelector(String selector, List<Map<String, String>> csvMap, CSVContext csvContext) {
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
        new LineIndexFormatValidator(selectorFragments[0]).validate();
        new ColumnSpecFormatValidator(selectorFragments[1]).validate();

        // first part of the selector is the line specification
        String[] lineSpec = CSVMapUtil.removeBracesFromString(selectorFragments[0]).split(",");
        this.allLineIndexes = (lineSpec.length == 1 && ALL_INDEXES_WILCARD_CHARACTER.equals(lineSpec[0]));
        this.selectedLineIndexList = this.getLineIndexListFromStringArray(lineSpec);

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

    private List<Integer> getLineIndexListFromStringArray(String[] lineSpec) {
        List<Integer> indexList = new ArrayList<>();
        for (String idx : lineSpec) {
            if (!idx.equals(ALL_INDEXES_WILCARD_CHARACTER)) {
                indexList.add(Integer.parseInt(idx));
            }
        }
        return indexList;
    }


    @Override
    public Map<Integer, String> getValues() {
        Map<Integer, String> result = new ConcurrentHashMap<>();
        int lineIndex = 0;
        for (Iterator<Map<String, String>> iter = this.csvMap.iterator(); iter.hasNext();) {
            Map<String, String> row = iter.next();
            if (this.isLineSelected(lineIndex)) {
                result.put(lineIndex, row.get(this.columnSpec));
            }
            lineIndex++;
        }

        return result;
    }

    private boolean isLineSelected(int lineIndex) {
        return this.allLineIndexes || this.selectedLineIndexList.contains(lineIndex);
    }

    @Override
    public int setValues(String value) {
        int affectedLines = 0;
        int lineIndex = 0;
        for (Iterator<Map<String, String>> iter = this.csvMap.iterator(); iter.hasNext();) {
            Map<String, String> row = iter.next();
            if (this.isLineSelected(lineIndex)) {
                this.validateWriteOperation(lineIndex);
                row.put(this.columnSpec, value);
                affectedLines++;
            }
            lineIndex++;
        }
        return affectedLines;
    }



    private void validateWriteOperation(int lineIndex) {
        if (lineIndex == this.csvContext.getHeaderLineIndex()) {
            throw new RuntimeException("It is not allowed to change values of the header line (line index " + lineIndex + ")");
        }
    }


}
