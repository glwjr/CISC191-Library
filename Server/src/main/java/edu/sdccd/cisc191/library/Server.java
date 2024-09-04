package edu.sdccd.cisc191.library;

import edu.sdccd.cisc191.library.utils.DataManager;

import java.io.IOException;
import java.util.List;

/**
 * Program description will go here
 */

public class Server {
    public static void main(String[] args) {
        int maxLoansPerUser = 5;
        String dataDirectory = "Server/data";
        DataManager dataManager = new DataManager(maxLoansPerUser, dataDirectory);

        try {
            List<User> users = dataManager.loadAllUsers();
            List<Book> books = dataManager.loadAllBooks();
            List<Loan> loans = dataManager.loadAllLoans();

            System.out.println("Server initialized. Loaded: ");
            System.out.println(users.size() + " users");
            System.out.println(books.size() + " books");
            System.out.println(loans.size() + " loans");
        } catch (IOException e) {
            System.err.println("Failed to load data: " + e.getMessage());
        }
    }
} //end class Server
