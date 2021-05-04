package fr.vincatif.perso.bookManager.listeners;

import fr.vincatif.perso.bookManager.controllers.LibrarySearch;

import java.util.EventListener;

@FunctionalInterface
public interface TextFieldListener extends EventListener {

    void update(LibrarySearch result);

    default void submit(LibrarySearch result) {
        update(result);
    }

    default void change(LibrarySearch result) {
        update(result);
    }
}
