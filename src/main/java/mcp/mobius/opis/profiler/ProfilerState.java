package mcp.mobius.opis.profiler;

import mcp.mobius.opis.profiler.Profilers.ProfilerType;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

/**
 * Created by covers1624 on 6/03/18.
 */
public class ProfilerState<A extends IProfiler> {

    public IProfiler profiler;
    public boolean isEnabled;
    private final A actualProfiler;
    private final Set<Side> runSides;
    private final ProfilerType type;

    public ProfilerState(A profiler, ProfilerType type, Side... runSides) {
        isEnabled = type == ProfilerType.REAL_TIME;
        this.actualProfiler = profiler;
        this.profiler = isEnabled ? actualProfiler : Profilers.getDummyProfiler();
        this.type = type;
        this.runSides = EnumSet.noneOf(Side.class);
        Collections.addAll(this.runSides, runSides);
    }

    public A get() {
        return actualProfiler;
    }

    public void setEnabled() {
        if (type == ProfilerType.ON_REQUEST) {
            isEnabled = true;
            profiler = actualProfiler;
        }
    }

    public void setDisabled() {
        if (type == ProfilerType.ON_REQUEST) {
            isEnabled = false;
            profiler = Profilers.getDummyProfiler();
        }
    }

    public boolean canRun(Side side) {
        return runSides.contains(side);
    }
}
