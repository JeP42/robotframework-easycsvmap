package de.jep.easycsvmap;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class LineIndexSelector implements CSVSelector {

    private static final Object ALL_INDEXES_WILCARD_CHARACTER = "*";

    private String selector;

    private List<Map<String, String>> csvMap;

    private CSVContext csvContext;

    private List<Integer> lineIndexList;

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
            this.parse(this.selector);
        } catch (InvalidSelectorException e) {
            // ignore this as
            return false;
        }

        return true;
    }

    private void parse(String selector) throws InvalidSelectorException {
        this.validateFormat();

        int guessedSeparatorIdx = this.selector.lastIndexOf('.');
        String[] selectorFragments = new String[] { this.selector.substring(0, guessedSeparatorIdx), this.selector.substring(guessedSeparatorIdx + 1) };

        this.validateLineSpec(selectorFragments[0]);
        this.validateColumnSpec(selectorFragments[1]);

        this.columnSpec = selectorFragments[1];

        String[] lineSpec = CSVMapUtil.removeBracesFromString(selectorFragments[0]).split(",");
        this.allLineIndexes = (lineSpec.length == 1 && ALL_INDEXES_WILCARD_CHARACTER.equals(lineSpec[0]));
        this.lineIndexList = this.getLineIndexListFromStringArray(lineSpec);
    }

    private void validateFormat() throws InvalidSelectorException {
        int guessedSeparatorIdx = this.selector.lastIndexOf('.');
        if (guessedSeparatorIdx == -1) {
            throw new InvalidSelectorException("The given format " + this.selector + " does not match the expected format.");
        }
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

    /*
     * format of a valid line spec is {lineIndex|lineIndexList} (TODO |lineIndexRange)
     */
    private void validateLineSpec(String lineSpec) throws InvalidSelectorException {
        if (!lineSpec.startsWith("{") || !lineSpec.endsWith("}")) {
            throw new InvalidSelectorException("Invalid format of the given line specification as part of " + this.selector);
        }

        String[] lineIndexList = CSVMapUtil.removeBracesFromString(lineSpec).split(",");
        validateLineIndexList(lineIndexList);
    }

    /*
     * If list contains just one element this may be the WILDCARD character, otherwise all list items
     * must be valid integers
     */
    private void validateLineIndexList(String[] lineIndexList) throws InvalidSelectorException {
        if (lineIndexList.length == 1 && ALL_INDEXES_WILCARD_CHARACTER.equals(lineIndexList[0])) {
            return;
        }

        for (String listitem : lineIndexList) {
            if (!CSVMapUtil.isValidInteger(listitem)) {
                if (ALL_INDEXES_WILCARD_CHARACTER.equals(listitem)) {
                    throw new InvalidSelectorException("Do not mix-up line indexes and the wilcard character (" + ALL_INDEXES_WILCARD_CHARACTER + ") " + this.selector);
                }
                throw new InvalidSelectorException("The given line index list contains invalid line indexes " + this.selector);
            }
        }
    }



    private void validateColumnSpec(String columnSpec) throws InvalidSelectorException {
        if (columnSpec.length() < 1) {
            throw new InvalidSelectorException("The columns specification is missing in the given selector " + this.selector);
        }
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
        return this.allLineIndexes || this.lineIndexList.contains(lineIndex);
    }

    @Override
    public void setValues(String value) {
        int lineIndex = 0;
        for (Iterator<Map<String, String>> iter = this.csvMap.iterator(); iter.hasNext();) {
            Map<String, String> row = iter.next();
            if (this.isLineSelected(lineIndex)) {
                this.validateWriteOperation(lineIndex);
                row.put(this.columnSpec, value);
            }
            lineIndex++;
        }
    }

    private void validateWriteOperation(int lineIndex) {
        if (lineIndex == this.csvContext.getHeaderLineIndex()) {
            throw new RuntimeException("It is not allowed to change values of the header line (line index " + lineIndex + ")");
        }
    }


}
