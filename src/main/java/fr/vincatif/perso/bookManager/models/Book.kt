package fr.vincatif.perso.bookManager.models

/**
 * @property title book title
 * @property author book author
 * @property copyNumber copy nb
 */
class Book (val title: String, val author: String, copyNumber: Int) {
    private val borrowers: ArrayList<Borrower> = arrayListOf()
    var copyNumber: Int = if (copyNumber > 1) copyNumber else 1
        set(value) {
            if (loanedBookNb <= value) {
                field = value
            }
        }
    /**
     * borrower count
     */
    val borrowersNb
        get() = borrowers.size
    /**
     * loaned book count
     */
    val loanedBookNb: Int
        get() {
            var nb = 0
            borrowers.forEach { nb += it.copyNb }
            return nb
        }

    /**
     * return true if a book can be loaned
     */
    val isAvailable
        get() = copyNumber != loanedBookNb

    /**
     * return a array of borrowers of this Book
     * @return borrower array
     */
    fun getBorrowers(): Array<Borrower> = borrowers.toTypedArray()

    /**
     * obtain borrower with name [name]
     * @param name name of the book
     * @return Borrower or null if doesn't exists
     */
    fun getBorrower(name: String): Borrower? = borrowers.find { it.name == name }

    /**
     * add a new Borrower [borrower] to this book if a Borrower with same name doesn't exists and if the book is available
     * @param borrower the Borrower
     * @return true if the book was add or false else
     */
    fun addBorrower(borrower: Borrower) = if (getBorrower(borrower.name) == null && isAvailable)
        borrowers.add(borrower)
    else
        false

    /**
     * remove Borrower [borrower] of this book
     * @param borrower the Borrower
     * @return true if the Borrower was exists, false else
     */
    fun removeBorrower(borrower: Borrower) = borrowers.remove(borrower)
}