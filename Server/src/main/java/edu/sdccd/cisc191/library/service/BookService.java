package edu.sdccd.cisc191.library.service;

import edu.sdccd.cisc191.library.model.Book;
import edu.sdccd.cisc191.library.repository.BookRepository;

import java.io.IOException;
import java.util.Map;

public class BookService {
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Map<String, Book> getAllBooks() {
        return bookRepository.getAllBooks();
    }

    public Book getBookById(String itemId) {
        return bookRepository.getBookById(itemId);
    }

    public Book addBook(Book book) throws IOException {
        return bookRepository.addBook(book);
    }

    public Book updateBook(Book book) throws IOException {
        return bookRepository.updateBook(book);
    }

    public void deleteBook(String itemId) throws IOException {
        bookRepository.deleteBook(itemId);
    }
}
