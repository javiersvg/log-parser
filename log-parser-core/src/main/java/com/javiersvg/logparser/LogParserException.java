package com.javiersvg.logparser;


public class LogParserException extends RuntimeException {
    public LogParserException(String message, Throwable cause) {
        super(message, cause);
    }

    public LogParserException(String message) {
        super(message);
    }
}
