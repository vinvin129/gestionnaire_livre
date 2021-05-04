package fr.vincatif.perso.bookManager.views.customComponents;

import javax.swing.*;
import java.awt.*;

public class ButtonEditor extends DefaultCellEditor {
    public ButtonEditor() {
        super(new JCheckBox());
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        //On renvoie le bouton
        return (JButton) value;
    }
}
