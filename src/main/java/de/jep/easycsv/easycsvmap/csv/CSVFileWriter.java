package de.jep.easycsv.easycsvmap.csv;

import java.io.IOException;

public interface CSVFileWriter {

    void writeNextLine(String[] values);

    void close() throws IOException;

}
