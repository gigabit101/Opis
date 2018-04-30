package mcp.mobius.opis.data.holders.basetypes;

import io.netty.buffer.ByteBuf;
import mcp.mobius.opis.data.holders.ISerializable;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class SerialString implements ISerializable {

    public String value = "";

    public SerialString(String value) {
        this.value = value;
    }

    @Override
    public void writeToStream(ByteBuf stream) {
        ByteBufUtils.writeUTF8String(stream, value);
    }

    public static SerialString readFromStream(ByteBuf stream) {
        return new SerialString(ByteBufUtils.readUTF8String(stream));
    }
}
