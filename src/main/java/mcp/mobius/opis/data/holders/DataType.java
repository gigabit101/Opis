package mcp.mobius.opis.data.holders;

import com.google.common.collect.HashBiMap;
import mcp.mobius.opis.Opis;
import mcp.mobius.opis.data.holders.basetypes.*;
import mcp.mobius.opis.data.holders.newtypes.*;
import mcp.mobius.opis.data.holders.stats.StatAbstract;
import mcp.mobius.opis.data.holders.stats.StatsChunk;

public enum DataType {
    AMOUNTHOLDER(AmountHolder.class),
    COORDINATESBLOCK(CoordinatesBlock.class),
    COORDINATESCHUNK(CoordinatesChunk.class),
    SERIALDOUBLE(SerialDouble.class),
    SERIALFLOAT(SerialFloat.class),
    SERIALINT(SerialInt.class),
    SERIALLONG(SerialLong.class),
    SERIALSTRING(SerialString.class),
    TARGETENTITY(TargetEntity.class),
    TICKETDATA(TicketData.class),

    CACHEDSTRING(CachedString.class),
    DATAAMOUNTRATE(DataAmountRate.class),
    DATABITRATE(DataByteRate.class),
    DATABITSIZE(DataByteSize.class),
    DATABLOCKRENDER(DataBlockRender.class),
    DATABLOCKTICK(DataBlockTick.class),
    DATABLOCKTILEENTITY(DataBlockTileEntity.class),
    DATABLOCKTILEENTITYPERCLASS(DataBlockTileEntityPerClass.class),
    DATACHUNK(DataChunk.class),
    DATACHUNKENTITIES(DataChunkEntities.class),
    DATADIMENSION(DataDimension.class),
    DATAENTITY(DataEntity.class),
    DATAENTITYPERCLASS(DataEntityPerClass.class),
    DATAENTITYRENDER(DataEntityRender.class),
    DATAEVENT(DataEvent.class),
    DATANETWORKTICK(DataNetworkTick.class),
    DATAPACKET(DataPacket.class),
    DATAPACKET250(DataPacket250.class),
    DATASTRINGUPD(DataStringUpdate.class),
    DATATHREAD(DataThread.class),
    DATATILEENTITY(DataTileEntity.class),
    DATATILEENTITYRENDER(DataTileEntityRender.class),
    DATATIMING(DataTiming.class),
    DATATIMINGMILLISECOND(DataTimingMillisecond.class),
    STATABSTRACT(StatAbstract.class),
    STATSCHUNK(StatsChunk.class);

    private Class clazz;
    private static HashBiMap<DataType, Class> bimap = HashBiMap.create(50);

    static {
        for (DataType type : DataType.values())
            bimap.put(type, type.clazz);
    }

    private DataType(Class clazz) {
        this.clazz = clazz;
    }

    public static DataType getForClass(Class clazz) {
        DataType type = bimap.inverse().get(clazz);
        if (type == null) {
            Opis.log.warn(String.format("Class %s was not registered with the DataType enum", clazz));
        }
        return type;
    }

    public static Class getForOrdinal(int ordinal) {
        Class retVal;
        try {
            DataType type = DataType.values()[ordinal];
            retVal = bimap.get(type);
            if (retVal == null) {
                Opis.log.warn(String.format("DataType doesn't have a class registered for %s", type));
            }
        } catch (Exception e) {
            retVal = null;
            Opis.log.warn(String.format("Index out of bound for ordinal %d in DataType", ordinal));
        }
        return retVal;
    }
}
