package edu.sdccd.cisc191.library.repository.implementations;

import edu.sdccd.cisc191.library.exceptions.LoanNotFoundException;
import edu.sdccd.cisc191.library.model.Book;
import edu.sdccd.cisc191.library.model.Loan;
import edu.sdccd.cisc191.library.model.User;
import edu.sdccd.cisc191.library.model.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FileLoanRepositoryTest {
    private FileLoanRepository loanRepository;
    private User user;

    @BeforeEach
    public void setUp() throws IOException {
        Path tempFile = Files.createTempFile("loans", ".json");
        loanRepository = new FileLoanRepository(tempFile);
        user = new User("User1", UserRole.MEMBER);
    }

    @Test
    public void testAddLoan() throws IOException {
        Book book = new Book("Book", "Author");
        Loan loan = new Loan(book, user.getUserId());
        loanRepository.addLoan(loan);

        Loan retrievedLoan = loanRepository.getLoanById(loan.getLoanId());
        assertNotNull(retrievedLoan);
        assertEquals(loan.getLoanId(), retrievedLoan.getLoanId());
    }

    @Test
    public void testGetLoansByUserId() throws IOException {
        Book book1 = new Book("Book1", "Author1");
        Book book2 = new Book("Book2", "Author2");
        Loan loan1 = new Loan(book1, user.getUserId());
        Loan loan2 = new Loan(book2, user.getUserId());

        loanRepository.addLoan(loan1);
        loanRepository.addLoan(loan2);

        List<Loan> loans = loanRepository.getLoansByUserId(user.getUserId());
        assertEquals(2, loans.size());
        assertEquals(loan1.getLoanId(), loans.get(0).getLoanId());
    }

    @Test
    public void testUpdateLoan() throws IOException {
        Book book = new Book("Book", "Author");
        Loan loan = new Loan(book, user.getUserId());

        loanRepository.addLoan(loan);

        loan.getBook().setTitle("New Title");

        loanRepository.updateLoan(loan);

        Loan updatedLoan = loanRepository.getLoanById(loan.getLoanId());
        assertEquals("New Title", updatedLoan.getBook().getTitle());
    }

    @Test
    public void testDeleteLoan() throws IOException {
        Book book1 = new Book("Book1", "Author1");
        Book book2 = new Book("Book2", "Author2");
        Loan loan1 = new Loan(book1, user.getUserId());
        Loan loan2 = new Loan(book2, user.getUserId());

        loanRepository.addLoan(loan1);
        loanRepository.addLoan(loan2);

        assertEquals(2, loanRepository.getLoansByUserId(user.getUserId()).size());

        loanRepository.deleteLoan(loan1.getLoanId());

        assertThrows(LoanNotFoundException.class, () -> loanRepository.getLoanById(loan1.getLoanId()));
        assertEquals(1, loanRepository.getLoansByUserId(user.getUserId()).size());
    }
}
