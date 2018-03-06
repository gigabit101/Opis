package mcp.mobius.opis.profiler.impl;

import mcp.mobius.opis.Opis;
import mcp.mobius.opis.profiler.Clock;
import mcp.mobius.opis.profiler.IProfiler.IProfilerSingle;
import net.minecraft.entity.Entity;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * Created by covers1624 on 6/03/18.
 */
public class ProfilerEntityUpdate implements IProfilerSingle<Entity> {

    private Clock clock = Clock.createClock();
    public Map<Entity, DescriptiveStatistics> data = new WeakHashMap<>();

    @Override
    public void reset() {
        data.clear();
    }

    @Override
    public void start(Entity entity) {
        if (entity.world.isRemote) {
            return;
        }
        if (!data.containsKey(entity)) {
            data.put(entity, new DescriptiveStatistics());
        }
        clock.start();
    }

    @Override
    public void stop(Entity entity) {
        if (entity.world.isRemote) {
            return;
        }
        clock.stop();
        try {
            data.get(entity).addValue(clock.getDelta());
        } catch (Exception e) {
            Opis.log.warn(String.format("Error while profiling entity %s.", entity));
        }
    }
}
