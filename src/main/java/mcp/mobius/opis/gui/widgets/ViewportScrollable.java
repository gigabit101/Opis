package mcp.mobius.opis.gui.widgets;

import mcp.mobius.opis.gui.events.MouseEvent;
import mcp.mobius.opis.gui.helpers.UIHelper;
import mcp.mobius.opis.gui.interfaces.CType;
import mcp.mobius.opis.gui.interfaces.IWidget;
import mcp.mobius.opis.gui.interfaces.Signal;
import mcp.mobius.opis.gui.interfaces.WAlign;
import org.lwjgl.util.Point;

public class ViewportScrollable extends WidgetBase {

    public class Escalator extends WidgetBase {

        int yOffset = 0;
        int sizeCursor = 8;
        int maxValue = 0;
        int step = 0;
        boolean drag = false;

        public Escalator(IWidget parent, int step) {
            this.parent = parent;
            this.step = step;
        }

        public void setOffset(int yoffset) {
            yOffset = yoffset;
        }

        public void setMaxValue(int value) {
            maxValue = value;
        }

        public void setStep(int step) {
            this.step = step;
        }

        @Override
        public void draw(Point pos) {
            UIHelper.drawGradientRect(getLeft(), getTop(), getRight(), getBottom(), 1, 0xff999999, 0xff999999);
            int offsetScaled = (int) (((double) getSize().getY() - (double) sizeCursor + 1) / (double) maxValue * yOffset);
            UIHelper.drawGradientRect(getLeft(), getTop() + offsetScaled, getRight(), getTop() + offsetScaled + sizeCursor, 1, 0xffffffff, 0xffffffff);
        }

        @Override
        public void onMouseClick(MouseEvent event) {
            if (event.button == 0) {
                int offsetScaled = getTop() + (int) (((double) getSize().getY() - (double) sizeCursor + 1) / (double) maxValue * yOffset);

                drag = false;

                if (event.y < offsetScaled) {
                    yOffset += step;
                } else if (event.y > offsetScaled + sizeCursor) {
                    yOffset -= step;
                } else {
                    drag = true;
                }

                emit(Signal.VALUE_CHANGED, yOffset);

            } else {
                super.onMouseClick(event);
            }
        }

        @Override
        public void onMouseRelease(MouseEvent event) {
            if (event.button == 0) {
                drag = false;
            }
            super.onMouseRelease(event);
        }

        @Override
        public void onMouseDrag(MouseEvent event) {
            if (drag) {
                int relativeY = (int) event.y - getTop();
                double factor = ((double) getSize().getY() - (double) sizeCursor + 1) / (double) maxValue;
                yOffset = (int) (relativeY / factor);
                yOffset = Math.max(yOffset, maxValue);
                yOffset = Math.min(yOffset, 0);
                emit(Signal.VALUE_CHANGED, yOffset);
                emit(Signal.DRAGGED, this);
            } else {
                super.onMouseDrag(event);
            }
        }

    }

    IWidget attachedWidget = null;
    int yOffset = 0;
    int step = 5;

    public ViewportScrollable(IWidget parent) {
        super(parent);
        addWidget("Cropping", new LayoutCropping(null)).setGeometry(new WidgetGeometry(0.0, 0.0, 100.0, 100.0, CType.RELXY, CType.RELXY, WAlign.LEFT, WAlign.TOP));
        addWidget("Escalator", new Escalator(null, step * 5)).setGeometry(new WidgetGeometry(100.0, 0, 8, 100.0, CType.RELXY, CType.REL_Y, WAlign.RIGHT, WAlign.TOP)).hide();
    }

    public IWidget attachWidget(IWidget widget) {
        attachedWidget = getWidget("Cropping").addWidget("Cropped", widget);
        return attachedWidget;
    }

    public IWidget getAttachedWidget() {
        return attachedWidget;
    }

    public IWidget setStep(int step) {
        this.step = step;
        ((Escalator) getWidget("Escalator")).setStep(this.step * 5);
        return this;
    }

    public int getOffset() {
        return yOffset;
    }

    @Override
    public void draw(Point pos) {
    }

    @Override
    public void draw() {
        //if (Display.wasResized())

        if ((attachedWidget != null) && (attachedWidget.getSize().getY() > getSize().getY())) {
            getWidget("Escalator").show();
        } else {
            getWidget("Escalator").hide();
        }

        super.draw();
    }

    @Override
    public void onMouseWheel(MouseEvent event) {
        yOffset += event.z / 120.0 * step;

        yOffset = Math.max(yOffset, getSize().getY() - attachedWidget.getSize().getY());
        yOffset = Math.min(yOffset, 0);

        ((LayoutCropping) getWidget("Cropping")).setOffsets(0, yOffset);
        ((Escalator) getWidget("Escalator")).setOffset(yOffset);
    }

    @Override
    public void onWidgetEvent(IWidget srcwidget, Signal signal, Object... params) {
        if (srcwidget.equals(attachedWidget) && signal == Signal.GEOM_CHANGED) {
            ((Escalator) getWidget("Escalator")).setMaxValue(getSize().getY() - srcwidget.getSize().getY());
        } else if (srcwidget.equals(getWidget("Escalator")) && signal == Signal.VALUE_CHANGED) {
            yOffset = (Integer) params[0];
            ((LayoutCropping) getWidget("Cropping")).setOffsets(0, yOffset);
        } else {
            super.onWidgetEvent(srcwidget, signal, params);
        }

    }

    @Override
    public void onMouseDrag(MouseEvent event) {
        if (getWidget("Escalator").shouldRender() && ((Escalator) getWidget("Escalator")).drag) {
            getWidget("Escalator").onMouseDrag(event);
        } else {
            super.onMouseDrag(event);
        }
    }

    @Override
    public void onMouseClick(MouseEvent event) {
        if (event.button == 0) {
            if (!getWidget("Escalator").isWidgetAtCoordinates(event.x, event.y)) {
                ((Escalator) getWidget("Escalator")).drag = false;
                super.onMouseClick(event);
            } else {
                getWidget("Escalator").onMouseClick(event);
            }
        } else {
            super.onMouseClick(event);
        }
    }

}
