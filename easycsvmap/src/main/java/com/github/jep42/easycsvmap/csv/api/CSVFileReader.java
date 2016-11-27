package com.github.jep42.easycsvmap.csv.api;

import java.io.IOException;

public interface CSVFileReader {

    String[] readNextLine() throws IOException;

    void close() throws IOException;

}
