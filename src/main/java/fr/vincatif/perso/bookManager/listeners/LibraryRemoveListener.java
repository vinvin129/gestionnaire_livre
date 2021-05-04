package fr.vincatif.perso.bookManager.listeners;

import java.util.EventListener;

@FunctionalInterface
public interface LibraryRemoveListener extends EventListener {
    void removed(String title, String author);
}
