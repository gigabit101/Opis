package mcp.mobius.opis.api;

import mcp.mobius.opis.data.holders.basetypes.SerialInt;
import mcp.mobius.opis.network.PacketManager;
import mcp.mobius.opis.network.enums.Message;
import mcp.mobius.opis.network.packets.client.PacketReqData;
import mcp.mobius.opis.swing.SelectedTab;
import mcp.mobius.opis.swing.SwingUI;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.HashMap;

public class TabPanelRegistrar implements ChangeListener {

    public static final TabPanelRegistrar INSTANCE = new TabPanelRegistrar();

    private HashMap<String, JTabbedPane> sections = new HashMap<>();
    private HashMap<SelectedTab, ITabPanel> lookup = new HashMap<>();

    public JTabbedPane registerSection(String name) {
        JTabbedPane pane = new JTabbedPane();
        pane.addChangeListener(this);
        sections.put(name, pane);
        SwingUI.instance().getTabbedPane().addTab(name, pane);
        return pane;
    }

    public <T extends ITabPanel> T registerTab(T panel, String name) {
        lookup.put(panel.getSelectedTab(), panel);
        SwingUI.instance().getTabbedPane().addTab(name, (JPanel) panel);
        return panel;
    }

    public <T extends ITabPanel> T registerTab(T panel, String name, String section) {
        lookup.put(panel.getSelectedTab(), panel);
        sections.get(section).addTab(name, (JPanel) panel);
        return panel;
    }

    public ITabPanel getTab(SelectedTab refname) {
        return lookup.get(refname);
    }

    public JPanel getTabAsPanel(SelectedTab refname) {
        return (JPanel) lookup.get(refname);
    }

    public void refreshAll() {
        for (ITabPanel panel : lookup.values()) {
            if (panel.refreshOnString()) {
                panel.refresh();
            }
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        Component source = ((JTabbedPane) e.getSource()).getSelectedComponent();

        if (source instanceof ITabPanel) {
            ITabPanel panel = (ITabPanel) source;
            PacketManager.sendToServer(new PacketReqData(Message.SWING_TAB_CHANGED, new SerialInt(panel.getSelectedTab().ordinal())));
        }

    }

	/*
	public ArrayList<ITabPanel> getTabs(){
		return this.panels;
	}
	*/
}
