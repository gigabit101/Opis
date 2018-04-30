package mcp.mobius.opis.data.holders.newtypes;

import io.netty.buffer.ByteBuf;
import mcp.mobius.opis.data.holders.ISerializable;

public class DataError implements ISerializable {

    @Override
    public void writeToStream(ByteBuf stream) {
    }

    public static DataError readFromStream(ByteBuf stream) {
        return new DataError();
    }
}
