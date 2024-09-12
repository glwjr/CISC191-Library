package edu.sdccd.cisc191.library;

import edu.sdccd.cisc191.library.controller.BookController;
import edu.sdccd.cisc191.library.controller.LoanController;
import edu.sdccd.cisc191.library.controller.UserController;
import edu.sdccd.cisc191.library.message.RequestWrapper;
import edu.sdccd.cisc191.library.message.ResponseWrapper;
import edu.sdccd.cisc191.library.repository.BookRepository;
import edu.sdccd.cisc191.library.repository.LoanRepository;
import edu.sdccd.cisc191.library.repository.UserRepository;
import edu.sdccd.cisc191.library.repository.implementations.FileBookRepository;
import edu.sdccd.cisc191.library.repository.implementations.FileLoanRepository;
import edu.sdccd.cisc191.library.repository.implementations.FileUserRepository;
import edu.sdccd.cisc191.library.service.BookService;
import edu.sdccd.cisc191.library.service.LoanService;
import edu.sdccd.cisc191.library.service.UserService;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Program description will go here
 */

public class Server {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private UserController userController;
    private BookController bookController;
    private LoanController loanController;
    private RequestHandler requestHandler;

    public void setUp() throws IOException {
        Path dataDirectory = Paths.get("Server", "data");
        File usersFile = new File(dataDirectory.toFile(), "users.json");
        File booksFile = new File(dataDirectory.toFile(), "books.json");
        File loansFile = new File(dataDirectory.toFile(), "loans.json");
        int maxLoansPerUser = 3;

        Files.createDirectories(dataDirectory);

        UserRepository userRepository = new FileUserRepository(usersFile.toPath());
        BookRepository bookRepository = new FileBookRepository(booksFile.toPath());
        LoanRepository loanRepository = new FileLoanRepository(loansFile.toPath());

        UserService userService = new UserService(userRepository);
        BookService bookService = new BookService(bookRepository);
        LoanService loanService = new LoanService(maxLoansPerUser, loanRepository, userRepository, bookRepository);

        userController = new UserController(userService);
        bookController = new BookController(bookService);
        loanController = new LoanController(loanService);
        requestHandler = new RequestHandler(userController, bookController, loanController);
    }

    public void start() throws IOException {
        int PORT = 3000;
        serverSocket = new ServerSocket(PORT);
        System.out.println("Server started on port " + PORT);
    }

    public void stop() throws IOException {
        clientSocket.close();
        serverSocket.close();
    }

    public static void main(String[] args) {
        Server server = new Server();
        try {
            server.setUp();
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }
}
