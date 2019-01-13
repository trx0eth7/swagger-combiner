package com.trx0eth7.exception;

public class ParseFileException extends RuntimeException {

    public ParseFileException() {
    }

    public ParseFileException(String message) {
        super(message);
    }

    public ParseFileException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParseFileException(Throwable cause) {
        super(cause);
    }
}
