package mcp.mobius.opis.data.holders.newtypes;

import io.netty.buffer.ByteBuf;
import mcp.mobius.opis.data.holders.ISerializable;
import mcp.mobius.opis.profiler.Profilers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.util.Map;


/* Data holder for infos about dimensions */

public class DataDimension implements ISerializable {

    public int dim;
    public CachedString name;
    public int players;
    public int forced;
    public int loaded;
    public int entities;
    public int mobs;
    public int neutral;
    public int itemstacks;
    public DataTiming update;

    public DataDimension fill(int dim) {
        WorldServer world = DimensionManager.getWorld(dim);

        this.dim = dim;
        name = new CachedString(world.provider.getDimensionType().getName());
        players = world.playerEntities.size();
        forced = world.getPersistentChunks().size();
        loaded = world.getChunkProvider().getLoadedChunkCount();

        Map<Integer, DescriptiveStatistics> data = Profilers.DIMENSION_TICK.get().data;
        update = new DataTiming(data.containsKey(dim) ? data.get(dim).getGeometricMean() : 0.0D);

        mobs = 0;
        neutral = 0;
        entities = world.loadedEntityList.size();

        for (Entity entity : world.loadedEntityList) {
            if (entity instanceof EntityMob) {
                mobs += 1;
            }
            if (entity instanceof EntityAnimal) {
                neutral += 1;
            }
            if (entity instanceof EntityItem) {
                itemstacks += 1;
            }
        }

        return this;
    }

    @Override
    public void writeToStream(ByteBuf stream) {
        stream.writeInt(dim);
        stream.writeInt(players);
        stream.writeInt(forced);
        stream.writeInt(loaded);
        stream.writeInt(entities);
        stream.writeInt(mobs);
        stream.writeInt(neutral);
        stream.writeInt(itemstacks);
        update.writeToStream(stream);
        name.writeToStream(stream);
    }

    public static DataDimension readFromStream(ByteBuf stream) {
        DataDimension retVal = new DataDimension();
        retVal.dim = stream.readInt();
        retVal.players = stream.readInt();
        retVal.forced = stream.readInt();
        retVal.loaded = stream.readInt();
        retVal.entities = stream.readInt();
        retVal.mobs = stream.readInt();
        retVal.neutral = stream.readInt();
        retVal.itemstacks = stream.readInt();
        //retVal.update  = stream.readDouble();
        retVal.update = DataTiming.readFromStream(stream);
        retVal.name = CachedString.readFromStream(stream);
        return retVal;
    }
}
