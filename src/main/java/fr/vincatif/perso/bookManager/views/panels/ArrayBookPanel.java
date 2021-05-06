package fr.vincatif.perso.bookManager.views.panels;

import fr.vincatif.perso.bookManager.controllers.LibraryFile;
import fr.vincatif.perso.bookManager.controllers.LibrarySearch;
import fr.vincatif.perso.bookManager.models.Book;
import fr.vincatif.perso.bookManager.models.BookTableModel;
import fr.vincatif.perso.bookManager.models.Library;
import fr.vincatif.perso.bookManager.views.customComponents.ButtonEditor;
import fr.vincatif.perso.bookManager.views.customComponents.ButtonRenderer;
import fr.vincatif.perso.bookManager.views.customComponents.TableComponent;
import fr.vincatif.perso.bookManager.views.frames.BookWindow;

import javax.swing.*;
import java.awt.*;

/**
 * {@link JPanel} to show {@link Book} in array. You can make search with the {@link SearchPanel} panel.
 */
public class ArrayBookPanel extends JPanel {
    JTable table = null;
    Library library;
    LibraryFile file;
    String textSearch = "";

    public ArrayBookPanel(LibraryFile file) {
        this.file = file;
        this.library = file.getLibrary();
        this.setLayout(new BorderLayout());
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.add(new JLabel("Liste des livres", JLabel.CENTER), BorderLayout.SOUTH);
        SearchPanel searchPanel = new SearchPanel(file);
        searchPanel.addTextFieldListener(r -> {
            textSearch = r.getSearch();
            BookTableModel tableModel = (BookTableModel) table.getModel();
            tableModel.removeRows();
            for (Book b : r.getBooks()) {
                addBookToTable(b);
            }
        });
        northPanel.add(searchPanel, BorderLayout.CENTER);
        this.add(northPanel, BorderLayout.NORTH);
        file.addLibraryLoadListener(() -> {
            LibrarySearch r = new LibrarySearch(textSearch, library);
            BookTableModel tableModel = (BookTableModel) table.getModel();
            tableModel.removeRows();
            for (Book b : r.getBooks()) {
                addBookToTable(b);
            }
        });
        initTable();
    }

    /**
     * initialise the table with all books
     */
    private void initTable() {
        String[] title = {"Titre", "Auteur", "nbr d'exemplaire", "nbr disponible", ""};
        BookTableModel tableModel = new BookTableModel(title);
        table = new JTable(tableModel);
        table.setDefaultRenderer(JComponent.class, new TableComponent());
        table.setRowHeight(30);
        this.table.getColumn("nbr d'exemplaire").setMaxWidth(130);
        this.table.getColumn("nbr d'exemplaire").setPreferredWidth(130);
        this.table.getColumn("nbr disponible").setMaxWidth(130);
        this.table.getColumn("nbr disponible").setPreferredWidth(130);

        for (Book b : library.getBooks()) {
            addBookToTable(b);
        }
        this.table.getColumn("").setCellRenderer(new ButtonRenderer());
        this.table.getColumn("").setCellEditor(new ButtonEditor());
        this.table.getColumn("").setMaxWidth(180);
        this.table.getColumn("").setPreferredWidth(180);
        this.add(new JScrollPane(table), BorderLayout.CENTER);
    }

    /**
     * add a row to the table of book
     * @param b the book
     */
    protected void addBookToTable(Book b) {
        JButton button = new JButton("Ouvrir");
        button.addActionListener(e -> new BookWindow(file, b));
        ((BookTableModel) table.getModel()).addRow(new Object[]{
                b.getTitle(),
                b.getAuthor(),
                b.getCopyNumber(),
                b.getCopyNumber() - b.getLoanedBookNb(),
                button
        });
    }
}
