package de.jep.easycsv.easycsvmap.selector;

import de.jep.easycsv.easycsvmap.core.InvalidSelectorFormatException;
import de.jep.easycsv.easycsvmap.util.CSVMapUtil;

public class RegExpLineValidator {

    private String regExpLineSpec;

    public RegExpLineValidator(String regExpLineSpec) {
        this.regExpLineSpec = regExpLineSpec;
    }

    /**
     * format of a valid RegExpLine spec is [key=value] where the value is a regular expression
     *
     * @throws InvalidSelectorFormatException
     */
    public void validate() throws InvalidSelectorFormatException {
        if (!this.regExpLineSpec.startsWith("[") || !this.regExpLineSpec.endsWith("]")) {
            throw new InvalidSelectorFormatException("Invalid format of the given line specification " + this.regExpLineSpec);
        }

        String[] lineSpecFragments = CSVMapUtil.removeBracesFromString(this.regExpLineSpec).split("=", 2);
        if (lineSpecFragments.length != 2) {
            throw new InvalidSelectorFormatException("Invalid format of the given line specification " + this.regExpLineSpec);
        }

        if (lineSpecFragments[0].length() < 1) {
            throw new InvalidSelectorFormatException("The column part of the line specification is invalid: " + this.regExpLineSpec);
        }

        if (lineSpecFragments[1].length() < 1) {
            throw new InvalidSelectorFormatException("The RegExp part of the line specification is invalid: " + this.regExpLineSpec);
        }
    }

}
