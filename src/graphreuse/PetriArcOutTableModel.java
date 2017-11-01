/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphreuse;

import PetriObj.ArcOut;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import graphnet.GraphArcOut;

/**
 *
 * @author User
 */
public class PetriArcOutTableModel extends AbstractTableModel {

    private final int TIE_PARAMETERS = 1;
    private int row;
    private int column = TIE_PARAMETERS + 1;
    private Object[][] mass;
    private static String[] COLUMN_NAMES = {"Дуга", "Кількість зв'язків"};
    private ArrayList<GraphArcOut> graphPetriTieOutList;

    public PetriArcOutTableModel(){
        
    }
    
    public void setGraphPetriTieOutList(ArrayList<GraphArcOut> list) {
        graphPetriTieOutList = list;
        row = list.size();
        this.mass = new Object[this.row][this.column];
        for (int i = 0; i < row; i++) {
            ArcOut to = list.get(i).getArcOut();
            mass[i][0] = to.getNameT() + " -> " + to.getNameP();
            mass[i][1] = to.getQuantity();
        }
    }

    @Override
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

    public ArrayList<GraphArcOut> createGraphPetriTieOutList() {
        for (int i = 0; i < graphPetriTieOutList.size(); i++) {
            ArcOut to = graphPetriTieOutList.get(i).getArcOut();
            to.setQuantity(Integer.valueOf(getValueAt(i, 1).toString()));
        }
        return graphPetriTieOutList;
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
}
