package mcp.mobius.opis.events;

import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import mcp.mobius.opis.modOpis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.client.event.RenderWorldLastEvent;

public class OpisClientEventHandler {

	@SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onRenderWorldLast(RenderWorldLastEvent event) {
			if (modOpis.selectedBlock == null) return;
			if (Minecraft.getMinecraft().world.provider.getDimension() != modOpis.selectedBlock.dim) return;
			if (Minecraft.getMinecraft().world.isAirBlock(new BlockPos(modOpis.selectedBlock.x, modOpis.selectedBlock.y, modOpis.selectedBlock.z))) return;
		
			double partialTicks = event.getPartialTicks();

            EntityLivingBase player = (EntityLivingBase) Minecraft.getMinecraft().getRenderViewEntity();
            double px = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks;
            double py = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks;
            double pz = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks;

            int bx = modOpis.selectedBlock.x;
            int by = modOpis.selectedBlock.y;
            int bz = modOpis.selectedBlock.z;

            double offset = 0.02;
            double delta = 1 + 2 * offset;

            double x = bx - px - offset;
            double y = by - py - offset;
            double z = bz - pz - offset;

            GL11.glPushAttrib(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glDepthMask(false);

            Tessellator tessellator = Tessellator.getInstance();
            //TODO
//            tessellator.startDrawingQuads();
//
//            tessellator.setColorRGBA(255, 0, 0, 150);
//
//            tessellator.addVertex(x, y, z);
//            tessellator.addVertex(x + delta, y, z);
//            tessellator.addVertex(x + delta, y, z + delta);
//            tessellator.addVertex(x, y, z + delta);
//
//            tessellator.addVertex(x, y + delta, z);
//            tessellator.addVertex(x, y + delta, z + delta);
//            tessellator.addVertex(x + delta, y + delta, z + delta);
//            tessellator.addVertex(x + delta, y + delta, z);
//
//            tessellator.addVertex(x, y, z);
//            tessellator.addVertex(x, y + delta, z);
//            tessellator.addVertex(x + delta, y + delta, z);
//            tessellator.addVertex(x + delta, y, z);
//
//            tessellator.addVertex(x, y, z + delta);
//            tessellator.addVertex(x + delta, y, z + delta);
//            tessellator.addVertex(x + delta, y + delta, z + delta);
//            tessellator.addVertex(x, y + delta, z + delta);
//
//            tessellator.addVertex(x, y, z);
//            tessellator.addVertex(x, y, z + delta);
//            tessellator.addVertex(x, y + delta, z + delta);
//            tessellator.addVertex(x, y + delta, z);
//
//            tessellator.addVertex(x + delta, y, z);
//            tessellator.addVertex(x + delta, y + delta, z);
//            tessellator.addVertex(x + delta, y + delta, z + delta);
//            tessellator.addVertex(x + delta, y, z + delta);

            tessellator.draw();

            GL11.glEnable(GL11.GL_TEXTURE_2D);

            GL11.glPopAttrib();
    }	
	
}
