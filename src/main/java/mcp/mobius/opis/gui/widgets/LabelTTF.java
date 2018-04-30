package mcp.mobius.opis.gui.widgets;

import mcp.mobius.opis.gui.font.FontHelper;
import mcp.mobius.opis.gui.font.TrueTypeFont;
import mcp.mobius.opis.gui.interfaces.CType;
import mcp.mobius.opis.gui.interfaces.IWidget;
import org.lwjgl.util.Point;

public class LabelTTF extends WidgetBase {

    protected TrueTypeFont font;
    protected String text = "";
    protected int color;
    protected float scale = 1.0f;

    public LabelTTF(IWidget parent, String text, TrueTypeFont font) {
        super(parent);
        this.font = font;
        color = 0xFFFFFF;
        setText(text);
    }

    public LabelTTF(IWidget parent, String text, int color, TrueTypeFont font) {
        super(parent);
        this.font = font;
        this.color = color;
        setText(text);
    }

    public void setText(String text) {
        this.text = text;
        updateGeometry();
    }

    @Override
    public IWidget setGeometry(WidgetGeometry geom) {
        this.geom = geom;
        updateGeometry();
        return this;
    }

    public LabelTTF setScale(float scale) {
        this.scale = scale;
        return this;
    }

    protected void updateGeometry() {
        if (geom == null) {
            geom = new WidgetGeometry(0, 0, 50, 50, CType.ABSXY, CType.ABSXY);
        }

        if (font == null || text == null) {
            return;
        }

        geom = new WidgetGeometry(geom.x, geom.y, font.getWidth(text), font.getHeight(text) / rez.getScaleFactor(), geom.posType, CType.ABSXY, geom.alignX, geom.alignY);
    }

    @Override
    public void draw(Point pos) {
        saveGLState();
        FontHelper.drawString(text, pos.getX(), pos.getY(), font, scale, scale, color);
        loadGLState();
    }

}
