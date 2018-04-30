package mcp.mobius.opis.data.holders.newtypes;

import io.netty.buffer.ByteBuf;
import mcp.mobius.opis.data.holders.ISerializable;

public class DataEntityPerClass implements Comparable, ISerializable {

    public int nents;
    public CachedString name;
    public DataTiming update;

    public DataEntityPerClass() {
    }

    public DataEntityPerClass(String name) {
        this.name = new CachedString(name);
        nents = 0;
        update = new DataTiming();
    }

    public DataEntityPerClass add(Double timing) {
        nents += 1;
        update.timing += timing;

        return this;
    }

    @Override
    public void writeToStream(ByteBuf stream) {
        stream.writeInt(nents);
        name.writeToStream(stream);
        update.writeToStream(stream);
    }

    public static DataEntityPerClass readFromStream(ByteBuf stream) {
        DataEntityPerClass retVal = new DataEntityPerClass();
        retVal.nents = stream.readInt();
        retVal.name = CachedString.readFromStream(stream);
        retVal.update = DataTiming.readFromStream(stream);
        return retVal;
    }

    @Override
    public int compareTo(Object o) {
        return update.compareTo(((DataEntityPerClass) o).update);
    }

}
