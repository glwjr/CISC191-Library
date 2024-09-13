package edu.sdccd.cisc191.library;

import edu.sdccd.cisc191.library.controller.BookController;
import edu.sdccd.cisc191.library.controller.LoanController;
import edu.sdccd.cisc191.library.controller.UserController;
import edu.sdccd.cisc191.library.exceptions.ItemAlreadyOnLoanException;
import edu.sdccd.cisc191.library.exceptions.LoanLimitExceededException;
import edu.sdccd.cisc191.library.message.RequestType;
import edu.sdccd.cisc191.library.message.RequestWrapper;
import edu.sdccd.cisc191.library.message.ResponseStatus;
import edu.sdccd.cisc191.library.message.ResponseWrapper;
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

public class RequestHandlerTest {
    private RequestHandler requestHandler;
    private UserController userController;
    private BookController bookController;
    private LoanController loanController;

    @BeforeEach
    public void setUp() throws IOException {
        Path loanTempFile = Files.createTempFile("loans", ".json");
        Path userTempFile = Files.createTempFile("users", ".json");
        Path bookTempFile = Files.createTempFile("books", ".json");

        LoanRepository loanRepository = new FileLoanRepository(loanTempFile);
        UserRepository userRepository = new FileUserRepository(userTempFile);
        BookRepository bookRepository = new FileBookRepository(bookTempFile);

        int maxLoansPerUser = 2;

        LoanService loanService = new LoanService(maxLoansPerUser, loanRepository, userRepository, bookRepository);
        UserService userService = new UserService(userRepository);
        BookService bookService = new BookService(bookRepository);

        loanController = new LoanController(loanService);
        userController = new UserController(userService);
        bookController = new BookController(bookService);

        requestHandler = new RequestHandler(userController, bookController, loanController);
    }

    @Test
    public void testHandleGetAllUsers() throws IOException {
        User user1 = new User("User1", UserRole.MEMBER);
        User user2 = new User("User2", UserRole.MEMBER);

        userController.addUser(user1);
        userController.addUser(user2);

        RequestWrapper<Void> requestWrapper = new RequestWrapper<>(RequestType.GET_ALL_USERS, null);
        ResponseWrapper<?> response = requestHandler.handleRequest(requestWrapper);

        assertEquals(ResponseStatus.SUCCESS, response.getResponseStatus());
        assertInstanceOf(Map.class, response.getData());
        assertEquals(2, ((Map<?, ?>) response.getData()).size());
    }

    @Test
    public void testHandleGetUser() throws IOException {
        User user = new User("User", UserRole.MEMBER);

        userController.addUser(user);

        RequestWrapper<String> requestWrapper = new RequestWrapper<>(RequestType.GET_USER, user.getUserId());
        ResponseWrapper<?> response = requestHandler.handleRequest(requestWrapper);

        assertEquals(ResponseStatus.SUCCESS, response.getResponseStatus());
        assertInstanceOf(User.class, response.getData());
        assertEquals(user.getName(), ((User) response.getData()).getName());
    }

    @Test
    public void testHandleAddUser() {
        User user = new User("User", UserRole.MEMBER);

        RequestWrapper<User> requestWrapper = new RequestWrapper<>(RequestType.ADD_USER, user);
        ResponseWrapper<?> response = requestHandler.handleRequest(requestWrapper);

        assertEquals(ResponseStatus.SUCCESS, response.getResponseStatus());
        assertInstanceOf(User.class, response.getData());
        assertEquals(user.getName(), ((User) response.getData()).getName());
    }

    @Test
    public void testHandleUpdateUser() {
        User user = new User("User", UserRole.MEMBER);

        RequestWrapper<User> requestWrapper1 = new RequestWrapper<>(RequestType.ADD_USER, user);
        ResponseWrapper<?> response1 = requestHandler.handleRequest(requestWrapper1);

        assertEquals(ResponseStatus.SUCCESS, response1.getResponseStatus());
        assertInstanceOf(User.class, response1.getData());
        assertEquals(user.getName(), ((User) response1.getData()).getName());

        user.setName("New Name");

        RequestWrapper<User> requestWrapper2 = new RequestWrapper<>(RequestType.UPDATE_USER, user);
        ResponseWrapper<?> response = requestHandler.handleRequest(requestWrapper2);

        assertEquals(ResponseStatus.SUCCESS, response.getResponseStatus());
        assertInstanceOf(User.class, response.getData());
        assertEquals(user.getName(), ((User) response.getData()).getName());
    }

    @Test
    public void testHandleDeleteUser() {
        User user = new User("User", UserRole.MEMBER);

        RequestWrapper<User> requestWrapper1 = new RequestWrapper<>(RequestType.ADD_USER, user);
        ResponseWrapper<?> response1 = requestHandler.handleRequest(requestWrapper1);

        assertEquals(ResponseStatus.SUCCESS, response1.getResponseStatus());
        assertInstanceOf(User.class, response1.getData());

        RequestWrapper<String> requestWrapper2 = new RequestWrapper<>(RequestType.DELETE_USER, user.getUserId());
        ResponseWrapper<?> response2 = requestHandler.handleRequest(requestWrapper2);

        assertEquals(ResponseStatus.SUCCESS, response2.getResponseStatus());
        assertEquals("User deleted successfully", response2.getMessage());

        RequestWrapper<String> requestWrapper3 = new RequestWrapper<>(RequestType.GET_USER, user.getUserId());
        ResponseWrapper<?> response3 = requestHandler.handleRequest(requestWrapper3);

        assertEquals(ResponseStatus.ERROR, response3.getResponseStatus());
        assertEquals("User with ID " + user.getUserId() + " not found", response3.getMessage());
    }

    @Test
    public void testHandleGetAllBooks() throws IOException {
        Book book1 = new Book("Book1", "Author1");
        Book book2 = new Book("Book2", "Author2");

        bookController.addBook(book1);
        bookController.addBook(book2);

        RequestWrapper<Void> requestWrapper = new RequestWrapper<>(RequestType.GET_ALL_BOOKS, null);
        ResponseWrapper<?> response = requestHandler.handleRequest(requestWrapper);

        assertEquals(ResponseStatus.SUCCESS, response.getResponseStatus());
        assertInstanceOf(Map.class, response.getData());
        assertEquals(2, ((Map<?, ?>) response.getData()).size());
    }

    @Test
    public void testHandleGetBook() throws IOException {
        Book book = new Book("Book", "Author");

        bookController.addBook(book);

        RequestWrapper<String> requestWrapper = new RequestWrapper<>(RequestType.GET_BOOK, book.getItemId());
        ResponseWrapper<?> response = requestHandler.handleRequest(requestWrapper);

        assertEquals(ResponseStatus.SUCCESS, response.getResponseStatus());
        assertInstanceOf(Book.class, response.getData());
        assertEquals(book.getTitle(), ((Book) response.getData()).getTitle());
    }

    @Test
    public void testHandleAddBook() {
        Book book = new Book("Book", "Author");

        RequestWrapper<Book> requestWrapper = new RequestWrapper<>(RequestType.ADD_BOOK, book);
        ResponseWrapper<?> response = requestHandler.handleRequest(requestWrapper);

        assertEquals(ResponseStatus.SUCCESS, response.getResponseStatus());
        assertInstanceOf(Book.class, response.getData());
        assertEquals(book.getTitle(), ((Book) response.getData()).getTitle());
    }

    @Test
    public void testHandleUpdateBook() {
        Book book = new Book("Book", "Author");

        RequestWrapper<Book> requestWrapper1 = new RequestWrapper<>(RequestType.ADD_BOOK, book);
        ResponseWrapper<?> response1 = requestHandler.handleRequest(requestWrapper1);

        assertEquals(ResponseStatus.SUCCESS, response1.getResponseStatus());
        assertInstanceOf(Book.class, response1.getData());
        assertEquals(book.getTitle(), ((Book) response1.getData()).getTitle());

        book.setTitle("New Title");

        RequestWrapper<Book> requestWrapper2 = new RequestWrapper<>(RequestType.UPDATE_BOOK, book);
        ResponseWrapper<?> response2 = requestHandler.handleRequest(requestWrapper2);

        assertEquals(ResponseStatus.SUCCESS, response2.getResponseStatus());
        assertInstanceOf(Book.class, response2.getData());
        assertEquals(book.getTitle(), ((Book) response2.getData()).getTitle());
    }

    @Test
    public void testHandleDeleteBook() {
        Book book = new Book("Book", "Author");

        RequestWrapper<Book> requestWrapper1 = new RequestWrapper<>(RequestType.ADD_BOOK, book);
        ResponseWrapper<?> response1 = requestHandler.handleRequest(requestWrapper1);

        assertEquals(ResponseStatus.SUCCESS, response1.getResponseStatus());
        assertInstanceOf(Book.class, response1.getData());

        RequestWrapper<String> requestWrapper2 = new RequestWrapper<>(RequestType.DELETE_BOOK, book.getItemId());
        ResponseWrapper<?> response2 = requestHandler.handleRequest(requestWrapper2);

        assertEquals(ResponseStatus.SUCCESS, response2.getResponseStatus());
        assertEquals("Book deleted successfully", response2.getMessage());

        RequestWrapper<String> requestWrapper3 = new RequestWrapper<>(RequestType.GET_BOOK, book.getItemId());
        ResponseWrapper<?> response3 = requestHandler.handleRequest(requestWrapper3);

        assertEquals(ResponseStatus.ERROR, response3.getResponseStatus());
        assertEquals("Book with ID " + book.getItemId() + " not found", response3.getMessage());
    }

    @Test
    public void testHandleGetAllLoans() throws LoanLimitExceededException, IOException, ItemAlreadyOnLoanException {
        User user1 = new User("User1", UserRole.MEMBER);
        User user2 = new User("User2", UserRole.MEMBER);
        Book book1 = new Book("Book1", "Author1");
        Book book2 = new Book("Book2", "Author2");

        userController.addUser(user1);
        userController.addUser(user2);

        bookController.addBook(book1);
        bookController.addBook(book2);

        loanController.addLoan(user1.getUserId(), book1.getItemId());
        loanController.addLoan(user2.getUserId(), book2.getItemId());

        RequestWrapper<Void> requestWrapper = new RequestWrapper<>(RequestType.GET_ALL_LOANS, null);
        ResponseWrapper<?> response = requestHandler.handleRequest(requestWrapper);

        assertEquals(ResponseStatus.SUCCESS, response.getResponseStatus());
        assertInstanceOf(Map.class, response.getData());
        assertEquals(2, ((Map<?, ?>) response.getData()).size());
    }

    @Test
    public void testHandleGetLoan() throws IOException, LoanLimitExceededException, ItemAlreadyOnLoanException {
        User user = new User("User", UserRole.MEMBER);
        Book book = new Book("Book", "Author");

        userController.addUser(user);

        bookController.addBook(book);

        Loan loan = loanController.addLoan(user.getUserId(), book.getItemId());

        RequestWrapper<String> requestWrapper = new RequestWrapper<>(RequestType.GET_LOAN, loan.getLoanId());
        ResponseWrapper<?> response = requestHandler.handleRequest(requestWrapper);

        assertEquals(ResponseStatus.SUCCESS, response.getResponseStatus());
        assertInstanceOf(Loan.class, response.getData());
        assertEquals(loan.getBook().getItemId(), ((Loan) response.getData()).getBook().getItemId());
    }

    @Test
    public void testHandleGetLoansByUser() throws IOException, LoanLimitExceededException, ItemAlreadyOnLoanException {
        User user1 = new User("User1", UserRole.MEMBER);
        User user2 = new User("User2", UserRole.MEMBER);
        Book book1 = new Book("Book1", "Author1");
        Book book2 = new Book("Book2", "Author2");
        Book book3 = new Book("Book3", "Author3");

        userController.addUser(user1);
        userController.addUser(user2);

        bookController.addBook(book1);
        bookController.addBook(book2);
        bookController.addBook(book3);

        loanController.addLoan(user1.getUserId(), book1.getItemId());
        loanController.addLoan(user1.getUserId(), book2.getItemId());
        loanController.addLoan(user2.getUserId(), book3.getItemId());

        RequestWrapper<String> requestWrapper = new RequestWrapper<>(RequestType.GET_LOANS_BY_USER, user1.getUserId());
        ResponseWrapper<?> response = requestHandler.handleRequest(requestWrapper);

        assertEquals(ResponseStatus.SUCCESS, response.getResponseStatus());
        assertInstanceOf(List.class, response.getData());
        assertEquals(2, ((List<?>) response.getData()).size());
    }

    @Test
    public void testHandleGetOverdueLoansByUser() throws IOException, LoanLimitExceededException,
            ItemAlreadyOnLoanException {
        User user = new User("User", UserRole.MEMBER);
        Book book1 = new Book("Book1", "Author1");
        Book book2 = new Book("Book2", "Author2");

        userController.addUser(user);

        bookController.addBook(book1);
        bookController.addBook(book2);

        Loan loan1 = loanController.addLoan(user.getUserId(), book1.getItemId());

        loanController.addLoan(user.getUserId(), book2.getItemId());

        loan1.setDueDate(LocalDate.now().minusDays(2));

        loanController.updateLoan(loan1);

        RequestWrapper<String> requestWrapper = new RequestWrapper<>(RequestType.GET_OVERDUE_LOANS_BY_USER, user.getUserId());
        ResponseWrapper<?> response = requestHandler.handleRequest(requestWrapper);

        assertEquals(ResponseStatus.SUCCESS, response.getResponseStatus());
        assertInstanceOf(List.class, response.getData());
        assertEquals(1, ((List<?>) response.getData()).size());
    }

    @Test
    public void testHandleAddLoan() throws IOException {
        User user = new User("User", UserRole.MEMBER);
        Book book = new Book("Book", "Author");

        userController.addUser(user);

        bookController.addBook(book);

        String[] loan = {user.getUserId(), book.getItemId()};

        RequestWrapper<String[]> requestWrapper = new RequestWrapper<>(RequestType.ADD_LOAN, loan);
        ResponseWrapper<?> response = requestHandler.handleRequest(requestWrapper);

        assertEquals(ResponseStatus.SUCCESS, response.getResponseStatus());
        assertInstanceOf(Loan.class, response.getData());
        assertEquals(book.getTitle(), ((Loan) response.getData()).getBook().getTitle());
    }

    @Test
    public void testHandleUpdateLoan() throws IOException {
        User user = new User("User", UserRole.MEMBER);
        Book book = new Book("Book", "Author");

        userController.addUser(user);

        bookController.addBook(book);

        String[] loanData = {user.getUserId(), book.getItemId()};

        RequestWrapper<String[]> requestWrapper1 = new RequestWrapper<>(RequestType.ADD_LOAN, loanData);
        ResponseWrapper<?> response1 = requestHandler.handleRequest(requestWrapper1);

        assertEquals(ResponseStatus.SUCCESS, response1.getResponseStatus());
        assertInstanceOf(Loan.class, response1.getData());

        Loan loan = (Loan) response1.getData();

        loan.setReturnDate(LocalDate.now().minusDays(2));

        RequestWrapper<Loan> requestWrapper2 = new RequestWrapper<>(RequestType.UPDATE_LOAN, loan);
        ResponseWrapper<?> response2 = requestHandler.handleRequest(requestWrapper2);

        assertEquals(ResponseStatus.SUCCESS, response2.getResponseStatus());
        assertInstanceOf(Loan.class, response2.getData());
        assertEquals(loan.isOverdue(), ((Loan) response2.getData()).isOverdue());
    }

    @Test
    public void testHandleDeleteLoan() throws IOException {
        User user = new User("User", UserRole.MEMBER);
        Book book = new Book("Book", "Author");

        userController.addUser(user);

        bookController.addBook(book);

        String[] loanData = {user.getUserId(), book.getItemId()};

        RequestWrapper<String[]> requestWrapper1 = new RequestWrapper<>(RequestType.ADD_LOAN, loanData);
        ResponseWrapper<?> response1 = requestHandler.handleRequest(requestWrapper1);

        assertEquals(ResponseStatus.SUCCESS, response1.getResponseStatus());
        assertInstanceOf(Loan.class, response1.getData());

        Loan loan = (Loan) response1.getData();

        RequestWrapper<String> requestWrapper2 = new RequestWrapper<>(RequestType.DELETE_LOAN, loan.getLoanId());
        ResponseWrapper<?> response2 = requestHandler.handleRequest(requestWrapper2);

        assertEquals(ResponseStatus.SUCCESS, response2.getResponseStatus());
        assertEquals("Loan deleted successfully", response2.getMessage());

        RequestWrapper<String> requestWrapper3 = new RequestWrapper<>(RequestType.GET_LOAN, loan.getLoanId());
        ResponseWrapper<?> response3 = requestHandler.handleRequest(requestWrapper3);

        assertEquals(ResponseStatus.ERROR, response3.getResponseStatus());
        assertEquals("Loan with ID " + loan.getLoanId() + " not found", response3.getMessage());
    }
}
