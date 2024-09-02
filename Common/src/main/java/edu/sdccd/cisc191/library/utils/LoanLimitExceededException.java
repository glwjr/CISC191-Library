package edu.sdccd.cisc191.library.utils;

public class LoanLimitExceededException extends Exception {
    public LoanLimitExceededException(String message) {
        super(message);
    }
}
