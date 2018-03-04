package mcp.mobius.opis.commands.client;

import mcp.mobius.opis.network.PacketManager;
import mcp.mobius.opis.network.enums.Message;
import mcp.mobius.opis.network.packets.server.NetDataCommand;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

public class CommandClientShowRenderTick extends CommandBase {

	@Override
    public int getRequiredPermissionLevel()
    {
        return 0;
    }

	@Override
	public String getName() {
		return "opis_ctick";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (sender instanceof EntityPlayerMP)
			PacketManager.validateAndSend(new NetDataCommand(Message.CLIENT_SHOW_RENDER_TICK), (EntityPlayerMP)sender);
	}
}
