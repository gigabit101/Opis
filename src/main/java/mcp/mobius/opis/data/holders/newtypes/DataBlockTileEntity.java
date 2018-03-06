package mcp.mobius.opis.data.holders.newtypes;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
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
        this.pos = coord;
        World world = DimensionManager.getWorld(this.pos.dim);

        this.id = (short) Block.getIdFromBlock(world.getBlockState(new BlockPos(this.pos.x, this.pos.y, this.pos.z)).getBlock());
        IBlockState state = world.getBlockState(new BlockPos(pos.x, pos.y, pos.z));
		this.meta   = (short) state.getBlock().getMetaFromState(state);

        Map<DimBlockPos, DescriptiveStatistics> data = Profilers.TILE_UPDATE.get().data;
        this.update = new DataTiming(data.containsKey(this.pos.toNew()) ? data.get(this.pos.toNew()).getGeometricMean() : 0.0D);

        return this;
    }

    @Override
    public void writeToStream(ByteArrayDataOutput stream) {
        stream.writeShort(this.id);
        stream.writeShort(this.meta);
        this.pos.writeToStream(stream);
        this.update.writeToStream(stream);
    }

    public static DataBlockTileEntity readFromStream(ByteArrayDataInput stream) {
        DataBlockTileEntity retVal = new DataBlockTileEntity();
        retVal.id = stream.readShort();
        retVal.meta = stream.readShort();
        retVal.pos = CoordinatesBlock.readFromStream(stream);
        retVal.update = DataTiming.readFromStream(stream);
        return retVal;
    }

    @Override
    public int compareTo(Object o) {
        return this.update.compareTo(((DataBlockTileEntity) o).update);
    }

}
