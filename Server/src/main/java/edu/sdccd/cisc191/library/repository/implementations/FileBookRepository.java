package edu.sdccd.cisc191.library.repository.implementations;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import edu.sdccd.cisc191.library.exceptions.ItemAlreadyExistsException;
import edu.sdccd.cisc191.library.exceptions.ItemFileException;
import edu.sdccd.cisc191.library.exceptions.ItemNotFoundException;
import edu.sdccd.cisc191.library.model.Book;
import edu.sdccd.cisc191.library.repository.BookRepository;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class FileBookRepository implements BookRepository {
    private final Path filePath;
    private final Gson gson = new Gson();

    public FileBookRepository(Path filePath) {
        this.filePath = filePath;
        initializeFile();
    }

    private void initializeFile() {
        try {
            if (!Files.exists(filePath)) {
                Files.createFile(filePath);
                saveAllBooks(new HashMap<>());
            }
        } catch (IOException e) {
            throw new ItemFileException("Failed to initialize file: " + filePath, e);
        }
    }

    @Override
    public Map<String, Book> getAllBooks() {
        try (FileReader reader = new FileReader(filePath.toFile())) {
            Type bookMapType = new TypeToken<HashMap<String, Book>>() {}.getType();
            HashMap<String, Book> books = gson.fromJson(reader, bookMapType);
            return (books != null ? books : new HashMap<>());
        } catch (IOException e) {
            throw new ItemFileException("Failed to initialize file: " + filePath, e);
        }
    }

    @Override
    public Book getBookById(String itemId) {
        Map<String, Book> books = getAllBooks();
        Book book = books.get(itemId);
        if (book == null) {
            throw new ItemNotFoundException("Book with ID " + itemId + " not found");
        }
        return book;
    }

    @Override
    public Book addBook(Book book) throws IOException {
        Map<String, Book> books = getAllBooks();
        if (books.containsKey(book.getItemId())) {
            throw new ItemAlreadyExistsException("Book with ID " + book.getItemId() + " already exists");
        }
        books.put(book.getItemId(), book);
        saveAllBooks(books);
        return book;
    }

    @Override
    public Book updateBook(Book updatedBook) throws IOException {
        Map<String, Book> books = getAllBooks();
        if (!books.containsKey(updatedBook.getItemId())) {
            throw new ItemNotFoundException("Book with ID " + updatedBook.getItemId() + " not found");
        }
        books.put(updatedBook.getItemId(), updatedBook);
        saveAllBooks(books);
        return updatedBook;
    }

    @Override
    public void deleteBook(String itemId) throws IOException {
        Map<String, Book> books = getAllBooks();
        if (books.remove(itemId) == null) {
            throw new ItemNotFoundException("Book with ID " + itemId + " not found");
        }
        saveAllBooks(books);
    }

    private void saveAllBooks(Map<String, Book> books) throws IOException {
        try (FileWriter writer = new FileWriter(filePath.toFile())) {
            gson.toJson(books, writer);
        } catch (IOException e) {
            throw new ItemFileException("Failed to save books to file: " + filePath, e);
        }
    }
}
