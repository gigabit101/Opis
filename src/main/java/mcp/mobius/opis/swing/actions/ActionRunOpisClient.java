package mcp.mobius.opis.swing.actions;

import mcp.mobius.opis.Opis;
import mcp.mobius.opis.profiler.Profilers;
import net.minecraftforge.fml.relauncher.Side;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ActionRunOpisClient implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        Opis.profilerRunClient = true;
        Profilers.resetProfilers(Side.CLIENT);
        Profilers.enableProfilers(Side.CLIENT);
    }
}
