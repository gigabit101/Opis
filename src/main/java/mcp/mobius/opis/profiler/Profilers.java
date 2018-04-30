package mcp.mobius.opis.profiler;

import mcp.mobius.opis.profiler.impl.*;
import net.minecraftforge.fml.relauncher.Side;

import java.util.ArrayList;
import java.util.List;

import static mcp.mobius.opis.profiler.Profilers.ProfilerType.ON_REQUEST;
import static mcp.mobius.opis.profiler.Profilers.ProfilerType.REAL_TIME;

/**
 * Created by covers1624 on 6/03/18.
 */
public class Profilers {

    public static long lastRun;

    public static List<ProfilerState<?>> allProfilers = new ArrayList<>();

    public static final ProfilerState<ProfilerTileUpdate> TILE_UPDATE = addProfiler(new ProfilerTileUpdate(), ON_REQUEST, Side.SERVER);
    public static final ProfilerState<ProfilerEntityUpdate> ENTITY_UPDATE = addProfiler(new ProfilerEntityUpdate(), ON_REQUEST, Side.SERVER);
    public static final ProfilerState<ProfilerWorldServerTick> WORLD_SERVER_TICK = addProfiler(new ProfilerWorldServerTick(), ON_REQUEST, Side.SERVER);
    public static final ProfilerState<ProfilerDimensionTick> DIMENSION_TICK = addProfiler(new ProfilerDimensionTick(), REAL_TIME, Side.SERVER);
    public static final ProfilerState<ProfilerServerTick> SERVER_TICK = addProfiler(new ProfilerServerTick(), REAL_TIME, Side.SERVER);
    public static final ProfilerState<ProfilerVanillaPacket> INBOUND_PACKET = addProfiler(new ProfilerVanillaPacket(), REAL_TIME, Side.SERVER);
    public static final ProfilerState<ProfilerVanillaPacket> OUTBOUND_PACKET = addProfiler(new ProfilerVanillaPacket(), REAL_TIME, Side.SERVER);
    public static final ProfilerState<ProfilerFMLProxyPacket> INBOUND_FML_PACKET = addProfiler(new ProfilerFMLProxyPacket(), REAL_TIME, Side.SERVER);
    public static final ProfilerState<ProfilerFMLProxyPacket> OUTBOUND_FML_PACKET = addProfiler(new ProfilerFMLProxyPacket(), REAL_TIME, Side.SERVER);
    public static final ProfilerState<ProfilerASMEventHandler> ASM_EVENT_HANDLER = addProfiler(new ProfilerASMEventHandler(), ON_REQUEST, Side.SERVER, Side.CLIENT);

    public static <A extends IProfiler> ProfilerState<A> addProfiler(A profiler, ProfilerType type, Side... runSides) {
        ProfilerState<A> state = new ProfilerState<>(profiler, type, runSides);
        allProfilers.add(state);
        return state;
    }

    public static void resetProfilers(Side side) {
        ProfilerSection.resetAll(side);
        allProfilers.stream().filter(p -> p.canRun(side)).forEach(p -> p.profiler.reset());
    }

    public static void enableProfilers(Side side) {
        ProfilerSection.activateAll(side);
        allProfilers.stream().filter(p -> p.canRun(side)).forEach(ProfilerState::setEnabled);
        Profilers.lastRun = System.currentTimeMillis();
    }

    public static void dissableProfilers(Side side) {
        ProfilerSection.desactivateAll(side);
        allProfilers.stream().filter(p -> p.canRun(side)).forEach(ProfilerState::setDisabled);
    }

    public enum ProfilerType {
        ON_REQUEST,
        REAL_TIME
    }

    //These are called via ASM.
    //@formatter:off
    public static IProfiler getTileUpdateProfiler() { return TILE_UPDATE.profiler; }
    public static IProfiler getEntityUpdateProfiler() { return ENTITY_UPDATE.profiler; }
    public static IProfiler getWorldServerTickProfiler() { return WORLD_SERVER_TICK.profiler; }
    public static IProfiler getDimensionTickProfiler() { return DIMENSION_TICK.profiler; }
    public static IProfiler getServerTickProfiler() {    return SERVER_TICK.profiler; }
    public static IProfiler getInboundPacketProfiler() { return INBOUND_PACKET.profiler; }
    public static IProfiler getOutboundPacketProfiler() { return OUTBOUND_PACKET.profiler; }
    public static IProfiler getInboundFMLPacketProfiler() { return INBOUND_FML_PACKET.profiler; }
    public static IProfiler getOutboundFMLPacketProfiler() { return OUTBOUND_FML_PACKET.profiler; }
    public static IProfiler getASMEventHandlerProfiler() { return ASM_EVENT_HANDLER.profiler; }
    //@formatter:on

    //Dummy profiler.
    //@formatter:off
    public static IProfiler getDummyProfiler() { return DummyProfiler.INSTANCE; }
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
