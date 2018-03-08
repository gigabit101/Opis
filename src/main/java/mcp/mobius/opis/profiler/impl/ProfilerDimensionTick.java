package mcp.mobius.opis.profiler.impl;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

/**
 * Created by covers1624 on 7/03/18.
 */
public class ProfilerDimensionTick extends AbstractProfilerWorldTick {

    @Override
    protected DescriptiveStatistics createStats() {
        return new DescriptiveStatistics(20);
    }
}
