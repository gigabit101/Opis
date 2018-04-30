package mcp.mobius.opis.data.holders.newtypes;

import io.netty.buffer.ByteBuf;
import mcp.mobius.opis.data.holders.ISerializable;
import net.minecraft.network.Packet;

public class DataPacket implements ISerializable {

    public int id;
    public DataByteSize size;
    public DataByteRate rate;
    public DataAmountRate amount;
    public CachedString type;

    public DataPacket() {
    }

    public DataPacket(Packet packet) {
        type = new CachedString(packet.getClass().getSimpleName());
        size = new DataByteSize(0);
        rate = new DataByteRate(0, 5);
        amount = new DataAmountRate(0, 5);
    }

    public DataPacket fill(Packet packet, int pktsize) {
        /*
		PacketBuffer buff = new PacketBuffer(Unpooled.buffer());
		int pktsize = 0;
		try{
			packet.writePacketData(buff);
			pktsize = buff.readableBytes();
		} catch (Exception e){
			
		}
		*/
        size.size += pktsize;
        rate.size += pktsize;
        amount.size += 1;
        return this;
    }

    public void startInterval() {
        rate.reset();
        amount.reset();
    }

    @Override
    public void writeToStream(ByteBuf stream) {
        size.writeToStream(stream);
        rate.writeToStream(stream);
        amount.writeToStream(stream);
        type.writeToStream(stream);
    }

    public static DataPacket readFromStream(ByteBuf stream) {
        DataPacket retVal = new DataPacket();
        retVal.size = DataByteSize.readFromStream(stream);
        retVal.rate = DataByteRate.readFromStream(stream);
        retVal.amount = DataAmountRate.readFromStream(stream);
        retVal.type = CachedString.readFromStream(stream);

        return retVal;
    }
}
