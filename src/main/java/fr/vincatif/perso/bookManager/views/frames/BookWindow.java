package fr.vincatif.perso.bookManager.views.frames;

import fr.vincatif.perso.bookManager.controllers.LibraryFile;
import fr.vincatif.perso.bookManager.models.Book;
import fr.vincatif.perso.bookManager.views.dialogs.BookSettingDialog;
import fr.vincatif.perso.bookManager.views.panels.BookViewPanel;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class BookWindow extends JFrame {
    private JPanel panel;
    private Book book;

    public BookWindow(LibraryFile file, Book book) {
        this.book = book;
        panel = new BookViewPanel(file, book);
        this.setTitle(book.getTitle() + " de " + book.getAuthor());
        this.setSize(500, 500);
        this.setContentPane(panel);
        this.setLocationRelativeTo(null);
        this.setMinimumSize(new Dimension(400, 300));
        file.addLibraryRemoveListener((title, author) -> {
            if (book.getTitle().equals(title) && book.getAuthor().equals(author)) {
                this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
            }
        });

        JMenuBar menuBar = new JMenuBar();
        JMenu editMenu = new JMenu("Edition");
        JMenuItem item1 = new JMenuItem("Modifier les données du livre");
        JMenuItem item2 = new JMenuItem("Supprimer le livre");
        editMenu.add(item1);
        editMenu.add(item2);
        menuBar.add(editMenu);

        item1.addActionListener(l -> {
            BookSettingDialog dialog = new BookSettingDialog(this, this.book);
            Book bookUpdated = dialog.getUpdatedBook();
            try {
                if (file.updateBook(this.book, bookUpdated)) {
                    this.book = bookUpdated;
                    panel = new BookViewPanel(file, bookUpdated);
                    this.setContentPane(panel);
                    this.revalidate();
                    this.repaint();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Le livre n'a pas pu être mis à jour.",
                            "Avertissement",
                            JOptionPane.WARNING_MESSAGE
                    );
                }
            } catch (ParserConfigurationException | TransformerException | SAXException | IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(
                        null,
                        "Une erreur fatale avec le fichier de sauvegarde s'est produite.",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        });

        item2.addActionListener(l -> {
            int option = JOptionPane.showConfirmDialog(this,
                    "Vous êtes sur le point de supprimer définitvement" +
                    " ce livre. Etes vous sûr de vouloir continuer ?",
                    "Supprimer le livre", JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.WARNING_MESSAGE);
            if (option == JOptionPane.YES_OPTION) {
                boolean error = true;
                try {
                    if (file.removeBook(book)) {
                        error = false;
                    }
                } catch (IOException | TransformerException | SAXException e) {
                    e.printStackTrace();
                }
                if (error) {
                    JOptionPane.showMessageDialog(this,
                            "Le livre n'a pas pu être supprimé.",
                            "Erreur",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });

        this.setJMenuBar(menuBar);
        this.setVisible(true);
    }
}
