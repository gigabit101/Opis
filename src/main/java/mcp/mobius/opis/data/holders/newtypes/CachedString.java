package mcp.mobius.opis.data.holders.newtypes;

import io.netty.buffer.ByteBuf;
import mcp.mobius.opis.data.holders.ISerializable;
import mcp.mobius.opis.data.managers.StringCache;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class CachedString implements Comparable, ISerializable {

    public String str;
    public int index;
    private boolean valid = false;

    public CachedString(int index) {
        this.index = index;
        str = StringCache.INSTANCE.getString(index);
        valid = !str.equals("<ERROR>");
    }

    public CachedString(String str) {

        //if(FMLCommonHandler.instance().getSide() == Side.SERVER){
        if (FMLCommonHandler.instance().getEffectiveSide().isServer()) {
            index = StringCache.INSTANCE.getIndex(str);
            this.str = str;
        } else {
            index = -1;
            this.str = str;
        }
    }

    @Override
    public int compareTo(Object o) {
        return ((CachedString) o).str.compareTo(str);    // Reverse order ! Put higher values FIRST
    }

    public String toString() {
        //return String.format("[%d] %s",this.index, this.str);
        if (!valid) {
            str = StringCache.INSTANCE.getString(index);
            valid = !str.equals("<ERROR>");
        }

        return str;

    }

    public void writeToStream(ByteBuf stream) {
        stream.writeInt(index);
    }

    public static CachedString readFromStream(ByteBuf stream) {
        return new CachedString(stream.readInt());
    }

}
