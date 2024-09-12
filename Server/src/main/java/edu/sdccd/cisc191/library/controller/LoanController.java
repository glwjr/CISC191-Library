package edu.sdccd.cisc191.library.controller;

import edu.sdccd.cisc191.library.dto.LoanDTO;
import edu.sdccd.cisc191.library.exceptions.ItemAlreadyOnLoanException;
import edu.sdccd.cisc191.library.exceptions.LoanLimitExceededException;
import edu.sdccd.cisc191.library.model.Loan;
import edu.sdccd.cisc191.library.service.LoanService;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class LoanController {
    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    private LoanDTO convertToDTO(Loan loan) {
        return new LoanDTO(loan.getBook(), loan.getUserId());
    }

    private Loan convertToLoan(LoanDTO loanDTO) {
        return new Loan(loanDTO.getBook(), loanDTO.getUserId());
    }

    public List<LoanDTO> getAllLoans() {
        List<Loan> loans = loanService.getAllLoans();
        return loans.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public LoanDTO getLoanById(String loanId) {
        Loan loan = loanService.getLoanById(loanId);
        return convertToDTO(loan);
    }

    public List<LoanDTO> getLoansByUserId(String userId) {
        List<Loan> loans = loanService.getLoansByUserId(userId);
        return loans.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<LoanDTO> getOverdueLoansByUserId(String userId) {
        List<Loan> loans = loanService.getOverdueLoansByUserId(userId);
        return loans.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public LoanDTO addLoan(String userId, String itemId) throws LoanLimitExceededException, IOException,
            ItemAlreadyOnLoanException {
        Loan newLoan = loanService.addLoan(userId, itemId);
        return convertToDTO(newLoan);
    }

    public LoanDTO updateLoan(LoanDTO updatedLoanDTO) throws IOException {
        Loan updatedLoan = convertToLoan(updatedLoanDTO);
        Loan savedLoan = loanService.updateLoan(updatedLoan);
        return convertToDTO(savedLoan);
    }

    public void deleteLoan(String loanId) throws IOException {
        loanService.deleteLoan(loanId);
    }
}