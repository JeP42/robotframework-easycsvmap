package de.jep.easycsvmap;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;


public class RegExpLineSelector implements CSVSelector {

    private String selector;

    private List<Map<String, String>> csvMap;

    private CSVContext csvContext;

    private String columnSpec;

    private String columnIdentifier;

    private Pattern columnRegExp;

    public RegExpLineSelector(String selector, List<Map<String, String>> csvMap, CSVContext csvContext) {
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
        int guessedSeparator = this.selector.lastIndexOf('.');
        String[] selectorFragments = new String[] { this.selector.substring(0, guessedSeparator), this.selector.substring(guessedSeparator + 1) };

        this.validateFormat(selectorFragments);
        this.validateLineSpec(selectorFragments[0]);
        this.validateColumnSpec(selectorFragments[1]);

        // this.lineIndex = Integer.valueOf(selectorFragments[0]);
        this.columnSpec = selectorFragments[1];
        this.columnIdentifier = this.getColumnIdentifier(selectorFragments[0]);
        this.columnRegExp = this.getColumnRegExpPattern(selectorFragments[0]);
    }

    private Pattern getColumnRegExpPattern(String lineSpec) {
        String[] lineSpecFragments = lineSpec.substring(1, lineSpec.length() - 1).split("=", 2);
        return Pattern.compile(lineSpecFragments[1]);
    }

    private String getColumnIdentifier(String lineSpec) {
        String[] lineSpecFragments = lineSpec.substring(1, lineSpec.length() - 1).split("=", 2);
        return lineSpecFragments[0];
    }

    /*
     * format of a valid line spec is key=value where the value is a regular expression
     */
    private void validateLineSpec(String lineSpec) throws InvalidSelectorException {
        if (!lineSpec.startsWith("[") || !lineSpec.endsWith("]")) {
            throw new InvalidSelectorException("Invalid format of the given line specification as part of " + this.selector);
        }

        String[] lineSpecFragments = this.selector.substring(1, this.selector.length() - 1).split("=", 2);
        if (lineSpecFragments.length != 2) {
            throw new InvalidSelectorException("Invalid format of the given line specification as part of " + this.selector);
        }

        if (lineSpecFragments[0].length() < 1) {
            throw new InvalidSelectorException("The columns spec as part of the line specification is invalid: " + this.selector);
        }

        if (lineSpecFragments[1].length() < 1) {
            throw new InvalidSelectorException("The columns reg exp as part of the line specification is invalid: " + this.selector);
        }
    }

    private void validateColumnSpec(String columnSpec) throws InvalidSelectorException {
        if (columnSpec.length() < 1) {
            throw new InvalidSelectorException("The columns specification is missing in the given selector " + this.selector);
        }
    }

    private void validateFormat(String[] selectorFragments) throws InvalidSelectorException {
        if (selectorFragments.length != 2) {
            throw new InvalidSelectorException("The given format " + this.selector + " does not match the expected format.");
        }
    }

    @Override
    public Map<Integer, String> getValues() {
        Map<Integer, String> result = new ConcurrentHashMap<>();

        int lineIndex = 0;
        for (Iterator<Map<String, String>> iter = this.csvMap.iterator(); iter.hasNext();) {
            Map<String, String> row = iter.next();
            String colSelectorValue = row.get(this.columnIdentifier);
            if (this.columnRegExp.matcher(colSelectorValue).matches()) {
                result.put(lineIndex, row.get(this.columnSpec));
            }
            lineIndex++;
        }

        return result;
    }

    @Override
    public void setValues(String value) {
        int lineIndex = 0;
        for (Iterator<Map<String, String>> iter = this.csvMap.iterator(); iter.hasNext();) {
            Map<String, String> row = iter.next();
            String colSelectorValue = row.get(this.columnIdentifier);
            if (this.columnRegExp.matcher(colSelectorValue).matches()) {
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
