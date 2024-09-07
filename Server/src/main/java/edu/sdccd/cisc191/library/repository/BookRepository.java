package edu.sdccd.cisc191.library.repository;

import edu.sdccd.cisc191.library.model.Book;

import java.io.IOException;
import java.util.List;

public interface BookRepository {
    List<Book> getAllBooks();
    Book getBookById(String itemId);
    void addBook(Book book) throws IOException;
    void updateBook(Book book) throws IOException;
    void deleteBook(String itemId) throws IOException;
}
