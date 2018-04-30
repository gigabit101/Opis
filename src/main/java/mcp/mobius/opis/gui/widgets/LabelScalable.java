package mcp.mobius.opis.gui.widgets;

import mcp.mobius.opis.gui.interfaces.IWidget;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Point;

public class LabelScalable extends LabelFixedFont {

    private float scale = 1.0f;

    public LabelScalable(IWidget parent, String text) {
        super(parent, text);
    }

    public LabelScalable(IWidget parent, String text, int color) {
        super(parent, text, color);
    }

    public LabelScalable setScale(float scale) {
        this.scale = scale;
        return this;
    }

    @Override
    public void draw(Point pos) {
        saveGLState();
        GL11.glScalef(scale, scale, 1.0f);
        mc.fontRenderer.drawString(text, (int) (pos.getX() / scale), (int) (pos.getY() / scale), color);
        loadGLState();
    }

}
