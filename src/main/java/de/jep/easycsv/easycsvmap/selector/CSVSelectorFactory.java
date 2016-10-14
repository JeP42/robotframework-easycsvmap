package de.jep.easycsv.easycsvmap.selector;

import java.util.List;
import java.util.Map;

import de.jep.easycsv.easycsvmap.core.CSVContext;

public final class CSVSelectorFactory {

    private CSVSelectorFactory() {}

    public static CSVSelector getCSVSelector(String csvSelector, List<Map<String, String>> csvMap, CSVContext csvContext) {

        CSVSelector selector = new LineIndexSelector(csvSelector, csvMap, csvContext);
        if (selector.isValid()) {
            return selector;
        }

        selector = new RegExpLineSelector(csvSelector, csvMap, csvContext);
        if (selector.isValid()) {
            return selector;
        }


        throw new RuntimeException("The given CSV selector " + csvSelector + " is not supported.");
    }




}
