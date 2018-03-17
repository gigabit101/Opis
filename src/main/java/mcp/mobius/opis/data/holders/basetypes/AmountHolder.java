package mcp.mobius.opis.data.holders.basetypes;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import io.netty.buffer.ByteBuf;
import mcp.mobius.opis.data.holders.ISerializable;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class AmountHolder implements ISerializable {

    public String key = null;
    public Integer value = 0;

    public AmountHolder(String key, Integer value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public void writeToStream(ByteBuf stream) {
        ByteBufUtils.writeUTF8String(stream, key);
        stream.writeInt(this.value);
    }

    public static AmountHolder readFromStream(ByteBuf istream) {
        String key = ByteBufUtils.readUTF8String(istream);
        Integer value = istream.readInt();

        return new AmountHolder(key, value);
    }
}
