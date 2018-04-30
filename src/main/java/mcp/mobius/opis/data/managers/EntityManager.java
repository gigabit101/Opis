package mcp.mobius.opis.data.managers;

import codechicken.lib.util.ServerUtils;
import mcp.mobius.opis.data.holders.basetypes.AmountHolder;
import mcp.mobius.opis.data.holders.basetypes.CoordinatesBlock;
import mcp.mobius.opis.data.holders.basetypes.CoordinatesChunk;
import mcp.mobius.opis.data.holders.newtypes.DataEntity;
import mcp.mobius.opis.data.holders.newtypes.DataEntityPerClass;
import mcp.mobius.opis.data.holders.newtypes.DataTiming;
import mcp.mobius.opis.helpers.TeleportUtils;
import mcp.mobius.opis.network.PacketManager;
import mcp.mobius.opis.profiler.Profilers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public enum EntityManager {
    INSTANCE;

    public List<DataEntity> getWorses(int amount) {
        List<DataEntity> sorted = new ArrayList<>();
        List<DataEntity> topEntities = new ArrayList<>();

        for (Entity entity : Profilers.ENTITY_UPDATE.get().data.keySet()) {
            sorted.add(new DataEntity().fill(entity));
        }
        sorted.sort(null);

        int i = 0;
        while (topEntities.size() < Math.min(amount, sorted.size()) && (i < sorted.size())) {
            DataEntity testats = sorted.get(i);

            if (testats.npoints < 40) {
                i++;
                continue;
            }
            if (DimensionManager.getWorld(testats.pos.dim) == null) {
                i++;
                continue;
            }

            topEntities.add(testats);
            i++;
        }
        return topEntities;
    }

    /* Returns all the entities in all dimensions (without timing data) */
    public List<DataEntity> getAllEntities() {
        List<DataEntity> entities = new ArrayList<>();
        for (int i : DimensionManager.getIDs()) {
            entities.addAll(getEntitiesInDim(i));
        }
        return entities;
    }

    /* Returns all the entities in the given dimension (without timing data) */
    public List<DataEntity> getEntitiesInDim(int dim) {
        List<DataEntity> entities = new ArrayList<>();

        World world = DimensionManager.getWorld(dim);
        if (world == null) {
            return entities;
        }

        ArrayList<Entity> copyList = new ArrayList<>(world.loadedEntityList);

        for (Entity ent : copyList) {
            entities.add(new DataEntity().fill(ent));
        }

        return entities;
    }

    /* Returns a hashmap of all entities per chunk (not timing) */
    public HashMap<CoordinatesChunk, ArrayList<DataEntity>> getAllEntitiesPerChunk() {
        HashMap<CoordinatesChunk, ArrayList<DataEntity>> entities = new HashMap<>();
        for (int i : DimensionManager.getIDs()) {
            entities.putAll(getEntitiesPerChunkInDim(i));
        }
        return entities;
    }

    /* Returns a hashmap of entities in the given dimension (not timing) */
    public HashMap<CoordinatesChunk, ArrayList<DataEntity>> getEntitiesPerChunkInDim(int dim) {
        HashMap<CoordinatesChunk, ArrayList<DataEntity>> entities = new HashMap<>();
        World world = DimensionManager.getWorld(dim);
        if (world == null) {
            return entities;
        }

        ArrayList<Entity> copyList = new ArrayList<>(world.loadedEntityList);

        for (Entity ent : copyList) {
            CoordinatesChunk chunk = new CoordinatesBlock(ent.dimension, (int) ent.posX, (int) ent.posY, (int) ent.posZ).asCoordinatesChunk();

            if (!entities.containsKey(chunk)) {
                entities.put(chunk, new ArrayList<>());
            }

            //entities.get(chunk).add(new EntityStats(ent.entityId, ent.getClass().getName(), ent.dimension, ent.posX, ent.posY, ent.posZ));
            entities.get(chunk).add(new DataEntity().fill(ent));
        }

        return entities;
    }

    /* Returns an array of all entities in a given chunk */
    public ArrayList<DataEntity> getEntitiesInChunk(CoordinatesChunk coord) {
        ArrayList<DataEntity> entities = new ArrayList<>();

        World world = DimensionManager.getWorld(coord.dim);
        if (world == null) {
            return entities;
        }

        ArrayList<Entity> copyList = new ArrayList<>(world.loadedEntityList);

        for (Entity ent : copyList) {
            CoordinatesChunk chunk = new CoordinatesBlock(ent.dimension, (int) ent.posX, (int) ent.posY, (int) ent.posZ).asCoordinatesChunk();
            if (chunk.equals(coord))
            //entities.add(new EntityStats(ent.entityId, ent.getClass().getName(), ent.dimension, ent.posX, ent.posY, ent.posZ));
            {
                entities.add(new DataEntity().fill(ent));
            }
        }

        return entities;
    }

    /* Returns a hashmap with the entity name and amount of it on the server */
    public ArrayList<AmountHolder> getCumulativeEntities(boolean filtered) {
        ArrayList<AmountHolder> cumData = new ArrayList<>();
        HashMap<String, Integer> entities = new HashMap<>();

        for (int dim : DimensionManager.getIDs()) {
            World world = DimensionManager.getWorld(dim);
            if (world == null) {
                continue;
            }

            ArrayList<Entity> copyList = new ArrayList<>(world.loadedEntityList);

            for (Entity ent : copyList) {
                //String name = ent.getClass().getName();
                String name = getEntityName(ent, filtered);

                if (!entities.containsKey(name)) {
                    entities.put(name, 0);
                }

                entities.put(name, entities.get(name) + 1);
            }
        }

        for (String key : entities.keySet()) {
            cumData.add(new AmountHolder(key, entities.get(key)));
        }

        return cumData;
    }

    public boolean teleportPlayer(CoordinatesBlock coord, EntityPlayerMP player) {
        //System.out.printf("%s %s\n", coord, getTeleportTarget(coord));
        TeleportUtils.teleportEntity(player, coord.dim, coord.x + 0.5, coord.y + 0.5, coord.z + 0.5);
        //		CoordinatesBlock target = Teleport.instance().getTeleportTarget(coord);
        //		if (target == null) return false;
        //
        //		target = Teleport.instance().fixNetherTP(target);
        //
        //		if (target == null) return false;
        //
        //		if (Teleport.instance().movePlayerToDimension(player, coord.dim))
        //			player.setPositionAndUpdate(target.x + 0.5, target.y, target.z + 0.5);
        //		else
        //			return false;

        return true;
    }

    public boolean teleportEntity(Entity src, Entity trg, EntityPlayerMP msgtrg) {
        if ((src == null) && (msgtrg != null)) {
            PacketManager.sendChatMsg(String.format("\u00A7oCannot find source entity %s", src), msgtrg);
            return false;
        }

        if ((trg == null) && (msgtrg != null)) {
            PacketManager.sendChatMsg(String.format("\u00A7oCannot find target entity %s", src), msgtrg);
            return false;
        }
        TeleportUtils.teleportEntity(src, trg.world.provider.getDimension(), trg.posX, trg.posY, trg.posZ, src.rotationYaw, src.rotationPitch);
        //		if (src instanceof EntityPlayerMP){
        //			if (Teleport.instance().movePlayerToDimension((EntityPlayerMP)src, trg.world.provider.getDimension()))
        //				src.setLocationAndAngles(trg.posX, trg.posY, trg.posZ, src.rotationYaw, src.rotationPitch);
        //			else
        //				return false;
        //		}
        //		else{
        //			if (Teleport.instance().moveEntityToDimension(src, trg.world.provider.getDimension()))
        //				src.setLocationAndAngles(trg.posX, trg.posY, trg.posZ, src.rotationYaw, src.rotationPitch);
        //			else
        //				return false;
        //		}

        return true;

    }

    public Entity getEntity(int eid, int dim) {
        World world = DimensionManager.getWorld(dim);
        if (world == null) {
            return null;
        }

        return world.getEntityByID(eid);
    }

    public String getEntityName(Entity ent) {
        return getEntityName(ent, false);
    }

    public String getEntityName(Entity ent, boolean filtered) {
        if (ent instanceof EntityItem && filtered) {
            return "Dropped Item";
        } else if (ent instanceof EntityItem && !filtered) {
            try {
                return "[Stack] " + ((EntityItem) ent).getItem().getDisplayName();
            } catch (Exception e) {
                return "<Unknown dropped item>";
            }
        }

        if (ent instanceof EntityPlayerMP && filtered) {
            return "Player";
        } else if (ent instanceof EntityPlayerMP && !filtered) {
            return "[ Player ] " + ((EntityPlayerMP) ent).getGameProfile().getName();
        }

        String name = ent.getName();

        if (name.contains(".")) {
            String[] namelst = ent.getClass().getName().split("\\.");
            return namelst[namelst.length - 1];
        }

        return name;
    }

    public DataTiming getTotalUpdateTime() {
        double updateTime = 0D;
        for (Entity entity : Profilers.ENTITY_UPDATE.get().data.keySet()) {
            updateTime += Profilers.ENTITY_UPDATE.get().data.get(entity).getGeometricMean();
        }
        return new DataTiming(updateTime);
    }

    public int killAll(String entName) {
        int nkilled = 0;

        if (entName.contains("Player")) {
            return -1; //Error msg for when trying to kill a player
        }

        for (int dim : DimensionManager.getIDs()) {
            World world = DimensionManager.getWorld(dim);
            if (world == null) {
                continue;
            }

            ArrayList<Entity> copyList = new ArrayList<>(world.loadedEntityList);

            for (Object o : copyList) {
                Entity ent = (Entity) o;
                String nameFiltered = getEntityName(ent, true).toLowerCase();
                String nameUnfiltered = getEntityName(ent, false).toLowerCase();

                if (nameFiltered.equals(entName.toLowerCase()) || nameUnfiltered.equals(entName.toLowerCase())) {
                    ent.setDead();
                    nkilled += 1;
                }
            }
        }

        System.out.printf("Killed %d %s\n", nkilled, entName);

        return nkilled;
    }

    public ArrayList<DataEntity> getAllPlayers() {
        //List players = MinecraftServer.getServerConfigurationManager(MinecraftServer.getServer()).playerEntityList;
        List<EntityPlayerMP> players = ServerUtils.mc().getPlayerList().getPlayers();

        ArrayList<DataEntity> outList = new ArrayList<>();

        for (EntityPlayerMP p : players) {
            outList.add(new DataEntity().fill(p));
        }

        return outList;
    }

    public int getAmountEntities() {
        int amountEntities = 0;
        for (WorldServer world : DimensionManager.getWorlds()) {
            amountEntities += world.loadedEntityList.size();
        }
        return amountEntities;
    }

    public int killAllPerClass(int dim, Class clazz) {
        WorldServer world = DimensionManager.getWorld(dim);
        if (world == null) {
            return -1;
        }

        int killedEnts = 0;

        ArrayList<Entity> copyList = new ArrayList<>(world.loadedEntityList);

        for (Entity entity : copyList) {
            if (clazz.isInstance(entity)) {
                entity.setDead();
                killedEnts += 1;
            }
        }
        return killedEnts;
    }

    public ArrayList<DataEntityPerClass> getTotalPerClass() {
        HashMap<String, DataEntityPerClass> data = new HashMap<>();

        for (Entity entity : Profilers.ENTITY_UPDATE.get().data.keySet()) {
            String name = getEntityName(entity, true);
            if (!data.containsKey(name)) {
                data.put(name, new DataEntityPerClass(name));
            }

            data.get(name).add(Profilers.ENTITY_UPDATE.get().data.get(entity).getGeometricMean());
        }
        return new ArrayList<>(data.values());
    }
}
