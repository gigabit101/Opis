package mcp.mobius.opis.data.holders.newtypes;

import mcp.mobius.opis.data.holders.basetypes.CoordinatesBlock;
import mcp.mobius.opis.data.profilers.ProfilerRenderBlock;
import mcp.mobius.opis.profiler.ProfilerSection;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.util.HashMap;

public class DataBlockRender extends DataBlockTileEntity {

    public DataBlockRender fill(CoordinatesBlock coord) {
        pos = coord;
        World world = Minecraft.getMinecraft().world;

        id = (short) Block.getIdFromBlock(world.getBlockState(new BlockPos(pos.x, pos.y, pos.z)).getBlock());
        IBlockState state = world.getBlockState(new BlockPos(pos.x, pos.y, pos.z));
        meta = (short) state.getBlock().getMetaFromState(state);

        HashMap<CoordinatesBlock, DescriptiveStatistics> data = ((ProfilerRenderBlock) ProfilerSection.RENDER_BLOCK.getProfiler()).data;
        update = new DataTiming(data.containsKey(coord) ? data.get(coord).getGeometricMean() : 0.0D);

        return this;
    }

}
