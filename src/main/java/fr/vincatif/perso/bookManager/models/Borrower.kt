package fr.vincatif.perso.bookManager.models

/**
 * @property name the name of the Borrower
 * @property copyNb the number of copy of the Borrower
 */
data class Borrower(val name: String, var copyNb: Int)