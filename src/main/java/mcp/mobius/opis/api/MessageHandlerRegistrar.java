package mcp.mobius.opis.api;

import mcp.mobius.opis.Opis;
import mcp.mobius.opis.network.PacketBase;
import mcp.mobius.opis.network.enums.Message;

import javax.swing.*;
import java.util.HashMap;
import java.util.HashSet;

public class MessageHandlerRegistrar {

    public static MessageHandlerRegistrar INSTANCE = new MessageHandlerRegistrar();

    private HashMap<Message, HashSet<IMessageHandler>> msgHandlers = new HashMap<>();

    public void registerHandler(Message msg, IMessageHandler handler) {
        if (!msgHandlers.containsKey(msg)) {
            msgHandlers.put(msg, new HashSet<>());
        }

        msgHandlers.get(msg).add(handler);
    }

    public void routeMessage(Message msg, PacketBase rawdata) {

        SwingUtilities.invokeLater(() -> {
            if (msgHandlers.containsKey(msg)) {
                for (IMessageHandler handler : msgHandlers.get(msg)) {
                    if (!handler.handleMessage(msg, rawdata)) {
                        Opis.log.warn(String.format("Unhandled msg %s in handler %s", msg, handler));
                    }
                }
            } else {
                Opis.log.warn(String.format("Unhandled msg : %s", msg));
            }
        });
    }
}
