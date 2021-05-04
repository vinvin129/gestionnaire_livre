package fr.vincatif.perso.bookManager.models

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class LibraryTest {
    private val book1 = Book("title1", "author1", 1)
    private val book2 = Book("title2", "author2", 5)
    private val book3 = Book("title3", "author1", 8)
    private val book2Bis = Book("title2", "author2", 3)

    private lateinit var books: ArrayList<Book>
    private lateinit var library: Library

    @BeforeEach
    fun setUp() {
        books = arrayListOf()
        library = Library()
    }

    @AfterEach
    fun tearDown() {
        assertArrayEquals(books.toTypedArray(), library.getBooks(), "the get books list method doesn't work")
    }

    @Test
    fun addBook() {
        assertAll("book must be added", {
            assertTrue(library.addBook(book1))
            assertEquals(book1, library.getBook("title1", "author1"))
        })
        books.add(book1)

        assertAll("book must be added", {
            assertTrue(library.addBook(book2))
            assertEquals(book2, library.getBook("title2", "author2"))
        })
        books.add(book2)

        assertAll("book must be added", {
            assertTrue(library.addBook(book3))
            assertEquals(book3, library.getBook("title3", "author1"))
        })
        books.add(book3)

        assertFalse(library.addBook(book1), "book must not be added because he already exists")

        assertFalse(library.addBook(book2Bis), "book must not be added because this title already exists with the same author")
    }

    @Test
    fun clear() {
        library.addBook(book1)
        library.addBook(book2)

        library.clear()
    }

    @Test
    fun removeBook() {
        library.addBook(book1)
        books.add(book1)
        library.addBook(book2)
        books.add(book2)

        assertAll("cannot remove a book", {
            assertTrue(library.removeBook(book1), "remove book method don't return correct value")
            assertNull(library.getBook("title1", "author1"))
        })
        books.remove(book1)

        assertFalse(library.removeBook(book3), "remove book method don't return correct value")
    }

    @Test
    fun getBook() {
        library.addBook(book1)
        books.add(book1)
        library.addBook(book2)
        books.add(book2)

        assertAll("book must be found", {
            assertEquals(book1, library.getBook("title1", "author1"))
            assertEquals(book2, library.getBook("title2", "author2"))
        })

        assertAll("book must not be found", {
            assertNull(library.getBook("title3", "author1"))
            assertNull(library.getBook("title2", "author1"))
            assertNull(library.getBook("title1", "author2"))
        })
    }

    @Test
    fun getBooksByAuthor() {
        library.addBook(book1)
        books.add(book1)
        library.addBook(book2)
        books.add(book2)
        library.addBook(book3)
        books.add(book3)

        assertAll("book list match", {
            assertArrayEquals(arrayOf(book1, book3), library.getBooksByAuthor("author1").toTypedArray())
            assertArrayEquals(arrayOf(book2), library.getBooksByAuthor("author2").toTypedArray())
            assertArrayEquals(arrayOf(), library.getBooksByAuthor("author3").toTypedArray())
        })
    }
}