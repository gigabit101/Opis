package mcp.mobius.opis.data.profilers;

import mcp.mobius.opis.data.profilers.Clock.IClock;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public class ProfilerNetworkTick extends ProfilerAbstract {

	private IClock clock = Clock.getNewClock();
	public DescriptiveStatistics data = new DescriptiveStatistics();
	
	@Override
	public void reset() {
		data.clear();
	}

	@Override
	public void start(){
		clock.start();
	}

	@Override
	public void stop(){
		clock.stop();
		data.addValue(clock.getDelta());
	}	
	
}
