package mcp.mobius.opis.profiler.impl;

import mcp.mobius.opis.profiler.Clock;
import mcp.mobius.opis.profiler.IClock;
import mcp.mobius.opis.profiler.IProfiler.IProfilerSingle;
import mcp.mobius.opis.util.DimBlockPos;
import net.minecraft.tileentity.TileEntity;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by covers1624 on 6/03/18.
 */
public class ProfilerTileUpdate implements IProfilerSingle<TileEntity> {

    private IClock clock = Clock.createClock();
    public Map<DimBlockPos, DescriptiveStatistics> data = new HashMap<>();
    public Map<DimBlockPos, Class> refs = new HashMap<>();

    @Override
    public void reset() {
        data.clear();
        refs.clear();
    }

    @Override
    public void start(TileEntity tile) {
        if (tile.getWorld().isRemote) {
            return;
        }

        DimBlockPos pair = new DimBlockPos(tile);
        if (!data.containsKey(pair) || (refs.get(pair) != tile.getClass())) {
            data.put(pair, new DescriptiveStatistics());
            refs.put(pair, tile.getClass());
        }
        clock.start();
    }

    @Override
    public void stop(TileEntity tile) {
        if (tile.getWorld().isRemote) {
            return;
        }

        clock.stop();
        DimBlockPos pair = new DimBlockPos(tile);
        data.get(pair).addValue(clock.getDelta());
    }
}
