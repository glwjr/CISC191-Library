package edu.sdccd.cisc191.library;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BookTest {
    private Book book;

    @BeforeEach
    void setUp() {
        book = new Book("Test Book", "Test Author");
    }

    @Test
    void testBookCreation() {
        assertNotNull(book.getItemId(), "Book (Item) ID should not be null");
        assertEquals("Test Book", book.getTitle(), "Book title should be 'Test Book'");
        assertEquals("Test Author", book.getAuthor(), "Book author should be 'Test Author'");
    }

    @Test
    void testSetTitle() {
        book.setTitle("New Title");
        assertEquals("New Title", book.getTitle(), "Book title should be 'New Title'");
    }

    @Test
    void testSetAuthor() {
        book.setAuthor("New Author");
        assertEquals("New Author", book.getAuthor(), "Book author should be 'New Author'");
    }
}
