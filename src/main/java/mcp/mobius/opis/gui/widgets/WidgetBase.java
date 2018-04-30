package mcp.mobius.opis.gui.widgets;

import mcp.mobius.opis.gui.events.MouseEvent;
import mcp.mobius.opis.gui.helpers.ReverseIterator;
import mcp.mobius.opis.gui.interfaces.CType;
import mcp.mobius.opis.gui.interfaces.IWidget;
import mcp.mobius.opis.gui.interfaces.RenderPriority;
import mcp.mobius.opis.gui.interfaces.Signal;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.texture.TextureManager;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Point;

import java.util.LinkedHashMap;

public abstract class WidgetBase implements IWidget {

    protected IWidget parent;
    protected WidgetGeometry geom;
    protected LinkedHashMap<String, IWidget> widgets = new LinkedHashMap<>();
    protected LinkedHashMap<String, IWidget> renderQueue_HIGH = new LinkedHashMap<>();
    protected LinkedHashMap<String, IWidget> renderQueue_MEDIUM = new LinkedHashMap<>();
    protected LinkedHashMap<String, IWidget> renderQueue_LOW = new LinkedHashMap<>();
    protected Minecraft mc;
    protected TextureManager texManager;
    protected ScaledResolution rez;

    protected boolean hasBlending;
    protected boolean hasLight;
    protected int boundTexIndex;

    protected boolean isRendering = true;

    protected float alpha = 1.0f;

    public WidgetBase() {
        setParent(null);
        mc = Minecraft.getMinecraft();
        rez = new ScaledResolution(mc);
        texManager = mc.renderEngine;
        setGeometry(new WidgetGeometry(0, 0, 50, 50, CType.ABSXY, CType.ABSXY));
    }

    public WidgetBase(IWidget parent) {
        setParent(parent);
        mc = Minecraft.getMinecraft();
        rez = new ScaledResolution(mc);
        texManager = mc.renderEngine;
        setGeometry(new WidgetGeometry(0, 0, 50, 50, CType.ABSXY, CType.ABSXY));
    }

    /////////////////////
    // WIDGET HANDLING //
    /////////////////////

    @Override
    public IWidget addWidget(String name, IWidget widget) {
        return addWidget(name, widget, RenderPriority.MEDIUM);
    }

    @Override
    public IWidget addWidget(String name, IWidget widget, RenderPriority priority) {
        widget.setParent(this);
        widgets.put(name, widget);

        switch (priority) {
            case LOW:
                renderQueue_LOW.put(name, widget);
                break;
            case HIGH:
                renderQueue_HIGH.put(name, widget);
                break;
            case MEDIUM:
                renderQueue_MEDIUM.put(name, widget);
                break;
        }

        return getWidget(name);
    }

    @Override
    public IWidget getWidget(String name) {
        return widgets.get(name);
    }

    @Override
    public IWidget delWidget(String name) {
        IWidget widget = getWidget(name);
        //this.widgets.remove(widget);
        widgets.remove(name);
        return widget;
    }

    @Override
    public boolean hasWidget(String name) {
        return widgets.containsKey(name);
    }

    @Override
    public IWidget getWidgetAtLayer(double posX, double posY, int layer) {
        if (layer == 0) {
            return this;
        }

        int depth = 0;
        return _getWidgetAtLayer(posX, posY, layer, depth + 1);
    }

    private IWidget _getWidgetAtLayer(double posX, double posY, int layer, int depth) {
        for (IWidget widget : widgets.values()) {
            if ((posX >= widget.getPos().getX()) && (posX <= widget.getPos().getX() + widget.getSize().getX()) && (posY >= widget.getPos().getY()) && (posY <= widget.getPos().getY() + widget.getSize().getY())) {
                if (depth == layer) {
                    return widget;
                } else {
                    return ((WidgetBase) widget)._getWidgetAtLayer(posX, posY, layer, depth + 1);
                }
            }
        }
        return null;
    }

    @Override
    public IWidget getWidgetAtCoordinates(double posX, double posY) {
        for (IWidget widget : new ReverseIterator<>(widgets.values())) {
            if ((posX >= widget.getPos().getX()) && (posX <= widget.getPos().getX() + widget.getSize().getX()) && (posY >= widget.getPos().getY()) && (posY <= widget.getPos().getY() + widget.getSize().getY())) {
                return widget.getWidgetAtCoordinates(posX, posY);
            }
        }

        if ((posX >= getPos().getX()) && (posX <= getPos().getX() + getSize().getX()) && (posY >= getPos().getY()) && (posY <= getPos().getY() + getSize().getY())) {
            return this;
        }

        return null;
    }

    @Override
    public boolean isWidgetAtCoordinates(double posx, double posy) {
        if (getLeft() > posx) {
            return false;
        }
        if (getRight() < posx) {
            return false;
        }
        if (getTop() > posy) {
            return false;
        }
        return !(getBottom() < posy);
    }

    ///////////////////////
    // IWIDGET INTERFACE //
    ///////////////////////

    @Override
    public IWidget getParent() {
        return parent;
    }

    @Override
    public void setParent(IWidget parent) {
        this.parent = parent;
    }

    @Override
    public void draw() {
        saveGLState();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, alpha);

        draw(getPos());

		/*
		for (IWidget widget: this.widgets.values())
			if (widget.shouldRender())
				widget.draw();
		*/

        for (IWidget widget : renderQueue_LOW.values()) {
            if (widget.shouldRender()) {
                widget.draw();
            }
        }

        for (IWidget widget : renderQueue_MEDIUM.values()) {
            if (widget.shouldRender()) {
                widget.draw();
            }
        }

        for (IWidget widget : renderQueue_HIGH.values()) {
            if (widget.shouldRender()) {
                widget.draw();
            }
        }

        loadGLState();
    }

    @Override
    public abstract void draw(Point pos);

    @Override
    public IWidget setGeometry(WidgetGeometry geom) {
        this.geom = geom;
        emit(Signal.GEOM_CHANGED, this.geom);
        return this;
    }

    public IWidget setGeometry(double x, double y, double sx, double sy, CType fp, CType fs) {
        setGeometry(new WidgetGeometry(x, y, sx, sy, fp, fs));
        emit(Signal.GEOM_CHANGED, geom);
        return this;
    }

    @Override
    public WidgetGeometry getGeometry() {
        return geom;
    }

    @Override
    public Point getPos() {
        return geom.getPos(parent);
    }

    @Override
    public Point getSize() {
        return geom.getSize(parent);
    }

    @Override
    public int getLeft() {
        return getPos().getX();
    }

    @Override
    public int getRight() {
        return getPos().getX() + getSize().getX();
    }

    @Override
    public int getTop() {
        return getPos().getY();
    }

    @Override
    public int getBottom() {
        return getPos().getY() + getSize().getY();
    }

    @Override
    public IWidget setPos(double x, double y) {
        return setPos(x, y, geom.fracPosX, geom.fracPosY);
    }

    @Override
    public IWidget setPos(double x, double y, boolean fracX, boolean fracY) {
        geom.setPos(x, y, fracX, fracY);
        emit(Signal.GEOM_CHANGED, geom);
        return this;
    }

    @Override
    public IWidget setSize(double sx, double sy) {
        return setSize(sx, sy, geom.fracSizeX, geom.fracSizeY);
    }

    @Override
    public IWidget setSize(double sx, double sy, boolean fracX, boolean fracY) {
        geom.setSize(sx, sy, fracX, fracY);
        emit(Signal.GEOM_CHANGED, geom);
        return this;
    }

    @Override
    public IWidget adjustSize() {
        int minleft = 9999;
        int maxright = 0;
        int mintop = 9999;
        int maxbottom = 0;

        for (IWidget widget : widgets.values()) {
            minleft = Math.min(minleft, widget.getLeft());
            maxright = Math.max(maxright, widget.getRight());

            mintop = Math.min(mintop, widget.getTop());
            maxbottom = Math.max(maxbottom, widget.getBottom());
        }

        setSize(maxright - minleft, maxbottom - mintop, false, false);

        emit(Signal.GEOM_CHANGED, geom);
        return this;
    }

    ////////////////////////////
    // SOME RENDERING HELPERS //
    ////////////////////////////

    protected void saveGLState() {
        hasBlending = GL11.glGetBoolean(GL11.GL_BLEND);
        hasLight = GL11.glGetBoolean(GL11.GL_LIGHTING);
        boundTexIndex = GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D);
        GL11.glPushAttrib(GL11.GL_CURRENT_BIT);
        GL11.glPushMatrix();
    }

    protected void loadGLState() {
        if (hasBlending) {
            GL11.glEnable(GL11.GL_BLEND);
        } else {
            GL11.glDisable(GL11.GL_BLEND);
        }
        if (hasLight) {
            GL11.glEnable(GL11.GL_LIGHTING);
        } else {
            GL11.glDisable(GL11.GL_LIGHTING);
        }
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, boundTexIndex);
        GL11.glPopMatrix();
        GL11.glPopAttrib();
        //GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    }

    @Override
    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    @Override
    public float getAlpha() {
        return alpha;
    }

    @Override
    public void show() {
        isRendering = true;
    }

    @Override
    public void hide() {
        isRendering = false;
    }

    @Override
    public boolean shouldRender() {
        return isRendering;
    }

    ////////////////////
    // INPUT HANDLING //
    ////////////////////

    @Override
    public void handleMouseInput() {
    }

    @Override
    public void onMouseClick(MouseEvent event) {
        //System.out.printf("%s %s\n", this, event);
        if (parent != null) {
            parent.onMouseClick(event);
        }

        //IWidget widget = this.getWidgetAtCoordinates(event.x, event.y);
        //if (widget != null && widget != this)
        //	widget.onMouseClick(event);
    }

    @Override
    public void onMouseDrag(MouseEvent event) {
        //System.out.printf("%s %s\n", this, event);
        if (parent != null) {
            parent.onMouseDrag(event);
        }

        //IWidget widget = this.getWidgetAtCoordinates(event.x, event.y);
        //if (widget != null && widget != this)
        //	widget.onMouseDrag(event);
    }

    @Override
    public void onMouseMove(MouseEvent event) {
        //System.out.printf("%s %s\n", this, event);
        if (parent != null) {
            parent.onMouseMove(event);
        }

        //IWidget widget = this.getWidgetAtCoordinates(event.x, event.y);
        //if (widget != null && widget != this)
        //	widget.onMouseMove(event);
    }

    @Override
    public void onMouseRelease(MouseEvent event) {
        //System.out.printf("%s %s\n", this, event);
        if (parent != null) {
            parent.onMouseRelease(event);
        }

        //IWidget widget = this.getWidgetAtCoordinates(event.x, event.y);
        //if (widget != null && widget != this)
        //	widget.onMouseRelease(event);
    }

    @Override
    public void onMouseWheel(MouseEvent event) {
        //System.out.printf("%s %s\n", this, event);
        if (parent != null) {
            parent.onMouseWheel(event);
        }
    }

    @Override
    public void onMouseEnter(MouseEvent event) {
        //System.out.printf("%s %s\n", this, event);
        if (parent != null) {
            parent.onMouseEnter(event);
        }
    }

    @Override
    public void onMouseLeave(MouseEvent event) {
        //System.out.printf("%s %s\n", this, event);
        if (parent != null) {
            parent.onMouseLeave(event);
        }
    }

    @Override
    public void emit(Signal signal, Object... params) {
        if (parent != null) {
            parent.onWidgetEvent(this, signal, params);
        }
    }

    @Override
    public void onWidgetEvent(IWidget srcwidget, Signal signal, Object... params) {
        if (parent != null) {
            parent.onWidgetEvent(srcwidget, signal, params);
        }
    }
}
