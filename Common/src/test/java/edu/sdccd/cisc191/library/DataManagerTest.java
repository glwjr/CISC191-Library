package edu.sdccd.cisc191.library;

import edu.sdccd.cisc191.library.utils.DataManager;
import edu.sdccd.cisc191.library.utils.ItemAlreadyOnLoanException;
import edu.sdccd.cisc191.library.utils.LoanLimitExceededException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class DataManagerTest {
    private static DataManager dataManager;
    private static Path tempDirectory;

    @BeforeAll
    static void setUp() throws IOException {
        int maxLoansPerUser = 2;
        tempDirectory = Files.createTempDirectory("dataTest");
        dataManager = new DataManager(maxLoansPerUser, tempDirectory.toString());
    }

    @AfterAll
    static void tearDown() throws IOException {
        try(Stream<Path> paths = Files.walk(tempDirectory)) {
            paths.map(Path::toFile).forEach(file -> {
                if (!file.delete()) {
                    file.deleteOnExit();
                }
            });
        }
    }

    @Test
    void testInitializeData() {
        assertTrue(Files.exists(tempDirectory.resolve("users.json")));
        assertTrue(Files.exists(tempDirectory.resolve("books.json")));
        assertTrue(Files.exists(tempDirectory.resolve("loans.json")));
    }

    @Test
    void testAddUser() throws IOException {
        User user = new User("User1", UserRole.MEMBER);
        dataManager.saveUser(user);

        User loadedUser = dataManager.loadUser(user.getUserId());
        assertNotNull(loadedUser);
        assertEquals("User1", loadedUser.getName());
    }

    @Test
    void testAddBook() throws IOException {
        Book book = new Book("Book1", "Author1");
        dataManager.saveBook(book);

        Book loadedBook = dataManager.loadBook(book.getItemId());
        assertNotNull(loadedBook);
        assertEquals("Book1", loadedBook.getTitle());
    }

    @Test
    void testAddLoan() throws IOException, LoanLimitExceededException, ItemAlreadyOnLoanException {
        User user = new User("User2", UserRole.MEMBER);
        dataManager.saveUser(user);

        Book book1 = new Book("Book", "Author");
        Book book2 = new Book("Book", "Author");
        dataManager.saveBook(book1);
        dataManager.saveBook(book2);

        dataManager.addLoan(user, book1);
        dataManager.addLoan(user, book2);

        List<Loan> loans = dataManager.getUserLoanHistory(user.getUserId());
        assertEquals(2, loans.size());
        assertEquals(book1.getTitle(), loans.get(0).getBook().getTitle());
        assertEquals(book2.getTitle(), loans.get(0).getBook().getTitle());
    }

    @Test
    void testLoanLimitExceededException() throws Exception {
        User user = new User("User3", UserRole.MEMBER);
        dataManager.saveUser(user);

        Book book1 = new Book("Book1", "Author1");
        Book book2 = new Book("Book2", "Author2");
        Book book3 = new Book("Book3", "Author3");
        dataManager.saveBook(book1);
        dataManager.saveBook(book2);
        dataManager.saveBook(book3);

        dataManager.addLoan(user, book1);
        dataManager.addLoan(user, book2);

        assertEquals(2, dataManager.getUserLoanHistory(user.getUserId()).size());
        assertThrows(LoanLimitExceededException.class, () -> dataManager.addLoan(user, book3));
    }

    @Test
    void testAlreadyOnLoanException() throws Exception {
        User user = new User("User4", UserRole.MEMBER);
        dataManager.saveUser(user);

        Book book = new Book("Book4", "Author4");
        dataManager.saveBook(book);

        dataManager.addLoan(user, book);

        assertThrows(ItemAlreadyOnLoanException.class, () -> dataManager.addLoan(user, book));
    }

    @Test
    void testReturnBook() throws IOException, LoanLimitExceededException, ItemAlreadyOnLoanException {
        User user = new User("User5", UserRole.MEMBER);
        dataManager.saveUser(user);

        Book book = new Book("Book5", "Author5");
        dataManager.saveBook(book);

        dataManager.addLoan(user, book);

        Loan loan = dataManager.getUserLoanHistory(user.getUserId()).get(0);
        loan.returnBook();
    }

    @Test
    void testGetOverdueLoans() throws IOException, LoanLimitExceededException, ItemAlreadyOnLoanException {
        User user = new User("User6", UserRole.MEMBER);
        dataManager.saveUser(user);

        Book book1 = new Book("Book6", "Author6");
        Book book2 = new Book("Book7", "Author7");
        dataManager.saveBook(book1);
        dataManager.saveBook(book2);

        dataManager.addLoan(user, book1);
        dataManager.addLoan(user, book2);

        Loan loan1 = dataManager.getUserLoanHistory(user.getUserId()).get(0);
        Loan loan2 = dataManager.getUserLoanHistory(user.getUserId()).get(1);

        loan1.setDueDate(loan1.getDueDate().minusDays(15));
        loan2.setDueDate(loan2.getDueDate().minusDays(15));

        dataManager.saveLoan(loan1);
        dataManager.saveLoan(loan2);

        List<Loan> overdueLoans = dataManager.getUserOverdueLoans(user.getUserId());
        assertEquals(2, overdueLoans.size());
    }
}
