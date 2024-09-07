package edu.sdccd.cisc191.library.repository.implementations;

import edu.sdccd.cisc191.library.exceptions.ItemAlreadyExistsException;
import edu.sdccd.cisc191.library.exceptions.ItemNotFoundException;
import edu.sdccd.cisc191.library.model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class FileBookRepositoryTest {
    private FileBookRepository bookRepository;

    @BeforeEach
    public void setUp() throws IOException {
        Path tempFile = Files.createTempFile("books", ".json");
        bookRepository = new FileBookRepository(tempFile);
    }

    @Test
    public void testAddBook() throws IOException {
        Book book = new Book("Book", "Author");

        bookRepository.addBook(book);

        Book retrievedBook = bookRepository.getBookById(book.getItemId());

        assertNotNull(retrievedBook);
        assertEquals(book.getItemId(), retrievedBook.getItemId());
        assertThrows(ItemAlreadyExistsException.class, () -> bookRepository.addBook(book));
    }

    @Test
    public void testGetAllBooks() throws IOException {
        Book book1 = new Book("Book1", "Author1");
        Book book2 = new Book("Book2", "Author2");

        bookRepository.addBook(book1);
        bookRepository.addBook(book2);

        assertEquals(2, bookRepository.getAllBooks().size());
    }

    @Test
    public void testUpdateBook() throws IOException {
        Book book = new Book("Book", "Author");

        bookRepository.addBook(book);

        book.setTitle("New Title");

        bookRepository.updateBook(book);

        Book updatedBook = bookRepository.getBookById(book.getItemId());
        assertEquals("New Title", updatedBook.getTitle());
    }

    @Test
    public void testDeleteBook() throws IOException {
        Book book1 = new Book("Book1", "Author1");
        Book book2 = new Book("Book2", "Author2");

        bookRepository.addBook(book1);
        bookRepository.addBook(book2);

        assertEquals(2, bookRepository.getAllBooks().size());

        bookRepository.deleteBook(book1.getItemId());

        assertThrows(ItemNotFoundException.class, () -> bookRepository.getBookById(book1.getItemId()));
        assertEquals(1, bookRepository.getAllBooks().size());
    }
}
