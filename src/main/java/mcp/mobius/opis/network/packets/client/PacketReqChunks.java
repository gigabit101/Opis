package mcp.mobius.opis.network.packets.client;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import io.netty.buffer.ByteBuf;
import mcp.mobius.opis.data.holders.basetypes.CoordinatesChunk;
import mcp.mobius.opis.events.PlayerTracker;
import mcp.mobius.opis.network.PacketBase;
import mcp.mobius.opis.network.enums.AccessLevel;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import java.util.ArrayList;

public class PacketReqChunks extends PacketBase {

    public int dim;
    public ArrayList<CoordinatesChunk> chunks = new ArrayList<CoordinatesChunk>();

    public PacketReqChunks() {
    }

    public PacketReqChunks(int dim, ArrayList<CoordinatesChunk> chunks) {
        this.dim = dim;
        this.chunks = chunks;
    }

    @Override
    public void encode(ByteBuf output) {
        output.writeInt(dim);
        output.writeInt(chunks.size());
        for (CoordinatesChunk coord : chunks)
            coord.writeToStream(output);
    }

    @Override
    public void decode(ByteBuf input) {
        dim = input.readInt();
        int nchunks = input.readInt();
        for (int i = 0; i < nchunks; i++)
            chunks.add(CoordinatesChunk.readFromStream(input));
    }

    @Override
    public void actionServer(World world, EntityPlayerMP player) {
        if (PlayerTracker.INSTANCE.getPlayerAccessLevel(player).ordinal() > AccessLevel.PRIVILEGED.ordinal()) {
            ArrayList<Chunk> list = new ArrayList();

            if (world != null) {
                for (CoordinatesChunk chunk : chunks)
                    list.add(world.getChunkFromChunkCoords(chunk.chunkX, chunk.chunkZ));
            }
        }
    }
}
