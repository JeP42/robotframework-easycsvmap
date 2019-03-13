package com.github.jep42.easycsvmap.csv;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.ByteOrderMark;

import com.github.jep42.easycsvmap.csv.api.CSVFileReader;
import com.github.jep42.easycsvmap.csv.api.CSVFileWriter;
import com.github.jep42.easycsvmap.csv.impl.OpenCSVReader;
import com.github.jep42.easycsvmap.csv.impl.OpenCSVWriter;

public final class CSVFileFactory {

    private CSVFileFactory() {
        super();
    }

    /**
     * Creates a new CSVFileReader object with the given properties and CSV content.
     *
     * @param csvFile         The actual CSV file
     * @param columnSeparator The delimiter to use for separating entries
     * @param quoteCharacter The character to use for quoted elements
     * @return New CSVFileReader instance
     * @throws IOException
     */
    public static CSVFileReader getReader(File csvFile, char columnSeparator, char quoteCharacter) throws IOException {
        return new OpenCSVReader(csvFile, columnSeparator, quoteCharacter);
    }

    /**
     * Creates a new CSVFileWriter object with the given properties. CSV output will be written to the given target
     * path. If the BOM is not <code>null</code> a
     * matching byte order mark will be written to the very beginning of the output file.
     *
     * @param targetPath      The path to the output file
     * @param bom             Byte order mark. If not <code>null</code> the corresponding bytes will be added to the
     *                            very beginning of the output file.
     * @param columnSeparator The delimiter to use for separating entries
     * @param quoteCharacter  The character to use for quoted elements
     * @param lineEnd         The line feed terminator to use
     * @return New CSVFileWriter instance with optional byte order mark
     * @throws IOException If the named file exists but is a directory rather than a regular file, does not exist but
     *                         cannot be created, or cannot be opened for any other reason
     */
    public static CSVFileWriter getWriter(String targetPath, ByteOrderMark bom, char columnSeparator,
            char quoteCharacter, String lineEnd) throws IOException {
        return new OpenCSVWriter(targetPath, bom, columnSeparator, quoteCharacter, lineEnd);
    }

}
