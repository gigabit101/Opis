package mcp.mobius.opis.data.managers;

import mcp.mobius.opis.api.IMessageHandler;
import mcp.mobius.opis.data.holders.ISerializable;
import mcp.mobius.opis.data.holders.basetypes.CoordinatesBlock;
import mcp.mobius.opis.data.holders.basetypes.CoordinatesChunk;
import mcp.mobius.opis.data.holders.basetypes.TicketData;
import mcp.mobius.opis.data.holders.newtypes.DataBlockTileEntity;
import mcp.mobius.opis.data.holders.newtypes.DataEntity;
import mcp.mobius.opis.data.holders.stats.StatsChunk;
import mcp.mobius.opis.network.PacketBase;
import mcp.mobius.opis.network.enums.Message;
import mcp.mobius.opis.profiler.Profilers;
import mcp.mobius.opis.util.DimBlockPos;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.stream.Collectors;

public enum ChunkManager implements IMessageHandler {
    INSTANCE;

    private ArrayList<CoordinatesChunk> chunksLoad = new ArrayList<>();
    private HashMap<CoordinatesChunk, StatsChunk> chunkMeanTime = new HashMap<>();
    public ArrayList<TicketData> tickets = new ArrayList<>();

    public void addLoadedChunks(ArrayList<ISerializable> data) {
        for (ISerializable chunk : data) {
            chunksLoad.add((CoordinatesChunk) chunk);
        }
    }

    public ArrayList<CoordinatesChunk> getLoadedChunks() {
        return chunksLoad;
    }

    public void setChunkMeanTime(ArrayList<ISerializable> data) {
        chunkMeanTime.clear();
        for (ISerializable stat : data) {
            chunkMeanTime.put(((StatsChunk) stat).getChunk(), (StatsChunk) stat);
        }
    }

    public HashMap<CoordinatesChunk, StatsChunk> getChunkMeanTime() {
        return chunkMeanTime;
    }

    public ArrayList<CoordinatesChunk> getLoadedChunks(int dimension) {
        HashSet<CoordinatesChunk> chunkStatus = new HashSet<>();
        WorldServer world = DimensionManager.getWorld(dimension);
        if (world != null) {
            for (ChunkPos coord : world.getPersistentChunks().keySet()) {
                chunkStatus.add(new CoordinatesChunk(dimension, coord, (byte) 1));
            }

            for (Object o : world.getChunkProvider().getLoadedChunks()) {
                Chunk chunk = (Chunk) o;

                chunkStatus.add(new CoordinatesChunk(dimension, chunk.getPos(), (byte) 0));
            }
        }

        return new ArrayList<>(chunkStatus);
    }

    public HashSet<TicketData> getTickets() {
        HashSet<TicketData> tickets = new HashSet<>();
        for (int dim : DimensionManager.getIDs()) {
            for (Ticket ticket : DimensionManager.getWorld(dim).getPersistentChunks().values()) {
                tickets.add(new TicketData(ticket));
            }
        }

        return tickets;
    }

    public ArrayList<StatsChunk> getChunksUpdateTime() {
        HashMap<CoordinatesChunk, StatsChunk> chunks = new HashMap<>();

        for (CoordinatesBlock coords : Profilers.TILE_UPDATE.get().data.keySet().stream().map(DimBlockPos::toOld).collect(Collectors.toList())) {
            DataBlockTileEntity data = new DataBlockTileEntity().fill(coords);
            CoordinatesChunk chunk = data.pos.asCoordinatesChunk();

            if (!chunks.containsKey(chunk)) {
                chunks.put(chunk, new StatsChunk(chunk));
            }

            chunks.get(chunk).addTileEntity();
            chunks.get(chunk).addMeasure(data.update.timing);

        }

        for (Entity entity : Profilers.ENTITY_UPDATE.get().data.keySet()) {
            DataEntity data = new DataEntity().fill(entity);
            CoordinatesChunk chunk = data.pos.asCoordinatesChunk();

            if (!chunks.containsKey(chunk)) {
                chunks.put(chunk, new StatsChunk(chunk));
            }

            chunks.get(chunk).addEntity();
            chunks.get(chunk).addMeasure(data.update.timing);
        }

        ArrayList<StatsChunk> chunksUpdate = new ArrayList<>(chunks.values());
        return chunksUpdate;
    }

    public ArrayList<StatsChunk> getTopChunks(int quantity) {
        ArrayList<StatsChunk> chunks = getChunksUpdateTime();
        ArrayList<StatsChunk> outList = new ArrayList<>();
        Collections.sort(chunks);

        for (int i = 0; i < Math.min(quantity, chunks.size()); i++) {
            outList.add(chunks.get(i));
        }

        return outList;
    }

    public int getLoadedChunkAmount() {
        int loadedChunks = 0;
        for (WorldServer world : DimensionManager.getWorlds()) {
            int loadedChunksForDim = world.getChunkProvider().getLoadedChunkCount();
            loadedChunks += loadedChunksForDim;
            //System.out.printf("[ %2d ]  %d chunks\n", world.provider.dimensionId, loadedChunksForDim);
        }
        //System.out.printf("Total : %d chunks\n", loadedChunks);
        return loadedChunks;
    }

    public int getForcedChunkAmount() {
        int forcedChunks = 0;
        for (WorldServer world : DimensionManager.getWorlds()) {
            forcedChunks += world.getPersistentChunks().size();
        }
        return forcedChunks;
    }

    public void purgeChunks(int dim) {
        WorldServer world = DimensionManager.getWorld(dim);
        if (world == null) {
            return;
        }

        int loadedChunksDelta = 100;

        world.getChunkProvider().queueUnloadAll();

        while (loadedChunksDelta >= 100) {
            int loadedBefore = world.getChunkProvider().getLoadedChunkCount();
            world.getChunkProvider().queueUnloadAll();
            loadedChunksDelta = loadedBefore - world.getChunkProvider().getLoadedChunkCount();
        }
    }

    @Override
    public boolean handleMessage(Message msg, PacketBase rawdata) {
        switch (msg) {
            case LIST_TIMING_CHUNK: {
                setChunkMeanTime(rawdata.array);
                break;
            }
            case LIST_CHUNK_LOADED: {
                addLoadedChunks(rawdata.array);
                break;
            }
            case LIST_CHUNK_LOADED_CLEAR: {
                chunksLoad.clear();
                break;
            }
            default:
                return false;
        }

        return true;
    }
}
