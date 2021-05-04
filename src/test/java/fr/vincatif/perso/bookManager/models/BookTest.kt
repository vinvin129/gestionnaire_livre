package fr.vincatif.perso.bookManager.models

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class BookTest {
    private lateinit var book: Book
    private val beforeTitle = "My book"
    private lateinit var afterTitle: String
    private val beforeAuthor = "The Author"
    private lateinit var afterAuthor: String
    private val beforeCopyNumber = 3
    private var afterCopyNumber: Int = beforeCopyNumber
    private val afterBorrowers = arrayListOf<Borrower>()
    private var afterLoanedNb = 0

    @BeforeEach
    fun setUp() {
        book = Book(beforeTitle, beforeAuthor, beforeCopyNumber)
        afterAuthor = beforeAuthor
        afterCopyNumber = beforeCopyNumber
        afterTitle = beforeTitle
    }

    @AfterEach
    fun tearDown() {
        assertEquals(afterTitle, book.title, "the title accessor method does not work")
        assertEquals(afterAuthor, book.author, "the author accessor method does not work")
        assertEquals(afterCopyNumber, book.copyNumber, "the copyNumber accessor method does not work")
        assertEquals(afterCopyNumber > afterLoanedNb, book.isAvailable, "the available check method does not work")
        assertArrayEquals(afterBorrowers.toTypedArray(), book.getBorrowers(), "the get borrowers list method does not work")
        assertEquals(afterBorrowers.size, book.borrowersNb, "the get borrower number does not work")
        assertEquals(afterLoanedNb, book.loanedBookNb, "the get loaned book number doesn't work")
    }

    @Test
    fun constructor() {
        var bookTest = Book(afterTitle, afterAuthor, 0)
        assertEquals(1, bookTest.copyNumber, "the constructor does not work")

        bookTest = Book(afterTitle, afterAuthor, -3)
        assertEquals(1, bookTest.copyNumber, "the constructor does not work")
    }

    @Test
    fun isAvailable() {
        // 3 books
        assertTrue(book.isAvailable, "the book don't have borrowers, so it must be true")

        val borrower1 = Borrower("test", 3)
        val borrower2 = Borrower("test2", 2)
        val borrower3 = Borrower("test3", 1)

        afterBorrowers.add(borrower1)
        book.addBorrower(borrower1)
        afterLoanedNb+=3
        assertFalse(book.isAvailable, "the book have too borrowers but this method says the opposite")

        afterBorrowers.remove(borrower1)
        book.removeBorrower(borrower1)
        afterLoanedNb-=3

        afterBorrowers.add(borrower2)
        book.addBorrower(borrower2)
        afterLoanedNb+=2
        assertTrue(book.isAvailable, "the book is available but this method says the opposite")

        afterBorrowers.add(borrower3)
        book.addBorrower(borrower3)
        afterLoanedNb++
        assertFalse(book.isAvailable, "the book have too borrowers but this method says the opposite")

        book.addBorrower(borrower1)
        assertFalse(book.isAvailable, "the book have too borrowers but this method says the opposite")
    }

    @Test
    fun setCopyNumber() {
        afterCopyNumber += 10
        book.copyNumber = afterCopyNumber
        val borrower1 = Borrower("test", 1)
        val borrower2 = Borrower("test2", 1)
        book.addBorrower(borrower1)
        book.addBorrower(borrower2)
        afterBorrowers.add(borrower1)
        afterBorrowers.add(borrower2)
        afterLoanedNb += 2
        // 2 loaned books

        book.copyNumber = 1
        assertEquals(afterCopyNumber, book.copyNumber, "the copy number must not be updated")

        book.copyNumber = -1
        assertEquals(afterCopyNumber, book.copyNumber, "the copy number must not be updated")

        book.copyNumber = 0
        assertEquals(afterCopyNumber, book.copyNumber, "the copy number must not be updated")
    }

    @Test
    fun getLoanedBookNb() {
        val borrower1 = Borrower("test", 3)
        val borrower2 = Borrower("test2", 2)
        val borrower3 = Borrower("test3", 1)

        book.addBorrower(borrower1)
        afterLoanedNb += 3
        assertEquals(afterLoanedNb, book.loanedBookNb)

        book.removeBorrower(borrower1)
        afterLoanedNb -= 3

        book.addBorrower(borrower2)
        afterLoanedNb += 2
        assertEquals(afterLoanedNb, book.loanedBookNb)

        book.addBorrower(borrower3)
        afterLoanedNb++
        assertEquals(afterLoanedNb, book.loanedBookNb)

        book.removeBorrower(borrower3)
        afterLoanedNb--
        book.removeBorrower(borrower2)
        afterLoanedNb -= 2
    }

    @Test
    fun getBorrower() {
        val borrower1 = Borrower("test", 1)
        val borrower2 = Borrower("test2", 1)

        book.addBorrower(borrower1)
        afterBorrowers.add(borrower1)
        afterLoanedNb++
        book.addBorrower(borrower2)
        afterBorrowers.add(borrower2)
        afterLoanedNb++

        assertAll("borrower not found", {
            assertEquals(borrower1, book.getBorrower("test"))
            assertEquals(borrower2, book.getBorrower("test2"))
        })
    }

    @Test
    fun addBorrower() {
        val borrower1 = Borrower("test", 3)
        val borrower2 = Borrower("test2", 2)
        val borrower3 = Borrower("test3", 1)
        val borrower3Bis = Borrower("test3", 2)

        assertAll("borrower must be added", {
            assertTrue(book.addBorrower(borrower1))
            assertEquals(borrower1, book.getBorrower("test"))
        })
        afterBorrowers.add(borrower1)
        afterLoanedNb+=3

        assertAll("borrower must not be again added", {
            assertFalse(book.addBorrower(borrower1))
            assertEquals(borrower1, book.getBorrower("test"))
        })

        assertAll("borrower must not be added", {
            assertFalse(book.addBorrower(borrower2))
            assertNull(book.getBorrower("test2"))
        })

        book.removeBorrower(borrower1)
        afterBorrowers.remove(borrower1)
        afterLoanedNb -= 3

        assertAll("borrower must be added", {
            assertTrue(book.addBorrower(borrower3))
            assertEquals(borrower3, book.getBorrower("test3"))
        })
        afterBorrowers.add(borrower3)
        afterLoanedNb+=1

        assertFalse(book.addBorrower(borrower3Bis), "borrower must not be added because this name already exists")
    }

    @Test
    fun removeBorrower() {
        val borrower1 = Borrower("test", 1)
        val borrower2 = Borrower("test2", 1)

        assertFalse(book.removeBorrower(borrower1), "borrower must not be remove because it don't exist")

        book.addBorrower(borrower1)
        afterBorrowers.add(borrower1)
        afterLoanedNb++
        book.addBorrower(borrower2)
        afterBorrowers.add(borrower2)
        afterLoanedNb++

        assertAll("borrower must be remove", {
            assertTrue(book.removeBorrower(borrower1))
            assertNull(book.getBorrower("test1"))
        })
        afterBorrowers.remove(borrower1)
        afterLoanedNb--
    }
}