package mcp.mobius.opis.data.holders.newtypes;

import io.netty.buffer.ByteBuf;
import mcp.mobius.opis.data.holders.ISerializable;

public class DataTiming implements Comparable, ISerializable {

    public Double timing;

    public DataTiming() {
        timing = 0.0D;
    }

    public DataTiming(double timing) {
        this.timing = timing;
    }

    @Override
    public int compareTo(Object o) {
        return ((DataTiming) o).timing.compareTo(timing);    // Reverse order ! Put higher values FIRST
    }

    public DataTimingMillisecond asMillisecond() {
        return new DataTimingMillisecond(timing);
    }

    public String toString() {
        return String.format("%.3f µs", timing / 1000.0);
    }

    public void writeToStream(ByteBuf stream) {
        stream.writeDouble(timing);
    }

    public static DataTiming readFromStream(ByteBuf stream) {
        return new DataTiming(stream.readDouble());
    }
}
