package net.easycsv.easycsvmap.csv;

import java.io.IOException;

public final class CSVFileFactory {

    private CSVFileFactory() {
        super();
    }

    public static CSVFileReader getReader(String csvString, char columnSeparator, char quoteCharacter) {
        return new OpenCSVReader(csvString, columnSeparator, quoteCharacter);
    }

    public static CSVFileWriter getWriter(String targetPath, char columnSeparator) throws IOException {
        return new OpenCSVWriter(targetPath, columnSeparator);
    }

}
