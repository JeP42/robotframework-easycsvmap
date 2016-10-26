package net.easycsv.easycsvmap.csv;

import java.io.FileWriter;
import java.io.IOException;

import au.com.bytecode.opencsv.CSVWriter;

public class OpenCSVWriter implements CSVFileWriter {

    private CSVWriter csvWriter;

    public OpenCSVWriter(String targetPath, char columnSeparator) throws IOException {
        this.csvWriter = new CSVWriter(new FileWriter(targetPath), columnSeparator);
    }

    @Override
    public void writeNextLine(String[] values) {
        this.csvWriter.writeNext(values);
    }

    @Override
    public void close() throws IOException {
        this.csvWriter.close();
    }

}
