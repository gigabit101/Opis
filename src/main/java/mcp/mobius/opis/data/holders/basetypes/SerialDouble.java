package mcp.mobius.opis.data.holders.basetypes;

import io.netty.buffer.ByteBuf;
import mcp.mobius.opis.data.holders.ISerializable;

public class SerialDouble implements ISerializable {

    public double value = 0;

    public SerialDouble(double value) {
        this.value = value;
    }

    @Override
    public void writeToStream(ByteBuf stream) {
        stream.writeDouble(value);
    }

    public static SerialDouble readFromStream(ByteBuf stream) {
        return new SerialDouble(stream.readDouble());
    }

}
