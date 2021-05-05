package fr.vincatif.perso.bookManager.views.dialogs;

import fr.vincatif.perso.bookManager.models.Book;
import fr.vincatif.perso.bookManager.models.Borrower;

import javax.swing.*;
import java.awt.*;

/**
 * {@link JDialog} for add a {@link Borrower} in a {@link Book}. <BR>
 *     The {@link Borrower} is added in linked {@link Book}
 */
public class BorrowerAddDialog extends JDialog {
    private final Book book;
    private final JTextField nameTextField;
    private int copyNb = 1;

    /**
     * create the dialog with parent and book
     * @param parent the parent of dialog
     * @param book the book to add {@link Borrower}
     */
    public BorrowerAddDialog(JFrame parent, Book book) {
        super(parent, "Ajout d'un emprunteur", true);
        this.book = book;

        this.nameTextField = new JTextField(20);

        JButton buttonValidate = new JButton("Ajouter");
        buttonValidate.addActionListener(l -> {
            String name = nameTextField.getText();
            if (book.getBorrower(name) == null) {
                book.addBorrower(new Borrower(name, copyNb));
                this.setVisible(false);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Le nom entré existe déjà",
                        "Avertissement",
                        JOptionPane.WARNING_MESSAGE
                );
            }
        });

        this.setLayout(new BorderLayout());
        this.setSize(300, 300);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.add(new CenterPanel(), BorderLayout.CENTER);
        this.add(buttonValidate, BorderLayout.SOUTH);
        this.setVisible(true);
    }

    class CenterPanel extends JPanel {
        private final JButton bPlus = new JButton("+");
        private final JButton bMinus = new JButton("-");
        private final JLabel nbLabel = new JLabel(String.valueOf(copyNb));

        public CenterPanel() {
            //this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
            JPanel p1 = new JPanel();
            p1.add(new JLabel("Nom : "));
            p1.add(nameTextField);
            this.add(p1);

            actualization();

            bPlus.addActionListener(l -> {
                copyNb++;
                actualization();
            });

            bMinus.addActionListener(l -> {
                copyNb--;
                actualization();
            });

            JPanel p2 = new JPanel(new BorderLayout());
            //p2.setLayout(new BoxLayout(p2, BoxLayout.LINE_AXIS));
            p2.add(bPlus, BorderLayout.WEST);
            p2.add(nbLabel, BorderLayout.CENTER);
            p2.add(bMinus, BorderLayout.EAST);
            this.add(p2);
        }

        /**
         * actualize enable button to change nb of loaned exemplar.
         */
        private void actualization() {
            nbLabel.setText(String.valueOf(copyNb));
            bPlus.setEnabled((copyNb+1) <= book.getCopyNumber()-book.getLoanedBookNb());
            bMinus.setEnabled((copyNb-1) >= 1);
            this.revalidate();
            this.repaint();
        }
    }
}
