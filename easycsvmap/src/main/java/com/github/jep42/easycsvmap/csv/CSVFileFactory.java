package com.github.jep42.easycsvmap.csv;

import java.io.IOException;

import com.github.jep42.easycsvmap.csv.api.CSVFileReader;
import com.github.jep42.easycsvmap.csv.api.CSVFileWriter;
import com.github.jep42.easycsvmap.csv.impl.OpenCSVReader;
import com.github.jep42.easycsvmap.csv.impl.OpenCSVWriter;

public final class CSVFileFactory {

    private CSVFileFactory() {
        super();
    }

    public static CSVFileReader getReader(String csvString, char columnSeparator, char quoteCharacter) {
        return new OpenCSVReader(csvString, columnSeparator, quoteCharacter);
    }

    public static CSVFileWriter getWriter(String targetPath, char columnSeparator) throws IOException {
        return new OpenCSVWriter(targetPath, columnSeparator);
    }

}
