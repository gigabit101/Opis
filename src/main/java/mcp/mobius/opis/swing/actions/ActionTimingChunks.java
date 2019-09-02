package mcp.mobius.opis.swing.actions;

//import mapwriter.Mw;
//import mapwriter.api.MwAPI;
//import mapwriter.gui.MwGui;
import mcp.mobius.opis.api.TabPanelRegistrar;
import mcp.mobius.opis.data.holders.stats.StatsChunk;
//import mcp.mobius.opis.gui.overlay.OverlayMeanTime;
import mcp.mobius.opis.network.PacketManager;
import mcp.mobius.opis.network.enums.Message;
import mcp.mobius.opis.network.packets.client.PacketReqData;
import mcp.mobius.opis.swing.SelectedTab;
import mcp.mobius.opis.swing.panels.timingserver.PanelTimingChunks;
import mcp.mobius.opis.swing.widgets.JTableStats;
//import net.minecraft.client.Minecraft;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ActionTimingChunks implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        PanelTimingChunks panel = (PanelTimingChunks) TabPanelRegistrar.INSTANCE.getTab(SelectedTab.TIMINGCHUNKS);

        JTableStats table = panel.getTable();
        if (table == null || table.getSelectedRow() == -1) {
            return;
        }
        int indexData = table.convertRowIndexToModel(table.getSelectedRow());
        StatsChunk data = (StatsChunk) table.getTableData().get(indexData);

        if (e.getSource() == panel.getBtnCenter()) {
//            OverlayMeanTime.INSTANCE.setSelectedChunk(data.getChunk().dim, data.getChunk().chunkX, data.getChunk().chunkZ);
//            MwAPI.setCurrentDataProvider(OverlayMeanTime.INSTANCE);
//            Minecraft.getMinecraft().displayGuiScreen(new MwGui(Mw.getInstance(), data.getChunk().dim, data.getChunk().x + 8, data.getChunk().z + 8));
        }

        if (e.getSource() == panel.getBtnTeleport()) {
            PacketManager.sendToServer(new PacketReqData(Message.COMMAND_TELEPORT_CHUNK, data.getChunk()));
        }
    }
}
