package mcp.mobius.opis.data.holders.basetypes;

import io.netty.buffer.ByteBuf;
import mcp.mobius.opis.data.holders.ISerializable;

public class TargetEntity implements ISerializable {

    public int entityID = 0;
    public int dim = 0;

    public TargetEntity(int id, int dim) {
        entityID = id;
        this.dim = dim;
    }

    @Override
    public void writeToStream(ByteBuf stream) {
        stream.writeInt(entityID);
        stream.writeInt(dim);
    }

    public static TargetEntity readFromStream(ByteBuf stream) {
        return new TargetEntity(stream.readInt(), stream.readInt());
    }
}
