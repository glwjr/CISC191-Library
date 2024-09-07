package edu.sdccd.cisc191.library.exceptions;

public class LoanFileException extends RuntimeException {
    public LoanFileException(String message, Throwable cause) {
        super(message, cause);
    }
}
