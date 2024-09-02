package edu.sdccd.cisc191.library;

import edu.sdccd.cisc191.library.utils.ItemAlreadyOnLoanException;
import edu.sdccd.cisc191.library.utils.LoanHistory;
import edu.sdccd.cisc191.library.utils.LoanLimitExceededException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LoanHistoryTest {
    private LoanHistory loanHistory;
    private User user;
    private Book book1;
    private Book book2;

    @BeforeEach
    void setUp() {
        loanHistory = new LoanHistory(10, 2);
        user = new User("Zora", UserRole.MEMBER);
        book1 = new Book("Book1", "Author1");
        book2 = new Book("Book2", "Author2");
    }

    @Test
    void testAddLoan() throws ItemAlreadyOnLoanException, LoanLimitExceededException {
        loanHistory.addLoan(user, book1);
        loanHistory.addLoan(user, book2);

        List<Loan> userLoans = loanHistory.getUserLoanHistory(user.getUserId());
        assertEquals(2, userLoans.size());
        assertEquals(book1.getTitle(), userLoans.get(0).getBook().getTitle());
        assertEquals(book2.getTitle(), userLoans.get(1).getBook().getTitle());
    }

    @Test
    void testLoanLimitExceeded() {
        Book book3 = new Book("Book3", "Author3");
        assertDoesNotThrow(() -> loanHistory.addLoan(user, book1));
        assertDoesNotThrow(() -> loanHistory.addLoan(user, book2));

        Exception exception = assertThrows(LoanLimitExceededException.class, () -> loanHistory.addLoan(user, book3));

        String expectedMessage = "User " + user.getUserId() + " has reached the maximum number of loans.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testItemAlreadyOnLoan() {
        User user2 = new User("Noah", UserRole.MEMBER);
        assertDoesNotThrow(() -> loanHistory.addLoan(user, book1));

        Exception exception = assertThrows(ItemAlreadyOnLoanException.class, () -> loanHistory.addLoan(user2, book1));

        String expectedMessage = "The book '" + book1.getTitle() + "' is already on loan.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testGetUserLoanHistory() throws LoanLimitExceededException, ItemAlreadyOnLoanException {
        loanHistory.addLoan(user, book1);

        List<Loan> loans = loanHistory.getUserLoanHistory(user.getUserId());
        assertEquals(1, loans.size());
        assertEquals(book1.getTitle(), loans.get(0).getBook().getTitle());
    }

    @Test
    void testGetUserOverdueLoans() throws LoanLimitExceededException, ItemAlreadyOnLoanException {
        loanHistory.addLoan(user, book1);
        List<Loan> loans = loanHistory.getUserLoanHistory(user.getUserId());
        loans.get(0).setDueDate(LocalDate.now().minusDays(1));

        List<Loan> overdueLoans = loanHistory.getUserOverdueLoans(user.getUserId());
        assertEquals(1, overdueLoans.size());
        assertEquals(book1.getTitle(), overdueLoans.get(0).getBook().getTitle());
    }
}
