package mcp.mobius.opis.tools;

import mcp.mobius.opis.Opis;
import net.minecraft.tileentity.TileEntity;

public class TileDebug extends TileEntity {

	public TileDebug(){
		Opis.log.info("=============================");
		Opis.log.info(String.format("Hello, I will be your companion debug block for the day. My ID is 0x%s", System.identityHashCode(this)));
		Opis.log.info("For a starter, let me show you who created me :");
		StackTraceElement[] cause = Thread.currentThread().getStackTrace();
		
		for (StackTraceElement elem : cause){
			Opis.log.info(String.format("%s.%s:%s", elem.getClassName(), elem.getMethodName(), elem.getLineNumber()));
		}
		Opis.log.info("=============================");
		
	}
}
