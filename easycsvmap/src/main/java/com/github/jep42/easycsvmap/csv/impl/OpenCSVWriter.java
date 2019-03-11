package com.github.jep42.easycsvmap.csv.impl;

import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.io.ByteOrderMark;

import com.github.jep42.easycsvmap.csv.api.CSVFileWriter;

import au.com.bytecode.opencsv.CSVWriter;

public class OpenCSVWriter implements CSVFileWriter {

    private CSVWriter csvWriter;

    /**
     * Creates a new CSVWriter instance which will use a new {@link FileWriter} to store the CSV content to the given target file.
     *
     * @param targetPath      The path to the output file
     * @param bom             ByteOrderMark, may be null
     * @param columnSeparator The delimiter to use for separating entries
     * @param quoteCharacter The character to use for quoted elements
     * @param lineEnd The line feed terminator to use
     * @throws IOException If the named file exists but is a directory rather than a regular file, does not exist but cannot be created, or cannot be opened for any other reason
     */
    public OpenCSVWriter(String targetPath, ByteOrderMark bom, char columnSeparator, char quoteCharacter,
            String lineEnd) throws IOException {

        FileWriter fileWriter = new FileWriter(targetPath);

        if (bom != null) {
            // If we have a BOM re-add it as first bytes in the output file
            fileWriter.write(new String(bom.getBytes()));
            fileWriter.flush();
        }

        this.csvWriter = new CSVWriter(fileWriter, columnSeparator, quoteCharacter, lineEnd);
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
