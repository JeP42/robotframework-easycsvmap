package de.jep.easycsv.easycsvmap.selector;

import de.jep.easycsv.easycsvmap.core.InvalidSelectorFormatException;
import de.jep.easycsv.easycsvmap.util.CSVMapUtil;

public class RowIndexFormatValidator {

    private String rowIndexSpec;

    public RowIndexFormatValidator(String rowIndexSpec) {
        this.rowIndexSpec = rowIndexSpec;
    }

    /**
     * Format of a valid row specification is {rowIndex|rowIndexList} (TODO |rowIndexRange)
     * .
     * If list contains just one element this may be the WILDCARD character, otherwise all list items
     * must be valid integers.
     * Examples for valid row index specifications: {*}, {1}, {0,1,3,4,5}
     * Example for invalid row index specifications: {0,1,*}
     */
    public void validate() throws InvalidSelectorFormatException {
        if (!this.rowIndexSpec.startsWith("{") || !this.rowIndexSpec.endsWith("}")) {
            throw new InvalidSelectorFormatException("Invalid format of the given row specification " + this.rowIndexSpec);
        }

        String[] rowIndexList = CSVMapUtil.removeBracesFromString(this.rowIndexSpec).split(",");
        validateRowIndexList(rowIndexList);
    }

    private void validateRowIndexList(String[] rowIndexList) throws InvalidSelectorFormatException {
        if (rowIndexList.length == 1 && RowIndexSelector.ALL_INDEXES_WILCARD_CHARACTER.equals(rowIndexList[0])) {
            return;
        }

        for (String listitem : rowIndexList) {
            if (!CSVMapUtil.isValidInteger(listitem)) {
                if (RowIndexSelector.ALL_INDEXES_WILCARD_CHARACTER.equals(listitem)) {
                    throw new InvalidSelectorFormatException(
                            "Do not mix-up row indexes and the wilcard character (" + RowIndexSelector.ALL_INDEXES_WILCARD_CHARACTER + ") " + rowIndexList);
                }
                throw new InvalidSelectorFormatException("The given row index list contains invalid row indexes " + rowIndexList);
            }
        }
    }

}
