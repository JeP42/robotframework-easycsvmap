package de.jep.easycsv.easycsvmap.csv;

import java.io.IOException;

public interface CSVFileReader {

    String[] readNextLine() throws IOException;

    void close() throws IOException;

}
