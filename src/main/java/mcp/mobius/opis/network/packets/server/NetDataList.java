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

import java.util.ArrayList;
import java.util.List;

public class NetDataList extends PacketBase {

    public NetDataList() {
    }

    public NetDataList(Message msg, List<? extends ISerializable> data) {
        this.msg = msg;
        array = new ArrayList<>(data);
    }

    @Override
    public void encode(ByteBuf output) {
        output.writeInt(msg.ordinal());
        output.writeInt(array.size());

        if (array.size() > 0)
        //Packet.writeString(data.get(0).getClass().getCanonicalName(), ostream);
        {
            output.writeInt(DataType.getForClass(array.get(0).getClass()).ordinal());
        }

        for (ISerializable odata : array) {
            odata.writeToStream(output);
        }
    }

    @Override
    public void decode(ByteBuf input) {
        msg = Message.values()[input.readInt()];
        int ndata = input.readInt();
        clazzStr = "";
        if (ndata > 0) {
            clazz = DataType.getForOrdinal(input.readInt());
        }

        array = new ArrayList<>();

        for (int i = 0; i < ndata; i++) {
            array.add(dataRead(clazz, input));
        }
    }

    @Override
    @SideOnly (Side.CLIENT)
    public void actionClient(World world, EntityPlayer player) {
        MessageHandlerRegistrar.INSTANCE.routeMessage(msg, this);
    }

}
