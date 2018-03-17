package mcp.mobius.opis.data.holders.basetypes;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import io.netty.buffer.ByteBuf;
import mcp.mobius.opis.data.holders.ISerializable;

public class SerialInt implements ISerializable {

    public int value = 0;

    public SerialInt(int value) {
        this.value = value;
    }

    @Override
    public void writeToStream(ByteBuf stream) {
        stream.writeInt(this.value);
    }

    public static SerialInt readFromStream(ByteBuf stream) {
        return new SerialInt(stream.readInt());
    }

}
