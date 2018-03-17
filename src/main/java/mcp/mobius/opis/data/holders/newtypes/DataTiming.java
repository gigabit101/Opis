package mcp.mobius.opis.data.holders.newtypes;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import io.netty.buffer.ByteBuf;
import mcp.mobius.opis.data.holders.ISerializable;

public class DataTiming implements Comparable, ISerializable {
    public Double timing;

    public DataTiming() {
        this.timing = 0.0D;
    }

    public DataTiming(double timing) {
        this.timing = timing;
    }

    @Override
    public int compareTo(Object o) {
        return ((DataTiming) o).timing.compareTo(this.timing);    // Reverse order ! Put higher values FIRST
    }

    public DataTimingMillisecond asMillisecond() {
        return new DataTimingMillisecond(this.timing);
    }

    public String toString() {
        return String.format("%.3f µs", this.timing / 1000.0);
    }

    public void writeToStream(ByteBuf stream) {
        stream.writeDouble(this.timing);
    }

    public static DataTiming readFromStream(ByteBuf stream) {
        return new DataTiming(stream.readDouble());
    }
}
