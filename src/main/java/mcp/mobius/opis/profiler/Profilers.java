package mcp.mobius.opis.profiler;

import mcp.mobius.opis.profiler.impl.*;
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
    public static final ProfilerState<ProfilerDimensionTick> DIMENSION_TICK = addProfiler(new ProfilerDimensionTick(), ProfilerType.REAL_TIME, Side.SERVER);
    public static final ProfilerState<ProfilerServerTick> SERVER_TICK = addProfiler(new ProfilerServerTick(), ProfilerType.REAL_TIME, Side.SERVER);
    public static final ProfilerState<ProfilerVanillaPacket> INBOUND_PACKET = addProfiler(new ProfilerVanillaPacket(), ProfilerType.REAL_TIME, Side.SERVER);
    public static final ProfilerState<ProfilerVanillaPacket> OUTBOUND_PACKET = addProfiler(new ProfilerVanillaPacket(), ProfilerType.REAL_TIME, Side.SERVER);
    public static final ProfilerState<ProfilerFMLProxyPacket> INBOUND_FML_PACKET = addProfiler(new ProfilerFMLProxyPacket(), ProfilerType.REAL_TIME, Side.SERVER);
    public static final ProfilerState<ProfilerFMLProxyPacket> OUTBOUND_FML_PACKET = addProfiler(new ProfilerFMLProxyPacket(), ProfilerType.REAL_TIME, Side.SERVER);

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
    public static IProfiler getDimensionTickProfiler() { return getProfiler(DIMENSION_TICK); }
    public static IProfiler getServerTickProfiler() { return getProfiler(SERVER_TICK); }
    public static IProfiler getInboundPacketProfiler() { return getProfiler(INBOUND_PACKET); }
    public static IProfiler getOutboundPacketProfiler() { return getProfiler(OUTBOUND_PACKET); }
    public static IProfiler getInboundFMLPacketProfiler() { return getProfiler(INBOUND_FML_PACKET); }
    public static IProfiler getOutboundFMLPacketProfiler() { return getProfiler(OUTBOUND_FML_PACKET); }
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
