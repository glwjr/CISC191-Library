package edu.sdccd.cisc191.library.service;

import edu.sdccd.cisc191.library.model.Book;
import edu.sdccd.cisc191.library.repository.BookRepository;
import edu.sdccd.cisc191.library.repository.implementations.FileBookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BookServiceTest {
    private BookService bookService;

    @BeforeEach
    public void setUp() throws IOException {
        Path booksTempFile = Files.createTempFile("books", ".json");

        BookRepository bookRepository = new FileBookRepository(booksTempFile);

        bookService = new BookService(bookRepository);
    }

    @Test
    public void testGetAllBooks() throws IOException {
        Book book1 = new Book("Book1", "Author1");
        Book book2 = new Book("Book2", "Author2");

        bookService.addBook(book1);
        bookService.addBook(book2);

        assertEquals(2, bookService.getAllBooks().size());
    }

    @Test
    public void testAddBook() throws IOException {
        Book book = new Book("Book", "Author");

        bookService.addBook(book);

        assertEquals(1, bookService.getAllBooks().size());
        assertEquals(book.getTitle(), bookService.getBookById(book.getItemId()).getTitle());
    }

    @Test
    public void testUpdateBook() throws IOException {
        Book book = new Book("Book", "Author");

        bookService.addBook(book);

        book.setTitle("New Title");

        bookService.updateBook(book);

        assertEquals(book.getTitle(), bookService.getBookById(book.getItemId()).getTitle());
    }

    @Test
    public void testDeleteBook() throws IOException {
        Book book1 = new Book("Book1", "Author1");
        Book book2 = new Book("Book2", "Author2");

        bookService.addBook(book1);
        bookService.addBook(book2);

        assertEquals(2, bookService.getAllBooks().size());

        bookService.deleteBook(book1.getItemId());

        assertEquals(1, bookService.getAllBooks().size());
    }
}
