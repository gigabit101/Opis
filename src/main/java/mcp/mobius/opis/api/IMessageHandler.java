package mcp.mobius.opis.api;

import mcp.mobius.opis.network.PacketBase;
import mcp.mobius.opis.network.enums.Message;

public interface IMessageHandler {

    boolean handleMessage(Message msg, PacketBase rawdata);
}
