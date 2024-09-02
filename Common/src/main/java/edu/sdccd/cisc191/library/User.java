package edu.sdccd.cisc191.library;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class User {
    private final String userId;
    private String name;
    private UserRole role;
    private final List<Loan> borrowedBooks;

    public User(String name, UserRole role) {
        this.userId = UUID.randomUUID().toString();
        this.name = name;
        this.role = role;
        this.borrowedBooks = new ArrayList<>();
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public List<Loan> getBorrowedBooks() {
        return borrowedBooks;
    }

    public void borrowBook(Loan loan) {
        borrowedBooks.add(loan);
    }

    public void returnBook(Loan loan) {
        borrowedBooks.remove(loan);
    }
}
