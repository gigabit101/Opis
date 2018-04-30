package mcp.mobius.opis.gui.widgets;

import mcp.mobius.opis.gui.interfaces.IWidget;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Point;

public class LayoutCropping extends LayoutBase {

    int xOffset = 0;
    int yOffset = 0;

    public LayoutCropping(IWidget parent) {
        super(parent);
    }

    public void setOffsets(int xoffset, int yoffset) {
        xOffset = xoffset;
        yOffset = yoffset;
    }

    @Override
    public void draw() {
        rez = new ScaledResolution(mc);
        saveGLState();

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, alpha);

        draw(getPos());

        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor(getPos().getX() * rez.getScaleFactor(), (rez.getScaledHeight() - (getPos().getY() + getSize().getY())) * rez.getScaleFactor(), getSize().getX() * rez.getScaleFactor(), getSize().getY() * rez.getScaleFactor());
        //GL11.glScissor(this.getPos().getX()*this.rez.getScaleFactor(), this.getPos().getY()*this.rez.getScaleFactor(), this.getSize().getX()*this.rez.getScaleFactor(), this.getSize().getY()*this.rez.getScaleFactor());

        //this.setPos(this.getLeft() - xOffset, this.getTop() - yOffset);

        GL11.glTranslatef(xOffset, yOffset, 0.0f);

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

        //this.setPos(this.getLeft() + xOffset, this.getTop() + yOffset);

        GL11.glDisable(GL11.GL_SCISSOR_TEST);

        loadGLState();
    }

    @Override
    public void draw(Point pos) {
        super.draw(pos);
    }

}
