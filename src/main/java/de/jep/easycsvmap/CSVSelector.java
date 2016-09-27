package de.jep.easycsvmap;

import java.util.List;
import java.util.Map;

public interface CSVSelector {


    void setSelector(String csvSelectorString);

    void setCSVContext(CSVContext csvContext);

    void setCSVMap(List<Map<String, String>> csvMap);


    /**
     * Checks whether the CSVSelector is valid
     *
     * @return
     */
    boolean isValid();

    /**
     * Retrieves cell values from the given map according to the given selector and returns as map in which the key is the line index.
     * @return
     */
    Map<Integer, String> getValues();

    /**
     * Sets values of cells identified by the given selector to the passed value.
     * @param value
     */
    void setValues(String value);

}
