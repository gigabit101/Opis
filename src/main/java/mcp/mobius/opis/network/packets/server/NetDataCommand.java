package mcp.mobius.opis.network.packets.server;

import io.netty.buffer.ByteBuf;
import mcp.mobius.opis.api.MessageHandlerRegistrar;
import mcp.mobius.opis.network.PacketBase;
import mcp.mobius.opis.network.enums.Message;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class NetDataCommand extends PacketBase {

    public NetDataCommand() {
    }

    public NetDataCommand(Message msg) {
        this.msg = msg;
    }

    @Override
    public void encode(ByteBuf output) {
        output.writeInt(msg.ordinal());
    }

    @Override
    public void decode(ByteBuf input) {
        msg = Message.values()[input.readInt()];
    }

    @Override
    @SideOnly (Side.CLIENT)
    public void actionClient(World world, EntityPlayer player) {
        MessageHandlerRegistrar.INSTANCE.routeMessage(msg, this);
    }

}
