package mcp.mobius.opis.data.holders.basetypes;

import io.netty.buffer.ByteBuf;
import mcp.mobius.opis.data.holders.ISerializable;

public class SerialInt implements ISerializable {

    public int value = 0;

    public SerialInt(int value) {
        this.value = value;
    }

    @Override
    public void writeToStream(ByteBuf stream) {
        stream.writeInt(value);
    }

    public static SerialInt readFromStream(ByteBuf stream) {
        return new SerialInt(stream.readInt());
    }

}
