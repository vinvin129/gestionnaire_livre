package fr.vincatif.perso.bookManager.models;

import javax.swing.table.AbstractTableModel;

/**
 * Model for {@link javax.swing.JTable} of {@link Book}
 */
public class BookTableModel extends AbstractTableModel {

    private Object[][] data;
    private final String[] title;

    /**
     * create a model without data
     * @param title the title array of columns
     */
    public BookTableModel(String[] title){
        this(new Object[][]{}, title);
    }

    /**
     * create a model without data
     * @param data initials rows
     * @param title the title array of columns
     */
    public BookTableModel(Object[][] data, String[] title){
        this.data = data;
        this.title = title;
    }

    /**
     * obtain column title at specified index
     * @param col column index
     * @return title
     */
    @Override
    public String getColumnName(int col) {
        return this.title[col];
    }

    /**
     * obtain column count
     * @return count
     */
    @Override
    public int getColumnCount() {
        return this.title.length;
    }

    /**
     * obtain row count
     * @return count
     */
    @Override
    public int getRowCount() {
        return this.data.length;
    }

    /**
     * obtain value at specified emplacement
     * @param row the row of value
     * @param col the column of value
     * @return value
     */
    @Override
    public Object getValueAt(int row, int col) {
        return this.data[row][col];
    }

    /**
     * obtain the {@link Class} of value column
     * @param col the column of value
     * @return Class
     */
    @Override
    public Class<?> getColumnClass(int col){
        //On retourne le type de la cellule à la colonne demandée
        //On se moque de la ligne puisque les données sont les mêmes
        //On choisit donc la première ligne
        return this.data[0][col].getClass();
    }

    /**
     * Do nothing in this class
     * @param value the value
     * @param row row of table
     * @param col column of table
     */
    @Override
    public void setValueAt(Object value, int row, int col) {

    }

    /**
     * remove a table line
     * @param position position of row
     */
    public void removeRow(int position){

        int indice = 0, indice2 = 0,
        nbRow = this.getRowCount()-1, nbCol = this.getColumnCount();
        Object[][] temp = new Object[nbRow][nbCol];

        for(Object[] value : this.data){
            if(indice != position){
                temp[indice2++] = value;
            }
            System.out.println("Indice = " + indice);
            indice++;
        }
        this.data = temp;
        //Cette méthode permet d'avertir le tableau que les données
        //ont été modifiées, ce qui permet une mise à jour complète du tableau
        this.fireTableDataChanged();
    }

    /**
     * remove all rows (clean the table)
     */
    public void removeRows() {
        this.data = new Object[][] {};
        this.fireTableDataChanged();
    }

    /**
     * add a line at table
     * @param data array of content of row
     */
    public void addRow(Object[] data){
        int indice = 0, nbRow = this.getRowCount(), nbCol = this.getColumnCount();

        Object[][] temp = this.data;
        this.data = new Object[nbRow+1][nbCol];

        for(Object[] value : temp)
            this.data[indice++] = value;


        this.data[indice] = data;
        //Cette méthode permet d'avertir le tableau que les données
        //ont été modifiées, ce qui permet une mise à jour complète du tableau
        this.fireTableDataChanged();
    }

    /**
     * say if cell can be edited.<BR>
     *     WARRING ! only fourth column can be edited
     * @param row the row of table
     * @param col the column of table
     * @return true if editable, false else
     */
    @Override
    public boolean isCellEditable(int row, int col){

        return col == 4;
    }
}
