package mcp.mobius.opis.data.holders;

import com.google.common.io.ByteArrayDataOutput;
import io.netty.buffer.ByteBuf;

public interface ISerializable {
    void writeToStream(ByteBuf stream);
    //Object readFromStream(DataInputStream stream) throws IOException;
}
