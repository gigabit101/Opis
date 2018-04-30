package mcp.mobius.opis.data.holders;

import io.netty.buffer.ByteBuf;

public interface ISerializable {

    void writeToStream(ByteBuf stream);
    //Object readFromStream(DataInputStream stream) throws IOException;
}
