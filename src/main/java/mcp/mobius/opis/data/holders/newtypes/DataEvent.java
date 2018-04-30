package mcp.mobius.opis.data.holders.newtypes;

import io.netty.buffer.ByteBuf;
import mcp.mobius.opis.data.holders.ISerializable;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public class DataEvent implements ISerializable, Comparable {

    public CachedString event;
    public CachedString package_;
    public CachedString mod;
    public long nCalls;
    public DataTiming update;

    public DataEvent fill(Class clazz, String pkg, String modName, DescriptiveStatistics stats) {
        package_ = new CachedString(pkg);
        String name = clazz.getName();
        event = new CachedString(name.substring(name.lastIndexOf(".") + 1));//TODO this removed "net.minecraftforge.event.", why..
        nCalls = stats.getN();
        mod = new CachedString(modName);
        update = new DataTiming(stats.getGeometricMean());
        return this;
    }

    @Override
    public void writeToStream(ByteBuf stream) {
        event.writeToStream(stream);
        package_.writeToStream(stream);
        update.writeToStream(stream);
        mod.writeToStream(stream);
        stream.writeLong(nCalls);
    }

    public static DataEvent readFromStream(ByteBuf stream) {
        DataEvent retVal = new DataEvent();
        retVal.event = CachedString.readFromStream(stream);
        retVal.package_ = CachedString.readFromStream(stream);
        retVal.update = DataTiming.readFromStream(stream);
        retVal.mod = CachedString.readFromStream(stream);
        retVal.nCalls = stream.readLong();
        return retVal;
    }

    @Override
    public int compareTo(Object o) {
        return update.compareTo(((DataEvent) o).update);
    }
}
