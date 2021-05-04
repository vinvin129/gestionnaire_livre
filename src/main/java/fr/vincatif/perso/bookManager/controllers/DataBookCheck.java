package fr.vincatif.perso.bookManager.controllers;

/**
 * The interface for check data of book before others actions. When the check event occurs, that object's validate method is invoked.
 * @author vincent GUILLOU
 */
@FunctionalInterface
public interface DataBookCheck {
    /**
     * the method that is invoked for check a book
     * @param title title of the book
     * @param author author of the book
     * @param copyNumber number of copy
     * @return true if validated book or false else
     */
    boolean validate(String title, String author, int copyNumber);
}
