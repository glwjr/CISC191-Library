package edu.sdccd.cisc191.library;

import java.time.LocalDate;
import java.util.UUID;

public class Loan {
    private static final int LOAN_PERIOD_DAYS = 14;

    private final String loanId;
    private final Book book;
    private final String userId;
    private final LocalDate loanDate;
    private LocalDate dueDate;
    private LocalDate returnDate;

    public Loan(Book book, String userId) {
        this.loanId = UUID.randomUUID().toString();
        this.book = book;
        this.userId = userId;
        this.loanDate = LocalDate.now();
        this.dueDate = loanDate.plusDays(LOAN_PERIOD_DAYS);
        this.returnDate = null;

        book.setOnLoan(true);
    }

    public String getLoanId() {
        return loanId;
    }

    public Book getBook() {
        return book;
    }

    public String getUserId() {
        return userId;
    }

    public LocalDate getLoanDate() {
        return loanDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
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

    public void returnBook() {
        setReturnDate(LocalDate.now());
        book.setOnLoan(false);
    }
}
