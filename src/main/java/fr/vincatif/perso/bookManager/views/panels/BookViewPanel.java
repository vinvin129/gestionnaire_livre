package fr.vincatif.perso.bookManager.views.panels;

import fr.vincatif.perso.bookManager.controllers.LibraryFile;
import fr.vincatif.perso.bookManager.models.Book;
import fr.vincatif.perso.bookManager.models.Borrower;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.transform.TransformerException;
import java.awt.*;
import java.io.IOException;

/**
 * the {@link JPanel} for {@link fr.vincatif.perso.bookManager.views.frames.BookWindow}
 */
public class BookViewPanel extends JPanel {
    private final Book book;
    private final LibraryFile file;
    private final JPanel mainPanel = this;
    private CenterPanel centerPanel;

    public BookViewPanel(LibraryFile file, Book book) {
        this.book = book;
        this.file = file;
        this.centerPanel = new CenterPanel();
        this.setLayout(new BorderLayout());

        this.add(centerPanel, BorderLayout.CENTER);
    }

    class CenterPanel extends JPanel {
        public CenterPanel() {
            this.setLayout(new BorderLayout());
            JPanel pTop = new JPanel();
            pTop.setLayout(new BoxLayout(pTop, BoxLayout.PAGE_AXIS));
            pTop.add(new JLabel("Titre : " + book.getTitle(), JLabel.LEFT));
            pTop.add(new JLabel("Auteur : " + book.getAuthor()));
            pTop.add(new JLabel("Nombre d'exemplaire : " + book.getCopyNumber()));
            JPanel pBottom = new JPanel();
            pBottom.setLayout(new BoxLayout(pBottom, BoxLayout.PAGE_AXIS));
            if (book.getBorrowersNb() >= 1) {
                pTop.add(new JLabel("Liste des emprunteurs : "));

                for (Borrower borrower : book.getBorrowers()) {
                    JPanel p = new JPanel();
                    JButton bPlus = new JButton("+");
                    JButton bMinus = new JButton("-");

                    if (book.getLoanedBookNb() >= book.getCopyNumber()) {
                        bPlus.setEnabled(false);
                    } else {
                        bPlus.addActionListener(l -> {
                            borrower.setCopyNb(borrower.getCopyNb()+1);
                            actualization();
                        });
                    }

                    bMinus.addActionListener(l -> {
                        borrower.setCopyNb(borrower.getCopyNb()-1);
                        if (borrower.getCopyNb() <= 0) {
                            book.removeBorrower(borrower);
                        }
                        actualization();
                    });

                    p.setLayout(new BoxLayout(p, BoxLayout.LINE_AXIS));
                    p.add(new JLabel(borrower.getName() + " (" + borrower.getCopyNb() + " exemplaires)"));
                    p.add(bPlus);
                    p.add(bMinus);
                    pBottom.add(p);
                }
            }
            this.add(pTop, BorderLayout.NORTH);
            this.add(pBottom, BorderLayout.CENTER);
        }

        /**
         * actualize enable button to change nb of loaned exemplar.
         */
        public void actualization() {
            mainPanel.remove(this);
            centerPanel = new CenterPanel();
            mainPanel.add(centerPanel, BorderLayout.CENTER);
            mainPanel.getParent().getParent().getParent().repaint();
            mainPanel.revalidate();
            mainPanel.repaint();
            try {
                if (!file.updateBook(book)) {
                    JOptionPane.showMessageDialog(this,
                            "Les informations n'ont pas pu être mise à jour",
                            "Avertissement",
                            JOptionPane.WARNING_MESSAGE
                    );
                }
            } catch (IOException | SAXException | TransformerException e) {
                e.printStackTrace();
            }
        }
    }
}
