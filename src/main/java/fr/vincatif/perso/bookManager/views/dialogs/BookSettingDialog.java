package fr.vincatif.perso.bookManager.views.dialogs;

import fr.vincatif.perso.bookManager.models.Book;
import fr.vincatif.perso.bookManager.models.Borrower;

import javax.swing.*;
import java.awt.*;

public class BookSettingDialog extends JDialog {
    private Book book;
    private final JTextField titleTextField;
    private final JTextField authorTextField;
    private final JTextField copyNumberTextField;

    public BookSettingDialog(JFrame parent, Book book) {
        super(parent, "Modification des données du livre", true);
        this.book = book;

        titleTextField =  new JTextField(book.getTitle(), 20);
        authorTextField = new JTextField(book.getAuthor(), 20);
        copyNumberTextField = new JTextField(String.valueOf(book.getCopyNumber()), 5);

        JButton buttonValidate = new JButton("Valider");
        buttonValidate.addActionListener(l -> {
            try {
                int newCopyNb = Integer.parseInt(copyNumberTextField.getText());
                if (newCopyNb >= book.getLoanedBookNb() && newCopyNb > 0) {
                    Borrower[] borrowers = book.getBorrowers();
                    this.book = new Book(titleTextField.getText(),
                            authorTextField.getText(),
                            newCopyNb
                    );
                    for (Borrower borrower : borrowers) {
                        this.book.addBorrower(borrower);
                    }
                    this.setVisible(false);
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Le nombre d'exemplaire en votre possession doit être suppérieur ou égal au nombre de livre prêtés",
                            "Avertissement",
                            JOptionPane.WARNING_MESSAGE
                    );
                }
            } catch (NumberFormatException ignored) {
                JOptionPane.showMessageDialog(this,
                        "Vous devez entrer un nombre valide",
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

    public Book getUpdatedBook() {
        return book;
    }

    class CenterPanel extends JPanel {
        public CenterPanel() {
            //this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
            JPanel p1 = new JPanel();
            p1.add(new JLabel("Titre : "));
            p1.add(titleTextField);
            this.add(p1);

            JPanel p2 = new JPanel();
            p2.add(new JLabel("Auteur : "));
            p2.add(authorTextField);
            this.add(p2);

            JPanel p3 = new JPanel();
            p3.add(new JLabel("Nombre d'exemplaire : "));
            p3.add(copyNumberTextField);
            this.add(p3);
        }
    }
}
