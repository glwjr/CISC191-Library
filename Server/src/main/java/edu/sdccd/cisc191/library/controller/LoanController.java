package edu.sdccd.cisc191.library.controller;

import edu.sdccd.cisc191.library.exceptions.ItemAlreadyOnLoanException;
import edu.sdccd.cisc191.library.exceptions.LoanLimitExceededException;
import edu.sdccd.cisc191.library.model.Loan;
import edu.sdccd.cisc191.library.service.LoanService;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class LoanController {
    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    public Map<String, Loan> getAllLoans() {
        return loanService.getAllLoans();
    }

    public Loan getLoanById(String loanId) {
        return loanService.getLoanById(loanId);
    }

    public List<Loan> getLoansByUserId(String userId) {
        return loanService.getLoansByUserId(userId);
    }

    public List<Loan> getOverdueLoansByUserId(String userId) {
        return loanService.getOverdueLoansByUserId(userId);
    }

    public Loan addLoan(String userId, String itemId) throws LoanLimitExceededException, IOException,
            ItemAlreadyOnLoanException {
        return loanService.addLoan(userId, itemId);
    }

    public Loan updateLoan(Loan loan) throws IOException {
        return loanService.updateLoan(loan);
    }

    public void deleteLoan(String loanId) throws IOException {
        loanService.deleteLoan(loanId);
    }
}