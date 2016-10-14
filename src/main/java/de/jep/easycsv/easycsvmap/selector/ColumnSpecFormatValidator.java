package de.jep.easycsv.easycsvmap.selector;

import de.jep.easycsv.easycsvmap.core.InvalidSelectorFormatException;

public class ColumnSpecFormatValidator {

    private String columnSpec;

    public ColumnSpecFormatValidator(String columnSpec) {
        this.columnSpec = columnSpec;
    }

    /**
     * A valid column specification is a string with column name. The column name is either the column name from the given header row or the
     * column index for the case that no header row was specified.
     *
     * @throws InvalidSelectorFormatException
     */
    public void validate() throws InvalidSelectorFormatException {
        if (columnSpec.length() < 1) {
            throw new InvalidSelectorFormatException("The columns specification is missing in the given selector");
        }
    }


}
