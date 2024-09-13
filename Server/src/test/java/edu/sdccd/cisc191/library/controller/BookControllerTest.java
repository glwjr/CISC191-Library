package edu.sdccd.cisc191.library.controller;

import edu.sdccd.cisc191.library.model.Book;
import edu.sdccd.cisc191.library.repository.BookRepository;
import edu.sdccd.cisc191.library.repository.implementations.FileBookRepository;
import edu.sdccd.cisc191.library.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BookControllerTest {
    private BookController bookController;

    @BeforeEach
    public void setUp() throws IOException {
        Path booksTempFile = Files.createTempFile("books", ".json");

        BookRepository bookRepository = new FileBookRepository(booksTempFile);
        BookService bookService = new BookService(bookRepository);
        bookController = new BookController(bookService);
    }

    @Test
    public void testGetAllBooks() throws IOException {
        Book book1 = new Book("Book1", "Author1");
        Book book2 = new Book("Book2", "Author2");
        Book book3 = new Book("Book3", "Author3");

        bookController.addBook(book1);
        bookController.addBook(book2);
        bookController.addBook(book3);

        Map<String, Book> books = bookController.getAllBooks();
        assertEquals(3, books.size());
    }

    @Test
    public void testGetBookById() throws IOException {
        Book book1 = new Book("Book1", "Author1");

        bookController.addBook(book1);

        assertEquals("Book1", bookController.getBookById(book1.getItemId()).getTitle());
        assertEquals("Author1", bookController.getBookById(book1.getItemId()).getAuthor());
    }

    @Test
    public void testAddBook() throws IOException {
        Book book1 = new Book("Book1", "Author1");
        Book book2 = new Book("Book2", "Author2");

        bookController.addBook(book1);
        bookController.addBook(book2);

        assertEquals("Book1", bookController.getBookById(book1.getItemId()).getTitle());
        assertEquals("Author1", bookController.getBookById(book1.getItemId()).getAuthor());
        assertEquals("Book2", bookController.getBookById(book2.getItemId()).getTitle());
        assertEquals("Author2", bookController.getBookById(book2.getItemId()).getAuthor());
    }

    @Test
    public void testUpdateBook() throws IOException {
        Book book1 = new Book("Book1", "Author1");

        bookController.addBook(book1);

        book1.setTitle("New Title");
        book1.setAuthor("New Author");

        bookController.updateBook(book1);

        assertEquals("New Title", bookController.getBookById(book1.getItemId()).getTitle());
        assertEquals("New Author", bookController.getBookById(book1.getItemId()).getAuthor());
    }

    @Test
    public void testDeleteBook() throws IOException {
        Book book1 = new Book("Book1", "Author1");
        Book book2 = new Book("Book2", "Author2");

        bookController.addBook(book1);
        bookController.addBook(book2);

        bookController.deleteBook(book1.getItemId());

        Map<String, Book> books = bookController.getAllBooks();
        assertEquals(1, books.size());
    }
}
