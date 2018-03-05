package mcp.mobius.opis.swing.actions;

import mcp.mobius.opis.network.PacketManager;
import mcp.mobius.opis.network.enums.Message;
import mcp.mobius.opis.network.packets.client.PacketReqData;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ActionRunOpis implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        PacketManager.sendToServer(new PacketReqData(Message.COMMAND_START));
    }
}
