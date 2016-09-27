package de.jep.easycsvmap;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class LineIndexSelector implements CSVSelector {

    private String selector;
    private int lineIndex;
    private String columnSpec;
    private List<Map<String, String>> csvMap;
    private CSVContext csvContext;


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
        String[] selectorFragments = this.selector.split("\\.");

        this.validateFormat(selectorFragments);
        this.validateLineSpec(selectorFragments[0]);
        this.validateColumnSpec(selectorFragments[1]);

        this.lineIndex = Integer.valueOf(selectorFragments[0]);
        this.columnSpec = selectorFragments[1];
    }

    private void validateColumnSpec(String columnSpec) throws InvalidSelectorException {
        if (columnSpec.length() < 1) {
            throw new InvalidSelectorException("The columns specification is missing in the given selector " + this.selector);
        }
    }

    private void validateLineSpec(String lineSpec) throws InvalidSelectorException {
        if (!isValidInteger(lineSpec)) {
            throw new InvalidSelectorException("The line specification of the given selector " + this.selector + " is not a valid integer.");
        }
    }

    private void validateFormat(String[] selectorFragments) throws InvalidSelectorException {
        if (selectorFragments.length != 2) {
            throw new InvalidSelectorException("The given format " + this.selector + " does not match the expected format.");
        }
    }

    private boolean isValidInteger(String intString) {
        try {
            Integer.parseInt(intString);
        } catch (NumberFormatException e) {
            // we can ignore this...its ok...really...
            return false;
        }
        return true;
    }


    @Override
    public Map<Integer, String> getValues() {

        Map<Integer, String> result = new ConcurrentHashMap<>();
        result.put(this.lineIndex, this.csvMap.get(this.lineIndex).get(this.columnSpec));

        return result;
    }


    @Override
    public void setValues(String value) {
        if (this.lineIndex == this.csvContext.getHeaderLineIndex()) {
            throw new RuntimeException("It is not allowed to change values of the header line (line index " + this.lineIndex + ")");
        }

        this.csvMap.get(this.lineIndex).put(this.columnSpec, value);
    }


}
