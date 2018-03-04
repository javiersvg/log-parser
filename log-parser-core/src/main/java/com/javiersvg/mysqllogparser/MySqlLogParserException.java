package com.javiersvg.mysqllogparser;


public class MySqlLogParserException extends RuntimeException {
    public MySqlLogParserException(String message, Throwable cause) {
        super(message, cause);
    }

    public MySqlLogParserException(String message) {
        super(message);
    }
}
