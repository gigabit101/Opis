package mcp.mobius.opis.data.holders.basetypes;

import com.google.common.collect.ImmutableSet;
import io.netty.buffer.ByteBuf;
import mcp.mobius.opis.data.holders.ISerializable;
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public final class TicketData implements ISerializable {

    public final CoordinatesChunk coord;
    public final int nchunks;
    public final String modID;
    public final Ticket ticket;

    public TicketData(Ticket ticket) {
        ImmutableSet requestedChunks = ticket.getChunkList();
        int maxChunkX = -999999, minChunkX = 9999999, maxChunkZ = -999999, minChunkZ = 9999999;

        for (Object obj : requestedChunks) {
            ChunkPos chunk = (ChunkPos) obj;
            maxChunkX = Math.max(maxChunkX, chunk.getXStart());
            minChunkX = Math.min(minChunkX, chunk.getXStart());
            maxChunkZ = Math.max(maxChunkZ, chunk.getZStart());
            minChunkZ = Math.min(minChunkZ, chunk.getZStart());
        }

        coord = new CoordinatesChunk(ticket.world.provider.getDimension(), (minChunkX + maxChunkX) / 2, (minChunkZ + maxChunkZ) / 2);
        nchunks = requestedChunks.size();
        modID = ticket.getModId();
        this.ticket = ticket;
    }

    public TicketData(CoordinatesChunk coord, int nchunks, String modid) {
        this.coord = coord;
        this.nchunks = nchunks;
        modID = modid;
        ticket = null;
    }

    @Override
    public void writeToStream(ByteBuf stream) {
        coord.writeToStream(stream);
        stream.writeInt(nchunks);
        ByteBufUtils.writeUTF8String(stream, modID);
    }

    public static TicketData readFromStream(ByteBuf stream) {
        return new TicketData(CoordinatesChunk.readFromStream(stream), stream.readInt(), ByteBufUtils.readUTF8String(stream));
    }

    public boolean equals(Object o) {
        TicketData c = (TicketData) o;

        if (ticket != null && c.ticket != null) {
            return ticket.equals(c.ticket);
        }

        return (coord.dim == c.coord.dim) && (coord.chunkX == c.coord.chunkX) && (coord.chunkZ == c.coord.chunkZ) && (nchunks == c.nchunks);
    }

    public int hashCode() {
        if (ticket != null) {
            return ticket.hashCode();
        }
        return String.format("%s %s %s %s", coord.dim, coord.chunkX, coord.chunkZ, nchunks).hashCode();
    }

    public String toString() {
        return String.format("Ticket %s [%d %d %d] %d", modID, coord.dim, coord.chunkX, coord.chunkZ, nchunks);
    }

}
