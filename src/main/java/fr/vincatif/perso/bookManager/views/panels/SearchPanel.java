package fr.vincatif.perso.bookManager.views.panels;

import fr.vincatif.perso.bookManager.controllers.LibraryFile;
import fr.vincatif.perso.bookManager.controllers.LibrarySearch;
import fr.vincatif.perso.bookManager.listeners.TextFieldListener;

import javax.swing.*;
import javax.swing.event.EventListenerList;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class SearchPanel extends JPanel {
    private final JTextField searchBar = new JTextField(30);
    private final EventListenerList listenerList = new EventListenerList();

    public SearchPanel(LibraryFile file) {
        JLabel searchLabel = new JLabel("Effectuer une recherche : ");
        searchBar.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                fireChangeTextFieldListener(new LibrarySearch(searchBar.getText(), file.getLibrary()));
            }
        });
        searchBar.addActionListener(e -> fireSubmitTextFieldListener(new LibrarySearch(searchBar.getText(), file.getLibrary())));
        this.add(searchLabel);
        this.add(searchBar);
    }

    public TextFieldListener[] getTextFieldListenerList() {
        return listenerList.getListeners(TextFieldListener.class);
    }

    public void addTextFieldListener(TextFieldListener listener) {
        listenerList.add(TextFieldListener.class, listener);
    }

    protected void fireSubmitTextFieldListener(LibrarySearch result) {
        for (TextFieldListener listener : listenerList.getListeners(TextFieldListener.class)) {
            listener.submit(result);
        }
    }

    protected void fireChangeTextFieldListener(LibrarySearch result) {
        for (TextFieldListener listener : listenerList.getListeners(TextFieldListener.class)) {
            listener.change(result);
        }
    }
}
