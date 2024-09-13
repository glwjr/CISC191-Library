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

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class RequestHandler {
    private final UserController userController;
    private final BookController bookController;
    private final LoanController loanController;

    public RequestHandler(UserController userController, BookController bookController, LoanController loanController) {
        this.userController = userController;
        this.bookController = bookController;
        this.loanController = loanController;
    }

    public ResponseWrapper<?> handleRequest(RequestWrapper<?> requestWrapper) {
        try {
            RequestType requestType = requestWrapper.getRequestType();
            Object payload = requestWrapper.getPayload();

            switch (requestType) {
                case GET_ALL_USERS:
                    return handleGetAllUsers();
                case GET_USER:
                    return handleGetUser((String) payload);
                case ADD_USER:
                    return handleAddUser((User) payload);
                case UPDATE_USER:
                    return handleUpdateUser((User) payload);
                case DELETE_USER:
                    return handleDeleteUser((String) payload);
                case GET_ALL_BOOKS:
                    return handleGetAllBooks();
                case GET_BOOK:
                    return handleGetBook((String) payload);
                case ADD_BOOK:
                    return handleAddBook((Book) payload);
                case UPDATE_BOOK:
                    return handleUpdateBook((Book) payload);
                case DELETE_BOOK:
                    return handleDeleteBook((String) payload);
                case GET_ALL_LOANS:
                    return handleGetAllLoans();
                case GET_LOAN:
                    return handleGetLoan((String) payload);
                case GET_LOANS_BY_USER:
                    return handleGetLoansByUser((String) payload);
                case GET_OVERDUE_LOANS_BY_USER:
                    return handleGetOverdueLoansByUser((String) payload);
                case ADD_LOAN:
                    return handleAddLoan((String[]) payload);
                case UPDATE_LOAN:
                    return handleUpdateLoan((Loan) payload);
                case DELETE_LOAN:
                    return handleDeleteLoan((String) payload);
                default:
                    return new ResponseWrapper<>(ResponseStatus.ERROR, "Unknown request type");
            }
        } catch (Exception e) {
            return new ResponseWrapper<>(ResponseStatus.ERROR, e.getMessage());
        }
    }

    private ResponseWrapper<Map<String, User>> handleGetAllUsers() {
        Map<String, User> users = userController.getAllUsers();
        return new ResponseWrapper<>(ResponseStatus.SUCCESS, users);
    }

    private ResponseWrapper<User> handleGetUser(String userId) {
        User user = userController.getUserById(userId);
        return new ResponseWrapper<>(ResponseStatus.SUCCESS, user);
    }

    private ResponseWrapper<User> handleAddUser(User newUser) throws IOException {
        User user = userController.addUser(newUser);
        return new ResponseWrapper<>(ResponseStatus.SUCCESS, user);
    }

    private ResponseWrapper<User> handleUpdateUser(User updatedUser) throws IOException {
        User user = userController.updateUser(updatedUser);
        return new ResponseWrapper<>(ResponseStatus.SUCCESS, user);
    }

    private ResponseWrapper<String> handleDeleteUser(String userId) throws IOException {
        userController.deleteUser(userId);
        return new ResponseWrapper<>(ResponseStatus.SUCCESS, "User deleted successfully");
    }

    private ResponseWrapper<Map<String, Book>> handleGetAllBooks() {
        Map<String, Book> books = bookController.getAllBooks();
        return new ResponseWrapper<>(ResponseStatus.SUCCESS, books);
    }

    private ResponseWrapper<Book> handleGetBook(String itemId) {
        Book book = bookController.getBookById(itemId);
        return new ResponseWrapper<>(ResponseStatus.SUCCESS, book);
    }

    private ResponseWrapper<Book> handleAddBook(Book book) throws IOException {
        Book newBook = bookController.addBook(book);
        return new ResponseWrapper<>(ResponseStatus.SUCCESS, newBook);
    }

    private ResponseWrapper<Book> handleUpdateBook(Book book) throws IOException {
        Book updatedBook = bookController.updateBook(book);
        return new ResponseWrapper<>(ResponseStatus.SUCCESS, updatedBook);
    }

    private ResponseWrapper<String> handleDeleteBook(String itemId) throws IOException {
        bookController.deleteBook(itemId);
        return new ResponseWrapper<>(ResponseStatus.SUCCESS, "Book deleted successfully");
    }

    private ResponseWrapper<Map<String, Loan>> handleGetAllLoans() {
        Map<String, Loan> loans = loanController.getAllLoans();
        return new ResponseWrapper<>(ResponseStatus.SUCCESS, loans);
    }

    private ResponseWrapper<Loan> handleGetLoan(String loanId) {
        Loan loan = loanController.getLoanById(loanId);
        return new ResponseWrapper<>(ResponseStatus.SUCCESS, loan);
    }

    private ResponseWrapper<List<Loan>> handleGetLoansByUser(String userId) {
        List<Loan> loans = loanController.getLoansByUserId(userId);
        return new ResponseWrapper<>(ResponseStatus.SUCCESS, loans);
    }

    private ResponseWrapper<List<Loan>> handleGetOverdueLoansByUser(String userId) {
        List<Loan> loans = loanController.getOverdueLoansByUserId(userId);
        return new ResponseWrapper<>(ResponseStatus.SUCCESS, loans);
    }

    private ResponseWrapper<Loan> handleAddLoan(String[] loanData) throws LoanLimitExceededException,
            IOException, ItemAlreadyOnLoanException {
        String userId = loanData[0];
        String itemId = loanData[1];
        Loan loan = loanController.addLoan(userId, itemId);
        return new ResponseWrapper<>(ResponseStatus.SUCCESS, loan);
    }

    private ResponseWrapper<Loan> handleUpdateLoan(Loan loan) throws IOException {
        Loan updatedLoan = loanController.updateLoan(loan);
        return new ResponseWrapper<>(ResponseStatus.SUCCESS, updatedLoan);
    }

    private ResponseWrapper<String> handleDeleteLoan(String loanId) throws IOException {
        loanController.deleteLoan(loanId);
        return new ResponseWrapper<>(ResponseStatus.SUCCESS, "Loan deleted successfully");
    }
}
