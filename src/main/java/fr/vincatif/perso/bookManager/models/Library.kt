package fr.vincatif.perso.bookManager.models

open class Library {
    private val books = arrayListOf<Book>()

    /**
     * give all books
     * @return array of book
     */
    fun getBooks(): Array<Book> = books.toTypedArray()

    /**
     * add [book] to this Library if it's doesn't exist
     * @param book the book
     * @return true if the book was added or false else
     */
    fun addBook(book: Book) =
        if (getBook(book.title, book.author) == null)
            books.add(book)
        else
            false

    /**
     * clear the book list
     */
    fun clear() = books.clear()

    /**
     * remove [book] and return true if this list contained the specified element
     * @param book the book
     * @return true if the was existed, false else
     */
    fun removeBook(book: Book): Boolean = books.remove(book)

    /**
     * give a book by this [title] and this [author]
     * @param author author of the book
     * @param title title of the book
     * @return the book or null if it's don't exist
     */
    fun getBook(title: String, author: String): Book? {
        books.forEach {
            if (it.title == title && it.author == author)
                return it
        }
        return null
    }

    /**
     * give all books by the author [author]
     * @param author the author
     * @return List of matched books
     */
    fun getBooksByAuthor(author: String): List<Book> {
        return books.filter {
            it.author == author
        }
    }
}