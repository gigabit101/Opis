package mcp.mobius.opis.gui.screens;

import mcp.mobius.opis.gui.interfaces.IWidget;
import mcp.mobius.opis.gui.widgets.LayoutCanvas;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Mouse;

import java.util.HashMap;

public abstract class ScreenBase extends GuiScreen {

    protected GuiScreen parent;                    // Return screen if available.
    protected Minecraft mc;                        // Minecraft instance
    protected HashMap<String, IWidget> widgets;    // List of widgets on this ui

    public ScreenBase(GuiScreen parent) {
        this.parent = parent;
        mc = Minecraft.getMinecraft();
        widgets = new HashMap<>();

        addWidget("canvas", new LayoutCanvas());

        Mouse.getDWheel();            // We init the DWheel method (getDWheel returns the value since the last call, so we have to call it once on ui creation)
    }

    /////////////////////
    // WIDGET HANDLING //
    /////////////////////

    public IWidget addWidget(String name, IWidget widget) {
        widgets.put(name, widget);
        return getWidget(name);
    }

    public IWidget getWidget(String name) {
        return widgets.get(name);
    }

    public IWidget delWidget(String name) {
        IWidget widget = getWidget(name);
        //this.widgets.remove(widget);
        widgets.remove(name);
        return widget;
    }

    public IWidget getRoot() {
        return getWidget("canvas");
    }

    /////////////////////
    // DRAWING METHODS //
    /////////////////////

    @Override
    public void drawScreen(int i, int j, float f) {
        drawDefaultBackground();    //The dark background common to most of the UIs
        super.drawScreen(i, j, f);

        for (IWidget widget : widgets.values()) {
            widget.draw();
        }
    }

    // Used to easily display a UI without having to mess with mc handle.
    public void display() {
        parent = mc.currentScreen;
        mc.displayGuiScreen(this);
    }

    /////////////////////
    // INPUT METHODS   //
    /////////////////////

    //Keyboard handling. Basic one is just returning to the previous UI when Esc is pressed
    @Override
    public void keyTyped(char keyChar, int keyID) {
        if (keyID == 1) {
            if (parent == null) {
                mc.displayGuiScreen(null);
                mc.setIngameFocus();
            } else {
                mc.displayGuiScreen(parent);
            }
        }
    }

    @Override
    public void handleMouseInput() {
        getRoot().handleMouseInput();
    }

}
