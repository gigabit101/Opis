package mcp.mobius.opis.profiler.impl;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

/**
 * Created by covers1624 on 6/03/18.
 */
public class ProfilerWorldServerTick extends AbstractProfilerWorldTick {

    @Override
    protected DescriptiveStatistics createStats() {
        return new DescriptiveStatistics();
    }
}
