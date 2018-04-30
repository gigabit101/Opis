package mcp.mobius.opis.data.holders.newtypes;

import io.netty.buffer.ByteBuf;
import mcp.mobius.opis.data.holders.ISerializable;
import mcp.mobius.opis.data.holders.basetypes.CoordinatesBlock;
import mcp.mobius.opis.data.managers.EntityManager;
import mcp.mobius.opis.profiler.Profilers;
import net.minecraft.entity.Entity;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.util.Map;

public class DataEntity implements ISerializable, Comparable {

    public int eid;
    public long npoints;
    public CachedString name;
    public CoordinatesBlock pos;
    public DataTiming update;

    public DataEntity fill(Entity entity) {

        eid = entity.getEntityId();
        name = new CachedString(EntityManager.INSTANCE.getEntityName(entity, false));
        pos = new CoordinatesBlock(entity);

        Map<Entity, DescriptiveStatistics> data = Profilers.ENTITY_UPDATE.get().data;
        update = new DataTiming(data.containsKey(entity) ? data.get(entity).getGeometricMean() : 0.0D);
        npoints = data.containsKey(entity) ? data.get(entity).getN() : 0;

        return this;
    }

    @Override
    public void writeToStream(ByteBuf stream) {
        stream.writeInt(eid);
        name.writeToStream(stream);
        pos.writeToStream(stream);
        update.writeToStream(stream);
        stream.writeLong(npoints);
    }

    public static DataEntity readFromStream(ByteBuf stream) {
        DataEntity retVal = new DataEntity();
        retVal.eid = stream.readInt();
        retVal.name = CachedString.readFromStream(stream);
        retVal.pos = CoordinatesBlock.readFromStream(stream);
        retVal.update = DataTiming.readFromStream(stream);
        retVal.npoints = stream.readLong();
        return retVal;
    }

    @Override
    public int compareTo(Object o) {
        return update.compareTo(((DataEntity) o).update);
    }
}
