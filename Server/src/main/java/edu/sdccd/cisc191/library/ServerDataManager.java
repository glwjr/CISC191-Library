package edu.sdccd.cisc191.library;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ServerDataManager {
    private static final String DATA_DIR = "Server/data/";
    private static final String USERS_FILE = DATA_DIR + "users.json";
    private static final String BOOKS_FILE = DATA_DIR + "books.json";
    private static final String LOANS_FILE = DATA_DIR + "loans.json";

    private final Gson gson = new Gson();

    public ServerDataManager() {
        try {
            Files.createDirectories(Paths.get(DATA_DIR));
            initializeFile(USERS_FILE);
            initializeFile(BOOKS_FILE);
            initializeFile(LOANS_FILE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initializeFile(String filePath) throws IOException {
        if (!Files.exists(Paths.get(filePath))) {
            try (FileWriter writer = new FileWriter(filePath)) {
                writer.write("[]");
            }
        }
    }

    public List<User> loadAllUsers() throws IOException {
        try (FileReader reader = new FileReader(USERS_FILE)) {
            Type userListType = new TypeToken<ArrayList<User>>() {}.getType();
            return gson.fromJson(reader, userListType);
        }
    }

    public void saveAllUsers(List<User> users) throws IOException {
        try (FileWriter writer = new FileWriter(USERS_FILE)) {
            gson.toJson(users, writer);
        }
    }

    public User loadUser(String userId) throws IOException {
        return loadAllUsers().stream()
                .filter(user -> user.getUserId().equals(userId))
                .findFirst()
                .orElse(null);
    }

    public void saveUser(User user) throws IOException {
        List<User> users = loadAllUsers();
        users.removeIf(existingUser -> existingUser.getUserId().equals(user.getUserId()));
        users.add(user);
        saveAllUsers(users);
    }

    public List<Book> loadAllBooks() throws IOException {
        try (FileReader reader = new FileReader(BOOKS_FILE)) {
            Type bookListType = new TypeToken<ArrayList<Book>>() {}.getType();
            return gson.fromJson(reader, bookListType);
        }
    }

    public void saveAllBooks(List<Book> books) throws IOException {
        try (FileWriter writer = new FileWriter(BOOKS_FILE)) {
            gson.toJson(books, writer);
        }
    }

    public Book loadBook(String itemId) throws IOException {
        return loadAllBooks().stream()
                .filter(book -> book.getItemId().equals(itemId))
                .findFirst()
                .orElse(null);
    }

    public void saveBook(Book book) throws IOException {
        List<Book> books = loadAllBooks();
        books.removeIf(existingBook -> existingBook.getItemId().equals(book.getItemId()));
        books.add(book);
        saveAllBooks(books);
    }

    public List<Loan> loadAllLoans() throws IOException {
        try (FileReader reader = new FileReader(LOANS_FILE)) {
            Type loanListType = new TypeToken<ArrayList<Loan>>() {}.getType();
            return gson.fromJson(reader, loanListType);
        }
    }

    public void saveAllLoans(List<Loan> loans) throws IOException {
        try (FileWriter writer = new FileWriter(LOANS_FILE)) {
            gson.toJson(loans, writer);
        }
    }

    public Loan loadLoan(String loanId) throws IOException {
        return loadAllLoans().stream()
                .filter(loan -> loan.getLoanId().equals(loanId))
                .findFirst()
                .orElse(null);
    }

    public void saveLoan(Loan loan) throws IOException {
        List<Loan> loans = loadAllLoans();
        loans.removeIf(existingLoan -> existingLoan.getLoanId().equals(loan.getLoanId()));
        loans.add(loan);
        saveAllLoans(loans);
    }
}