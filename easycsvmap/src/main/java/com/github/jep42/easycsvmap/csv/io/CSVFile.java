package com.github.jep42.easycsvmap.csv.io;

import org.apache.commons.io.ByteOrderMark;

/**
 * Wrapper class for CSV files. Holds information about the actual CSV file content and if the underlying file
 * contained any kind of byte order mark which should be re-added when the content is saved again.
 */
public class CSVFile {

    /** The ByteOrderMark from the very beginning of the CSV file. Might be null. */
    private ByteOrderMark bom;

    /** The actual CSV content */
    private String content;

    public CSVFile(String content) {
        this.content = content;
        this.bom = null;
    }

    public CSVFile(String content, ByteOrderMark bom) {
        this.content = content;
        this.bom = bom;
    }

    public ByteOrderMark getBom() {
        return bom;
    }

    public String getContent() {
        return content;
    }

}