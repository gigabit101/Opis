package mcp.mobius.opis.data.managers;

import com.google.common.collect.HashBasedTable;
import mcp.mobius.opis.Opis;
import mcp.mobius.opis.data.holders.basetypes.CoordinatesBlock;
import mcp.mobius.opis.data.holders.basetypes.CoordinatesChunk;
import mcp.mobius.opis.data.holders.newtypes.DataBlockTileEntity;
import mcp.mobius.opis.data.holders.newtypes.DataBlockTileEntityPerClass;
import mcp.mobius.opis.data.holders.newtypes.DataTileEntity;
import mcp.mobius.opis.data.holders.newtypes.DataTiming;
import mcp.mobius.opis.data.holders.stats.StatsChunk;
import mcp.mobius.opis.data.profilers.ProfilerTileEntityUpdate;
import mcp.mobius.opis.profiler.ProfilerSection;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

public enum TileEntityManager {
    INSTANCE;

    public HashMap<CoordinatesChunk, StatsChunk> getTimes(int dim) {
        HashMap<CoordinatesChunk, StatsChunk> chunks = new HashMap<CoordinatesChunk, StatsChunk>();

        for (CoordinatesBlock coord : ((ProfilerTileEntityUpdate) ProfilerSection.TILEENT_UPDATETIME.getProfiler()).data.keySet()) {
            if (coord.dim == dim) {

                CoordinatesChunk coordC = new CoordinatesChunk(coord);
                if (!(chunks.containsKey(coordC)))
                    chunks.put(coordC, new StatsChunk());

                chunks.get(coordC).addEntity();
                chunks.get(coordC).addMeasure(((ProfilerTileEntityUpdate) ProfilerSection.TILEENT_UPDATETIME.getProfiler()).data.get(coord).getGeometricMean());
            }
        }
        return chunks;
    }

    private void cleanUpStats() {
    }

    public ArrayList<DataBlockTileEntity> getTileEntitiesInChunk(CoordinatesChunk coord) {
        cleanUpStats();

        ArrayList<DataBlockTileEntity> returnList = new ArrayList<DataBlockTileEntity>();

        for (CoordinatesBlock tecoord : ((ProfilerTileEntityUpdate) ProfilerSection.TILEENT_UPDATETIME.getProfiler()).data.keySet()) {
            if (coord.equals(tecoord.asCoordinatesChunk())) {
                DataBlockTileEntity testats = new DataBlockTileEntity().fill(tecoord);

                returnList.add(testats);
            }
        }

        return returnList;
    }

    public ArrayList<DataBlockTileEntity> getWorses(int amount) {
        ArrayList<DataBlockTileEntity> sorted = new ArrayList<DataBlockTileEntity>();
        ArrayList<DataBlockTileEntity> topEntities = new ArrayList<DataBlockTileEntity>();

        for (CoordinatesBlock coord : ((ProfilerTileEntityUpdate) ProfilerSection.TILEENT_UPDATETIME.getProfiler()).data.keySet())
            sorted.add(new DataBlockTileEntity().fill(coord));

        Collections.sort(sorted);

        for (int i = 0; i < Math.min(amount, sorted.size()); i++)
            topEntities.add(sorted.get(i));


        return topEntities;
    }

    public DataTiming getTotalUpdateTime() {
        double updateTime = 0D;
        for (CoordinatesBlock coords : ((ProfilerTileEntityUpdate) ProfilerSection.TILEENT_UPDATETIME.getProfiler()).data.keySet()) {
            updateTime += ((ProfilerTileEntityUpdate) ProfilerSection.TILEENT_UPDATETIME.getProfiler()).data.get(coords).getGeometricMean();
        }
        return new DataTiming(updateTime);
    }

    public int getAmountTileEntities() {
        int amountTileEntities = 0;
        for (WorldServer world : DimensionManager.getWorlds()) {
            amountTileEntities += world.loadedTileEntityList.size();
        }
        return amountTileEntities;
    }

    public ArrayList<DataTileEntity> getOrphans() {
        ArrayList<DataTileEntity> orphans = new ArrayList<DataTileEntity>();
        HashMap<CoordinatesBlock, DataTileEntity> coordHashset = new HashMap<CoordinatesBlock, DataTileEntity>();
        HashSet<Integer> registeredEntities = new HashSet<Integer>();

        for (WorldServer world : DimensionManager.getWorlds()) {
            for (Object o : world.loadedTileEntityList) {
                TileEntity tileEntity = (TileEntity) o;
                CoordinatesBlock coord = new CoordinatesBlock(world.provider.getDimension(), tileEntity.getPos().getX(), tileEntity.getPos().getY(), tileEntity.getPos().getZ());
                int hash = System.identityHashCode(tileEntity);

                if (registeredEntities.contains(hash)) continue;    //This entitie has already been seen;

                Block block = world.getBlockState(tileEntity.getPos()).getBlock();
                if (block == Blocks.AIR || block == null
                        || !block.hasTileEntity()
                        || world.getTileEntity(tileEntity.getPos()) == null
                        || world.getTileEntity(tileEntity.getPos()).getClass() != tileEntity.getClass()) {

                    orphans.add(new DataTileEntity().fill(tileEntity, "Orphan"));
                    registeredEntities.add(hash);
                }

                if (coordHashset.containsKey(coord)) {
                    if (!registeredEntities.contains(hash))
                        orphans.add(new DataTileEntity().fill(tileEntity, "Duplicate"));

                    if (!registeredEntities.contains(coordHashset.get(coord).hashCode))
                        orphans.add(coordHashset.get(coord));
                }

                if (!coordHashset.containsKey(coord)) {
                    coordHashset.put(coord, new DataTileEntity().fill(tileEntity, "Duplicate"));
                }
            }
        }

        Opis.log.warn(String.format("Found %d potential orphans !", orphans.size()));

        return orphans;
    }

    public ArrayList<DataBlockTileEntityPerClass> getCumulativeAmountTileEntities() {
        HashBasedTable<Integer, Integer, DataBlockTileEntityPerClass> data = HashBasedTable.create();

        for (WorldServer world : DimensionManager.getWorlds()) {
            for (Object o : world.loadedTileEntityList) {
                TileEntity tileEntity = (TileEntity) o;
                //TODO
                IBlockState state = world.getBlockState(tileEntity.getPos());
                int id = Block.getIdFromBlock(state.getBlock());
                int meta = state.getBlock().getMetaFromState(state);

                if (!data.contains(id, meta))
                    data.put(id, meta, new DataBlockTileEntityPerClass(id, meta));

                data.get(id, meta).add();
            }
        }

        return new ArrayList<DataBlockTileEntityPerClass>(data.values());
    }

    public ArrayList<DataBlockTileEntityPerClass> getCumulativeTimingTileEntities() {
        HashBasedTable<Integer, Integer, DataBlockTileEntityPerClass> data = HashBasedTable.create();

        for (CoordinatesBlock coord : ((ProfilerTileEntityUpdate) ProfilerSection.TILEENT_UPDATETIME.getProfiler()).data.keySet()) {
            World world = DimensionManager.getWorld(coord.dim);
            //TODO
            IBlockState state = world.getBlockState(new BlockPos(coord.x, coord.y, coord.z));
            int id = Block.getIdFromBlock(state.getBlock());
            int meta = state.getBlock().getMetaFromState(state);

            if (!data.contains(id, meta))
                data.put(id, meta, new DataBlockTileEntityPerClass(id, meta));

            data.get(id, meta).add(((ProfilerTileEntityUpdate) ProfilerSection.TILEENT_UPDATETIME.getProfiler()).data.get(coord).getGeometricMean());

        }

        return new ArrayList<DataBlockTileEntityPerClass>(data.values());
    }
}
