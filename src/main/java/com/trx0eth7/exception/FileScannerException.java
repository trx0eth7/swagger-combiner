package com.trx0eth7.exception;

public class FileScannerException extends RuntimeException {

    public FileScannerException() {
    }

    public FileScannerException(String message) {
        super(message);
    }

    public FileScannerException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileScannerException(Throwable cause) {
        super(cause);
    }
}
