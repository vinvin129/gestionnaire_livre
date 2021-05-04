package fr.vincatif.perso.bookManager.controllers;

import fr.vincatif.perso.bookManager.models.Book;
import fr.vincatif.perso.bookManager.models.Library;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;
import java.util.stream.Stream;

/**
 * Create a Search in a {@link Library} source and add {@link Book} found in its own {@link Library}.
 */
public class LibrarySearch extends Library {
    private final String search;
    private final Library librarySource;

    /**
     * create an {@link LibrarySearch} with a search and a library source
     * @param search the search
     * @param librarySource the library to search {@link Book}
     */
    public LibrarySearch(String search, Library librarySource) {
        this.search = search;
        this.librarySource = librarySource;
        makeSearch();
    }

    /**
     * make the search and put results in librarySource attribute
     */
    protected void makeSearch() {
        this.clear();
        HashMap<Book, Integer> map = new HashMap<>();
        Stream<Book> results = Arrays.stream(librarySource.getBooks()).filter(b -> {
            int points = 0;
            String title = b.getTitle().toLowerCase(Locale.ROOT);
            String author = b.getAuthor().toLowerCase(Locale.ROOT);
            for (String word : search.toLowerCase(Locale.ROOT).split(" ")) {

                if (title.matches("(.*)" + word + "(.*)") || author.matches("(.*)" + word + "(.*)")) {
                    points++;
                    if (title.equals(word) || author.equals(word)) {
                        points++;
                    }
                }
            }

            if (title.equals(search.toLowerCase(Locale.ROOT)) || author.equals(search.toLowerCase(Locale.ROOT))) {
                points+= 100;
            }

            if (points > 0) {
                map.put(b, points);
                return true;
            }

            return false;
        }).sorted(Comparator.comparingInt(map::get).reversed());
        map.forEach((a, b) -> System.out.println(a.getTitle() + " -> " + b));
        results.iterator().forEachRemaining(this::addBook);
    }

    /**
     * obtain the request
     * @return a string
     */
    public String getSearch() {
        return search;
    }
}
