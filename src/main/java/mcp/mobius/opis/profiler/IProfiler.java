package mcp.mobius.opis.profiler;

/**
 * Created by covers1624 on 6/03/18.
 */
public interface IProfiler<A, B, C, D> {

    void reset();

    default void start() {
        throw new RuntimeException("This method is not supported by this profiler.");
    }

    default void stop() {
        throw new RuntimeException("This method is not supported by this profiler.");
    }

    default void start(A a) {
        throw new RuntimeException("This method is not supported by this profiler.");
    }

    default void stop(A a) {
        throw new RuntimeException("This method is not supported by this profiler.");
    }

    default void start(A a, B b) {
        throw new RuntimeException("This method is not supported by this profiler.");
    }

    default void stop(A a, B b) {
        throw new RuntimeException("This method is not supported by this profiler.");
    }

    default void start(A a, B b, C c) {
        throw new RuntimeException("This method is not supported by this profiler.");
    }

    default void stop(A a, B b, C c) {
        throw new RuntimeException("This method is not supported by this profiler.");
    }

    default void start(A a, B b, C c, D d) {
        throw new RuntimeException("This method is not supported by this profiler.");
    }

    default void stop(A a, B b, C c, D d) {
        throw new RuntimeException("This method is not supported by this profiler.");
    }

    interface IProfilerSingle<A> extends IProfiler<A, Object, Object, Object> {

        @Override
        void start(A a);

        @Override
        void stop(A a);
    }

    interface IProfilerDouble<A, B> extends IProfiler<A, B, Object, Object> {

        @Override
        void start(A a, B b);

        @Override
        void stop(A a, B b);
    }

    interface IProfilerTripple<A, B, C> extends IProfiler<A, B, C, Object> {

        @Override
        void start(A a, B b, C c);

        @Override
        void stop(A a, B b, C c);
    }

    interface IProfilerQuad<A, B, C, D> extends IProfiler<A, B, C, D> {

        @Override
        void start(A a, B b, C c, D d);

        @Override
        void stop(A a, B b, C c, D d);
    }

}
