package mcp.mobius.opis.gui.widgets.buttons;

import mcp.mobius.opis.gui.events.MouseEvent;
import mcp.mobius.opis.gui.helpers.UIHelper;
import mcp.mobius.opis.gui.interfaces.IWidget;
import mcp.mobius.opis.gui.interfaces.Signal;
import mcp.mobius.opis.gui.widgets.LabelFixedFont;
import mcp.mobius.opis.gui.widgets.WidgetBase;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Point;

public abstract class ButtonBase extends WidgetBase {

    protected boolean mouseOver = false;
    protected static ResourceLocation widgetsTexture = new ResourceLocation("textures/gui/widgets.png");

    public ButtonBase(IWidget parent) {
        super(parent);
    }

    @Override
    public void draw() {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        for (IWidget widget : widgets.values()) {
            if (widget instanceof LabelFixedFont) {
                if (mouseOver) {
                    ((LabelFixedFont) widget).setColor(0xffffa0);
                } else {
                    ((LabelFixedFont) widget).setColor(0xffffff);
                }
            }
        }

        super.draw();
    }

    @Override
    public void draw(Point pos) {
        saveGLState();
        int texOffset = 0;

        if (mouseOver) {
            texOffset = 1;
        }

        mc.getTextureManager().bindTexture(widgetsTexture);
        UIHelper.drawTexture(getPos().getX(), getPos().getY(), getSize().getX(), getSize().getY(), 0, 66 + texOffset * 20, 200, 20);

        loadGLState();
    }

    @Override
    public void onMouseEnter(MouseEvent event) {
        mouseOver = true;
    }

    @Override
    public void onMouseLeave(MouseEvent event) {
        mouseOver = false;
    }

    @Override
    public IWidget getWidgetAtCoordinates(double posX, double posY) {
        return this;
    }

    @Override
    public void onMouseClick(MouseEvent event) {
        if (event.button == 0) {
            mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
        }

        emit(Signal.CLICKED, event.button);
    }
}
