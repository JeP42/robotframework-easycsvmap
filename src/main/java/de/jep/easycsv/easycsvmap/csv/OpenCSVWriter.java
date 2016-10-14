package de.jep.easycsv.easycsvmap.csv;

import java.io.FileWriter;
import java.io.IOException;

import au.com.bytecode.opencsv.CSVWriter;

public class OpenCSVWriter implements CSVFileWriter {

    private CSVWriter openCsvWriter;

    public OpenCSVWriter(String targetPath, char columnSeparator) throws IOException {
        this.openCsvWriter = new CSVWriter(new FileWriter(targetPath), columnSeparator);
    }

    @Override
    public void writeNextLine(String[] values) {
        this.openCsvWriter.writeNext(values);
    }

    @Override
    public void close() throws IOException {
        this.openCsvWriter.close();
    }

}
