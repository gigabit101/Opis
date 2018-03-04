package mcp.mobius.opis.data.profilers;

import mcp.mobius.opis.profiler.IProfilerBase;
import net.minecraft.entity.Entity;

import mcp.mobius.opis.modOpis;
import mcp.mobius.opis.data.profilers.Clock.IClock;

public class ProfilerEntityUpdate extends ProfilerAbstract implements IProfilerBase {

	private IClock clock = Clock.getNewClock();
	public  WeakHashMap<Entity, DescriptiveStatistics> data = new WeakHashMap<Entity, DescriptiveStatistics>();
	
	@Override
	public void reset() {
		this.data = new WeakHashMap<Entity, DescriptiveStatistics>();
	}	
	
	@Override
	public void start(Object key) {
		Entity entity = (Entity)key;
		if (entity.worldObj.isRemote) return;
		
		if (!data.containsKey(entity))
			data.put(entity, new DescriptiveStatistics());
		clock.start();
	}
	
	@Override
	public void stop(Object key) {
		Entity entity = (Entity)key;
		if (entity.worldObj.isRemote) return;
		
		clock.stop();
		try{
			data.get(entity).addValue(clock.getDelta());
		} catch (Exception e){
			modOpis.log.warn(String.format("Error while profiling entity %s\n", key));
		}
	}
}
