package mcp.mobius.opis.commands.server;

import mcp.mobius.opis.commands.IOpisCommand;
import mcp.mobius.opis.data.managers.MetaManager;
import mcp.mobius.opis.events.PlayerTracker;
import mcp.mobius.opis.network.PacketManager;
import mcp.mobius.opis.network.enums.Message;
import mcp.mobius.opis.network.packets.server.NetDataCommand;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.util.text.TextComponentString;

public class CommandReset extends CommandBase implements IOpisCommand {

	@Override
	public String getCommandNameOpis() {
		return this.getName();
	}	


	@Override
    public int getRequiredPermissionLevel()
    {
        return 3;
    }

	@Override
	public String getDescription() {
		return "Cleans up all profiling data and remove client block overlay.";
	}

	@Override
	public String getName() {
		return "opis_reset";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

		MetaManager.reset();

		sender.sendMessage(new TextComponentString(String.format("\u00A7oInternal data reseted.")));

		//PacketDispatcher.sendPacketToAllPlayers(NetDataCommand.create(Message.CLIENT_CLEAR_SELECTION));
		if (sender instanceof EntityPlayerMP)
			PacketManager.validateAndSend(new NetDataCommand(Message.CLIENT_CLEAR_SELECTION), (EntityPlayerMP)sender);
	}

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		if (sender instanceof DedicatedServer) return true;
		//if ((sender instanceof EntityPlayerMP) && ((EntityPlayerMP)sender).playerNetServerHandler.netManager instanceof MemoryConnection) return true;
		if (!(sender instanceof DedicatedServer) && !(sender instanceof EntityPlayerMP)) return true;
		return PlayerTracker.INSTANCE.isPrivileged(((EntityPlayerMP)sender).getGameProfile().getName());
	}
}
