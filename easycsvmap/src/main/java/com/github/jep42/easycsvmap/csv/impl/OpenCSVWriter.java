package com.github.jep42.easycsvmap.csv.impl;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import com.github.jep42.easycsvmap.csv.api.CSVFileWriter;

import au.com.bytecode.opencsv.CSVWriter;

public class OpenCSVWriter implements CSVFileWriter {

    private CSVWriter csvWriter;

    /**
     * Creates a new CSVWriter instance which will use a new {@link FileWriter} to store the CSV content to the given target file.
     *
     * @param targetPath The path to the output file
     * @param columnSeparator The delimiter to use for separating entries
     * @param quoteCharacter The character to use for quoted elements
     * @param lineEnd The line feed terminator to use
     * @throws IOException If the named file exists but is a directory rather than a regular file, does not exist but cannot be created, or cannot be opened for any other reason
     */
    public OpenCSVWriter(String targetPath, char columnSeparator, char quoteCharacter, String lineEnd) throws IOException {
        this.csvWriter = new CSVWriter(new FileWriter(targetPath), columnSeparator, quoteCharacter, lineEnd);
    }

    /**
     * Creates a new CSVWriter instance. The object returned will use the provided <code>writer</code> as output target for CSV content.
     *
     * @param writer The writer to an underlying CSV source
     * @param columnSeparator The delimiter to use for separating entries
     * @param quoteCharacter The character to use for quoted elements
     * @param lineEnd The line feed terminator to use
     */
    public OpenCSVWriter(Writer writer, char columnSeparator, char quoteCharacter, String lineEnd) {
        this.csvWriter = new CSVWriter(writer, columnSeparator, quoteCharacter, lineEnd);
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
