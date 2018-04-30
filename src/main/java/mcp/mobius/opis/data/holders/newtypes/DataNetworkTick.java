//package mcp.mobius.opis.data.holders.newtypes;
//
//import com.google.common.io.ByteArrayDataInput;
//import com.google.common.io.ByteArrayDataOutput;
//import io.netty.buffer.ByteBuf;
//import mcp.mobius.opis.data.holders.ISerializable;
//import mcp.mobius.opis.data.profilers.ProfilerNetworkTick;
//import mcp.mobius.opis.profiler.ProfilerSection;
//import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
//
//public class DataNetworkTick implements ISerializable, Comparable {
//    public DataTiming update;
//
//    public DataNetworkTick fill() {
//
//        DescriptiveStatistics data = ((ProfilerNetworkTick) (ProfilerSection.NETWORK_TICK.getProfiler())).data;
//        this.update = new DataTiming(data.getN() != 0 ? data.getGeometricMean() : 0.0D);
//
//        return this;
//    }
//
//    @Override
//    public void writeToStream(ByteBuf stream) {
//        this.update.writeToStream(stream);
//    }
//
//    public static DataNetworkTick readFromStream(ByteBuf stream) {
//        DataNetworkTick retVal = new DataNetworkTick();
//        retVal.update = DataTiming.readFromStream(stream);
//        return retVal;
//    }
//
//    @Override
//    public int compareTo(Object o) {
//        return this.update.compareTo(((DataNetworkTick) o).update);
//    }
//}
