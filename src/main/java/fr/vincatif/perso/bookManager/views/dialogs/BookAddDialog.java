package fr.vincatif.perso.bookManager.views.dialogs;

import fr.vincatif.perso.bookManager.controllers.DataBookCheck;
import fr.vincatif.perso.bookManager.controllers.LibraryFile;
import fr.vincatif.perso.bookManager.models.Book;

import javax.swing.*;
import java.awt.*;

public class BookAddDialog extends JDialog {
    private final LibraryFile file;
    private Book book = null;
    private final JTextField titleTextField = new JTextField(20);
    private final JTextField authorTextField = new JTextField(20);
    private final JTextField copyNumberTextField = new JTextField(5);

    public BookAddDialog(JFrame parent, LibraryFile file, DataBookCheck check) {
        super(parent, "Ajout d'un nouveau livre", true);
        this.file = file;

        JButton buttonValidate = new JButton("Valider");
        buttonValidate.addActionListener(l -> {
            String title = titleTextField.getText();
            String author = authorTextField.getText();
            int copyNumber;
            try {
                copyNumber = Integer.parseInt(copyNumberTextField.getText());
            } catch (NumberFormatException ignored) {
                copyNumber = 0;
            }
            if (check.validate(title, author, copyNumber)) {
                this.book = new Book(title, author, copyNumber);
                this.setVisible(false);
            } else {
                JOptionPane.showMessageDialog(this,
                        "un des champs est incorrect ou le livre existe déjà " +
                                "(un auteur ne peut pas avoir deux livres du même nom)",
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

    /**
     * return the book at submit
     * @return book or null if found incorrect data
     */
    public Book getBook() {
        return book;
    }

    class CenterPanel extends JPanel {
        public CenterPanel() {
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
