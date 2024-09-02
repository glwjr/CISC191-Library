package edu.sdccd.cisc191.library;

import java.io.IOException;
import java.util.List;

/**
 * Program description will go here
 */

public class Server {
    private static ServerDataManager serverDataManager;

    public static void main(String[] args) {
        serverDataManager = new ServerDataManager();

        try {
            List<User> users = serverDataManager.loadAllUsers();
            List<Book> books = serverDataManager.loadAllBooks();
            List<Loan> loans = serverDataManager.loadAllLoans();

            System.out.println("Server initialized. Loaded: ");
            System.out.println(users.size() + " users");
            System.out.println(books.size() + " books");
            System.out.println(loans.size() + " loans");
        } catch (IOException e) {
            System.err.println("Failed to load data: " + e.getMessage());
        }
    }
} //end class Server
