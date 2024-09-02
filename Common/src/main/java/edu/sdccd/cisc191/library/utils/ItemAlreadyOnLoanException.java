package edu.sdccd.cisc191.library.utils;

public class ItemAlreadyOnLoanException extends Exception {
    public ItemAlreadyOnLoanException(String message) {
        super(message);
    }
}
