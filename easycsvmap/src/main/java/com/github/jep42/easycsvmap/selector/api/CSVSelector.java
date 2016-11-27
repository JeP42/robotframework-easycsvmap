package com.github.jep42.easycsvmap.selector.api;

import java.util.List;
import java.util.Map;

import com.github.jep42.easycsvmap.core.CSVContext;
import com.github.jep42.easycsvmap.core.InvalidSelectorFormatException;
import com.github.jep42.easycsvmap.core.InvalidSelectorValueException;

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

    public void parse() throws InvalidSelectorFormatException;

    /**
     * Retrieves cell values from the given map according to the given selector and returns as map in which the key is the row index.
     * The elements of the map are order by the key.
     * @throws InvalidSelectorValueException if the given selector does not match one of the existing columns
     * @return
     */
    Map<Integer, String> getValues();

    /**
     * Sets values of cells identified by the given selector to the passed value.
     * @param value
     * @return the number of updated rows
     */
    int setValues(String value);

}
