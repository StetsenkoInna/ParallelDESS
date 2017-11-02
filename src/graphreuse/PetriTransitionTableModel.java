/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphreuse;

import PetriObj.PetriT;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import graphnet.GraphPetriTransition;

/**
 *
 * @author Ольга
 */
public class PetriTransitionTableModel extends AbstractTableModel {

    private final int TRANSITION_PARAMETERS = 5;
    private int row;
    private int column = TRANSITION_PARAMETERS + 1;
    private Object[][] mass;
    private String[] COLUMN_NAMES = {"Перехід", "Назва", "Значення часової затримки", "Розподіл часу затримки", "Пріоритет переходу", "Ймовірність спрацьовування"};
    private ArrayList<GraphPetriTransition> graphPetriTransitionList;
    private static String[] DISTRIBUTION_OPTIONS = {"null", "exp", "unif", "norm"};
    private int distributionColumnIndex = 3;
    private int priorityColumnIndex = 4;

    public PetriTransitionTableModel() {
    }

    public void setGraphPetriTransitionList(ArrayList<GraphPetriTransition> list) {
        row = list.size();
        graphPetriTransitionList = list;
        this.mass = new Object[this.row][this.column];
        for (int i = 0; i < row; i++) {
            PetriT pt = list.get(i).getPetriTransition();
            mass[i][0] = pt.getName();
            mass[i][1] = pt.getName();
            mass[i][2] = pt.getParametr();
            mass[i][3] = pt.getDistribution();
            mass[i][4] = pt.getPriority();
            mass[i][5] = pt.getProbability();
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

    public ArrayList<GraphPetriTransition> createGraphPetriTransitionList() {
        for (int i = 0; i < graphPetriTransitionList.size(); i++) {
            PetriT pt = graphPetriTransitionList.get(i).getPetriTransition();
            pt.setName(getValueAt(i, 1).toString());
            if (getValueAt(i, 3) == null) {
                pt.setParametr(Double.valueOf(getValueAt(i, 2).toString()));
            } else {
                pt.setDistribution(getValueAt(i, 3).toString(), Double.valueOf(getValueAt(i, 2).toString()));
            }
            pt.setPriority(Integer.valueOf(getValueAt(i, 4).toString()));
            pt.setProbability(Double.valueOf(getValueAt(i, 5).toString()));
        }
        return graphPetriTransitionList;
    }

    public int getDistributionColumnIndex() {
        return distributionColumnIndex;
    }

    public void setDistributionColumnIndex(int distributionColumnIndex) {
        this.distributionColumnIndex = distributionColumnIndex;
    }

    public int getPriorityColumnIndex() {
        return priorityColumnIndex;
    }

    public void setPriorityColumnIndex(int priorityColumnIndex) {
        this.priorityColumnIndex = priorityColumnIndex;
    }

    public static String[] getDISTRIBUTION_OPTIONS() {
        return DISTRIBUTION_OPTIONS;
    }

    public static void setDISTRIBUTION_OPTIONS(String[] DISTRIBUTION_OPTIONS) {
        PetriTransitionTableModel.DISTRIBUTION_OPTIONS = DISTRIBUTION_OPTIONS;
    }
}
