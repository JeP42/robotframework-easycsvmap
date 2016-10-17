package de.jep.easycsv.easycsvmap.selector;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;

import de.jep.easycsv.easycsvmap.core.CSVContext;
import de.jep.easycsv.easycsvmap.core.CSVMapException;
import de.jep.easycsv.easycsvmap.core.InvalidSelectorFormatException;
import de.jep.easycsv.easycsvmap.util.CSVMapUtil;


public class RegExpRowSelector extends AbstractCSVSelector {

    private static final char FORMAT_SEPARATOR_CHARACTER = '.';

    private String columnSpec;

    private String columnIdentifier;

    private Pattern columnRegExp;


    public RegExpRowSelector(String selector, List<Map<String, String>> csvMap, CSVContext csvContext) {
        super(selector, csvMap, csvContext);
    }


    @Override
    public void parse() throws InvalidSelectorFormatException {
        this.validateFormat();

        String[] selectorFragments = this.getSelectorFragments();

        new RegExpRowValidator(selectorFragments[0]).validate();
        new ColumnSpecFormatValidator(selectorFragments[1]).validate();

        this.columnSpec = selectorFragments[1];
        this.columnIdentifier = this.getColumnIdentifier(selectorFragments[0]);
        this.columnRegExp = this.getColumnRegExpPattern(selectorFragments[0]);
    }

    private String[] getSelectorFragments() {
        int guessedSeparatorIdx = this.selector.lastIndexOf(FORMAT_SEPARATOR_CHARACTER);
        return new String[] { this.selector.substring(0, guessedSeparatorIdx), this.selector.substring(guessedSeparatorIdx + 1) };
    }

    private void validateFormat() throws InvalidSelectorFormatException {
        int guessedSeparatorIdx = this.selector.lastIndexOf('.');
        if (guessedSeparatorIdx == -1) {
            throw new InvalidSelectorFormatException("The given format " + this.selector + " does not match the expected format.");
        }
    }

    private Pattern getColumnRegExpPattern(String rowSpec) {
        String[] rowSpecFragments = CSVMapUtil.removeBracesFromString(rowSpec).split("=", 2);
        return Pattern.compile(rowSpecFragments[1]);
    }

    private String getColumnIdentifier(String rowSpec) {
        String[] rowSpecFragments = CSVMapUtil.removeBracesFromString(rowSpec).split("=", 2);
        return rowSpecFragments[0];
    }


    @Override
    @Nonnull
    public Map<Integer, String> getValues() {
        Map<Integer, String> result = new TreeMap<>();

        int rowIndex = 0;
        for (Iterator<Map<String, String>> iter = this.csvMap.iterator(); iter.hasNext();) {
            Map<String, String> row = iter.next();
            String colSelectorValue = row.get(this.columnIdentifier);
            if (this.columnRegExp.matcher(colSelectorValue).matches()) {
                result.put(rowIndex, row.get(this.columnSpec));
            }
            rowIndex++;
        }

        return result;
    }

    @Override
    public int setValues(String value) {
        int affectedRows = 0;
        int rowIndex = 0;
        for (Iterator<Map<String, String>> iter = this.csvMap.iterator(); iter.hasNext();) {
            Map<String, String> row = iter.next();
            String colSelectorValue = row.get(this.columnIdentifier);
            if (this.columnRegExp.matcher(colSelectorValue).matches()) {
                this.validateWriteOperation(rowIndex);
                row.put(this.columnSpec, value);
                affectedRows++;
            }
            rowIndex++;
        }
        return affectedRows;
    }

    private void validateWriteOperation(int rowIndex) {
        if (rowIndex == this.csvContext.getHeaderRowIndex()) {
            throw new CSVMapException("It is not allowed to change values of the header row (row index " + rowIndex + ")");
        }
    }


}
