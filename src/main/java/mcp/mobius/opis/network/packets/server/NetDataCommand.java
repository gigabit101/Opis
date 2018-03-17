package mcp.mobius.opis.network.packets.server;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import io.netty.buffer.ByteBuf;
import mcp.mobius.opis.api.MessageHandlerRegistrar;
import mcp.mobius.opis.network.PacketBase;
import mcp.mobius.opis.network.enums.Message;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class NetDataCommand extends PacketBase {

    public NetDataCommand(){}

    public NetDataCommand(Message msg) {
        this.msg = msg;
    }

    @Override
    public void encode(ByteBuf output) {
        output.writeInt(this.msg.ordinal());
    }

    @Override
    public void decode(ByteBuf input) {
        this.msg = Message.values()[input.readInt()];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void actionClient(World world, EntityPlayer player) {
        MessageHandlerRegistrar.INSTANCE.routeMessage(this.msg, this);
    }

}
