package mcp.mobius.opis.profiler.impl;

import mcp.mobius.opis.profiler.Clock;
import mcp.mobius.opis.profiler.IClock;
import mcp.mobius.opis.profiler.IProfiler.IProfilerSingle;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by covers1624 on 7/03/18.
 */
public abstract class AbstractProfilerWorldTick implements IProfilerSingle<World> {

    private IClock clock = Clock.createClock();
    public Map<Integer, DescriptiveStatistics> data = new HashMap<>();

    protected abstract DescriptiveStatistics createStats();

    @Override
    public void reset() {
        data.clear();
    }

    @Override
    public void start(World world) {
        int dim = world.provider.getDimension();
        if (DimensionManager.getWorld(dim).isRemote) {
            return;
        }
        if (!data.containsKey(dim)) {
            data.put(dim, createStats());
        }
        clock.start();
    }

    @Override
    public void stop(World world) {
        int dim = world.provider.getDimension();
        if (DimensionManager.getWorld(dim).isRemote) {
            return;
        }
        clock.stop();
        data.get(dim).addValue(clock.getDelta());
    }
}
