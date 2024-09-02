package edu.sdccd.cisc191.library;

import java.util.UUID;

public abstract class LibraryItem {
    private final String itemId;
    private String title;
    private boolean isOnLoan;

    public LibraryItem(String title) {
        this.itemId = UUID.randomUUID().toString();
        this.title = title;
        this.isOnLoan = false;
    }

    public String getItemId() {
        return itemId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isOnLoan() {
        return isOnLoan;
    }

    public void setOnLoan(boolean onLoan) {
        this.isOnLoan = onLoan;
    }
}
