package mcp.mobius.opis.data.holders.newtypes;

import io.netty.buffer.ByteBuf;
import mcp.mobius.opis.data.holders.ISerializable;

public class DataAmountRate implements Comparable, ISerializable {

    public Long size;    // Size is stored in byte
    public int interval;

    //public DataByteRate(int interval){ this.size = 0L; this.interval = interval;}
    public DataAmountRate(long size, int interval) {
        this.size = size;
        this.interval = interval;
    }

    @Override
    public int compareTo(Object o) {
        return ((DataAmountRate) o).size.compareTo(size);    // Reverse order ! Put higher values FIRST
    }

    public void reset() {
        size = 0L;
    }

    public String toString() {
        return String.format("%.2f packet/s", (double) size / (double) interval);
    }

    public void writeToStream(ByteBuf stream) {
        stream.writeLong(size);
        stream.writeInt(interval);
    }

    public static DataAmountRate readFromStream(ByteBuf stream) {
        return new DataAmountRate(stream.readLong(), stream.readInt());
    }
}
