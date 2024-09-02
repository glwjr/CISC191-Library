package edu.sdccd.cisc191.library;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class LoanTest {
    private Loan loan;
    private User user;
    private Book book;

    @BeforeEach
    void setUp() {
        user = new User("Test User", UserRole.MEMBER);
        book = new Book("Test Book", "Test Author");
        loan = new Loan(book, user);
    }

    @Test
    void testLoanCreation() {
        assertNotNull(loan.getLoanId(), "Loan ID should not be null");
        assertEquals(book, loan.getBook(), "Book should be the same");
        assertEquals(user, loan.getUser(), "User should be the same");
        assertEquals(LocalDate.now(), loan.getLoanDate(), "Loan Date should be the same");
        assertEquals(LocalDate.now().plusDays(14), loan.getDueDate(), "Due Date should be the same");
        assertNull(loan.getReturnDate(), "Return Date should initially be null");
    }

    @Test
    void testOverdueLoan() {
        assertFalse(loan.isOverdue(), "Loan should not be overdue immediately after creation");

        LocalDate overdueDate = LocalDate.now().plusDays(15);
        assertTrue(overdueDate.isAfter(loan.getDueDate()), "Overdue date should be after due date");

        LocalDate pastDueDate = LocalDate.now().minusDays(15);
        loan.setDueDate(pastDueDate);
        assertTrue(loan.isOverdue(), "Loan should be overdue after due date");
    }

    @Test
    void testReturnBook() {
        loan.returnBook();
        assertNotNull(loan.getReturnDate(), "Return Date should not be null after returning");
        assertEquals(LocalDate.now(), loan.getReturnDate(), "Return Date should be the same");
        assertTrue(user.getBorrowedBooks().isEmpty(), "User's borrowed books should be empty after returning");
    }
}
