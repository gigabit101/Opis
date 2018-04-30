package mcp.mobius.opis.gui.widgets;

import mcp.mobius.opis.gui.events.MouseEvent;
import mcp.mobius.opis.gui.helpers.UIHelper;
import mcp.mobius.opis.gui.interfaces.IWidget;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Point;

public class PictureSwitch extends WidgetBase {

    private ResourceLocation texture1;
    private ResourceLocation texture2;
    private boolean mouseOver = false;

    public PictureSwitch(IWidget parent, String uri1, String uri2) {
        super(parent);
        texture1 = new ResourceLocation(uri1);
        texture2 = new ResourceLocation(uri2);
    }

    @Override
    public void draw(Point pos) {
        saveGLState();

        ResourceLocation texture = mouseOver ? texture2 : texture1;

        GL11.glPushMatrix();
        texManager.bindTexture(texture);
        UIHelper.drawTexture(pos.getX(), pos.getY(), getSize().getX(), getSize().getY());
        GL11.glPopMatrix();

        loadGLState();
    }

    @Override
    public void onMouseEnter(MouseEvent event) {
        //System.out.printf("%s %s\n", this, event);
        mouseOver = true;
    }

    @Override
    public void onMouseLeave(MouseEvent event) {
        //System.out.printf("%s %s\n", this, event);
        mouseOver = false;
    }

}
