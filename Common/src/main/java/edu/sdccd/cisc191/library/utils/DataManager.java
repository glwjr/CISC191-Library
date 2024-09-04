package edu.sdccd.cisc191.library.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import edu.sdccd.cisc191.library.Book;
import edu.sdccd.cisc191.library.Loan;
import edu.sdccd.cisc191.library.User;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataManager {

    private final int maxLoansPerUser;
    private final Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter()).create();

    private final Map<String, List<Loan>> loanHistory;
    private final Map<String, User> users;
    private final Map<String, Book> books;

    private final Path dataDirectory;
    private final Path usersFile;
    private final Path booksFile;
    private final Path loansFile;

    public DataManager(int maxLoansPerUser, String dataDirectoryPath) {
        this.maxLoansPerUser = maxLoansPerUser;
        this.loanHistory = new HashMap<>();
        this.users = new HashMap<>();
        this.books = new HashMap<>();

        this.dataDirectory = Paths.get(dataDirectoryPath).toAbsolutePath();
        this.usersFile = dataDirectory.resolve("users.json");
        this.booksFile = dataDirectory.resolve("books.json");
        this.loansFile = dataDirectory.resolve("loans.json");

        initializeData();
    }

    private void initializeData() {
        try {
            Files.createDirectories(dataDirectory);
            initializeFile(usersFile);
            initializeFile(booksFile);
            initializeFile(loansFile);

            loadUsersFromStorage();
            loadBooksFromStorage();
            loadLoansFromStorage();
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize data: ", e);
        }
    }

    private void initializeFile(Path filePath) throws IOException {
        if (!Files.exists(filePath)) {
            try (FileWriter writer = new FileWriter(filePath.toFile())) {
                writer.write("[]");
            }
        }
    }

    private void loadUsersFromStorage() throws IOException {
        List<User> loadedUsers = loadAllUsers();
        for (User user : loadedUsers) {
            users.put(user.getUserId(), user);
        }
    }

    private void loadBooksFromStorage() throws IOException {
        List<Book> loadedBooks = loadAllBooks();
        for (Book book : loadedBooks) {
            books.put(book.getItemId(), book);
        }
    }

    private void loadLoansFromStorage() throws IOException {
        List<Loan> loadedLoans = loadAllLoans();
        for (Loan loan : loadedLoans) {
            String userId = loan.getUserId();
            loanHistory.computeIfAbsent(userId, id -> new ArrayList<>()).add(loan);
            loan.getBook().setOnLoan(true);
        }
    }

    public List<User> loadAllUsers() throws IOException {
        try (FileReader reader = new FileReader(usersFile.toFile())) {
            Type userListType = new TypeToken<ArrayList<User>>() {}.getType();
            return gson.fromJson(reader, userListType);
        }
    }

    public void saveAllUsers(List<User> users) throws IOException {
        try (FileWriter writer = new FileWriter(usersFile.toFile())) {
            gson.toJson(users, writer);
        }
    }

    public User loadUser(String userId) {
        return users.get(userId);
    }

    public void saveUser(User user) throws IOException {
        users.put(user.getUserId(), user);
        saveAllUsers(new ArrayList<>(users.values()));
    }

    public List<Book> loadAllBooks() throws IOException {
        try (FileReader reader = new FileReader(booksFile.toFile())) {
            Type bookListType = new TypeToken<ArrayList<Book>>() {}.getType();
            return gson.fromJson(reader, bookListType);
        }
    }

    public void saveAllBooks(List<Book> books) throws IOException {
        try (FileWriter writer = new FileWriter(booksFile.toFile())) {
            gson.toJson(books, writer);
        }
    }

    public Book loadBook(String itemId) {
        return books.get(itemId);
    }

    public void saveBook(Book book) throws IOException {
        books.put(book.getItemId(), book);
        saveAllBooks(new ArrayList<>(books.values()));
    }

    public List<Loan> loadAllLoans() throws IOException {
        try (FileReader reader = new FileReader(loansFile.toFile())) {
            Type loanListType = new TypeToken<ArrayList<Loan>>() {}.getType();
            return gson.fromJson(reader, loanListType);
        }
    }

    public void saveAllLoans(List<Loan> loans) throws IOException {
        try (FileWriter writer = new FileWriter(loansFile.toFile())) {
            gson.toJson(loans, writer);
        }
    }

    public void saveLoan(Loan loan) throws IOException {
        List<Loan> loans = loanHistory.computeIfAbsent(loan.getUserId(), id -> new ArrayList<>());
        loans.removeIf(existingLoan -> existingLoan.getLoanId().equals(loan.getLoanId()));
        loans.add(loan);
        saveAllLoans(loans);
    }

    public void addLoan(User user, Book book) throws ItemAlreadyOnLoanException, LoanLimitExceededException, IOException {
        String userId = user.getUserId();
        List<Loan> userLoans = loanHistory.computeIfAbsent(userId, id -> new ArrayList<>());

        if (userLoans.size() >= maxLoansPerUser) {
            throw new LoanLimitExceededException("User " + userId + " has reached maximum number of loans per user");
        }

        if (book.isOnLoan()) {
            throw new ItemAlreadyOnLoanException("Book " + book.getTitle() + " is already on loan");
        }

        Loan loan = new Loan(book, userId);
        userLoans.add(loan);

        saveLoan(loan);
    }

    public List<Loan> getUserLoanHistory(String userId) {
        return loanHistory.computeIfAbsent(userId, id -> new ArrayList<>());
    }

    public List<Loan> getUserOverdueLoans(String userId) {
        List<Loan> userLoans = loanHistory.computeIfAbsent(userId, id -> new ArrayList<>());
        List<Loan> overdueLoans = new ArrayList<>();

        for (Loan loan : userLoans) {
            if (loan.isOverdue()) {
                overdueLoans.add(loan);
            }
        }
        return overdueLoans;
    }
}
