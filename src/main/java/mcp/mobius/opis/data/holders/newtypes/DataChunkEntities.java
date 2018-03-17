package mcp.mobius.opis.data.holders.newtypes;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import io.netty.buffer.ByteBuf;
import mcp.mobius.opis.data.holders.ISerializable;
import mcp.mobius.opis.data.holders.basetypes.CoordinatesChunk;

public class DataChunkEntities implements ISerializable {

    public int entities;
    public CoordinatesChunk chunk;

    public DataChunkEntities(CoordinatesChunk chunk, int entities) {
        this.chunk = chunk;
        this.entities = entities;
    }

    @Override
    public void writeToStream(ByteBuf stream) {
        this.chunk.writeToStream(stream);
        stream.writeInt(this.entities);
    }

    public static DataChunkEntities readFromStream(ByteBuf stream) {
        return new DataChunkEntities(CoordinatesChunk.readFromStream(stream), stream.readInt());
    }

}
