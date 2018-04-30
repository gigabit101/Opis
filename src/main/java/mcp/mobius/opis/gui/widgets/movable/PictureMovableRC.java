package mcp.mobius.opis.gui.widgets.movable;

import mcp.mobius.opis.gui.events.MouseEvent;
import mcp.mobius.opis.gui.helpers.UIHelper;
import mcp.mobius.opis.gui.interfaces.IWidget;
import mcp.mobius.opis.gui.interfaces.Signal;
import mcp.mobius.opis.gui.widgets.WidgetBase;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Point;

// A movable picture setup especially for centered relative positions.

public class PictureMovableRC extends WidgetBase {

    private double offsetX, offsetY;
    protected ResourceLocation texture;

    public PictureMovableRC(IWidget parent, String uri) {
        super(parent);
        texture = new ResourceLocation(uri);
    }

    @Override
    public void onMouseClick(MouseEvent event) {
        offsetX = event.x - geom.getUnalignedPos(parent).getX();
        offsetY = event.y - geom.getUnalignedPos(parent).getY();
        //System.out.printf("%s %s\n", this.offsetX, this.offsetY);
    }

    @Override
    public void onMouseDrag(MouseEvent event) {
        double newX = event.x - offsetX;
        double newY = event.y - offsetY;

        newX = Math.max(newX, parent.getLeft());
        newY = Math.max(newY, parent.getTop());

        newX = Math.min(newX, parent.getRight());
        newY = Math.min(newY, parent.getBottom());

        setPos(((newX - parent.getLeft()) / parent.getSize().getX()) * 100.0, ((newY - parent.getTop()) / parent.getSize().getY()) * 100.0);

        //System.out.printf("%s\n", this.parent.getGeometry());
        //System.out.printf("%s %s\n", this.parent.getPos(), this.parent.getSize());

        emit(Signal.DRAGGED, getPos());
    }

    @Override
    public void draw(Point pos) {
        saveGLState();

        GL11.glPushMatrix();
        texManager.bindTexture(texture);
        UIHelper.drawTexture(pos.getX(), pos.getY(), getSize().getX(), getSize().getY());
        GL11.glPopMatrix();

        loadGLState();
    }
}

