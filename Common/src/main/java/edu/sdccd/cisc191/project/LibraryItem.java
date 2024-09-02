package edu.sdccd.cisc191.project;

import java.util.UUID;

public abstract class LibraryItem {
    private final String itemId;
    private String title;

    public LibraryItem(String title) {
        this.itemId = UUID.randomUUID().toString();
        this.title = title;
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
}
