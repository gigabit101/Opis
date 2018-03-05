package mcp.mobius.opis.gui.helpers;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

@Deprecated //TODO Covers: CCL has GuiDraw. Forge has GuiUtils. This should be dead.
public class UIHelper {

    public static void drawTexture(int posX, int posY, int sizeX, int sizeY) {
        UIHelper.drawTexture(posX, posY, sizeX, sizeY, 0, 0, 256, 256);
    }

    public static void drawTexture(int posX, int posY, int sizeX, int sizeY, int texU, int texV, int texSizeU, int texSizeV) {
        float zLevel = 0.0F;
        float f = 0.00390625F;

        Tessellator tess = Tessellator.getInstance();
        BufferBuilder buffer = tess.getBuffer();
        buffer.begin(0x07, DefaultVertexFormats.POSITION_TEX);
        buffer.pos((double) (posX + 0), (double) (posY + sizeY), (double) zLevel).tex(texU * f, (texV + texSizeV) * f).endVertex();
        buffer.pos((double) (posX + sizeX), (double) (posY + sizeY), (double) zLevel).tex((texU + texSizeU) * f, (texV + texSizeV) * f).endVertex();
        buffer.pos((double) (posX + sizeX), (double) (posY + 0), (double) zLevel).tex((texU + texSizeU) * f, texV * f).endVertex();
        buffer.pos((double) (posX + 0), (double) (posY + 0), (double) zLevel).tex(texU * f, texV * f).endVertex();
        tess.draw();
    }

    public static void drawGradientRect(int left, int top, int right, int bottom, int zLevel, int startColor, int endColor) {
        float f = (float) (startColor >> 24 & 255) / 255.0F;
        float f1 = (float) (startColor >> 16 & 255) / 255.0F;
        float f2 = (float) (startColor >> 8 & 255) / 255.0F;
        float f3 = (float) (startColor & 255) / 255.0F;
        float f4 = (float) (endColor >> 24 & 255) / 255.0F;
        float f5 = (float) (endColor >> 16 & 255) / 255.0F;
        float f6 = (float) (endColor >> 8 & 255) / 255.0F;
        float f7 = (float) (endColor & 255) / 255.0F;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos((double) right, (double) top, (double) zLevel).color(f1, f2, f3, f).endVertex();
        bufferbuilder.pos((double) left, (double) top, (double) zLevel).color(f1, f2, f3, f).endVertex();
        bufferbuilder.pos((double) left, (double) bottom, (double) zLevel).color(f5, f6, f7, f4).endVertex();
        bufferbuilder.pos((double) right, (double) bottom, (double) zLevel).color(f5, f6, f7, f4).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

//    public void drawLine(int x, int y, int x2, int y2, int width, float... rgba) {
//
//        Tessellator tess = Tessellator.instance;
//        GL11.glPushMatrix();
//        //GL11.glLineWidth(3);
//        GL11.glLineWidth(width);
//        GL11.glDisable(GL11.GL_TEXTURE_2D);
//        tess.startDrawing(3);
//
//        if (rgba.length == 4) {
//            tess.setColorRGBA_F(rgba[0], rgba[1], rgba[2], rgba[3]);
//        }
//        //tess.setColorRGBA_F(1,1,1,1);
//
//        tess.addVertex(x, y, 0);
//        tess.addVertex(x2, y2, 0);
//
//        tess.draw();
//        GL11.glDisable(32826 /*GL_RESCALE_NORMAL_EXT*/);
//        GL11.glEnable(GL11.GL_TEXTURE_2D);
//        GL11.glPopMatrix();
//    }

}
