package net.easycsv.easycsvmap.selector;

import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.easycsv.easycsvmap.core.CSVContext;
import net.easycsv.easycsvmap.core.InvalidSelectorFormatException;
import net.easycsv.easycsvmap.core.InvalidSelectorValueException;


public abstract class AbstractCSVSelector implements CSVSelector {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractCSVSelector.class);

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

    @Override
    public boolean isValid() {
        // try to parse the given selector string
        try {
            this.parse();
        } catch (InvalidSelectorFormatException e) {
            AbstractCSVSelector.LOGGER.debug("The given selector is not a valid " + this.getClass().getName(), e);
            return false;
        }

        return true;
    }

    protected String getValueFromRow(@Nonnull Map<String, String> row, @Nonnull String columnSpec) {
        String value = row.get(columnSpec);
        if (value == null) {
            throw new InvalidSelectorValueException("The column spec " + columnSpec + " of the given selector does not match an exsiting column");
        }
        return value;
    }

}
