package fr.vincatif.perso.bookManager.controllers;

import fr.vincatif.perso.bookManager.listeners.LibraryLoadListener;
import fr.vincatif.perso.bookManager.listeners.LibraryRemoveListener;
import fr.vincatif.perso.bookManager.models.Book;
import fr.vincatif.perso.bookManager.models.Borrower;
import fr.vincatif.perso.bookManager.models.Library;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.swing.event.EventListenerList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;

/**
 * the library file controller. This is used to update and read the xml file of library data.<BR>
 *     This generate a {@link Library} when instance was create. And updated when load method was invoked.
 * @author Vincent GUILLOU
 * @version 1.0.0
 */
public class LibraryFile {
    /**
     * version of file struct
     */
    public static final String VERSION = "1.0.0";

    private final EventListenerList listenerList = new EventListenerList();
    private final String path;
    private final Library library;
    private final File inputFile;
    private final DocumentBuilderFactory dbFactory =
            DocumentBuilderFactory.newInstance();
    private final DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
    private Document document;

    /**
     * create a new reader and writer on that path
     * @param path path of the library xml file
     * @throws ParserConfigurationException invoked if xml parsing fall
     * @throws IOException invoked if an error with file was occurred
     * @throws SAXException invoked if an error with SAX
     * @throws TransformerException invoked if xml transformation fail
     */
    public LibraryFile(String path)
            throws ParserConfigurationException, IOException, SAXException, TransformerException {
        this.path = path;
        this.library = new Library();
        inputFile = new File(path);
        load();
    }

    /**
     * initialize the struct of the file. this is call when the file doesn't exists
     * @throws TransformerException invoked if xml transformation fail
     */
    protected void init() throws TransformerException {

        document = dBuilder.newDocument();

        // root element
        Element rootElement = document.createElement("library");
        Element versionElement = document.createElement("version");
        versionElement.setTextContent(VERSION);
        rootElement.appendChild(versionElement);
        document.appendChild(rootElement);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(inputFile);
        transformer.transform(source, result);
    }

    /**
     * load data from file, update this {@link Library} object and invoke all {@link LibraryLoadListener} event.<BR>
     *     If file don't exist he will create
     * @see LibraryFile#init()
     * @throws IOException invoked if an error with file was occurred
     * @throws SAXException invoked if an error with SAX
     * @throws TransformerException invoked if xml transformation fail
     */
    protected void load() throws IOException, SAXException, TransformerException {
        library.clear();
        if (inputFile.createNewFile()) {
            init();
        }
        document = dBuilder.parse(inputFile);
        document.getDocumentElement().normalize(); //TODO maybe update that line
        NodeList nBooks = document.getElementsByTagName("book");
        for (int i = 0; i < nBooks.getLength(); i++) {
            String title, author;
            int copyNb;
            Element element = (Element) nBooks.item(i);
            title = element.getElementsByTagName("title").item(0).getTextContent();
            author = element.getElementsByTagName("author").item(0).getTextContent();
            copyNb = Integer.parseInt(element.getElementsByTagName("copyNb").item(0).getTextContent());
            Book book = new Book(title, author, copyNb);
            NodeList borrowers = element.getElementsByTagName("borrowers");
            if (borrowers.getLength() == 1) {
                NodeList nBorrowers = ((Element) borrowers.item(0)).getElementsByTagName("borrower");
                for (int j = 0; j < nBorrowers.getLength(); j++) {
                    Element borrower = (Element) nBorrowers.item(j);
                    String nameB = borrower.getElementsByTagName("name").item(0).getTextContent();
                    int copyNbB = Integer.parseInt(borrower.getElementsByTagName("copyNb").item(0).getTextContent());
                    book.addBorrower(new Borrower(nameB, copyNbB));
                }
            }
            library.addBook(book);
        }
        fireLibraryLoadListener();
    }

    /**
     * replace a book only if they titles and authors are different. this invoke updateBook(Book)
     * @param old old book
     * @param actual new book
     * @return true if was updated or false else
     * @see LibraryFile#updateBook(Book) 
     * @throws ParserConfigurationException invoked if xml parsing fall
     * @throws IOException invoked if an error with file was occurred
     * @throws SAXException invoked if an error with SAX
     * @throws TransformerException invoked if xml transformation fail
     */
    public boolean updateBook(Book old, Book actual)
            throws ParserConfigurationException, IOException, SAXException, TransformerException {
        Element element = getElementBook(old.getTitle(), old.getAuthor());
        Element element1 = getElementBook(actual.getTitle(), actual.getAuthor());
        if (element != element1) {
            if (element != null && element1 == null) {
                Node title = element.getElementsByTagName("title").item(0);
                Node author = element.getElementsByTagName("author").item(0);
                title.setTextContent(actual.getTitle());
                author.setTextContent(actual.getAuthor());
            } else {
                return false;
            }
        }

        return updateBook(actual);
    }

    /**
     * update all data of the book in file
     * @param book the book
     * @return true if was updated or false else
     * @throws IOException invoked if an error with file was occurred
     * @throws SAXException invoked if an error with SAX
     * @throws TransformerException invoked if xml transformation fail
     */
    public boolean updateBook(Book book) throws TransformerException, IOException, SAXException {
        if (book.getLoanedBookNb() > book.getCopyNumber()) {
            return false;
        }

        Element element = getElementBook(book.getTitle(), book.getAuthor());
        if (element != null) {
            Node copyNumber = element.getElementsByTagName("copyNb").item(0);
            Element borrowers = document.createElement("borrowers");

            copyNumber.setTextContent(String.valueOf(book.getCopyNumber()));
            for (Borrower borrower : book.getBorrowers()) {
                Element nBorrower = document.createElement("borrower");

                Element name = document.createElement("name");
                Element copyNb = document.createElement("copyNb");
                name.setTextContent(borrower.getName());
                copyNb.setTextContent(String.valueOf(borrower.getCopyNb()));
                nBorrower.appendChild(name);
                nBorrower.appendChild(copyNb);

                borrowers.appendChild(nBorrower);
            }
            Node oldBorrowersNode = element.getElementsByTagName("borrowers").item(0);
            if (oldBorrowersNode == null) {
                element.appendChild(borrowers);
            } else {
                element.replaceChild(borrowers, oldBorrowersNode);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(inputFile);
            transformer.transform(source, result);

            load();
            return true;
        }
        return false;
    }

    /**
     * add a book in the file
     * @param book the book
     * @return true if was added or false else
     * @throws IOException invoked if an error with file was occurred
     * @throws SAXException invoked if an error with SAX
     * @throws TransformerException invoked if xml transformation fail
     */
    public boolean addBook(Book book) throws IOException, TransformerException, SAXException {
        if (book != null && getElementBook(book.getTitle(), book.getAuthor()) == null && book.getCopyNumber() >= 1) {
            Element bookElt = document.createElement("book");
            Element titleElt = document.createElement("title");
            Element authorElt = document.createElement("author");
            Element copyNbElt = document.createElement("copyNb");

            titleElt.setTextContent(book.getTitle());
            authorElt.setTextContent(book.getAuthor());
            copyNbElt.setTextContent(String.valueOf(book.getCopyNumber()));
            bookElt.appendChild(titleElt);
            bookElt.appendChild(authorElt);
            bookElt.appendChild(copyNbElt);
            document.getDocumentElement().appendChild(bookElt);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(inputFile);
            transformer.transform(source, result);

            load();
            return true;
        }
        return false;
    }

    /**
     * remove a book of file
     * @param book the book
     * @return true if was removed or false else
     * @throws IOException invoked if an error with file was occurred
     * @throws SAXException invoked if an error with SAX
     * @throws TransformerException invoked if xml transformation fail
     */
    public boolean removeBook(Book book) throws IOException, TransformerException, SAXException {
        Element element = getElementBook(book.getTitle(), book.getAuthor());

        if (element != null) {
            document.getDocumentElement().removeChild(element);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(inputFile);
            transformer.transform(source, result);

            fireLibraryRemoveListener(book.getTitle(), book.getAuthor());
            load();
            return true;
        }
        return false;
    }

    /**
     * obtain a book as DOM element
     * @param title the title of book
     * @param author the author of book
     * @return the DOM element or null if not exist
     */
    protected Element getElementBook(String title, String author) {
        try {
            NodeList nBooks = document.getElementsByTagName("book");
            for (int i = 0; i < nBooks.getLength(); i++) {
                String title2, author2;
                Element element = (Element) nBooks.item(i);
                title2 = element.getElementsByTagName("title").item(0).getTextContent();
                author2 = element.getElementsByTagName("author").item(0).getTextContent();
                if (title2.equals(title) && author2.equals(author)) {
                    return element;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * obtain the {@link Library} from file
     * @return {@link Library}
     */
    public Library getLibrary() {
        return library;
    }

    /**
     * optain the path of file
     * @return a string
     */
    public String getPath() {
        return path;
    }

    /**
     * add a new listener on load event
     * @param listener the listener
     */
    public void addLibraryLoadListener(LibraryLoadListener listener) {
        this.listenerList.add(LibraryLoadListener.class, listener);
    }

    /**
     * add a new listener on remove book event
     * @param listener the listener
     */
    public void addLibraryRemoveListener(LibraryRemoveListener listener) {
        this.listenerList.add(LibraryRemoveListener.class, listener);
    }

    /**
     * obtain all load listener
     * @return a array of {@link LibraryLoadListener}
     */
    public LibraryLoadListener[] getLibraryLoadListener() {
        return this.listenerList.getListeners(LibraryLoadListener.class);
    }

    /**
     * obtain all remove book listener
     * @return a array of {@link LibraryRemoveListener}
     */
    public LibraryRemoveListener[] getLibraryRemoveListener() {
        return this.listenerList.getListeners(LibraryRemoveListener.class);
    }

    /**
     * remove a {@link LibraryLoadListener}
     * @param listener the listener
     */
    public void removeLibraryLoadListener(LibraryLoadListener listener) {
        this.listenerList.remove(LibraryLoadListener.class, listener);
    }

    /**
     * remove a {@link LibraryRemoveListener}
     * @param listener the listener
     */
    public void removeLibraryRemoveListener(LibraryRemoveListener listener) {
        this.listenerList.remove(LibraryRemoveListener.class, listener);
    }

    protected void fireLibraryLoadListener() {
        for (LibraryLoadListener listener : this.listenerList.getListeners(LibraryLoadListener.class)) {
            listener.loaded();
        }
    }

    protected void fireLibraryRemoveListener(String title, String author) {
        for (LibraryRemoveListener listener : this.listenerList.getListeners(LibraryRemoveListener.class)) {
            listener.removed(title, author);
        }
    }
}
