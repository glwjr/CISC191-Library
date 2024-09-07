package edu.sdccd.cisc191.library;

import edu.sdccd.cisc191.library.model.Book;
import edu.sdccd.cisc191.library.model.Loan;
import edu.sdccd.cisc191.library.model.User;

import java.io.IOException;
import java.util.List;

/**
 * Program description will go here
 */

//public class Server {
//    public static void main(String[] args) {
//        int maxLoansPerUser = 5;
//        String dataDirectory = "Server/data";
//        LibraryDataStoreManager libraryDataStoreManager = new LibraryDataStoreManager(maxLoansPerUser, dataDirectory);
//
//        try {
//            List<User> users = libraryDataStoreManager.loadAllUsers();
//            List<Book> books = libraryDataStoreManager.loadAllBooks();
//            List<Loan> loans = libraryDataStoreManager.loadAllLoans();
//
//            System.out.println("Server initialized. Loaded: ");
//            System.out.println(users.size() + " users");
//            System.out.println(books.size() + " books");
//            System.out.println(loans.size() + " loans");
//        } catch (IOException e) {
//            System.err.println("Failed to load data: " + e.getMessage());
//        }
//    }
//} //end class Server
