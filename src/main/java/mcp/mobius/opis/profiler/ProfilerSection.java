package mcp.mobius.opis.profiler;

import net.minecraftforge.fml.relauncher.Side;

import java.util.EnumSet;

@Deprecated
public enum ProfilerSection implements IProfilerBase {
    //DIMENSION_TICK(RunType.REALTIME, Side.SERVER),        //Global section around the ticks for each dim (Blocks & ents).
    //DIMENSION_BLOCKTICK(RunType.ONREQUEST, Side.SERVER),        //Subsection for dimension block tick.
    //ENTITY_UPDATETIME(RunType.ONREQUEST, Side.SERVER),        //Profiling of the entity tick time, per entity.
    //TICK(RunType.REALTIME, Side.SERVER),        //Tick timing profiling
    //TILEENT_UPDATETIME(RunType.ONREQUEST, Side.SERVER),        //Profiling of the TileEntity tick time, per TE.
    //HANDLER_TICKSTART  (RunType.ONREQUEST, EnumSet.of(Side.CLIENT, Side.SERVER)), 		//Server handler for ServerTick start.
    //HANDLER_TICKSTOP   (RunType.ONREQUEST, EnumSet.of(Side.CLIENT, Side.SERVER)),  		//Server handler for ServerTick stop.
    //PACKET_INBOUND(RunType.REALTIME, Side.SERVER),        //Outbound packet analysis
    //PACKET_OUTBOUND(RunType.REALTIME, Side.SERVER),        //Inbound packet analysis
    //PACKETFML_INBOUND  (RunType.REALTIME,  Side.SERVER),		//Outbound packet analysis
    //PACKETFML_OUTBOUND (RunType.REALTIME,  Side.SERVER),		//Inbound packet analysis

    //Was Unimplemented.
    //NETWORK_TICK(RunType.ONREQUEST, Side.SERVER),        //The time it takes for the server to handle the packets during a tick.
    //EVENT_INVOKE(RunType.ONREQUEST, EnumSet.of(Side.CLIENT, Side.SERVER)),//Timing of the event invocation
    RENDER_TILEENTITY(RunType.ONREQUEST, Side.CLIENT),
    //Profiler for TileEnt rendering
    RENDER_ENTITY(RunType.ONREQUEST, Side.CLIENT),
    //Profiler for Entity rendering
    RENDER_BLOCK(RunType.ONREQUEST, Side.CLIENT);//Profiler for Block rendering

    public enum RunType {
        REALTIME,
        ONREQUEST
    }

    private EnumSet<Side> sides;
    private RunType runType;
    private IProfilerBase profiler = new DummyProfiler();
    private IProfilerBase profilerSuspended = new DummyProfiler();

    public static long timeStampLastRun;

    ProfilerSection(RunType runType, Side side) {
        this.runType = runType;
        profiler = new DummyProfiler();
        sides = EnumSet.of(side);
    }

    ProfilerSection(RunType runType, EnumSet<Side> sides) {
        this.runType = runType;
        profiler = new DummyProfiler();
        this.sides = sides;
    }

    public RunType getRunType() {
        return runType;
    }

    public EnumSet<Side> getSide() {
        return sides;
    }

    public IProfilerBase getProfiler() {
        return profilerSuspended;
    }

    public void setProfiler(IProfilerBase profiler) {
        profilerSuspended = profiler;
        if (runType == RunType.REALTIME) {
            this.profiler = profiler;
        }
    }

    public void activate() {
        profiler = profilerSuspended;
        timeStampLastRun = System.currentTimeMillis();
    }

    public void desactivate() {
        if (runType == RunType.ONREQUEST) {
            profiler = new DummyProfiler();
        }
    }

    public static void activateAll(Side trgside) {
        for (ProfilerSection section : ProfilerSection.values()) {
            if (section.sides.contains(trgside)) {
                section.activate();
            }
        }
    }

    public static void desactivateAll(Side trgside) {
        for (ProfilerSection section : ProfilerSection.values()) {
            if (section.sides.contains(trgside)) {
                section.desactivate();
            }
        }
    }

    public static void resetAll(Side trgside) {
        for (ProfilerSection section : ProfilerSection.values()) {
            if (section.sides.contains(trgside)) {
                section.reset();
            }
        }
    }

    public static String getClassName() {
        return ProfilerSection.class.getCanonicalName().replace(".", "/");
    }

    public static String getTypeName() {
        return "L" + ProfilerSection.getClassName() + ";";
    }

    @Override
    public void reset() {
        profiler.reset();
        profilerSuspended.reset();
    }

    @Override
    public void start() {
        profiler.start();
    }

    @Override
    public void stop() {
        profiler.stop();
    }

    @Override
    public void start(Object key) {
        profiler.start(key);
    }

    @Override
    public void stop(Object key) {
        profiler.stop(key);
    }

    @Override
    public void start(Object key1, Object key2) {
        profiler.start(key1, key2);
    }

    @Override
    public void stop(Object key1, Object key2) {
        profiler.stop(key1, key2);
    }

    @Override
    public void start(Object key1, Object key2, Object key3) {
        profiler.start(key1, key2, key3);
    }

    @Override
    public void stop(Object key1, Object key2, Object key3) {
        profiler.stop(key1, key2, key3);
    }

    @Override
    public void start(Object key1, Object key2, Object key3, Object key4) {
        profiler.start(key1, key2, key3, key4);
    }

    @Override
    public void stop(Object key1, Object key2, Object key3, Object key4) {
        profiler.stop(key1, key2, key3, key4);
    }
}
