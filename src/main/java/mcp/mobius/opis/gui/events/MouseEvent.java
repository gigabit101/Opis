package mcp.mobius.opis.gui.events;

import mcp.mobius.opis.gui.interfaces.IWidget;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Mouse;

public class MouseEvent {

    public enum EventType {
        NONE,
        MOVE,
        CLICK,
        RELEASED,
        DRAG,
        WHEEL,
        ENTER,
        LEAVE
    }

    public long timestamp;
    public Minecraft mc;
    public IWidget srcwidget;
    public IWidget trgwidget;
    public double x, y;
    public int z;
    public static int buttonCount = Mouse.getButtonCount();
    public boolean[] buttonState = new boolean[buttonCount];
    public EventType type;
    public int button = -1;

    public MouseEvent(IWidget widget) {
        srcwidget = widget;
        timestamp = System.nanoTime();

        mc = Minecraft.getMinecraft();

        x = (double) Mouse.getEventX() * (double) srcwidget.getSize().getX() / (double) mc.displayWidth;
        y = (double) srcwidget.getSize().getY() - (double) Mouse.getEventY() * (double) srcwidget.getSize().getY() / (double) mc.displayHeight - 1.0;

        z = Mouse.getDWheel();

        for (int i = 0; i < buttonCount; i++) {
            buttonState[i] = Mouse.isButtonDown(i);
        }

        trgwidget = srcwidget.getWidgetAtCoordinates(x, y);
    }

    public String toString() {
        String retstring = String.format("MOUSE %s :  [%s] [ %.2f %.2f %d ] [", type, timestamp, x, y, z);
        if (buttonCount < 5) {
            for (int i = 0; i < buttonCount; i++) {
                retstring += String.format(" %s ", buttonState[i]);
            }
        } else {
            for (int i = 0; i < 5; i++) {
                retstring += String.format(" %s ", buttonState[i]);
            }
        }
        retstring += "]";

        if (button != -1) {
            retstring += String.format(" Button %s", button);
        }

        return retstring;
    }

    // Returns the event type based on the previous mouse event.
    public EventType getEventType(MouseEvent me) {

        type = EventType.NONE;

        if (trgwidget != me.trgwidget) {
            type = EventType.ENTER;
            return type;
        }

        if (z != 0) {
            type = EventType.WHEEL;
            return type;
        }

        for (int i = 0; i < buttonCount; i++) {
            if (buttonState[i] != me.buttonState[i]) {
                if (buttonState[i] == true) {
                    type = EventType.CLICK;
                } else {
                    type = EventType.RELEASED;
                }
                button = i;
                return type;
            }
        }

        //MOVE & DRAG EVENTS (we moved the mouse and button 0 was clicked or not)
        if ((x != me.x) || (y != me.y)) {
            if (buttonState[0] == true) {
                type = EventType.DRAG;
            } else {
                type = EventType.MOVE;
            }
            return type;
        }

        return type;
    }
}
