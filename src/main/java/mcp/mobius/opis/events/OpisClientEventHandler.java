package mcp.mobius.opis.events;

import codechicken.lib.render.RenderUtils;
import codechicken.lib.vec.Cuboid6;
import mcp.mobius.opis.Opis;
import mcp.mobius.opis.data.holders.basetypes.CoordinatesBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

public class OpisClientEventHandler {

    public static final Cuboid6 BOX = Cuboid6.full.copy().expand(0.02);

    @SubscribeEvent
    @SideOnly (Side.CLIENT)
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        if (Opis.selectedBlock == null) {
            return;
        }
        if (Minecraft.getMinecraft().world.provider.getDimension() != Opis.selectedBlock.dim) {
            return;
        }
        if (Minecraft.getMinecraft().world.isAirBlock(new BlockPos(Opis.selectedBlock.x, Opis.selectedBlock.y, Opis.selectedBlock.z))) {
            return;
        }

        GlStateManager.pushMatrix();
        RenderUtils.translateToWorldCoords(Minecraft.getMinecraft().getRenderViewEntity(), event.getPartialTicks());
        CoordinatesBlock pos = Opis.selectedBlock;
        GlStateManager.translate(pos.x, pos.y, pos.z);

        GL11.glPushAttrib(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.depthMask(false);

        RenderUtils.drawCuboidSolid(BOX);

        GlStateManager.enableTexture2D();
        GL11.glPopAttrib();

        GlStateManager.popMatrix();
    }

}
