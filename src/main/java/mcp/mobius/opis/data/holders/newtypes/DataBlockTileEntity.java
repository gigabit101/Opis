package mcp.mobius.opis.data.holders.newtypes;

import io.netty.buffer.ByteBuf;
import mcp.mobius.opis.data.holders.ISerializable;
import mcp.mobius.opis.data.holders.basetypes.CoordinatesBlock;
import mcp.mobius.opis.profiler.Profilers;
import mcp.mobius.opis.util.DimBlockPos;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.util.Map;

public class DataBlockTileEntity implements ISerializable, Comparable {

    public short id;
    public short meta;
    public CoordinatesBlock pos;
    public DataTiming update;

    public DataBlockTileEntity fill(CoordinatesBlock coord) {
        pos = coord;
        World world = DimensionManager.getWorld(pos.dim);

        id = (short) Block.getIdFromBlock(world.getBlockState(new BlockPos(pos.x, pos.y, pos.z)).getBlock());
        IBlockState state = world.getBlockState(new BlockPos(pos.x, pos.y, pos.z));
        meta = (short) state.getBlock().getMetaFromState(state);

        Map<DimBlockPos, DescriptiveStatistics> data = Profilers.TILE_UPDATE.get().data;
        update = new DataTiming(data.containsKey(pos.toNew()) ? data.get(pos.toNew()).getGeometricMean() : 0.0D);

        return this;
    }

    @Override
    public void writeToStream(ByteBuf stream) {
        stream.writeShort(id);
        stream.writeShort(meta);
        pos.writeToStream(stream);
        update.writeToStream(stream);
    }

    public static DataBlockTileEntity readFromStream(ByteBuf stream) {
        DataBlockTileEntity retVal = new DataBlockTileEntity();
        retVal.id = stream.readShort();
        retVal.meta = stream.readShort();
        retVal.pos = CoordinatesBlock.readFromStream(stream);
        retVal.update = DataTiming.readFromStream(stream);
        return retVal;
    }

    @Override
    public int compareTo(Object o) {
        return update.compareTo(((DataBlockTileEntity) o).update);
    }

}
