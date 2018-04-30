package mcp.mobius.opis.data.holders.basetypes;

import io.netty.buffer.ByteBuf;
import mcp.mobius.opis.data.holders.ISerializable;

public class SerialLong implements ISerializable {

    public long value = 0;

    public SerialLong(long value) {
        this.value = value;
    }

    @Override
    public void writeToStream(ByteBuf stream) {
        stream.writeLong(value);
    }

    public static SerialLong readFromStream(ByteBuf stream) {
        return new SerialLong(stream.readLong());
    }

}
