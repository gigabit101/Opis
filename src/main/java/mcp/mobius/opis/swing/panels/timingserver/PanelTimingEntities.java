package mcp.mobius.opis.swing.panels.timingserver;

import mcp.mobius.opis.api.ITabPanel;
import mcp.mobius.opis.data.holders.newtypes.DataEntity;
import mcp.mobius.opis.data.holders.newtypes.DataTiming;
import mcp.mobius.opis.network.PacketBase;
import mcp.mobius.opis.network.enums.AccessLevel;
import mcp.mobius.opis.network.enums.Message;
import mcp.mobius.opis.swing.SelectedTab;
import mcp.mobius.opis.swing.actions.ActionRunOpis;
import mcp.mobius.opis.swing.actions.ActionTimingEntities;
import mcp.mobius.opis.swing.widgets.JButtonAccess;
import mcp.mobius.opis.swing.widgets.JPanelMsgHandler;
import mcp.mobius.opis.swing.widgets.JTableStats;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class PanelTimingEntities extends JPanelMsgHandler implements ITabPanel {

    private JButtonAccess btnRun;
    private JButtonAccess btnPull;
    private JButtonAccess btnTeleport;
    private JButtonAccess btnCenter;
    private JLabel lblSummary;

    /**
     * Create the panel.
     */
    public PanelTimingEntities() {
        setLayout(new MigLayout("", "[][][][grow][]", "[][grow][]"));

        btnCenter = new JButtonAccess("Center Map", AccessLevel.NONE);
        add(btnCenter, "cell 0 0");
        btnCenter.addActionListener(new ActionTimingEntities());

        btnTeleport = new JButtonAccess("Teleport", AccessLevel.PRIVILEGED);
        add(btnTeleport, "cell 1 0");
        btnTeleport.addActionListener(new ActionTimingEntities());

        btnPull = new JButtonAccess("Pull", AccessLevel.PRIVILEGED);
        add(btnPull, "cell 2 0");
        btnPull.addActionListener(new ActionTimingEntities());

        btnRun = new JButtonAccess("Run Opis", AccessLevel.PRIVILEGED);
        add(btnRun, "cell 4 0");
        btnRun.addActionListener(new ActionRunOpis());

        JScrollPane scrollPane = new JScrollPane();
        add(scrollPane, "cell 0 1 5 1,grow");

        table = new JTableStats(new String[] { "Type", "ID", "Dim", "Pos", "Update Time", "Data" }, new Class[] { String.class, Integer.class, Integer.class, Object.class, DataTiming.class, Integer.class });
        scrollPane.setViewportView(table);

        lblSummary = new JLabel("New label");
        add(lblSummary, "cell 0 2 5 1,alignx center");
    }

    public JButton getBtnRun() {
        return btnRun;
    }

    public JButton getBtnPull() {
        return btnPull;
    }

    public JButton getBtnTeleport() {
        return btnTeleport;
    }

    public JButton getBtnCenter() {
        return btnCenter;
    }

    public JLabel getLblSummary() {
        return lblSummary;
    }

    @Override
    public boolean handleMessage(Message msg, PacketBase rawdata) {
        switch (msg) {
            case LIST_TIMING_ENTITIES: {
                cacheData(msg, rawdata);

                getTable().setTableData(rawdata.array);

                DefaultTableModel model = table.getModel();
                int row = getTable().clearTable(DataEntity.class);

                for (Object o : rawdata.array) {
                    DataEntity data = (DataEntity) o;
                    model.addRow(new Object[] { data.name, data.eid, data.pos.dim, String.format("[ %4d %4d %4d ]", data.pos.x, data.pos.y, data.pos.z), data.update, data.npoints });
                }

                getTable().dataUpdated(row);

                break;
            }
            case VALUE_TIMING_ENTITIES: {
                getLblSummary().setText(String.format("Total update time : %s", rawdata.value.toString()));
                break;
            }
            case STATUS_START: {
                getBtnRun().setText("Running...");
                break;
            }
            case STATUS_STOP: {
                getBtnRun().setText("Run Opis");
                break;
            }
            case STATUS_RUNNING: {
                getBtnRun().setText("Running...");
                break;
            }
            default:
                return false;

        }
        return true;
    }

    @Override
    public SelectedTab getSelectedTab() {
        return SelectedTab.TIMINGENTITES;
    }

    @Override
    public boolean refreshOnString() {
        return true;
    }
}
