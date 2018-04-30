package mcp.mobius.opis.events;

import mcp.mobius.opis.Opis;
import mcp.mobius.opis.data.holders.basetypes.SerialInt;
import mcp.mobius.opis.data.holders.basetypes.SerialLong;
import mcp.mobius.opis.data.holders.newtypes.DataDimension;
import mcp.mobius.opis.data.holders.newtypes.DataThread;
import mcp.mobius.opis.data.holders.newtypes.DataTiming;
import mcp.mobius.opis.data.holders.stats.StatsChunk;
import mcp.mobius.opis.data.managers.ChunkManager;
import mcp.mobius.opis.data.managers.EntityManager;
import mcp.mobius.opis.data.managers.StringCache;
import mcp.mobius.opis.data.managers.TileEntityManager;
import mcp.mobius.opis.gui.overlay.OverlayStatus;
import mcp.mobius.opis.network.PacketManager;
import mcp.mobius.opis.network.enums.AccessLevel;
import mcp.mobius.opis.network.enums.Message;
import mcp.mobius.opis.network.packets.server.NetDataCommand;
import mcp.mobius.opis.network.packets.server.NetDataList;
import mcp.mobius.opis.network.packets.server.NetDataValue;
import mcp.mobius.opis.profiler.Profilers;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.ArrayList;
import java.util.HashMap;

public enum OpisServerTickHandler {
    INSTANCE;

    public long profilerUpdateTickCounter = 0;
    public int profilerRunningTicks;
    public EventTimer timer500 = new EventTimer(500);
    public EventTimer timer1000 = new EventTimer(1000);
    public EventTimer timer2000 = new EventTimer(2000);
    public EventTimer timer5000 = new EventTimer(5000);
    public EventTimer timer10000 = new EventTimer(10000);

    public HashMap<EntityPlayerMP, AccessLevel> cachedAccess = new HashMap<>();

    @SubscribeEvent
    public void tickEnd(TickEvent.ServerTickEvent event) {

        StringCache.INSTANCE.syncNewCache();

        // One second timer
        if (timer1000.isDone()) {

            PacketManager.sendPacketToAllSwing(new NetDataValue(Message.VALUE_AMOUNT_UPLOAD, new SerialLong(Profilers.OUTBOUND_PACKET.get().dataAmount + Profilers.OUTBOUND_FML_PACKET.get().dataAmount)));
            PacketManager.sendPacketToAllSwing(new NetDataValue(Message.VALUE_AMOUNT_DOWNLOAD, new SerialLong(Profilers.INBOUND_PACKET.get().dataAmount + Profilers.INBOUND_FML_PACKET.get().dataAmount)));
            PacketManager.sendPacketToAllSwing(new NetDataValue(Message.VALUE_CHUNK_FORCED, new SerialInt(ChunkManager.INSTANCE.getForcedChunkAmount())));
            PacketManager.sendPacketToAllSwing(new NetDataValue(Message.VALUE_CHUNK_LOADED, new SerialInt(ChunkManager.INSTANCE.getLoadedChunkAmount())));
            PacketManager.sendPacketToAllSwing(new NetDataValue(Message.VALUE_TIMING_TICK, new DataTiming(Profilers.SERVER_TICK.get().data.getGeometricMean())));
            PacketManager.sendPacketToAllSwing(new NetDataValue(Message.VALUE_AMOUNT_TILEENTS, new SerialInt(TileEntityManager.INSTANCE.getAmountTileEntities())));
            PacketManager.sendPacketToAllSwing(new NetDataValue(Message.VALUE_AMOUNT_ENTITIES, new SerialInt(EntityManager.INSTANCE.getAmountEntities())));

            for (EntityPlayerMP player : PlayerTracker.INSTANCE.playersSwing) {
                if (!cachedAccess.containsKey(player) || cachedAccess.get(player) != PlayerTracker.INSTANCE.getPlayerAccessLevel(player)) {
                    PacketManager.validateAndSend(new NetDataValue(Message.STATUS_ACCESS_LEVEL, new SerialInt(PlayerTracker.INSTANCE.getPlayerAccessLevel(player).ordinal())), player);
                    cachedAccess.put(player, PlayerTracker.INSTANCE.getPlayerAccessLevel(player));
                }
            }

            ArrayList<DataThread> threads = new ArrayList<>();
            for (Thread t : Thread.getAllStackTraces().keySet()) {
                threads.add(new DataThread().fill(t));
            }
            PacketManager.sendPacketToAllSwing(new NetDataList(Message.LIST_THREADS, threads));

            // Dimension data update.
            ArrayList<DataDimension> dimData = new ArrayList<>();
            for (int dim : DimensionManager.getIDs()) {
                dimData.add(new DataDimension().fill(dim));
            }
            PacketManager.sendPacketToAllSwing(new NetDataList(Message.LIST_DIMENSION_DATA, dimData));

            // Profiler update (if running)
            if (Opis.profilerRun) {
                PacketManager.sendPacketToAllSwing(new NetDataValue(Message.STATUS_RUNNING, new SerialInt(Opis.profilerMaxTicks)));
                PacketManager.sendPacketToAllSwing(new NetDataValue(Message.STATUS_RUN_UPDATE, new SerialInt(profilerRunningTicks)));
            }
            Profilers.INBOUND_PACKET.get().reset();
            Profilers.OUTBOUND_PACKET.get().reset();
            Profilers.INBOUND_FML_PACKET.get().reset();
            Profilers.OUTBOUND_FML_PACKET.get().reset();
        }

        // Two second timer
        if (timer2000.isDone()) {
            PacketManager.sendPacketToAllSwing(new NetDataList(Message.LIST_PLAYERS, EntityManager.INSTANCE.getAllPlayers()));
        }

        // Five second timer
        if (timer5000.isDone()) {
            updatePlayers();

            PacketManager.sendPacketToAllSwing(new NetDataList(Message.LIST_PACKETS_OUTBOUND, new ArrayList<>(Profilers.OUTBOUND_PACKET.get().data.values())));
            PacketManager.sendPacketToAllSwing(new NetDataList(Message.LIST_PACKETS_INBOUND, new ArrayList<>(Profilers.INBOUND_PACKET.get().data.values())));

            PacketManager.sendPacketToAllSwing(new NetDataList(Message.LIST_PACKETS_OUTBOUND_250, new ArrayList<>(Profilers.OUTBOUND_FML_PACKET.get().data.values())));
            PacketManager.sendPacketToAllSwing(new NetDataList(Message.LIST_PACKETS_INBOUND_250, new ArrayList<>(Profilers.INBOUND_FML_PACKET.get().data.values())));

            Profilers.INBOUND_PACKET.get().start();
            Profilers.OUTBOUND_PACKET.get().start();
            Profilers.INBOUND_FML_PACKET.get().start();
            Profilers.OUTBOUND_FML_PACKET.get().start();

        }

        profilerUpdateTickCounter++;

        if (profilerRunningTicks < Opis.profilerMaxTicks && Opis.profilerRun) {
            profilerRunningTicks++;
        } else if (profilerRunningTicks >= Opis.profilerMaxTicks && Opis.profilerRun) {
            profilerRunningTicks = 0;
            Opis.profilerRun = false;
            Profilers.dissableProfilers(Side.SERVER);

            PacketManager.sendPacketToAllSwing(new NetDataValue(Message.STATUS_STOP, new SerialInt(Opis.profilerMaxTicks)));

            for (EntityPlayerMP player : PlayerTracker.INSTANCE.playersSwing) {
                PacketManager.sendFullUpdate(player);
            }
        }
    }

    private void updatePlayers() {
        for (EntityPlayerMP player : PlayerTracker.INSTANCE.playerOverlayStatus.keySet()) {

            if (PlayerTracker.INSTANCE.playerOverlayStatus.get(player) == OverlayStatus.CHUNKSTATUS) {
                PacketManager.validateAndSend(new NetDataCommand(Message.LIST_CHUNK_LOADED_CLEAR), player);
                PacketManager.splitAndSend(Message.LIST_CHUNK_LOADED, ChunkManager.INSTANCE.getLoadedChunks(PlayerTracker.INSTANCE.playerDimension.get(player)), player);
            }

            if (PlayerTracker.INSTANCE.playerOverlayStatus.get(player) == OverlayStatus.MEANTIME) {
                ArrayList<StatsChunk> timingChunks = ChunkManager.INSTANCE.getTopChunks(100);
                PacketManager.validateAndSend(new NetDataList(Message.LIST_TIMING_CHUNK, timingChunks), player);
            }
        }
    }
}
