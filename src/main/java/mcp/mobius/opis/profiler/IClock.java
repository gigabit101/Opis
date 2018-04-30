package mcp.mobius.opis.profiler;

public interface IClock {

    void start();

    void stop();

    long getDelta();
}
