package mcp.mobius.opis.swing.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import mcp.mobius.opis.Opis;
import mcp.mobius.opis.profiler.ProfilerSection;
import net.minecraftforge.fml.relauncher.Side;

public class ActionRunOpisClient implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		Opis.profilerRunClient = true;
		ProfilerSection.resetAll   (Side.CLIENT);
		ProfilerSection.activateAll(Side.CLIENT);
	}
}
