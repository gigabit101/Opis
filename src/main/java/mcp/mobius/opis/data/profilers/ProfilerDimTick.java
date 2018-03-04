package mcp.mobius.opis.data.profilers;

import mcp.mobius.opis.profiler.IProfilerBase;
import net.minecraft.world.World;
import mcp.mobius.opis.data.profilers.Clock.IClock;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.util.HashMap;

public class ProfilerDimTick extends ProfilerAbstract implements IProfilerBase {

	private IClock clock = Clock.getNewClock();
	public HashMap<Integer, DescriptiveStatistics> data = new HashMap<Integer, DescriptiveStatistics>();
	
	@Override
	public void reset() {
		this.data.clear();
	}	
	
	@Override
	public void start(Object key) {
		World world = (World)key;
		if (world.isRemote) return;
		
		if (!data.containsKey(world.provider.dimensionId))
			data.put(world.provider.dimensionId, new DescriptiveStatistics(20));
		clock.start();
	}
	
	@Override
	public void stop(Object key) {
		World world = (World)key;		
		if (world.isRemote) return;
		
		clock.stop();
		data.get(world.provider.dimensionId).addValue(clock.getDelta());
	}
}
