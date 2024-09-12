package edu.sdccd.cisc191.library.controller;

import edu.sdccd.cisc191.library.dto.BookDTO;
import edu.sdccd.cisc191.library.model.Book;
import edu.sdccd.cisc191.library.service.BookService;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    private BookDTO convertToDTO(Book book) {
        return new BookDTO(book.getTitle(), book.getAuthor());
    }

    private Book convertToBook(BookDTO bookDTO) {
        return new Book(bookDTO.getTitle(), bookDTO.getAuthor());
    }

    public List<BookDTO> getAllBooks() {
        List<Book> books = bookService.getAllBooks();
        return books.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public BookDTO getBookById(String bookId) {
        Book book = bookService.getBookById(bookId);
        return convertToDTO(book);
    }

    public BookDTO addBook(BookDTO newBookDTO) throws IOException {
        Book newBook = convertToBook(newBookDTO);
        Book addedBook = bookService.addBook(newBook);
        return convertToDTO(addedBook);
    }

    public BookDTO updateBook(BookDTO updatedBookDTO) throws IOException {
        Book updatedBook = convertToBook(updatedBookDTO);
        Book savedBook = bookService.updateBook(updatedBook);
        return convertToDTO(savedBook);
    }

    public void deleteBook(String bookId) throws IOException {
        bookService.deleteBook(bookId);
    }
}