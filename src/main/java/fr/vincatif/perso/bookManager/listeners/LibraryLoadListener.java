package fr.vincatif.perso.bookManager.listeners;

import java.util.EventListener;

/**
 * The load listener event interface. When the load event occurs, that object's loaded method is invoked.
 * @author vincent GUILLOU
 */
@FunctionalInterface
public interface LibraryLoadListener extends EventListener {
    /**
     * invoked when library is loaded from file
     */
    void loaded();
}
