package mcp.mobius.opis.data.holders.newtypes;

import io.netty.buffer.ByteBuf;
import mcp.mobius.opis.data.holders.ISerializable;

public class DataBlockTileEntityPerClass implements ISerializable, Comparable {

    public int id;
    public int meta;
    public int amount;
    public DataTiming update;

    public DataBlockTileEntityPerClass() {
    }

    public DataBlockTileEntityPerClass(int id, int meta) {
        this.id = id;
        this.meta = meta;
        amount = 0;
        update = new DataTiming();
    }

    public DataBlockTileEntityPerClass add() {
        amount += 1;
        return this;
    }

    public DataBlockTileEntityPerClass add(Double timing) {
        amount += 1;
        update.timing += timing;

        return this;
    }

    public DataBlockTileEntityPerClass add(int amount, Double timing) {
        this.amount += amount;
        update.timing += timing;

        return this;
    }

    @Override
    public void writeToStream(ByteBuf stream) {
        stream.writeInt(id);
        stream.writeInt(meta);
        stream.writeInt(amount);
        update.writeToStream(stream);
    }

    public static DataBlockTileEntityPerClass readFromStream(ByteBuf stream) {
        DataBlockTileEntityPerClass retVal = new DataBlockTileEntityPerClass();
        retVal.id = stream.readInt();
        retVal.meta = stream.readInt();
        retVal.amount = stream.readInt();
        retVal.update = DataTiming.readFromStream(stream);
        return retVal;
    }

    @Override
    public int compareTo(Object o) {
        return update.compareTo(((DataBlockTileEntityPerClass) o).update);
    }

}
