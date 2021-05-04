package fr.vincatif.perso.bookManager.models;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class BookTableModelTest {
    private final String[] title = new String[]{"column 1", "column 2", "column 3"};
    private final ArrayList<Object[]> data = new ArrayList<>();
    private BookTableModel model;

    @BeforeEach
    void setUp() {
        model = new BookTableModel(title);
        data.clear();
    }

    @AfterEach
    void tearDown() {
        assertEquals(title.length, model.getColumnCount(), "the column number is incorrect");
        assertEquals(data.size(), model.getRowCount(), "the row number is incorrect");
    }

    @Test
    void getColumnName() {
        assertEquals(title[0], model.getColumnName(0), "the column name is incorrect");
    }

    @Test
    void getValueAt() {
        Object[] row1 = new Object[]{"toto", "titi", "tata"};
        Object[] row2 = new Object[]{"abcd", "cola", "sale"};
        model.addRow(row1);
        data.add(row1);
        model.addRow(row2);
        data.add(row2);

        assertAll("wrong value returned", () -> {
            assertEquals("titi", model.getValueAt(0, 1));
            assertEquals("cola", model.getValueAt(1, 1));
            assertEquals("sale", model.getValueAt(1, 2));
        });
    }

    @Test
    void getColumnClass() {
    }

    @Test
    void setValueAt() {
    }

    @Test
    void removeRow() {
    }

    @Test
    void removeRows() {
    }

    @Test
    void addRow() {
    }

    @Test
    void isCellEditable() {
    }
}