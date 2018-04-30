package mcp.mobius.opis.network.packets.server;

import io.netty.buffer.ByteBuf;
import mcp.mobius.opis.api.MessageHandlerRegistrar;
import mcp.mobius.opis.data.holders.DataType;
import mcp.mobius.opis.data.holders.ISerializable;
import mcp.mobius.opis.network.PacketBase;
import mcp.mobius.opis.network.enums.Message;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class NetDataValue extends PacketBase {

    public NetDataValue() {
    }

    public NetDataValue(Message msg, ISerializable data) {
        this.msg = msg;
        value = data;
    }

    @Override
    public void encode(ByteBuf output) {
        output.writeInt(msg.ordinal());
        output.writeInt(DataType.getForClass(value.getClass()).ordinal());
        value.writeToStream(output);
    }

    @Override
    public void decode(ByteBuf input) {
        msg = Message.values()[input.readInt()];
        clazz = DataType.getForOrdinal(input.readInt());
        value = dataRead(clazz, input);

    }

    @Override
    @SideOnly (Side.CLIENT)
    public void actionClient(World world, EntityPlayer player) {
        MessageHandlerRegistrar.INSTANCE.routeMessage(msg, this);
    }

}
