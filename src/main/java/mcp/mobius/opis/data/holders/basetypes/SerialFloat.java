package mcp.mobius.opis.data.holders.basetypes;

import io.netty.buffer.ByteBuf;
import mcp.mobius.opis.data.holders.ISerializable;

public class SerialFloat implements ISerializable {

    public float value = 0;

    public SerialFloat(float value) {
        this.value = value;
    }

    @Override
    public void writeToStream(ByteBuf stream) {
        stream.writeFloat(value);
    }

    public static SerialFloat readFromStream(ByteBuf stream) {
        return new SerialFloat(stream.readFloat());
    }

}
