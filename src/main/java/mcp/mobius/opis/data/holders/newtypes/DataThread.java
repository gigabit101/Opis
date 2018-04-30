package mcp.mobius.opis.data.holders.newtypes;

import io.netty.buffer.ByteBuf;
import mcp.mobius.opis.data.holders.ISerializable;

public class DataThread implements ISerializable {

    public CachedString name;
    public CachedString clzz;

    public DataThread fill(Thread thread) {
        name = new CachedString(thread.getName());
        clzz = new CachedString(thread.getClass().getSimpleName());
        return this;
    }

    public void writeToStream(ByteBuf stream) {
        name.writeToStream(stream);
        clzz.writeToStream(stream);

    }

    public static DataThread readFromStream(ByteBuf stream) {
        DataThread retVal = new DataThread();
        retVal.name = CachedString.readFromStream(stream);
        retVal.clzz = CachedString.readFromStream(stream);
        return retVal;
    }

}
