package fr.vincatif.perso.bookManager.listeners;

import java.util.EventListener;

@FunctionalInterface
public interface LibraryLoadListener extends EventListener {
    void loaded();
}
