package mcp.mobius.opis.profiler;

@Deprecated
public interface IProfilerBase {

    void reset();

    void start();

    void stop();

    void start(Object key);

    void stop(Object key);

    void start(Object key1, Object key2);

    void stop(Object key1, Object key2);

    void start(Object key1, Object key2, Object key3);

    void stop(Object key1, Object key2, Object key3);

    void start(Object key1, Object key2, Object key3, Object key4);

    void stop(Object key1, Object key2, Object key3, Object key4);
}
