/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphreuse;

import PetriObj.PetriP;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import graphnet.GraphPetriPlace;

/**
 *
 * @author Ольга
 */
public class PetriPlaceTableModel extends AbstractTableModel {

    private final int PLACE_PARAMETERS = 2;
    private int row;
    private int column = PLACE_PARAMETERS + 1;
    private Object[][] mass;
    private String[] COLUMN_NAMES = {"Позиція", "Ім'я", "Маркери"};
    private ArrayList<GraphPetriPlace> graphPetriPlaceList;   

    public PetriPlaceTableModel(){

    }
    
    public void setGraphPetriPlaceList(ArrayList<GraphPetriPlace> list) {
        row = list.size();
        graphPetriPlaceList = list;
        this.mass = new Object[this.row][this.column];
        for (int i = 0; i < row; i++) {
            PetriP pp = list.get(i).getPetriPlace();
            mass[i][0] = pp.getName();
            mass[i][1] = pp.getName();
            mass[i][2] = pp.getMark();
        }
    }

    @Override
    public int getRowCount() {
        return this.row;
    }

    @Override
    public int getColumnCount() {
        return this.column;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return this.mass[rowIndex][columnIndex];
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
       // System.out.println("PetriPlaceTableModel: Value has been set");
        this.mass[row][col] = (Object) value;
        fireTableCellUpdated(row, col);
    }

    @Override
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    public ArrayList<GraphPetriPlace> createGraphPetriPlaceList() {
        for (int i = 0; i < graphPetriPlaceList.size(); i++) {
            graphPetriPlaceList.get(i).getPetriPlace().setName(getValueAt(i, 1).toString());
            graphPetriPlaceList.get(i).getPetriPlace().setMark(Integer.valueOf(getValueAt(i, 2).toString()));
        }
        return graphPetriPlaceList;
    }
}
