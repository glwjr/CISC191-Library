package edu.sdccd.cisc191.library.exceptions;

public class ItemAlreadyOnLoanException extends Exception {
    public ItemAlreadyOnLoanException(String message) {
        super(message);
    }
}
