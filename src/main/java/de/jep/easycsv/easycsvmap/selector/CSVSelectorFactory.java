package de.jep.easycsv.easycsvmap.selector;

import java.util.List;
import java.util.Map;

import de.jep.easycsv.easycsvmap.core.CSVContext;
import de.jep.easycsv.easycsvmap.core.CSVMapException;

public final class CSVSelectorFactory {

    private CSVSelectorFactory() {}

    public static CSVSelector getCSVSelector(String csvSelector, List<Map<String, String>> csvMap, CSVContext csvContext) {

        CSVSelector selector = new RowIndexSelector(csvSelector, csvMap, csvContext);
        if (selector.isValid()) {
            return selector;
        }

        selector = new RegExpRowSelector(csvSelector, csvMap, csvContext);
        if (selector.isValid()) {
            return selector;
        }


        throw new CSVMapException("The given CSV selector " + csvSelector + " is not supported.");
    }




}
