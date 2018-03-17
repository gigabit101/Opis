package mcp.mobius.opis.data.holders.newtypes;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import io.netty.buffer.ByteBuf;
import mcp.mobius.opis.data.holders.ISerializable;
import mcp.mobius.opis.data.holders.basetypes.CoordinatesBlock;
import net.minecraft.tileentity.TileEntity;

public class DataTileEntity implements ISerializable {

    public CachedString clazz;
    public CoordinatesBlock pos;
    public int hashCode;
    public boolean isValid;
    public CachedString cause;

    public DataTileEntity fill(TileEntity tileEntity, String cause) {
        this.pos = new CoordinatesBlock(tileEntity.getWorld().provider.getDimension(), tileEntity.getPos().getX(), tileEntity.getPos().getY(), tileEntity.getPos().getZ());
        this.clazz = new CachedString(tileEntity.getClass().getCanonicalName());
        this.hashCode = System.identityHashCode(tileEntity);
        this.isValid = !tileEntity.isInvalid();
        this.cause = new CachedString(cause);
        return this;
    }

	/*
	public DataTileEntity fill(CoordinatesBlock coord, String clazz, int hashCode, boolean isInvalid){
		this.pos       = coord;
		this.clazz     = new CachedString(clazz);
		this.hashCode  = hashCode;
		this.isValid = !isInvalid;
		return this;
	}
	*/

    @Override
    public void writeToStream(ByteBuf stream) {
        this.clazz.writeToStream(stream);
        this.pos.writeToStream(stream);
        this.cause.writeToStream(stream);
        stream.writeInt(this.hashCode);
        stream.writeBoolean(this.isValid);

    }

    public static DataTileEntity readFromStream(ByteBuf stream) {
        DataTileEntity retVal = new DataTileEntity();
        retVal.clazz = CachedString.readFromStream(stream);
        retVal.pos = CoordinatesBlock.readFromStream(stream);
        retVal.cause = CachedString.readFromStream(stream);
        retVal.hashCode = stream.readInt();
        retVal.isValid = stream.readBoolean();
        return retVal;
    }

}
