package com.github.jep42.easycsvmap.selector.impl;

import com.github.jep42.easycsvmap.core.InvalidSelectorFormatException;
import com.github.jep42.easycsvmap.util.CSVMapUtil;

public class RegExpRowValidator {

    private String regExpRowSpec;

    public RegExpRowValidator(String regExpRowSpec) {
        this.regExpRowSpec = regExpRowSpec;
    }

    /**
     * format of a valid RegExpRow spec is [key=value] where the value is a regular expression
     *
     * @throws InvalidSelectorFormatException
     */
    public void validate() throws InvalidSelectorFormatException {
        if (!this.regExpRowSpec.startsWith("[") || !this.regExpRowSpec.endsWith("]")) {
            throw new InvalidSelectorFormatException("Invalid format of the given row specification " + this.regExpRowSpec);
        }

        String[] rowSpecFragments = CSVMapUtil.removeBracesFromString(this.regExpRowSpec).split("=", 2);
        if (rowSpecFragments.length != 2) {
            throw new InvalidSelectorFormatException("Invalid format of the given row specification " + this.regExpRowSpec);
        }

        if (rowSpecFragments[0].length() < 1) {
            throw new InvalidSelectorFormatException("The column part of the row specification is invalid: " + this.regExpRowSpec);
        }

        if (rowSpecFragments[1].length() < 1) {
            throw new InvalidSelectorFormatException("The RegExp part of the row specification is invalid: " + this.regExpRowSpec);
        }
    }

}
