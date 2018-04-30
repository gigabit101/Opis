package mcp.mobius.opis.swing.widgets;

import mcp.mobius.opis.api.IMessageHandler;
import mcp.mobius.opis.network.PacketBase;
import mcp.mobius.opis.network.enums.Message;

import javax.swing.*;

public abstract class JPanelMsgHandler extends JPanel implements IMessageHandler {

    public JTableStats table;
    public Message cachedmsg;
    public PacketBase cachedrawdata;

    public JTableStats getTable() {
        return table;
    }

    public boolean cacheData(Message msg, PacketBase rawdata) {
        cachedmsg = msg;
        cachedrawdata = rawdata;
        return true;
    }

    public boolean refresh() {
        if (cachedmsg != null && cachedrawdata != null) {
            return handleMessage(cachedmsg, cachedrawdata);
        } else {
            return false;
        }
    }

}
