package mcp.mobius.opis.swing.actions;

import mcp.mobius.opis.api.TabPanelRegistrar;
import mcp.mobius.opis.network.PacketManager;
import mcp.mobius.opis.network.enums.Message;
import mcp.mobius.opis.network.packets.client.PacketReqData;
import mcp.mobius.opis.swing.SelectedTab;
import mcp.mobius.opis.swing.panels.tracking.PanelAmountTileEnts;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ActionAmountTileEnts implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        PanelAmountTileEnts panel = (PanelAmountTileEnts) TabPanelRegistrar.INSTANCE.getTab(SelectedTab.AMOUNTTES);

        if (e.getSource() == panel.getBtnRefresh()) {
            PacketManager.sendToServer(new PacketReqData(Message.LIST_AMOUNT_TILEENTS));
        }
    }
}
