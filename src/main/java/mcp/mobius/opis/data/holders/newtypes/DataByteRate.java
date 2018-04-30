package mcp.mobius.opis.data.holders.newtypes;

import io.netty.buffer.ByteBuf;
import mcp.mobius.opis.data.holders.ISerializable;

public class DataByteRate implements Comparable, ISerializable {

    public Long size;    // Size is stored in byte
    public int interval;

    //public DataByteRate(int interval){ this.size = 0L; this.interval = interval;}
    public DataByteRate(long size, int interval) {
        this.size = size;
        this.interval = interval;
    }

    @Override
    public int compareTo(Object o) {
        return ((DataByteRate) o).size.compareTo(size);    // Reverse order ! Put higher values FIRST
    }

    public void reset() {
        size = 0L;
    }

    public String toString() {

        if (size >= 1024 && size < (1024 * 1024)) {
            return String.format("%.3f KiB/s", size / 1024.0 / interval);
        }

        if (size >= (1024 * 1024)) {
            return String.format("%.3f MiB/s", size / 1024.0 / 1024.0 / interval);
        }

        return String.format("%4d   B/s", size / interval);
    }

    public void writeToStream(ByteBuf stream) {
        stream.writeLong(size);
        stream.writeInt(interval);
    }

    public static DataByteRate readFromStream(ByteBuf stream) {
        return new DataByteRate(stream.readLong(), stream.readInt());
    }
}
