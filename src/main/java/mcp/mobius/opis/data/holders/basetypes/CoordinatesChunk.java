package mcp.mobius.opis.data.holders.basetypes;

import io.netty.buffer.ByteBuf;
import mcp.mobius.opis.data.holders.ISerializable;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.ChunkPos;

public final class CoordinatesChunk implements ISerializable {

    public final int dim, x, y, z;
    public final int chunkX, chunkZ;
    public final byte metadata;

    public final static CoordinatesChunk INVALID = new CoordinatesChunk(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);

    public CoordinatesChunk(CoordinatesBlock coord) {
        dim = coord.dim;
        chunkX = coord.chunkX;
        chunkZ = coord.chunkZ;

        x = chunkX << 4;
        y = 0;
        z = chunkZ << 4;

        metadata = 0;
    }

    public CoordinatesChunk(int dim, int chunkX, int chunkZ) {
        this.dim = dim;
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;

        x = chunkX << 4;
        y = 0;
        z = chunkZ << 4;

        metadata = 0;
    }

    public CoordinatesChunk(int dim, int chunkX, int chunkZ, byte metadata) {
        this.dim = dim;
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;

        x = chunkX << 4;
        y = 0;
        z = chunkZ << 4;

        this.metadata = metadata;
    }

    public CoordinatesChunk(int dim, ChunkPos coord) {
        this.dim = dim;
        chunkX = coord.x;
        chunkZ = coord.z;

        x = chunkX << 4;
        y = 0;
        z = chunkZ << 4;

        metadata = 0;
    }

    public CoordinatesChunk(int dim, ChunkPos coord, byte metadata) {
        this.dim = dim;
        chunkX = coord.x;
        chunkZ = coord.z;

        x = chunkX << 4;
        y = 0;
        z = chunkZ << 4;

        this.metadata = metadata;
    }

    public CoordinatesChunk(TileEntity te) {
        dim = te.getWorld().provider.getDimension();
        chunkX = te.getPos().getX() >> 4;
        chunkZ = te.getPos().getZ() >> 4;

        x = chunkX << 4;
        y = 0;
        z = chunkZ << 4;

        metadata = 0;
    }

    public boolean isInvalid() {
        return equals(CoordinatesChunk.INVALID);
    }

    public String toString() {
        return String.format("[%6d %6d %6d]", dim, chunkX, chunkZ);
    }

    public boolean equals(Object o) {
        CoordinatesChunk c = (CoordinatesChunk) o;
        return (dim == c.dim) && (chunkX == c.chunkX) && (chunkZ == c.chunkZ);
    }

    //00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000
    //11111111 11111111 11111111 11122222 22222222 22222222 22222200 00000000

    public int hashCode() {
        return dim + 31 * chunkX + 877 * chunkZ;
    }

    public ChunkPos toChunkCoordIntPair() {
        return new ChunkPos(chunkX, chunkZ);
    }

    public CoordinatesBlock asCoordinatesBlock() {
        return new CoordinatesBlock(this);
    }

    @Override
    public void writeToStream(ByteBuf stream) {
        stream.writeInt(dim);
        stream.writeInt(chunkX);
        stream.writeInt(chunkZ);
        stream.writeByte(metadata);
    }

    public static CoordinatesChunk readFromStream(ByteBuf stream) {
        return new CoordinatesChunk(stream.readInt(), stream.readInt(), stream.readInt(), stream.readByte());
    }
}
