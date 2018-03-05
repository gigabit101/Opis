package mcp.mobius.opis.swing.actions;

import mcp.mobius.opis.Opis;
import mcp.mobius.opis.profiler.ProfilerSection;
import net.minecraftforge.fml.relauncher.Side;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ActionRunOpisClient implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        Opis.profilerRunClient = true;
        ProfilerSection.resetAll(Side.CLIENT);
        ProfilerSection.activateAll(Side.CLIENT);
    }
}
