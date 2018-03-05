package mcp.mobius.opis.swing.widgets;

import mcp.mobius.opis.network.enums.AccessLevel;
import mcp.mobius.opis.swing.SwingUI;

import javax.swing.*;

public class JButtonAccess extends JButton {
    private AccessLevel al = AccessLevel.NONE;

    public JButtonAccess(AccessLevel level) {
        super();
        this.al = level;
        SwingUI.registeredButtons.add(this);
    }

    public JButtonAccess(String label) {
        super(label);
        SwingUI.registeredButtons.add(this);
    }

    public JButtonAccess(String label, AccessLevel level) {
        super(label);
        this.al = level;
        SwingUI.registeredButtons.add(this);
    }

    public void setAccessLevel(AccessLevel level) {
        this.al = level;
    }

    public AccessLevel getAccessLevel() {
        return this.al;
    }
}
