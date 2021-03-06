package fr.vincatif.perso.bookManager.views.frames;

import fr.vincatif.perso.bookManager.controllers.LibraryFile;
import fr.vincatif.perso.bookManager.models.Book;
import fr.vincatif.perso.bookManager.views.dialogs.BookSettingDialog;
import fr.vincatif.perso.bookManager.views.dialogs.BorrowerAddDialog;
import fr.vincatif.perso.bookManager.views.panels.BookViewPanel;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.io.IOException;

/**
 * {@link JFrame} for show one {@link Book}.<BR>
 *     Several windows of the same book is allowed
 */
public class BookWindow extends JFrame {
    private JPanel panel;
    private Book book;
    private final JMenuItem item1;

    /**
     * create a window for a {@link Book} with a {@link LibraryFile } for save change.
     * @param file the linked {@link LibraryFile}
     * @param book the linked {@link Book}
     */
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
        item1 = new JMenuItem("Ajouter un emprunteur");
        JMenuItem item2 = new JMenuItem("Modifier les données du livre");
        JMenuItem item3 = new JMenuItem("Supprimer le livre");
        editMenu.add(item1);
        editMenu.add(item2);
        editMenu.add(item3);
        menuBar.add(editMenu);

        item1.addActionListener(l -> {
            boolean error = true;
            new BorrowerAddDialog(this, book);

            try {
                if (file.updateBook(book)) {
                    panel = new BookViewPanel(file, book);
                    this.setContentPane(panel);
                    this.revalidate();
                    this.repaint();
                    error = false;
                }
            } catch (TransformerException | IOException | SAXException e) {
                e.printStackTrace();
            }

            if (error) {
                JOptionPane.showMessageDialog(this,
                        "Le livre n'a pas pu être mis à jour.",
                        "Avertissement",
                        JOptionPane.WARNING_MESSAGE
                );
            }
        });

        item2.addActionListener(l -> {
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

        item3.addActionListener(l -> {
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
        this.repaint();
        this.setJMenuBar(menuBar);
        this.setVisible(true);
    }

    /**
     * repaint also the add borrower item
     * @see JComponent#repaint()
     */
    @Override
    public void repaint() {
        this.item1.setEnabled(book.getLoanedBookNb() < book.getCopyNumber());
        super.repaint();
    }
}
