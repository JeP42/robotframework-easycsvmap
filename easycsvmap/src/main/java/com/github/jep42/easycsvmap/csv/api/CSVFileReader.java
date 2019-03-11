package com.github.jep42.easycsvmap.csv.api;

import java.io.IOException;

import org.apache.commons.io.ByteOrderMark;

public interface CSVFileReader {

    String[] readNextLine() throws IOException;

    void close() throws IOException;

    ByteOrderMark getBom();

    void resetReader();

}
