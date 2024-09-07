package edu.sdccd.cisc191.library.service;

import edu.sdccd.cisc191.library.model.Book;
import edu.sdccd.cisc191.library.repository.BookRepository;

import java.io.IOException;
import java.util.List;

public class BookService {
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getAllBooks() throws IOException {
        return bookRepository.getAllBooks();
    }

    public Book getBookById(String itemId) throws IOException {
        return bookRepository.getBookById(itemId);
    }

    public void addBook(Book book) throws IOException {
        bookRepository.addBook(book);
    }

    public void updateBook(Book book) throws IOException {
        bookRepository.updateBook(book);
    }

    public void deleteBook(String itemId) throws IOException {
        bookRepository.deleteBook(itemId);
    }
}
