package edu.sdccd.cisc191.library.controller;

import edu.sdccd.cisc191.library.model.Book;
import edu.sdccd.cisc191.library.service.BookService;

import java.io.IOException;
import java.util.Map;

public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    public Map<String, Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    public Book getBookById(String bookId) {
        return bookService.getBookById(bookId);
    }

    public Book addBook(Book book) throws IOException {
        return bookService.addBook(book);
    }

    public Book updateBook(Book book) throws IOException {
        return bookService.updateBook(book);
    }

    public void deleteBook(String bookId) throws IOException {
        bookService.deleteBook(bookId);
    }
}