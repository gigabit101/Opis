package mcp.mobius.opis.gui.widgets;

import mcp.mobius.opis.gui.interfaces.CType;
import mcp.mobius.opis.gui.interfaces.IWidget;
import org.lwjgl.util.Point;

public class LabelFixedFont extends WidgetBase {

    protected String text = "";
    protected int color;

    public LabelFixedFont(IWidget parent, String text) {
        super(parent);
        setText(text);
        color = 0xFFFFFF;
    }

    public LabelFixedFont(IWidget parent, String text, int color) {
        super(parent);
        setText(text);
        this.color = color;
    }

    @Override
    public IWidget setGeometry(WidgetGeometry geom) {
        this.geom = geom;
        updateGeometry();
        return this;
    }

    public void setText(String text) {
        this.text = text;
        updateGeometry();
    }

    public void setColor(int color) {
        this.color = color;
    }

    protected void updateGeometry() {
        if (geom == null) {
            geom = new WidgetGeometry(0, 0, 50, 50, CType.ABSXY, CType.ABSXY);
        }

        geom = new WidgetGeometry(geom.x, geom.y, mc.fontRenderer.getStringWidth(text), 8, geom.posType, CType.ABSXY, geom.alignX, geom.alignY);
    }

    @Override
    public void draw(Point pos) {
        saveGLState();
        mc.fontRenderer.drawString(text, pos.getX(), pos.getY(), color);
        loadGLState();
    }
}
