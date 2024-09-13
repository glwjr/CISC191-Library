package edu.sdccd.cisc191.library.service;

import edu.sdccd.cisc191.library.exceptions.ItemAlreadyOnLoanException;
import edu.sdccd.cisc191.library.exceptions.ItemNotFoundException;
import edu.sdccd.cisc191.library.exceptions.LoanLimitExceededException;
import edu.sdccd.cisc191.library.exceptions.UserNotFoundException;
import edu.sdccd.cisc191.library.model.Book;
import edu.sdccd.cisc191.library.model.Loan;
import edu.sdccd.cisc191.library.model.User;
import edu.sdccd.cisc191.library.model.UserRole;
import edu.sdccd.cisc191.library.repository.BookRepository;
import edu.sdccd.cisc191.library.repository.LoanRepository;
import edu.sdccd.cisc191.library.repository.UserRepository;
import edu.sdccd.cisc191.library.repository.implementations.FileBookRepository;
import edu.sdccd.cisc191.library.repository.implementations.FileLoanRepository;
import edu.sdccd.cisc191.library.repository.implementations.FileUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LoanServiceTest {
    private User user;
    private LoanService loanService;
    private LoanRepository loanRepository;
    private BookRepository bookRepository;
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() throws IOException {
        int maxLoansPerUser = 2;
        user = new User("User", UserRole.MEMBER);

        Path loansTempFile = Files.createTempFile("loans", ".json");
        Path booksTempFile = Files.createTempFile("books", ".json");
        Path usersTempFile = Files.createTempFile("users", ".json");

        loanRepository = new FileLoanRepository(loansTempFile);
        bookRepository = new FileBookRepository(booksTempFile);
        userRepository = new FileUserRepository(usersTempFile);

        loanService = new LoanService(maxLoansPerUser, loanRepository, userRepository, bookRepository);

        userRepository.addUser(user);
    }

    @Test
    public void testGetAllLoans() throws IOException, LoanLimitExceededException, ItemAlreadyOnLoanException {
        User user2 = new User("User2", UserRole.MEMBER);
        Book book1 = new Book("Book1", "Author1");
        Book book2 = new Book("Book2", "Author2");

        userRepository.addUser(user2);

        bookRepository.addBook(book1);
        bookRepository.addBook(book2);

        loanService.addLoan(user.getUserId(), book1.getItemId());
        loanService.addLoan(user2.getUserId(), book2.getItemId());

        assertEquals(2, loanService.getAllLoans().size());
    }

    @Test
    public void testGetLoanById() throws IOException {
        Book book1 = new Book("Book1", "Author1");
        Loan loan = new Loan(book1, user.getUserId());

        bookRepository.addBook(book1);

        loanRepository.addLoan(loan);

        assertTrue(book1.isOnLoan());
        assertEquals(loan.getLoanId(), loanService.getLoanById(loan.getLoanId()).getLoanId());
    }

    @Test
    public void testGetLoansByUserId() throws IOException, LoanLimitExceededException, ItemAlreadyOnLoanException {
        Book book1 = new Book("Book1", "Author1");
        Book book2 = new Book("Book2", "Author2");

        bookRepository.addBook(book1);
        bookRepository.addBook(book2);

        Loan loan1 = loanService.addLoan(user.getUserId(), book1.getItemId());
        loanService.addLoan(user.getUserId(), book2.getItemId());

        assertEquals(2, loanService.getLoansByUserId(user.getUserId()).size());
        assertEquals(book1.getTitle(), loanService.getLoanById(loan1.getLoanId()).getBook().getTitle());
    }

    @Test
    public void testGetOverdueLoansByUserId() throws IOException, LoanLimitExceededException,
            ItemAlreadyOnLoanException {
        Book book1 = new Book("Book1", "Author1");
        Book book2 = new Book("Book2", "Author2");

        bookRepository.addBook(book1);
        bookRepository.addBook(book2);

        loanService.addLoan(user.getUserId(), book1.getItemId());
        loanService.addLoan(user.getUserId(), book2.getItemId());

        Loan loan1 = loanService.getLoansByUserId(user.getUserId()).get(0);
        Loan loan2 = loanService.getLoansByUserId(user.getUserId()).get(1);

        loan1.setDueDate(LocalDate.now().minusDays(2));
        loan2.setDueDate(LocalDate.now().minusDays(2));

        loanService.updateLoan(loan1);
        loanService.updateLoan(loan2);

        List<Loan> overdueLoans = loanService.getOverdueLoansByUserId(user.getUserId());

        assertEquals(2, overdueLoans.size());
    }

    @Test
    public void testAddLoan() throws IOException, LoanLimitExceededException, ItemAlreadyOnLoanException {
        Book book1 = new Book("Book1", "Author1");
        Book book2 = new Book("Book2", "Author2");

        assertEquals(user.getName(), userRepository.getUserById(user.getUserId()).getName());

        bookRepository.addBook(book1);
        bookRepository.addBook(book2);

        assertEquals(book1.getTitle(), bookRepository.getBookById(book1.getItemId()).getTitle());
        assertEquals(book2.getTitle(), bookRepository.getBookById(book2.getItemId()).getTitle());

        loanService.addLoan(user.getUserId(), book1.getItemId());
        loanService.addLoan(user.getUserId(), book2.getItemId());

        assertEquals(2, loanService.getLoansByUserId(user.getUserId()).size());
        assertThrows(UserNotFoundException.class, () -> loanService.addLoan("InvalidUserId", book1.getItemId()));
        assertThrows(ItemNotFoundException.class, () -> loanService.addLoan(user.getUserId(), "InvalidItemId"));
    }

    @Test
    public void testLoanLimitExceeded() throws IOException, LoanLimitExceededException, ItemAlreadyOnLoanException {
        Book book1 = new Book("Book1", "Author1");
        Book book2 = new Book("Book2", "Author2");
        Book book3 = new Book("Book3", "Author3");

        bookRepository.addBook(book1);
        bookRepository.addBook(book2);
        bookRepository.addBook(book3);

        loanService.addLoan(user.getUserId(), book1.getItemId());
        loanService.addLoan(user.getUserId(), book2.getItemId());

        assertThrows(LoanLimitExceededException.class, () -> loanService.addLoan(user.getUserId(), book3.getItemId()));
    }

    @Test
    public void testBookAlreadyOnLoan() throws IOException, LoanLimitExceededException, ItemAlreadyOnLoanException {
        User user2 = new User("User2", UserRole.MEMBER);
        Book book = new Book("Book1", "Author1");

        userRepository.addUser(user2);

        bookRepository.addBook(book);

        loanService.addLoan(user.getUserId(), book.getItemId());

        assertThrows(ItemAlreadyOnLoanException.class, () -> loanService.addLoan(user2.getUserId(), book.getItemId()));
    }

    @Test
    public void testDeleteLoan() throws IOException, LoanLimitExceededException, ItemAlreadyOnLoanException {
        Book book = new Book("Book", "Author");

        bookRepository.addBook(book);

        loanService.addLoan(user.getUserId(), book.getItemId());

        Loan loan = loanService.getLoansByUserId(user.getUserId()).get(0);

        assertNotNull(loan);

        loanService.deleteLoan(loan.getLoanId());

        assertEquals(0, loanService.getLoansByUserId(user.getUserId()).size());
    }
}
