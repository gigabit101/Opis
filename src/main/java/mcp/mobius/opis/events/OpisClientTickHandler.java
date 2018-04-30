package mcp.mobius.opis.events;

import mcp.mobius.opis.Opis;
import mcp.mobius.opis.api.TabPanelRegistrar;
import mcp.mobius.opis.data.holders.basetypes.CoordinatesBlock;
import mcp.mobius.opis.data.holders.basetypes.SerialLong;
import mcp.mobius.opis.data.holders.newtypes.DataBlockRender;
import mcp.mobius.opis.data.holders.newtypes.DataEntityRender;
import mcp.mobius.opis.data.holders.newtypes.DataTileEntityRender;
import mcp.mobius.opis.data.profilers.ProfilerRenderBlock;
import mcp.mobius.opis.data.profilers.ProfilerRenderEntity;
import mcp.mobius.opis.data.profilers.ProfilerRenderTileEntity;
import mcp.mobius.opis.network.PacketManager;
import mcp.mobius.opis.network.enums.Message;
import mcp.mobius.opis.network.packets.client.PacketReqData;
import mcp.mobius.opis.profiler.ProfilerSection;
import mcp.mobius.opis.profiler.Profilers;
import mcp.mobius.opis.swing.SelectedTab;
import mcp.mobius.opis.swing.panels.timingclient.PanelEventClient;
import mcp.mobius.opis.swing.panels.timingclient.PanelRenderEntities;
import mcp.mobius.opis.swing.panels.timingclient.PanelRenderHandlers;
import mcp.mobius.opis.swing.panels.timingclient.PanelRenderTileEnts;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Collections;

public enum OpisClientTickHandler {
    INSTANCE;

    public long profilerUpdateTickCounter = 0;
    public long profilerRunningTicks = 0;
    public EventTimer timer500 = new EventTimer(500);
    public EventTimer timer1000 = new EventTimer(1000);
    public EventTimer timer2000 = new EventTimer(2000);
    public EventTimer timer5000 = new EventTimer(5000);
    public EventTimer timer10000 = new EventTimer(10000);

    @SubscribeEvent
    @SideOnly (Side.CLIENT)
    public void tickEnd(TickEvent.ClientTickEvent event) {

        // One second timer
        if (timer1000.isDone()) {
            if (Opis.swingOpen) {
                PacketManager.sendToServer(new PacketReqData(Message.STATUS_PING, new SerialLong(System.nanoTime())));
            }
        }

        if (Opis.profilerRunClient) {
            ((PanelRenderTileEnts) TabPanelRegistrar.INSTANCE.getTab(SelectedTab.RENDERTILEENTS)).getBtnRunRender().setText("Running...");
            ((PanelRenderEntities) TabPanelRegistrar.INSTANCE.getTab(SelectedTab.RENDERENTITIES)).getBtnRunRender().setText("Running...");
            ((PanelRenderHandlers) TabPanelRegistrar.INSTANCE.getTab(SelectedTab.RENDERHANDLERS)).getBtnRunRender().setText("Running...");
            ((PanelEventClient) TabPanelRegistrar.INSTANCE.getTab(SelectedTab.CLIENTEVENTS)).getBtnRunRender().setText("Running...");
        } else {
            ((PanelRenderTileEnts) TabPanelRegistrar.INSTANCE.getTab(SelectedTab.RENDERTILEENTS)).getBtnRunRender().setText("Run Render");
            ((PanelRenderEntities) TabPanelRegistrar.INSTANCE.getTab(SelectedTab.RENDERENTITIES)).getBtnRunRender().setText("Run Render");
            ((PanelRenderHandlers) TabPanelRegistrar.INSTANCE.getTab(SelectedTab.RENDERHANDLERS)).getBtnRunRender().setText("Run Render");
            ((PanelEventClient) TabPanelRegistrar.INSTANCE.getTab(SelectedTab.CLIENTEVENTS)).getBtnRunRender().setText("Run Render");
        }

        profilerUpdateTickCounter++;

        if (profilerRunningTicks < Opis.profilerMaxTicks && Opis.profilerRunClient) {
            profilerRunningTicks++;
        } else if (profilerRunningTicks >= Opis.profilerMaxTicks && Opis.profilerRunClient) {
            profilerRunningTicks = 0;
            Opis.profilerRunClient = false;
            Profilers.dissableProfilers(Side.CLIENT);

            System.out.printf("Profiling done\n");

            updateTabs();

        }
    }

    private void updateTabs() {
        //====================================================================================

        ArrayList<DataTileEntityRender> tileEntData = new ArrayList<>();
        double tileEntTotal = 0.0D;
        for (TileEntity te : ((ProfilerRenderTileEntity) ProfilerSection.RENDER_TILEENTITY.getProfiler()).data.keySet()) {
            try {
                DataTileEntityRender dataTe = new DataTileEntityRender().fill(te);
                tileEntData.add(dataTe);
                tileEntTotal += dataTe.update.timing;
            } catch (Exception e) {
                Opis.log.warn(String.format("Error while adding entity %s to the list", te));
            }
        }

        System.out.printf("Rendered %d TileEntities\n", tileEntData.size());

        Collections.sort(tileEntData);
        ((PanelRenderTileEnts) TabPanelRegistrar.INSTANCE.getTab(SelectedTab.RENDERTILEENTS)).setTable(tileEntData);
        ((PanelRenderTileEnts) TabPanelRegistrar.INSTANCE.getTab(SelectedTab.RENDERTILEENTS)).getLblTotal().setText(String.format("Total : %.3f µs", tileEntTotal / 1000.0));

        //====================================================================================

        ArrayList<DataEntityRender> entData = new ArrayList<>();
        double entTotal = 0.0D;
        for (Entity ent : ((ProfilerRenderEntity) ProfilerSection.RENDER_ENTITY.getProfiler()).data.keySet()) {
            try {
                DataEntityRender dataEnt = new DataEntityRender().fill(ent);
                entData.add(dataEnt);
                entTotal += dataEnt.update.timing;
            } catch (Exception e) {
                Opis.log.warn(String.format("Error while adding entity %s to the list", ent));
            }
        }

        System.out.printf("Rendered %d Entities\n", entData.size());

        Collections.sort(entData);
        ((PanelRenderEntities) TabPanelRegistrar.INSTANCE.getTab(SelectedTab.RENDERENTITIES)).setTable(entData);
        ((PanelRenderEntities) TabPanelRegistrar.INSTANCE.getTab(SelectedTab.RENDERENTITIES)).getLblTotal().setText(String.format("Total : %.3f µs", entTotal / 1000.0));

        //====================================================================================

        //((PanelRenderHandlers)(TabPanelRegistrar.INSTANCE.getTab(SelectedTab.RENDERHANDLERS))).setTable(TickHandlerManager.getCumulatedStatsRender());

        //====================================================================================

        ((PanelEventClient) TabPanelRegistrar.INSTANCE.getTab(SelectedTab.CLIENTEVENTS)).setTable(Profilers.ASM_EVENT_HANDLER.get().getData());

        //====================================================================================

        ArrayList<DataBlockRender> blockData = new ArrayList<>();
        for (CoordinatesBlock coord : ((ProfilerRenderBlock) ProfilerSection.RENDER_BLOCK.getProfiler()).data.keySet()) {
            try {
                DataBlockRender dataBlock = new DataBlockRender().fill(coord);
                blockData.add(dataBlock);
            } catch (Exception e) {
                Opis.log.warn(String.format("Error while adding block %s to the list", coord));
            }
        }
    }
}
