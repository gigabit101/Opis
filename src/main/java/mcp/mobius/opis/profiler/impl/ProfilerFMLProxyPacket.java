package mcp.mobius.opis.profiler.impl;

import io.netty.buffer.ByteBuf;
import mcp.mobius.opis.data.holders.newtypes.DataPacket250;
import mcp.mobius.opis.profiler.IProfiler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by covers1624 on 11/03/18.
 */
public class ProfilerFMLProxyPacket implements IProfiler<FMLProxyPacket, Object, Object, Object> {

    public long dataAmount = 0;
    public Map<String, DataPacket250> data = new HashMap<>();

    @Override
    public void reset() {
        dataAmount = 0;
    }

    @Override
    public void start() {
        data.values().forEach(DataPacket250::start);
    }

    @Override
    public void start(FMLProxyPacket packet) {
        if (FMLCommonHandler.instance().getEffectiveSide().isServer()) {
            String channel = packet.channel();
            ByteBuf payload = packet.payload();
            int size = payload.capacity();
            dataAmount += size;
            data.computeIfAbsent(channel, DataPacket250::new).fill(packet, size);
        }
    }
}
