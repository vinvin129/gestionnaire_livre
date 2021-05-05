package fr.vincatif.perso.bookManager.views.frames;

import fr.vincatif.perso.bookManager.controllers.DataBookCheck;
import fr.vincatif.perso.bookManager.controllers.LibraryFile;
import fr.vincatif.perso.bookManager.models.Library;
import fr.vincatif.perso.bookManager.views.dialogs.BookAddDialog;
import fr.vincatif.perso.bookManager.views.panels.ArrayBookPanel;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;

/**
 * Principal {@link JFrame}. This show {@link ArrayBookPanel}
 */
public class MainWindow extends JFrame {
    public static final String PATH = "library.xml";

    private JPanel panel;
    private LibraryFile libraryFile = null;
    private final DataBookCheck bookCheck = (title, author, copyNumber) -> {
        Library library = libraryFile.getLibrary();
        return !title.equals("") && !author.equals("") && copyNumber >= 1 && library.getBook(title, author) == null;
    };

    public MainWindow() {
        try {
            libraryFile = new LibraryFile(PATH);
            this.panel = new ArrayBookPanel(libraryFile);
        } catch (ParserConfigurationException | IOException | SAXException | TransformerException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                    null,
                    "Une erreur fatale avec le fichier de sauvegarde s'est produite.",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        this.setTitle("Gestionnaire des livres");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setBounds(100, 100, dim.width-200, dim.height-200);
        this.setLocationRelativeTo(null);

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Fichier");
        JMenuItem fileMenuNewItem = new JMenuItem("Nouveau livre");
        JMenuItem fileMenuExitItem = new JMenuItem("Quitter");
        fileMenuNewItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));
        fileMenuExitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, KeyEvent.CTRL_DOWN_MASK));
        fileMenuNewItem.addActionListener(e -> {
            boolean error = true;
            BookAddDialog dialog = new BookAddDialog(this, bookCheck);
            try {
                if (libraryFile.addBook(dialog.getBook()))
                    error = false;
            } catch (IOException | TransformerException | SAXException ioException) {
                ioException.printStackTrace();
            }

            if (error) {
                JOptionPane.showMessageDialog(this,
                        "Le livre n'a pas pu être ajouté. Erreur de fichier",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });
        fileMenuExitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(fileMenuNewItem);
        fileMenu.add(fileMenuExitItem);
        menuBar.add(fileMenu);
        this.setJMenuBar(menuBar);
        this.setContentPane(panel);
    }

    public static void main(String[] args) {
        MainWindow mainWindow = new MainWindow();
        mainWindow.setVisible(true);
    }
}
