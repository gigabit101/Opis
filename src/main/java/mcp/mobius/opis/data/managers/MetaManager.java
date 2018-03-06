package mcp.mobius.opis.data.managers;

import mcp.mobius.opis.Opis;
import mcp.mobius.opis.events.OpisServerTickHandler;
import mcp.mobius.opis.profiler.ProfilerSection;
import mcp.mobius.opis.profiler.Profilers;
import net.minecraftforge.fml.relauncher.Side;

public class MetaManager {

    public static void reset() {
        Opis.profilerRun = false;
        Opis.selectedBlock = null;
        OpisServerTickHandler.INSTANCE.profilerRunningTicks = 0;

        Profilers.resetProfilers(Side.SERVER);
        Profilers.dissableProfilers(Side.SERVER);
    }
}
