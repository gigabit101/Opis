package mcp.mobius.opis.data.holders.basetypes;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import io.netty.buffer.ByteBuf;
import mcp.mobius.opis.data.holders.ISerializable;

public class SerialFloat implements ISerializable {

    public float value = 0;

    public SerialFloat(float value) {
        this.value = value;
    }

    @Override
    public void writeToStream(ByteBuf stream) {
        stream.writeFloat(this.value);
    }

    public static SerialFloat readFromStream(ByteBuf stream) {
        return new SerialFloat(stream.readFloat());
    }

}
