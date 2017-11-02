/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphreuse;

import PetriObj.ArcIn;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import graphnet.GraphTieIn;

/**
 *
 * @author Ольга
 */
public class PetriTieInTableModel extends AbstractTableModel {

    private final int TIE_PARAMETERS = 2;
    private int row;
    private int column = TIE_PARAMETERS + 1;
    private Object[][] mass;
    private static String[] COLUMN_NAMES = {"Дуга", "Кількість зв'язків", "Інформаційний зв'язок"};
    private ArrayList<GraphTieIn> graphPetriTieInList;
    private static int isInfColumnIndex = 2;
    private static String isInfColumnName = COLUMN_NAMES[isInfColumnIndex];

    public PetriTieInTableModel(){
        
    }
    
    public void setGraphPetriTieInList(ArrayList<GraphTieIn> list) {
        graphPetriTieInList = list;
        row = list.size();
        this.mass = new Object[this.row][this.column];
        for (int i = 0; i < row; i++) {
            ArcIn ti = list.get(i).getTieIn();
            mass[i][0] = ti.getNameP() + " -> " + ti.getNameT();
            mass[i][1] = ti.getQuantity();
            if (ti.getIsInf() == true) {
                mass[i][2] = true;
            } else {
                mass[i][2] = false;
            }
        }
    }

    @Override
    public int getRowCount() {
        return row;
    }

    @Override
    public int getColumnCount() {
        return column;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return this.mass[rowIndex][columnIndex];
    }

    public boolean isCellEditable(int row, int col) {
        if (col == 0) {
            return false;
        }
        return true;
    }

    @Override
    public String getColumnName(int col) {
        return COLUMN_NAMES[col];
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        this.mass[row][col] = (Object) value;
        fireTableCellUpdated(row, col);
    }

    @Override
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    public ArrayList<GraphTieIn> createGraphPetriTieInList() {
        for (int i = 0; i < graphPetriTieInList.size(); i++) {
            ArcIn ti = graphPetriTieInList.get(i).getTieIn();
            ti.setQuantity(Integer.valueOf(getValueAt(i, 1).toString()));
            ti.setInf(Boolean.valueOf(getValueAt(i, 2).toString()));
        }
        return graphPetriTieInList;
    }

    public static int getIsInfColumnIndex() {
        return isInfColumnIndex;
    }

    public static String getIsInfColumnName() {
        return isInfColumnName;
    }

    public static void setIsInfColumnName(String isInfColumnName) {
        PetriTieInTableModel.isInfColumnName = isInfColumnName;
    }
}
