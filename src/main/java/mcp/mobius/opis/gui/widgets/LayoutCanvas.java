package mcp.mobius.opis.gui.widgets;

import mcp.mobius.opis.gui.events.MouseEvent;
import mcp.mobius.opis.gui.events.MouseEvent.EventType;
import mcp.mobius.opis.gui.interfaces.CType;
import mcp.mobius.opis.gui.interfaces.IWidget;
import mcp.mobius.opis.gui.interfaces.Signal;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.Point;

public class LayoutCanvas extends LayoutBase {

    private MouseEvent lastMouseEvent;
    private IWidget draggedWidget = null;

    public LayoutCanvas() {
        super(null);
        setGeometry(0, 0, rez.getScaledWidth(), rez.getScaledHeight(), CType.ABSXY, CType.ABSXY);
        Mouse.getDWheel();    // This is to "calibrate" the DWheel
        lastMouseEvent = new MouseEvent(this);
    }

    @Override
    public void draw() {
        rez = new ScaledResolution(mc);
        setGeometry(0, 0, rez.getScaledWidth(), rez.getScaledHeight(), CType.ABSXY, CType.ABSXY);

		/*
		this.draw(this.getPos());
		
		for (IWidget widget: this.widgets.values())
			if (widget.shouldRender())
				widget.draw();
		*/

        super.draw();

        handleMouseInput();
    }

    @Override
    public void draw(Point pos) {
        super.draw(pos);
    }

    @Override
    public void handleMouseInput() {
        // Here we are going to generate the require mouse events we will pass down to all the other widgets
        // This is more or less where the magic happens. If you want mouse support for a widget, it should be
        // attached to a canvas like this one, or any inheriting one.

        MouseEvent event = new MouseEvent(this);
        EventType type = event.getEventType(lastMouseEvent);
        IWidget targetWidget = getWidgetAtCoordinates(event.x, event.y);

        switch (type) {
            case CLICK:
                if (targetWidget != null) {
                    targetWidget.onMouseClick(event);
                }
                //this.onMouseClick(event);
                break;
            case DRAG:
                if (targetWidget != null) {
                    targetWidget.onMouseDrag(event);
                }
                //this.onMouseDrag(event);
                break;
            case MOVE:
                if (targetWidget != null) {
                    targetWidget.onMouseMove(event);
                }
                //this.onMouseMove(event);
                break;
            case RELEASED:
                if (targetWidget != null) {
                    targetWidget.onMouseRelease(event);
                }
                //this.onMouseRelease(event);
                break;
            case WHEEL:
                if (targetWidget != null) {
                    targetWidget.onMouseWheel(event);
                }
                //this.onMouseWheel(event);
                break;
            case ENTER:
                if (event.trgwidget != null) {
                    event.trgwidget.onMouseEnter(event);
                }
                if (lastMouseEvent.trgwidget != null) {
                    event.type = EventType.LEAVE;
                    lastMouseEvent.trgwidget.onMouseLeave(event);
                }
                break;
            case NONE:
                break;
            default:
                break;
        }

        lastMouseEvent = event;
    }

    @Override
    public void onWidgetEvent(IWidget srcwidget, Signal signal, Object... params) {
        if (signal == Signal.DRAGGED) {
            draggedWidget = srcwidget;
        } else {
            super.onWidgetEvent(srcwidget, signal, params);
        }
    }

    @Override
    public void onMouseRelease(MouseEvent event) {
        if (event.button == 0) {
            draggedWidget = null;
        }
        super.onMouseRelease(event);
    }

    @Override
    public void onMouseDrag(MouseEvent event) {
        if (draggedWidget != null) {
            draggedWidget.onMouseDrag(event);
        } else {
            super.onMouseDrag(event);
        }
    }

    public boolean hasWidgetAtCursor() {
        double x = (double) Mouse.getEventX() * (double) getSize().getX() / (double) mc.displayWidth;
        double y = (double) getSize().getY() - (double) Mouse.getEventY() * (double) getSize().getY() / (double) mc.displayHeight - 1.0;
        IWidget widget = getWidgetAtCoordinates(x, y);
        return widget != null && !widget.equals(this);
    }
}
