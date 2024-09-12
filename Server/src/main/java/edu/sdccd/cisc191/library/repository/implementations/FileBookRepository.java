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
import java.util.ArrayList;
import java.util.List;

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
                saveAllBooks(new ArrayList<>());
            }
        } catch (IOException e) {
            throw new ItemFileException("Failed to initialize file: " + filePath, e);
        }
    }

    @Override
    public List<Book> getAllBooks() {
        try (FileReader reader = new FileReader(filePath.toFile())) {
            Type bookListType = new TypeToken<ArrayList<Book>>() {}.getType();
            List<Book> books = gson.fromJson(reader, bookListType);
            return (books != null ? books : new ArrayList<>());
        } catch (IOException e) {
            throw new ItemFileException("Failed to initialize file: " + filePath, e);
        }
    }

    @Override
    public Book getBookById(String itemId) {
        return getAllBooks().stream()
                .filter(book -> book.getItemId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new ItemNotFoundException("Book with ID " + itemId + " not found"));
    }

    @Override
    public Book addBook(Book book) throws IOException {
        List<Book> books = getAllBooks();
        if (books.stream().anyMatch(b -> b.getItemId().equals(book.getItemId()))) {
            throw new ItemAlreadyExistsException("Book with ID " + book.getItemId() + " already exists");
        }
        books.add(book);
        saveAllBooks(books);
        return book;
    }

    @Override
    public Book updateBook(Book updatedBook) throws IOException {
        List<Book> books = getAllBooks();
        boolean bookFound = false;
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getItemId().equals(updatedBook.getItemId())) {
                books.set(i, updatedBook);
                bookFound = true;
                break;
            }
        }

        if (!bookFound) {
            throw new ItemNotFoundException("Book with ID " + updatedBook.getItemId() + " not found");
        }

        saveAllBooks(books);
        return updatedBook;
    }

    @Override
    public void deleteBook(String itemId) throws IOException {
        List<Book> books = getAllBooks();
        boolean bookRemoved = books.removeIf(book -> book.getItemId().equals(itemId));

        if (!bookRemoved) {
            throw new ItemNotFoundException("Book with ID " + itemId + " not found");
        }

        saveAllBooks(books);
    }

    private void saveAllBooks(List<Book> books) throws IOException {
        try (FileWriter writer = new FileWriter(filePath.toFile())) {
            gson.toJson(books, writer);
        } catch (IOException e) {
            throw new ItemFileException("Failed to save books to file: " + filePath, e);
        }
    }
}
