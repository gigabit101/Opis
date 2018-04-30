package mcp.mobius.opis.data.holders.newtypes;

import io.netty.buffer.ByteBuf;
import mcp.mobius.opis.data.holders.ISerializable;
import mcp.mobius.opis.data.holders.basetypes.CoordinatesChunk;
import mcp.mobius.opis.data.managers.EntityManager;
import mcp.mobius.opis.data.managers.TileEntityManager;

import java.util.ArrayList;
import java.util.List;

public class DataChunk implements ISerializable, Comparable {

    int nentities;
    int ntileents;
    DataTiming update;
    CoordinatesChunk chunk;

    public DataChunk fill(CoordinatesChunk chunk) {
        this.chunk = chunk;
        List<DataBlockTileEntity> tileEnts = TileEntityManager.INSTANCE.getTileEntitiesInChunk(this.chunk);
        ArrayList<DataEntity> entities = EntityManager.INSTANCE.getEntitiesInChunk(this.chunk);

        nentities = entities.size();
        ntileents = tileEnts.size();
        double totalUpdate = 0.0D;
        for (DataBlockTileEntity tileent : tileEnts) {
            totalUpdate += tileent.update.timing;
        }

        for (DataEntity ent : entities) {
            totalUpdate += ent.update.timing;
        }

        update = new DataTiming(totalUpdate);
        return this;
    }

    @Override
    public void writeToStream(ByteBuf stream) {
        stream.writeInt(nentities);
        stream.writeInt(ntileents);
        update.writeToStream(stream);
        chunk.writeToStream(stream);
    }

    public static DataChunk readFromStream(ByteBuf stream) {
        DataChunk retVal = new DataChunk();
        retVal.nentities = stream.readInt();
        retVal.ntileents = stream.readInt();
        retVal.update = DataTiming.readFromStream(stream);
        retVal.chunk = CoordinatesChunk.readFromStream(stream);
        return retVal;
    }

    @Override
    public int compareTo(Object o) {
        return update.compareTo(((DataEntity) o).update);
    }
}
