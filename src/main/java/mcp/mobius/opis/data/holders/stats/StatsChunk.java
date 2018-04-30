package mcp.mobius.opis.data.holders.stats;

import io.netty.buffer.ByteBuf;
import mcp.mobius.opis.data.holders.basetypes.CoordinatesChunk;

public class StatsChunk extends StatAbstract {

    public int tileEntities = 0;
    public int entities = 0;

    public StatsChunk() {
    }

    public StatsChunk(CoordinatesChunk chunk) {
        this.chunk = chunk;
        coord = chunk.asCoordinatesBlock();
    }

    public StatsChunk(CoordinatesChunk chunk, int tileEntities, int entities) {
        this.chunk = chunk;
        coord = chunk.asCoordinatesBlock();
        this.tileEntities = tileEntities;
        this.entities = entities;
    }

    public StatsChunk(CoordinatesChunk chunk, int tileEntities, int entities, double time) {
        this.chunk = chunk;
        coord = chunk.asCoordinatesBlock();
        this.tileEntities = tileEntities;
        this.entities = entities;
        setDataSum(time);
    }

    public void addTileEntity() {
        tileEntities += 1;
    }

    public void addEntity() {
        entities += 1;
    }

    @Override
    public void writeToStream(ByteBuf stream) {
        chunk.writeToStream(stream);
        stream.writeInt(tileEntities);
        stream.writeInt(entities);
        stream.writeDouble(getDataSum());
    }

    public static StatsChunk readFromStream(ByteBuf stream) {
        CoordinatesChunk chunk = CoordinatesChunk.readFromStream(stream);
        StatsChunk chunkStats = new StatsChunk(chunk, stream.readInt(), stream.readInt());
        chunkStats.setDataSum(stream.readDouble());
        return chunkStats;
    }

    @Override
    public int compareTo(Object o) {
        double value = ((StatAbstract) o).getDataSum() - getDataSum();
        if (value > 0) {
            return 1;
        }
        if (value < 0) {
            return -1;
        }
        return 0;
    }

    public String toString() {
        return String.format("%.3f µs", getDataSum() / 1000.0);
    }
}
