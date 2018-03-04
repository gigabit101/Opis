package mcp.mobius.opis.commands.client;

import mcp.mobius.opis.events.PlayerTracker;
import mcp.mobius.opis.network.PacketManager;
import mcp.mobius.opis.network.enums.Message;
import mcp.mobius.opis.network.packets.server.NetDataCommand;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

public class CommandOpis extends CommandBase {
	
	@Override
    public int getRequiredPermissionLevel(){
        return 0;
    }

	@Override
	public String getName() {
		return "opis";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (!(sender instanceof EntityPlayerMP)){
			sender.sendMessage(new TextComponentString("You are not a normal client and can't open the Swing interface."));
			return;
		}

		if (!Message.COMMAND_OPEN_SWING.canPlayerUseCommand((EntityPlayerMP)sender)){
			sender.sendMessage(new TextComponentString("Your access level prevents you from doing that."));
			return;
		}

		PlayerTracker.INSTANCE.playersSwing.add((EntityPlayerMP)sender);
		//((EntityPlayerMP)icommandsender).playerNetServerHandler.sendPacketToPlayer(NetDataCommand.create(Message.CLIENT_SHOW_SWING));
		if (sender instanceof EntityPlayerMP)
			PacketManager.validateAndSend(new NetDataCommand(Message.CLIENT_SHOW_SWING), (EntityPlayerMP)sender);
		PacketManager.sendFullUpdate((EntityPlayerMP)sender);
	}
}
