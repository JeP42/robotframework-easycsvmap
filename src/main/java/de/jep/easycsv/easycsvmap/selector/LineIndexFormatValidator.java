package de.jep.easycsv.easycsvmap.selector;

import de.jep.easycsv.easycsvmap.core.InvalidSelectorFormatException;
import de.jep.easycsv.easycsvmap.util.CSVMapUtil;

public class LineIndexFormatValidator {

    private String lineIndexSpec;

    public LineIndexFormatValidator(String lineIndexSpec) {
        this.lineIndexSpec = lineIndexSpec;
    }

    /**
     * Format of a valid line specification is {lineIndex|lineIndexList} (TODO |lineIndexRange)
     * .
     * If list contains just one element this may be the WILDCARD character, otherwise all list items
     * must be valid integers.
     * Examples for valid line index specifications: {*}, {1}, {0,1,3,4,5}
     * Example for invalid line index specifications: {0,1,*}
     */
    public void validate() throws InvalidSelectorFormatException {
        if (!this.lineIndexSpec.startsWith("{") || !this.lineIndexSpec.endsWith("}")) {
            throw new InvalidSelectorFormatException("Invalid format of the given line specification " + this.lineIndexSpec);
        }

        String[] lineIndexList = CSVMapUtil.removeBracesFromString(this.lineIndexSpec).split(",");
        validateLineIndexList(lineIndexList);
    }

    private void validateLineIndexList(String[] lineIndexList) throws InvalidSelectorFormatException {
        if (lineIndexList.length == 1 && LineIndexSelector.ALL_INDEXES_WILCARD_CHARACTER.equals(lineIndexList[0])) {
            return;
        }

        for (String listitem : lineIndexList) {
            if (!CSVMapUtil.isValidInteger(listitem)) {
                if (LineIndexSelector.ALL_INDEXES_WILCARD_CHARACTER.equals(listitem)) {
                    throw new InvalidSelectorFormatException(
                            "Do not mix-up line indexes and the wilcard character (" + LineIndexSelector.ALL_INDEXES_WILCARD_CHARACTER + ") " + lineIndexList);
                }
                throw new InvalidSelectorFormatException("The given line index list contains invalid line indexes " + lineIndexList);
            }
        }
    }

}
