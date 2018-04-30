package mcp.mobius.opis.data.profilers;

import mcp.mobius.opis.Opis;
import mcp.mobius.opis.data.profilers.Clock.IClock;
import net.minecraft.entity.Entity;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.util.WeakHashMap;

public class ProfilerRenderEntity extends ProfilerAbstract {

    public WeakHashMap<Entity, DescriptiveStatistics> data = new WeakHashMap<>();
    private IClock clock = Clock.getNewClock();

    @Override
    public void reset() {
        data.clear();
    }

    @Override
    public void start(Object key) {
        Entity entity = (Entity) key;
        if (!data.containsKey(entity)) {
            data.put(entity, new DescriptiveStatistics());
        }

        clock.start();
    }

    @Override
    public void stop(Object key) {
        clock.stop();
        try {
            //data.get((Entity)key).addValue((double)clock.getDelta());
            data.get(key).addValue(clock.getDelta());
        } catch (Exception e) {
            Opis.log.warn(String.format("Error while profiling entity %s\n", key));
        }
    }
}
