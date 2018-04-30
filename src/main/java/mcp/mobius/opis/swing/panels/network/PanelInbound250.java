package mcp.mobius.opis.swing.panels.network;

import mcp.mobius.opis.api.ITabPanel;
import mcp.mobius.opis.data.holders.newtypes.*;
import mcp.mobius.opis.network.PacketBase;
import mcp.mobius.opis.network.enums.Message;
import mcp.mobius.opis.swing.SelectedTab;
import mcp.mobius.opis.swing.widgets.JPanelMsgHandler;
import mcp.mobius.opis.swing.widgets.JTableStats;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class PanelInbound250 extends JPanelMsgHandler implements ITabPanel {

    public PanelInbound250() {
        setLayout(new MigLayout("", "[grow]", "[grow]"));

        JScrollPane scrollPane = new JScrollPane();
        add(scrollPane, "cell 0 0,grow");

        table = new JTableStats(new String[] { "Channel", "Amount", "Rate", "Total Size" }, new Class[] { CachedString.class, DataAmountRate.class, DataByteRate.class, DataByteSize.class }, new int[] { SwingConstants.LEFT, SwingConstants.RIGHT, SwingConstants.RIGHT, SwingConstants.RIGHT });
        scrollPane.setViewportView(table);
    }

    @Override
    public boolean handleMessage(Message msg, PacketBase rawdata) {
        switch (msg) {
            case LIST_PACKETS_INBOUND_250: {
                cacheData(msg, rawdata);

                getTable().setTableData(rawdata.array);

                DefaultTableModel model = getTable().getModel();
                int row = getTable().clearTable(DataPacket250.class);

                for (Object o : rawdata.array) {
                    DataPacket250 packet = (DataPacket250) o;
                    model.addRow(new Object[] { packet.channel, packet.amount, packet.rate, packet.size });
                }

                getTable().dataUpdated(row);

                break;
            }

            default:
                return false;

        }
        return true;
    }

    @Override
    public SelectedTab getSelectedTab() {
        return SelectedTab.PACKETINBOUND250;
    }

    @Override
    public boolean refreshOnString() {
        return false;
    }
}
