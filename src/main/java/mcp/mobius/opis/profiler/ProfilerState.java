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

    private final A profiler;
    private final Set<Side> runSides;
    private final ProfilerType type;
    private boolean isEnabled;

    public ProfilerState(A profiler, ProfilerType type, Side... runSides) {
        this.profiler = profiler;
        this.type = type;
        this.runSides = EnumSet.noneOf(Side.class);
        Collections.addAll(this.runSides, runSides);
        isEnabled = type == ProfilerType.REAL_TIME;
    }

    public A get() {
        return profiler;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled() {
        if (type == ProfilerType.ON_REQUEST) {
            isEnabled = true;
            Profilers.lastRun = System.currentTimeMillis();
        }
    }

    public void setDissabled() {
        if (type == ProfilerType.ON_REQUEST) {
            isEnabled = false;
        }
    }

    public boolean canRun(Side side) {
        return runSides.contains(side);
    }
}
