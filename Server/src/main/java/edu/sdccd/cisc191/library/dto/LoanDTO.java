package edu.sdccd.cisc191.library.dto;

import edu.sdccd.cisc191.library.model.Book;

public class LoanDTO {
    private Book book;
    private String userId;

    public LoanDTO(Book book, String userId) {
        this.book = book;
        this.userId = userId;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
