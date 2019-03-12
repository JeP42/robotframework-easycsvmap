package com.github.jep42.easycsvmap.csv.impl;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import org.apache.commons.io.ByteOrderMark;

import com.github.jep42.easycsvmap.csv.api.CSVFileReader;
import com.github.jep42.easycsvmap.util.FileUtil;

import au.com.bytecode.opencsv.CSVReader;

public class OpenCSVReader implements CSVFileReader {

    private CSVReader csvReader;

    private FileUtil fileUtil;

    private char columnSeparator;

    private char quoteCharacter;

    public OpenCSVReader(File csvFile, char columnSeparator, char quoteCharacter) throws IOException {
        super();

        this.columnSeparator = columnSeparator;

        this.quoteCharacter = quoteCharacter;

        this.fileUtil = FileUtil.getFileUtilFor(csvFile.getAbsolutePath());

        this.initializeReader();

    }

    private void initializeReader() {
        this.csvReader = new CSVReader(new StringReader(fileUtil.getContent()), columnSeparator, quoteCharacter);
    }

    @Override
    public String[] readNextLine() throws IOException {
        return this.csvReader.readNext();
    }

    @Override
    public void close() throws IOException {
        this.csvReader.close();
    }

    @Override
    public ByteOrderMark getBom() {
        return this.fileUtil.getBom();
    }

    @Override
    public void resetReader() throws IOException {
        close();

        this.initializeReader();
    }

}
