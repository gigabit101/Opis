package mcp.mobius.opis.data.holders.newtypes;

import io.netty.buffer.ByteBuf;
import mcp.mobius.opis.data.holders.ISerializable;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;

public class DataPacket250 implements ISerializable {

    public CachedString channel;
    public DataByteSize size;
    public DataByteRate rate;
    public DataAmountRate amount;

    public DataPacket250() {
    }

    public DataPacket250(String channel) {
        this.channel = new CachedString(channel);
        size = new DataByteSize(0);
        rate = new DataByteRate(0, 5);
        amount = new DataAmountRate(0, 5);
    }

    public DataPacket250 fill(FMLProxyPacket packet, int pktsize) {
        size.size += pktsize;
        rate.size += pktsize;
        amount.size += 1;
        return this;
    }

    public void start() {
        rate.reset();
        amount.reset();
    }

    @Override
    public void writeToStream(ByteBuf stream) {
        channel.writeToStream(stream);
        size.writeToStream(stream);
        rate.writeToStream(stream);
        amount.writeToStream(stream);
    }

    public static DataPacket250 readFromStream(ByteBuf stream) {
        DataPacket250 retVal = new DataPacket250();
        retVal.channel = CachedString.readFromStream(stream);
        retVal.size = DataByteSize.readFromStream(stream);
        retVal.rate = DataByteRate.readFromStream(stream);
        retVal.amount = DataAmountRate.readFromStream(stream);

        return retVal;
    }
}
