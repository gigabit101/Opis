package mcp.mobius.opis.data.managers;

import mcp.mobius.opis.Opis;
import mcp.mobius.opis.events.OpisServerTickHandler;
import mcp.mobius.opis.profiler.ProfilerSection;
import net.minecraftforge.fml.relauncher.Side;

public class MetaManager {

    public static void reset() {
        Opis.profilerRun = false;
        Opis.selectedBlock = null;
        OpisServerTickHandler.INSTANCE.profilerRunningTicks = 0;

        ProfilerSection.resetAll(Side.SERVER);
        ProfilerSection.desactivateAll(Side.SERVER);
    }
}
