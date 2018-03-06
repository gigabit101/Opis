package mcp.mobius.opis.profiler.impl;

import mcp.mobius.opis.profiler.Clock;
import mcp.mobius.opis.profiler.IProfiler.IProfilerSingle;
import net.minecraftforge.common.DimensionManager;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by covers1624 on 6/03/18.
 */
public class ProfilerWorldServerTick implements IProfilerSingle<Integer> {

    private Clock clock = Clock.createClock();
    public Map<Integer, DescriptiveStatistics> data = new HashMap<>();

    @Override
    public void reset() {
        data.clear();
    }

    @Override
    public void start(Integer dim) {
        if(DimensionManager.getWorld(dim).isRemote) {
            return;
        }
        if(!data.containsKey(dim)) {
            data.put(dim, new DescriptiveStatistics());
        }
        clock.start();
    }

    @Override
    public void stop(Integer dim) {
        if(DimensionManager.getWorld(dim).isRemote) {
            return;
        }
        clock.stop();
        data.get(dim).addValue(clock.getDelta());
    }
}
