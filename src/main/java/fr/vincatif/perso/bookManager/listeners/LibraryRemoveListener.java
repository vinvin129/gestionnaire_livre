package fr.vincatif.perso.bookManager.listeners;

import java.util.EventListener;

/**
 * The remove listener event interface. When the remove event occurs, that object's removed method is invoked.
 * @author vincent GUILLOU
 */
@FunctionalInterface
public interface LibraryRemoveListener extends EventListener {
    /**
     * invoked when library is removed from file
     */
    void removed(String title, String author);
}
