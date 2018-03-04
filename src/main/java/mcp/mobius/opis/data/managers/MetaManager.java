package mcp.mobius.opis.data.managers;

import cpw.mods.fml.relauncher.Side;
import mcp.mobius.mobiuscore.profiler.ProfilerSection;
import mcp.mobius.opis.Opis;
import mcp.mobius.opis.events.OpisServerTickHandler;

public class MetaManager {

	public static void reset(){
		Opis.profilerRun   = false;
		Opis.selectedBlock = null;
		OpisServerTickHandler.INSTANCE.profilerRunningTicks = 0;
		
		ProfilerSection.resetAll(Side.SERVER);
		ProfilerSection.desactivateAll(Side.SERVER);
		//ProfilerSection.resetAll(Side.CLIENT);
		//ProfilerSection.desactivateAll(Side.CLIENT);		
	}
	
}
