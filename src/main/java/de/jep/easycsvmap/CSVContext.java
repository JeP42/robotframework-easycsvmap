package de.jep.easycsvmap;

public class CSVContext {

    public static final int NO_HEADER_INDEX = -1;
    public static final char STANDARD_COLUMN_SEPARATOR = ';';
    public static final char STANDARD_QUOTE_CHARACTER = '\"';

    private int headerLineIndex;
    private char columnSeparator;
    private char quoteCharacter;

    public CSVContext(int headerLineIndex) {
        this.headerLineIndex = headerLineIndex > NO_HEADER_INDEX ? headerLineIndex : NO_HEADER_INDEX;
        this.columnSeparator = STANDARD_COLUMN_SEPARATOR;
        this.quoteCharacter = STANDARD_QUOTE_CHARACTER;
    }

    public boolean hasHeaderLine() {
        return this.headerLineIndex > NO_HEADER_INDEX;
    }

    public int getHeaderLineIndex() {
        return headerLineIndex;
    }

    public void setHeaderLine(int headerLineIndex) {
        this.headerLineIndex = headerLineIndex > NO_HEADER_INDEX ? headerLineIndex : NO_HEADER_INDEX;
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

}
