package mcp.mobius.opis.data.holders.newtypes;

import mcp.mobius.opis.data.holders.basetypes.CoordinatesBlock;
import mcp.mobius.opis.data.managers.EntityManager;
import mcp.mobius.opis.data.profilers.ProfilerRenderEntity;
import mcp.mobius.opis.profiler.ProfilerSection;
import net.minecraft.entity.Entity;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.util.WeakHashMap;

public class DataEntityRender extends DataEntity {

    public DataEntityRender fill(Entity entity) {

        eid = entity.getEntityId();
        name = new CachedString(EntityManager.INSTANCE.getEntityName(entity, false));
        pos = new CoordinatesBlock(entity);

        WeakHashMap<Entity, DescriptiveStatistics> data = ((ProfilerRenderEntity) ProfilerSection.RENDER_ENTITY.getProfiler()).data;
        update = new DataTiming(data.containsKey(entity) ? data.get(entity).getGeometricMean() : 0.0D);
        npoints = data.containsKey(entity) ? data.get(entity).getN() : 0;

        return this;
    }

}
