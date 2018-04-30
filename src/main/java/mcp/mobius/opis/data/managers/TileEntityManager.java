package mcp.mobius.opis.data.managers;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import mcp.mobius.opis.Opis;
import mcp.mobius.opis.data.holders.basetypes.CoordinatesBlock;
import mcp.mobius.opis.data.holders.basetypes.CoordinatesChunk;
import mcp.mobius.opis.data.holders.newtypes.DataBlockTileEntity;
import mcp.mobius.opis.data.holders.newtypes.DataBlockTileEntityPerClass;
import mcp.mobius.opis.data.holders.newtypes.DataTileEntity;
import mcp.mobius.opis.data.holders.newtypes.DataTiming;
import mcp.mobius.opis.data.holders.stats.StatsChunk;
import mcp.mobius.opis.profiler.Profilers;
import mcp.mobius.opis.util.DimBlockPos;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;

import java.util.*;
import java.util.stream.Collectors;

public class TileEntityManager {

    public static final TileEntityManager INSTANCE = new TileEntityManager();

    public Map<CoordinatesChunk, StatsChunk> getTimes(int dim) {
        Map<CoordinatesChunk, StatsChunk> chunks = new HashMap<>();

        for (CoordinatesBlock coord : Profilers.TILE_UPDATE.get().data.keySet().stream().map(DimBlockPos::toOld).collect(Collectors.toList())) {
            if (coord.dim == dim) {

                CoordinatesChunk coordC = new CoordinatesChunk(coord);
                if (!chunks.containsKey(coordC)) {
                    chunks.put(coordC, new StatsChunk());
                }

                chunks.get(coordC).addEntity();
                chunks.get(coordC).addMeasure(Profilers.TILE_UPDATE.get().data.get(coord.toNew()).getGeometricMean());
            }
        }
        return chunks;
    }

    private void cleanUpStats() {
    }

    public List<DataBlockTileEntity> getTileEntitiesInChunk(CoordinatesChunk coord) {
        cleanUpStats();

        List<DataBlockTileEntity> returnList = new ArrayList<>();

        for (CoordinatesBlock tecoord : Profilers.TILE_UPDATE.get().data.keySet().stream().map(DimBlockPos::toOld).collect(Collectors.toList())) {
            if (coord.equals(tecoord.asCoordinatesChunk())) {
                DataBlockTileEntity testats = new DataBlockTileEntity().fill(tecoord);

                returnList.add(testats);
            }
        }

        return returnList;
    }

    public List<DataBlockTileEntity> getWorses(int amount) {
        List<DataBlockTileEntity> sorted = new ArrayList<>();
        List<DataBlockTileEntity> topEntities = new ArrayList<>();

        for (CoordinatesBlock coord : Profilers.TILE_UPDATE.get().data.keySet().stream().map(DimBlockPos::toOld).collect(Collectors.toList())) {
            sorted.add(new DataBlockTileEntity().fill(coord));
        }

        sorted.sort(null);

        for (int i = 0; i < Math.min(amount, sorted.size()); i++) {
            topEntities.add(sorted.get(i));
        }

        return topEntities;
    }

    public DataTiming getTotalUpdateTime() {
        double updateTime = 0D;
        for (CoordinatesBlock coords : Profilers.TILE_UPDATE.get().data.keySet().stream().map(DimBlockPos::toOld).collect(Collectors.toList())) {
            updateTime += Profilers.TILE_UPDATE.get().data.get(coords.toNew()).getGeometricMean();
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

    public List<DataTileEntity> getOrphans() {
        List<DataTileEntity> orphans = new ArrayList<>();
        Map<CoordinatesBlock, DataTileEntity> coordMap = new HashMap<>();
        Set<Integer> registeredEntities = new HashSet<>();

        for (WorldServer world : DimensionManager.getWorlds()) {
            for (Object o : world.loadedTileEntityList) {
                TileEntity tileEntity = (TileEntity) o;
                CoordinatesBlock coord = new CoordinatesBlock(world.provider.getDimension(), tileEntity.getPos().getX(), tileEntity.getPos().getY(), tileEntity.getPos().getZ());
                int hash = System.identityHashCode(tileEntity);

                if (registeredEntities.contains(hash)) {
                    continue;    //This entity has already been seen;
                }

                Block block = world.getBlockState(tileEntity.getPos()).getBlock();
                if (block == Blocks.AIR || block == null || !block.hasTileEntity() || world.getTileEntity(tileEntity.getPos()) == null || world.getTileEntity(tileEntity.getPos()).getClass() != tileEntity.getClass()) {

                    orphans.add(new DataTileEntity().fill(tileEntity, "Orphan"));
                    registeredEntities.add(hash);
                }

                if (coordMap.containsKey(coord)) {
                    if (!registeredEntities.contains(hash)) {
                        orphans.add(new DataTileEntity().fill(tileEntity, "Duplicate"));
                    }

                    if (!registeredEntities.contains(coordMap.get(coord).hashCode)) {
                        orphans.add(coordMap.get(coord));
                    }
                }

                if (!coordMap.containsKey(coord)) {
                    coordMap.put(coord, new DataTileEntity().fill(tileEntity, "Duplicate"));
                }
            }
        }

        Opis.log.warn(String.format("Found %d potential orphans !", orphans.size()));

        return orphans;
    }

    public List<DataBlockTileEntityPerClass> getCumulativeAmountTileEntities() {
        Table<Integer, Integer, DataBlockTileEntityPerClass> data = HashBasedTable.create();

        for (WorldServer world : DimensionManager.getWorlds()) {
            for (Object o : world.loadedTileEntityList) {
                TileEntity tileEntity = (TileEntity) o;
                //TODO
                IBlockState state = world.getBlockState(tileEntity.getPos());
                int id = Block.getIdFromBlock(state.getBlock());
                int meta = state.getBlock().getMetaFromState(state);

                if (!data.contains(id, meta)) {
                    data.put(id, meta, new DataBlockTileEntityPerClass(id, meta));
                }

                data.get(id, meta).add();
            }
        }

        return new ArrayList<>(data.values());
    }

    public List<DataBlockTileEntityPerClass> getCumulativeTimingTileEntities() {
        Table<Integer, Integer, DataBlockTileEntityPerClass> data = HashBasedTable.create();

        for (CoordinatesBlock coord : Profilers.TILE_UPDATE.get().data.keySet().stream().map(DimBlockPos::toOld).collect(Collectors.toList())) {
            World world = DimensionManager.getWorld(coord.dim);
            //TODO
            IBlockState state = world.getBlockState(new BlockPos(coord.x, coord.y, coord.z));
            int id = Block.getIdFromBlock(state.getBlock());
            int meta = state.getBlock().getMetaFromState(state);

            if (!data.contains(id, meta)) {
                data.put(id, meta, new DataBlockTileEntityPerClass(id, meta));
            }

            data.get(id, meta).add(Profilers.TILE_UPDATE.get().data.get(coord.toNew()).getGeometricMean());

        }

        return new ArrayList<>(data.values());
    }
}
