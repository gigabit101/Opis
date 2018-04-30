package mcp.mobius.opis.profiler;

/**
 * Created by covers1624 on 23/04/18.
 */
public class ThreadedClock implements IClock {

    private ThreadLocal<Clock> clocks = ThreadLocal.withInitial(Clock::createClock);

    @Override
    public void start() {
        clocks.get().start();
    }

    @Override
    public void stop() {
        clocks.get().stop();
    }

    @Override
    public long getDelta() {
        return clocks.get().getDelta();
    }

    public static IClock createClock() {
        return new ThreadedClock();
    }
}
