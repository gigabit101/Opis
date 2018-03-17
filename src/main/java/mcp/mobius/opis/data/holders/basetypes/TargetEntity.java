package mcp.mobius.opis.data.holders.basetypes;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import io.netty.buffer.ByteBuf;
import mcp.mobius.opis.data.holders.ISerializable;

public class TargetEntity implements ISerializable {

    public int entityID = 0;
    public int dim = 0;

    public TargetEntity(int id, int dim) {
        this.entityID = id;
        this.dim = dim;
    }

    @Override
    public void writeToStream(ByteBuf stream) {
        stream.writeInt(this.entityID);
        stream.writeInt(this.dim);
    }

    public static TargetEntity readFromStream(ByteBuf stream) {
        return new TargetEntity(stream.readInt(), stream.readInt());
    }
}
