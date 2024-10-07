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

public class Server {
    private ServerSocket serverSocket;
    private RequestHandler requestHandler;

    public void setUp() throws IOException {
        Path dataDirectory = Paths.get("Server", "data");
        File usersFile = new File(dataDirectory.toFile(), "users.json");
        File booksFile = new File(dataDirectory.toFile(), "books.json");
        File loansFile = new File(dataDirectory.toFile(), "loans.json");

        int maxLoansPerUser = 2;

        Files.createDirectories(dataDirectory);

        UserRepository userRepository = new FileUserRepository(usersFile.toPath());
        BookRepository bookRepository = new FileBookRepository(booksFile.toPath());
        LoanRepository loanRepository = new FileLoanRepository(loansFile.toPath());

        UserService userService = new UserService(userRepository);
        BookService bookService = new BookService(bookRepository);
        LoanService loanService = new LoanService(maxLoansPerUser, loanRepository, userRepository, bookRepository);

        UserController userController = new UserController(userService);
        BookController bookController = new BookController(bookService);
        LoanController loanController = new LoanController(loanService);

        requestHandler = new RequestHandler(userController, bookController, loanController);
    }

    public void start() throws IOException {
        int PORT = 8080;
        serverSocket = new ServerSocket(PORT);
        System.out.println("Server started on port " + PORT);

        while (true) {
            try (Socket clientSocket = serverSocket.accept()) {
                ObjectInputStream in  = new ObjectInputStream(clientSocket.getInputStream());
                ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());

                RequestWrapper<?> request = (RequestWrapper<?>) in.readObject();
                ResponseWrapper<?> response = requestHandler.handleRequest(request);
                out.writeObject(response);
            } catch (Exception e) {
                System.out.println("Error handling request: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        try {
            server.setUp();
            server.start();
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }
}
