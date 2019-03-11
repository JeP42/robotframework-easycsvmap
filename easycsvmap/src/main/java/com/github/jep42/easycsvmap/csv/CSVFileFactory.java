package com.github.jep42.easycsvmap.csv;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

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
     * @param csvString The actual CSV content
     * @param columnSeparator The delimiter to use for separating entries
     * @param quoteCharacter The character to use for quoted elements
     * @return New CSVFileReader instance
     */
    public static CSVFileReader getReader(String csvString, char columnSeparator, char quoteCharacter) {
        return new OpenCSVReader(csvString, columnSeparator, quoteCharacter);
    }

    /**
     * Creates a new CSVFileWriter object with the given properties. CSV output will be written to the given target path.
     *
     * @param targetPath The path to the output file
     * @param columnSeparator The delimiter to use for separating entries
     * @param quoteCharacter The character to use for quoted elements
     * @param lineEnd The line feed terminator to use
     * @return New CSVFileWriter instance
     * @throws IOException If the named file exists but is a directory rather than a regular file, does not exist but cannot be created, or cannot be opened for any other reason
     */
    public static CSVFileWriter getWriter(String targetPath, char columnSeparator, char quoteCharacter, String lineEnd) throws IOException {
        return new OpenCSVWriter(targetPath, columnSeparator, quoteCharacter, lineEnd);
    }

    /**
     * Creates a new CSVFileWriter object with the given properties. CSV output will be written to the given target path. If the BOM is not <code>null</code> a
     * matching byte order mark will be written to the very beginning of the output file.
     *
     * @param targetPath The path to the output file
     * @param bom Byte order mark. If not <code>null</code> the corresponding bytes will be added to the very beginning of the output file.
     * @param columnSeparator The delimiter to use for separating entries
     * @param quoteCharacter The character to use for quoted elements
     * @param lineEnd The line feed terminator to use
     * @return New CSVFileWriter instance with optional byte order mark
     * @throws IOException If the named file exists but is a directory rather than a regular file, does not exist but cannot be created, or cannot be opened for any other reason
     */
    public static CSVFileWriter getWriter(String targetPath, ByteOrderMark bom, char columnSeparator, char quoteCharacter, String lineEnd) throws IOException {
        Writer fileWriter = createFileWriterWithBOM(targetPath, bom);   // Create own writer and add BOM bytes to the very beginning

        return new OpenCSVWriter(fileWriter, columnSeparator, quoteCharacter, lineEnd);
    }

    /**
     * Creates and returns a new {@link Writer} referencing the given target path. If a byte order mark is provided the corresponding byte representation will already be pushed
     * to the returned Writer object.
     *
     * @param targetPath The path to the output file
     * @param bom Byte order mark. If not <code>null</code> the corresponding bytes will already be written to the output file.
     * @return New Writer instance with optional byte order mark
     * @throws IOException If the named file exists but is a directory rather than a regular file, does not exist but cannot be created, or cannot be opened for any other reason
     */
    private static Writer createFileWriterWithBOM(String targetPath, ByteOrderMark bom) throws IOException {
        FileWriter fileWriter = new FileWriter(targetPath);

        if (bom != null) {
            // If we have a BOM re-add it as first bytes in the output file
            fileWriter.write(new String(bom.getBytes()));
            fileWriter.flush();
        }

        return fileWriter;
    }

}
