package mcp.mobius.opis.events;

public class EventTimer {

    private long interval;
    private long lastTick = System.nanoTime();

    public EventTimer(long interval) {
        this.interval = interval * 1000L * 1000L; //Interval is passed in millisecond but stored in nanosecond.
    }

    public boolean isDone() {
        long time = System.nanoTime();
        long delta = time - lastTick - interval;
        boolean done = delta >= 0;
        if (!done) {
            return false;
        }

        lastTick = time - delta;
        return true;
    }
}
