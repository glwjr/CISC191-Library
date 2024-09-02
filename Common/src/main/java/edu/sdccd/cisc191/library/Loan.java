package edu.sdccd.cisc191.library;

import java.time.LocalDate;
import java.util.UUID;

public class Loan {
    private static final int LOAN_PERIOD_DAYS = 14;

    private final String loanId;
    private Book book;
    private User user;
    private final LocalDate loanDate;
    private final LocalDate dueDate;
    private LocalDate returnDate;

    public Loan(Book book, User user) {
        this.loanId = UUID.randomUUID().toString();
        this.book = book;
        this.user = user;
        this.loanDate = LocalDate.now();
        this.dueDate = loanDate.plusDays(LOAN_PERIOD_DAYS);
        this.returnDate = null;

        user.borrowBook(this);
    }

    public String getLoanId() {
        return loanId;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDate getLoanDate() {
        return loanDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public boolean isOverdue() {
        return returnDate == null && LocalDate.now().isAfter(dueDate);
    }
}
