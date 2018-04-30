package mcp.mobius.opis.profiler;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.function.LongSupplier;

/**
 * Created by covers1624 on 6/03/18.
 */
public class Clock implements IClock {

    private static ThreadMXBean threadMX = ManagementFactory.getThreadMXBean();
    public static boolean threadCPUTimeSupported = false; //threadMX.isCurrentThreadCpuTimeSupported();

    private static LongSupplier MX_TIME = threadMX::getCurrentThreadCpuTime;
    private static LongSupplier SYS_TIME = System::nanoTime;

    public long startTime = 0L;
    public long timeDelta = 0L;
    private LongSupplier supplier;

    public Clock(LongSupplier supplier) {
        this.supplier = supplier;
    }

    @Override
    public void start() {
        startTime = supplier.getAsLong();
    }

    @Override
    public void stop() {
        timeDelta = supplier.getAsLong() - startTime;
    }

    @Override
    public long getDelta() {
        return timeDelta;
    }

    public static Clock createClock() {
        if (threadCPUTimeSupported) {
            return new Clock(MX_TIME);
        } else {
            return new Clock(SYS_TIME);
        }
    }
}
