package mcp.mobius.opis.profiler;

import mcp.mobius.opis.profiler.impl.ProfilerWorldServerTick;
import mcp.mobius.opis.profiler.impl.ProfilerEntityUpdate;
import mcp.mobius.opis.profiler.impl.ProfilerTileUpdate;
import net.minecraftforge.fml.relauncher.Side;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by covers1624 on 6/03/18.
 */
public class Profilers {

    public static long lastRun;

    public static List<ProfilerState<?>> allProfilers = new ArrayList<>();

    public static final ProfilerState<ProfilerTileUpdate> TILE_UPDATE = addProfiler(new ProfilerTileUpdate(), ProfilerType.ON_REQUEST, Side.SERVER);
    public static final ProfilerState<ProfilerEntityUpdate> ENTITY_UPDATE = addProfiler(new ProfilerEntityUpdate(), ProfilerType.ON_REQUEST, Side.SERVER);
    public static final ProfilerState<ProfilerWorldServerTick> WORLD_SERVER_TICK = addProfiler(new ProfilerWorldServerTick(), ProfilerType.ON_REQUEST, Side.SERVER);

    public static <A extends IProfiler> ProfilerState<A> addProfiler(A profiler, ProfilerType type, Side... runSides) {
        ProfilerState<A> state = new ProfilerState<>(profiler, type, runSides);
        allProfilers.add(state);
        return state;
    }

    public static void resetProfilers(Side side) {
        ProfilerSection.resetAll(side);
        allProfilers.stream().filter(p -> p.canRun(side)).forEach(p -> p.get().reset());
    }

    public static void enableProfilers(Side side) {
        ProfilerSection.activateAll(side);
        allProfilers.stream().filter(p -> p.canRun(side)).forEach(ProfilerState::setEnabled);
    }

    public static void dissableProfilers(Side side) {
        ProfilerSection.desactivateAll(side);
        allProfilers.stream().filter(p -> p.canRun(side)).forEach(ProfilerState::setDissabled);
    }

    public static enum ProfilerType {
        ON_REQUEST,
        REAL_TIME
    }


    //These are called via ASM, so we can dummy the profiler instance.
    //@formatter:off
    public static IProfiler getTileUpdateProfiler() { return getProfiler(TILE_UPDATE); }
    public static IProfiler getEntityUpdateProfiler() { return getProfiler(ENTITY_UPDATE); }
    public static IProfiler getWorldServerTickProfiler() { return getProfiler(WORLD_SERVER_TICK); }
    public static IProfiler getProfiler(ProfilerState<?> state) {
        if (state.isEnabled()) {
            return state.get();
        } else {
            return DummyProfiler.INSTANCE;
        }
    }
    //@formatter:on

    //Dummy profiler.
    //@formatter:off
    private static class DummyProfiler implements IProfiler<Object, Object, Object, Object> {
        public static final DummyProfiler INSTANCE = new DummyProfiler();
        private DummyProfiler(){}
        @Override public void reset() {}
        @Override public void start() {}
        @Override public void stop() {}
        @Override public void start(Object a) {}
        @Override public void stop(Object a) {}
        @Override public void start(Object a, Object b) {}
        @Override public void stop(Object a, Object b) {}
        @Override public void start(Object a, Object b, Object c) {}
        @Override public void stop(Object a, Object b, Object c) {}
        @Override public void start(Object a, Object b, Object c, Object d) {}
        @Override public void stop(Object a, Object b, Object c, Object d) {}
    }
    //@formatter:on
}
