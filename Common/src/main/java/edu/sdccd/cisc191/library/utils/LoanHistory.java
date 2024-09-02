package edu.sdccd.cisc191.library.utils;

import edu.sdccd.cisc191.library.Book;
import edu.sdccd.cisc191.library.Loan;
import edu.sdccd.cisc191.library.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoanHistory {
    private final List<List<Loan>> loanHistory;
    private final Map<String, Integer> userIdToIndexMap;
    private final int maxLoansPerUser;

    public LoanHistory(int numberOfUsers, int maxLoansPerUser) {
        loanHistory = new ArrayList<>(numberOfUsers);
        userIdToIndexMap = new HashMap<>();
        this.maxLoansPerUser = maxLoansPerUser;

        for (int i = 0; i < numberOfUsers; i++) {
            loanHistory.add(new ArrayList<>(maxLoansPerUser));
        }
    }

    public void addUser(User user) {
        String userId = user.getUserId();
        userIdToIndexMap.computeIfAbsent(userId, id -> userIdToIndexMap.size());
    }

    private int getUserIndex(String userId) {
        Integer index = userIdToIndexMap.get(userId);
        if (index == null) {
            throw new IllegalArgumentException("User not found in LoanHistory");
        }
        return index;
    }

    public void addLoan(User user, Book book) throws ItemAlreadyOnLoanException, LoanLimitExceededException {
        String userId = user.getUserId();

        int userIndex = userIdToIndexMap.computeIfAbsent(userId, id -> {
            int newIndex = loanHistory.size();
            loanHistory.add(new ArrayList<>(maxLoansPerUser));
            return newIndex;
        });

        List<Loan> userLoans = loanHistory.get(userIndex);

        if (userLoans.size() >= maxLoansPerUser) {
            throw new LoanLimitExceededException("User " + userId + " has reached the maximum number of loans.");
        }

        if (book.isOnLoan()) {
            throw new ItemAlreadyOnLoanException("The book '" + book.getTitle() + "' is already on loan.");
        }

        Loan loan = new Loan(book, user);
        userLoans.add(loan);
        book.setOnLoan(true);
    }

    public List<Loan> getUserLoanHistory(String userId) {
        int userIndex = getUserIndex(userId);
        return loanHistory.get(userIndex);
    }

    public List<Loan> getUserOverdueLoans(String userId) {
        int userIndex = getUserIndex(userId);
        List<Loan> allLoans = loanHistory.get(userIndex);
        List<Loan> overdueLoans = new ArrayList<>();

        for (Loan loan : allLoans) {
            if (loan.isOverdue()) {
                overdueLoans.add(loan);
            }
        }
        return overdueLoans;
    }
}
