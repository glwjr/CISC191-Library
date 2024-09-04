package edu.sdccd.cisc191.library;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserTest {
    private User user;

    @BeforeEach
    void setUp() {
        user = new User("Zora", UserRole.MEMBER);
    }

    @Test
    void testUserCreation() {
        assertNotNull(user.getUserId(), "User ID should not be null");
        assertEquals("Zora", user.getName(), "User name should be Zora");
        assertEquals(UserRole.MEMBER, user.getRole(), "User role should be 'MEMBER'");
    }

    @Test
    void testSetUserName() {
        user.setName("Noah");
        assertEquals("Noah", user.getName(), "User name should be 'Noah' after setting user name");
    }

    @Test
    void testSetUserRole() {
        user.setRole(UserRole.LIBRARIAN);
        assertEquals(UserRole.LIBRARIAN, user.getRole(), "User role should be 'LIBRARIAN'");
    }

    @Test
    void testLoanLifecycle() {
        Book book = new Book("Test Book", "Test Author");
        Loan loan = new Loan(book, user.getUserId());

        assertEquals(book, loan.getBook(), "Book in loan should match the original book");
        assertEquals(user.getUserId(), loan.getUserId(), "User in loan should match the original user");

        loan.returnBook();
        assertNotNull(loan.getReturnDate(), "Loan should have a returned date");
    }
}
