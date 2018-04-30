package mcp.mobius.opis.gui.widgets;

import mcp.mobius.opis.gui.helpers.UIHelper;
import mcp.mobius.opis.gui.interfaces.IWidget;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Point;

public class PictureDisplay extends WidgetBase {

    protected ResourceLocation texture;

    public PictureDisplay(IWidget parent, String uri) {
        super(parent);
        texture = new ResourceLocation(uri);
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
