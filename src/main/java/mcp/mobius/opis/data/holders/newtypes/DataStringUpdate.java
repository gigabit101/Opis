package mcp.mobius.opis.data.holders.newtypes;

import io.netty.buffer.ByteBuf;
import mcp.mobius.opis.data.holders.ISerializable;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class DataStringUpdate implements ISerializable {

    public String str;
    public int index;

    public DataStringUpdate() {
    }

    public DataStringUpdate(String str, int index) {
        this.str = str;
        this.index = index;
    }

    @Override
    public void writeToStream(ByteBuf stream) {
        ByteBufUtils.writeUTF8String(stream, str);
        stream.writeInt(index);

    }

    public static DataStringUpdate readFromStream(ByteBuf stream) {
        return new DataStringUpdate(ByteBufUtils.readUTF8String(stream), stream.readInt());
    }

}
