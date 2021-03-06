package mcp.mobius.opis.swing.widgets;

import mcp.mobius.opis.data.holders.ISerializable;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.HashMap;

public class JTableStats extends JTable {

    public class OpisTableModel extends DefaultTableModel {

        String[] headers;
        Class[] columnTypes;
        boolean[] columnEditables;

        public OpisTableModel(String[] headers, Class[] columnTypes, boolean[] columnEditables) {
            super(new Object[][] {}, headers);
            this.headers = headers;
            this.columnTypes = columnTypes;
            this.columnEditables = columnEditables;
        }

        @Override
        public Class getColumnClass(int columnIndex) {
            return columnTypes[columnIndex];
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return columnEditables[column];
        }
    }

    protected ArrayList<ISerializable> statistics;
    protected int[] alignList;

    public JTableStats(String[] headers, Class[] columnTypes) {
        this(headers, columnTypes, new int[] { SwingConstants.CENTER, SwingConstants.CENTER, SwingConstants.CENTER, SwingConstants.CENTER, SwingConstants.CENTER, SwingConstants.CENTER, SwingConstants.CENTER, SwingConstants.CENTER, SwingConstants.CENTER, SwingConstants.CENTER }, new boolean[] { false, false, false, false, false, false, false, false, false, false });
    }

    public JTableStats(String[] headers, Class[] columnTypes, boolean[] editable) {
        this(headers, columnTypes, new int[] { SwingConstants.CENTER, SwingConstants.CENTER, SwingConstants.CENTER, SwingConstants.CENTER, SwingConstants.CENTER, SwingConstants.CENTER, SwingConstants.CENTER, SwingConstants.CENTER, SwingConstants.CENTER, SwingConstants.CENTER }, editable);
    }

    public JTableStats(String[] headers, Class[] columnTypes, int[] align) {
        this(headers, columnTypes, align, new boolean[] { false, false, false, false, false, false, false, false, false, false });
    }

    public JTableStats(String[] headers, Class[] columnTypes, int[] align, boolean[] editable) {

        super();
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setAutoCreateRowSorter(true);
        alignList = align;
        setModel(new OpisTableModel(headers, columnTypes, editable));

        HashMap<Integer, DefaultTableCellRenderer> hashAlign = new HashMap<>();

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        hashAlign.put(SwingConstants.CENTER, centerRenderer);

        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment(SwingConstants.LEFT);
        hashAlign.put(SwingConstants.LEFT, leftRenderer);

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        hashAlign.put(SwingConstants.RIGHT, rightRenderer);

        for (int i = 0; i < getColumnCount(); i++) {
            getColumnModel().getColumn(i).setCellRenderer(hashAlign.get(align[i]));
        }
    }

    public ArrayList<ISerializable> getTableData() {
        return statistics;
    }

    public void setTableData(ArrayList<ISerializable> statistics) {
        this.statistics = statistics;
    }

    public void clearTableData() {
        statistics = new ArrayList<>();
    }

    public void addTableData(ArrayList<ISerializable> statistics) {
        this.statistics.addAll(statistics);
    }

    @Override
    public DefaultTableModel getModel() {
        return (DefaultTableModel) super.getModel();
    }

    public <U> int clearTable(Class<U> datatype) {
        int row = getSelectedRow();
        try {
            getModel().setRowCount(0);
        } catch (NullPointerException e) {
        }
        return row;
    }

    public void dataUpdated(int row) {
        getModel().fireTableDataChanged();

        try {
            setRowSelectionInterval(row, row);
        } catch (IllegalArgumentException e) {

        }
    }
}
