package mcp.mobius.opis.network;

import codechicken.lib.util.ServerUtils;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import mcp.mobius.opis.data.holders.ISerializable;
import mcp.mobius.opis.data.holders.basetypes.AmountHolder;
import mcp.mobius.opis.data.holders.basetypes.SerialInt;
import mcp.mobius.opis.data.holders.basetypes.SerialLong;
import mcp.mobius.opis.data.holders.newtypes.*;
import mcp.mobius.opis.data.holders.stats.StatsChunk;
import mcp.mobius.opis.data.managers.ChunkManager;
import mcp.mobius.opis.data.managers.EntityManager;
import mcp.mobius.opis.data.managers.TileEntityManager;
import mcp.mobius.opis.data.profilers.ProfilerEvent;
import mcp.mobius.opis.events.PlayerTracker;
import mcp.mobius.opis.network.enums.Message;
import mcp.mobius.opis.network.packets.client.PacketReqChunks;
import mcp.mobius.opis.network.packets.client.PacketReqData;
import mcp.mobius.opis.network.packets.server.NetDataCommand;
import mcp.mobius.opis.network.packets.server.NetDataList;
import mcp.mobius.opis.network.packets.server.NetDataValue;
import mcp.mobius.opis.network.packets.server.PacketChunks;
import mcp.mobius.opis.profiler.ProfilerSection;
import mcp.mobius.opis.profiler.Profilers;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.Packet;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.FMLEmbeddedChannel;
import net.minecraftforge.fml.common.network.FMLIndexedMessageToMessageCodec;
import net.minecraftforge.fml.common.network.FMLOutboundHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.EnumMap;

@Sharable
public class PacketManager {
    //private static final PacketManager INSTANCE = new PacketManager();
    //private static final Logger        LOGGER   = LogManager.getLogger();
    private static final EnumMap<Side, FMLEmbeddedChannel> channels = Maps.newEnumMap(Side.class);

    public static void init() {
        if (!channels.isEmpty()) // avoid duplicate inits..
            return;

        Codec codec = new Codec();

        codec.addDiscriminator(0, PacketReqChunks.class);
        codec.addDiscriminator(1, PacketReqData.class);
        codec.addDiscriminator(2, NetDataCommand.class);
        codec.addDiscriminator(3, NetDataList.class);
        codec.addDiscriminator(4, NetDataValue.class);
        codec.addDiscriminator(5, PacketChunks.class);

        channels.putAll(NetworkRegistry.INSTANCE.newChannel("Opis", codec));

        // add handlers
        if (FMLCommonHandler.instance().getSide().isClient()) {
            // for the client
            FMLEmbeddedChannel channel = channels.get(Side.CLIENT);
            String codecName = channel.findChannelHandlerNameForType(Codec.class);
            channel.pipeline().addAfter(codecName, "ClientHandler", new HandlerClient());
        }
        //else
        {
            // for the server
            FMLEmbeddedChannel channel = channels.get(Side.SERVER);
            String codecName = channel.findChannelHandlerNameForType(Codec.class);
            channel.pipeline().addAfter(codecName, "ServerHandler", new HandlerServer());
        }

    }

    /**
     * @author abrarsyed
     * This class converts FMLMessage to PacketBase for my system to handle.
     */
    private static final class Codec extends FMLIndexedMessageToMessageCodec<PacketBase> {
        @Override
        public void encodeInto(ChannelHandlerContext ctx, PacketBase packet, ByteBuf target) throws Exception {
            packet.encode(target);
        }

        @Override
        public void decodeInto(ChannelHandlerContext ctx, ByteBuf source, PacketBase packet) {
            System.out.println(source.capacity());
            //source.readByte();
            packet.decode(source.slice());

            if (ctx.channel().attr(NetworkRegistry.CHANNEL_SOURCE).get().isClient()) {
                Minecraft.getMinecraft().addScheduledTask(() -> actionClient(packet));
            } else {
                ServerUtils.mc().addScheduledTask(() -> actionServer(ctx, packet));
            }
        }

        @SideOnly(Side.CLIENT)
        private void actionClient(PacketBase packet) {
            Minecraft mc = Minecraft.getMinecraft();
            packet.actionClient(mc.world, mc.player);
        }

        private void actionServer(ChannelHandlerContext ctx, PacketBase packet) {
            EntityPlayerMP player = ((NetHandlerPlayServer) ctx.channel().attr(NetworkRegistry.NET_HANDLER).get()).player;
            packet.actionServer(player.world, player);
        }

        @Override
        public FMLIndexedMessageToMessageCodec<PacketBase> addDiscriminator(int discriminator, Class<? extends PacketBase> type) {
            // double check it has an empty constructor. or fail early.
            // This is to guard myself against my own stupidity.

            if (!hasEmptyContructor(type)) {
                LogManager.getLogger().log(Level.FATAL, type.getName() + "does not have an empty constructor!");
            }

            return super.addDiscriminator(discriminator, type);
        }

        @SuppressWarnings("rawtypes")
        private static boolean hasEmptyContructor(Class type) {
            try {
                for (Constructor c : type.getConstructors()) {
                    if (c.getParameterTypes().length == 0) {
                        return true;
                    }
                }
            } catch (SecurityException e) {
                // really?
            }

            return false;
        }
    }

    @Sharable
    @SideOnly(Side.CLIENT)
    private static final class HandlerClient extends SimpleChannelInboundHandler<PacketBase> {
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, PacketBase packet) throws Exception {
            Minecraft mc = Minecraft.getMinecraft();
            mc.addScheduledTask(() -> packet.actionClient(mc.world, mc.player));
        }
    }

    @Sharable
    private static final class HandlerServer extends SimpleChannelInboundHandler<PacketBase> {
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, PacketBase packet) throws Exception {
            EntityPlayerMP player = ((NetHandlerPlayServer) ctx.channel().attr(NetworkRegistry.NET_HANDLER).get()).player;
            ServerUtils.mc().addScheduledTask(() -> packet.actionServer(player.world, player));
        }
    }

    // UTIL SENDING METHODS

    public static void sendToServer(PacketBase packet) {
        channels.get(Side.CLIENT).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.TOSERVER);
        channels.get(Side.CLIENT).writeAndFlush(packet);
    }

    public static void sendToPlayer(PacketBase packet, EntityPlayer player) {
        channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.PLAYER);
        channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(player);
        channels.get(Side.SERVER).writeAndFlush(packet);
    }

    public static void sendToAllAround(PacketBase packet, NetworkRegistry.TargetPoint point) {
        channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALLAROUNDPOINT);
        channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(point);
        channels.get(Side.SERVER).writeAndFlush(packet);
    }

    public static void sendToDimension(PacketBase packet, int dimension) {
        channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.DIMENSION);
        channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(dimension);
        channels.get(Side.SERVER).writeAndFlush(packet);
    }

    public static void sendToAll(PacketBase packet) {
        channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALL);
        channels.get(Side.SERVER).writeAndFlush(packet);
    }

    public static Packet toMcPacket(PacketBase packet) {
        return channels.get(FMLCommonHandler.instance().getEffectiveSide()).generatePacketFrom(packet);
    }

    public static void validateAndSend(PacketBase capsule, EntityPlayerMP player) {
        if (!capsule.msg.isDisplayActive(PlayerTracker.INSTANCE.getPlayerSelectedTab(player))) return;

        if (capsule.msg.canPlayerUseCommand(player))
            PacketManager.sendToPlayer(capsule, player);
    }

    public static void sendPacketToAllSwing(PacketBase capsule) {
        for (EntityPlayerMP player : PlayerTracker.INSTANCE.playersSwing)
            PacketManager.validateAndSend(capsule, player);
    }

    public static void sendChatMsg(String msg, EntityPlayerMP player) {
        player.sendMessage(new TextComponentString(msg));
    }

    public static void splitAndSend(Message msg, ArrayList<? extends ISerializable> data, EntityPlayerMP player) {
        int i = 0;
        while (i < data.size()) {
            validateAndSend(new NetDataList(msg, data.subList(i, Math.min(i + 500, data.size()))), player);
            i += 500;
        }
    }

    public static void sendFullUpdate(EntityPlayerMP player) {
        ArrayList<DataEntity> timingEntities = EntityManager.INSTANCE.getWorses(100);
        ArrayList<DataBlockTileEntity> timingTileEnts = TileEntityManager.INSTANCE.getWorses(100);
        ArrayList<StatsChunk> timingChunks = ChunkManager.INSTANCE.getTopChunks(100);
        ArrayList<DataEntityPerClass> timingEntsClass = EntityManager.INSTANCE.getTotalPerClass();
        ArrayList<DataBlockTileEntityPerClass> timingTEsClass = TileEntityManager.INSTANCE.getCumulativeTimingTileEntities();

        DataTiming totalTimeTE = TileEntityManager.INSTANCE.getTotalUpdateTime();
        DataTiming totalTimeEnt = EntityManager.INSTANCE.getTotalUpdateTime();
        DataNetworkTick totalNetwork = new DataNetworkTick().fill();
        DataBlockTick totalWorldTick = new DataBlockTick().fill();

        ArrayList<DataEvent> timingEvents = new ArrayList<DataEvent>();
        HashBasedTable<Class, String, DescriptiveStatistics> eventData = ((ProfilerEvent) ProfilerSection.EVENT_INVOKE.getProfiler()).data;
        HashBasedTable<Class, String, String> eventMod = ((ProfilerEvent) ProfilerSection.EVENT_INVOKE.getProfiler()).dataMod;
        for (Table.Cell<Class, String, DescriptiveStatistics> cell : eventData.cellSet()) {
            timingEvents.add(new DataEvent().fill(cell, eventMod.get(cell.getRowKey(), cell.getColumnKey())));
        }

        ArrayList<DataEvent> timingTicks = new ArrayList<DataEvent>();
        HashBasedTable<Class, String, DescriptiveStatistics> eventTickData = ((ProfilerEvent) ProfilerSection.EVENT_INVOKE.getProfiler()).dataTick;
        HashBasedTable<Class, String, String> eventTickMod = ((ProfilerEvent) ProfilerSection.EVENT_INVOKE.getProfiler()).dataModTick;
        for (Table.Cell<Class, String, DescriptiveStatistics> cell : eventTickData.cellSet()) {
            timingTicks.add(new DataEvent().fill(cell, eventTickMod.get(cell.getRowKey(), cell.getColumnKey())));
        }

        PacketManager.validateAndSend(new NetDataList(Message.LIST_TIMING_HANDLERS, timingTicks), player);
        PacketManager.validateAndSend(new NetDataList(Message.LIST_TIMING_ENTITIES, timingEntities), player);
        PacketManager.validateAndSend(new NetDataList(Message.LIST_TIMING_TILEENTS, timingTileEnts), player);
        PacketManager.validateAndSend(new NetDataList(Message.LIST_TIMING_TILEENTS_PER_CLASS, timingTEsClass), player);
        PacketManager.validateAndSend(new NetDataList(Message.LIST_TIMING_CHUNK, timingChunks), player);
        PacketManager.validateAndSend(new NetDataList(Message.LIST_TIMING_EVENTS, timingEvents), player);
        PacketManager.validateAndSend(new NetDataList(Message.LIST_TIMING_ENTITIES_PER_CLASS, timingEntsClass), player);
        PacketManager.validateAndSend(new NetDataValue(Message.VALUE_TIMING_TILEENTS, totalTimeTE), player);
        PacketManager.validateAndSend(new NetDataValue(Message.VALUE_TIMING_ENTITIES, totalTimeEnt), player);
        PacketManager.validateAndSend(new NetDataValue(Message.VALUE_TIMING_WORLDTICK, totalWorldTick), player);
        PacketManager.validateAndSend(new NetDataValue(Message.VALUE_TIMING_NETWORK, totalNetwork), player);

        PacketManager.validateAndSend(new NetDataValue(Message.STATUS_TIME_LAST_RUN, new SerialLong(Profilers.lastRun)), player);

        PacketManager.validateAndSend(new NetDataValue(Message.STATUS_ACCESS_LEVEL, new SerialInt(PlayerTracker.INSTANCE.getPlayerAccessLevel(player).ordinal())), player);

        // This portion is to get the proper filtered amounts depending on the player preferences.
        String name = player.getGameProfile().getName();
        boolean filtered = false;
        if (PlayerTracker.INSTANCE.filteredAmount.containsKey(name))
            filtered = PlayerTracker.INSTANCE.filteredAmount.get(name);
        ArrayList<AmountHolder> amountEntities = EntityManager.INSTANCE.getCumulativeEntities(filtered);

        // Here we send a full update to the player
        PacketManager.validateAndSend(new NetDataList(Message.LIST_AMOUNT_ENTITIES, amountEntities), player);
    }
}
