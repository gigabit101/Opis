package mcp.mobius.opis.profiler.impl;

import io.netty.channel.ChannelHandlerContext;
import mcp.mobius.opis.data.holders.newtypes.DataPacket;
import mcp.mobius.opis.profiler.IProfiler;
import net.minecraft.network.Packet;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by covers1624 on 9/03/18.
 */
public class ProfilerVanillaPacket implements IProfiler<Packet<?>, ChannelHandlerContext, Integer, Object> {

    public long dataAmount = 0;
    public Map<String, DataPacket> data = new HashMap<>();

    @Override
    public void reset() {
        dataAmount = 0;
    }

    @Override
    public void start() {
        data.values().forEach(DataPacket::startInterval);
    }

    @Override
    public void start(Packet<?> packet, ChannelHandlerContext ctx, Integer capacity) {
        if (packet != null && FMLCommonHandler.instance().getEffectiveSide().isServer()) {
            if (!(packet instanceof FMLProxyPacket)) {
                dataAmount += capacity;
                data.computeIfAbsent(packet.getClass().getSimpleName(), s -> new DataPacket(packet)).fill(packet, capacity);
            }
        }
    }
}
