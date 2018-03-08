package mcp.mobius.opis.profiler.impl;

import mcp.mobius.opis.profiler.Clock;
import mcp.mobius.opis.profiler.IProfiler.IProfilerNone;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

/**
 * Created by covers1624 on 8/03/18.
 */
public class ProfilerServerTick implements IProfilerNone {

    private Clock clock = Clock.createClock();
    public DescriptiveStatistics data = new DescriptiveStatistics(20);

    @Override
    public void reset() {

    }

    @Override
    public void start() {
        clock.start();
    }

    @Override
    public void stop() {
        clock.stop();
        data.addValue(clock.getDelta());
    }
}
