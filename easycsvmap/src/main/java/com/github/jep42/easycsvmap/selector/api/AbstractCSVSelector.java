package com.github.jep42.easycsvmap.selector.api;

import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import com.github.jep42.easycsvmap.core.CSVContext;
import com.github.jep42.easycsvmap.core.InvalidSelectorValueException;


public abstract class AbstractCSVSelector implements CSVSelector {

    protected String selector;

    protected CSVContext csvContext;

    protected List<Map<String, String>> csvMap;


    public AbstractCSVSelector(String selector, List<Map<String, String>> csvMap, CSVContext csvContext) {
        this.selector = selector;
        this.csvMap = csvMap;
        this.csvContext = csvContext;
    }

    @Override
    public void setSelector(String csvSelectorString) {
        this.selector = csvSelectorString;
    }

    @Override
    public void setCSVContext(CSVContext csvContext) {
        this.csvContext = csvContext;
    }

    @Override
    public void setCSVMap(List<Map<String, String>> csvMap) {
        this.csvMap = csvMap;
    }

    protected String getValueFromRow(@Nonnull Map<String, String> row, @Nonnull String columnSpec) {
        String value = row.get(columnSpec);
        if (value == null) {
            throw new InvalidSelectorValueException("The column spec " + columnSpec + " of the given selector does not match an exsiting column");
        }
        return value;
    }

}
