package edu.sdccd.cisc191.library.controller;

import edu.sdccd.cisc191.library.exceptions.ItemAlreadyOnLoanException;
import edu.sdccd.cisc191.library.exceptions.LoanLimitExceededException;
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
import edu.sdccd.cisc191.library.service.BookService;
import edu.sdccd.cisc191.library.service.LoanService;
import edu.sdccd.cisc191.library.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class LoanControllerTest {
    private LoanController loanController;
    private UserController userController;
    private BookController bookController;
    private LoanService loanService;
    private User user1;
    private User user2;

    @BeforeEach
    public void setUp() throws IOException {
        Path loanTempFile = Files.createTempFile("loans", ".json");
        Path userTempFile = Files.createTempFile("users", ".json");
        Path bookTempFile = Files.createTempFile("books", ".json");

        LoanRepository loanRepository = new FileLoanRepository(loanTempFile);
        UserRepository userRepository = new FileUserRepository(userTempFile);
        BookRepository bookRepository = new FileBookRepository(bookTempFile);

        int maxLoansPerUser = 2;

        loanService = new LoanService(maxLoansPerUser, loanRepository, userRepository, bookRepository);
        UserService userService = new UserService(userRepository);
        BookService bookService = new BookService(bookRepository);

        loanController = new LoanController(loanService);
        userController = new UserController(userService);
        bookController = new BookController(bookService);

        user1 = new User("User1", UserRole.MEMBER);
        user2 = new User("User2", UserRole.MEMBER);

        userController.addUser(user1);
        userController.addUser(user2);

    }

    @Test
    public void testGetAllLoans() throws IOException, LoanLimitExceededException, ItemAlreadyOnLoanException {
        Book book1 = new Book("Book1", "Author1");
        Book book2 = new Book("Book2", "Author2");

        bookController.addBook(book1);
        bookController.addBook(book2);

        loanController.addLoan(user1.getUserId(), book1.getItemId());
        loanController.addLoan(user2.getUserId(), book2.getItemId());

        Map<String, Loan> loans = loanController.getAllLoans();
        assertEquals(2, loans.size());
    }

    @Test
    public void testGetLoanById() throws IOException, LoanLimitExceededException, ItemAlreadyOnLoanException {
        Book book = new Book("Book", "Author");

        bookController.addBook(book);

        Loan loan = loanService.addLoan(user1.getUserId(), book.getItemId());

        Loan retrievedLoan = loanController.getLoanById(loan.getLoanId());

        assertEquals(user1.getUserId(), retrievedLoan.getUserId());
        assertEquals(book.getTitle(), retrievedLoan.getBook().getTitle());
    }

    @Test
    public void testGetOverdueLoansByUserId() throws IOException, LoanLimitExceededException,
            ItemAlreadyOnLoanException {
        Book book1 = new Book("Book1", "Author1");
        Book book2 = new Book("Book2", "Author2");
        Book book3 = new Book("Book3", "Author3");

        bookController.addBook(book1);
        bookController.addBook(book2);
        bookController.addBook(book3);

        Loan loan1 = loanController.addLoan(user1.getUserId(), book1.getItemId());
        Loan loan2 = loanController.addLoan(user1.getUserId(), book2.getItemId());

        loan1.setDueDate(LocalDate.now().minusDays(1));
        loan2.setDueDate(LocalDate.now().minusDays(1));

        loanController.updateLoan(loan1);
        loanController.updateLoan(loan2);

        List<Loan> overdueLoans = loanController.getOverdueLoansByUserId(user1.getUserId());

        assertEquals(2, overdueLoans.size());
    }

    @Test
    public void testAddLoan() throws IOException, LoanLimitExceededException, ItemAlreadyOnLoanException {
        Book book1 = new Book("Book1", "Author1");

        bookController.addBook(book1);

        loanController.addLoan(user1.getUserId(), book1.getItemId());

        assertEquals(1, loanController.getAllLoans().size());
    }

    @Test
    public void testLoanLimitExceededException() throws IOException, LoanLimitExceededException,
            ItemAlreadyOnLoanException {
        Book book1 = new Book("Book1", "Author1");
        Book book2 = new Book("Book2", "Author2");
        Book book3 = new Book("Book3", "Author3");

        bookController.addBook(book1);
        bookController.addBook(book2);
        bookController.addBook(book3);

        loanController.addLoan(user1.getUserId(), book1.getItemId());
        loanController.addLoan(user1.getUserId(), book2.getItemId());

        assertThrows(LoanLimitExceededException.class, () ->
                loanController.addLoan(user1.getUserId(), book3.getItemId()));
    }

    @Test
    public void testItemAlreadyOnLoanException() throws IOException, ItemAlreadyOnLoanException,
            LoanLimitExceededException {
        Book book1 = new Book("Book1", "Author1");

        bookController.addBook(book1);

        loanController.addLoan(user1.getUserId(), book1.getItemId());

        assertThrows(ItemAlreadyOnLoanException.class, () ->
                loanController.addLoan(user2.getUserId(), book1.getItemId()));
    }

    @Test
    public void testUpdateLoan() throws IOException, LoanLimitExceededException, ItemAlreadyOnLoanException {
        Book book = new Book("Book1", "Author1");

        bookController.addBook(book);

        Loan loan = loanController.addLoan(user1.getUserId(), book.getItemId());

        loan.returnBook();

        loanController.updateLoan(loan);

        assertNotNull(loan.getReturnDate());
    }

    @Test
    public void testDeleteLoan() throws IOException, LoanLimitExceededException, ItemAlreadyOnLoanException {
        Book book1 = new Book("Book1", "Author1");
        Book book2 = new Book("Book2", "Author2");

        bookController.addBook(book1);
        bookController.addBook(book2);

        Loan loan1 = loanController.addLoan(user1.getUserId(), book1.getItemId());
        loanController.addLoan(user1.getUserId(), book2.getItemId());

        loanController.deleteLoan(loan1.getLoanId());

        assertEquals(1, loanController.getAllLoans().size());
    }
}
