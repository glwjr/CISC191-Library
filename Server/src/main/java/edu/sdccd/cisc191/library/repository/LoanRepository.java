package edu.sdccd.cisc191.library.repository;

import edu.sdccd.cisc191.library.model.Loan;

import java.io.IOException;
import java.util.List;

public interface LoanRepository {
    List<Loan> getAllLoans();
    Loan getLoanById(String loanId);
    List<Loan> getLoansByUserId(String userId);
    List<Loan> getOverdueLoansByUserId(String userId);
    Loan addLoan(Loan loan) throws IOException;
    Loan updateLoan(Loan loan) throws IOException;
    void deleteLoan(String loanId) throws IOException;
}
