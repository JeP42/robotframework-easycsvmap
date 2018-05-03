package com.github.jep42.easycsvmap.selector;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jep42.easycsvmap.core.CSVContext;
import com.github.jep42.easycsvmap.core.CSVMapException;
import com.github.jep42.easycsvmap.core.InvalidSelectorFormatException;
import com.github.jep42.easycsvmap.selector.api.CSVSelector;
import com.github.jep42.easycsvmap.selector.impl.RegExpRowSelector;
import com.github.jep42.easycsvmap.selector.impl.RowIndexSelector;

public final class CSVSelectorFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(CSVSelectorFactory.class);


    private CSVSelectorFactory() {}

    public static CSVSelector getCSVSelector(String csvSelector, List<Map<String, String>> csvMap, CSVContext csvContext) {

        CSVSelector selector = new RowIndexSelector(csvSelector, csvMap, csvContext);

        if (CSVSelectorFactory.parseSelectorAndCheckIfValid(selector)) {
            return selector;
        }

        selector = new RegExpRowSelector(csvSelector, csvMap, csvContext);
        if (CSVSelectorFactory.parseSelectorAndCheckIfValid(selector)) {
            return selector;
        }

        throw new CSVMapException("The given CSV selector " + csvSelector + " is not supported.");
    }

    private static boolean parseSelectorAndCheckIfValid(CSVSelector selector) {
        try {
            selector.parse();
        } catch (InvalidSelectorFormatException e) {
            CSVSelectorFactory.LOGGER.debug("The given selector is not a valid " + selector.getClass().getName(), e);
            return false;
        }
        return true;
    }




}
