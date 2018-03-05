package mcp.mobius.opis.swing.panels.tracking;

import mcp.mobius.opis.api.ITabPanel;
import mcp.mobius.opis.data.holders.newtypes.DataEntity;
import mcp.mobius.opis.network.PacketBase;
import mcp.mobius.opis.network.enums.AccessLevel;
import mcp.mobius.opis.network.enums.Message;
import mcp.mobius.opis.swing.SelectedTab;
import mcp.mobius.opis.swing.actions.ActionPlayers;
import mcp.mobius.opis.swing.widgets.JButtonAccess;
import mcp.mobius.opis.swing.widgets.JPanelMsgHandler;
import mcp.mobius.opis.swing.widgets.JTableStats;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class PanelPlayers extends JPanelMsgHandler implements ITabPanel {
    private JButtonAccess btnCenter;
    private JButtonAccess btnTeleport;
    private JButtonAccess btnPull;

    /**
     * Create the panel.
     */
    public PanelPlayers() {
        setLayout(new MigLayout("", "[][][][][][grow]", "[][grow]"));

        btnCenter = new JButtonAccess("Center Map", AccessLevel.NONE);
        add(btnCenter, "cell 0 0");
        btnCenter.addActionListener(new ActionPlayers());

        btnTeleport = new JButtonAccess("Teleport", AccessLevel.PRIVILEGED);
        add(btnTeleport, "cell 1 0");
        btnTeleport.addActionListener(new ActionPlayers());

        btnPull = new JButtonAccess("Pull", AccessLevel.PRIVILEGED);
        add(btnPull, "cell 2 0");
        btnPull.addActionListener(new ActionPlayers());

        JButton btnKill = new JButton("Kill");
        btnKill.setEnabled(false);
        add(btnKill, "cell 3 0");

        JButton btnMorph = new JButton("Morph");
        btnMorph.setEnabled(false);
        add(btnMorph, "cell 4 0");

        JScrollPane scrollPane = new JScrollPane();
        add(scrollPane, "cell 0 1 6 1,grow");

        table = new JTableStats(
                new String[]{"Name", "Dimension", "Coordinates"},
                new Class[]{String.class, Integer.class, Object.class}
        );
        scrollPane.setViewportView(table);
    }

    public JButton getBtnCenter() {
        return btnCenter;
    }

    public JButton getBtnTeleport() {
        return btnTeleport;
    }

    public JButton getBtnPull() {
        return btnPull;
    }

    @Override
    public boolean handleMessage(Message msg, PacketBase rawdata) {
        switch (msg) {
            case LIST_PLAYERS: {
                this.cacheData(msg, rawdata);

                this.getTable().setTableData(rawdata.array);

                DefaultTableModel model = this.getTable().getModel();
                int row = this.getTable().clearTable(DataEntity.class);

                for (Object o : rawdata.array) {
                    DataEntity player = (DataEntity) o;
                    model.addRow(new Object[]{
                            player.name,
                            player.pos.dim,
                            String.format("[ %4d %4d %4d ]", player.pos.x, player.pos.y, player.pos.z),
                    });
                }

                this.getTable().dataUpdated(row);

                break;
            }

            default:
                return false;

        }
        return true;

    }

    @Override
    public SelectedTab getSelectedTab() {
        return SelectedTab.PLAYERS;
    }

    @Override
    public boolean refreshOnString() {
        return true;
    }
}
