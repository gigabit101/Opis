package mcp.mobius.opis.network.packets.client;

import io.netty.buffer.ByteBuf;
import mcp.mobius.opis.data.holders.DataType;
import mcp.mobius.opis.data.holders.ISerializable;
import mcp.mobius.opis.network.PacketBase;
import mcp.mobius.opis.network.ServerMessageHandler;
import mcp.mobius.opis.network.enums.Message;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;

public class PacketReqData extends PacketBase {

    public Message dataReq;
    public ISerializable param1 = null;
    public ISerializable param2 = null;

    public PacketReqData() {
    }

    public PacketReqData(Message dataReq) {
        this(dataReq, null, null);
    }

    public PacketReqData(Message dataReq, ISerializable param1) {
        this(dataReq, param1, null);
    }

    public PacketReqData(Message dataReq, ISerializable param1, ISerializable param2) {
        this.dataReq = dataReq;
        this.param1 = param1;
        this.param2 = param2;
    }

    @Override
    public void encode(ByteBuf output) {
        output.writeInt(dataReq.ordinal());

        if (param1 != null) {
            output.writeBoolean(true);
            output.writeInt(DataType.getForClass(param1.getClass()).ordinal());
            param1.writeToStream(output);
        } else {
            output.writeBoolean(false);
        }

        if (param2 != null) {
            output.writeBoolean(true);
            output.writeInt(DataType.getForClass(param2.getClass()).ordinal());
            param2.writeToStream(output);
        } else {
            output.writeBoolean(false);
        }
    }

    @Override
    public void decode(ByteBuf input) {
        dataReq = Message.values()[input.readInt()];

        if (input.readBoolean()) {
            Class datatype = DataType.getForOrdinal(input.readInt());
            param1 = dataRead(datatype, input);
        }

        if (input.readBoolean()) {
            Class datatype = DataType.getForOrdinal(input.readInt());
            param2 = dataRead(datatype, input);
        }

    }

    @Override
    public void actionServer(World world, EntityPlayerMP player) {
        String logmsg = String.format("Received request %s from player %s ... ", dataReq, player.getGameProfile().getName());

        if (dataReq.canPlayerUseCommand(player)) {
            logmsg += "Accepted";
            ServerMessageHandler.instance().handle(dataReq, param1, param2, player);
        } else {
            logmsg += "Rejected";
        }
    }
}
