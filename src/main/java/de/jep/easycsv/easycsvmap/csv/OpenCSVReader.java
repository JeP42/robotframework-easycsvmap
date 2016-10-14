package de.jep.easycsv.easycsvmap.csv;

import java.io.IOException;
import java.io.StringReader;

import au.com.bytecode.opencsv.CSVReader;

public class OpenCSVReader implements CSVFileReader {


    CSVReader openCsvReader;

    public OpenCSVReader(String csvString, char columnSeparator, char quoteCharacter) {
        super();
        this.openCsvReader = new CSVReader(new StringReader(csvString), columnSeparator, quoteCharacter);
    }

    @Override
    public String[] readNextLine() throws IOException {
        return this.openCsvReader.readNext();
    }

    @Override
    public void close() throws IOException {
        this.openCsvReader.close();
    }

}
