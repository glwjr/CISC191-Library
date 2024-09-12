package edu.sdccd.cisc191.library;

import edu.sdccd.cisc191.library.controller.BookController;
import edu.sdccd.cisc191.library.controller.LoanController;
import edu.sdccd.cisc191.library.controller.UserController;
import edu.sdccd.cisc191.library.dto.BookDTO;
import edu.sdccd.cisc191.library.dto.LoanDTO;
import edu.sdccd.cisc191.library.dto.UserDTO;
import edu.sdccd.cisc191.library.exceptions.ItemAlreadyOnLoanException;
import edu.sdccd.cisc191.library.exceptions.LoanLimitExceededException;
import edu.sdccd.cisc191.library.message.RequestType;
import edu.sdccd.cisc191.library.message.RequestWrapper;
import edu.sdccd.cisc191.library.message.ResponseStatus;
import edu.sdccd.cisc191.library.message.ResponseWrapper;

import java.io.IOException;
import java.util.List;

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
                    return handleAddUser((UserDTO) payload);
                case UPDATE_USER:
                    return handleUpdateUser((UserDTO) payload);
                case DELETE_USER:
                    return handleDeleteUser((String) payload);
                case GET_ALL_BOOKS:
                    return handleGetAllBooks();
                case GET_BOOK:
                    return handleGetBook((String) payload);
                case ADD_BOOK:
                    return handleAddBook((BookDTO) payload);
                case UPDATE_BOOK:
                    return handleUpdateBook((BookDTO) payload);
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
                    return handleUpdateLoan((LoanDTO) payload);
                case DELETE_LOAN:
                    return handleDeleteLoan((String) payload);
                default:
                    return new ResponseWrapper<>(ResponseStatus.ERROR, "Unknown request type");
            }
        } catch (Exception e) {
            return new ResponseWrapper<>(ResponseStatus.ERROR, e.getMessage());
        }
    }

    private ResponseWrapper<List<UserDTO>> handleGetAllUsers() {
        List<UserDTO> users = userController.getAllUsers();
        return new ResponseWrapper<>(ResponseStatus.SUCCESS, users);
    }

    private ResponseWrapper<UserDTO> handleGetUser(String userId) {
        UserDTO user = userController.getUserById(userId);
        return new ResponseWrapper<>(ResponseStatus.SUCCESS, user);
    }

    private ResponseWrapper<UserDTO> handleAddUser(UserDTO newUser) throws IOException {
        UserDTO user = userController.addUser(newUser);
        return new ResponseWrapper<>(ResponseStatus.SUCCESS, user);
    }

    private ResponseWrapper<UserDTO> handleUpdateUser(UserDTO updatedUser) throws IOException {
        UserDTO user = userController.updateUser(updatedUser);
        return new ResponseWrapper<>(ResponseStatus.SUCCESS, user);
    }

    private ResponseWrapper<String> handleDeleteUser(String userId) throws IOException {
        userController.deleteUser(userId);
        return new ResponseWrapper<>(ResponseStatus.SUCCESS, "User deleted successfully");
    }

    private ResponseWrapper<List<BookDTO>> handleGetAllBooks() {
        List<BookDTO> books = bookController.getAllBooks();
        return new ResponseWrapper<>(ResponseStatus.SUCCESS, books);
    }

    private ResponseWrapper<BookDTO> handleGetBook(String itemId) {
        BookDTO book = bookController.getBookById(itemId);
        return new ResponseWrapper<>(ResponseStatus.SUCCESS, book);
    }

    private ResponseWrapper<BookDTO> handleAddBook(BookDTO newBook) throws IOException {
        BookDTO book = bookController.addBook(newBook);
        return new ResponseWrapper<>(ResponseStatus.SUCCESS, book);
    }

    private ResponseWrapper<BookDTO> handleUpdateBook(BookDTO updatedBook) throws IOException {
        BookDTO book = bookController.updateBook(updatedBook);
        return new ResponseWrapper<>(ResponseStatus.SUCCESS, book);
    }

    private ResponseWrapper<String> handleDeleteBook(String itemId) throws IOException {
        bookController.deleteBook(itemId);
        return new ResponseWrapper<>(ResponseStatus.SUCCESS, "Book deleted successfully");
    }

    private ResponseWrapper<List<LoanDTO>> handleGetAllLoans() {
        List<LoanDTO> loans = loanController.getAllLoans();
        return new ResponseWrapper<>(ResponseStatus.SUCCESS, loans);
    }

    private ResponseWrapper<LoanDTO> handleGetLoan(String loanId) {
        LoanDTO loan = loanController.getLoanById(loanId);
        return new ResponseWrapper<>(ResponseStatus.SUCCESS, loan);
    }

    private ResponseWrapper<List<LoanDTO>> handleGetLoansByUser(String userId) {
        List<LoanDTO> loans = loanController.getLoansByUserId(userId);
        return new ResponseWrapper<>(ResponseStatus.SUCCESS, loans);
    }

    private ResponseWrapper<List<LoanDTO>> handleGetOverdueLoansByUser(String userId) {
        List<LoanDTO> loans = loanController.getOverdueLoansByUserId(userId);
        return new ResponseWrapper<>(ResponseStatus.SUCCESS, loans);
    }

    private ResponseWrapper<LoanDTO> handleAddLoan(String[] loanData) throws LoanLimitExceededException,
            IOException, ItemAlreadyOnLoanException {
        String userId = loanData[0];
        String itemId = loanData[1];
        LoanDTO loan = loanController.addLoan(userId, itemId);
        return new ResponseWrapper<>(ResponseStatus.SUCCESS, loan);
    }

    private ResponseWrapper<LoanDTO> handleUpdateLoan(LoanDTO updatedLoan) throws IOException {
        LoanDTO loan = loanController.updateLoan(updatedLoan);
        return new ResponseWrapper<>(ResponseStatus.SUCCESS, loan);
    }

    private ResponseWrapper<String> handleDeleteLoan(String loanId) throws IOException {
        loanController.deleteLoan(loanId);
        return new ResponseWrapper<>(ResponseStatus.SUCCESS, "Loan deleted successfully");
    }
}
