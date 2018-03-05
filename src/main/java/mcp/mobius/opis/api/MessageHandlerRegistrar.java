package mcp.mobius.opis.api;

import java.util.HashMap;
import java.util.HashSet;

import mcp.mobius.opis.Opis;
import mcp.mobius.opis.network.PacketBase;
import mcp.mobius.opis.network.enums.Message;

public class MessageHandlerRegistrar {
	public static MessageHandlerRegistrar INSTANCE = new MessageHandlerRegistrar();
	
	private HashMap<Message, HashSet<IMessageHandler>> msgHandlers = new HashMap<Message, HashSet<IMessageHandler>>();
	
	public void registerHandler(Message msg, IMessageHandler handler){
		if (!msgHandlers.containsKey(msg))
			msgHandlers.put(msg, new HashSet<IMessageHandler>());
		
		msgHandlers.get(msg).add(handler);
	}
	
	public void routeMessage(Message msg, PacketBase rawdata){
		
		if (msgHandlers.containsKey(msg)){
			for (IMessageHandler handler : msgHandlers.get(msg)){
				if (!handler.handleMessage(msg, rawdata)){
					Opis.log.warn(String.format("Unhandled msg %s in handler %s", msg, handler));
				}
			}
		}
		else{
			Opis.log.warn(String.format("Unhandled msg : %s", msg));
		}
	}
}
