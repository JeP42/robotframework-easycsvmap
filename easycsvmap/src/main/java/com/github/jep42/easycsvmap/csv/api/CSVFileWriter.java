package com.github.jep42.easycsvmap.csv.api;

import java.io.IOException;

public interface CSVFileWriter {

    void writeNextLine(String[] values);

    void close() throws IOException;

}
