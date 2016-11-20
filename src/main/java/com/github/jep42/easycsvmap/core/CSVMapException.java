package com.github.jep42.easycsvmap.core;

import java.io.IOException;

public class CSVMapException extends RuntimeException {

    private static final long serialVersionUID = 2062314631723122868L;

    public CSVMapException(String message, IOException rootcause) {
        super(message, rootcause);
    }

    public CSVMapException(String message) {
        super(message);
    }

}
