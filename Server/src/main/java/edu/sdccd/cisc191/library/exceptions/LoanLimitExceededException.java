package edu.sdccd.cisc191.library.exceptions;

public class LoanLimitExceededException extends Exception {
    public LoanLimitExceededException(String message) {
        super(message);
    }
}
