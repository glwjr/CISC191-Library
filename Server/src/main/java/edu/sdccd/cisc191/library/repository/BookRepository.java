package edu.sdccd.cisc191.library.repository;

import edu.sdccd.cisc191.library.model.Book;

import java.io.IOException;
import java.util.Map;

public interface BookRepository {
    Map<String, Book> getAllBooks();
    Book getBookById(String itemId);
    Book addBook(Book book) throws IOException;
    Book updateBook(Book book) throws IOException;
    void deleteBook(String itemId) throws IOException;
}
