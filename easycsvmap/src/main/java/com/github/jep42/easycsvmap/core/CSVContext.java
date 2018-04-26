package com.github.jep42.easycsvmap.core;

public class CSVContext {

    public static final int NO_HEADER_INDEX = -1;
    public static final char STANDARD_COLUMN_SEPARATOR = ';';
    public static final char STANDARD_QUOTE_CHARACTER = '\"';
    public static final String STANDARD_LINE_END = "\n";

    private int headerRowIndex;
    private char columnSeparator;
    private char quoteCharacter;
    private String lineEnd;

    public CSVContext(int headerRowIndex) {
        this.headerRowIndex = headerRowIndex > NO_HEADER_INDEX ? headerRowIndex : NO_HEADER_INDEX;
        this.columnSeparator = STANDARD_COLUMN_SEPARATOR;
        this.quoteCharacter = STANDARD_QUOTE_CHARACTER;
        this.lineEnd = STANDARD_LINE_END;
    }

    public boolean hasHeaderRow() {
        return this.headerRowIndex > NO_HEADER_INDEX;
    }

    public int getHeaderRowIndex() {
        return headerRowIndex;
    }

    public void setHeaderRow(int headerRowIndex) {
        this.headerRowIndex = headerRowIndex > NO_HEADER_INDEX ? headerRowIndex : NO_HEADER_INDEX;
    }

    public char getColumnSeparator() {
        return columnSeparator;
    }

    public void setColumnSeparator(char columnSeparator) {
        this.columnSeparator = columnSeparator;
    }

    public char getQuoteCharacter() {
        return quoteCharacter;
    }

    public void setQuoteCharacter(char quoteCharacter) {
        this.quoteCharacter = quoteCharacter;
    }

    public String getLineEnd() {
        return lineEnd;
    }

    public void setLineEnd(String lineEnd) {
        this.lineEnd = lineEnd;
    }



}
