package com.github.jep42.easycsvmap.csv.impl;

import java.io.FileWriter;
import java.io.IOException;

import com.github.jep42.easycsvmap.csv.api.CSVFileWriter;

import au.com.bytecode.opencsv.CSVWriter;

public class OpenCSVWriter implements CSVFileWriter {

    private CSVWriter csvWriter;

    public OpenCSVWriter(String targetPath, char columnSeparator, char quoteCharacter, String lineEnd) throws IOException {
        this.csvWriter = new CSVWriter(new FileWriter(targetPath), columnSeparator, quoteCharacter, lineEnd);
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
