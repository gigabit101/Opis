package mcp.mobius.opis.profiler.impl;

import gnu.trove.impl.sync.TSynchronizedLongObjectMap;
import gnu.trove.map.TLongObjectMap;
import gnu.trove.map.hash.TLongObjectHashMap;
import mcp.mobius.opis.data.holders.newtypes.DataEvent;
import mcp.mobius.opis.profiler.IClock;
import mcp.mobius.opis.profiler.IProfiler;
import mcp.mobius.opis.profiler.ThreadedClock;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by covers1624 on 19/04/18.
 */
public class ProfilerASMEventHandler implements IProfiler<Event, String, ModContainer, Object> {

    private final IClock clock = ThreadedClock.createClock();

    //My fucking jesus these generics are cancer..
    private final TLongObjectMap<Map<Triple<Class, String, String>, DescriptiveStatistics>> data = new TSynchronizedLongObjectMap<>(new TLongObjectHashMap<>());
    private final ThreadLocal<Map<Triple<Class, String, String>, DescriptiveStatistics>> dataThreadLookup = ThreadLocal.withInitial(() -> {
        Map<Triple<Class, String, String>, DescriptiveStatistics> table = new HashMap<>();
        data.put(Thread.currentThread().getId(), table);
        return table;
    });
    private final TLongObjectMap<Map<Triple<Class, String, String>, DescriptiveStatistics>> tickData = new TSynchronizedLongObjectMap<>(new TLongObjectHashMap<>());
    private final ThreadLocal<Map<Triple<Class, String, String>, DescriptiveStatistics>> tickDataThreadLookup = ThreadLocal.withInitial(() -> {
        Map<Triple<Class, String, String>, DescriptiveStatistics> table = new HashMap<>();
        tickData.put(Thread.currentThread().getId(), table);
        return table;
    });

    @Override
    public void reset() {
        data.valueCollection().forEach(Map::clear);
    }

    @Override
    public void start() {
        clock.start();
    }

    @Override
    public void stop(Event event, String handler_method, ModContainer mod) {
        clock.stop();
        long delta = clock.getDelta();

        Map<Triple<Class, String, String>, DescriptiveStatistics> map;
        if (event instanceof TickEvent) {
            map = tickDataThreadLookup.get();
        } else {
            map = dataThreadLookup.get();
        }

        Triple<Class, String, String> holder = Triple.of(event.getClass(), handler_method, mod.getName());
        DescriptiveStatistics stats = map.computeIfAbsent(holder, h -> new DescriptiveStatistics());
        stats.addValue(delta);
    }

    public List<DataEvent> getData() {
        return makeData(joinData(data));
    }

    public List<DataEvent> getTickData() {
        return makeData(joinData(tickData));
    }

    private List<DataEvent> makeData(Map<Triple<Class, String, String>, DescriptiveStatistics> map) {
        List<DataEvent> dataList = new ArrayList<>();
        for (Map.Entry<Triple<Class, String, String>, DescriptiveStatistics> entry : map.entrySet()) {
            Triple<Class, String, String> triple = entry.getKey();
            dataList.add(new DataEvent().fill(triple.getLeft(), triple.getMiddle(), triple.getRight(), entry.getValue()));
        }
        return dataList;
    }

    private <T> Map<T, DescriptiveStatistics> joinData(TLongObjectMap<Map<T, DescriptiveStatistics>> map) {
        Map<T, DescriptiveStatistics> merged = new HashMap<>();
        for (Map<T, DescriptiveStatistics> threadMap : map.valueCollection()) {
            for (Map.Entry<T, DescriptiveStatistics> entry : threadMap.entrySet()) {
                DescriptiveStatistics stats = merged.computeIfAbsent(entry.getKey(), h -> new DescriptiveStatistics());
                for (double d : entry.getValue().getValues()) {
                    stats.addValue(d);
                }
            }
        }
        return merged;
    }

}
