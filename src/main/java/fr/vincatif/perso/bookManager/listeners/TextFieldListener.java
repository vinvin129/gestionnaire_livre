package fr.vincatif.perso.bookManager.listeners;

import fr.vincatif.perso.bookManager.controllers.LibrarySearch;

import java.util.EventListener;

/**
 * The update {@link java.awt.TextField} listener event interface. When the update, submit, or change event occurs, that object's update method is invoked.
 * @author vincent GUILLOU
 */
@FunctionalInterface
public interface TextFieldListener extends EventListener {
    /**
     * invoked on any change with an result of {@link LibrarySearch}
     * @param result the {@link LibrarySearch} of result search
     */
    void update(LibrarySearch result);

    /**
     * invoked on submit depending on the context with an result of {@link LibrarySearch}.<BR>
     *     by default this method invoke update
     * @param result the {@link LibrarySearch} of result search
     * @see TextFieldListener#update(LibrarySearch)
     */
    default void submit(LibrarySearch result) {
        update(result);
    }

    /**
     * invoked on character change with an result of {@link LibrarySearch}.<BR>
     *     By default this method invoke update
     * @param result the {@link LibrarySearch} of result search
     * @see TextFieldListener#update(LibrarySearch)
     */
    default void change(LibrarySearch result) {
        update(result);
    }
}
