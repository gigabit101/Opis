package mcp.mobius.opis.data.holders.newtypes;

import mcp.mobius.opis.data.holders.basetypes.CoordinatesBlock;
import mcp.mobius.opis.data.profilers.ProfilerRenderTileEntity;
import mcp.mobius.opis.profiler.ProfilerSection;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.util.WeakHashMap;

public class DataTileEntityRender extends DataBlockTileEntity {

    public DataTileEntityRender fill(TileEntity ent) {
        pos = new CoordinatesBlock(ent.getWorld().provider.getDimension(), ent.getPos().getX(), ent.getPos().getY(), ent.getPos().getZ());
        World world = Minecraft.getMinecraft().world; //DimensionManager.getWorld(this.pos.dim);

        id = (short) Block.getIdFromBlock(world.getBlockState(new BlockPos(pos.x, pos.y, pos.z)).getBlock());
        //TODO
        //		this.meta   = (short) world.getBlockMetadata(this.pos.x, this.pos.y, this.pos.z);

        WeakHashMap<TileEntity, DescriptiveStatistics> data = ((ProfilerRenderTileEntity) ProfilerSection.RENDER_TILEENTITY.getProfiler()).data;
        update = new DataTiming(data.containsKey(ent) ? data.get(ent).getGeometricMean() : 0.0D);

        return this;
    }

}
