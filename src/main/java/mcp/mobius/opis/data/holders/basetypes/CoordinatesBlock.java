package mcp.mobius.opis.data.holders.basetypes;

import io.netty.buffer.ByteBuf;
import mcp.mobius.opis.data.holders.ISerializable;
import mcp.mobius.opis.util.DimBlockPos;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.MathHelper;

@Deprecated//TODO Covers: Why.
public final class CoordinatesBlock implements ISerializable {

    public final int dim, x, y, z;
    public final int chunkX, chunkZ;
    //public boolean isChunk;

    public final static CoordinatesBlock INVALID = new CoordinatesBlock(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);

    public CoordinatesBlock(Entity entity) {
        dim = entity.world.provider.getDimension();
        x = MathHelper.floor(entity.posX);
        y = MathHelper.floor(entity.posY);
        z = MathHelper.floor(entity.posZ);
        chunkX = x >> 4;
        chunkZ = z >> 4;
        //this.isChunk = false;
    }

    public CoordinatesBlock(int dim, int x, int y, int z) {
        this.dim = dim;
        this.x = x;
        this.y = y;
        this.z = z;
        chunkX = x >> 4;
        chunkZ = z >> 4;
        //this.isChunk = false;
    }

    public CoordinatesBlock(int dim, double x, double y, double z) {
        this.dim = dim;
        this.x = MathHelper.floor(x);
        this.y = MathHelper.floor(y);
        this.z = MathHelper.floor(z);
        chunkX = MathHelper.floor(x) >> 4;
        chunkZ = MathHelper.floor(z) >> 4;
        //this.isChunk = false;
    }

    public CoordinatesBlock(CoordinatesChunk coord) {
        dim = coord.dim;
        chunkX = coord.chunkX;
        chunkZ = coord.chunkZ;

        x = coord.x;
        y = coord.y;
        z = coord.z;
    }

	/*
    public CoordinatesBlock(int dim, int chunkX, int chunkZ){
		this.dim = dim;
		this.chunkX = chunkX;
		this.chunkZ = chunkZ;
		this.x = chunkX << 4;
		this.y = 0;
		this.z = chunkZ << 4;
		this.isChunk = true;
	}
	*/

    public CoordinatesBlock(TileEntity te) {
        dim = te.getWorld().provider.getDimension();
        x = te.getPos().getX();
        y = te.getPos().getY();
        z = te.getPos().getZ();
        chunkX = x >> 4;
        chunkZ = z >> 4;
        //this.isChunk = false;
    }

    public CoordinatesChunk asCoordinatesChunk() {
        return new CoordinatesChunk(this);
    }

    public String toString() {
        //if (this.isChunk)
        //	return String.format("[%6d %6d %6d]", this.dim, this.chunkX, this.chunkZ);
        //else
        return String.format("[%6d %6d %6d %6d]", dim, x, y, z);
    }

    public boolean equals(Object o) {
        CoordinatesBlock c = (CoordinatesBlock) o;
        //if (this.isChunk)
        //	return (this.dim == c.dim) && (this.chunkX == c.chunkX) && (this.chunkZ == c.chunkZ);
        //else
        return (dim == c.dim) && (x == c.x) && (y == c.y) && (z == c.z);
    }

    public boolean isInvalid() {
        return equals(CoordinatesBlock.INVALID);
    }

    public int hashCode() {
        //if (this.isChunk)
        //	return String.format("%s %s %s", this.dim, this.chunkX, this.chunkZ).hashCode();
        //else
        //	return String.format("%s %s %s %s", this.dim, this.x, this.y, this.z).hashCode();
        return dim + 31 * x + 877 * y + 3187 * z;
    }

    public DimBlockPos toNew() {
        return new DimBlockPos(this);
    }

    @Override
    public void writeToStream(ByteBuf stream) {
        stream.writeInt(dim);
        stream.writeInt(x);
        stream.writeInt(y);
        stream.writeInt(z);
    }

    public static CoordinatesBlock readFromStream(ByteBuf stream) {
        return new CoordinatesBlock(stream.readInt(), stream.readInt(), stream.readInt(), stream.readInt());
    }

}
