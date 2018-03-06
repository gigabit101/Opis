package mcp.mobius.opis.data.holders.newtypes;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
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

        this.eid = entity.getEntityId();
        this.name = new CachedString(EntityManager.INSTANCE.getEntityName(entity, false));
        this.pos = new CoordinatesBlock(entity);

        Map<Entity, DescriptiveStatistics> data = Profilers.ENTITY_UPDATE.get().data;
        this.update = new DataTiming(data.containsKey(entity) ? data.get(entity).getGeometricMean() : 0.0D);
        this.npoints = data.containsKey(entity) ? data.get(entity).getN() : 0;

        return this;
    }

    @Override
    public void writeToStream(ByteArrayDataOutput stream) {
        stream.writeInt(this.eid);
        this.name.writeToStream(stream);
        this.pos.writeToStream(stream);
        this.update.writeToStream(stream);
        stream.writeLong(this.npoints);
    }

    public static DataEntity readFromStream(ByteArrayDataInput stream) {
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
        return this.update.compareTo(((DataEntity) o).update);
    }
}
