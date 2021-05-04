package fr.vincatif.perso.bookManager.views.panels;

import fr.vincatif.perso.bookManager.controllers.LibraryFile;
import fr.vincatif.perso.bookManager.models.Book;
import fr.vincatif.perso.bookManager.models.Borrower;
import fr.vincatif.perso.bookManager.views.dialogs.BorrowerAddDialog;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.transform.TransformerException;
import java.awt.*;
import java.io.IOException;

public class BookViewPanel extends JPanel {
    private final Book book;
    private final LibraryFile file;
    private final JButton borrowButton;
    private final JPanel mainPanel = this;
    private CenterPanel centerPanel;
    public BookViewPanel(LibraryFile file, Book book) {
        this.book = book;
        this.file = file;
        borrowButton = new JButton("Ajouter un emprunteur");
        this.centerPanel = new CenterPanel();
        this.setLayout(new BorderLayout());
        borrowButton.addActionListener(l -> {
            boolean error = true;
            new BorrowerAddDialog((JFrame)this.getParent().getParent().getParent(), book);

            try {
                if (file.updateBook(book)) {
                    this.centerPanel.actualization();
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

        this.add(centerPanel, BorderLayout.CENTER);
        this.add(new SouthPanel(), BorderLayout.SOUTH);
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

        public void actualization() {
            mainPanel.remove(this);
            centerPanel = new CenterPanel();
            mainPanel.add(centerPanel, BorderLayout.CENTER);
            borrowButton.setEnabled(book.getLoanedBookNb() < book.getCopyNumber());
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

    class SouthPanel extends JPanel {
        public SouthPanel() {
            borrowButton.setEnabled(book.getLoanedBookNb() < book.getCopyNumber());
            this.add(borrowButton);
        }
    }
}
