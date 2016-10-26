package net.easycsv.easycsvmap.csv;

import java.io.IOException;
import java.io.StringReader;

import au.com.bytecode.opencsv.CSVReader;

public class OpenCSVReader implements CSVFileReader {


    CSVReader csvReader;

    public OpenCSVReader(String csvString, char columnSeparator, char quoteCharacter) {
        super();
        this.csvReader = new CSVReader(new StringReader(csvString), columnSeparator, quoteCharacter);
    }

    @Override
    public String[] readNextLine() throws IOException {
        return this.csvReader.readNext();
    }

    @Override
    public void close() throws IOException {
        this.csvReader.close();
    }

}
