package mcp.mobius.opis.util;

import codechicken.lib.data.MCDataInput;
import codechicken.lib.data.MCDataOutput;
import mcp.mobius.opis.data.holders.basetypes.CoordinatesBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

import java.util.Objects;

/**
 * A Dim, BlockPos, pair.
 * Used exactly like a pair, Except no generics.
 * Also removes Boxing of primitive type, and cleaner
 * constructors.
 *
 * Created by covers1624 on 6/03/18.
 */
public class DimBlockPos {

    public int dim;
    public BlockPos pos;

    public DimBlockPos(int dim, BlockPos pos) {
        this.dim = dim;
        this.pos = pos;
    }

    public DimBlockPos(TileEntity tile) {
        dim = tile.getWorld().provider.getDimension();
        pos = tile.getPos();
    }

    public DimBlockPos(MCDataInput packet) {
        dim = packet.readVarInt();
        pos = packet.readPos();
    }

    @Deprecated
    public DimBlockPos(CoordinatesBlock old) {
        dim = old.dim;
        pos = new BlockPos(old.x, old.y, old.z);
    }

    @Deprecated
    public CoordinatesBlock toOld() {
        return new CoordinatesBlock(dim, pos.getX(), pos.getY(), pos.getZ());
    }

    public void write(MCDataOutput packet) {
        packet.writeVarInt(dim);
        packet.writePos(pos);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof DimBlockPos) {
            DimBlockPos other = (DimBlockPos) obj;
            return other.dim == dim && other.pos.equals(pos);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(dim, pos);
    }
}
