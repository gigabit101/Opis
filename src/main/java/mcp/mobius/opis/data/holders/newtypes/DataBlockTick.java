package mcp.mobius.opis.data.holders.newtypes;

import io.netty.buffer.ByteBuf;
import mcp.mobius.opis.data.holders.ISerializable;
import mcp.mobius.opis.profiler.Profilers;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.util.HashMap;
import java.util.Map;

//TODO, Rename, WorldServer not BlockTick
public class DataBlockTick implements ISerializable {

    public HashMap<Integer, DataTiming> perdim = new HashMap<>();
    public DataTiming total;

    public DataBlockTick fill() {
        total = new DataTiming();
        Map<Integer, DescriptiveStatistics> data = Profilers.WORLD_SERVER_TICK.get().data;

        for (Integer dim : data.keySet()) {
            perdim.put(dim, new DataTiming(data.get(dim).getGeometricMean()));
            total.timing += data.get(dim).getGeometricMean();
        }

        return this;
    }

    @Override
    public void writeToStream(ByteBuf stream) {
        stream.writeShort(perdim.size());
        for (Integer key : perdim.keySet()) {
            stream.writeInt(key);
            perdim.get(key).writeToStream(stream);
        }
        total.writeToStream(stream);
    }

    public static DataBlockTick readFromStream(ByteBuf stream) {
        DataBlockTick retVal = new DataBlockTick();
        int nkeys = stream.readShort();
        for (int i = 0; i < nkeys; i++) {
            retVal.perdim.put(stream.readInt(), DataTiming.readFromStream(stream));
        }
        retVal.total = DataTiming.readFromStream(stream);
        return retVal;
    }
}
