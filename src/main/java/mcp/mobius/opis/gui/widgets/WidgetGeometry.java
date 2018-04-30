package mcp.mobius.opis.gui.widgets;

import mcp.mobius.opis.gui.interfaces.CType;
import mcp.mobius.opis.gui.interfaces.IWidget;
import mcp.mobius.opis.gui.interfaces.WAlign;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.util.Point;

//public class WidgetGeometry implements Cloneable{
public class WidgetGeometry {

    //public enum Align {LEFT, CENTER, RIGHT, TOP, BOTTOM};

    double x = -1;
    double y = -1;
    double sx = -1;
    double sy = -1;

    CType posType;
    CType sizeType;

    boolean fracPosX = false;
    boolean fracPosY = false;
    boolean fracSizeX = false;
    boolean fracSizeY = false;

    WAlign alignX;
    WAlign alignY;

    public class PointDouble {

        double x;
        double y;

        public PointDouble(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public String toString() {
            return String.format("PointDouble : %.5f %.5f", x, y);
        }
    }

    public WidgetGeometry(double x, double y, double sx, double sy, CType fracPos, CType fracSize) {
        this(x, y, sx, sy, fracPos, fracSize, WAlign.LEFT, WAlign.TOP);
    }

    public WidgetGeometry(double x, double y, double sx, double sy, CType fracPos, CType fracSize, WAlign alignX, WAlign alignY) {
        this.x = x;
        this.y = y;
        this.sx = sx;
        this.sy = sy;
        posType = fracPos;
        sizeType = fracSize;

        switch (fracPos) {
            case REL_X:
                fracPosX = true;
                break;
            case RELXY:
                fracPosX = true;
                fracPosY = true;
                break;
            case REL_Y:
                fracPosY = true;
                break;
            default:
                break;

        }

        switch (fracSize) {
            case REL_X:
                fracSizeX = true;
                break;
            case RELXY:
                fracSizeX = true;
                fracSizeY = true;
                break;
            case REL_Y:
                fracSizeY = true;
                break;
            default:
                break;

        }

        this.alignX = alignX;
        this.alignY = alignY;
    }

    public void setPos(double x, double y, boolean fracX, boolean fracY) {
        this.x = x;
        this.y = y;
        fracPosX = fracX;
        fracPosY = fracY;
    }

    public void setSize(double sx, double sy, boolean fracSizeX, boolean fracSizeY) {
        this.sx = sx;
        this.sy = sy;
        this.fracSizeX = fracSizeX;
        this.fracSizeY = fracSizeY;
    }

    public PointDouble getRawPos() {
        return new PointDouble(x, y);
    }

    public Point getUnalignedPos(IWidget parent) {
        int x = -1;
        if (fracPosX) {
            x = MathHelper.ceil(parent.getPos().getX() + parent.getSize().getX() * this.x / 100D);
        }
        if (!fracPosX && parent != null) {
            x = parent.getPos().getX() + (int) this.x;
        }
        if (!fracPosX && parent == null) {
            x = (int) this.x;
        }

        int y = -1;
        if (fracPosY) {
            y = MathHelper.ceil(parent.getPos().getY() + parent.getSize().getY() * this.y / 100D);
        }
        if (!fracPosY && parent != null) {
            y = parent.getPos().getY() + (int) this.y;
        }
        if (!fracPosY && parent == null) {
            y = (int) this.y;
        }

        return new Point(x, y);
    }

    public Point getPos(IWidget parent) {

        int x = -1;
        if (fracPosX) {
            x = MathHelper.ceil(parent.getPos().getX() + parent.getSize().getX() * this.x / 100D);
        }
        if (!fracPosX && parent != null) {
            x = parent.getPos().getX() + (int) this.x;
        }
        if (!fracPosX && parent == null) {
            x = (int) this.x;
        }

        int y = -1;
        if (fracPosY) {
            y = MathHelper.ceil(parent.getPos().getY() + parent.getSize().getY() * this.y / 100D);
        }
        if (!fracPosY && parent != null) {
            y = parent.getPos().getY() + (int) this.y;
        }
        if (!fracPosY && parent == null) {
            y = (int) this.y;
        }

        if (alignX == WAlign.CENTER) {
            x -= getSize(parent).getX() / 2;
        }

        if (alignY == WAlign.CENTER) {
            y -= getSize(parent).getY() / 2;
        }

        if (alignX == WAlign.RIGHT) {
            x -= getSize(parent).getX() - 1;
        }

        if (alignY == WAlign.BOTTOM) {
            y -= getSize(parent).getY() - 1;
        }

        return new Point(x, y);
    }

    public Point getSize(IWidget parent) {
        int sx = -1;
        if (fracSizeX) {
            sx = MathHelper.ceil(parent.getSize().getX() * this.sx / 100D);
        }
        if (!fracSizeX) {
            sx = (int) this.sx;
        }

        int sy = -1;
        if (fracSizeY) {
            sy = MathHelper.ceil(parent.getSize().getY() * this.sy / 100D);
        }
        if (!fracSizeY) {
            sy = (int) this.sy;
        }

        return new Point(sx, sy);
    }

    public String toString() {
        return String.format("Geometry : [%s %s] [%s %s] [%s %s] [%s %s]", x, y, sx, sy, posType, sizeType, alignX, alignY);
    }

    //@Override
    //public WidgetGeometry clone() throws CloneNotSupportedException{
    //	return (WidgetGeometry)super.clone();
    //}
}
