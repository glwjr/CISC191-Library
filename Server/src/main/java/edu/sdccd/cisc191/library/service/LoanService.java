package edu.sdccd.cisc191.library.service;

import edu.sdccd.cisc191.library.exceptions.*;
import edu.sdccd.cisc191.library.model.Book;
import edu.sdccd.cisc191.library.model.Loan;
import edu.sdccd.cisc191.library.model.User;
import edu.sdccd.cisc191.library.repository.BookRepository;
import edu.sdccd.cisc191.library.repository.LoanRepository;
import edu.sdccd.cisc191.library.repository.UserRepository;

import java.io.IOException;
import java.util.List;

public class LoanService {
    private final int maxLoansPerUser;
    private final LoanRepository loanRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public LoanService(int maxLoansPerUser, LoanRepository loanRepository, UserRepository userRepository,
                       BookRepository bookRepository) {
        this.maxLoansPerUser = maxLoansPerUser;
        this.loanRepository = loanRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    public List<Loan> getAllLoans() {
        return loanRepository.getAllLoans();
    }

    public Loan getLoanById(String loanId) {
        return loanRepository.getLoanById(loanId);
    }

    public List<Loan> getLoansByUserId(String userId) {
        return loanRepository.getLoansByUserId(userId);
    }

    public List<Loan> getOverdueLoansByUserId(String userId) {
        return loanRepository.getOverdueLoansByUserId(userId);
    }

    public void addLoan(String userId, String itemId) throws LoanLimitExceededException, ItemAlreadyOnLoanException,
            IOException {
        User user = userRepository.getUserById(userId);
        Book book = bookRepository.getBookById(itemId);

        if (user == null) {
            throw new UserNotFoundException("User not found");
        }

        if (book == null) {
            throw new ItemNotFoundException("Book not found");
        }

        List<Loan> userLoans = loanRepository.getLoansByUserId(userId);
        if (userLoans.size() >= maxLoansPerUser) {
            throw new LoanLimitExceededException("User has reached the loan limit");
        }

        if (book.isOnLoan()) {
            throw new ItemAlreadyOnLoanException("Book is already on loan");
        }

        Loan newLoan = new Loan(book, userId);

        loanRepository.addLoan(newLoan);
        bookRepository.updateBook(book);
    }

    public void updateLoan(Loan loan) throws IOException {
        loanRepository.updateLoan(loan);
    }

    public void deleteLoan(String loanId) throws IOException {
        loanRepository.deleteLoan(loanId);
    }
}
